package com.thinkincab.app.ui.activity.setting;

import com.thinkincab.app.base.MvpPresenter;

import java.util.HashMap;

public interface SettingsIPresenter<V extends SettingsIView> extends MvpPresenter<V> {
    void addAddress(HashMap<String, Object> params);

    void deleteAddress(Integer id, HashMap<String, Object> params);

    void address();

    void changeLanguage(String languageID);
}
