package com.nordeck.wiki.reader.ui;

import android.support.annotation.NonNull;

import com.nordeck.wiki.reader.model.WikiDetail;
import com.nordeck.wiki.reader.presenters.NdView;

/**
 * Created by parker on 9/7/15.
 */
public interface IWikiDetailView extends NdView {

    void onWikiDetailFetched(@NonNull WikiDetail detail);

    void displayErrorWikiDetail(@NonNull String error);
}
