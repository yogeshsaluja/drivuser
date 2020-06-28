package com.thinkincab.app.ui.activity.login;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.ForgotResponse;
import com.thinkincab.app.data.network.model.Token;

public interface LoginIView extends MvpView {
    void onSuccess(Token token);

    void onSuccess(ForgotResponse object);

    void onError(Throwable e);
}
