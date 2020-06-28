package com.thinkincab.app.ui.activity.register;


import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.PhoneOtpResponse;

public interface RegisterMobileIView extends MvpView {

   

    void onSuccess(PhoneOtpResponse object);
 
    void onError(Throwable e);


}
