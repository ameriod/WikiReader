package com.nordeck.wiki.reader.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nordeck.lib.core.NdUtils;
import com.nordeck.lib.core.mvp.NdView;
import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.ui.top.ActivityTopPages;
import com.nordeck.wiki.reader.ui.viewer.ActivityArticleViewer;
import com.nordeck.wiki.reader.ui.wiki.ActivityWikis;

/**
 * Created by parker on 9/4/15.
 */
public abstract class BaseActivity extends AppCompatActivity implements NdView, WikiDrawerHelper
        .OnNavItemSelectedListener {

    private Toolbar mToolbar;
    protected View mLoading;
    protected RecyclerView mLeftDrawer;
    protected RecyclerView mRightDrawer;
    protected DrawerLayout mDrawerLayout;
    protected WikiDrawerHelper mDrawerHelper;

    protected void showToastMessage(@Nullable String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    protected void setupActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mLoading = findViewById(R.id.loading);
    }

    protected void setupDrawers() {
        mLeftDrawer = (RecyclerView) findViewById(R.id.drawer_left);
        mRightDrawer = (RecyclerView) findViewById(R.id.drawer_right);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerHelper = new WikiDrawerHelper(this, mDrawerLayout, mLeftDrawer, mRightDrawer);
        mDrawerHelper.setListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerHelper != null) {
            mDrawerHelper.onCreate();
            mDrawerHelper.onPostCreate();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerHelper != null) {
            mDrawerHelper.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerHelper != null) {
            boolean selected = mDrawerHelper.onOptionsItemSelected(item);
            if (selected) {
                return selected;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    public RecyclerView getLeftDrawer() {
        return mLeftDrawer;
    }

    @Nullable
    public RecyclerView getRightDrawer() {
        return mRightDrawer;
    }

    @Nullable
    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public WikiDrawerHelper getDrawerHelper() {
        return mDrawerHelper;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void onNavItemSelected(@NonNull String item) {
        if (TextUtils.equals(item, getString(R.string.nav_chose_new_wiki))) {
            ActivityWikis.launchActivity(this);
        } else if (TextUtils.equals(item, getString(R.string.nav_chose_random))) {
            ActivityArticleViewer.launchActivityRandom(this);
        } else if (TextUtils.equals(item, getString(R.string.nav_chose_top))) {
            ActivityTopPages.launchActivity(this);
        }
    }

    @Override
    public void showProgressIndicator(boolean show) {
        NdUtils.setViewVisibility(mLoading, show);
    }

    @Override
    public void displayError(@Nullable String error) {
        showToastMessage(error);
    }
}
