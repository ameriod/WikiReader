package in.nordeck.lib.base.presenter;

import android.os.Bundle;

import timber.log.Timber;

public class PresenterDelegate<V extends IView, P extends IPresenter<V>> {

    private P presenter;
    private boolean isDestroyedBySystem;
    private PresenterCache presenterCache;
    private String presenterTag;

    public void onCreate(Bundle savedInstanceState, String who, IPresenterFactory<? extends P> factory) {
        presenterTag = who;
        presenterCache = PresenterCache.getInstance();
        presenter = presenterCache.getPresenter(who, factory, savedInstanceState);
        Timber.d("Presenter fetched from PresenterDelegate. Presenter tag %s", presenterTag);
    }

    public void onViewCreated(V view) {
        try {
            presenter.bindView(view);
        } catch (ClassCastException ex) {
            Timber.e(ex, "Error: Your view does not implement the proper IView.");
            throw new IllegalStateException("Error: Your view does not implement the proper IView.");
        }
    }

    public void onResume() {
        isDestroyedBySystem = false;
    }

    public void onSaveInstanceState(Bundle outState) {
        isDestroyedBySystem = true;
        presenter.onSaveInstanceState(outState);
    }

    public void onDestroyView() {
        presenter.unbindView();
    }

    public void onDestroy() {
        if (!isDestroyedBySystem) {
            presenter.onDestroy();
            presenterCache.removePresenter(presenterTag);
            Timber.d("Presenter removed from cache. Presenter Tag: %s", presenterTag);
        }
    }

    public P getPresenter() {
        return presenter;
    }

}
