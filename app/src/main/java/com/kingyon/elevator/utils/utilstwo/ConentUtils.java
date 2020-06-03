package com.kingyon.elevator.utils.utilstwo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.AttentionAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.TopicSearchAdapter;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

import static com.czh.myversiontwo.utils.CodeType.ACCESS_IMAGE_PATH;
import static com.czh.myversiontwo.utils.CodeType.ACCESS_VOIDE_PATH;
import static com.czh.myversiontwo.utils.CodeType.LIKE;

/**
 * @Created By Admin  on 2020/5/13
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class ConentUtils {
    /**
     * 点赞取消点赞
     * */
    public static void httpHandlerLikeOrNot(BaseActivity baseActivity, int objId, String likeType, String handleType, int position,
                                            QueryRecommendEntity conent,String type){
        NetService.getInstance().setHandlerLikeOrNot(objId,likeType,handleType)
                .compose(baseActivity.bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.showToast(baseActivity,ex.getDisplayMessage(),1000);
                    }
                    @Override
                    public void onNext(String s) {
                        if (likeType.equals(LIKE)){
                            conent.liked = true;
                            conent.likes = conent.likes++;
                        }else {
                            conent.liked = false;
                            conent.likes = conent.likes--;
                        }
                    }
                });
    }


    /**
     * 内容举报
     * */

    public static  void httpReport(BaseActivity baseActivity,int objId, String reportType, String reportContent){
        LogUtils.e(objId,reportType,reportContent);
        NetService.getInstance().setReport(objId,reportType,reportContent)
                .compose(baseActivity.bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.showToast(baseActivity,ex.getDisplayMessage(),1000);
                    }

                    @Override
                    public void onNext(String s) {
                        ToastUtils.showToast(baseActivity,"举报成功",1000);
                    }
                });
    }


    /**
     * 内容删除
     * */
    public static void httpDelContent(BaseActivity baseActivity, int contentId, RecyclerView.Adapter attentionAdapter,
                                      String type, int position, List<QueryRecommendEntity> conentEntity, List<CommentListEntity> conentEntity1 ){
      LogUtils.e(contentId,type,position);
        NetService.getInstance().setDelContent(contentId)
                .compose(baseActivity.bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.showToast(baseActivity,ex.getDisplayMessage(),1000);
                    }

                    @Override
                    public void onNext(String s) {
                    ToastUtils.showToast(baseActivity,"删除成功",1000);
                    if (type.equals("1")){
                        conentEntity.remove(position);
                        attentionAdapter.notifyItemRemoved(position);
                        attentionAdapter.notifyItemRangeChanged(position,conentEntity.size()-position);

                    }else if (type.equals("3")){
                        conentEntity1.remove(position);
                        attentionAdapter.notifyItemRemoved(position);
                        attentionAdapter.notifyItemRangeChanged(position,conentEntity1.size()-position);

                    }else {
                        baseActivity.finish();

                    }

                    }
                });

    }

    /***
     * 评论删除
     * commentId 评论id
     * position 所在条数
     * */

    public static void httpDelcommen(BaseActivity baseActivity,int commentId,int position, RecyclerView.Adapter attentionAdapter, List<CommentListEntity> conentEntity1){
      LogUtils.e(commentId,position);
        NetService.getInstance().setDelcomment(commentId)
                .compose(baseActivity.bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.showToast(baseActivity,ex.getDisplayMessage(),1000);
                    }
                    @Override
                    public void onNext(String s) {
                        ToastUtils.showToast(baseActivity,"删除成功",1000);
                        conentEntity1.remove(position);
                        attentionAdapter.notifyItemRemoved(position);
                        attentionAdapter.notifyItemRangeChanged(position,conentEntity1.size()-position);
                    }
                });

    }

    /**
     * 内容评论
     * */

    public static void httpComment(BaseActivity baseActivity,int contentId,int parentId,String comment,IsSuccedListener isSuccedListener){
        LogUtils.e(contentId,comment,parentId);
        NetService.getInstance().setComment(contentId,parentId,comment)
                .compose(baseActivity.bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(),ex.getDisplayMessage());
                        isSuccedListener.onisSucced(false);
                    }

                    @Override
                    public void onNext(String s) {
                        isSuccedListener.onisSucced(true);
                    ToastUtils.showToast(baseActivity,"评论成功",1000);
                    }
                });
    }



    /**
     * 添加浏览数
     * */
    public static void httpAddBrowse(BaseActivity baseActivity,int contentId){
        NetService.getInstance().setAddBrowse(contentId)
                .compose(baseActivity.bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(),ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                    LogUtils.e("成功",s);
                    }
                });
    }

    public static void httpAddAttention(BaseActivity baseActivity,String handlerType,String beFollowerAccount,IsAddattention isAddattention){
        NetService.getInstance().setAddAttention(handlerType,beFollowerAccount)
                .compose(baseActivity.bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        isAddattention.onErron(ex.getDisplayMessage(),ex.getCode());
                    }

                    @Override
                    public void onNext(String s) {
                        isAddattention.onisSucced();
                    }
                });
    }



    public interface IsSuccedListener{
         void onisSucced(boolean isSucced) ;
    }

    public interface IsAddattention{
        void onisSucced();
        void onErron(String magssger,int code);
    }

/**
 * 选择图片
 * */

    public static  void startAction(Activity context) {
        Matisse.from(context)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .capture(false)
                .captureStrategy(
                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test"))
                .maxSelectable(6)
                .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .setOnSelectedListener((uriList, pathList) -> {
                    Log.e("onSelected", "onSelected: pathList=" + pathList);
                })
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(isChecked -> {
                    Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                })
                .forResult(ACCESS_IMAGE_PATH);

    }

    public static void startVideoAction(Activity activity) {
        Matisse.from(activity)
                .choose(MimeType.ofVideo1(), false)
                .countable(false)
                .capture(false)
                .captureStrategy(
                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test"))
                .maxSelectable(1)
                .gridExpectedSize(activity.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .setOnSelectedListener((uriList, pathList) -> {
                    Log.e("onSelected", "onSelected: pathList=" + pathList);
                })
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(isChecked -> {
                    Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                })
                .forResult(ACCESS_VOIDE_PATH);

    }


}
