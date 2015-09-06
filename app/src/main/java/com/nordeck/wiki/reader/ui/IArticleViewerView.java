package com.nordeck.wiki.reader.ui;

import android.support.annotation.NonNull;

import com.nordeck.wiki.reader.model.ArticleResponse;
import com.nordeck.wiki.reader.model.RelatedPagesResponse;
import com.nordeck.wiki.reader.presenters.NdView;

/**
 * Created by parker on 9/4/15.
 */
public interface IArticleViewerView extends NdView {

    void onArticleFetched(@NonNull ArticleResponse article);

    void onRelatedArticlesFetched(@NonNull RelatedPagesResponse relatedPages);
}
