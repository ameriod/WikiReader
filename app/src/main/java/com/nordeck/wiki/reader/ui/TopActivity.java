package com.nordeck.wiki.reader.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.adapters.PageDetailAdapter;
import com.nordeck.wiki.reader.model.IPage;
import com.nordeck.wiki.reader.model.PageRelated;
import com.nordeck.wiki.reader.model.PagesResponse;
import com.nordeck.wiki.reader.model.RelatedResponse;
import com.nordeck.wiki.reader.presenters.TopArticlesPresenter;
import com.nordeck.wiki.reader.adapters.base.NdDividerItemDecoration;
import com.nordeck.wiki.reader.adapters.base.RecyclerItemClickSupport;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TopActivity extends BaseActivity implements ITopArticlesView, RecyclerItemClickSupport
        .OnItemClickListener {

    public static final String TEST_WIKIA = /*"http://starwars.wikia.com"*/ "http://muppet.wikia.com";

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private PageDetailAdapter mAdapter;

    private TopArticlesPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new NdDividerItemDecoration(this, NdDividerItemDecoration.VERTICAL_LIST));
        RecyclerItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(this);
        mAdapter = new PageDetailAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new TopArticlesPresenter();
        mPresenter.bindView(this);
        mPresenter.onCreate(savedInstanceState);
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
    public void showProgressIndicator(boolean show) {

    }

    @Override
    public void displayError(@Nullable String error) {
        displayErrorMessage(error);
    }

    @Override
    public boolean onItemClick(RecyclerView parent, View view, int position, long id) {
        IPage article = mAdapter.getItem(position);
        ArticleViewerActivity.launchActivity(this, article.getId());
        return true;
    }
}
