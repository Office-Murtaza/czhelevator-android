package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.NewsEntity;
import com.kingyon.elevator.entities.YesterdayIncomeEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.List;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 * 新闻数据适配器
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private List<NewsEntity> newsEntityList;
    private Context context;
    protected LayoutInflater inflater;
    private BaseOnItemClick<NewsEntity> baseOnItemClick;


    public NewsListAdapter(Context context, List<NewsEntity> newsEntityList) {
        this.context = context;
        this.newsEntityList = newsEntityList;
        this.inflater = LayoutInflater.from(context);
    }

    public void setBaseOnItemClick(BaseOnItemClick<NewsEntity> baseOnItemClick) {
        this.baseOnItemClick = baseOnItemClick;
    }

    public void reflashData(List<NewsEntity> newsEntityList) {
        this.newsEntityList = newsEntityList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.home_news_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            NewsEntity newsEntity = newsEntityList.get(position);
            holder.item_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    baseOnItemClick.onItemClick(newsEntity, position);
                }
            });
            holder.news_title.setText(newsEntity.getTitle());
            holder.type_name.setText(newsEntity.getCategory());
            GlideUtils.loadImage(context,newsEntity.getCoverUrl(),holder.news_img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return newsEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView news_img;
        TextView news_title;
        TextView type_name;
        CardView item_container;

        public ViewHolder(View itemView) {
            super(itemView);
            news_title = itemView.findViewById(R.id.news_title);
            news_img = itemView.findViewById(R.id.news_img);
            item_container = itemView.findViewById(R.id.item_container);
            type_name = itemView.findViewById(R.id.type_name);
        }
    }

}
