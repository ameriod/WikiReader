package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parker on 9/4/15.
 */
public class TopArticle implements Parcelable{

    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("url")
    private String url;
    @Expose
    @SerializedName("ns")
    private String ns;

    protected TopArticle(Parcel in) {
        id = in.readString();
        title = in.readString();
        url = in.readString();
        ns = in.readString();
    }

    public static final Creator<TopArticle> CREATOR = new Creator<TopArticle>() {
        @Override
        public TopArticle createFromParcel(Parcel in) {
            return new TopArticle(in);
        }

        @Override
        public TopArticle[] newArray(int size) {
            return new TopArticle[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getNs() {
        return ns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopArticle topArticle = (TopArticle) o;

        if (id != null ? !id.equals(topArticle.id) : topArticle.id != null) return false;
        if (title != null ? !title.equals(topArticle.title) : topArticle.title != null) return false;
        if (url != null ? !url.equals(topArticle.url) : topArticle.url != null) return false;
        return !(ns != null ? !ns.equals(topArticle.ns) : topArticle.ns != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (ns != null ? ns.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Top{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", ns='" + ns + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(ns);
    }
}
