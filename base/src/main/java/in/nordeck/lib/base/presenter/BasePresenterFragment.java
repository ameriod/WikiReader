package in.nordeck.lib.base.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import timber.log.Timber;

/**
 * {@link Fragment}
 */
public abstract class BasePresenterFragment<V extends IView, P extends IPresenter<V>> extends
        Fragment implements IView {


    private PresenterDelegate<V, P> mPresenterDelegate = new PresenterDelegate<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Timber.d("~~Fragment %s Attached~~", this.toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenterDelegate.onCreate(savedInstanceState, getPresenterTag(), getPresenterFactory());
        Timber.d("~~Fragment %s Created~~", this.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("~~Fragment %s Resumed~~", this.toString());
        mPresenterDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("~~Fragment %s Paused~~", this.toString());
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.d("~~Fragment %s Stopped~~", this.toString());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenterDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.d("~~Fragment %s Detached~~", this.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenterDelegate.onDestroyView();
        Timber.d("~~Fragment %s View Destroyed~~", this.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenterDelegate.onDestroy();
        Timber.d("~~Fragment %s destroyed~~", this.toString());
    }

    @NonNull
    public P getPresenter() {
        return mPresenterDelegate.getPresenter();
    }

    protected abstract String getPresenterTag();

    protected abstract IPresenterFactory<P> getPresenterFactory();
}
