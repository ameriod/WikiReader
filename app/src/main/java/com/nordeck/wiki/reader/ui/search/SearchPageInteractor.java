package com.nordeck.wiki.reader.ui.search;

import android.support.annotation.NonNull;

import com.nordeck.wiki.reader.SelectedWiki;
import com.nordeck.wiki.reader.api.SearchArticlesService;
import com.nordeck.wiki.reader.model.IPage;
import com.nordeck.wiki.reader.model.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by parker on 2/27/16.
 */
public class SearchPageInteractor implements SearchPageContract.Interactor {
    private SearchArticlesService service;


    public SearchPageInteractor() {
        this.service = new SearchArticlesService(SelectedWiki.getInstance().getSelectedWiki().getUrl());
    }

    @Override
    public Observable<List<IPage>> getSearchResults(@NonNull String query) {
        return service.getSearchResults(query).map(new Func1<SearchResponse, List<IPage>>() {
            @Override
            public List<IPage> call(SearchResponse searchResponse) {
                return new ArrayList<IPage>(searchResponse.getItems());
            }
        });
    }
}
