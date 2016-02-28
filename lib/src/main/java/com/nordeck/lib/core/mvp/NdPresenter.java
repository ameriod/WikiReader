package com.nordeck.lib.core.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface NdPresenter<V extends NdView> {

    void bindView(@NonNull V view, @Nullable Bundle bundle);

    void unbindView();

    void onSaveInstanceState(@NonNull Bundle bundle);

}