package com.thinkincab.app.ui.activity.register;


import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.RegisterResponse;
import com.thinkincab.app.data.network.model.SettingsResponse;

public interface RegisterIView extends MvpView {

    void onSuccess(SettingsResponse response);

    void onSuccess(RegisterResponse object);

    void onSuccess(Object object);

    void onSuccessPhoneNumber(Object object);

    void onVerifyPhoneNumberError(Throwable e);

    void onError(Throwable e);

    void onVerifyEmailError(Throwable e);
}
