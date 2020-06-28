package com.thinkincab.app.ui.activity.location_pick;

import com.thinkincab.app.base.MvpPresenter;

public interface LocationPickIPresenter<V extends LocationPickIView> extends MvpPresenter<V> {
    void address();
}
