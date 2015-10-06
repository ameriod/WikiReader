package com.nordeck.wiki.reader.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nordeck.wiki.reader.api.ArticleService;
import com.nordeck.wiki.reader.api.RelatedArticleService;
import com.nordeck.wiki.reader.model.ArticleResponse;
import com.nordeck.wiki.reader.model.RelatedResponse;
import com.nordeck.wiki.reader.ui.IArticleViewerView;

import in.nordeck.lib.base.presenter.BasePresenter;
import rx.Subscriber;
import timber.log.Timber;

/**
 * TODO make interface
 * Created by parker on 9/4/15.
 */
public class ArticleViewerPresenter extends BasePresenter<IArticleViewerView> {

    private ArticleResponse mResponse;
    private RelatedResponse mRelatedResponse;
    private String mId;

    private static final String OUT_STATE_ARTICLE_RESPONSE = "out_state_article_response";
    private static final String OUT_STATE_RELATED_RESPONSE = "out_state_related_response";
    private static final String OUT_STATE_ARTICLE_ID = "out_state_article_id";


    private String mBaseUrl;

    public ArticleViewerPresenter(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            mResponse = bundle.getParcelable(OUT_STATE_ARTICLE_RESPONSE);
            mRelatedResponse = bundle.getParcelable(OUT_STATE_RELATED_RESPONSE);
            mId = bundle.getString(OUT_STATE_ARTICLE_ID);
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
        bundle.putString(OUT_STATE_ARTICLE_ID, mId);
    }

    public void fetchRandomArticle(boolean forceLoad) {
        if (TextUtils.isEmpty(mId)) {
            getView().showProgressIndicator(true);
            addToSubscriptions(new ArticleService(mBaseUrl)
                    .getRandomArticle()
                    .subscribe(new RandomSubscriber()));
        } else {
            fetchArticle(mId, forceLoad);
        }
    }

    public void fetchArticle(@NonNull String id, boolean forceLoad) {
        mId = id;
        if (forceLoad || mResponse == null || mResponse.getSections() == null || mResponse.getSections().size() == 0) {
            getView().showProgressIndicator(true);
            // Do not flat map the responses since the related articles seems to be optional?
            addToSubscriptions(new ArticleService(mBaseUrl)
                    .getArticle(id)
                    .subscribe(new ArticleSubscriber()));
            fetchRelatedArticles(id);
        } else {
            getView().onArticleFetched(mResponse);
            getView().onRelatedArticlesFetched(mRelatedResponse);
        }
    }

    private void fetchRelatedArticles(String id) {
        addToSubscriptions(new RelatedArticleService(mBaseUrl)
                .getRelatedPages(id)
                .subscribe(new RelatedSubscriber()));
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
            handleResponse(articleResponse);
        }
    }

    private class RelatedSubscriber extends Subscriber<RelatedResponse> {
        @Override
        public void onCompleted() {
            Timber.d("onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Timber.d(e, "onError");
        }

        @Override
        public void onNext(RelatedResponse relatedRelatedResponse) {
            Timber.d("onNext");
            mRelatedResponse = relatedRelatedResponse;
            getView().onRelatedArticlesFetched(relatedRelatedResponse);
        }
    }

    private class RandomSubscriber extends Subscriber<ArticleResponse> {
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
            mId = articleResponse.getId();
            handleResponse(articleResponse);
            // Now get the related articles
            fetchRelatedArticles(mId);

        }
    }

    private void handleResponse(ArticleResponse articleResponse) {
        mResponse = articleResponse;
        getView().showProgressIndicator(false);
        getView().onArticleFetched(articleResponse);
    }

}
