package com.nordeck.wiki.reader.ui.top;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.nordeck.lib.core.adapter.NdDividerItemDecoration;
import com.nordeck.lib.core.rx.NdObservableScheduler;
import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.SelectedWiki;
import com.nordeck.wiki.reader.adapters.PageDetailAdapter;
import com.nordeck.wiki.reader.model.IPage;
import com.nordeck.wiki.reader.ui.viewer.ActivityArticleViewer;
import com.nordeck.wiki.reader.ui.BaseActivity;
import com.nordeck.wiki.reader.ui.search.ActivitySearchPages;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityTopPages extends BaseActivity implements
        PageDetailAdapter.OnItemClickListener<IPage>, TopArticlesContract.View {

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, ActivityTopPages.class);
    }

    public static void launchActivity(Activity activity) {
        activity.startActivity(getLaunchIntent(activity));
    }

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private PageDetailAdapter mAdapter;

    private TopArticlesContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupActionBar();
        setupDrawers();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new NdDividerItemDecoration(this, NdDividerItemDecoration.VERTICAL_LIST));
        mAdapter = new PageDetailAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new TopArticlesPresenter(NdObservableScheduler.SUBSCRIBE_IO_OBSERVE_ANDROID_MAIN,
                new TopArticlesInteractor());
        mPresenter.bindView(this, savedInstanceState);

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
        boolean selected = super.onOptionsItemSelected(item);
        if (selected) {
            return selected;
        }
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
        mPresenter.unbindView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setItems(@Nullable List<IPage> items) {
        mAdapter.addAll(new ArrayList<>(items), true);
    }

    @Override
    public void onItemClicked(@NonNull IPage item) {
        ActivityArticleViewer.launchActivity(this, item.getId());
    }
}
