package com.thinkincab.app.ui.activity.home;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.DataResponse;
import com.thinkincab.app.data.network.model.SettingsResponse;
import com.thinkincab.app.data.network.model.User;

import java.util.List;

public interface CategoriesView extends MvpView {
    void onError(Throwable e);


    void onSuccess(DataResponse dataResponse);


    void onSuccess(User user);


    void onDestinationSuccess(Object object);

    void onSuccessLogout(Object object);


    void onSuccess(SettingsResponse settingsResponse);

    void onSettingError(Throwable throwable);
}
