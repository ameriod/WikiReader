package com.nordeck.wiki.reader.ui.search;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nordeck.lib.core.mvp.NdPresenter;
import com.nordeck.lib.core.mvp.NdView;
import com.nordeck.wiki.reader.model.IPage;

import java.util.List;

import rx.Observable;

/**
 * Created by parker on 2/27/16.
 */
public class SearchPageContract {

    public interface View extends NdView {
        void setItems(@Nullable List<IPage> items);
    }

    public interface Presenter extends NdPresenter<View> {
        void getSearchResults(@NonNull String query);
    }

    public interface Interactor {
        Observable<List<IPage>> getSearchResults(@NonNull String query);
    }
}
