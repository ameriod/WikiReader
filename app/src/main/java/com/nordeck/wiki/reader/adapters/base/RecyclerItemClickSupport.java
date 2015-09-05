package com.nordeck.wiki.reader.adapters.base;

import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.SoundEffectConstants;
import android.view.View;

import com.nordeck.wiki.reader.R;


public class RecyclerItemClickSupport {
    /**
     * Interface definition for a callback to be invoked when an item in the
     * RecyclerView has been clicked.
     */
    public interface OnItemClickListener {
        /**
         * Callback method to be invoked when an item in the RecyclerView
         * has been clicked.
         *
         * @param parent   The RecyclerView where the click happened.
         * @param view     The view within the RecyclerView that was clicked
         * @param position The position of the view in the adapter.
         * @param id       The row id of the item that was clicked.
         * @return true if the callback consumed the click, false otherwise
         */
        boolean onItemClick(RecyclerView parent, View view, int position, long id);
    }

    /**
     * Interface definition for a callback to be invoked
     * import com.duethealth.lib.component.R;when an item in the
     * RecyclerView has been clicked and held.
     */
    public interface OnItemLongClickListener {
        /**
         * Callback method to be invoked when an item in the RecyclerView
         * has been clicked and held.
         *
         * @param parent   The RecyclerView where the click happened
         * @param view     The view within the RecyclerView that was clicked
         * @param position The position of the view in the list
         * @param id       The row id of the item that was clicked
         * @return true if the callback consumed the long click, false otherwise
         */
        boolean onItemLongClick(RecyclerView parent, View view, int position, long id);
    }

    private final RecyclerView mRecyclerView;
    private final TouchListener mTouchListener;

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    private RecyclerItemClickSupport(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;

        mTouchListener = new TouchListener(recyclerView);
        recyclerView.addOnItemTouchListener(mTouchListener);
    }

    /**
     * Register a callback to be invoked when an item in the
     * RecyclerView has been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an item in the
     * RecyclerView has been clicked and held.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        if (!mRecyclerView.isLongClickable()) {
            mRecyclerView.setLongClickable(true);
        }

        mItemLongClickListener = listener;
    }

    public static RecyclerItemClickSupport addTo(RecyclerView recyclerView) {
        RecyclerItemClickSupport itemClickSupport = from(recyclerView);
        if (itemClickSupport == null) {
            itemClickSupport = new RecyclerItemClickSupport(recyclerView);
            recyclerView.setTag(R.id.nd_recyclerview_item_click_support, itemClickSupport);
        } else {
            // TODO: Log warning
        }

        return itemClickSupport;
    }

    public static void removeFrom(RecyclerView recyclerView) {
        final RecyclerItemClickSupport itemClickSupport = from(recyclerView);
        if (itemClickSupport == null) {
            // TODO: Log warning
            return;
        }

        recyclerView.removeOnItemTouchListener(itemClickSupport.mTouchListener);
        recyclerView.setTag(R.id.nd_recyclerview_item_click_support, null);
    }

    public static RecyclerItemClickSupport from(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return null;
        }

        return (RecyclerItemClickSupport) recyclerView.getTag(R.id.nd_recyclerview_item_click_support);
    }

    private class TouchListener extends RecyclerTouchListener {
        TouchListener(RecyclerView recyclerView) {
            super(recyclerView);
        }

        @Override
        boolean performItemClick(RecyclerView parent, View view, int position, long id) {
            if (mItemClickListener != null) {
                boolean consumed = mItemClickListener.onItemClick(parent, view, position, id);
                if (consumed) {
                    view.playSoundEffect(SoundEffectConstants.CLICK);
                }
                return consumed;
            }

            return false;
        }

        @Override
        boolean performItemLongClick(RecyclerView parent, View view, int position, long id) {
            if (mItemLongClickListener != null) {
                boolean consumed = mItemLongClickListener.onItemLongClick(parent, view, position, id);
                if (consumed) {
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                }
                return consumed;
            }

            return false;
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
