package com.nordeck.wiki.reader.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.model.Section;
import com.nordeck.wiki.reader.adapters.base.NdBaseRecyclerAdapter;
import com.nordeck.wiki.reader.views.HtmlTagHandler;

import timber.log.Timber;

/**
 * TODO make the content expandable?
 * <p/>
 * Created by parker on 9/4/15.
 */
public class SectionContentViewerAdapter extends NdBaseRecyclerAdapter<Section, SectionContentViewerAdapter.SectionViewHolder> {

    private SparseArray<Spanned> mSpanCache;

    public SectionContentViewerAdapter(Context context) {
        super(context);
    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_article_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {
        Section item = getItem(position);
        holder.tvTitle.setText(item.getTitle());
        // Some sections do not have titles
        showView(holder.tvTitle, !TextUtils.isEmpty(item.getTitle()));
        if (item.isContentTitle()) {
            // no content so hide the tv
            showView(holder.tvContent, false);
        } else if (item.isContentParagraph()) {
            holder.tvContent.setText(item.getParagraphStr());
            showView(holder.tvContent, true);
        } else if (item.isContentList()) {
            holder.tvContent.setText(getSpan(position, item.getListStr()));
            showView(holder.tvContent, true);
        } else {
            showView(holder.tvContent, false);
            Timber.e("Unsupported content type section: " + item);
        }
    }

    /**
     * Either creates a span (make the ui stutter) or fetches it from the {@link #mSpanCache}
     *
     * @param position
     * @param html
     * @return
     */
    private Spanned getSpan(int position, String html) {
        if (mSpanCache == null) {
            mSpanCache = new SparseArray<>();
        }
        Spanned span = mSpanCache.get(position);
        if (span == null) {
            span = Html.fromHtml(html, null, new HtmlTagHandler());
        }
        return span;
    }

    class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvContent;

        public SectionViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.list_item_article_section_tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.list_item_article_section_tv_content);
        }
    }

    private void showView(View v, boolean show) {
        if (show) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }
}
