package com.thinkincab.app.ui.activity.upcoming_trip_detail;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.Datum;

import java.util.List;

public interface UpcomingTripDetailsIView extends MvpView {

    void onSuccess(List<Datum> upcomingTripDetails);

    void onError(Throwable e);
}
