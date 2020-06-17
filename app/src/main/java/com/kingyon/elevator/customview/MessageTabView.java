package com.kingyon.elevator.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By SongPeng  on 2019/11/27
 * Email : 1531603384@qq.com
 * 消息页顶部tab模块
 */
public class MessageTabView extends RelativeLayout {
    protected View contentView;
    protected Context mContext;
    @BindView(R.id.tab_icon)
    ImageView tab_icon;
    @BindView(R.id.tv_msg_num)
    TextView tv_msg_num;
    @BindView(R.id.tab_name)
    TextView tab_name;
    private int tabType=0;


    public MessageTabView(Context context) {
        this(context, null);
    }

    public MessageTabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MessageTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = getContext();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MessageTabView);
        tabType = array.getInt(R.styleable.MessageTabView_tabType, 0);
        array.recycle();
        initView();
    }


    private void initView() {
        contentView = RelativeLayout.inflate(mContext, R.layout.message_tab_item_view, this);
        ButterKnife.bind(this, contentView);
        tv_msg_num.setVisibility(GONE);
        if (tabType==0) {
            tab_icon.setImageResource(R.mipmap.tongzhi);
            tab_name.setText("通知");
        }else if (tabType==1) {
            tab_icon.setImageResource(R.mipmap.xiaozhushou);
            tab_name.setText("小助手");
        }else if (tabType==2) {
            tab_icon.setImageResource(R.mipmap.dianzan);
            tab_name.setText("点赞");
        }else if (tabType==3) {
            tab_icon.setImageResource(R.mipmap.pinglun);
            tab_name.setText("评论");
        }
    }


    public void showUnReadCount(int count){
        if (count>0&&count<=99){
            tv_msg_num.setVisibility(VISIBLE);
            tv_msg_num.setText(count+"");
        } else if(count<=0) {
            tv_msg_num.setVisibility(GONE);
        } else {
            tv_msg_num.setVisibility(VISIBLE);
            tv_msg_num.setText("99+");
        }
    }

    public void hideNums(){
        tv_msg_num.setVisibility(GONE);
    }


}
