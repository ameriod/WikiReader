package com.nordeck.lib.core.mvp;

import android.support.annotation.Nullable;

public interface NdErrorHandler {

    String handleError(@Nullable Throwable e);
}
