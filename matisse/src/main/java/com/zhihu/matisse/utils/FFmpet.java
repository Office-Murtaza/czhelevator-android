package com.zhihu.matisse.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.zhihu.matisse.R;

import java.util.ArrayList;

import VideoHandle.EpEditor;
import VideoHandle.OnEditorListener;

import static android.app.Activity.RESULT_OK;
import static com.zhihu.matisse.ui.MatisseActivity.EXTRA_RESULT_ORIGINAL_ENABLE;
import static com.zhihu.matisse.ui.MatisseActivity.EXTRA_RESULT_SELECTION_PATH;

/**
 * @Created By Admin  on 2020/8/10
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class FFmpet {
    /**
     * 视频压缩命令
     * 使用ffmpeg压制H.264视频
     * */
    public static String  compression(String INPUT, String OUTPUT){
//        return "-i "+INPUT+" -r 29.97 -vcodec libx264 -s 480x272 -flags +loop -cmp chroma -deblockalpha 0 -deblockbeta 0 -crf 24 -bt 256k -refs 1 -coder 0 -me umh -me_range 16 -subq 5 -partitions parti4x4+parti8x8+partp8x8 -g 250 -keyint_min 25 -level 30 -qmin 10 -qmax 51 -trellis 2 -sc_threshold 40 -i_qfactor 0.71 -acodec libfaac -ab 128k -ar 48000 -ac 2 "+OUTPUT;
    return "-i "+INPUT+" -c:v libx264 -x264-params profile=high:level=3.0 "+OUTPUT;
    }

    /**
     * 压缩视频方法
     * */
    private static ProgressDialog processDia;

    public static void GetCompression(String INPUT, String OUTPUT,IsonSuccess isonSuccess){
        Log.e("TAG",compression(INPUT, OUTPUT));

        EpEditor.execCmd(compression(INPUT, OUTPUT), 0, new OnEditorListener() {
            @Override
            public void onSuccess() {
                Log.e("TAG","==onSuccess="+OUTPUT);
                    isonSuccess.onSuccess(OUTPUT);

            }
            @Override
            public void onFailure() {
                Log.e("TAG","==onFailure=");
            }

            @Override
            public void onProgress(float progress) {
                Log.e("TAG",progress+"===");
            }
        });
    }

    public interface  IsonSuccess{
        void onSuccess(String outpath);
    }

    /**
     * 显示加载中对话框
     *
     * @param context
     */
    public static void showLoadingDialog(Context context,String message,boolean isCancelable) {
        if (processDia == null) {
            processDia= new ProgressDialog(context, R.style.dialog);
            //点击提示框外面是否取消提示框
            processDia.setCanceledOnTouchOutside(false);
            //点击返回键是否取消提示框
            processDia.setCancelable(isCancelable);
            processDia.setIndeterminate(true);
            processDia.setMessage(message);
            processDia.show();
        }
    }

    /**
     * 关闭加载对话框
     */
    public static void closeLoadingDialog() {
        if (processDia != null) {
            if (processDia.isShowing()) {
                processDia.cancel();
            }
            processDia = null;
        }
    }
}
