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
import com.nordeck.wiki.reader.adapters.WikiTitleAdapter;
import com.nordeck.wiki.reader.adapters.base.NdDividerItemDecoration;
import com.nordeck.wiki.reader.adapters.base.RecyclerItemClickSupport;
import com.nordeck.wiki.reader.model.Wiki;
import com.nordeck.wiki.reader.model.WikiResponse;
import com.nordeck.wiki.reader.presenters.WikiPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by parker on 9/6/15.
 */
public class ActivityWikis extends BaseActivity implements IWikiView, RecyclerItemClickSupport.OnItemClickListener,
        SearchView.OnQueryTextListener {

    private static final int MIN_SEARCH_LENGTH = 3;

    private WikiPresenter mPresenter;

    public static Intent getLaunchIntent(Context context) {
        Intent intent = new Intent(context, ActivityWikis.class);
        return intent;
    }

    public static void launchActivity(Activity activity) {
        activity.startActivity(getLaunchIntent(activity));
    }

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private WikiTitleAdapter mAdapter;
    private SearchView mSearchView;

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
        mAdapter = new WikiTitleAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new WikiPresenter();
        mPresenter.bindView(this);
        mPresenter.onCreate(savedInstanceState);

        if (!TextUtils.isEmpty(mPresenter.getSearchQuery())) {
            mSearchView.setQuery(mPresenter.getSearchQuery(), false);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search_wiki, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setQueryHint(getString(R.string.action_search_wikis));
        if (!TextUtils.isEmpty(mPresenter.getSearchQuery())) {
            mSearchView.setQuery(mPresenter.getSearchQuery(), false);
            onSearchWikisFetched(mPresenter.getSearchResponse());
        }
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (TextUtils.isEmpty(mPresenter.getSearchQuery())) {
            mPresenter.fetchTopWikis(false);
        }
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
    public void displayError(@Nullable String error) {
        super.displayError(error);
        mAdapter.clear(true);
    }

    @Override
    public void onTopWikisFetched(WikiResponse response) {
        mAdapter.addAll(response.getItems(), true);
    }

    @Override
    public void onSearchWikisFetched(WikiResponse response) {
        mAdapter.addAll(response.getItems(), true);
    }

    @Override
    public boolean onItemClick(RecyclerView parent, View view, int position, long id) {
        Wiki wiki = mAdapter.getItem(position);
        // TODO impl saving of the wiki when
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText.trim()) && newText.length() >= MIN_SEARCH_LENGTH) {
            mPresenter.preformWikiSearch(newText.trim());
            return true;
        } else {
            if (mPresenter.getTopResponse() != null && !TextUtils.isEmpty(mPresenter.getSearchQuery())) {
                mPresenter.setSearchQuery(null);
                mAdapter.addAll(mPresenter.getTopResponse().getItems(), true);
            }
        }
        return false;
    }
}
