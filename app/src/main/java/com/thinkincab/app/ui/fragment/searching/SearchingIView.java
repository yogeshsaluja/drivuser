package com.thinkincab.app.ui.fragment.searching;

import com.thinkincab.app.base.MvpView;

public interface SearchingIView extends MvpView {
    void onSuccess(Object object);

    void onError(Throwable e);
}
