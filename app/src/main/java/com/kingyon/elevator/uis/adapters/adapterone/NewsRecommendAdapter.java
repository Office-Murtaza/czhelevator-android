package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.customview.NonScrollableListView;
import com.kingyon.elevator.entities.CommentEntity;
import com.kingyon.elevator.entities.NewsEntity;
import com.kingyon.elevator.entities.NewsItemEntity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DensityUtil;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created By SongPeng  on 2019/12/13
 * Email : 1531603384@qq.com
 * 新闻推荐适配器
 */
public class NewsRecommendAdapter extends BaseAdapter {

    private List<NewsEntity> commentEntities;
    private LayoutInflater mInflater;
    Context context;

    public NewsRecommendAdapter(Context context, List<NewsEntity> commentEntities) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.commentEntities = commentEntities;
    }

    public void reflashData(List<NewsEntity> commentEntities) {
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
            convertView = mInflater.inflate(R.layout.news_item_layout, null);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(130));
            convertView.setLayoutParams(layoutParams);
            holder.news_img = convertView.findViewById(R.id.news_img);
            holder.news_title = convertView.findViewById(R.id.news_title);
            holder.news_content = convertView.findViewById(R.id.news_content);
            holder.news_time = convertView.findViewById(R.id.news_time);
            holder.news_img = convertView.findViewById(R.id.news_img);
            holder.read_count = convertView.findViewById(R.id.read_count);
            holder.type_name = convertView.findViewById(R.id.type_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NewsEntity newsEntity = commentEntities.get(position);
        try {
            GlideUtils.loadImage(context, newsEntity.getCoverUrl(), holder.news_img);
            holder.news_content.setText(newsEntity.getSummary());
            holder.news_title.setText(newsEntity.getTitle());
            holder.news_time.setText(newsEntity.getCreateTime());
            holder.type_name.setText(newsEntity.getCategory());
            if (newsEntity.getReadCount() >= 1000) {
                holder.read_count.setText("阅读量：" + CommonUtil.getMayOneFloat(newsEntity.getReadCount() / 1000)+"k");
            } else {
                holder.read_count.setText("阅读量：" + CommonUtil.getMayOneFloat(newsEntity.getReadCount()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public final class ViewHolder {
        CircleImageView news_img;
        TextView news_title;
        TextView news_content;
        TextView news_time;
        TextView read_count;
        TextView type_name;
    }

}
