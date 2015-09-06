package com.nordeck.wiki.reader.adapters;

import android.content.Context;
import android.view.ViewGroup;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.adapters.base.NdBaseRecyclerAdapter;
import com.nordeck.wiki.reader.model.IPage;

/**
 * Created by parker on 9/5/15.
 */
public class PageDetailAdapter extends NdBaseRecyclerAdapter<IPage, PageDetailViewHolder> {
    public PageDetailAdapter(Context context) {
        super(context);
    }

    @Override
    public PageDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PageDetailViewHolder(inflater.inflate(R.layout.list_item_related_article, parent, false));
    }

    @Override
    public void onBindViewHolder(PageDetailViewHolder holder, int position) {
        holder.setupPage(getItem(position), context.getApplicationContext());
    }
}
