package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parker on 9/4/15.
 */
public class Image implements Parcelable {
    @Expose
    @SerializedName("src")
    private String src;
    @Expose
    @SerializedName("caption")
    private String caption;

    public String getSrc() {
        return src;
    }

    public String getCaption() {
        return caption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        if (src != null ? !src.equals(image.src) : image.src != null) return false;
        return !(caption != null ? !caption.equals(image.caption) : image.caption != null);

    }

    @Override
    public int hashCode() {
        int result = src != null ? src.hashCode() : 0;
        result = 31 * result + (caption != null ? caption.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Image{" +
                "src='" + src + '\'' +
                ", caption='" + caption + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.src);
        dest.writeString(this.caption);
    }

    public Image() {
    }

    protected Image(Parcel in) {
        this.src = in.readString();
        this.caption = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
