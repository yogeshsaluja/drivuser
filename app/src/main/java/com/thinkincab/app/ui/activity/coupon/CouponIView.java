package com.thinkincab.app.ui.activity.coupon;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.PromoResponse;

public interface CouponIView extends MvpView {
    void onSuccess(PromoResponse object);

    void onError(Throwable e);
}
