package com.nordeck.wiki.reader.ui.top;

import com.nordeck.wiki.reader.SelectedWiki;
import com.nordeck.wiki.reader.api.TopArticlesService;
import com.nordeck.wiki.reader.model.IPage;
import com.nordeck.wiki.reader.model.PagesResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by parker on 2/27/16.
 */
public class TopArticlesInteractor implements TopArticlesContract.Interactor {

    private TopArticlesService service;

    public TopArticlesInteractor() {
        service = new TopArticlesService(SelectedWiki.getInstance().getSelectedWiki().getUrl());
    }

    @Override
    public Observable<List<IPage>> getTopArticles() {
        return service.getTopArticlesExpanded().map(new Func1<PagesResponse, List<IPage>>() {
            @Override
            public List<IPage> call(PagesResponse pagesResponse) {
                return new ArrayList<IPage>(pagesResponse.getItems());
            }
        });
    }
}
