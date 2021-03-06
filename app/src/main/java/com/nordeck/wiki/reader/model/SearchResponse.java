package com.nordeck.wiki.reader.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by parker on 9/5/15.
 */
public class SearchResponse implements Parcelable {
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
    private List<Page> items;

    protected SearchResponse(Parcel in) {
        next = in.readInt();
        total = in.readInt();
        batches = in.readInt();
        currentBatch = in.readInt();
        items = in.createTypedArrayList(Page.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(next);
        dest.writeInt(total);
        dest.writeInt(batches);
        dest.writeInt(currentBatch);
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchResponse> CREATOR = new Creator<SearchResponse>() {
        @Override
        public SearchResponse createFromParcel(Parcel in) {
            return new SearchResponse(in);
        }

        @Override
        public SearchResponse[] newArray(int size) {
            return new SearchResponse[size];
        }
    };

    public int getTotal() {
        return total;
    }

    public int getBatches() {
        return batches;
    }

    public int getCurrentBatch() {
        return currentBatch;
    }

    public List<Page> getItems() {
        return items;
    }

    public int getNext() {
        return next;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "next=" + next +
                ", total=" + total +
                ", batches=" + batches +
                ", currentBatch=" + currentBatch +
                ", items=" + items +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResponse that = (SearchResponse) o;

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
}
