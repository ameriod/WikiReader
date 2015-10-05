package in.nordeck.lib.base.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import timber.log.Timber;

/**
 * {@link AppCompatActivity}
 */
public abstract class BasePresenterActivity<V extends IView, P extends IPresenter<V>> extends
        AppCompatActivity implements IView {

    private PresenterDelegate<V, P> mPresenterDelegate = new PresenterDelegate<>();

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenterDelegate.onCreate(savedInstanceState, getPresenterTag(), getPresenterFactory());
        mPresenterDelegate.onViewCreated((V) this);
        Timber.d("~~Activity %s Created~~", this.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenterDelegate.onResume();
        Timber.d("~~Activity %s Resume~~", this.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenterDelegate.onDestroyView();
        mPresenterDelegate.onDestroy();
        Timber.d("~~Activity %s Destroy~~", this.toString());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenterDelegate.onSaveInstanceState(outState);
        Timber.d("~~Activity %s Save Instance State~~", this.toString());
    }

    @NonNull
    public P getPresenter() {
        return mPresenterDelegate.getPresenter();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    protected abstract String getPresenterTag();

    protected abstract IPresenterFactory<P> getPresenterFactory();
}
