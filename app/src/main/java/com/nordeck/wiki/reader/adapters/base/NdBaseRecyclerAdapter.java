package com.nordeck.wiki.reader.adapters.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

public abstract class NdBaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private static final int LAST_POSITION = -1;

    protected LayoutInflater inflater;
    protected Context context;
    protected ArrayList<T> items;

    public NdBaseRecyclerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        items = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public T getItem(int position) {
        if (items != null && position >= 0 && position < items.size()) {
            return items.get(position);
        }
        return null;
    }

    public boolean removeItem(int position, boolean notify) {
        T item = items.remove(position);
        if (item == null) {
            return false;
        }
        if (notify) {
            notifyItemRemoved(position);
        }
        return true;
    }

    public boolean removeItem(int position) {
        return removeItem(position, false);
    }

    public void clear(boolean notify) {
        if (items != null) {
            int countPreClear = items.size();
            items.clear();
            if (notify) {
                notifyItemRangeRemoved(0, countPreClear);
            }
        }
    }

    public void addItem(T item, boolean notify) {
        if (items == null) {
            return;
        }
        items.add(item);
        if (notify) {
            notifyItemInserted(getItemCount());
        }
    }

    public void addItem(T item) {
        addItem(item, false);
    }

    public void addAll(List<T> itemsToAdd, boolean replace) {
        if (itemsToAdd == null || itemsToAdd.size() == 0) {
            return;
        }
        int currentItemSize = items.size();
        int start = items.size();
        if (replace && items.size() > 0) {
            items.clear();
            notifyItemRangeRemoved(0, currentItemSize);
            start = 0;
        }
        for (T item : itemsToAdd) {
            items.add(item);
        }
        notifyItemRangeInserted(start, items.size());
    }

    public void addItem(int position, T item, boolean notify) {
        if (items == null) {
            return;
        }
        int itemCount = getItemCount();
        position = position > itemCount ? itemCount : position;
        items.add(position, item);
        if (notify) {
            notifyItemInserted(position);
        }
    }

}
