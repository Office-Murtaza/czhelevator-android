package com.kingyon.elevator.mvpbase;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.R;
import com.leo.afbaselibrary.widgets.StateLayout;

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
    public StateLayout stateLayout;
    protected ProgressDialog progressDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
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


    //如果是有刷新或者显示空内容的，则需要先初始化
    public void setStateLayout() {
        stateLayout = contentView.findViewById(R.id.stateLayout);
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
        ToastUtils.showShort(tipsContent);
    }

    @Override
    public void showLongToast(String tipsContent) {
        ToastUtils.showLong(tipsContent);
    }

    @Override
    public void showEmptyContentView() {
        if (stateLayout != null) {
            stateLayout.showEmptyView();
        }
    }

    @Override
    public void showEmptyContentView(String content) {
        if (stateLayout != null) {
            stateLayout.showEmptyView(content);
        }
    }


    @Override
    public void showErrorView() {
        if (stateLayout != null) {
            stateLayout.showErrorView();
        }
    }

    @Override
    public void showErrorView(String content) {
        if (stateLayout != null) {
            stateLayout.showErrorView(content);
        }
    }

    @Override
    public void showContentView() {
        if (stateLayout != null) {
            stateLayout.showContentView();
        }
    }

    @Override
    public void showProgressView() {
        if (stateLayout != null) {
            stateLayout.showProgressView();
        }
    }

    @Override
    public void showProgressView(String content) {
        if (stateLayout != null) {
            stateLayout.showProgressView(content);
        }
    }

    public void showProgressDialogView(String message, Boolean isCancel) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(isCancel);
        }
        progressDialog.setMessage(message != null ? message : "");
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void hideProgressDialogView() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    public void showProgressDialog(String message, Boolean isCancel) {
        showProgressDialogView(message, isCancel);
    }


    @Override
    public void hideProgressDailog() {
        hideProgressDialogView();
    }
}
