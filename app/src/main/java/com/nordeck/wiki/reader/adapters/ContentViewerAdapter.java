package com.nordeck.wiki.reader.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
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
import com.nordeck.wiki.reader.Utils;
import com.nordeck.wiki.reader.adapters.base.NdBaseRecyclerAdapter;
import com.nordeck.wiki.reader.model.IPage;
import com.nordeck.wiki.reader.model.ISection;
import com.nordeck.wiki.reader.model.Image;
import com.nordeck.wiki.reader.model.PageRelated;
import com.nordeck.wiki.reader.model.RelatedResponse;
import com.nordeck.wiki.reader.model.Section;
import com.nordeck.wiki.reader.views.HtmlTagHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by parker on 9/4/15.
 */
public class ContentViewerAdapter extends NdBaseRecyclerAdapter<ISection, RecyclerView.ViewHolder>
        implements View.OnClickListener {

    public interface OnClickRelatedArticleListener {
        void onClickArticle(IPage page);
    }

    private OnClickRelatedArticleListener mArticleListener;

    private static final int TYPE_SECTION = 0;
    private static final int TYPE_RELATED = 1;

    private RelatedResponse mRelatedResponse;

    private SparseArray<Spanned> mSpanCache;

    public ContentViewerAdapter(Context context, OnClickRelatedArticleListener listener) {
        super(context, null);
        mArticleListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        ISection section = getItem(position);
        if (section instanceof PageRelated) {
            return TYPE_RELATED;
        }
        return TYPE_SECTION;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_RELATED) {
            return new PageDetailViewHolder(inflater.inflate(R.layout.list_item_related_article, parent, false));
        }
        return new SectionViewHolder(inflater.inflate(R.layout.list_item_article_section, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ISection iSection = getItem(position);

        int viewType = getItemViewType(position);

        if (viewType == TYPE_SECTION) {
            Section item = (Section) iSection;
            SectionViewHolder sectionHolder = (SectionViewHolder) holder;

            sectionHolder.tvTitle.setText(item.getTitle());
            // Some sections do not have titles
            Utils.setViewVisibility(sectionHolder.tvTitle, !TextUtils.isEmpty(item.getTitle()));
            if (item.isContentTitle()) {
                // no content so hide the tv
                Utils.setViewVisibility(sectionHolder.tvContent, false);
            } else if (item.isContentParagraph()) {
                sectionHolder.tvContent.setText(item.getParagraphStr());
                Utils.setViewVisibility(sectionHolder.tvContent, true);
            } else if (item.isContentList()) {
                sectionHolder.tvContent.setText(getSpan(position, item.getListStr()));
                Utils.setViewVisibility(sectionHolder.tvContent, true);
            } else {
                Utils.setViewVisibility(sectionHolder.tvContent, false);
                Timber.e("Unsupported content type section: " + item);
            }

            if (item.getImages() != null && item.getImages().size() > 0) {
                addImagesToView(sectionHolder.containerImages, item.getImages());
                Utils.setViewVisibility(sectionHolder.hsImages, true);
                Utils.setViewVisibility(sectionHolder.containerImages, true);
            } else {
                Utils.setViewVisibility(sectionHolder.containerImages, false);
                Utils.setViewVisibility(sectionHolder.hsImages, false);
            }
        } else if (viewType == TYPE_RELATED) {
            IPage page = (IPage) iSection;
            PageDetailViewHolder holderPage = (PageDetailViewHolder) holder;
            holderPage.setupPage(page, context.getApplicationContext());
            holderPage.itemView.setTag(page);
            holderPage.itemView.setOnClickListener(this);
        }

    }

    @Override
    public void addAll(List<ISection> itemsToAdd, boolean replace) {
        super.addAll(itemsToAdd, replace);
        // re-add the response if already added
        addRelatedArticles(mRelatedResponse);
    }

    public void addRelatedArticles(@Nullable RelatedResponse response) {
        mRelatedResponse = response;
        if (response != null && response.getItems() != null && response.getItems().size() > 0) {
            List<ISection> relatedSections = new ArrayList<>();
            ISection sectionTitle = Section.newInstance(context.getString(R.string.article_viewer_related));
            relatedSections.add(sectionTitle);
            relatedSections.addAll(response.getItems());
            int currentCount = getItemCount();
            for (int i = 0, size = relatedSections.size(); i < size; i++) {
                ISection section = relatedSections.get(i);
                addItem(section, false);
                notifyItemInserted(currentCount + i);
            }
        } else {
            Timber.d("RelatedPagesResponse is null");
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

    private class SectionViewHolder extends RecyclerView.ViewHolder {
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
                Utils.setViewVisibility(v, false);
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
            Utils.setViewVisibility(iv, true);
        }
    }

    private ImageView createImageView() {
        ImageView iv = new ImageView(context);
        return iv;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageView) {
            Object tag = v.getTag();
            if (tag != null) {
                String caption = v.getTag().toString();
                if (!TextUtils.isEmpty(caption)) {
                    Toast.makeText(context.getApplicationContext(), caption, Toast.LENGTH_SHORT).show();
                }
            }
        } else if (v.getTag() instanceof PageRelated) {
            IPage page = (PageRelated) v.getTag();
            mArticleListener.onClickArticle(page);
        }
    }
}
