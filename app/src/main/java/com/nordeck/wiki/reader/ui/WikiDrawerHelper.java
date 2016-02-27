package com.nordeck.wiki.reader.ui;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.adapters.WikiNavAdapter;
import com.nordeck.wiki.reader.adapters.base.NdDividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by parker on 9/7/15.
 */
public class WikiDrawerHelper implements WikiNavAdapter.OnItemClickListener<String> {

    public interface OnNavItemSelectedListener {
        void onNavItemSelected(@NonNull String item);
    }

    private OnNavItemSelectedListener mListener;

    private DrawerLayout mDrawerLayout;

    private RecyclerView mLeftDrawer;
    private RecyclerView mRightDrawer;

    private WikiNavAdapter mNavAdapter;

    private ActionBarDrawerToggle mToggle;
    private AppCompatActivity mActivity;

    public WikiDrawerHelper(@NonNull AppCompatActivity activity, @NonNull DrawerLayout drawerLayout, @Nullable
    RecyclerView leftDrawer, @Nullable RecyclerView rightDrawer) {
        this.mActivity = activity;
        this.mDrawerLayout = drawerLayout;
        this.mRightDrawer = rightDrawer;
        this.mLeftDrawer = leftDrawer;
        if (mLeftDrawer != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mLeftDrawer.setLayoutManager(linearLayoutManager);
            mLeftDrawer.addItemDecoration(new NdDividerItemDecoration(activity, NdDividerItemDecoration.VERTICAL_LIST));
            mNavAdapter = new WikiNavAdapter(activity, this);
            mLeftDrawer.setAdapter(mNavAdapter);
            ArrayList<String> navItems = new ArrayList<>(Arrays.asList(activity.getApplication().getResources()
                    .getStringArray(R.array.wiki_nav_list)));
            // account for the header
            navItems.add(0, null);
            mNavAdapter.addAll(navItems, true);
        }
    }

    public void setListener(OnNavItemSelectedListener listener) {
        this.mListener = listener;
    }

    /**
     * Only creates a toggle if there is a left drawer
     */
    public void onCreate() {
        if (mLeftDrawer != null) {
            // Only toggle for the left drawer
            mToggle = new ActionBarDrawerToggle(mActivity, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    if (drawerView == mLeftDrawer) {
                        super.onDrawerClosed(drawerView);
                    }
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    if (drawerView == mLeftDrawer) {
                        super.onDrawerOpened(drawerView);
                    }
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    if (drawerView == mLeftDrawer) {
                        super.onDrawerSlide(drawerView, slideOffset);
                    }
                }
            };
            mDrawerLayout.addDrawerListener(mToggle);

            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        }

    }

    public void onPostCreate() {
        if (mToggle != null) {
            mToggle.syncState();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (mToggle != null) {
            mToggle.onConfigurationChanged(newConfig);
        }
    }

    /**
     * False do the regular action items
     *
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle != null) {
            return mToggle.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onItemClicked(@NonNull String item) {
        if (mListener != null) {
            mListener.onNavItemSelected(item);
        }
    }
}
