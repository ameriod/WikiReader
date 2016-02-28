package com.nordeck.wiki.reader.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.nordeck.lib.core.adapter.NdBaseRecyclerAdapter;
import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.model.IPage;

/**
 * Created by parker on 9/5/15.
 */
public class PageDetailAdapter extends NdBaseRecyclerAdapter<IPage, PageDetailViewHolder> {
    public PageDetailAdapter(@NonNull Context context, @Nullable OnItemClickListener<IPage> listener) {
        super(context, listener);
    }

    @Override
    public PageDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PageDetailViewHolder(inflater.inflate(R.layout.list_item_related_article, parent, false));
    }

    @Override
    public void onBindViewHolder(PageDetailViewHolder holder, int position) {
        IPage page = getItem(position);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(page);
        holder.setupPage(getItem(position), context);
    }
}
