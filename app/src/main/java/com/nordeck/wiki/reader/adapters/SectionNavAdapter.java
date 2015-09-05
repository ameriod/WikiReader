package com.nordeck.wiki.reader.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.adapters.base.NdBaseRecyclerAdapter;
import com.nordeck.wiki.reader.model.Section;

/**
 * Created by parker on 9/5/15.
 */
public class SectionNavAdapter extends NdBaseRecyclerAdapter<Section, SectionNavAdapter.SectionNavHolder> {
    private int mCurrentPosition;

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

    public void setCurrentPosition(int currentPosition) {
        notifyItemChanged(mCurrentPosition);
        this.mCurrentPosition = currentPosition;
        notifyItemChanged(currentPosition);
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }
}
