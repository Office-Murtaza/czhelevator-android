package com.kingyon.elevator.others;

import com.alibaba.fastjson.JSON;
import com.kingyon.elevator.others.auth.AuthUtills;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by GongLi on 2019/2/21.
 * Email：lc824767150@163.com
 * 用于电梯摄像头连接
 */

public class CameraWebSocketClient {
    private static final int DEFAULT_SOCKET_CONNECTTIMEOUT = 5000;
    private static final int DEFAULT_SOCKET_RECONNECTINTERVAL = 5000;
    private static final int FRAME_QUEUE_SIZE = 5;

    WebSocketFactory mWebSocketFactory;
    WebSocket mWebSocket;

    WebSocketListener mWebSocketListener;

    private ConnectStatus mConnectStatus = ConnectStatus.CONNECT_DISCONNECT;
    private Timer mReconnectTimer = new Timer();
    private TimerTask mReconnectTimerTask;

    private String mUri;

    public enum ConnectStatus {
        CONNECT_DISCONNECT,// 断开连接
        CONNECT_SUCCESS,//连接成功
        CONNECT_FAIL,//连接失败
        CONNECTING;//正在连接
    }

    public CameraWebSocketClient(String uri) {
        this(uri, DEFAULT_SOCKET_CONNECTTIMEOUT);
    }

    public CameraWebSocketClient(String uri, int timeout) {
        mUri = uri;
        mWebSocketFactory = new WebSocketFactory().setConnectionTimeout(timeout);
    }

    public void setWebSocketListener(WebSocketListener webSocketListener) {
        mWebSocketListener = webSocketListener;
    }

    public void connect() {
        try {
            mWebSocket = mWebSocketFactory.createSocket(mUri)
                    .setFrameQueueSize(FRAME_QUEUE_SIZE)//设置帧队列最大值为5
                    .setMissingCloseFrameAllowed(false)//设置不允许服务端关闭连接却未发送关闭帧
                    .addListener(new CameraWebSocketListener())
                    .connectAsynchronously();
            setConnectStatus(ConnectStatus.CONNECTING);
        } catch (IOException e) {
            e.printStackTrace();
            reconnect();
        }
    }

    public void sendLogin() throws UnsupportedEncodingException {
        String loginParams = String.format("{\"type\":\"login\",\"usertype\":2,\"appid\":\"1000\",\"authorization\":\"%s\"}", AuthUtills.getAuth());
//        mWebSocket.sendBinary(loginParams.getBytes("UTF-8"));
        mWebSocket.sendText(loginParams);
        Logger.i("CameraWebSocketClient sendLogin()\n%s", loginParams);
    }

    public void sendHeart() throws UnsupportedEncodingException {
        String heartParam = new String("{\"type\":\"ping\"}");
        mWebSocket.sendText(heartParam);
        Logger.i("CameraWebSocketClient sendHeart()\n%s", heartParam);
    }

    public void sendOpen(String cid) throws UnsupportedEncodingException {
        String openParam = String.format("{\"type\":\"monitor\",\"cid\":\"%s\"}", cid);
//        mWebSocket.sendBinary(openParam.getBytes("UTF-8"));
        mWebSocket.sendText(openParam);
        Logger.i("CameraWebSocketClient sendOpen()\n%s", openParam);
    }

    public void sendStop() throws UnsupportedEncodingException {
        String stopParam = new String("{\"type\":\"stopmonitor\"}");
//        mWebSocket.sendBinary(stopParam.getBytes("UTF-8"));
        mWebSocket.sendText(stopParam);
        Logger.i("CameraWebSocketClient sendStop()\n%s", stopParam);
    }

    private void setConnectStatus(ConnectStatus connectStatus) {
        mConnectStatus = connectStatus;
    }

    public ConnectStatus getConnectStatus() {
        return mConnectStatus;
    }

    public void disconnect() {
        if (mWebSocket != null) {
            mWebSocket.sendClose();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mWebSocket.disconnect();
            mWebSocketListener = null;
            mWebSocket = null;
        }
        setConnectStatus(null);
    }

    public interface WebSocketListener {
        void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception;

        void onTextMessage(WebSocket websocket, String text) throws Exception;

        void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception;

        void onConnectedError(WebSocket webSocket, WebSocketException exception);

        void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer);
    }

    class CameraWebSocketListener extends WebSocketAdapter {

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            super.onConnected(websocket, headers);
            Logger.i(String.format("OS. WebSocket onConnected\n%s", JSON.toJSONString(headers)));
            setConnectStatus(ConnectStatus.CONNECT_SUCCESS);
            if (mWebSocketListener != null) {
                mWebSocketListener.onConnected(websocket, headers);
            }
        }

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
            super.onConnectError(websocket, exception);
            Logger.e(String.format("OS. WebSocket onConnectError\n%s", exception.getMessage()));
            setConnectStatus(ConnectStatus.CONNECT_FAIL);
            if (mWebSocketListener != null) {
                mWebSocketListener.onConnectedError(websocket, exception);
            }
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
            Logger.w("OS. WebSocket onDisconnected");
            setConnectStatus(ConnectStatus.CONNECT_DISCONNECT);
            if (mWebSocketListener != null) {
                mWebSocketListener.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
            }
            reconnect();
        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            super.onTextMessage(websocket, text);
            Logger.i(String.format("OS. WebSocket onTextMessage\n%s", text));
            if (mWebSocketListener != null) {
                mWebSocketListener.onTextMessage(websocket, text);
            }
        }

        @Override
        public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
            super.onTextMessageError(websocket, cause, data);
            Logger.e("OS. WebSocket onTextMessageError");
        }

        @Override
        public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
            super.onBinaryMessage(websocket, binary);
            Logger.i("OS. WebSocket onBinaryMessage\n%s");
            if (mWebSocketListener != null) {
                mWebSocketListener.onBinaryMessage(websocket, binary);
            }
        }
    }

    public void reconnect() {
        if (mWebSocket != null && !mWebSocket.isOpen() && getConnectStatus() != ConnectStatus.CONNECTING) {
            mReconnectTimerTask = new TimerTask() {
                @Override
                public void run() {
                    connect();
                }
            };
            mReconnectTimer.schedule(mReconnectTimerTask, DEFAULT_SOCKET_RECONNECTINTERVAL);
        }
    }
}
