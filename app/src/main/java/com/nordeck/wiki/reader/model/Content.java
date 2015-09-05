package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by parker on 9/4/15.
 */
public class Content implements Parcelable {

    public static final String TYPE_LIST = "list";
    public static final String TYPE_PARAGRAPH = "paragraph";

    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("text")
    private String text;
    @Expose
    @SerializedName("elements")
    private List<Element> elements;

    protected Content(Parcel in) {
        type = in.readString();
        text = in.readString();
        elements = in.createTypedArrayList(Element.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(text);
        dest.writeTypedList(elements);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public List<Element> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        return "Content{" +
                "type='" + type + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Content content = (Content) o;

        if (type != null ? !type.equals(content.type) : content.type != null) return false;
        if (text != null ? !text.equals(content.text) : content.text != null) return false;
        return !(elements != null ? !elements.equals(content.elements) : content.elements != null);

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (elements != null ? elements.hashCode() : 0);
        return result;
    }


}
