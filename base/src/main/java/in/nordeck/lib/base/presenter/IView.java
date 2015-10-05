package in.nordeck.lib.base.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

/**
 * Base View to be used within all Views (Activities / Fragments).
 */
public interface IView {

    Context getContext();

    void showProgressIndicator(boolean show);

    void displayError(@Nullable String error);

}
