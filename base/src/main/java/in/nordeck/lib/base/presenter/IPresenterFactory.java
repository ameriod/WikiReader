package in.nordeck.lib.base.presenter;

import android.support.annotation.NonNull;

public interface IPresenterFactory<T extends IPresenter> {

    @NonNull
    T createPresenter();

}
