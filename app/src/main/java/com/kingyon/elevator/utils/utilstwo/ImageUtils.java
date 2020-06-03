package com.kingyon.elevator.utils.utilstwo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * @Created By Admin  on 2020/5/13
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class ImageUtils {

    public static boolean isSameImage(Context context, int drw, ImageView imageView){
        Drawable drawable = context.getResources().getDrawable(drw);
        // ImageView
                 imageView.setBackground(drawable);
        Drawable.ConstantState buttonConstantState = imageView.getBackground()
                .getConstantState();
        Drawable.ConstantState resourceConstantState = context.getResources().getDrawable(
                drw).getConstantState();
        boolean isEqual = buttonConstantState.equals(resourceConstantState);

        return isEqual;
    }
}
