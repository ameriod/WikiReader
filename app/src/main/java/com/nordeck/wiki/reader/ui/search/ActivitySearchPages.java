package com.nordeck.wiki.reader.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;

import com.nordeck.lib.core.adapter.NdDividerItemDecoration;
import com.nordeck.lib.core.rx.NdObservableScheduler;
import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.SelectedWiki;
import com.nordeck.wiki.reader.adapters.PageTitleAdapter;
import com.nordeck.wiki.reader.model.IPage;
import com.nordeck.wiki.reader.ui.viewer.ActivityArticleViewer;
import com.nordeck.wiki.reader.ui.BaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * TODO remove transition
 * <p/>
 * Created by parker on 9/6/15.
 */
public class ActivitySearchPages extends BaseActivity implements PageTitleAdapter
        .OnItemClickListener<IPage>, SearchView.OnQueryTextListener, SearchPageContract.View {

    private static final int MIN_SEARCH_LENGTH = 3;

    private static Intent getLaunchIntent(Context context) {
        return new Intent(context, ActivitySearchPages.class);
    }

    public static void launchActivity(Activity activity) {
        activity.startActivity(getLaunchIntent(activity));
    }

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private SearchView mSearchView;

    private SearchPagePresenter mPresenter;

    private PageTitleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupActionBar();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new NdDividerItemDecoration(this, NdDividerItemDecoration.VERTICAL_LIST));
        mAdapter = new PageTitleAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new SearchPagePresenter(NdObservableScheduler.SUBSCRIBE_IO_OBSERVE_ANDROID_MAIN,
                new SearchPageInteractor());
        mPresenter.bindView(this, savedInstanceState);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search_page, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setQueryHint(getString(R.string.action_search_pages_hint, SelectedWiki.getInstance()
                .getSelectedWiki().getTitle()));
        mSearchView.onActionViewExpanded();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQuery(mPresenter.getSearchQuery(), true);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unbindView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setItems(@Nullable List<IPage> items) {
        mAdapter.addAll(items, true);
    }

    @Override
    public void displayError(@Nullable String error) {
        super.displayError(error);
        mAdapter.clear(true);
    }

    @Override
    public void onItemClicked(@NonNull IPage page) {
        ActivityArticleViewer.launchActivity(this, page.getId());
        mSearchView.clearFocus();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText.trim()) && newText.length() >= MIN_SEARCH_LENGTH) {
            mPresenter.getSearchResults(newText.trim());
            return true;
        } else {
            mAdapter.clear(true);
        }
        return false;
    }
}
