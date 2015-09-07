package com.nordeck.wiki.reader.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.adapters.base.NdBaseRecyclerAdapter;

/**
 * Created by parker on 9/7/15.
 */
public class WikiNavAdapter extends NdBaseRecyclerAdapter<String, WikiNavAdapter.ItemViewHolder> {
    public WikiNavAdapter(Context context) {
        super(context);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(inflater.inflate(R.layout.list_item_wiki_nav, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.tvName.setText(getItem(position));
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.list_item_wiki_nav_tv);
        }
    }
}
