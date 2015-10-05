package in.nordeck.lib.base.presenter;

import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;

import timber.log.Timber;

/**
 * A cache that will help keep proper instances of the Presenters alive when needed.
 */
public class PresenterCache {

    private static PresenterCache sInstance = null;

    private SimpleArrayMap<String, IPresenter> presenters;

    private PresenterCache() {
    }

    public static PresenterCache getInstance() {
        if (sInstance == null) {
            sInstance = new PresenterCache();
        }
        return sInstance;
    }

    @SuppressWarnings("unchecked")
    public final <T extends IPresenter> T getPresenter(String who, IPresenterFactory<T> presenterFactory) {
        if (presenters == null) {
            presenters = new SimpleArrayMap<>();
        }
        T p = null;
        try {
            p = (T) presenters.get(who);
        } catch (ClassCastException ex) {
            Timber.e(ex, "Duplicate Presenter tag identified: %s. This could cause issues with state.", who);
        }
        if (p == null) {
            p = presenterFactory.createPresenter();
            presenters.put(who, p);
        }
        return p;
    }

    @SuppressWarnings("unchecked")
    public final <T extends IPresenter> T getPresenter(String who, IPresenterFactory<T> factory,
                                                       Bundle savedInstanceState) {
        T p = getPresenter(who, factory);
        p.onCreate(savedInstanceState);
        return p;
    }

    public final void removePresenter(String who) {
        if (presenters != null) {
            presenters.remove(who);
        }
    }

}
