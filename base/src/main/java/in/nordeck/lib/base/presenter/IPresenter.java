package in.nordeck.lib.base.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Handles the life cycle of the presenter.
 */
public interface IPresenter<V extends IView> {

    void bindView(V view);

    void unbindView();

    void onDestroy();

    void onCreate(@Nullable Bundle bundle);

    void onSaveInstanceState(@NonNull Bundle bundle);

}
