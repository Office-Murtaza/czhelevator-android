package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.actiivty2.activityutils.ImagerEdit;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.zhaoss.weixinrecorded.util.Utils;
import com.zhaoss.weixinrecorded.view.TouchView;
import com.zhaoss.weixinrecorded.view.TuyaView;

import java.util.ArrayList;

/**
 * @Created By Admin  on 2020/7/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class ImageEditAdapter  extends RecyclerView.Adapter<ImageEditAdapter.ViewHolder> {

    Activity context;
    ArrayList<ImagerEdit> listImager;

    private int windowWidth;
    private int windowHeight;
    private int dp100;
    public ImageEditAdapter(Activity context) {
        this.context = context;
    }

    public void addData(ArrayList<ImagerEdit> listImager){
        this.listImager = listImager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_edit,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        windowWidth = Utils.getWindowWidth(context);
        windowHeight = Utils.getWindowHeight(context);
        dp100 = (int) context.getResources().getDimension(com.zhaoss.weixinrecorded.R.dimen.dp100);

        GlideUtils.loadImage(context,listImager.get(position).imgPath,holder.img_image);

        addExpressionToWindow(listImager.get(position).icon,holder.rl_touch_view);

    }

    @Override
    public int getItemCount() {
        return listImager.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_image ;
        RelativeLayout rl_touch_view;
        public ViewHolder(View itemView) {
            super(itemView);
            img_image = itemView.findViewById(R.id.img_image);
            rl_touch_view = itemView.findViewById(R.id.rl_touch_view);
        }
    }

    /**
     * 添加表情到界面上
     */
    private void addExpressionToWindow(int result, RelativeLayout rl_touch_view) {
        TouchView touchView = new TouchView(context);
        touchView.setBackgroundResource(result);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dp100, dp100);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        touchView.setLayoutParams(layoutParams);

        touchView.setLimitsX(0, windowWidth);
        touchView.setLimitsY(0, windowHeight - dp100 / 2);
        touchView.setOnLimitsListener(new TouchView.OnLimitsListener() {
            @Override
            public void OnOutLimits(float x, float y) {
//                tv_hint_delete.setTextColor(Color.RED);
            }

            @Override
            public void OnInnerLimits(float x, float y) {
//                tv_hint_delete.setTextColor(Color.WHITE);
            }
        });
        touchView.setOnTouchListener(new TouchView.OnTouchListener() {
            @Override
            public void onDown(TouchView view, MotionEvent event) {
//                tv_hint_delete.setVisibility(View.VISIBLE);
//                Log.e("TAG",event.toString());
//                changeMode(false);
            }

            @Override
            public void onMove(TouchView view, MotionEvent event) {

            }

            @Override
            public void onUp(TouchView view, MotionEvent event) {
//                tv_hint_delete.setVisibility(View.GONE);
//                changeMode(true);
                if (view.isOutLimits()) {
                    rl_touch_view.removeView(view);
                }
            }
        });

        rl_touch_view.addView(touchView);
    }

}
