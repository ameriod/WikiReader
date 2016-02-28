package com.nordeck.wiki.reader.api;

import com.nordeck.lib.api.BaseService;
import com.nordeck.wiki.reader.model.ArticleResponse;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.functions.Func1;

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

    /**
     * Gets an article by id
     *
     * @param id
     * @return
     */
    public Observable<ArticleResponse> getArticle(String id) {
        return getArticleNonAsync(id);
    }

    private Observable<ArticleResponse> getArticleNonAsync(final String id) {
        return mWebService.fetchArticle(id)
                .map(new Func1<ArticleResponse, ArticleResponse>() {
                    @Override
                    public ArticleResponse call(ArticleResponse articleResponse) {
                        // Set the article id on the response since it does not come back in the response
                        articleResponse.setId(id);
                        return articleResponse;
                    }
                });
    }

    /**
     * Gets a random article using the {@link WikiHtmlService#getRandomPageId()} flat mapped to the
     * {@link #getArticle(String)}
     *
     * @return
     */
    public Observable<ArticleResponse> getRandomArticle() {
        return new WikiHtmlService(getBaseUrlPath())
                .getRandomPageId().flatMap(new Func1<String, Observable<ArticleResponse>>() {
                    @Override
                    public Observable<ArticleResponse> call(String s) {
                        return getArticleNonAsync(s);
                    }
                });
    }

}
