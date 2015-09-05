package com.nordeck.wiki.reader;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by parker on 9/4/15.
 */
public class WikiReaderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
