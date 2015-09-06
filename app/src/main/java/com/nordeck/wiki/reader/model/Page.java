package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parker on 9/5/15.
 */
public class Page implements Parcelable, ISection {
    @Expose
    @SerializedName("url")
    private String url;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("imgUrl")
    private String imgUrl;
    @Expose
    @SerializedName("text")
    private String text;

    protected Page(Parcel in) {
        url = in.readString();
        title = in.readString();
        id = in.readString();
        imgUrl = in.readString();
        text = in.readString();
    }

    public static final Creator<Page> CREATOR = new Creator<Page>() {
        @Override
        public Page createFromParcel(Parcel in) {
            return new Page(in);
        }

        @Override
        public Page[] newArray(int size) {
            return new Page[size];
        }
    };

    public String getUrl() {
        return url;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        if (url != null ? !url.equals(page.url) : page.url != null) return false;
        if (title != null ? !title.equals(page.title) : page.title != null) return false;
        if (id != null ? !id.equals(page.id) : page.id != null) return false;
        if (imgUrl != null ? !imgUrl.equals(page.imgUrl) : page.imgUrl != null) return false;
        return !(text != null ? !text.equals(page.text) : page.text != null);

    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Page{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(title);
        dest.writeString(id);
        dest.writeString(imgUrl);
        dest.writeString(text);
    }
}
