package com.nordeck.lib.api;

import retrofit.RestAdapter;

/**
 * Base class for all OkHttp Services
 * <p/>
 * Created by parker on 9/4/15.
 */
public abstract class BaseService {

    private String baseUrlPath;

    public BaseService(String baseUrlPath) {
        this.baseUrlPath = baseUrlPath;
        initService(getBuilder(baseUrlPath)
                .build());
    }

    protected RestAdapter.Builder getBuilder(String baseUrlPath) {
        return new RestAdapter.Builder()
                .setEndpoint(baseUrlPath)
                .setLogLevel(getLogLevel());
    }

    protected RestAdapter.LogLevel getLogLevel() {
        return RestAdapter.LogLevel.FULL;
    }

    protected String getBaseUrlPath() {
        return baseUrlPath;
    }

    protected abstract void initService(RestAdapter restAdapter);

}
