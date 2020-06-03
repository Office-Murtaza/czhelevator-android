package com.kingyon.elevator.uis.fragments.main2.found.utilsf;

import android.support.v4.app.Fragment;

import com.leo.afbaselibrary.utils.RxCheckLifeCycleTransformer;

import rx.subjects.BehaviorSubject;

public abstract class LazyFragment extends Fragment {

    protected boolean isVisible;
    protected BehaviorSubject<RxCheckLifeCycleTransformer.LifeCycleEvent> eventBehaviorSubject = BehaviorSubject.create();
    /**
     * 在这里实现Fragment数据的缓加载.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    protected void onVisible() {
        lazyLoad();
    }

    protected abstract void lazyLoad();

    protected void onInvisible() {
    }
}
