package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * {@link com.nordeck.wiki.reader.api.ArticleService}
 * <p/>
 * Created by parker on 9/4/15.
 */
public class ArticleResponse implements Parcelable {
    @Expose
    @SerializedName("sections")
    private List<Section> sections;

    protected ArticleResponse(Parcel in) {
        sections = in.createTypedArrayList(Section.CREATOR);
    }

    public static final Creator<ArticleResponse> CREATOR = new Creator<ArticleResponse>() {
        @Override
        public ArticleResponse createFromParcel(Parcel in) {
            return new ArticleResponse(in);
        }

        @Override
        public ArticleResponse[] newArray(int size) {
            return new ArticleResponse[size];
        }
    };

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArticleResponse that = (ArticleResponse) o;

        return !(sections != null ? !sections.equals(that.sections) : that.sections != null);

    }

    @Override
    public int hashCode() {
        return sections != null ? sections.hashCode() : 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(sections);
    }
}
