package com.nordeck.wiki.reader.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.Utils;
import com.nordeck.wiki.reader.model.IPage;
import com.squareup.picasso.Picasso;

class PageDetailViewHolder extends RecyclerView.ViewHolder {
    ImageView iv;
    TextView tvTitle;
    TextView tvSummary;

    public PageDetailViewHolder(View itemView) {
        super(itemView);
        iv = (ImageView) itemView.findViewById(R.id.list_item_related_article_iv);
        tvTitle = (TextView) itemView.findViewById(R.id.list_item_related_article_tv_title);
        tvSummary = (TextView) itemView.findViewById(R.id.list_item_related_article_tv_summary);
    }

    public void setupPage(IPage page, Context context) {
        tvTitle.setText(page.getTitle());
        tvSummary.setText(page.getSummary());
        if (TextUtils.isEmpty(page.getImageUrl())) {
            Utils.setViewVisibility(iv, false);
        } else {
            Utils.setViewVisibility(iv, true);
            Picasso.with(context.getApplicationContext()).load(page.getImageUrl()).into(iv);
        }
    }

}