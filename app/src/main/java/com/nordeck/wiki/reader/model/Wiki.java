package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by parker on 9/6/15.
 */
public class Wiki implements Parcelable{
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("hub")
    private String hub;
    @Expose
    @SerializedName("language")
    private String language;
    @Expose
    @SerializedName("topic")
    private String topic;
    @Expose
    @SerializedName("domain")
    private String domain;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.hub);
        dest.writeString(this.language);
        dest.writeString(this.topic);
        dest.writeString(this.domain);
    }

    public Wiki() {
    }

    protected Wiki(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.hub = in.readString();
        this.language = in.readString();
        this.topic = in.readString();
        this.domain = in.readString();
    }

    public static final Creator<Wiki> CREATOR = new Creator<Wiki>() {
        public Wiki createFromParcel(Parcel source) {
            return new Wiki(source);
        }

        public Wiki[] newArray(int size) {
            return new Wiki[size];
        }
    };

    @Override
    public String toString() {
        return "Wiki{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", hub='" + hub + '\'' +
                ", language='" + language + '\'' +
                ", topic='" + topic + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}
