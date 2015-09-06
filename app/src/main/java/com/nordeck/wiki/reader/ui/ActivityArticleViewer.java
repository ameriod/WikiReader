package com.nordeck.wiki.reader.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.Utils;
import com.nordeck.wiki.reader.adapters.ContentViewerAdapter;
import com.nordeck.wiki.reader.adapters.SectionNavAdapter;
import com.nordeck.wiki.reader.adapters.base.NdDividerItemDecoration;
import com.nordeck.wiki.reader.adapters.base.RecyclerItemClickSupport;
import com.nordeck.wiki.reader.model.ArticleResponse;
import com.nordeck.wiki.reader.model.IPage;
import com.nordeck.wiki.reader.model.ISection;
import com.nordeck.wiki.reader.model.RelatedResponse;
import com.nordeck.wiki.reader.presenters.ArticleViewerPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * TODO save the recycler view positions and restore their states?
 * <p/>
 * Created by parker on 9/4/15.
 */
public class ActivityArticleViewer extends BaseActivity implements IArticleViewerView, RecyclerItemClickSupport
        .OnItemClickListener, ContentViewerAdapter.OnClickRelatedArticleListener {

    private static final String EXTRA_ARTICLE_ID = "extra_article_id";
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @Bind(R.id.sections_drawer_recycler_view)
    RecyclerView mSectionsNavRecyclerView;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ArticleViewerPresenter mPresenter;
    private String mId;
    private ContentViewerAdapter mContentAdapter;
    private LinearLayoutManager mContentLayoutManager;
    private SectionNavAdapter mNavAdapter;
    private LinearLayoutManager mNavLayoutManager;

    private static Intent getLaunchIntent(Context context, @NonNull String articleId) {
        Intent intent = new Intent(context, ActivityArticleViewer.class);
        intent.putExtra(EXTRA_ARTICLE_ID, articleId);
        return intent;
    }

    public static void launchActivity(Activity activity, @NonNull String articleId) {
        // TODO create cool transition
        activity.startActivity(getLaunchIntent(activity, articleId));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_view);
        ButterKnife.bind(this);
        setupActionBar();

        mId = getIntent().getStringExtra(EXTRA_ARTICLE_ID);

        mContentLayoutManager = new LinearLayoutManager(getApplicationContext());
        mContentLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // The content adapter
        mRecyclerView.setLayoutManager(mContentLayoutManager);
        mContentAdapter = new ContentViewerAdapter(this, this);
        mRecyclerView.setAdapter(mContentAdapter);
        // Section nav adapter
        mNavLayoutManager = new LinearLayoutManager(getApplicationContext());
        mNavLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSectionsNavRecyclerView.setLayoutManager(mNavLayoutManager);
        mSectionsNavRecyclerView.addItemDecoration(new NdDividerItemDecoration(this, NdDividerItemDecoration
                .VERTICAL_LIST));
        RecyclerItemClickSupport.addTo(mSectionsNavRecyclerView).setOnItemClickListener(this);
        mNavAdapter = new SectionNavAdapter(this);
        mSectionsNavRecyclerView.setAdapter(mNavAdapter);

        // Keep both lists in sync with each other
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mNavAdapter != null) {
                    int position = mContentLayoutManager.findLastVisibleItemPosition();
                    if (position != mNavAdapter.getCurrentPosition()) {
                        // Center the selected section the best that we can
                        mNavAdapter.setCurrentPosition(position);
                        centerNavListOnSelectedPos(position);
                    }
                }
            }
        });
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
        List<ISection> sections = new ArrayList<ISection>(article.getSections());
        mContentAdapter.addAll(sections, true);
        mNavAdapter.addAll(sections, true);
        mNavAdapter.setCurrentPosition(mNavAdapter.getCurrentPosition());
    }

    @Override
    public void onRelatedArticlesFetched(@NonNull RelatedResponse relatedPages) {
        mContentAdapter.addRelatedArticles(relatedPages);
        mNavAdapter.addRelatedArticles(relatedPages);
    }

    @Override
    public void showProgressIndicator(boolean show) {
        Utils.setViewVisibility(mLoading, show);
    }

    @Override
    public void displayError(@Nullable String error) {
        displayErrorMessage(error);
    }

    @Override
    public boolean onItemClick(RecyclerView parent, View view, int position, long id) {
        // move the content to the selection section
        mContentLayoutManager.scrollToPositionWithOffset(position, getResources().getDimensionPixelOffset(R.dimen
                .padding));
        // Set the position on the adapter, then close the drawer so the user can read the new content, and center
        // the item
        mDrawer.closeDrawer(mSectionsNavRecyclerView);
        // TODO when closing the drawer the user can see the animation of the view moving
        mNavAdapter.setCurrentPosition(position);
        centerNavListOnSelectedPos(position);
        return true;
    }

    @Override
    public void onClickArticle(IPage page) {
        ActivityArticleViewer.launchActivity(this, page.getId());
    }

    private void centerNavListOnSelectedPos(int selectedPos) {
        mNavLayoutManager.scrollToPositionWithOffset(selectedPos,
                mSectionsNavRecyclerView.getHeight() / 2 - getResources().getDimensionPixelOffset(R
                        .dimen.default_touch_target));
    }

}