package com.leo.afbaselibrary.uis.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leo.afbaselibrary.mvp.presenters.BasePresenter;
import com.leo.afbaselibrary.mvp.views.IBaseView;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.RxCheckLifeCycleTransformer;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subjects.BehaviorSubject;

/**
 * created by arvin on 16/10/24 15:02
 * email：1035407623@qq.com
 */
public abstract class BaseFragment extends Fragment implements IBaseView {
    protected View mRoot;
    protected Unbinder unbinder;
    protected BasePresenter mPresenter;
    protected BehaviorSubject<RxCheckLifeCycleTransformer.LifeCycleEvent> eventBehaviorSubject = BehaviorSubject.create();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = LayoutInflater.from(getActivity()).inflate(getContentViewId(), container, false);
        unbinder = ButterKnife.bind(this, mRoot);
        mPresenter = new BasePresenter(getActivity(), mRoot);
        eventBehaviorSubject.onNext(RxCheckLifeCycleTransformer.LifeCycleEvent.CREATE_VIEW);
        init(savedInstanceState);
        return mRoot;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        eventBehaviorSubject.onNext(RxCheckLifeCycleTransformer.LifeCycleEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public final <T extends View> T getView(int id) {
        return mPresenter.getView(id);
    }

    @Override
    public void showToast(String message) {
        mPresenter.showToast(message);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void startActivity(Class clazz) {
        mPresenter.startActivity(clazz, null);
    }

    @Override
    public void startActivity(Class clazz, Bundle bundle) {
        mPresenter.startActivity(clazz, bundle);
    }

    @Override
    public void startActivityForResult(Class clazz, int requestCode) {
        mPresenter.startActivityForResult(clazz, requestCode, null);
    }

    @Override
    public void startActivityForResult(Class clazz, int requestCode, Bundle bundle) {
        mPresenter.startActivityForResult(clazz, requestCode, bundle);
    }

    @Override
    public abstract int getContentViewId();

    @Override
    public abstract void init(Bundle savedInstanceState);

    @Override
    public void onResume() {
        super.onResume();
        eventBehaviorSubject.onNext(RxCheckLifeCycleTransformer.LifeCycleEvent.RESUME);
//        mobResume();
    }

    private void mobResume() {
        MobclickAgent.onPageStart(getClass().getSimpleName());//fragment页面统计
    }

    @Override
    public void onPause() {
        super.onPause();
//        mobPause();
    }

    private void mobPause() {
        MobclickAgent.onPageEnd(getClass().getSimpleName());//fragment页面统计
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBehaviorSubject.onNext(RxCheckLifeCycleTransformer.LifeCycleEvent.DESTROY);
        dealLeackCanary();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        eventBehaviorSubject.onNext(RxCheckLifeCycleTransformer.LifeCycleEvent.DETACH);
    }

    /**
     * 把Observable的生命周期与Activity绑定
     *
     * @param <D>
     * @return
     */
    protected <D> RxCheckLifeCycleTransformer<D> bindLifeCycle() {
        return new RxCheckLifeCycleTransformer<D>(eventBehaviorSubject);
    }


    protected abstract void dealLeackCanary();


    public void showProgressDialog(String message) {
        if (getActivity() instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) getActivity();
            baseActivity.showProgressDialog(message);
        }
    }

    public void hideProgress() {
        if (getActivity() instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) getActivity();
            baseActivity.hideProgress();
        }
    }
}
