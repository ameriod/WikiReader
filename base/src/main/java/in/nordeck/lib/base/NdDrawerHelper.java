package in.nordeck.lib.base;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

/**
 * Handles two drawers and the
 * Created by parker on 9/7/15.
 */
public class NdDrawerHelper {

    protected DrawerLayout mDrawerLayout;

    protected View mLeftDrawer;
    protected View mRightDrawer;

    protected ActionBarDrawerToggle mToggle;
    protected AppCompatActivity mActivity;

    public NdDrawerHelper(@NonNull AppCompatActivity activity, @NonNull DrawerLayout drawerLayout, @Nullable
    View leftDrawer, @Nullable View rightDrawer) {
        this.mActivity = activity;
        this.mDrawerLayout = drawerLayout;
        this.mRightDrawer = rightDrawer;
        this.mLeftDrawer = leftDrawer;
    }

    /**
     * Only creates a toggle if there is a left drawer
     */
    public void onCreate() {
        if (mLeftDrawer != null) {
            // Only toggle for the left drawer
            mToggle = new ActionBarDrawerToggle(mActivity, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    if (drawerView == mLeftDrawer) {
                        super.onDrawerClosed(drawerView);
                    }
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    if (drawerView == mLeftDrawer) {
                        super.onDrawerOpened(drawerView);
                    }
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    if (drawerView == mLeftDrawer) {
                        super.onDrawerSlide(drawerView, slideOffset);
                    }
                }
            };
            mDrawerLayout.setDrawerListener(mToggle);

            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mActivity.getSupportActionBar().setHomeButtonEnabled(true);
        }

    }

    public void onPostCreate() {
        if (mToggle != null) {
            mToggle.syncState();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (mToggle != null) {
            mToggle.onConfigurationChanged(newConfig);
        }
    }

    /**
     * False do the regular action items
     *
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle != null) {
            return mToggle.onOptionsItemSelected(item);
        }
        return false;
    }
}
