package com.nordeck.wiki.reader.ui.search;

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
 * Created by parker on 9/6/15.
 */
public class SearchPagePresenter extends NdBasePresenter<SearchPageContract.View> implements
        SearchPageContract.Presenter {

    @Nullable
    private String searchQuery;
    @Nullable
    private List<IPage> pages;
    @NonNull
    private SearchPageContract.Interactor interactor;

    private static final String OUT_STATE_SEARCH_RESPONSE_PAGES = "out_state_search_response_pages";
    private static final String OUT_STATE_SEARCH_QUERY = "out_state_search_query";

    @Nullable
    public String getSearchQuery() {
        return searchQuery;
    }

    public SearchPagePresenter(@NonNull NdObservableScheduler scheduler,
                               @NonNull SearchPageContract.Interactor interactor) {
        super(scheduler);
        this.interactor = interactor;
    }


    @Override
    public void bindView(@NonNull SearchPageContract.View view, @Nullable Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(OUT_STATE_SEARCH_QUERY);
            pages = savedInstanceState.getParcelable(OUT_STATE_SEARCH_RESPONSE_PAGES);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(OUT_STATE_SEARCH_QUERY, searchQuery);
        if (pages != null) {
            bundle.putParcelableArrayList(OUT_STATE_SEARCH_RESPONSE_PAGES, new ArrayList<>(pages));
        }
    }

    @Override
    public void getSearchResults(@NonNull String query) {
        getView().showProgressIndicator(true);
        if (query.isEmpty()) {
            // Null everything out
            searchQuery = null;
            pages = null;
            getView().setItems(null);
            getView().showProgressIndicator(false);
        } else {
            if (searchQuery == null) {
                // never searched before
                search(query);
            } else if (searchQuery.equals(query) && pages != null && !pages.isEmpty()) {
                // restore the search
                getView().setItems(pages);
                getView().showProgressIndicator(false);
            } else {
                // new search
                search(query);
            }
        }
    }

    private void search(String query) {
        searchQuery = query;
        addToSubscriptions(interactor.getSearchResults(query)
                .compose(getScheduler().<List<IPage>>scheduleObservable())
                .subscribe(new Subscriber<List<IPage>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "onError");
                        getView().showProgressIndicator(false);
                        getView().displayError("Error Searching.");
                    }

                    @Override
                    public void onNext(List<IPage> items) {
                        pages = items;
                        getView().showProgressIndicator(false);
                        getView().setItems(pages);
                    }
                }));
    }
}
