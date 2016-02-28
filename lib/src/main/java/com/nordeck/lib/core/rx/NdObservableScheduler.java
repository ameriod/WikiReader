package com.nordeck.lib.core.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public interface NdObservableScheduler {
    <T> Observable.Transformer<T, T> scheduleObservable();

    NdObservableScheduler SUBSCRIBE_IO_OBSERVE_ANDROID_MAIN = new NdObservableScheduler() {
        @Override
        public <T> Observable.Transformer<T, T> scheduleObservable() {
            return new Observable.Transformer<T, T>() {
                @Override
                public Observable<T> call(Observable<T> observable) {
                    return observable
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            };
        }
    };
}