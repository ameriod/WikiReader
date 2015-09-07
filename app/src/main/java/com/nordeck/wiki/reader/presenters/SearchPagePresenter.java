package com.nordeck.wiki.reader.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nordeck.wiki.reader.api.SearchArticlesService;
import com.nordeck.wiki.reader.model.SearchResponse;
import com.nordeck.wiki.reader.ui.ISearchPagesView;
import com.nordeck.wiki.reader.ui.ActivityTopPages;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by parker on 9/6/15.
 */
public class SearchPagePresenter extends NdBasePresenter<ISearchPagesView> {

    private String mSearchQuery;
    private SearchResponse mResponse;
    private String mBaseUrl;

    private static final String OUT_STATE_SEARCH_RESPONSE = "out_state_search_response";
    private static final String OUT_STATE_SEARCH_QUERY = "out_state_search_query";

    public String getSearchQuery() {
        return mSearchQuery;
    }

    public SearchResponse getResponse() {
        return mResponse;
    }

    public SearchPagePresenter(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            mSearchQuery = bundle.getString(OUT_STATE_SEARCH_QUERY);
            mResponse = bundle.getParcelable(OUT_STATE_SEARCH_RESPONSE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(OUT_STATE_SEARCH_QUERY, mSearchQuery);
        if (mResponse != null) {
            bundle.putParcelable(OUT_STATE_SEARCH_RESPONSE, mResponse);
        }
    }

    public void fetchSearchResults(@NonNull String query) {
        mSearchQuery = query;
        getView().showProgressIndicator(true);
        addToSubscriptions(new SearchArticlesService(mBaseUrl)
                .getSearchResults(query)
                .subscribe(new SearchSubscriber()));
    }

    private class SearchSubscriber extends Subscriber<SearchResponse> {
        @Override
        public void onCompleted() {
            Timber.d("onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Timber.d(e, "onError");
            getView().showProgressIndicator(false);
            getView().displayError("Error Searching.");
        }

        @Override
        public void onNext(SearchResponse response) {
            Timber.d("onNext");
            mResponse = response;
            getView().showProgressIndicator(false);
            getView().onResultsFetched(response);
        }
    }
}
