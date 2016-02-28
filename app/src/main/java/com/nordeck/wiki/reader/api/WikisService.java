package com.nordeck.wiki.reader.api;

import android.support.annotation.NonNull;

import com.nordeck.lib.api.BaseService;
import com.nordeck.wiki.reader.model.WikiResponse;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Gets the top articles
 * <p/>
 * Created by parker on 9/4/15.
 */
public class WikisService extends BaseService {

    private TopWikiWebService mWebService;

    public static final String WIKIA_BASE = "http://www.wikia.com";
    private static final int LIMIT = 25;

    public WikisService() {
        super(WIKIA_BASE);
    }

    @Override
    protected void initService(RestAdapter restAdapter) {
        mWebService = restAdapter.create(TopWikiWebService.class);
    }

    private interface TopWikiWebService {

        @GET("/api/v1/Wikis/List")
        Observable<WikiResponse> fetchTopWikis(@Query("limit") int limit, @Query("batch") int batch);

        @GET("/api/v1/Wikis/ByString")
        Observable<WikiResponse> fetchWikisByQuery(@Query("string") String query, @Query("limit") int limit, @Query
                ("batch") int batch);
    }

    public Observable<WikiResponse> getTopWikis() {
        return mWebService.fetchTopWikis(LIMIT, 1);
    }

    public Observable<WikiResponse> getWikisByQuery(@NonNull String query) {
        return mWebService.fetchWikisByQuery(query, LIMIT, 1);
    }
}
