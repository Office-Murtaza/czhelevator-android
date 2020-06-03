package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.customview.NonScrollableListView;
import com.kingyon.elevator.entities.CommentEntity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created By SongPeng  on 2019/12/13
 * Email : 1531603384@qq.com
 * 新闻评论的回复数据适配器
 */
public class NewsReplyAdapter extends BaseAdapter {

    private List<CommentEntity> commentEntities;
    private LayoutInflater mInflater;
    Context context;

    public NewsReplyAdapter(Context context, List<CommentEntity> commentEntities) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.commentEntities = commentEntities;
    }

    public void reflashData(List<CommentEntity> commentEntities) {
        this.commentEntities = commentEntities;
        notifyDataSetChanged();
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
            convertView = mInflater.inflate(R.layout.news_reply_item_layout, null);
            holder.user_head = convertView.findViewById(R.id.user_head);
            holder.user_name = convertView.findViewById(R.id.user_name);
            holder.reply_content = convertView.findViewById(R.id.reply_content);
            holder.comment_time = convertView.findViewById(R.id.comment_time);
            holder.iv_dianzan = convertView.findViewById(R.id.iv_dianzan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommentEntity commentEntity = commentEntities.get(position);
        return convertView;
    }

    public final class ViewHolder {
        CircleImageView user_head;
        TextView user_name;
        TextView reply_content;
        TextView comment_time;
        ImageView iv_dianzan;
    }

}
