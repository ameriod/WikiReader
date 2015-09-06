package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parker on 9/5/15.
 */
public class Page implements Parcelable, IPage {
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("url")
    private String url;
    @Expose
    @SerializedName("abstract")
    private String abstractStr;
    @Expose
    @SerializedName("thumbnail")
    private String thumbnail;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getImageUrl() {
        return thumbnail;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getSummary() {
        return abstractStr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.id);
        dest.writeString(this.url);
        dest.writeString(this.abstractStr);
        dest.writeString(this.thumbnail);
    }

    public Page() {
    }

    protected Page(Parcel in) {
        this.title = in.readString();
        this.id = in.readString();
        this.url = in.readString();
        this.abstractStr = in.readString();
        this.thumbnail = in.readString();
    }

    public static final Creator<Page> CREATOR = new Creator<Page>() {
        public Page createFromParcel(Parcel source) {
            return new Page(source);
        }

        public Page[] newArray(int size) {
            return new Page[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        if (title != null ? !title.equals(page.title) : page.title != null) return false;
        if (id != null ? !id.equals(page.id) : page.id != null) return false;
        if (url != null ? !url.equals(page.url) : page.url != null) return false;
        if (abstractStr != null ? !abstractStr.equals(page.abstractStr) : page.abstractStr != null) return false;
        return !(thumbnail != null ? !thumbnail.equals(page.thumbnail) : page.thumbnail != null);

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (abstractStr != null ? abstractStr.hashCode() : 0);
        result = 31 * result + (thumbnail != null ? thumbnail.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Page{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", abstractStr='" + abstractStr + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
