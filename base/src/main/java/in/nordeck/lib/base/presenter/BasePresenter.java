package in.nordeck.lib.base.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * A base Presenter that handles the necessary methods for the {@link IPresenter}
 * interface.
 */
public abstract class BasePresenter<V extends IView> implements IPresenter<V> {
    private V view;
    private CompositeSubscription compositeSubscription;
    private Context context;

    @Override
    public void bindView(V view) {
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
    public void onDestroy() {
        Timber.d("%s onDestroy", getDebugTag());
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        Timber.d("%s onCreate", getDebugTag());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        Timber.d("%s onSaveInstanceState", getDebugTag());
    }

    protected Context getContext() {
        if (context == null) {
            if (view == null) {
                throw new IllegalStateException("view == null ; Tried to get context of a view that has not been set.");
            }
            context = view.getContext();
        }
        return context;
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

    private String getDebugTag() {
        return "Presenter flow for class: " + getClass().getSimpleName() + ";";
    }

    public V getView() {
        return view;
    }
}
