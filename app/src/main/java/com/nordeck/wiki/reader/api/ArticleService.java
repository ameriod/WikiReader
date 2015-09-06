package com.nordeck.wiki.reader.api;

import android.support.annotation.NonNull;

import com.nordeck.wiki.reader.model.ArticleResponse;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Gets an article by id
 * <p/>
 * Created by parker on 9/4/15.
 */
public class ArticleService extends BaseService {

    private ArticleWebService mWebService;

    public ArticleService(String baseUrlPath) {
        super(baseUrlPath);
    }

    @Override
    protected void initService(RestAdapter restAdapter) {
        mWebService = restAdapter.create(ArticleWebService.class);
    }

    private interface ArticleWebService {

        @GET("/api/v1/Articles/AsSimpleJson")
        Observable<ArticleResponse> fetchArticle(@Query("id") String id);
    }

    public Observable<ArticleResponse> getArticle(@NonNull String id) {
        return makeAsync(mWebService.fetchArticle(id));
    }

}
