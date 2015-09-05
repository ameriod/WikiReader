package com.nordeck.wiki.reader.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nordeck.wiki.reader.api.ArticleService;
import com.nordeck.wiki.reader.model.ArticleResponse;
import com.nordeck.wiki.reader.ui.IArticleViewerView;
import com.nordeck.wiki.reader.ui.TopActivity;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by parker on 9/4/15.
 */
public class ArticleViewerPresenter extends NdBasePresenter<IArticleViewerView> {

    private ArticleResponse mResponse;

    private static final String OUT_STATE_ARTICLE_RESPONSE = "out_state_article_response";


    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            mResponse = bundle.getParcelable(OUT_STATE_ARTICLE_RESPONSE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (mResponse != null) {
            bundle.putParcelable(OUT_STATE_ARTICLE_RESPONSE, mResponse);
        }
    }

    public void fetchArticle(@NonNull String id, boolean forceLoad) {
        if (forceLoad || mResponse == null || mResponse.getSections() == null || mResponse.getSections().size() == 0) {
            getView().showProgressIndicator(true);
            addToSubscriptions(new ArticleService(TopActivity.STAR_WARS_WIKI).getArticle(id)
                    .subscribe(new ArticleSubscriber()));
        } else {
            getView().onArticleFetched(mResponse);
        }
    }


    private class ArticleSubscriber extends Subscriber<ArticleResponse> {
        @Override
        public void onCompleted() {
            Timber.d("onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Timber.d("onError");
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
}
