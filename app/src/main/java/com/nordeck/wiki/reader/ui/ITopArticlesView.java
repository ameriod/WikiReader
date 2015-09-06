package com.nordeck.wiki.reader.ui;

import android.support.annotation.NonNull;

import com.nordeck.wiki.reader.model.PagesResponse;
import com.nordeck.wiki.reader.model.RelatedResponse;
import com.nordeck.wiki.reader.presenters.NdView;

/**
 * Created by parker on 9/4/15.
 */
public interface ITopArticlesView extends NdView {

    void onTopArticlesFetched(@NonNull PagesResponse response);
}
