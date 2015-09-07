package com.nordeck.wiki.reader.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.adapters.base.NdBaseRecyclerAdapter;
import com.nordeck.wiki.reader.model.Wiki;

/**
 * Created by parker on 9/4/15.
 */
public class WikiTitleAdapter extends NdBaseRecyclerAdapter<Wiki, WikiTitleAdapter.ItemViewHolder> {
    public WikiTitleAdapter(Context context) {
        super(context);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_name, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.tvName.setText(getItem(position).getName());
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.list_item_name_tv);
        }
    }

}
