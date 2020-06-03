package com.kingyon.elevator.uis.fragments.homepage;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.entities.SearchHistoryEntity;
import com.kingyon.elevator.others.OnParamsChangeInterface;
import com.kingyon.elevator.uis.activities.homepage.SearchHistoryActivity;
import com.kingyon.elevator.uis.widgets.CustomFlowLayout;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;

/**
 * Created by GongLi on 2018/12/27.
 * Email：lc824767150@163.com
 */

public class HistoryFragment extends BaseFragment implements OnParamsChangeInterface {

    @BindView(R.id.cfl_history)
    CustomFlowLayout cflHistory;
    @BindView(R.id.ll_history)
    LinearLayout llHistory;

    public static HistoryFragment newInstance() {
        Bundle args = new Bundle();
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_history;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        updateHistoryUi();
    }

    @Override
    public void onParamsChange(Object... objects) {
        String keyWord = (String) objects[0];
        if (!TextUtils.isEmpty(keyWord)) {
//            updateHistory(keyWord);
        } else {
            updateHistoryUi();
        }
    }

    private void updateHistoryUi() {
        llHistory.post(new Runnable() {
            @Override
            public void run() {
                List<SearchHistoryEntity> searchHistoryEntities = DataSupport
                        .where("ownerId = ?", String.valueOf(AppContent.getInstance().getMyUserId()))
                        .order("latestTime desc")
                        .limit(20)
                        .find(SearchHistoryEntity.class);
                if (searchHistoryEntities == null || searchHistoryEntities.size() < 1) {
                    llHistory.setVisibility(View.GONE);
                } else {
                    llHistory.setVisibility(View.VISIBLE);
                    showKeyword(cflHistory, searchHistoryEntities);
                }
            }
        });
    }

    private void showKeyword(CustomFlowLayout cfl, List contents) {
        cfl.removeAllViews();
        for (Object o : contents) {
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.history_item, cfl, false);
            String keyword = String.valueOf(o);
            textView.setText(keyword);
            textView.setTag(keyword);
            cfl.addView(textView);
            textView.setOnClickListener(onKeyWordClickListener);
        }
    }

    private View.OnClickListener onKeyWordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null && v.getTag() != null) {
                ((SearchHistoryActivity) getActivity()).onKeywordClick((String) v.getTag());
            }
        }
    };

    private void updateHistory(String keyWords) {
        List<SearchHistoryEntity> searchHistoryEntities = DataSupport.where("ownerId = ? and keyWord = ?"
                , String.valueOf(AppContent.getInstance().getMyUserId()), keyWords)
                .find(SearchHistoryEntity.class);
        if (searchHistoryEntities != null && searchHistoryEntities.size() > 0) {
            ContentValues values = new ContentValues();
            values.put("latestTime", System.currentTimeMillis());
            DataSupport.updateAll(SearchHistoryEntity.class, values, "ownerId = ? and keyWord = ?"
                    , String.valueOf(AppContent.getInstance().getMyUserId()), keyWords);
        } else {
            try {
                new SearchHistoryEntity(keyWords, System.currentTimeMillis(), AppContent.getInstance().getMyUserId()).saveThrows();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }


}
