package com.thinkincab.app.ui.fragment.book_ride;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.PromoResponse;


public interface BookRideIView extends MvpView {
    void onSuccess(Object object);

    void onError(Throwable e);

    void onSuccessCoupon(PromoResponse promoResponse);
}
