package com.thinkincab.app.ui.activity.coupon;

import com.thinkincab.app.base.MvpPresenter;

public interface CouponIPresenter<V extends CouponIView> extends MvpPresenter<V> {
    void coupon();
}
