package com.kingyon.elevator.utils.utilstwo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.youth.banner.loader.ImageLoader;

/**
 * @Created By Admin  on 2020/5/26
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MyLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

//        Glide.with(context).load((String) path).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        GlideUtils.loadRoundCornersImage(context,(String)path,imageView,20);

    }

    public static String getStringFromDrawableRes(Context context, int id) {

        Resources resources = context.getResources();

         String path = ContentResolver.SCHEME_ANDROID_RESOURCE +"://"

          + resources.getResourcePackageName(id) +"/"

          + resources.getResourceTypeName(id) +"/"

          + resources.getResourceEntryName(id);

         return path;

    }


}



