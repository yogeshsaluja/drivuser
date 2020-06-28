package com.thinkincab.app.ui.activity.splash;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.CheckVersion;
import com.thinkincab.app.data.network.model.Service;
import com.thinkincab.app.data.network.model.User;

import java.util.List;

public interface SplashIView extends MvpView {

    void onSuccess(List<Service> serviceList);

    void onSuccess(User user);

    void onError(Throwable e);

    void onSuccess(CheckVersion checkVersion);
}
