package com.thinkincab.app.ui.fragment.service;

import com.thinkincab.app.base.MvpPresenter;

import java.util.HashMap;

public interface ServiceTypesIPresenter<V extends ServiceTypesIView> extends MvpPresenter<V> {

    void services();

    void rideNow(HashMap<String, Object> obj);

}
