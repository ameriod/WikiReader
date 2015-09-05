package com.nordeck.wiki.reader.ui;

import android.support.annotation.NonNull;

import com.nordeck.wiki.reader.model.TopArticlesResponse;
import com.nordeck.wiki.reader.presenters.NdView;

/**
 * Created by parker on 9/4/15.
 */
public interface ITopArticlesView extends NdView {

    void onTopArticlesFetched(@NonNull TopArticlesResponse response);
}
