package com.nordeck.wiki.reader.api;

import android.support.annotation.NonNull;

import com.nordeck.lib.api.BaseService;
import com.nordeck.wiki.reader.model.SearchResponse;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by parker on 9/5/15.
 */
public class SearchArticlesService extends BaseService {
    private SearchWebService mService;

    public static final int LIMIT = 25;

    public SearchArticlesService(String baseUrlPath) {
        super(baseUrlPath);
    }

    @Override
    protected void initService(@NonNull RestAdapter restAdapter) {
        mService = restAdapter.create(SearchWebService.class);
    }

    private interface SearchWebService {

        @GET("/api/v1/Search/List")
        Observable<SearchResponse> fetchSearchResults(@Query("query") String query, @Query("limit") int limit,
                                                      @Query("minArticleQuality") String minArticleQuality, @Query
                                                              ("batch") int batch);
    }

    /**
     * Initial search page set to limit of 25
     *
     * @param query
     * @return
     */
    public Observable<SearchResponse> getSearchResults(@NonNull String query) {
        return mService.fetchSearchResults(query, LIMIT, "10", 1);
    }

    /**
     * Paging search
     *
     * @param query
     * @param batch
     * @return
     */
    public Observable<SearchResponse> getSearchResults(@NonNull String query, @NonNull int batch) {
        return mService.fetchSearchResults(query, LIMIT, "10", batch);
    }
}
