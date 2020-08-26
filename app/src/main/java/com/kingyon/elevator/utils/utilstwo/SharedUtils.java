package com.kingyon.elevator.utils.utilstwo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.czh.myversiontwo.utils.QuickClickUtils;
import com.google.android.exoplayer2.C;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.NewsSharedEntity;
import com.kingyon.elevator.uis.activities.advertising.AdEditActivity;
import com.kingyon.elevator.uis.fragments.main.PlanNewFragment;
import com.kingyon.elevator.videocrop.EditVideoActivity;
import com.kingyon.elevator.view.IsSrcSuccess;
import com.kingyon.library.social.BaseSharePramsProvider;
import com.kingyon.library.social.ShareDialog;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.muzhi.camerasdk.model.CameraSdkParameterInfo;
import com.umeng.commonsdk.debug.I;
import com.zhihu.matisse.utils.FFmpet;

import java.io.File;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.kingyon.elevator.photopicker.UtilsHelper.getString;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isToken;

/**
 * @Created By Admin  on 2020/5/13
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class SharedUtils {

    public static  void shared(Context context, ShareDialog shareDialog, String summary, String shareUrl, String title, boolean isshow ) {
        try {
            if (QuickClickUtils.isFastClick()) {
                NewsSharedEntity newsSharedEntity = new NewsSharedEntity();
                newsSharedEntity = new NewsSharedEntity();
                newsSharedEntity.setShareContent(summary);
                newsSharedEntity.setShareLink(shareUrl);
                newsSharedEntity.setShareTitle(title);
                if (shareDialog == null) {
                    BaseSharePramsProvider<NewsSharedEntity> baseSharePramsProvider = new BaseSharePramsProvider<>(context);
                    baseSharePramsProvider.setShareEntity(newsSharedEntity);
                    shareDialog = new ShareDialog(context, baseSharePramsProvider,isshow);
                    shareDialog.setDialoOncilk(new ShareDialog.Oncilk() {
                        @Override
                        public void setOncilk(String type) {
                            if (isToken(context)) {
                                if (type.equals("THOROEW")) {
                                    fileDown((BaseActivity) context, shareUrl, "adrhorew.mp4");
                                }
                            }else {
                                ActivityUtils.setLoginActivity();
                            }
                        }
                    });
                }
                shareDialog.show();
            }
        }catch (Exception e){
            ToastUtils.showToast(context,"分享没有准备好 请稍等再试",1000);
        }
    }



    public static  void fileDown(BaseActivity baseActivity, String urlVideo, String name) {
        baseActivity.showProgressDialog(getString(R.string.wait),false);
        DownloadManager.download(urlVideo, baseActivity.getExternalCacheDir() + File.separator + "/PDD/",
                name, new DownloadManager.OnDownloadListener() {
                    @Override
                    public void onSuccess(File file) {
                        baseActivity.hideProgress();
                        LogUtils.e(file.toString(),file);

                        Bundle bundle1 = new Bundle();
                        bundle1.putString("type", "thoroew");
                        bundle1.putString("thoroew", file.toString());
                        baseActivity.startActivity(PlanNewFragment.class, bundle1);

                    }
                    @Override
                    public void onProgress(int progress) {
                        LogUtils.e(progress);
                    }

                    @Override
                    public void onFail() {
                        baseActivity.hideProgress();
                        baseActivity.showToast("添加视频，请稍后重试");
                        LogUtils.e("onFail");
                    }
                });

    }
}
