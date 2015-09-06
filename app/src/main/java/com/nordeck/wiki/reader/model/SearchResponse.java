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
    @SerializedName("total")
    private int total;
    @Expose
    @SerializedName("total")
    private int batches;
    @Expose
    @SerializedName("currentBatch")
    private int currentBatch;
    @Expose
    @SerializedName("items")
    private List<Page> items;

    protected SearchResponse(Parcel in) {
        total = in.readInt();
        batches = in.readInt();
        currentBatch = in.readInt();
        items = in.createTypedArrayList(Page.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total);
        dest.writeInt(batches);
        dest.writeInt(currentBatch);
        dest.writeTypedList(items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResponse that = (SearchResponse) o;

        if (total != that.total) return false;
        if (batches != that.batches) return false;
        if (currentBatch != that.currentBatch) return false;
        return !(items != null ? !items.equals(that.items) : that.items != null);

    }

    @Override
    public int hashCode() {
        int result = total;
        result = 31 * result + batches;
        result = 31 * result + currentBatch;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "total=" + total +
                ", batches=" + batches +
                ", currentBatch=" + currentBatch +
                ", items=" + items +
                '}';
    }
}
