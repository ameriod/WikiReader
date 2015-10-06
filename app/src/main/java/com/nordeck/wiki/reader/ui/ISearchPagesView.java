package com.nordeck.wiki.reader.ui;

import com.nordeck.wiki.reader.model.SearchResponse;

import in.nordeck.lib.base.presenter.IView;

/**
 * Created by parker on 9/6/15.
 */
public interface ISearchPagesView extends IView {

    void onResultsFetched(SearchResponse response);
}
