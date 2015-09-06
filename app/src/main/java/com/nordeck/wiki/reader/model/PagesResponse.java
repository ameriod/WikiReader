package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * For the {@link com.nordeck.wiki.reader.api.RelatedArticleService}:
 * <p/>
 * Not Gson based since the response's list is an objects keys are dynamic based on the id we are looking at. Use
 * {@link PagesResponse.Deserializer} to convert the response
 * <p/>
 * {@link com.nordeck.wiki.reader.api.TopArticlesService} Gson based response
 * <p/>
 * Created by parker on 9/5/15.
 */
public class PagesResponse implements Parcelable {
    @Expose
    @SerializedName("items")
    private List<Page> pages;
    @Expose
    @SerializedName("basepath")
    private String basePath;

    private PagesResponse(List<Page> pages, String basePath) {
        this.pages = pages;
        this.basePath = basePath;
    }

    protected PagesResponse(Parcel in) {
        pages = in.createTypedArrayList(Page.CREATOR);
        basePath = in.readString();
    }

    public static final Creator<PagesResponse> CREATOR = new Creator<PagesResponse>() {
        @Override
        public PagesResponse createFromParcel(Parcel in) {
            return new PagesResponse(in);
        }

        @Override
        public PagesResponse[] newArray(int size) {
            return new PagesResponse[size];
        }
    };

    public List<Page> getItems() {
        return pages;
    }

    public String getBasePath() {
        return basePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(pages);
        dest.writeString(basePath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PagesResponse that = (PagesResponse) o;

        if (pages != null ? !pages.equals(that.pages) : that.pages != null) return false;
        return !(basePath != null ? !basePath.equals(that.basePath) : that.basePath != null);

    }

    @Override
    public int hashCode() {
        int result = pages != null ? pages.hashCode() : 0;
        result = 31 * result + (basePath != null ? basePath.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RelatedPagesResponse{" +
                "pages=" + pages +
                ", basePath='" + basePath + '\'' +
                '}';
    }

    /**
     * Converts the server response into one we can actually use
     */
    public static class Deserializer implements JsonDeserializer<PagesResponse> {

        public Deserializer() {
        }

        @Override
        public PagesResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            String basePath = obj.getAsJsonPrimitive("basepath").getAsString();
            ArrayList<Page> pages = new ArrayList<>();
            JsonObject itemsObj = obj.getAsJsonObject("items");
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            for (Map.Entry<String, JsonElement> entry : itemsObj.entrySet()) {
                // every array is keyed to a different page id
                JsonArray pagesArray = entry.getValue().getAsJsonArray();
                int size = pagesArray.size();
                for (int i = 0; i < size; i++) {
                    pages.add(gson.fromJson(pagesArray.get(i), Page.class));
                }
            }
            return new PagesResponse(pages, basePath);
        }
    }
}
