package com.nordeck.wiki.reader.adapters.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class NdBaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
        implements View.OnClickListener {

    protected LayoutInflater inflater;
    protected Context context;
    protected ArrayList<T> items;
    @Nullable
    protected OnItemClickListener<T> listener;

    public NdBaseRecyclerAdapter(@NonNull Context context, @Nullable OnItemClickListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.items = new ArrayList<>();
        this.listener = listener;
    }

    public interface OnItemClickListener<T> {
        void onItemClicked(@NonNull T item);
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

    public int getPosition(@NonNull T item) {
        if (items != null) {
            for (int i = 0, size = items.size(); i < size; i++) {
                T checkItem = items.get(i);
                if (checkItem.equals(item)) {
                    return i;
                }
            }
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            Object obj = v.getTag();
            if (obj != null) {
                listener.onItemClicked((T) obj);
            }
        }
    }
}
