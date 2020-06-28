package com.thinkincab.app.ui.activity.past_trip_detail;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.Datum;

import java.util.List;

public interface PastTripDetailsIView extends MvpView {

    void onSuccess(List<Datum> pastTripDetails);

    void onError(Throwable e);
}
