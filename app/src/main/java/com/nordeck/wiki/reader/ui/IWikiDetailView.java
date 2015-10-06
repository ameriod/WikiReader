package com.nordeck.wiki.reader.ui;

import android.support.annotation.NonNull;

import com.nordeck.wiki.reader.model.WikiDetail;

import in.nordeck.lib.base.presenter.IView;

/**
 * Created by parker on 9/7/15.
 */
public interface IWikiDetailView extends IView {

    void onWikiDetailFetched(@NonNull WikiDetail detail);

    void displayErrorWikiDetail(@NonNull String error);
}
