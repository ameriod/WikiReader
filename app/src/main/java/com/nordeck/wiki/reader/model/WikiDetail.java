package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by parker on 9/6/15.
 */
public class WikiDetail implements Parcelable {

    private String id;
    private String wordmark;
    private String title;
    private String url;
    private String headline;
    private String lang;
    private String desc;
    private String image;

    protected WikiDetail(Parcel in) {
        id = in.readString();
        wordmark = in.readString();
        title = in.readString();
        url = in.readString();
        headline = in.readString();
        lang = in.readString();
        desc = in.readString();
        image = in.readString();
    }

    public static final Creator<WikiDetail> CREATOR = new Creator<WikiDetail>() {
        @Override
        public WikiDetail createFromParcel(Parcel in) {
            return new WikiDetail(in);
        }

        @Override
        public WikiDetail[] newArray(int size) {
            return new WikiDetail[size];
        }
    };

    private WikiDetail(Builder builder) {
        this.id = builder.id;
        this.wordmark = builder.wordmark;
        this.title = builder.title;
        this.url = builder.url;
        this.headline = builder.headline;
        this.lang = builder.lang;
        this.desc = builder.desc;
        this.image = builder.image;
    }

    private static Builder newWikiDetail() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getWordmark() {
        return wordmark;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getHeadline() {
        return headline;
    }

    public String getLang() {
        return lang;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(wordmark);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(headline);
        dest.writeString(lang);
        dest.writeString(desc);
        dest.writeString(image);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WikiDetail that = (WikiDetail) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (wordmark != null ? !wordmark.equals(that.wordmark) : that.wordmark != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (headline != null ? !headline.equals(that.headline) : that.headline != null) return false;
        if (lang != null ? !lang.equals(that.lang) : that.lang != null) return false;
        if (desc != null ? !desc.equals(that.desc) : that.desc != null) return false;
        return !(image != null ? !image.equals(that.image) : that.image != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (wordmark != null ? wordmark.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (headline != null ? headline.hashCode() : 0);
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WikiDetail{" +
                "id='" + id + '\'' +
                ", wordmark='" + wordmark + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", headline='" + headline + '\'' +
                ", lang='" + lang + '\'' +
                ", desc='" + desc + '\'' +
                ", image='" + image + '\'' +
                '}';
    }


    private static final class Builder {
        private String id;
        private String wordmark;
        private String title;
        private String url;
        private String headline;
        private String lang;
        private String desc;
        private String image;

        private Builder() {
        }

        public WikiDetail build() {
            return new WikiDetail(this);
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder wordmark(String wordmark) {
            this.wordmark = wordmark;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder headline(String headline) {
            this.headline = headline;
            return this;
        }

        public Builder lang(String lang) {
            this.lang = lang;
            return this;
        }

        public Builder desc(String desc) {
            this.desc = desc;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }
    }

    public static class Deserializer implements JsonDeserializer<WikiDetail> {
        @Override
        public WikiDetail deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
                JsonParseException {
            Builder builder = WikiDetail.newWikiDetail();
            JsonObject itemsObj = json.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : itemsObj.entrySet()) {
                JsonObject obj = entry.getValue().getAsJsonObject();
                for (Map.Entry<String, JsonElement> item : obj.entrySet()) {
                    JsonObject itemObj = item.getValue().getAsJsonObject();
                    builder.id(itemObj.get("id").getAsString());
                    builder.wordmark(itemObj.get("wordmark").getAsString());
                    builder.title(itemObj.get("title").getAsString());
                    builder.url(itemObj.get("url").getAsString());
                    builder.headline(itemObj.get("headline").getAsString());
                    builder.lang(itemObj.get("lang").getAsString());
                    builder.desc(itemObj.get("desc").getAsString());
                    builder.image(itemObj.get("image").getAsString());
                }
            }
            return builder.build();
        }
    }

}
