package com.nordeck.wiki.reader.ui.viewer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nordeck.lib.core.mvp.NdBasePresenter;
import com.nordeck.lib.core.rx.NdObservableScheduler;
import com.nordeck.wiki.reader.model.ArticleResponse;
import com.nordeck.wiki.reader.model.ISection;
import com.nordeck.wiki.reader.model.RelatedResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by parker on 9/4/15.
 */
public class ArticleViewerPresenter extends NdBasePresenter<ArticleViewerContract.View> implements
        ArticleViewerContract.Presenter {

    private List<ISection> sections;
    private RelatedResponse mRelatedResponse;
    private String mId;
    @NonNull
    private ArticleViewerContract.Interactor interactor;

    private static final String OUT_STATE_ARTICLE_SECTIONS = "out_state_article_response";
    private static final String OUT_STATE_RELATED_RESPONSE = "out_state_related_response";
    private static final String OUT_STATE_ARTICLE_ID = "out_state_article_id";

    public ArticleViewerPresenter(@NonNull NdObservableScheduler scheduler,
                                  @NonNull ArticleViewerContract.Interactor interactor) {
        super(scheduler);
        this.interactor = interactor;
    }

    @Override
    public void bindView(@NonNull ArticleViewerContract.View view, @Nullable Bundle savedInstanceState) {
        super.bindView(view, savedInstanceState);
        if (savedInstanceState != null) {
            sections = savedInstanceState.getParcelableArrayList(OUT_STATE_ARTICLE_SECTIONS);
            mRelatedResponse = savedInstanceState.getParcelable(OUT_STATE_RELATED_RESPONSE);
            mId = savedInstanceState.getString(OUT_STATE_ARTICLE_ID);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (sections != null) {
            bundle.putParcelableArrayList(OUT_STATE_ARTICLE_SECTIONS, new ArrayList<>(sections));
        }
        if (mRelatedResponse != null) {
            bundle.putParcelable(OUT_STATE_RELATED_RESPONSE, mRelatedResponse);
        }
        bundle.putString(OUT_STATE_ARTICLE_ID, mId);
    }

    @Override
    public void fetchRandomArticle(boolean forceLoad) {
        if (TextUtils.isEmpty(mId)) {
            getView().showProgressIndicator(true);
            addToSubscriptions(interactor.fetchRandomArticle()
                    .compose(getScheduler().<ArticleResponse>scheduleObservable())
                    .subscribe(new Subscriber<ArticleResponse>() {
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, "onError");
                        }

                        @Override
                        public void onNext(ArticleResponse articleResponse) {
                            mId = articleResponse.getId();
                            handleResponse(new ArrayList<ISection>(articleResponse.getSections()));
                            // Now get the related articles
                            fetchRelatedArticles(mId);
                        }
                    }));
        } else {
            fetchArticle(mId, forceLoad);
        }
    }

    @Override
    public void fetchArticle(@NonNull String id, boolean forceLoad) {
        mId = id;
        if (forceLoad || sections == null || sections.isEmpty()) {
            getView().showProgressIndicator(true);
            addToSubscriptions(interactor.fetchArticle(id)
                    .compose(getScheduler().<List<ISection>>scheduleObservable())
                    .subscribe(new Subscriber<List<ISection>>() {

                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.d(e, "onError");
                            getView().displayError("Error Fetching Article");
                        }

                        @Override
                        public void onNext(List<ISection> articleResponse) {
                            handleResponse(articleResponse);
                        }
                    }));
            // Do not flat map the responses since the related articles seems to be optional?
            fetchRelatedArticles(mId);
        } else {
            getView().onArticleFetched(sections);
            getView().onRelatedArticlesFetched(mRelatedResponse);
        }
    }

    private void fetchRelatedArticles(String id) {
        addToSubscriptions(interactor.fetchRelatedArticles(id)
                .compose(getScheduler().<RelatedResponse>scheduleObservable())
                .subscribe(new Subscriber<RelatedResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "onError");
                    }

                    @Override
                    public void onNext(RelatedResponse relatedRelatedResponse) {
                        mRelatedResponse = relatedRelatedResponse;
                        getView().onRelatedArticlesFetched(relatedRelatedResponse);
                    }
                }));
    }

    private void handleResponse(List<ISection> articleResponse) {
        sections = articleResponse;
        getView().showProgressIndicator(false);
        getView().onArticleFetched(articleResponse);
    }

}
