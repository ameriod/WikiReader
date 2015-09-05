package com.nordeck.wiki.reader.api;

import android.support.annotation.NonNull;

import retrofit.RestAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Base class for all OkHttp Services
 * <p/>
 * Created by parker on 9/4/15.
 */
abstract class BaseService {

    public BaseService(String baseUrlPath) {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(baseUrlPath)
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();

        initService(restAdapter);
    }

    protected abstract void initService(@NonNull RestAdapter restAdapter);

    protected Observable makeAsync(Observable observable) {
        return observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
