package com.nordeck.wiki.reader.api;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.nordeck.lib.api.BaseService;
import com.nordeck.wiki.reader.model.WikiDetail;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by parker on 9/6/15.
 */
public class WikiDetailService extends BaseService {

    private TopWikiWebService mWebService;


    @NonNull
    @Override
    protected RestAdapter.Builder getBuilder(String baseUrlPath) {
        return super.getBuilder(baseUrlPath)
                .setConverter(new GsonConverter(new GsonBuilder().registerTypeAdapter(WikiDetail.class,
                        new WikiDetail.Deserializer())
                        .create()));
    }

    public WikiDetailService() {
        super(WikisService.WIKIA_BASE);
    }

    @Override
    protected void initService(@NonNull RestAdapter restAdapter) {
        mWebService = restAdapter.create(TopWikiWebService.class);
    }

    private interface TopWikiWebService {

        @GET("/api/v1/Wikis/Details")
        Observable<WikiDetail> fetchWikiDetail(@Query("ids") String id);

    }


    public Observable<WikiDetail> getWikiDetail(@NonNull String id) {
        return mWebService.fetchWikiDetail(id);
    }

}