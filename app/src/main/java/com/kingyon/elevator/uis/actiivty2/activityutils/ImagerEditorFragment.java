package com.kingyon.elevator.uis.actiivty2.activityutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.view.ColorBar;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.marvhong.videoeffect.helper.MagicFilterFactory;
import com.marvhong.videoeffect.helper.MagicFilterType;
import com.marvhong.videoeffect.utils.ConfigUtils;
import com.yalantis.ucrop.UCrop;
import com.zhaoss.weixinrecorded.activity.MyiconAdapter;
import com.zhaoss.weixinrecorded.util.FilterModel;
import com.zhaoss.weixinrecorded.view.TouchView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @Created By Admin  on 2020/7/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class ImagerEditorFragment extends BaseFragment {
    @BindView(R.id.img_image)
    ImageView imgImage;
    Unbinder unbinder;
    @BindView(R.id.ll_edit_filter)
    LinearLayout llEditFilter;
    @BindView(R.id.ll_edit_sticket)
    LinearLayout llEditSticket;
    @BindView(R.id.ll_edit_text)
    LinearLayout llEditText;
    @BindView(R.id.ll_edit_cut)
    LinearLayout llEditCut;
    @BindView(R.id.ll_edit_fuzzy)
    LinearLayout llEditFuzzy;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.rl_expression)
    RelativeLayout rlExpression;
    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.et_tag)
    EditText etTag;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.text_size)
    SeekBar textSize;
    @BindView(R.id.colorbar)
    ColorBar colorbar;
    @BindView(R.id.rl_edit_text)
    RelativeLayout rlEditText;
    @BindView(R.id.ll_effect_container)
    LinearLayout llEffectContainer;
    @BindView(R.id.hsv_effect)
    HorizontalScrollView hsvEffect;
    private String imgPath;

    /*功能*/
    public boolean isFilter = true;
    public boolean isSticket = true;
    public boolean isText = true;
    private List<FilterModel> mVideoEffects = new ArrayList<>(); //视频滤镜效果
    private MagicFilterType[] mMagicFilterTypes;
    private List<String> strlist = new ArrayList<>();
    private List<Integer> integers = new ArrayList<>();
    public ImagerEditorFragment setIndex(String imgPath) {
        this.imgPath = imgPath;
        return (this);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_imager_efitor;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        LogUtils.e(imgPath);
        GlideUtils.loadImage(getActivity(), imgPath, imgImage);
        initFfects();
        initExpression();
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

    @OnClick({R.id.ll_edit_filter, R.id.ll_edit_sticket, R.id.ll_edit_text,
            R.id.ll_edit_cut, R.id.ll_edit_fuzzy, R.id.ll_root, R.id.rl_expression,
            R.id.tv_close, R.id.tv_finish, R.id.tv_tag, R.id.tv_size, R.id.rl_edit_text,
            R.id.ll_effect_container, R.id.hsv_effect,R.id.img_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_image:
                initView();
                break;
            case R.id.ll_edit_filter:
                /*滤镜*/
                if (isFilter) {
                    isFilter = false;
                    hsvEffect.setVisibility(View.VISIBLE);
                }else {
                    isFilter = true;
                    hsvEffect.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_edit_sticket:
                /*贴图*/
                if (isSticket){
                    isSticket = false;
                    rlExpression.setVisibility(View.VISIBLE);
                }else {
                    isSticket = true;
                    rlExpression.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_edit_text:
                /*文字*/
                if (isText){
                    isText = false;
                    rlEditText.setVisibility(View.VISIBLE);
                }else {
                    isText = true;
                    rlEditText.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_edit_cut:
                /*剪切*/
                startCrop(getActivity(),Uri.fromFile(new File(imgPath)));
                break;
            case R.id.ll_edit_fuzzy:
                /*马赛克*/

                break;
            case R.id.ll_root:

                break;
            case R.id.rl_expression:

                break;
            case R.id.tv_close:

                break;
            case R.id.tv_finish:

                break;
            case R.id.tv_tag:

                break;
            case R.id.tv_size:

                break;
            case R.id.rl_edit_text:

                break;
            case R.id.ll_effect_container:

                break;
            case R.id.hsv_effect:

                break;
        }
    }

    private void initView() {
        isFilter = true;
        isSticket = true;
        isText = true;
        hsvEffect.setVisibility(View.GONE);
        rlExpression.setVisibility(View.GONE);
        rlEditText.setVisibility(View.GONE);
    }


    private void initFfects() {
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.expression1);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.expression2);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.expression3);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.expression4);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.expression5);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.expression6);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.expression7);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.expression8);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.expression9);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.expression10);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.expression11);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.expression12);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.expression13);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.expression14);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.s1);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.s2);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.s3);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.s4);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.s5);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.s6);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.s11);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.s7);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.s8);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.s9);
        integers.add(com.zhaoss.weixinrecorded.R.mipmap.s10);

        strlist.add("原图");
        strlist.add("胶片");
        strlist.add("怀旧");
        strlist.add("黑白");
        strlist.add("色温");
        strlist.add("重叠");
        strlist.add("模糊");
        strlist.add("噪点");
        strlist.add("对比度");
        strlist.add("伽马线");
        strlist.add("色度");
        strlist.add("交叉");
        strlist.add("灰度");
        strlist.add("染料");
        //滤镜效果集合
        mMagicFilterTypes = new MagicFilterType[]{
                MagicFilterType.NONE, MagicFilterType.INVERT,
                MagicFilterType.SEPIA, MagicFilterType.BLACKANDWHITE,
                MagicFilterType.TEMPERATURE, MagicFilterType.OVERLAY,
                MagicFilterType.BARRELBLUR, MagicFilterType.POSTERIZE,
                MagicFilterType.CONTRAST, MagicFilterType.GAMMA,
                MagicFilterType.HUE, MagicFilterType.CROSSPROCESS,
                MagicFilterType.GRAYSCALE, MagicFilterType.CGACOLORSPACE,
        };
        ConfigUtils.getInstance().setMagicFilterType(mMagicFilterTypes[0]);
        for (int i = 0; i < strlist.size(); i++) {
            FilterModel model = new FilterModel();
            model.setName(strlist.get(i));
            mVideoEffects.add(model);
        }
        addEffectView();

    }
    private void addEffectView() {
        llEffectContainer.removeAllViews();
        for (int i = 0; i < mVideoEffects.size(); i++) {
            View itemView = LayoutInflater.from(getActivity())
                    .inflate(com.zhaoss.weixinrecorded.R.layout.item_video_effect, llEffectContainer, false);
            TextView tv = itemView.findViewById(com.zhaoss.weixinrecorded.R.id.tv);
            ImageView iv = itemView.findViewById(com.zhaoss.weixinrecorded.R.id.iv);
            FilterModel model = mVideoEffects.get(i);
            int thumbId = MagicFilterFactory.filterType2Thumb(mMagicFilterTypes[i]);
            iv.setImageResource(thumbId);
            tv.setText(model.getName());
            final int index = i;
            itemView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < llEffectContainer.getChildCount(); j++) {
                        View tempItemView = llEffectContainer.getChildAt(j);
                        TextView tempTv = tempItemView.findViewById(com.zhaoss.weixinrecorded.R.id.tv);
                        FilterModel tempModel = mVideoEffects.get(j);
                        if (j == index) {
                            //选中的滤镜效果
                            if (!tempModel.isChecked()) {
                                tempTv.setTextColor(Color.parseColor("#f00000"));
//                                openEffectAnimation(tempTv, tempModel, true);
                                tempModel.setChecked(true);
                            }
                            ConfigUtils.getInstance().setMagicFilterType(mMagicFilterTypes[j]);
                            LogUtils.e(MagicFilterFactory.getFilter().getFragmentShader(), MagicFilterFactory.getFilter());
//                            mSurfaceView.setFilter(MagicFilterFactory.getFilter());
                        } else {
                            //未选中的滤镜效果
                            if (tempModel.isChecked()) {
                                tempTv.setTextColor(Color.parseColor("#000000"));
//                                openEffectAnimation(tempTv, tempModel, false);
                                tempModel.setChecked(false);
                            }
                        }
                    }
                }
            });
            llEffectContainer.addView(itemView);
        }

    }
    /**
     * 初始化表情
     */
    private void initExpression() {
        int dp80 = (int) getResources().getDimension(com.zhaoss.weixinrecorded.R.dimen.dp80);
        int dp10 = (int) getResources().getDimension(com.zhaoss.weixinrecorded.R.dimen.dp10);
        GridView gridView = new GridView(getActivity());
        gridView.setPadding(dp10, dp10, dp10, dp10);
        gridView.setNumColumns(4);
        gridView.setVerticalSpacing(15);
        gridView.setHorizontalSpacing(15);
        MyiconAdapter myiconAdapter = new MyiconAdapter(getActivity(), integers);
        gridView.setAdapter(myiconAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rlExpression.setVisibility(View.GONE);
//                iv_icon.setImageResource(R.mipmap.icon);
//                addExpressionToWindow(integers.get(position));
            }
        });
        rlExpression.addView(gridView);
    }

    /**
     * 添加表情到界面上
     */
    private void addExpressionToWindow(int result) {
//        TouchView touchView = new TouchView(this);
//        touchView.setBackgroundResource(result);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dp100, dp100);
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        touchView.setLayoutParams(layoutParams);
//
//        touchView.setLimitsX(0, windowWidth);
//        touchView.setLimitsY(0, windowHeight - dp100 / 2);
//        touchView.setOnLimitsListener(new TouchView.OnLimitsListener() {
//            @Override
//            public void OnOutLimits(float x, float y) {
//                tv_hint_delete.setTextColor(Color.RED);
//            }
//
//            @Override
//            public void OnInnerLimits(float x, float y) {
//                tv_hint_delete.setTextColor(Color.WHITE);
//            }
//        });
//        touchView.setOnTouchListener(new TouchView.OnTouchListener() {
//            @Override
//            public void onDown(TouchView view, MotionEvent event) {
//                tv_hint_delete.setVisibility(View.VISIBLE);
//                Log.e("TAG",event.toString());
//                changeMode(false);
//            }
//
//            @Override
//            public void onMove(TouchView view, MotionEvent event) {
//
//            }
//
//            @Override
//            public void onUp(TouchView view, MotionEvent event) {
//                tv_hint_delete.setVisibility(View.GONE);
//                changeMode(true);
//                if (view.isOutLimits()) {
//                    rl_touch_view.removeView(view);
//                }
//            }
//        });
//
//        rl_touch_view.addView(touchView);
    }

    public static void startCrop(Activity activity, Uri uri) {
        //裁剪后保存到文件中
        Uri sourceUri = uri;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String imageName = simpleDateFormat.format(date);
        Uri destinationUri = Uri.fromFile(new File(activity.getCacheDir(), imageName + ".jpeg"));
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(Color.parseColor("#F53051"));
        options.setStatusBarColor(Color.parseColor("#F53051"));
        options.setFreeStyleCropEnabled(true);
        UCrop.of(sourceUri, destinationUri).start(activity, UCrop.REQUEST_CROP);
    }
}
