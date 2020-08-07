package com.kingyon.elevator.utils.utilstwo;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

/**
 * @Created By Admin  on 2020/7/7
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MassageUtils {

    public static void httpDel(String robot_id,IsSuccess isSuccess){
        NetService.getInstance().removeRobot(robot_id)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        isSuccess.isSuccess(false);
                    }

                    @Override
                    public void onNext(String s) {
                        isSuccess.isSuccess(true);
                    }
                });
    }

    /*2.0删除@我的记录*/
    public static void httpremoveAtMe(String robot_id,IsSuccess isSuccess){
        NetService.getInstance().removeAtMe(robot_id)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        isSuccess.isSuccess(false);
                    }

                    @Override
                    public void onNext(String s) {
                        isSuccess.isSuccess(true);
                    }
                });
    }

    /*2.0删除评论点赞*/
    public static void httpremoveCommentLikes(String robot_id,IsSuccess isSuccess){
        NetService.getInstance().removeCommentLikes(robot_id)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        isSuccess.isSuccess(false);
                    }

                    @Override
                    public void onNext(String s) {
                        isSuccess.isSuccess(true);
                    }
                });
    }

    /*2.0删除评论我的记录*/

    public static void httpremoveCommentMe(String robot_id,IsSuccess isSuccess){
        NetService.getInstance().removeCommentMe(robot_id)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        isSuccess.isSuccess(false);
                    }

                    @Override
                    public void onNext(String s) {
                        isSuccess.isSuccess(true);
                    }
                });
    }

    /*2.删除内容点赞*/
    public static void httremoveContentLikes(String robot_id,IsSuccess isSuccess){
        NetService.getInstance().removeContentLikes(robot_id)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        isSuccess.isSuccess(false);
                    }

                    @Override
                    public void onNext(String s) {
                        isSuccess.isSuccess(true);
                    }
                });
    }

    /*2.删除关注消息*/
    public static void httpremoveLikeMsg(String robot_id,IsSuccess isSuccess){
        NetService.getInstance().removeLikeMsg(robot_id)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        isSuccess.isSuccess(false);
                    }

                    @Override
                    public void onNext(String s) {
                        isSuccess.isSuccess(true);
                    }
                });
    }

    /*2.删除我的消息*/
    public static void httpremoveMsg(String robot_id,IsSuccess isSuccess){
        LogUtils.e(robot_id);
        NetService.getInstance().removeMsg(robot_id)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        isSuccess.isSuccess(false);
                    }

                    @Override
                    public void onNext(String s) {
                        isSuccess.isSuccess(true);
                    }
                });
    }

    /*2.删除推送消息*/
    public static void httremovePushMsg(String robot_id,IsSuccess isSuccess){
        NetService.getInstance().removePushMsg(robot_id)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        isSuccess.isSuccess(false);
                    }

                    @Override
                    public void onNext(String s) {
                        isSuccess.isSuccess(true);
                    }
                });
    }

}
