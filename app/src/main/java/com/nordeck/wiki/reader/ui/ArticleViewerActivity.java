package com.nordeck.wiki.reader.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.adapters.SectionViewerAdapter;
import com.nordeck.wiki.reader.model.ArticleResponse;
import com.nordeck.wiki.reader.presenters.ArticleViewerPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by parker on 9/4/15.
 */
public class ArticleViewerActivity extends BaseActivity implements IArticleViewerView {

    private static final String EXTRA_ARTICLE_ID = "extra_article_id";
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ArticleViewerPresenter mPresenter;
    private String mId;
    private SectionViewerAdapter mAdapter;

    public static Intent getLaunchIntent(Context context, @NonNull String articleId) {
        Intent intent = new Intent(context, ArticleViewerActivity.class);
        intent.putExtra(EXTRA_ARTICLE_ID, articleId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_view);
        ButterKnife.bind(this);

        mId = getIntent().getStringExtra(EXTRA_ARTICLE_ID);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SectionViewerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new ArticleViewerPresenter();
        mPresenter.bindView(this);
        mPresenter.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.fetchArticle(mId, false);
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
    public void onArticleFetched(@NonNull ArticleResponse article) {
        mAdapter.addAll(article.getSections(), true);
    }

    @Override
    public void showProgressIndicator(boolean show) {

    }

    @Override
    public void displayError(@Nullable String error) {
        displayErrorMessage(error);
    }
}
