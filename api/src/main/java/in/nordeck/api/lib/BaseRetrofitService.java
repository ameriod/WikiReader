package in.nordeck.api.lib;

import android.support.annotation.NonNull;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/**
 * Base class for all OkHttp Services. Uses the {@link NdApiSingleton}
 * <p/>
 * Created by parker on 9/4/15.
 */
abstract class BaseRetrofitService {

    private String baseUrlPath;

    public BaseRetrofitService(@NonNull String baseUrlPath) {
        this.baseUrlPath = baseUrlPath;
        initService(getBuilder(baseUrlPath).build());
    }

    @NonNull
    protected RestAdapter.Builder getBuilder(@NonNull String baseUrlPath) {
        return new RestAdapter.Builder()
                .setEndpoint(baseUrlPath)
                .setClient(getClient())
                .setLogLevel(getLogLevel())
                .setConverter(getConverter());
    }

    @NonNull
    protected String getBaseUrlPath() {
        return baseUrlPath;
    }

    protected abstract void initService(@NonNull RestAdapter restAdapter);

    @NonNull
    protected Converter getConverter() {
        return new GsonConverter(NdApiSingleton.getInstance().getGson());
    }

    @NonNull
    protected RestAdapter.LogLevel getLogLevel() {
        return NdApiSingleton.getInstance().getLogLevel();
    }

    @NonNull
    protected Client getClient() {
        return new OkClient(NdApiSingleton.getInstance().getClient());
    }
}
