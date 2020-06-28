package com.thinkincab.app.ui.activity.past_trip_detail;

import com.thinkincab.app.base.MvpPresenter;

public interface PastTripDetailsIPresenter<V extends PastTripDetailsIView> extends MvpPresenter<V> {

    void getPastTripDetails(Integer requestId);
}
