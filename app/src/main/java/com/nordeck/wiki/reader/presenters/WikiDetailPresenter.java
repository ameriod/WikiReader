package com.nordeck.wiki.reader.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nordeck.wiki.reader.api.WikiDetailService;
import com.nordeck.wiki.reader.model.WikiDetail;
import com.nordeck.wiki.reader.ui.IWikiDetailView;

import in.nordeck.lib.base.presenter.BasePresenter;
import rx.Subscriber;
import timber.log.Timber;

/**
 * TODO make interface
 * Created by parker on 9/7/15.
 */
public class WikiDetailPresenter extends BasePresenter<IWikiDetailView> {

    private WikiDetail mDetail;
    private static final String OUT_STATE_WIKI_DETAIL = "out_state_wiki_detail";

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            mDetail = bundle.getParcelable(OUT_STATE_WIKI_DETAIL);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (mDetail != null) {
            bundle.putParcelable(OUT_STATE_WIKI_DETAIL, mDetail);
        }
    }

    public void fetchWikibyId(String id) {
        getView().showProgressIndicator(true);
        addToSubscriptions(new WikiDetailService().getWikiDetail(id).subscribe(new WikiDetailSubscriber()));
    }

    private class WikiDetailSubscriber extends Subscriber<WikiDetail> {
        @Override
        public void onCompleted() {
            Timber.d("onComplete");
        }

        @Override
        public void onError(Throwable e) {
            Timber.d(e, "onError");
            getView().showProgressIndicator(false);
            getView().displayErrorWikiDetail("Error loading Wiki");
        }

        @Override
        public void onNext(WikiDetail detail) {
            Timber.d("onNext");
            getView().showProgressIndicator(false);
            getView().onWikiDetailFetched(detail);
        }
    }
}
