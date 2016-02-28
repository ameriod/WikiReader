package com.nordeck.wiki.reader.ui.wiki;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nordeck.lib.core.mvp.NdBasePresenter;
import com.nordeck.lib.core.rx.NdObservableScheduler;
import com.nordeck.wiki.reader.model.WikiDetail;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by parker on 9/7/15.
 */
public class WikiDetailPresenter extends NdBasePresenter<WikiDetailContract.View> implements WikiDetailContract
        .Presenter {

    private WikiDetail detail;
    private WikiDetailContract.Interactor interactor;
    private static final String OUT_STATE_WIKI_DETAIL = "out_state_wiki_detail";

    public WikiDetailPresenter(@NonNull NdObservableScheduler scheduler,
                               @NonNull WikiDetailContract.Interactor interactor) {
        super(scheduler);
        this.interactor = interactor;
    }

    @Override
    public void bindView(@NonNull WikiDetailContract.View view, @Nullable Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        if (savedInstanceState != null) {
            detail = savedInstanceState.getParcelable(OUT_STATE_WIKI_DETAIL);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (detail != null) {
            bundle.putParcelable(OUT_STATE_WIKI_DETAIL, detail);
        }
    }

    @Override
    public void fetchWikibyId(String id) {
        getView().showProgressIndicator(true);
        addToSubscriptions(interactor.getWiki(id)
                .compose(getScheduler().<WikiDetail>scheduleObservable())
                .subscribe(new Subscriber<WikiDetail>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "onError");
                        getView().showProgressIndicator(false);
                        getView().displayErrorWikiDetail("Error loading Wiki");
                    }

                    @Override
                    public void onNext(WikiDetail detail) {
                        getView().showProgressIndicator(false);
                        getView().onWikiDetailFetched(detail);
                    }
                }));
    }
}
