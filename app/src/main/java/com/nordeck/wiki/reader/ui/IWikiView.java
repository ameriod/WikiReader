package com.nordeck.wiki.reader.ui;

import android.support.annotation.NonNull;

import com.nordeck.wiki.reader.model.WikiResponse;

import in.nordeck.lib.base.presenter.IView;

/**
 * Created by parker on 9/6/15.
 */
public interface IWikiView extends IView {

    void onTopWikisFetched(@NonNull WikiResponse response);

    void onSearchWikisFetched(@NonNull WikiResponse response);
}
