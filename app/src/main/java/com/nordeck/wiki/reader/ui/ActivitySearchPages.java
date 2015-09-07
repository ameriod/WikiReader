package com.nordeck.wiki.reader.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.SelectedWiki;
import com.nordeck.wiki.reader.adapters.PageTitleAdapter;
import com.nordeck.wiki.reader.adapters.base.NdDividerItemDecoration;
import com.nordeck.wiki.reader.adapters.base.RecyclerItemClickSupport;
import com.nordeck.wiki.reader.model.IPage;
import com.nordeck.wiki.reader.model.SearchResponse;
import com.nordeck.wiki.reader.presenters.SearchPagePresenter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * TODO remove transition
 * <p/>
 * Created by parker on 9/6/15.
 */
public class ActivitySearchPages extends BaseActivity implements ISearchPagesView, RecyclerItemClickSupport
        .OnItemClickListener, SearchView.OnQueryTextListener {

    private static final int MIN_SEARCH_LENGTH = 3;

    private static Intent getLaunchIntent(Context context) {
        Intent intent = new Intent(context, ActivitySearchPages.class);
        return intent;
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
        RecyclerItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(this);
        mAdapter = new PageTitleAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new SearchPagePresenter(SelectedWiki.getInstance().getSelectedWiki().getUrl());
        mPresenter.bindView(this);
        mPresenter.onCreate(savedInstanceState);

        if (mPresenter.getResponse() != null) {
            onResultsFetched(mPresenter.getResponse());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search_page, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setQueryHint(getString(R.string.action_search_pages_hint, SelectedWiki.getInstance()
                .getSelectedWiki().getTitle()));
        mSearchView.onActionViewExpanded();
        if (!TextUtils.isEmpty(mPresenter.getSearchQuery())) {
            mSearchView.setQuery(mPresenter.getSearchQuery(), false);
        }
        mSearchView.setOnQueryTextListener(this);
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
        mPresenter.onDestroy();
        mPresenter.unbindView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResultsFetched(SearchResponse response) {
        mAdapter.addAll(new ArrayList<IPage>(response.getItems()), true);
    }

    @Override
    public void displayError(@Nullable String error) {
        super.displayError(error);
        mAdapter.clear(true);
    }

    @Override
    public boolean onItemClick(RecyclerView parent, View view, int position, long id) {
        IPage page = mAdapter.getItem(position);
        ActivityArticleViewer.launchActivity(this, page.getId());
        mSearchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText.trim()) && newText.length() >= MIN_SEARCH_LENGTH) {
            mPresenter.fetchSearchResults(newText.trim());
            return true;
        } else {
            mAdapter.clear(true);
        }
        return false;
    }
}
