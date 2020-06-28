package com.thinkincab.app.ui.activity.register;


import com.thinkincab.app.base.MvpPresenter;
import com.thinkincab.app.ui.activity.register.RegisterIView;

import java.util.HashMap;

public interface RegisterIPresenter<V extends RegisterIView> extends MvpPresenter<V> {

    void register(HashMap<String, Object> obj);

    void getSettings();

    void verifyEmail(String email);

    void verifyCredentials(String phoneNumber, String countryCode);

}
