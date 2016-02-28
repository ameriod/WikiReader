package com.nordeck.wiki.reader.ui.top;

import android.support.annotation.Nullable;

import com.nordeck.lib.core.mvp.NdPresenter;
import com.nordeck.lib.core.mvp.NdView;
import com.nordeck.wiki.reader.model.IPage;

import java.util.List;

import rx.Observable;

/**
 * Created by parker on 2/27/16.
 */
public class TopArticlesContract {

    public interface View extends NdView {
        void setItems(@Nullable List<IPage> items);
    }

    public interface Presenter extends NdPresenter<View> {
        void fetchTopArticles(boolean forceLoad);
    }

    public interface Interactor {
        Observable<List<IPage>> getTopArticles();
    }
}
