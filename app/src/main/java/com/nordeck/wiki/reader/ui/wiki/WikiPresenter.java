package com.nordeck.wiki.reader.ui.wiki;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nordeck.lib.core.mvp.NdBasePresenter;
import com.nordeck.lib.core.rx.NdObservableScheduler;
import com.nordeck.wiki.reader.model.WikiResponse;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by parker on 9/6/15.
 */
public class WikiPresenter extends NdBasePresenter<WikiContract.View> implements WikiContract.Presenter {

    private String searchQuery;
    private WikiResponse searchResponse;
    private WikiResponse topResponse;
    @NonNull
    private WikiContract.Interactor interactor;

    private static final String OUT_STATE_SEARCH_QUERY = "out_state_search_query";
    private static final String OUT_STATE_SEARCH_RESPONSE = "out_state_search_response";
    private static final String OUT_STATE_TOP_RESPONSE = "out_state_top_response";

    public WikiPresenter(@NonNull NdObservableScheduler scheduler, @NonNull WikiContract.Interactor interactor) {
        super(scheduler);
        this.interactor = interactor;
    }

    @Override
    public String getSearchQuery() {
        return searchQuery;
    }

    @Override
    public WikiResponse getSearchResponse() {
        return searchResponse;
    }

    @Override
    public WikiResponse getTopResponse() {
        return topResponse;
    }

    @Override
    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public void bindView(@NonNull WikiContract.View view, @Nullable Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(OUT_STATE_SEARCH_QUERY);
            searchResponse = savedInstanceState.getParcelable(OUT_STATE_SEARCH_RESPONSE);
            topResponse = savedInstanceState.getParcelable(OUT_STATE_TOP_RESPONSE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(OUT_STATE_SEARCH_QUERY, searchQuery);
        if (searchResponse != null) {
            bundle.putParcelable(OUT_STATE_SEARCH_RESPONSE, searchResponse);
        }
        if (topResponse != null) {
            bundle.putParcelable(OUT_STATE_TOP_RESPONSE, topResponse);
        }
    }

    @Override
    public void fetchTopWikis(boolean forceLoad) {
        if (forceLoad || topResponse == null || topResponse.getItems() == null || topResponse.getItems().isEmpty()) {
            getView().showProgressIndicator(true);
            addToSubscriptions(interactor.fetchTopWikis()
                    .compose(getScheduler().<WikiResponse>scheduleObservable())
                    .subscribe(new Subscriber<WikiResponse>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, "onError");
                            topResponse = null;
                            getView().showProgressIndicator(false);
                            getView().displayError("Error Fetching Top Wikis");
                        }

                        @Override
                        public void onNext(WikiResponse response) {
                            topResponse = response;
                            getView().showProgressIndicator(false);
                            getView().onTopWikisFetched(response);
                        }
                    }));
        } else {
            getView().onTopWikisFetched(topResponse);
        }
    }

    @Override
    public void preformWikiSearch(@NonNull String query) {
        searchQuery = query;
        getView().showProgressIndicator(true);
        addToSubscriptions(interactor.preformWikiSearch(query)
                .compose(getScheduler().<WikiResponse>scheduleObservable())
                .subscribe(new Subscriber<WikiResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "onError");
                        searchResponse = null;
                        getView().showProgressIndicator(false);
                        getView().displayError("Error No Results");

                    }

                    @Override
                    public void onNext(WikiResponse response) {
                        searchResponse = response;
                        getView().showProgressIndicator(false);
                        getView().onSearchWikisFetched(searchResponse);
                    }
                }));
    }
}
