package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by parker on 9/4/15.
 */
public class Element implements Parcelable {
    @Expose
    @SerializedName("text")
    private String text;
    @Expose
    @SerializedName("element")
    private List<Element> elements;

    protected Element(Parcel in) {
        text = in.readString();
        elements = in.createTypedArrayList(Element.CREATOR);
    }

    public static final Creator<Element> CREATOR = new Creator<Element>() {
        @Override
        public Element createFromParcel(Parcel in) {
            return new Element(in);
        }

        @Override
        public Element[] newArray(int size) {
            return new Element[size];
        }
    };

    public String getText() {
        return text;
    }

    public List<Element> getElements() {
        return elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        if (text != null ? !text.equals(element.text) : element.text != null) return false;
        return !(elements != null ? !elements.equals(element.elements) : element.elements != null);

    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (elements != null ? elements.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Element{" +
                "text='" + text + '\'' +
                ", elements=" + elements +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeTypedList(elements);
    }
}
