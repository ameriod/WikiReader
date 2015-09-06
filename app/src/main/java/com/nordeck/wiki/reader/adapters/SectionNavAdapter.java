package com.nordeck.wiki.reader.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.adapters.base.NdBaseRecyclerAdapter;
import com.nordeck.wiki.reader.model.ISection;
import com.nordeck.wiki.reader.model.PagesResponse;
import com.nordeck.wiki.reader.model.Section;

import java.util.List;

/**
 * Created by parker on 9/5/15.
 */
public class SectionNavAdapter extends NdBaseRecyclerAdapter<ISection, SectionNavAdapter.SectionNavHolder> {
    private int mCurrentPosition;

    private PagesResponse relatedResponse;

    public SectionNavAdapter(Context context) {
        super(context);
    }

    @Override
    public SectionNavHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_article_nav, parent, false);
        return new SectionNavHolder(view);
    }

    @Override
    public void onBindViewHolder(SectionNavHolder holder, int position) {
        holder.tvName.setText(getItem(position).getTitle());
        holder.itemView.setSelected(position == mCurrentPosition);
        if (holder.itemView.isSelected()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.section_nav_selected_color));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        }
    }

    class SectionNavHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public SectionNavHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.list_item_article_nav_tv_title);
        }
    }

    @Override
    public void addAll(List<ISection> itemsToAdd, boolean replace) {
        super.addAll(itemsToAdd, replace);
        if (replace) {
            addRelatedArticles(relatedResponse);
        }
    }

    public void addRelatedArticles(@Nullable PagesResponse relatedResponse) {
        this.relatedResponse = relatedResponse;
        if (relatedResponse != null && relatedResponse.getItems() != null && relatedResponse.getItems().size() > 0) {
            ISection section = Section.newInstance(context.getString(R.string.article_viewer_related));
            addItem(section);
            notifyItemInserted(getItemCount() - 1);
        }
    }

    public void setCurrentPosition(int currentPosition) {
        if (currentPosition < getItemCount()) {
            notifyItemChanged(mCurrentPosition);
            this.mCurrentPosition = currentPosition;
            notifyItemChanged(currentPosition);
        } else {
            notifyItemChanged(mCurrentPosition);
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }
}
