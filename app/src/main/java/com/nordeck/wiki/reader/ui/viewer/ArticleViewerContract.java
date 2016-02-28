package com.nordeck.wiki.reader.ui.viewer;

import android.support.annotation.NonNull;

import com.nordeck.lib.core.mvp.NdPresenter;
import com.nordeck.lib.core.mvp.NdView;
import com.nordeck.wiki.reader.model.ArticleResponse;
import com.nordeck.wiki.reader.model.ISection;
import com.nordeck.wiki.reader.model.RelatedResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by parker on 2/27/16.
 */
public class ArticleViewerContract {

    public interface View extends NdView {

        void onArticleFetched(List<ISection> sections);

        void onRelatedArticlesFetched(@NonNull RelatedResponse relatedPages);
    }

    public interface Presenter extends NdPresenter<View> {
        void fetchRandomArticle(boolean forceLoad);

        void fetchArticle(@NonNull String id, boolean forceLoad);
    }

    public interface Interactor {
        Observable<ArticleResponse> fetchRandomArticle();

        Observable<List<ISection>> fetchArticle(@NonNull String id);

        Observable<RelatedResponse> fetchRelatedArticles(@NonNull String id);

    }
}
