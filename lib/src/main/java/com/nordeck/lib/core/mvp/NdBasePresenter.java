package com.nordeck.lib.core.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nordeck.lib.core.rx.NdObservableScheduler;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;


public abstract class NdBasePresenter<V extends NdView> implements NdPresenter<V> {
    private V view;
    @Nullable
    private CompositeSubscription compositeSubscription;
    @NonNull
    private NdObservableScheduler scheduler;

    public NdBasePresenter(@NonNull NdObservableScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void bindView(@NonNull V view, @Nullable Bundle savedInstanceState) {
        Timber.d("%s bindView", getDebugTag());
        this.view = view;
    }

    @Override
    public void unbindView() {
        Timber.d("%s unbindView", getDebugTag());
        this.view = null;
        unsubscribe();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        Timber.d("%s onSaveInstanceState", getDebugTag());
    }

    protected void addToSubscriptions(@NonNull Subscription subscription) {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription(subscription);
        } else {
            compositeSubscription.add(subscription);
        }
    }

    protected void unsubscribe() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
    }

    @NonNull
    protected NdObservableScheduler getScheduler() {
        return scheduler;
    }

    private String getDebugTag() {
        return "Presenter flow for class: " + getClass().getSimpleName() + ";";
    }

    public V getView() {
        return view;
    }
}