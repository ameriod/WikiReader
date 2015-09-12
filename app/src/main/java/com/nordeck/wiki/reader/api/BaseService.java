package com.nordeck.wiki.reader.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import retrofit.RestAdapter;
import retrofit.converter.Converter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Base class for all OkHttp Services
 * <p/>
 * Created by parker on 9/4/15.
 */
abstract class BaseService {

    private String baseUrlPath;

    public BaseService(String baseUrlPath) {
        this.baseUrlPath = baseUrlPath;
        initService(getBuilder(baseUrlPath)
                .build());
    }

    @NonNull
    protected RestAdapter.Builder getBuilder(String baseUrlPath) {
        return new RestAdapter.Builder()
                .setEndpoint(baseUrlPath)
                .setLogLevel(RestAdapter.LogLevel.FULL);
    }

    protected String getBaseUrlPath() {
        return baseUrlPath;
    }

    protected abstract void initService(@NonNull RestAdapter restAdapter);

    protected Observable makeAsync(Observable observable) {
        return observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
