package com.thinkincab.app.ui.activity.location_pick;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.AddressResponse;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface LocationPickIView extends MvpView {

    void onSuccess(AddressResponse address);

    void onError(Throwable e);
}
