package com.nordeck.wiki.reader.ui.wiki;

import android.support.annotation.NonNull;

import com.nordeck.lib.core.mvp.NdPresenter;
import com.nordeck.lib.core.mvp.NdView;
import com.nordeck.wiki.reader.model.WikiResponse;

import rx.Observable;

/**
 * Created by parker on 2/27/16.
 */
public class WikiContract {

    public interface View extends NdView {
        void onTopWikisFetched(@NonNull WikiResponse response);

        void onSearchWikisFetched(@NonNull WikiResponse response);
    }

    public interface Presenter extends NdPresenter<View> {
        String getSearchQuery();

        WikiResponse getSearchResponse();

        WikiResponse getTopResponse();

        void setSearchQuery(String query);

        void preformWikiSearch(String query);

        void fetchTopWikis(boolean forceLoad);
    }

    public interface Interactor {
        Observable<WikiResponse> preformWikiSearch(String query);

        Observable<WikiResponse> fetchTopWikis();
    }
}
