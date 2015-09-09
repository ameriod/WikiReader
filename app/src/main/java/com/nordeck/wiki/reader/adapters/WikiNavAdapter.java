package com.nordeck.wiki.reader.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.SelectedWiki;
import com.nordeck.wiki.reader.Utils;
import com.nordeck.wiki.reader.adapters.base.NdBaseRecyclerAdapter;
import com.nordeck.wiki.reader.model.WikiDetail;
import com.squareup.picasso.Picasso;

/**
 * Created by parker on 9/7/15.
 */
public class WikiNavAdapter extends NdBaseRecyclerAdapter<String, RecyclerView.ViewHolder> {
    public WikiNavAdapter(Context context) {
        super(context);
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
                Utils.setViewVisibility(headerHolder.ivLogo, true);
            } else {
                Utils.setViewVisibility(headerHolder.ivLogo, false);
            }
        } else {
            ((ItemViewHolder) holder).tvName.setText(getItem(position));
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
