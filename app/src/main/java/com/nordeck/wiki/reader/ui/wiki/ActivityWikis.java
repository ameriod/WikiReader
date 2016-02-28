package com.nordeck.wiki.reader.ui.wiki;

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
import com.nordeck.wiki.reader.adapters.WikiTitleAdapter;
import com.nordeck.wiki.reader.model.Wiki;
import com.nordeck.wiki.reader.model.WikiDetail;
import com.nordeck.wiki.reader.model.WikiResponse;
import com.nordeck.wiki.reader.ui.BaseActivity;
import com.nordeck.wiki.reader.ui.top.ActivityTopPages;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by parker on 9/6/15.
 */
public class ActivityWikis extends BaseActivity implements
        WikiTitleAdapter.OnItemClickListener<Wiki>, SearchView.OnQueryTextListener,
        DialogFragmentWikiDetail.OnWikiSelectedListener, WikiDetailContract.View, WikiContract.View {

    private static final int MIN_SEARCH_LENGTH = 3;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private WikiContract.Presenter mPresenter;
    private WikiDetailContract.Presenter mDetailPresenter;
    private boolean mIsSelectable;
    private WikiTitleAdapter mAdapter;
    private SearchView mSearchView;

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, ActivityWikis.class);
    }

    public static void launchActivity(Activity activity) {
        activity.startActivity(getLaunchIntent(activity));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wiki);
        ButterKnife.bind(this);
        setupActionBar();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new NdDividerItemDecoration(this, NdDividerItemDecoration.VERTICAL_LIST));
        mAdapter = new WikiTitleAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new WikiPresenter(NdObservableScheduler.SUBSCRIBE_IO_OBSERVE_ANDROID_MAIN, new WikiInteractor());
        mPresenter.bindView(this, savedInstanceState);
        mDetailPresenter = new WikiDetailPresenter(NdObservableScheduler.SUBSCRIBE_IO_OBSERVE_ANDROID_MAIN,
                new WikiDetailInteractor());
        mDetailPresenter.bindView(this, savedInstanceState);

        if (!TextUtils.isEmpty(mPresenter.getSearchQuery())) {
            mSearchView.setQuery(mPresenter.getSearchQuery(), false);
        }
        mIsSelectable = true;
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
        mDetailPresenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unbindView();
        mDetailPresenter.unbindView();
        ButterKnife.unbind(this);
    }

    @Override
    public void displayError(@Nullable String error) {
        super.displayError(error);
        mAdapter.clear(true);
    }

    @Override
    public void onWikiDetailFetched(@NonNull WikiDetail detail) {
        DialogFragmentWikiDetail.newInstance(detail, this).show(getFragmentManager(), DialogFragmentWikiDetail.TAG);
        mIsSelectable = true;
    }

    @Override
    public void onWikiSelected(WikiDetail wiki) {
        // Set the selected wiki
        SelectedWiki.getInstance().setSelectedWiki(wiki, getApplicationContext());
        Intent intent = ActivityTopPages.getLaunchIntent(getApplicationContext());
        // Remove clear all activities to remove the old wiki stuff in the back stack.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void displayErrorWikiDetail(@NonNull String error) {
        mIsSelectable = true;
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
    public void onItemClicked(@NonNull Wiki wiki) {
        if (mIsSelectable) {
            mIsSelectable = false;
            mDetailPresenter.fetchWikibyId(wiki.getId());
        }
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
