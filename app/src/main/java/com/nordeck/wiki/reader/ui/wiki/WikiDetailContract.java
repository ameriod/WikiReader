package com.nordeck.wiki.reader.ui.wiki;

import android.support.annotation.NonNull;

import com.nordeck.lib.core.mvp.NdPresenter;
import com.nordeck.lib.core.mvp.NdView;
import com.nordeck.wiki.reader.model.WikiDetail;

import rx.Observable;

/**
 * Created by parker on 2/27/16.
 */
public class WikiDetailContract {

    public interface View extends NdView {
        void onWikiDetailFetched(@NonNull WikiDetail detail);

        void displayErrorWikiDetail(@NonNull String error);
    }

    public interface Presenter extends NdPresenter<View> {
        void fetchWikibyId(String id);
    }

    public interface Interactor {
        Observable<WikiDetail> getWiki(String id);
    }
}
