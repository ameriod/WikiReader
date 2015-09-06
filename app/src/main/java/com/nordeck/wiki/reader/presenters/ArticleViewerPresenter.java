package com.nordeck.wiki.reader.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nordeck.wiki.reader.api.ArticleService;
import com.nordeck.wiki.reader.api.RelatedArticleService;
import com.nordeck.wiki.reader.model.ArticleResponse;
import com.nordeck.wiki.reader.model.PagesResponse;
import com.nordeck.wiki.reader.ui.IArticleViewerView;
import com.nordeck.wiki.reader.ui.TopActivity;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by parker on 9/4/15.
 */
public class ArticleViewerPresenter extends NdBasePresenter<IArticleViewerView> {

    private ArticleResponse mResponse;
    private PagesResponse mRelatedResponse;

    private static final String OUT_STATE_ARTICLE_RESPONSE = "out_state_article_response";
    private static final String OUT_STATE_RELATED_RESPONSE = "out_state_related_response";


    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            mResponse = bundle.getParcelable(OUT_STATE_ARTICLE_RESPONSE);
            mRelatedResponse = bundle.getParcelable(OUT_STATE_RELATED_RESPONSE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (mResponse != null) {
            bundle.putParcelable(OUT_STATE_ARTICLE_RESPONSE, mResponse);
        }
        if (mRelatedResponse != null) {
            bundle.putParcelable(OUT_STATE_RELATED_RESPONSE, mRelatedResponse);
        }
    }

    public void fetchArticle(@NonNull String id, boolean forceLoad) {
        if (forceLoad || mResponse == null || mResponse.getSections() == null || mResponse.getSections().size() == 0) {
            getView().showProgressIndicator(true);
            // Do not flat map the responses since the related articles seems to be optional?
            addToSubscriptions(new ArticleService(TopActivity.TEST_WIKIA)
                    .getArticle(id)
                    .subscribe(new ArticleSubscriber()));
            addToSubscriptions(new RelatedArticleService(TopActivity.TEST_WIKIA)
                    .getRelatedPages(id)
                    .subscribe(new RelatedSubscriber()));
        } else {
            getView().onArticleFetched(mResponse);
            getView().onRelatedArticlesFetched(mRelatedResponse);
        }
    }


    private class ArticleSubscriber extends Subscriber<ArticleResponse> {
        @Override
        public void onCompleted() {
            Timber.d("onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Timber.d(e, "onError");
            getView().displayError("Error Fetching Article");
        }

        @Override
        public void onNext(ArticleResponse articleResponse) {
            Timber.d("onNext");
            mResponse = articleResponse;
            getView().showProgressIndicator(false);
            getView().onArticleFetched(articleResponse);
        }
    }

    private class RelatedSubscriber extends Subscriber<PagesResponse> {
        @Override
        public void onCompleted() {
            Timber.d("onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Timber.d(e, "onError");
        }

        @Override
        public void onNext(PagesResponse relatedPagesResponse) {
            Timber.d("onNext");
            mRelatedResponse = relatedPagesResponse;
            getView().onRelatedArticlesFetched(relatedPagesResponse);
        }
    }
}
