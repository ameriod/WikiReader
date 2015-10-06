package com.nordeck.wiki.reader.ui;

import android.support.annotation.NonNull;

import com.nordeck.wiki.reader.model.ArticleResponse;
import com.nordeck.wiki.reader.model.RelatedResponse;

import in.nordeck.lib.base.presenter.IView;

/**
 * Created by parker on 9/4/15.
 */
public interface IArticleViewerView extends IView {

    void onArticleFetched(@NonNull ArticleResponse article);

    void onRelatedArticlesFetched(@NonNull RelatedResponse relatedPages);
}
