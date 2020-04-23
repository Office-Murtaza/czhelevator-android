package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.czh.myversiontwo.R;

import java.util.List;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_CONTENT_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VIDEO_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS;

/**
 * Created By Admin  on 2020/4/14
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */
public class AttentionAdapter extends RecyclerView.Adapter<AttentionAdapter.ViewHolder>{
    Context context;
    List<String> list;
    public AttentionAdapter(Context context, List<String> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_attention,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /**
         * 1文字
         * 2图片
         * 3视频
         * 4文字+图片
         * 5文字+视频
         * */

        switch (list.get(position)){
            case "1":
                holder.rv_conent_img.setVisibility(View.GONE);
                holder.tv_title.setVisibility(View.VISIBLE);
                holder.ll_xssjcs.setVisibility(View.GONE);
                break;
            case "2":
                holder.tv_title.setVisibility(View.GONE);
                holder.rv_conent_img.setVisibility(View.VISIBLE);
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.mipmap.btn_release);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY );
                holder.ll_conent_img.addView(imageView);
                holder.ll_xssjcs.setVisibility(View.GONE);
                break;
            case "3":
                holder.tv_title.setVisibility(View.GONE);
                holder.rv_conent_img.setVisibility(View.VISIBLE);
//                StandardGSYVideoPlayer video_view = new StandardGSYVideoPlayer(context);
//                video_view.setUp("http://cdn.tlwgz.com/FmR2tIv-8FwdXiSVNE9wQODoZk9E", true, "");
//                video_view.getBackButton().setVisibility(View.GONE);
//                video_view.getFullscreenButton().setVisibility(View.GONE);
//                video_view.startPlayLogic();
//                holder.ll_conent_img.addView(video_view);
                holder.ll_xssjcs.setVisibility(View.VISIBLE);
                ImageView imageView1 = new ImageView(context);
                imageView1.setImageResource(R.mipmap.bg_cooperation_auth);
                imageView1.setScaleType(ImageView.ScaleType.FIT_XY );
                holder.ll_conent_img.addView(imageView1);

                break;
            case "4":
                holder.tv_title.setVisibility(View.VISIBLE);
                holder.rv_conent_img.setVisibility(View.VISIBLE);
                RecyclerView recyclerView = new RecyclerView(context);
                ImagAdapter imagAdapter = new ImagAdapter(context,2);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false);
                gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(imagAdapter);
                holder.ll_conent_img.addView(recyclerView);
                holder.ll_xssjcs.setVisibility(View.GONE);
                break;
            case "5":
                holder.tv_title.setVisibility(View.VISIBLE);
                holder.rv_conent_img.setVisibility(View.VISIBLE);
                ImageView imageView2 = new ImageView(context);
                imageView2.setImageResource(R.mipmap.bg_cooperation_auth);
                imageView2.setScaleType(ImageView.ScaleType.FIT_XY );
                holder.ll_conent_img.addView(imageView2);
                holder.ll_xssjcs.setVisibility(View.VISIBLE);
                break;

        }

        holder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ACTIVITY_MAIN2_CONTENT_DRTAILS).navigation();
            }
        });
        holder.ll_conent_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ACTIVITY_MAIN2_VIDEO_DRTAILS).navigation();
            }
        });

        holder.img_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS).navigation();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_conent_img,ll_xssjcs;
        RelativeLayout rv_conent_img;
        TextView tv_title;
        ImageView img_tx;
        public ViewHolder(View itemView) {
            super(itemView);
            ll_conent_img = itemView.findViewById(R.id.ll_conent_img);
            tv_title = itemView.findViewById(R.id.tv_title);
            rv_conent_img = itemView.findViewById(R.id.rv_conent_img);
            ll_xssjcs = itemView.findViewById(R.id.ll_xssjcs);
            img_tx = itemView.findViewById(R.id.img_tx);
        }
    }
}
