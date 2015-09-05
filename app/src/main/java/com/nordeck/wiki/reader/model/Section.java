package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by parker on 9/4/15.
 */
public class Section implements Parcelable {

    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("level")
    private int level;
    @Expose
    @SerializedName("images")
    private List<Image> images;
    @Expose
    @SerializedName("content")
    private List<Content> content;

    private String contentStr;

    protected Section(Parcel in) {
        title = in.readString();
        level = in.readInt();
        images = in.createTypedArrayList(Image.CREATOR);
        content = in.createTypedArrayList(Content.CREATOR);
        contentStr = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(level);
        dest.writeTypedList(images);
        dest.writeTypedList(content);
        dest.writeString(contentStr);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Section> CREATOR = new Creator<Section>() {
        @Override
        public Section createFromParcel(Parcel in) {
            return new Section(in);
        }

        @Override
        public Section[] newArray(int size) {
            return new Section[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public int getLevel() {
        return level;
    }

    public List<Image> getImages() {
        return images;
    }

    public List<Content> getContent() {
        return content;
    }

    public boolean isContentTitle() {
        return getContent() == null || getContent().size() == 0;
    }

    private boolean isContentType(String type) {
        boolean isType = false;
        int size = getContent().size();
        for (int i = size - 1; i >= 0; i--) {
            isType = TextUtils.equals(getContent().get(i).getType(), type);
            if (isType) {
                break;
            }
        }
        return isType;
    }

    public boolean isContentParagraph() {
        return isContentType(Content.TYPE_PARAGRAPH);
    }

    public boolean isContentList() {
        return isContentType(Content.TYPE_LIST);
    }

    public String getParagraphStr() {
        if (TextUtils.isEmpty(contentStr)) {
            contentStr = "";
            int size = getContent().size();
            for (int i = 0; i < size; i++) {
                Content content = getContent().get(i);
                contentStr = contentStr + content.getText();
                if (i != size - 1) {
                    contentStr = contentStr + "\n\n";
                }
            }
        }
        return contentStr;
    }

    /**
     * Convert the list of elements into an unordered list with html...
     *
     * @param elements
     * @return
     */
    private String getElementList(List<Element> elements) {
        String elementStr = "";
        int size = elements.size();
        for (int i = 0; i < size; i++) {
            Element element = elements.get(i);
            if (i == 0) {
                // first element start the list
                elementStr = "<ul>";
            }
            // start the list item
            elementStr = elementStr + "<li>";

            if (!TextUtils.isEmpty(element.getText())) {
                elementStr = elementStr + element.getText();
            }
            if (element.getElements() != null && element.getElements().size() > 0) {
                // There are lists in list...
                elementStr = getElementList(element.getElements());
            }
            // end the list item
            elementStr = elementStr + "</li>";
            if (i == size - 1) {
                // last element close the list
                elementStr = elementStr + "</ul>";
            }
        }
        return elementStr;
    }

    /**
     * TODO there are extra spaces at the end of a list
     *
     * @return
     */
    public String getListStr() {
        if (TextUtils.isEmpty(contentStr)) {
            contentStr = "";
            int size = getContent().size();
            for (int i = 0; i < size; i++) {
                Content content = getContent().get(i);
                contentStr = contentStr + getElementList(content.getElements());
                if (i != size - 1) {
                    contentStr.trim();
                }
            }
        }
        return contentStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        if (level != section.level) return false;
        if (title != null ? !title.equals(section.title) : section.title != null) return false;
        if (images != null ? !images.equals(section.images) : section.images != null) return false;
        return !(content != null ? !content.equals(section.content) : section.content != null);

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + level;
        result = 31 * result + (images != null ? images.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Section{" +
                "title='" + title + '\'' +
                ", level=" + level +
                ", images=" + images +
                ", content=" + content +
                '}';
    }
}
