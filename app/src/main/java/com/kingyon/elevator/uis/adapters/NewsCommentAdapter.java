package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.customview.NonScrollableListView;
import com.kingyon.elevator.entities.CommentEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import com.kingyon.elevator.photopicker.MediaData;
import com.kingyon.elevator.utils.DensityUtil;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.nereo.multi_image_selector.bean.Image;
import me.nereo.multi_image_selector.utils.TimeUtils;

/**
 * Created By SongPeng  on 2019/12/13
 * Email : 1531603384@qq.com
 * 新闻评论适配器
 */
public class NewsCommentAdapter extends BaseAdapter {

    private List<CommentEntity> commentEntities;
    private LayoutInflater mInflater;
    Context context;
    private BaseOnItemClick<CommentEntity> baseOnItemClick;

    public NewsCommentAdapter(Context context, List<CommentEntity> commentEntities) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.commentEntities = commentEntities;
    }

    public void reflashData(List<CommentEntity> commentEntities) {
        this.commentEntities = commentEntities;
        notifyDataSetChanged();
    }

    public void setBaseOnItemClick(BaseOnItemClick<CommentEntity> baseOnItemClick) {
        this.baseOnItemClick = baseOnItemClick;
    }

    @Override
    public int getCount() {
        return commentEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return commentEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.news_comment_item_layout, null);
            holder.user_head = convertView.findViewById(R.id.user_head);
            holder.user_name = convertView.findViewById(R.id.user_name);
            holder.comment_content = convertView.findViewById(R.id.comment_content);
            holder.comment_time = convertView.findViewById(R.id.comment_time);
            holder.iv_dianzan = convertView.findViewById(R.id.iv_dianzan);
            holder.replay_count = convertView.findViewById(R.id.replay_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommentEntity commentEntity = commentEntities.get(position);
        try {
            GlideUtils.loadImage(context, commentEntity.getPhotoUrl(), holder.user_head);
            holder.user_name.setText(commentEntity.getNickname());
            holder.comment_time.setText(commentEntity.getShowTime());
            holder.comment_content.setText(commentEntity.getComment());
            if (commentEntity.isLike()) {
                holder.iv_dianzan.setImageResource(R.mipmap.details_shoucangtubiaoyi);
            } else {
                holder.iv_dianzan.setImageResource(R.mipmap.details_shoucangtubiaosan);
            }
            if (commentEntity.getComCount() > 0) {
                holder.replay_count.setText(commentEntity.getComCount() + "回复");
            } else {
                holder.replay_count.setText("回复");
            }
            holder.iv_dianzan.setOnClickListener(v -> {
                if (baseOnItemClick != null) {
                    baseOnItemClick.onItemClick(commentEntity, position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public final class ViewHolder {
        CircleImageView user_head;
        TextView user_name;
        TextView comment_content;
        TextView comment_time;
        ImageView iv_dianzan;
        TextView replay_count;
    }

}
