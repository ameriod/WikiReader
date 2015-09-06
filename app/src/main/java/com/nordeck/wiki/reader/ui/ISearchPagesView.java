package com.nordeck.wiki.reader.ui;

import com.nordeck.wiki.reader.model.SearchResponse;
import com.nordeck.wiki.reader.presenters.NdView;

/**
 * Created by parker on 9/6/15.
 */
public interface ISearchPagesView extends NdView {

    void onResultsFetched(SearchResponse response);
}
