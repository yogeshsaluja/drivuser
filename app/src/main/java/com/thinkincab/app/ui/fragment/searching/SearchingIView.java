package com.thinkincab.app.ui.fragment.searching;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.DataResponse;

public interface SearchingIView extends MvpView {
    void onSuccess(Object object);
    void onSuccessResukt(Object object);

    void onSuccessData(DataResponse object);

    void onError(Throwable e);

 }
