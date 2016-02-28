package com.nordeck.wiki.reader.ui.viewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.nordeck.lib.core.adapter.NdDividerItemDecoration;
import com.nordeck.lib.core.rx.NdObservableScheduler;
import com.nordeck.wiki.reader.R;
import com.nordeck.wiki.reader.SelectedWiki;
import com.nordeck.wiki.reader.adapters.ContentViewerAdapter;
import com.nordeck.wiki.reader.adapters.SectionNavAdapter;
import com.nordeck.wiki.reader.model.IPage;
import com.nordeck.wiki.reader.model.ISection;
import com.nordeck.wiki.reader.model.RelatedResponse;
import com.nordeck.wiki.reader.ui.BaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * TODO save the recycler view positions and restore their states?
 * <p/>
 * Created by parker on 9/4/15.
 */
public class ActivityArticleViewer extends BaseActivity implements SectionNavAdapter.OnItemClickListener<ISection>,
        ContentViewerAdapter.OnClickRelatedArticleListener, ArticleViewerContract.View {

    private static final String EXTRA_ARTICLE_ID = "extra_article_id";

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ArticleViewerContract.Presenter mPresenter;
    private String mId;
    private ContentViewerAdapter mContentAdapter;
    private LinearLayoutManager mContentLayoutManager;
    private SectionNavAdapter mNavAdapter;
    private LinearLayoutManager mNavLayoutManager;

    /**
     * @param context
     * @param articleId null == random article
     * @return
     */
    private static Intent getLaunchIntent(Context context, @Nullable String articleId) {
        Intent intent = new Intent(context, ActivityArticleViewer.class);
        intent.putExtra(EXTRA_ARTICLE_ID, articleId);
        return intent;
    }

    public static void launchActivity(Activity activity, @NonNull String articleId) {
        activity.startActivity(getLaunchIntent(activity, articleId));
    }

    public static void launchActivityRandom(Activity activity) {
        activity.startActivity(getLaunchIntent(activity, null));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_view);
        ButterKnife.bind(this);
        setupActionBar();
        setupDrawers();

        mId = getIntent().getStringExtra(EXTRA_ARTICLE_ID);

        mContentLayoutManager = new LinearLayoutManager(getApplicationContext());
        mContentLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // The content adapter
        mRecyclerView.setLayoutManager(mContentLayoutManager);
        mContentAdapter = new ContentViewerAdapter(this, this);
        mRecyclerView.setAdapter(mContentAdapter);
        // Section nav adapter which is in the right drawer
        mNavLayoutManager = new LinearLayoutManager(getApplicationContext());
        mNavLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        getRightDrawer().setLayoutManager(mNavLayoutManager);
        getRightDrawer().addItemDecoration(new NdDividerItemDecoration(this, NdDividerItemDecoration
                .VERTICAL_LIST));
        mNavAdapter = new SectionNavAdapter(this, this);
        getRightDrawer().setAdapter(mNavAdapter);

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
        mPresenter = new ArticleViewerPresenter(NdObservableScheduler.SUBSCRIBE_IO_OBSERVE_ANDROID_MAIN,
                new ArticleViewInteractor());
        mPresenter.bindView(this, savedInstanceState);

        getSupportActionBar().setTitle(SelectedWiki.getInstance().getSelectedWiki().getTitle());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (TextUtils.isEmpty(mId)) {
            mPresenter.fetchRandomArticle(false);
        } else {
            mPresenter.fetchArticle(mId, false);
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
        mPresenter.unbindView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onArticleFetched(@NonNull List<ISection> article) {
        mContentAdapter.addAll(article, true);
        mNavAdapter.addAll(article, true);
        mNavAdapter.setCurrentPosition(mNavAdapter.getCurrentPosition());
    }

    @Override
    public void onRelatedArticlesFetched(@NonNull RelatedResponse relatedPages) {
        mContentAdapter.addRelatedArticles(relatedPages);
        mNavAdapter.addRelatedArticles(relatedPages);
    }

    @Override
    public void onItemClicked(@NonNull ISection item) {
        int position = mNavAdapter.getPosition(item);
        mContentLayoutManager.scrollToPositionWithOffset(position, getResources().getDimensionPixelOffset(R.dimen
                .nd_padding));
        // Set the position on the adapter, then close the drawer so the user can read the new content, and center
        // the item
        getDrawerLayout().closeDrawer(getRightDrawer());
        // TODO when closing the drawer the user can see the animation of the view moving
        mNavAdapter.setCurrentPosition(position);
        centerNavListOnSelectedPos(position);
    }

    @Override
    public void onClickArticle(IPage page) {
        ActivityArticleViewer.launchActivity(this, page.getId());
    }

    private void centerNavListOnSelectedPos(int selectedPos) {
        mNavLayoutManager.scrollToPositionWithOffset(selectedPos,
                getRightDrawer().getHeight() / 2 - getResources().getDimensionPixelOffset(R
                        .dimen.nd_default_touch_target));
    }

}
