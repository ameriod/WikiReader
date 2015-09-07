package com.nordeck.wiki.reader.ui;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.Utils;
import com.nordeck.wiki.reader.presenters.NdView;

/**
 * Created by parker on 9/4/15.
 */
public abstract class BaseActivity extends AppCompatActivity implements NdView {

    private Toolbar mToolbar;
    protected View mLoading;

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

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void showProgressIndicator(boolean show) {
        Utils.setViewVisibility(mLoading, show);
    }

    @Override
    public void displayError(@Nullable String error) {
        showToastMessage(error);
    }
}
