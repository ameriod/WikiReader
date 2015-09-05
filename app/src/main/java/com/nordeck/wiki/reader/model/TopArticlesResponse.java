package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * {@link com.nordeck.wiki.reader.api.TopArticlesService}
 * <p/>
 * Created by parker on 9/4/15.
 */
public class TopArticlesResponse implements Parcelable {
    @Expose
    @SerializedName("base_path")
    private String basePath;
    @Expose
    @SerializedName("items")
    private List<TopArticle> items;

    protected TopArticlesResponse(Parcel in) {
        basePath = in.readString();
        items = in.createTypedArrayList(TopArticle.CREATOR);
    }

    public static final Creator<TopArticlesResponse> CREATOR = new Creator<TopArticlesResponse>() {
        @Override
        public TopArticlesResponse createFromParcel(Parcel in) {
            return new TopArticlesResponse(in);
        }

        @Override
        public TopArticlesResponse[] newArray(int size) {
            return new TopArticlesResponse[size];
        }
    };

    public String getBasePath() {
        return basePath;
    }

    public List<TopArticle> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopArticlesResponse that = (TopArticlesResponse) o;

        if (basePath != null ? !basePath.equals(that.basePath) : that.basePath != null) return false;
        return !(items != null ? !items.equals(that.items) : that.items != null);

    }

    @Override
    public int hashCode() {
        int result = basePath != null ? basePath.hashCode() : 0;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TopResponse{" +
                "basePath='" + basePath + '\'' +
                ", items=" + items +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(basePath);
        dest.writeTypedList(items);
    }
}
