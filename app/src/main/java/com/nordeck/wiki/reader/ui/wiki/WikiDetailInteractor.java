package com.nordeck.wiki.reader.ui.wiki;

import com.nordeck.wiki.reader.api.WikiDetailService;
import com.nordeck.wiki.reader.model.WikiDetail;

import rx.Observable;

/**
 * Created by parker on 2/27/16.
 */
public class WikiDetailInteractor implements WikiDetailContract.Interactor {
    private WikiDetailService service;

    public WikiDetailInteractor() {
        this.service = new WikiDetailService();
    }

    @Override
    public Observable<WikiDetail> getWiki(String id) {
        return service.getWikiDetail(id);
    }
}
