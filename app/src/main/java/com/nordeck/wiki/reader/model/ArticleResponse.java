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
    private String id;

    protected ArticleResponse(Parcel in) {
        sections = in.createTypedArrayList(Section.CREATOR);
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(sections);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Section> getSections() {
        return sections;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArticleResponse that = (ArticleResponse) o;

        if (sections != null ? !sections.equals(that.sections) : that.sections != null) return false;
        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        int result = sections != null ? sections.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
