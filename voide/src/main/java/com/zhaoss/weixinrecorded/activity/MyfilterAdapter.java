package com.zhaoss.weixinrecorded.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhaoss.weixinrecorded.R;

import java.util.List;

/**
 * Created By Admin  on 2020/3/20
 * Email : 1531603384@qq.com
 */
class MyfilterAdapter extends RecyclerView.Adapter<MyfilterAdapter.ViewHolder> {
    Context context;
    List<Integer> integers;
    private int mposition = -1;
    private AdapterView.OnItemClickListener mOnItemClickLitener;

    public MyfilterAdapter(Context context, List<Integer> integers) {
        this.context = context;
        this.integers = integers;
    }
    public void setSelection( int position )
    {
        this.mposition = position;
        notifyDataSetChanged();
    }
    public void setOnItemClickLitener( AdapterView.OnItemClickListener mOnItemClickLitener )
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_filter,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.imageView.setImageResource(integers.get(position));

        if (mOnItemClickLitener != null) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(null,null,position,1);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return integers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
