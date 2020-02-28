package com.kingyon.elevator.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.entities.IncomeDetailsEntity;
import com.kingyon.elevator.entities.NewsEntity;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.view.NewsRecommendationView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By SongPeng  on 2020/2/26
 * Email : 1531603384@qq.com
 */
public class NewsRecommendationPresenter extends BasePresenter<NewsRecommendationView> {
    private int startPosition = 0;
    private int size = 20;
    private List<NewsEntity> newsEntityList;

    public NewsRecommendationPresenter(Context mContext) {
        super(mContext);
        newsEntityList = new ArrayList<>();
    }

    public List<NewsEntity> getNewsEntityList() {
        return newsEntityList;
    }

    public void loadNewsList(int reflashType, Integer newsType, String keyWords) {
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().getNewsList(startPosition, size, newsType, keyWords)
                .subscribe(new CustomApiCallback<List<NewsEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (startPosition == 0) {
                                getView().showErrorView();
                            }
                        }
                    }

                    @Override
                    public void onNext(List<NewsEntity> incomeDetailsEntities) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            if (reflashType == ReflashConstants.Refalshing) {
                                newsEntityList = incomeDetailsEntities;
                            } else {
                                newsEntityList.addAll(incomeDetailsEntities);
                                if (incomeDetailsEntities.size() == 0) {
                                    getView().loadMoreIsComplete();
                                }
                            }
                            startPosition = newsEntityList.size();
                            LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + incomeDetailsEntities.size());
                            getView().showListData(newsEntityList);
                        }
                    }
                });
    }
}
