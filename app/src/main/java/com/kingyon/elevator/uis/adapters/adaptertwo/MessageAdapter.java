package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
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
import com.kingyon.elevator.entities.entities.MassageLitsEntiy;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.kingyon.elevator.utils.utilstwo.MassageUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MESSAGE_PUSH;

/**
 * @Created By Admin  on 2020/4/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    BaseActivity context;
    List<MassageLitsEntiy> list;


    public MessageAdapter(BaseActivity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_massager, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MassageLitsEntiy massageLitsEntiy = list.get(position);
        GlideUtils.loadCircleImage(context, massageLitsEntiy.robotPhoto, holder.imgRobotphoto);
        holder.title.setText(massageLitsEntiy.robotName);
        holder.tvConent.setText(massageLitsEntiy.pushTitle);
        holder.tvTime.setText(massageLitsEntiy.pushTime);
        if (massageLitsEntiy.num<=0){
            holder.tvNumber.setVisibility(View.GONE);
        }else if (massageLitsEntiy.num>=10){
            holder.tvNumber.setText("···");
        }else {
            holder.tvNumber.setText(massageLitsEntiy.num + "");
        }
        holder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("robotId",massageLitsEntiy.robotId,"name",massageLitsEntiy.robotName);

                ActivityUtils.setActivity(ACTIVITY_MESSAGE_PUSH,"robotId",massageLitsEntiy.robotId,"name",massageLitsEntiy.robotName);
            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MassageUtils.httpDel(String.valueOf(massageLitsEntiy.robotId), new IsSuccess() {
                    @Override
                    public void isSuccess(boolean success) {
                        if (success){
                            context.showToast("删除成功");
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, list.size() - position);
                        }else {
                            context.showToast("删除失败");
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void addData(List<MassageLitsEntiy> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_robotphoto)
        ImageView imgRobotphoto;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.tv_conent)
        TextView tvConent;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.ll_content)
        LinearLayout llContent;
        @BindView(R.id.tv_delete)
        TextView tvDelete;
        @BindView(R.id.swipe)
        SwipeMenuLayout swipe;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
