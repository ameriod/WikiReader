package com.nordeck.wiki.reader.ui.top;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nordeck.lib.core.mvp.NdBasePresenter;
import com.nordeck.lib.core.rx.NdObservableScheduler;
import com.nordeck.wiki.reader.model.IPage;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by parker on 9/4/15.
 */
public class TopArticlesPresenter extends NdBasePresenter<TopArticlesContract.View> implements TopArticlesContract
        .Presenter {

    @Nullable
    private List<IPage> items;
    @NonNull
    private TopArticlesContract.Interactor interactor;

    private static final String OUT_STATE_ARTICLES = "out_state_articles";

    public TopArticlesPresenter(@NonNull NdObservableScheduler scheduler,
                                @NonNull TopArticlesContract.Interactor interactor) {
        super(scheduler);
        this.interactor = interactor;
    }

    public void fetchTopArticles(boolean forceLoad) {
        if (items != null && items.isEmpty() && !forceLoad) {
            getView().setItems(items);
        } else {
            getView().showProgressIndicator(true);
            addToSubscriptions(interactor.getTopArticles()
                    .compose(getScheduler().<List<IPage>>scheduleObservable())
                    .subscribe(new Subscriber<List<IPage>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, "onError");
                            getView().showProgressIndicator(false);
                            getView().displayError("Error Fetching Top Articles");
                        }

                        @Override
                        public void onNext(List<IPage> iPages) {
                            items = iPages;
                            getView().showProgressIndicator(false);
                            getView().setItems(items);
                        }
                    }));
        }
    }

    @Override
    public void bindView(@NonNull TopArticlesContract.View view, @Nullable Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        if (savedInstanceState != null) {
            items = savedInstanceState.getParcelable(OUT_STATE_ARTICLES);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (items != null) {
            bundle.putParcelableArrayList(OUT_STATE_ARTICLES, new ArrayList<>(items));
        }
    }
}
