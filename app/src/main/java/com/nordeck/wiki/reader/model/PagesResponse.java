package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by parker on 9/5/15.
 */
public class PagesResponse implements Parcelable {
    @Expose
    @SerializedName("items")
    private List<Page> items;
    @Expose
    @SerializedName("basepath")
    private String basePath;

    public List<Page> getItems() {
        return items;
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
        dest.writeTypedList(items);
        dest.writeString(this.basePath);
    }

    public PagesResponse() {
    }

    protected PagesResponse(Parcel in) {
        this.items = in.createTypedArrayList(Page.CREATOR);
        this.basePath = in.readString();
    }

    public static final Creator<PagesResponse> CREATOR = new Creator<PagesResponse>() {
        public PagesResponse createFromParcel(Parcel source) {
            return new PagesResponse(source);
        }

        public PagesResponse[] newArray(int size) {
            return new PagesResponse[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PagesResponse that = (PagesResponse) o;

        if (items != null ? !items.equals(that.items) : that.items != null) return false;
        return !(basePath != null ? !basePath.equals(that.basePath) : that.basePath != null);

    }

    @Override
    public int hashCode() {
        int result = items != null ? items.hashCode() : 0;
        result = 31 * result + (basePath != null ? basePath.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PageResponse{" +
                "items=" + items +
                ", basePath='" + basePath + '\'' +
                '}';
    }
}
