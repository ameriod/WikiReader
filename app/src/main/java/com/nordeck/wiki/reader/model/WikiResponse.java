package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by parker on 9/6/15.
 */
public class WikiResponse implements Parcelable {
    @Expose
    @SerializedName("next")
    private int next;
    @Expose
    @SerializedName("total")
    private int total;
    @Expose
    @SerializedName("batches")
    private int batches;
    @Expose
    @SerializedName("currentBatch")
    private int currentBatch;
    @Expose
    @SerializedName("items")
    private List<Wiki> items;

    public int getNext() {
        return next;
    }

    public int getTotal() {
        return total;
    }

    public int getBatches() {
        return batches;
    }

    public int getCurrentBatch() {
        return currentBatch;
    }

    public List<Wiki> getItems() {
        return items;
    }

    protected WikiResponse(Parcel in) {
        items = in.createTypedArrayList(Wiki.CREATOR);
    }

    public static final Creator<WikiResponse> CREATOR = new Creator<WikiResponse>() {
        @Override
        public WikiResponse createFromParcel(Parcel in) {
            return new WikiResponse(in);
        }

        @Override
        public WikiResponse[] newArray(int size) {
            return new WikiResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WikiResponse that = (WikiResponse) o;

        if (next != that.next) return false;
        if (total != that.total) return false;
        if (batches != that.batches) return false;
        if (currentBatch != that.currentBatch) return false;
        return !(items != null ? !items.equals(that.items) : that.items != null);

    }

    @Override
    public int hashCode() {
        int result = next;
        result = 31 * result + total;
        result = 31 * result + batches;
        result = 31 * result + currentBatch;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WikiTopResponse{" +
                "next=" + next +
                ", total=" + total +
                ", batches=" + batches +
                ", currentBatch=" + currentBatch +
                ", items=" + items +
                '}';
    }
}
