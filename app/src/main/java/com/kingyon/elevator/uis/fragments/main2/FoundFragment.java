package com.kingyon.elevator.uis.fragments.main2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.AdNoticeWindowEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.fragments.main2.found.AttentionFragment;
import com.kingyon.elevator.uis.fragments.main2.found.RecommendFragment;
import com.kingyon.elevator.uis.fragments.main2.found.TopicFragment;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.ConfirmPopWindow;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.PublicFuncation;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.videocrop.EditVideoActivity;
import com.kingyon.elevator.view.ModifyTabLayout;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.zhihu.matisse.Matisse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.ACCESS_VOIDE_CODE;
import static com.czh.myversiontwo.utils.CodeType.ACCESS_VOIDE_PATH;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ADVERTISING;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_SEARCH;

/**
 * Created By Admin  on 2020/4/14
 * Email : 163235610@qq.com
 *
 * @Author:Mrczh Instructions:发现
 */
public class FoundFragment extends BaseFragment {


    @BindView(R.id.img_advertising)
    ImageView imgAdvertising;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.ll_tab)
    LinearLayout llTab;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.img_edit)
    ImageView imgEdit;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.modiftTabLayout)
    ModifyTabLayout tabLayout;
    @BindView(R.id.fl_title)
    LinearLayout flTitle;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_found;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setHeadViewPadding(getActivity(), flTitle);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        ConentUtils.topicStr = "";

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (DataSharedPreferences.getDialog()) {
                loadWindowAd();
            } else {
                if (PublicFuncation.isIntervalSixMin()) {
                    loadWindowAd();
                }
            }
        }
    }

    private void initView() {

        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new AttentionFragment(), "关注");
        adapter.addFrag(new RecommendFragment().setIndex(stateLayout), "推荐");
        adapter.addFrag(new TopicFragment(), "话题");
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(adapter.getCount());
        viewpagertab.setViewPager(viewPager);
        viewPager.setCurrentItem(1);

    }

    @Override
    protected void dealLeackCanary() {
    }

    /**
     * dp转换成px
     */
    public int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @OnClick({R.id.img_advertising, R.id.img_search, R.id.img_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_advertising:
                /*广告专区*/
                ActivityUtils.setActivity(ACTIVITY_MAIN2_ADVERTISING);
                break;
            case R.id.img_search:
                /*搜索*/
                ActivityUtils.setActivity(ACTIVITY_MAIN2_SEARCH);
                break;
            case R.id.img_edit:
                /*添加内容*/
                new ConfirmPopWindow(getActivity()).showAtBottom(imgEdit);
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && null != data) {
            switch (requestCode) {
                case ACCESS_VOIDE_PATH:
                    LogUtils.e(Matisse.obtainPathResult(data), Matisse.obtainResult(data), Matisse.obtainOriginalState(data));
                    Intent intent = new Intent(getActivity(), EditVideoActivity.class);
                    intent.putExtra("path", Matisse.obtainPathResult(data).get(0));
                    intent.putExtra("fromType", ACCESS_VOIDE_CODE);
                    startActivity(intent);
//                    Uri selectedVideo = data.getData();
//                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
//                    Cursor cursor = getActivity().getContentResolver().query(selectedVideo,
//                            filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    Intent intent = new Intent(getActivity(), EditVideoActivity.class);
//                    intent.putExtra("path",cursor.getString(columnIndex));
//                    intent.putExtra("fromType",ACCESS_VOIDE_CODE);
//                    startActivity(intent);
//                    cursor.close();
                    break;
            }
        }
    }

    /**
     * 加载弹窗通知
     */
    private void loadWindowAd() {
        NetService.getInstance().getTipsList("0")
                .subscribe(new CustomApiCallback<List<AdNoticeWindowEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e("弹窗广告加载失败：" + GsonUtils.toJson(ex));
                    }

                    @Override
                    public void onNext(List<AdNoticeWindowEntity> adNoticeWindowEntities) {
                        if (adNoticeWindowEntities != null && adNoticeWindowEntities.size() > 0) {
                            DataSharedPreferences.saveDialog(false);
                            LogUtils.e("弹窗广告数据：" + GsonUtils.toJson(adNoticeWindowEntities), adNoticeWindowEntities.toString());
                            AdNoticeWindowEntity adNoticeWindowEntity = PublicFuncation.getLastAdItem(adNoticeWindowEntities);
                            if (adNoticeWindowEntity != null) {
                                LogUtils.e(adNoticeWindowEntity.type, "********************");
                                if (adNoticeWindowEntity.type == 0) {
                                    //展示弹窗广告
                                    DialogUtils.getInstance().showMainWindowNoticeDialog(getActivity(), adNoticeWindowEntity);
                                } else if (adNoticeWindowEntity.type == 1) {
                                    DialogUtils.getInstance().showMainText(getActivity(), adNoticeWindowEntity);
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
