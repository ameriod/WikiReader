package com.nordeck.wiki.reader.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.SelectedWiki;
import com.nordeck.wiki.reader.adapters.PageDetailAdapter;
import com.nordeck.wiki.reader.model.IPage;
import com.nordeck.wiki.reader.model.PagesResponse;
import com.nordeck.wiki.reader.presenters.TopArticlesPresenter;
import com.nordeck.wiki.reader.adapters.base.NdDividerItemDecoration;
import com.nordeck.wiki.reader.adapters.base.RecyclerItemClickSupport;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityTopPages extends BaseActivity implements ITopArticlesView, RecyclerItemClickSupport
        .OnItemClickListener {

    public static Intent getLaunchIntent(Context context) {
        Intent intent = new Intent(context, ActivityTopPages.class);
        return intent;
    }

    public static void launchActivity(Activity activity) {
        activity.startActivity(getLaunchIntent(activity));
    }

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private PageDetailAdapter mAdapter;

    private TopArticlesPresenter mPresenter;

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
        mAdapter = new PageDetailAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new TopArticlesPresenter((SelectedWiki.getInstance().getSelectedWiki().getUrl()));
        mPresenter.bindView(this);
        mPresenter.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(getString(R.string.title_top, SelectedWiki.getInstance().getSelectedWiki()
                .getTitle()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                // TODO impl
                return true;
            case R.id.action_search_pages:
                ActivitySearchPages.launchActivity(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.fetchTopArticles(false);
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
    public void onTopArticlesFetched(PagesResponse response) {
        mAdapter.addAll(new ArrayList<IPage>(response.getItems()), true);
    }

    @Override
    public boolean onItemClick(RecyclerView parent, View view, int position, long id) {
        IPage article = mAdapter.getItem(position);
        ActivityArticleViewer.launchActivity(this, article.getId());
        return true;
    }
}
