package com.kingyon.elevator.uis.activities.plan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.IndustryEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.adapters.IndustryLeftAdapter;
import com.kingyon.elevator.uis.adapters.IndustryRightAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.GridSpacingItemDecoration;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseStateLoadingActivity;
import com.leo.afbaselibrary.utils.ScreenUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/3.
 * Email：lc824767150@163.com
 */

public class IndustryActivity extends BaseStateLoadingActivity {

    @BindView(R.id.rv_left)
    RecyclerView rvLeft;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rv_right)
    RecyclerView rvRight;
    @BindView(R.id.pre_v_right)
    TextView preVRight;

    private IndustryEntity choosedIndustry;
    private long choosedCacheId;

    private IndustryLeftAdapter leftAdapter;
    private IndustryRightAdapter rightAdapter;

    @Override
    protected String getTitleText() {
        return "选择行业";
    }

    @Override
    public int getContentViewId() {
        choosedIndustry = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        choosedCacheId = choosedIndustry != null ? choosedIndustry.getObjectId() : 0;
        return R.layout.activity_industry;
    }

    private BaseAdapterWithHF.OnItemClickListener<IndustryEntity> leftItemClickListener = new BaseAdapterWithHF.OnItemClickListener<IndustryEntity>() {
        @Override
        public void onItemClick(View view, int position, IndustryEntity entity, BaseAdapterWithHF<IndustryEntity> baseAdaper) {
            if (entity != null) {
                tvName.setText(entity.getName());
                List<IndustryEntity> leftDatas = baseAdaper.getDatas();
                for (IndustryEntity leftItem : leftDatas) {
                    leftItem.setChoosed(leftItem == entity);
                }
                baseAdaper.notifyDataSetChanged();

//                List<IndustryEntity> rightDatas = rightAdapter.getDatas();
//                for (IndustryEntity rightItem : rightDatas) {
//                    rightItem.setChoosed(false);
//                }
                rightAdapter.refreshDatas(entity.getChilds());
            }
        }
    };

    private BaseAdapterWithHF.OnItemClickListener<IndustryEntity> rightItemClickListener = new BaseAdapterWithHF.OnItemClickListener<IndustryEntity>() {
        @Override
        public void onItemClick(View view, int position, IndustryEntity entity, BaseAdapterWithHF<IndustryEntity> baseAdaper) {
            if (entity != null) {
                List<IndustryEntity> datas = baseAdaper.getDatas();
                for (IndustryEntity item : datas) {
                    item.setChoosed(item == entity);
                }
                baseAdaper.notifyDataSetChanged();
                onBackPressed();
            }
        }
    };

    @Override
    protected void initViews(Bundle savedInstanceState) {
        preVRight.setVisibility(View.GONE);
        leftAdapter = new IndustryLeftAdapter(this);
        rvLeft.setLayoutManager(new LinearLayoutManager(this));
        rvLeft.setAdapter(leftAdapter);
        rvRight.addItemDecoration(new GridSpacingItemDecoration(3, ScreenUtil.dp2px(10), true));
        rightAdapter = new IndustryRightAdapter(this);
        rvRight.setLayoutManager(new GridLayoutManager(this, 3));
        rvRight.setAdapter(rightAdapter);
        leftAdapter.setOnItemClickListener(leftItemClickListener);
        rightAdapter.setOnItemClickListener(rightItemClickListener);
    }

    @Override
    protected void loadData() {
        NetService.getInstance().getIndustrys()
                .compose(this.<List<IndustryEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<List<IndustryEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(List<IndustryEntity> industryEntities) {
                        if (industryEntities == null || industryEntities.size() == 0) {
                            loadingComplete(STATE_EMPTY);
                        } else {
                            int indexLeft = 0;
                            int indexRight = -1;
                            for (int i = 0; i < industryEntities.size(); i++) {
                                IndustryEntity industryGroup = industryEntities.get(i);
                                List<IndustryEntity> childs = industryGroup.getChilds();
                                if (childs != null) {
                                    for (int j = 0; j < childs.size(); j++) {
                                        IndustryEntity industryChild = childs.get(j);
                                        if (industryChild.getObjectId() == choosedCacheId) {
                                            indexLeft = i;
                                            indexRight = j;
                                            break;
                                        }
                                    }
                                }
                                if (indexRight != -1) {
                                    break;
                                }
                            }
                            choosedIndustry = null;
                            IndustryEntity leftGroup = industryEntities.get(indexLeft);
                            List<IndustryEntity> childs = leftGroup.getChilds();
                            IndustryEntity rightChild = (childs != null && indexRight >= 0 && indexRight < childs.size()) ? childs.get(indexRight) : null;
                            leftGroup.setChoosed(true);
                            tvName.setText(leftGroup.getName());
                            if (rightChild != null) {
                                rightChild.setChoosed(true);
                            }
                            leftAdapter.refreshDatas(industryEntities);
                            rightAdapter.refreshDatas(childs);
                            loadingComplete(STATE_CONTENT);
//                            preVRight.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    @OnClick(R.id.pre_v_right)
    public void onViewClicked() {
        if (rightAdapter != null) {
            IndustryEntity choosed = null;
            List<IndustryEntity> datas = rightAdapter.getDatas();
            for (IndustryEntity item : datas) {
                if (item.isChoosed()) {
                    choosed = item;
                    break;
                }
            }
            if (choosed != null) {
                Intent intent = new Intent();
                intent.putExtra(CommonUtil.KEY_VALUE_1, choosed);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                showToast("需要选择一个行业");
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        IndustryEntity choosed = null;
        if (choosedIndustry != null) {
            choosed = choosedIndustry;
        } else {
            if (rightAdapter != null) {
                List<IndustryEntity> datas = rightAdapter.getDatas();
                for (IndustryEntity item : datas) {
                    if (item.isChoosed()) {
                        choosed = item;
                        break;
                    }
                }
            }
        }
        if (choosed != null) {
            intent.putExtra(CommonUtil.KEY_VALUE_1, choosed);
        }
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
