package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.entities.AttenionUserEntiy;
import com.kingyon.elevator.uis.dialogs.NotAttentionDialog;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_CENTER;
import static com.zhaoss.weixinrecorded.util.UIUtils.getResources;

/**
 * @Created By Admin  on 2020/6/1
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MessageAttentionAdapter extends RecyclerView.Adapter<MessageAttentionAdapter.ViewHolder> {
    BaseActivity context;
    List<AttenionUserEntiy> list;



    public MessageAttentionAdapter(BaseActivity context) {
        this.context = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_message_atention, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttenionUserEntiy userEntiy = list.get(position);
        holder.tvName.setText("" + userEntiy.nickname);
        GlideUtils.loadCircleImage(context, userEntiy.photo, holder.imgPortrait);
        holder.tvConent.setText("" + userEntiy.personalizedSignature);
        if (userEntiy.isAttention == 1) {
            holder.tvAttention.setTextColor(Color.parseColor("#FF3049"));
            holder.tvAttention.setBackgroundResource(R.drawable.message_attention_bj);
            holder.tvAttention.setText("已关注");
        } else {
            holder.tvAttention.setTextColor(Color.parseColor("#ffffff"));
            holder.tvAttention.setBackgroundResource(R.drawable.message_attention_bj1);
            holder.tvAttention.setText("关注");
        }
        holder.llTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(userEntiy.followerAccount, userEntiy.beFollowerAccount,DataSharedPreferences.getCreatateAccount());
                ActivityUtils.setActivity(ACTIVITY_USER_CENTER, "type", "1","otherUserAccount",userEntiy.beFollowerAccount);
            }
        });

        holder.tvAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.tvAttention.getText().toString().equals("关注")) {
                    ConentUtils.httpAddAttention(context, "add", userEntiy.beFollowerAccount, new ConentUtils.IsAddattention() {
                        @Override
                        public void onisSucced() {
                            holder.tvAttention.setText("已关注");
                            holder.tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_cancel_attention));
                            holder.tvAttention.setTextColor(Color.parseColor("#FF1330"));
                        }

                        @Override
                        public void onErron(String magssger, int code) {
                            ToastUtils.showToast(context, magssger, 1000);
                        }
                    });
                } else {
                    NotAttentionDialog notAttentionDialog = new NotAttentionDialog(context, new NotAttentionDialog.OnClick() {
                        @Override
                        public void onclick() {
                            LogUtils.e("212121332");
                            ConentUtils.httpAddAttention(context, "cancel", userEntiy.beFollowerAccount, new ConentUtils.IsAddattention() {
                                @Override
                                public void onisSucced() {
                                    holder.tvAttention.setTextColor(Color.parseColor("#ffffff"));
                                    holder.tvAttention.setBackgroundResource(R.drawable.message_attention_bj1);
                                    holder.tvAttention.setText("关注");
                                }

                                @Override
                                public void onErron(String magssger, int code) {
                                    ToastUtils.showToast(context, magssger, 1000);
                                }
                            });
                        }
                    });
                    notAttentionDialog.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(List<AttenionUserEntiy> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_portrait)
        ImageView imgPortrait;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_conent)
        TextView tvConent;
        @BindView(R.id.tv_attention)
        TextView tvAttention;
        @BindView(R.id.ll_top)
        LinearLayout llTop;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
