package com.nordeck.lib.core.mvp;

import android.support.annotation.NonNull;

public interface NdView {

    void showProgressIndicator(boolean show);

    void displayError(@NonNull String error);

}