package com.thinkincab.app.ui.fragment.upcoming_trip;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.Datum;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface UpcomingTripIView extends MvpView {
    void onSuccess(List<Datum> datumList);

    void onSuccess(Object object);

    void onError(Throwable e);
}
