package com.kingyon.elevator.mvpbase;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.leo.afbaselibrary.utils.ToastUtils;

/**
 * Created by zeng on 2017/7/17.
 * Introduction:
 */

public abstract class MvpBaseFragment<P extends BasePresenter> extends Fragment implements BaseView {
    private View contentView;
    private Context context;
    protected P presenter;
    public Boolean isLoadData = false;//是否已经加载数据，懒加载控制
    private boolean currentVisibleState = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    public abstract P initPresenter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = LayoutInflater.from(getActivity()).inflate(getContentViewId(), container, false);
        presenter = initPresenter();
        presenter.attachView(this);
        initView(savedInstanceState);
        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.detachView();
        }
        contentView = null;
    }

    public View getContentView() {
        return contentView;
    }

    public Context getApplicationContext() {
        return context;
    }

    public abstract void initView(Bundle savedInstanceState);

    public abstract int getContentViewId();

    public View findViewById(int id) {
        if (contentView != null)
            return contentView.findViewById(id);
        return null;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showLoading(String tips) {

    }

    @Override
    public void showShortToast(String tipsContent) {
        ToastUtils.showToast(getContext(),tipsContent, Toast.LENGTH_SHORT);
    }

    @Override
    public void showLongToast(String tipsContent) {
        ToastUtils.showToast(getContext(),tipsContent, Toast.LENGTH_LONG);
    }

    @Override
    public void showEmptyContentView() {

    }

    @Override
    public void showEmptyContentView(String content) {

    }
}
