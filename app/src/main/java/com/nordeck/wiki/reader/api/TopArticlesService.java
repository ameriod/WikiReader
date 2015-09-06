package com.nordeck.wiki.reader.api;

import com.nordeck.wiki.reader.model.PagesResponse;

import retrofit.RestAdapter;
import retrofit.http.GET;
import rx.Observable;

/**
 * Gets the top articles
 * <p/>
 * Created by parker on 9/4/15.
 */
public class TopArticlesService extends BaseService {

    private TopWebService mWebService;

    public TopArticlesService(String baseUrlPath) {
        super(baseUrlPath);
    }

    @Override
    protected void initService(RestAdapter restAdapter) {
        mWebService = restAdapter.create(TopWebService.class);
    }

    private interface TopWebService {

        @GET("/api/v1/Articles/Top/AsSimpleJson")
        Observable<PagesResponse> fetchTopArticles();
    }


    public Observable<PagesResponse> getTopArticles() {
        return makeAsync(mWebService.fetchTopArticles());
    }
}
