package com.kingyon.elevator.uis.fragments.main2.advertis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Created By Admin  on 2020/5/28
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CommercialFragment extends BaseFragment {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_content1)
    TextView tvContent1;
    @BindView(R.id.img_image)
    ImageView imgImage;
    Unbinder unbinder;
    private String string;
    private String type;

    public CommercialFragment setIndex(String string, String type) {
        this.string = string;
        this.type = type;
        return (this);
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_adv_style;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        switch (type){
            case "1":
                /*商业广告*/
                tvContent.setText("广告时长15秒，以轮流播放的方式投放，经济实惠。");
                tvContent1.setText("推荐投放尺寸——7（宽）: 11（高）\n视频大小≤15M，支持MP4格式\n图片大小≤5M，支持png、jpg格式");
                imgImage.setImageResource(R.mipmap.im_style_business_l);
                break;
            case "2":
                /*DAY广告*/
                tvContent.setText("广告时长60秒，以独占屏幕的方式投放，播放密度大，投放效果好。");
                tvContent1.setText("推荐投放尺寸——7（宽）: 11（高）\n视频大小≤15M，支持MP4格式\n图片大小≤5M，支持png、jpg格式");
                imgImage.setImageResource(R.mipmap.im_style_diy_l);
                break;
            case "3":
                /*便民信息*/
                tvContent.setText("免费发布文字类消息，简单实用。");
                tvContent1.setText("投放内容限制——不超过60字符");
                imgImage.setImageResource(R.mipmap.im_style_service_l);
                break;
                default:

        }

    }

    @Override
    protected void dealLeackCanary() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
