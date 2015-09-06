package com.nordeck.wiki.reader.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.model.Image;
import com.nordeck.wiki.reader.model.Section;
import com.nordeck.wiki.reader.adapters.base.NdBaseRecyclerAdapter;
import com.nordeck.wiki.reader.views.HtmlTagHandler;
import com.squareup.picasso.Picasso;

import java.util.List;

import timber.log.Timber;

/**
 * TODO make the content expandable?
 * <p/>
 * Created by parker on 9/4/15.
 */
public class ContentViewerAdapter extends NdBaseRecyclerAdapter<Section, ContentViewerAdapter.SectionViewHolder>
        implements View.OnClickListener {

    private SparseArray<Spanned> mSpanCache;

    public ContentViewerAdapter(Context context) {
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

        if (item.getImages() != null && item.getImages().size() > 0) {
            addImagesToView(holder.containerImages, item.getImages());
            showView(holder.hsImages, true);
            showView(holder.containerImages, true);
        } else {
            showView(holder.containerImages, false);
            showView(holder.hsImages, false);
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
        HorizontalScrollView hsImages;
        LinearLayout containerImages;

        public SectionViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.list_item_article_section_tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.list_item_article_section_tv_content);
            hsImages = (HorizontalScrollView) itemView.findViewById(R.id.list_item_article_section_hs_images);
            containerImages = (LinearLayout) itemView.findViewById(R.id.list_item_article_section_container_images);
        }
    }

    private void showView(View v, boolean show) {
        if (show) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    private void addImagesToView(LinearLayout container, List<Image> imageList) {
        // check to make sure there are enough image views to display in
        int containerCount = container.getChildCount();
        int imagesSize = imageList.size();
        int diff = containerCount - imagesSize;
        if (container.getChildCount() != imageList.size()) {
            for (int i = diff; i <= 0; i++) {
                // add more image views
                container.addView(createImageView());
            }
        } else if (diff < 0) {
            // Too many image views
            diff = Math.abs(diff);
            for (int i = diff; i <= 0; i--) {
                View v = container.getChildAt(i);
                showView(v, false);
            }
        }
        for (int i = 0; i < imagesSize; i++) {
            Image image = imageList.get(i);
            ImageView iv = (ImageView) container.getChildAt(i);
            if (i == imagesSize - 1) {
                // build in item padding will pad 8dps
                iv.setPadding(0, 0, 0, 0);
            } else {
                iv.setPadding(0, 0, context.getResources().getDimensionPixelOffset(R.dimen.padding), 0);
            }
            iv.setTag(image.getCaption());
            iv.setOnClickListener(this);
            Picasso.with(context.getApplicationContext()).load(image.getSrc()).into(iv);
            showView(iv, true);
        }
    }

    private ImageView createImageView() {
        ImageView iv = new ImageView(context);
        return iv;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, v.getTag().toString(), Toast.LENGTH_SHORT).show();
    }
}
