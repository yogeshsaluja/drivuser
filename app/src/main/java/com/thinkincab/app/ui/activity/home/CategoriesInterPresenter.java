package com.thinkincab.app.ui.activity.home;

import com.thinkincab.app.base.MvpPresenter;

public interface CategoriesInterPresenter<V extends CategoriesView> extends MvpPresenter<V> {


    void checkStatus();

    void getUserInfo();


    void logout(String id);

    void getNavigationSettings();
}
