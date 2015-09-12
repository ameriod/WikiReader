package com.nordeck.wiki.reader.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxSearchView;
import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.adapters.base.NdBaseRecyclerAdapter;
import com.nordeck.wiki.reader.model.IPage;

/**
 * Created by parker on 9/4/15.
 */
public class PageTitleAdapter extends NdBaseRecyclerAdapter<IPage, PageTitleAdapter.ItemViewHolder> {
    public PageTitleAdapter(Context context) {
        super(context);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_name, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.tvName.setText(getItem(position).getTitle());
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.list_item_name_tv);
        }
    }

}
