package com.thinkincab.app.ui.fragment.dispute;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.DisputeResponse;
import com.thinkincab.app.data.network.model.Help;

import java.util.List;

public interface DisputeIView extends MvpView {

    void onSuccess(Object object);

    void onSuccessDispute(List<DisputeResponse> responseList);

    void onError(Throwable e);

    void onSuccess(Help help);
}
