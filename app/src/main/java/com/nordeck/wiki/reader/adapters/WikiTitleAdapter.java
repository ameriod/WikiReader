package com.nordeck.wiki.reader.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nordeck.lib.core.adapter.NdBaseRecyclerAdapter;
import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.model.Wiki;

/**
 * Created by parker on 9/4/15.
 */
public class WikiTitleAdapter extends NdBaseRecyclerAdapter<Wiki, WikiTitleAdapter.ItemViewHolder>
        implements View.OnClickListener {

    public WikiTitleAdapter(@NonNull Context context, @Nullable OnItemClickListener<Wiki> listener) {
        super(context, listener);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_name, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Wiki wiki = getItem(position);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(wiki);
        holder.tvName.setText(wiki.getName());
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.list_item_name_tv);
        }
    }

}
