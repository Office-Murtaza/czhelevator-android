package com.kingyon.elevator.utils.utilstwo;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.ConfirmPopWindow;

/**
 * @Created By Admin  on 2020/8/17
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class WebViewUtils {

    public static void loadWebImage(WebView webView, final Context mContext)
    {
        webView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                WebView.HitTestResult result = ((WebView) v).getHitTestResult();
                if (null == result)
                {
                    return false;
                }
                LogUtils.e(result.getExtra(),result.getType());

//                int type = result.getType();
//                if (type == WebView.HitTestResult.UNKNOWN_TYPE)
//                {
//                    return false;
//                }
//                if (type == WebView.HitTestResult.EDIT_TEXT_TYPE)
//                {
//                    //let TextViewhandles context menu return true;
//                    LogUtils.e(result.getExtra(),result.getType());
//                }
//                switch (type)
//                {
//                    case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
//                        String imgurl1 = result.getExtra();
//                        LogUtils.e(imgurl1);
//                        break;
//                    case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
//                        String imgurl2 = result.getExtra();
//                        LogUtils.e(imgurl2);
//                        break;
//                    case WebView.HitTestResult.GEO_TYPE: // TODO
//                        String imgurl3 = result.getExtra();
//                        LogUtils.e(imgurl3);
//                        break;
//                    case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
//                        // Log.d(DEG_TAG, "超链接");
//                        String imgurl4 = result.getExtra();
//                        LogUtils.e(imgurl4);
//                        break;
//                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
//                        String imgurl5 = result.getExtra();
//                        LogUtils.e(imgurl5);
//                        break;
//                    case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
//                        String imgurl = result.getExtra();
//                        LogUtils.e(imgurl);
//
////                        DialogUtil.showNoTipTwoBnttonDialog(mContext, mContext.getResources().getString(R.string.person_sure_to_save),
////                                mContext.getResources().getString(R.string.money_tranfer_cancel),
////                                mContext.getResources().getString(R.string.reset_password_sure),
////                                imgurl,
////                                NotiTag.TAG_MONEY_DIALOG_LEFT_BINDCARD, NotiTag.TAG_SAVE_WEB_IMAGE);
//                        break;
//                    default:
//                        String imgurl7 = result.getExtra();
//                        LogUtils.e(imgurl7);
//                        break;

//                }

                return true;
            }
        });
    }
}
