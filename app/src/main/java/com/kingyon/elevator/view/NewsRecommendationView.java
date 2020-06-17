package com.kingyon.elevator.view;

import com.kingyon.elevator.entities.NewsEntity;
import com.kingyon.elevator.mvpbase.BaseView;

import java.util.List;

/**
 * Created By SongPeng  on 2020/2/26
 * Email : 1531603384@qq.com
 */
public interface NewsRecommendationView extends BaseView {

    void showListData(List<NewsEntity> incomeDetailsEntities);

    /**
     * 已经全部加载完成了
     */
    void  loadMoreIsComplete();
}
