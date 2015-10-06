package com.nordeck.wiki.reader.ui;

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
import android.view.View;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.SelectedWiki;
import com.nordeck.wiki.reader.adapters.WikiTitleAdapter;
import com.nordeck.wiki.reader.adapters.base.NdDividerItemDecoration;
import com.nordeck.wiki.reader.adapters.base.RecyclerItemClickSupport;
import com.nordeck.wiki.reader.model.Wiki;
import com.nordeck.wiki.reader.model.WikiDetail;
import com.nordeck.wiki.reader.model.WikiResponse;
import com.nordeck.wiki.reader.presenters.WikiDetailPresenter;
import com.nordeck.wiki.reader.presenters.WikiPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.nordeck.lib.base.presenter.IPresenterFactory;

/**
 * Created by parker on 9/6/15.
 */
public class ActivityWikis extends BaseActivity<IWikiView, WikiPresenter> implements IWikiView, IWikiDetailView,
        RecyclerItemClickSupport.OnItemClickListener, SearchView.OnQueryTextListener, DialogFragmentWikiDetail
                .OnWikiSelectedListener {

    private static final int MIN_SEARCH_LENGTH = 3;

    private WikiDetailPresenter mDetailPresenter;

    private boolean mIsSelectable;

    public static Intent getLaunchIntent(Context context) {
        Intent intent = new Intent(context, ActivityWikis.class);
        return intent;
    }

    public static void launchActivity(Activity activity) {
        activity.startActivity(getLaunchIntent(activity));
    }

    @Override
    protected IPresenterFactory<WikiPresenter> getPresenterFactory() {
        return new IPresenterFactory<WikiPresenter>() {
            @NonNull
            @Override
            public WikiPresenter createPresenter() {
                return new WikiPresenter();
            }
        };
    }

    @Override
    protected String getPresenterTag() {
        return null;
    }

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private WikiTitleAdapter mAdapter;
    private SearchView mSearchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wiki);
        ButterKnife.bind(this);
        setupActionBar();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new NdDividerItemDecoration(this, NdDividerItemDecoration.VERTICAL_LIST));
        RecyclerItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(this);
        mAdapter = new WikiTitleAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mDetailPresenter = new WikiDetailPresenter();
        mDetailPresenter.bindView(this);
        mDetailPresenter.onCreate(savedInstanceState);

        if (!TextUtils.isEmpty(getPresenter().getSearchQuery())) {
            mSearchView.setQuery(getPresenter().getSearchQuery(), false);
        }
        mIsSelectable = true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search_wiki, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setQueryHint(getString(R.string.action_search_wikis));
        if (!TextUtils.isEmpty(getPresenter().getSearchQuery())) {
            mSearchView.setQuery(getPresenter().getSearchQuery(), false);
            onSearchWikisFetched(getPresenter().getSearchResponse());
        }
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (TextUtils.isEmpty(getPresenter().getSearchQuery())) {
            getPresenter().fetchTopWikis(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDetailPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDetailPresenter.onDestroy();
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
    public boolean onItemClick(RecyclerView parent, View view, int position, long id) {
        if (mIsSelectable) {
            Wiki wiki = mAdapter.getItem(position);
            mIsSelectable = false;
            mDetailPresenter.fetchWikibyId(wiki.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText.trim()) && newText.length() >= MIN_SEARCH_LENGTH) {
            getPresenter().preformWikiSearch(newText.trim());
            return true;
        } else {
            if (getPresenter().getTopResponse() != null && !TextUtils.isEmpty(getPresenter().getSearchQuery())) {
                getPresenter().setSearchQuery(null);
                mAdapter.addAll(getPresenter().getTopResponse().getItems(), true);
            }
        }
        return false;
    }
}
