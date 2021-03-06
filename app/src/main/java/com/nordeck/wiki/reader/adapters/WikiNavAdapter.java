package com.nordeck.wiki.reader.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nordeck.lib.core.NdUtils;
import com.nordeck.lib.core.adapter.NdBaseRecyclerAdapter;
import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.SelectedWiki;
import com.nordeck.wiki.reader.model.WikiDetail;
import com.squareup.picasso.Picasso;

/**
 * Created by parker on 9/7/15.
 */
public class WikiNavAdapter extends NdBaseRecyclerAdapter<String, RecyclerView.ViewHolder> implements
        View.OnClickListener {

    public WikiNavAdapter(@NonNull Context context, @Nullable OnItemClickListener<String> listener) {
        super(context, listener);
    }

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_ITEM = 0;

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(inflater.inflate(R.layout.list_item_wiki_nav_header, parent, false));
        }
        return new ItemViewHolder(inflater.inflate(R.layout.list_item_wiki_nav, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            WikiDetail detail = SelectedWiki.getInstance().getSelectedWiki();
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.tvName.setText(detail.getTitle());
            if (!TextUtils.isEmpty(detail.getImage())) {
                Picasso.with(context.getApplicationContext()).load(detail.getImage()).into(headerHolder.ivLogo);
                NdUtils.setViewVisibility(headerHolder.ivLogo, true);
            } else {
                NdUtils.setViewVisibility(headerHolder.ivLogo, false);
            }
        } else {
            String item = getItem(position);
            holder.itemView.setOnClickListener(this);
            holder.itemView.setTag(item);
            ((ItemViewHolder) holder).tvName.setText(item);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.list_item_wiki_nav_tv);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivLogo;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.list_item_wiki_nav_tv_name);
            this.ivLogo = (ImageView) itemView.findViewById(R.id.list_item_wiki_nav_iv);
        }
    }
}
