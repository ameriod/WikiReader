package com.nordeck.wiki.reader.presenters;

import android.support.annotation.Nullable;

public interface NdView {

    public void showProgressIndicator(boolean show);

    public void displayError(@Nullable String error);

}