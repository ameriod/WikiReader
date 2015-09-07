package com.nordeck.wiki.reader.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nordeck.wiki.reader.api.WikisService;
import com.nordeck.wiki.reader.model.WikiResponse;
import com.nordeck.wiki.reader.ui.IWikiView;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by parker on 9/6/15.
 */
public class WikiPresenter extends NdBasePresenter<IWikiView> {

    private String mSearchQuery;
    private WikiResponse mSearchResponse;
    private WikiResponse mTopResponse;

    private static final String OUT_STATE_SEARCH_QUERY = "out_state_search_query";
    private static final String OUT_STATE_SEARCH_RESPONSE = "out_state_search_response";
    private static final String OUT_STATE_TOP_RESPONSE = "out_state_top_response";

    public String getSearchQuery() {
        return mSearchQuery;
    }

    public WikiResponse getSearchResponse() {
        return mSearchResponse;
    }

    public WikiResponse getTopResponse() {
        return mTopResponse;
    }

    public void setSearchQuery(String searchQuery) {
        this.mSearchQuery = searchQuery;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            mSearchQuery = bundle.getString(OUT_STATE_SEARCH_QUERY);
            mSearchResponse = bundle.getParcelable(OUT_STATE_SEARCH_RESPONSE);
            mTopResponse = bundle.getParcelable(OUT_STATE_TOP_RESPONSE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(OUT_STATE_SEARCH_QUERY, mSearchQuery);
        if (mSearchResponse != null) {
            bundle.putParcelable(OUT_STATE_SEARCH_RESPONSE, mSearchResponse);
        }
        if (mTopResponse != null) {
            bundle.putParcelable(OUT_STATE_TOP_RESPONSE, mTopResponse);
        }
    }

    public void fetchTopWikis(boolean forceLoad) {
        if (forceLoad || mTopResponse == null || mTopResponse.getItems() == null || mTopResponse.getItems().size() ==
                0) {
            getView().showProgressIndicator(true);
            addToSubscriptions(new WikisService().getTopWikis().subscribe(new TopSubscriber()));
        } else {
            getView().onTopWikisFetched(mTopResponse);
        }
    }

    public void preformWikiSearch(@NonNull String query) {
        mSearchQuery = query;
        getView().showProgressIndicator(true);
        addToSubscriptions(new WikisService().getWikisByQuery(query).subscribe(new SearchSubscriber()));
    }


    private class SearchSubscriber extends Subscriber<WikiResponse> {
        @Override
        public void onCompleted() {
            Timber.d("onComplete");

        }

        @Override
        public void onError(Throwable e) {
            Timber.d(e, "onError");
            mSearchResponse = null;
            getView().showProgressIndicator(false);
            getView().displayError("Error No Results");

        }

        @Override
        public void onNext(WikiResponse response) {
            Timber.d("onNext");
            mSearchResponse = response;
            getView().showProgressIndicator(false);
            getView().onSearchWikisFetched(mSearchResponse);
        }
    }

    private class TopSubscriber extends Subscriber<WikiResponse> {
        @Override
        public void onCompleted() {
            Timber.d("onComplete");
        }

        @Override
        public void onError(Throwable e) {
            Timber.d(e, "onError");
            mTopResponse = null;
            getView().showProgressIndicator(false);
            getView().displayError("Error Fetching Top Wikis");
        }

        @Override
        public void onNext(WikiResponse response) {
            Timber.d("onComplete");
            mTopResponse = response;
            getView().showProgressIndicator(false);
            getView().onTopWikisFetched(response);
        }
    }
}
