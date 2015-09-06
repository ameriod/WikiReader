package com.nordeck.wiki.reader.api;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.nordeck.wiki.reader.model.RelatedPagesResponse;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by parker on 9/5/15.
 */
public class RelatedArticleService extends BaseService {

    private RelatedWebService mWebService;

    public RelatedArticleService(String baseUrlPath) {
        super(baseUrlPath);
    }

    @NonNull
    @Override
    protected RestAdapter.Builder getBuilder(String baseUrlPath) {
        return super.getBuilder(baseUrlPath).setConverter(new GsonConverter(new GsonBuilder().registerTypeAdapter
                (RelatedPagesResponse.class, new RelatedPagesResponse.Deserializer())
                .create()));
    }

    @Override
    protected void initService(@NonNull RestAdapter restAdapter) {
        mWebService = restAdapter.create(RelatedWebService.class);
    }

    private interface RelatedWebService {

        @GET("/api/v1/RelatedPages/List")
        Observable<RelatedPagesResponse> fetchRelatedPages(@Query("ids") String id);
    }


    public Observable<RelatedPagesResponse> getRelatedPages(@NonNull String id) {
        return makeAsync(mWebService.fetchRelatedPages(id));
    }
}
