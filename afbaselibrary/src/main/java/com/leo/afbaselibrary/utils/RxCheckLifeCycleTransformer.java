package com.leo.afbaselibrary.utils;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

/**
 * Created by zhengliao on 2017/4/10 0010.
 * Email:dantemustcry@126.com
 * 用于处理RxJava回调的生命周期，把Activity和Fragment的生命周期和{@link LifeCycleEvent}绑定
 */

public class RxCheckLifeCycleTransformer<T> implements Observable.Transformer<T, T> {
    BehaviorSubject<LifeCycleEvent> eventBehavior;

    public RxCheckLifeCycleTransformer(BehaviorSubject<LifeCycleEvent> eventBehavior) {
        if (eventBehavior == null) {
            throw new RuntimeException("eventBehavior can not be null");
        }
        this.eventBehavior = eventBehavior;
    }


    @Override
    public Observable<T> call(Observable<T> tObservable) {
        return tObservable.takeUntil(eventBehavior.skipWhile(new Func1<LifeCycleEvent, Boolean>() {
            @Override
            public Boolean call(LifeCycleEvent event) {
                return event != LifeCycleEvent.DESTROY && event != LifeCycleEvent.DETACH && event != LifeCycleEvent.DESTROY_VIEW;
            }
        }));
    }


    public enum LifeCycleEvent {
        // Activity Events
        CREATE,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY,

        // Fragment Events
        ATTACH,
        CREATE_VIEW,
        DESTROY_VIEW,
        DETACH
    }

}
