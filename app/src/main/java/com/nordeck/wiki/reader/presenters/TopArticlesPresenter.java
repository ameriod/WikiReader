package com.nordeck.wiki.reader.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nordeck.wiki.reader.api.TopArticlesService;
import com.nordeck.wiki.reader.model.TopArticlesResponse;
import com.nordeck.wiki.reader.ui.ITopArticlesView;
import com.nordeck.wiki.reader.ui.TopActivity;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by parker on 9/4/15.
 */
public class TopArticlesPresenter extends NdBasePresenter<ITopArticlesView> {

    private TopArticlesResponse mResponse;

    private static final String OUT_STATE_RESPONSE = "out_state_response";

    public void fetchTopArticles(boolean forceLoad) {
        if (mResponse != null && mResponse.getItems() != null && mResponse.getItems().size() > 0 && !forceLoad) {
            getView().onTopArticlesFetched(mResponse);
        } else {
            getView().showProgressIndicator(true);
            addToSubscriptions(new TopArticlesService(TopActivity.STAR_WARS_WIKI).getTopArticles()
                    .subscribe(new TopArticlesSubscriber()));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            mResponse = bundle.getParcelable(OUT_STATE_RESPONSE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (mResponse != null) {
            bundle.putParcelable(OUT_STATE_RESPONSE, mResponse);
        }
    }

    private class TopArticlesSubscriber extends Subscriber<TopArticlesResponse> {

        @Override
        public void onCompleted() {
            Timber.d("TopSubscriber.onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            getView().displayError("Error Fetching Top Articles");
            Timber.e(e, "TopSubscriber.onError");
        }

        @Override
        public void onNext(TopArticlesResponse topArticlesResponse) {
            mResponse = topArticlesResponse;
            Timber.d("TopSubscriber.onNext");
            getView().onTopArticlesFetched(topArticlesResponse);
        }
    }
}
