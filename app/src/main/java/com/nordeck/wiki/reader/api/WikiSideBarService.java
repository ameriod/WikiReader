package com.nordeck.wiki.reader.api;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.RestAdapter;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by parker on 9/11/15.
 */
public class WikiSideBarService extends BaseService {

    private WikiSidebarWebService mWebService;

    @NonNull
    @Override
    protected RestAdapter.Builder getBuilder(String baseUrlPath) {
        return super.getBuilder(baseUrlPath).setConverter(new HtmlConverter());
    }

    public WikiSideBarService(String baseUrlPath) {
        super(baseUrlPath);
    }

    @Override
    protected void initService(@NonNull RestAdapter restAdapter) {
        mWebService = restAdapter.create(WikiSidebarWebService.class);

    }

    private interface WikiSidebarWebService {

        @GET("/wiki/Special:Random")
        Observable<String> fetchRandomPage();

    }

    /**
     * WARNING  Not Async
     *
     * @return
     */
    public Observable<String> getRandomPageId() {
        return mWebService.fetchRandomPage()
                .map(getPageIdFromRawHtml());
    }

    /**
     * Input page's raw  html and get the id out of it.
     *
     * @return
     */
    public static Func1<String, String> getPageIdFromRawHtml() {
        return new Func1<String, String>() {
            @Override
            public String call(String html) {
                String prefix = "wgArticleId=";
                String postfix = ",";
                String pattern = "(.*?)";
                Pattern p = Pattern.compile(Pattern.quote(prefix) + pattern + Pattern.quote(postfix));
                Matcher m = p.matcher(html);
                while (m.find()) {
                    // Get the sandwiched id
                    return m.group(1);
                }
                return null;
            }
        };
    }

    /**
     * {@link Converter} that just returns the raw response as a string.
     */
    public static class HtmlConverter implements Converter {
        @Override
        public Object fromBody(TypedInput body, Type type) throws ConversionException {
            String text = null;
            try {
                text = fromStream(body.in());
            } catch (IOException e) {
                // no impl
            }

            return text;
        }

        @Override
        public TypedOutput toBody(Object object) {
            return null;
        }

        public String fromStream(InputStream in) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(newLine);
            }
            return out.toString();
        }
    }
}
