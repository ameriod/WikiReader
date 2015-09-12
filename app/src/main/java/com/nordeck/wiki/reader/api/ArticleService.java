package com.nordeck.wiki.reader.api;

import android.support.annotation.NonNull;

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
    public Observable<ArticleResponse> getArticle(@NonNull String id) {
        return makeAsync(getArticleNonAsync(id));
    }

    private Observable<ArticleResponse> getArticleNonAsync(@NonNull final String id) {
        return mWebService.fetchArticle(id).map(new Func1<ArticleResponse, ArticleResponse>() {
            @Override
            public ArticleResponse call(ArticleResponse articleResponse) {
                // Set the article id on the response since it does not come back in the response
                articleResponse.setId(id);
                return articleResponse;
            }
        });
    }

    /**
     * Gets a random article using the {@link WikiSideBarService#getRandomPageId()} flat mapped to the
     * {@link #getArticle(String)}
     *
     * @return
     */
    public Observable<ArticleResponse> getRandomArticle() {
        return makeAsync(new WikiSideBarService(getBaseUrlPath())
                .getRandomPageId()
                .flatMap(new Func1<String, Observable<ArticleResponse>>() {
                    @Override
                    public Observable<ArticleResponse> call(String id) {
                        return getArticleNonAsync(id);
                    }
                }));
    }

}
