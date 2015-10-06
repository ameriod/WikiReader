package com.nordeck.wiki.reader.ui;

import android.support.annotation.NonNull;

import com.nordeck.wiki.reader.model.PagesResponse;

import in.nordeck.lib.base.presenter.IView;

/**
 * Created by parker on 9/4/15.
 */
public interface ITopArticlesView extends IView {

    void onTopArticlesFetched(@NonNull PagesResponse response);
}
