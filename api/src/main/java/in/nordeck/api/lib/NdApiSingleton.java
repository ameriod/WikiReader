package in.nordeck.api.lib;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;

/**
 * Provides the {@link OkHttpClient}, the log level, and the {@link Gson}
 * Created by parker on 10/4/15.
 */
public class NdApiSingleton {

    private static NdApiSingleton sInstance;
    private OkHttpClient sClient;
    private Gson sGson;

    /**
     * Defaults to full
     */
    private RestAdapter.LogLevel logLevel;

    @NonNull
    public static NdApiSingleton getInstance() {
        if (sInstance == null) {
            sInstance = new NdApiSingleton();
        }
        return sInstance;
    }

    private NdApiSingleton() {
    }

    public RestAdapter.LogLevel getLogLevel() {
        if (logLevel == null) {
            logLevel = RestAdapter.LogLevel.FULL;
        }
        return logLevel;
    }

    public void setLogLevel(@NonNull RestAdapter.LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    @NonNull
    public OkHttpClient getClient() {
        if (sClient == null) {
            sClient = new OkHttpClient();
        }
        return sClient;
    }

    public void setClient(@NonNull OkHttpClient client) {
        this.sClient = client;
    }

    @NonNull
    public Gson getGson() {
        if (sGson == null) {
            sGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        }
        return sGson;
    }
}
