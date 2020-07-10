package com.kingyon.elevator.uis.actiivty2.activityutils;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.ocr.ui.camera.CameraView;
import com.blankj.utilcode.util.LogUtils;
import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.activities.devices.CameraBrandActivity;

import java.io.File;

import static com.cjt2325.cameralibrary.util.FileUtil.saveBitmap;

/**
 * @Created By Admin  on 2020/6/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CameraViewActivit extends AppCompatActivity {
    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码
    private JCameraView jCameraView;
    private boolean granted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
//设置只能录像或只能拍照或两种都可以（默认两种都可以）

        jCameraView = (JCameraView) findViewById(R.id.jcameraview);
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_CAPTURE);
        jCameraView.setTip("点击拍照");
        //设置视频保存路径
        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");

        //JCameraView监听
        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                String path = saveBitmap("PDD", bitmap);
                LogUtils.e("JCameraView", "bitmap = " + bitmap.getWidth(),path);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(101, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {

            }
        });
        //左边按钮点击事件
        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                jCameraView.setLeftClickListener(new ClickListener() {
                    @Override
                    public void onClick() {
//                        Intent intent7 = new Intent();
//                        intent7.putExtra("path", "");
//                        setResult(103, intent7);
//                        CameraViewActivit.this.finish();
                        finish();
                    }
                });
            }
        });
        getPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (granted) {
            jCameraView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    /**
     * 获取权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //具有权限
                granted = true;
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
                granted = false;
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    granted = true;
                    jCameraView.onResume();
                }else{
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
