package com.nordeck.wiki.reader.ui.wiki;

import com.nordeck.wiki.reader.api.WikisService;
import com.nordeck.wiki.reader.model.WikiResponse;

import rx.Observable;

/**
 * Created by parker on 2/27/16.
 */
public class WikiInteractor implements WikiContract.Interactor {

    private WikisService service;

    public WikiInteractor() {
        this.service = new WikisService();
    }

    @Override
    public Observable<WikiResponse> preformWikiSearch(String query) {
        return service.getWikisByQuery(query);
    }

    @Override
    public Observable<WikiResponse> fetchTopWikis() {
        return service.getTopWikis();
    }

}
