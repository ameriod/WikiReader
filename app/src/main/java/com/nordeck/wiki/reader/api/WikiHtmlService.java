package com.nordeck.wiki.reader.api;

import android.support.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.RestAdapter;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.http.GET;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by parker on 9/11/15.
 */
public class WikiHtmlService extends BaseService {

    private WikiSidebarWebService mWebService;

    @NonNull
    @Override
    protected RestAdapter.Builder getBuilder(String baseUrlPath) {
        return super.getBuilder(baseUrlPath).setConverter(new HtmlConverter());
    }

    public WikiHtmlService(String baseUrlPath) {
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

    public Observable<HtmlArticle> getHtmlArticleObj() {
        return getRandomPageId().map(convertHtmlToArticle());
    }

    public static Func1<String, HtmlArticle> convertHtmlToArticle() {
        return new Func1<String, HtmlArticle>() {
            @Override
            public HtmlArticle call(String s) {
                HtmlArticle htmlArticle = new HtmlArticle();
                htmlArticle.setHtml(s);
                htmlArticle.setHrefMap(getLinkMap(s));
                htmlArticle.setId(getHtmlId(s));
                return htmlArticle;
            }
        };
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
                return getHtmlId(html);
            }
        };
    }

    private static String getHtmlId(String html) {
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

    /**
     * Gets all of the links from a page key is the link text and with the link element
     *
     * @return
     */
    public static Func1<String, Map<String, Element>> getLinksFromPage() {
        return new Func1<String, Map<String, Element>>() {
            @Override
            public Map<String, Element> call(String html) {
                return getLinkMap(html);
            }
        };
    }

    private static Map<String, Element> getLinkMap(String html) {
        Map<String, Element> hrefMap = new HashMap<>();
        Document document = Jsoup.parse(html);
        Elements links = document.select("a[href]");
        for (int i = 0, size = links.size(); i < size; i++) {
            Element link = links.get(i);
            hrefMap.put(link.text(), link);
        }
        return hrefMap;
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


    public static class HtmlArticle {
        private String id;
        private String html;
        private Map<String, Element> hrefMap;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        public Map<String, Element> getHrefMap() {
            return hrefMap;
        }

        public void setHrefMap(Map<String, Element> hrefMap) {
            this.hrefMap = hrefMap;
        }
    }

}
