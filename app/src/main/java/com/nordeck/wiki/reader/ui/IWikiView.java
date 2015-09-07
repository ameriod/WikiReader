package com.nordeck.wiki.reader.ui;

import android.support.annotation.NonNull;

import com.nordeck.wiki.reader.model.WikiResponse;
import com.nordeck.wiki.reader.presenters.NdView;

/**
 * Created by parker on 9/6/15.
 */
public interface IWikiView extends NdView {

    void onTopWikisFetched(@NonNull WikiResponse response);

    void onSearchWikisFetched(@NonNull WikiResponse response);
}
