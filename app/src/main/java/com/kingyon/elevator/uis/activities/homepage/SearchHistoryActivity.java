package com.kingyon.elevator.uis.activities.homepage;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.SearchHistoryEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.uis.fragments.homepage.HistoryFragment;
import com.kingyon.elevator.uis.fragments.homepage.SearchPoiFragment;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by GongLi on 2018/12/27.
 * Email：lc824767150@163.com
 */

public class SearchHistoryActivity extends BaseSwipeBackActivity {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.img_clear)
    ImageView imgClear;

    private HistoryFragment historyFragment;
    private SearchPoiFragment searchFragment;
    private AMapCityEntity cityEntity;
    private boolean fromSearch;

    @Override
    public int getContentViewId() {
        return R.layout.search_history;
    }

    @Override
    protected String getTitleText() {
        cityEntity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_2);
        fromSearch = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_3, false);
        return "搜索";
    }

    /**
     * 禁止输入空格进行检索
     */
    private InputFilter filter = (source, start, end, dest, dstart, dend) -> {
        if (source.equals(" ")) return "";
        else return null;
    };

    @Override
    protected void initViews(Bundle savedInstanceState) {
        etSearch.setFilters(new InputFilter[]{filter});
        RxTextView.textChanges(etSearch)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        tvSearch.setText(charSequence.length() > 0 ? "搜索" : "取消");
                        imgClear.setVisibility(TextUtils.isEmpty(charSequence) ? View.GONE : View.VISIBLE);
                    }
                })
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomApiCallback<CharSequence>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        Logger.e(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        updateFragment(String.valueOf(charSequence));
                    }
                });
        tvSearch.setText("取消");
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    KeyBoardUtils.closeKeybord(etSearch, SearchHistoryActivity.this);
//                    updateFragment(etSearch.getText().toString());
//                    return true;
//                }
//                return false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWord = etSearch.getText().toString();
                    if (!TextUtils.isEmpty(keyWord)) {
                        realSearch(keyWord,"0","0");
                        return true;
                    }
                }
                return false;
            }
        });
//        updateFragment(etSearch.getText().toString());
        String stringExtra = getIntent().getStringExtra(CommonUtil.KEY_VALUE_1);
        if (!TextUtils.isEmpty(stringExtra)) {
            updateFragment(etSearch.getText().toString());
            etSearch.setText(stringExtra);
            etSearch.setSelection(etSearch.getText().length());
        }
    }

    public void realSearch(String keyWord,String latitude,String longitude) {
        if (fromSearch) {
            Intent intent = new Intent();
            intent.putExtra(CommonUtil.KEY_VALUE_1, keyWord);
            intent.putExtra(CommonUtil.KEY_VALUE_2, latitude);
            intent.putExtra(CommonUtil.KEY_VALUE_3, longitude);
            setResult(RESULT_OK, intent);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(CommonUtil.KEY_VALUE_1, keyWord);
            bundle.putParcelable(CommonUtil.KEY_VALUE_2, cityEntity);
            bundle.putBoolean(CommonUtil.KEY_VALUE_3, false);
            startActivity(SearchActivity.class, bundle);
            updateHistory(keyWord);
        }
        finish();
    }

    @OnClick(R.id.tv_search)
    public void onSearchClicked() {
        if (TextUtils.equals("取消", tvSearch.getText())) {
            onBackPressed();
        } else {
            KeyBoardUtils.closeKeybord(etSearch, this);
            updateFragment(etSearch.getText().toString());
        }
    }

    @OnClick(R.id.img_clear)
    public void onClearClicked() {
        etSearch.setText("");
    }

    private void updateFragment(String s) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (historyFragment != null) {
            fragmentTransaction.hide(historyFragment);
        }
        if (searchFragment != null) {
            fragmentTransaction.hide(searchFragment);
        }
        if (TextUtils.isEmpty(s)) {
            if (historyFragment == null) {
                historyFragment = HistoryFragment.newInstance();
                fragmentTransaction.add(R.id.fl_content, historyFragment);
            } else {
                fragmentTransaction.show(historyFragment);
                historyFragment.onParamsChange(s);
            }
            if (searchFragment != null) {
                searchFragment.onParamsChange(s);
            }
        } else {
            if (searchFragment == null) {
                searchFragment = SearchPoiFragment.newInstance(s, cityEntity.getName());
                fragmentTransaction.add(R.id.fl_content, searchFragment);
            } else {
                fragmentTransaction.show(searchFragment);
                searchFragment.onParamsChange(s);
            }
            if (historyFragment != null) {
                historyFragment.onParamsChange(s);
            }
        }
        fragmentTransaction.commit();
    }

    public void onKeywordClick(String s) {
        if (!TextUtils.isEmpty(s)) {
            realSearch(s,"0","0");
        } else {
            if (!TextUtils.equals(s, etSearch.getText().toString())) {
                etSearch.setText(s);
                etSearch.setSelection(etSearch.getText().length());
                updateFragment(etSearch.getText().toString());
            }
        }
    }

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
}