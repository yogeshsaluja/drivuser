package com.thinkincab.app.ui.activity.profile;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.User;

public interface ProfileIView extends MvpView {

    void onSuccess(User user);

    void onUpdateSuccess(User user);

    void onError(Throwable e);

    void onSuccessPhoneNumber(Object object);

    void onVerifyPhoneNumberError(Throwable e);
}
