package com.thinkincab.app.ui.fragment.cancel_ride;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.CancelResponse;

import java.util.List;

public interface CancelRideIView extends MvpView {
    void onSuccess(Object object);

    void onError(Throwable e);

    void onSuccess(List<CancelResponse> response);

    void onReasonError(Throwable e);
}
