package com.nordeck.wiki.reader.api;

import com.nordeck.wiki.reader.model.PagesResponse;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Gets the top articles
 * <p/>
 * Created by parker on 9/4/15.
 */
public class TopArticlesService extends BaseService {

    private TopWebService mWebService;
    private static final int LIMIT = 100;

    public TopArticlesService(String baseUrlPath) {
        super(baseUrlPath);
    }

    @Override
    protected void initService(RestAdapter restAdapter) {
        mWebService = restAdapter.create(TopWebService.class);
    }

    private interface TopWebService {

        @GET("/api/v1/Articles/Top/")
        Observable<PagesResponse> fetchTopArticles(@Query("limit") int limit);

        @GET("/api/v1/Articles/Top/")
        Observable<PagesResponse> fetchTopArticlesExpanded(@Query("expand") String expand, @Query("limit") int limit);
    }


    /**
     * Response comes back with the title and id
     *
     * @return
     */
    public Observable<PagesResponse> getTopArticles() {
        return makeAsync(mWebService.fetchTopArticles(LIMIT));
    }

    /**
     * Response comes back with the image, summary text, title, id
     *
     * @return
     */
    public Observable<PagesResponse> getTopArticlesExpanded() {
        return makeAsync(mWebService.fetchTopArticlesExpanded("1", LIMIT));
    }
}
