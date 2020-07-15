package com.kingyon.elevator.utils.utilstwo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.BaseMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.entities.entities.MassageHomeEntiy;
import com.kingyon.elevator.entities.entities.MassageLitsEntiy;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.partnership.WithdrawalWayAdapter;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

import static com.czh.myversiontwo.utils.CodeType.ACCESS_IMAGE_PATH;
import static com.czh.myversiontwo.utils.CodeType.ACCESS_VOIDE_PATH;
import static com.czh.myversiontwo.utils.CodeType.LIKE;
import static com.kingyon.elevator.photopicker.UtilsHelper.getString;

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
//                            conent.liked = true;
                            conent.likes = conent.likes++;
                        }else {
//                            conent.liked = false;
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
        NetService.getInstance().setAddAttention(handlerType,beFollowerAccount,DataSharedPreferences.getCreatateAccount())
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


    /**2.0收藏点位及内容*/

    public  static void httpAddCollect(String objectId,String type,AddCollect addCollect){
        NetService.getInstance().setAddCollect(objectId,type)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        addCollect.Collect(false);
                    }

                    @Override
                    public void onNext(String s) {
                        addCollect.Collect(true);
                    }
                });
    }

    /**2.0取消收藏*/
    public  static void httpCancelCollect(String collectId,AddCollect addCollect){
        NetService.getInstance().setCancelCollect(collectId)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        addCollect.Collect(false);
                    }

                    @Override
                    public void onNext(String s) {
                        addCollect.Collect(true);
                    }
                });
    }

   public interface AddCollect{
        void Collect(boolean is);
    }

    /*修改用户资料*/
    public static void httpEidtProfile(BaseActivity baseActivity,String avatar,String nikeName,
                           String sex,String city,String birthday,
                           String intro,String cover,AddCollect addCollect){
        baseActivity.showProgressDialog(getString(R.string.wait));
        NetService.getInstance().userEidtProfile(avatar,nikeName,sex,city,birthday,intro,cover)
                .compose(baseActivity.<UserEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<UserEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(),ex.getDisplayMessage());
                        baseActivity.showToast(ex.getDisplayMessage());
                        baseActivity.hideProgress();
                        addCollect.Collect(false);
                    }

                    @Override
                    public void onNext(UserEntity userEntity) {
                        baseActivity.hideProgress();
                        addCollect.Collect(true);
                        if (userEntity != null) {
                            DataSharedPreferences.saveUserBean(userEntity);
                        }
                    }
                });

    }

    public static  void httpBin3Rd(String thirdId, String bindType,OnSuccess onSuccess ){
        NetService.getInstance().setBin3Rd(thirdId, bindType)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(),ex.getDisplayMessage());
                        onSuccess.onSuccess(false,ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        LogUtils.e("成功");
                        onSuccess.onSuccess(true,"绑定成功");

                    }
                });

    }

    public  interface OnSuccess{
        void onSuccess(boolean isSuccess,String massage);
    }
    /**
     * 替换LinkMovementMethod，这个不会触发TextView的滑动事件
     * 单例模式——饿汉
     */
    public static class CustomMovementMethod extends BaseMovementMethod {

        private static CustomMovementMethod customMovementMethod;

        public static CustomMovementMethod getInstance() {
            if (customMovementMethod == null) {
                synchronized (CustomMovementMethod .class) {
                    if (customMovementMethod == null) {
                        customMovementMethod = new CustomMovementMethod ();
                    }
                }
            }
            return customMovementMethod;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        //除了点击事件，我们不要其他东西
                        link[0].onClick(widget);
                    }
                    return true;
                }
            }
            return true;
        }

        private CustomMovementMethod () {

        }
    }

    /*指定标记全部已读*/
    public static void httpMarkAll(IsSuccess isSuccess){
        NetService.getInstance().markAll()
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(),ex.getCode());
                        isSuccess.isSuccess(false);
                    }

                    @Override
                    public void onNext(String s) {
                        isSuccess.isSuccess(true);
                    }
                });
    }

    /*指定标记已读*/
    public static void httpGetMarkRead(String id,String type,String isAll,IsSuccess isSuccess){
        NetService.getInstance().getMarkRead(id, type, isAll)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(),ex.getCode());
                        isSuccess.isSuccess(false);
                    }

                    @Override
                    public void onNext(String s) {
                        isSuccess.isSuccess(true);
                    }
                });
    }
    public  static int totalNum = 0;

    public static void httpHomeData(int page) {
        NetService.getInstance().getMsgOverview(page,20)
                .subscribe(new CustomApiCallback<MassageHomeEntiy<MassageLitsEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {

                    }
                    @Override
                    public void onNext(MassageHomeEntiy<MassageLitsEntiy> conentEntity) {
                        totalNum = conentEntity.totalNum;
                    }
                });
    }

}
