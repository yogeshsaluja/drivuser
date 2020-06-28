package com.thinkincab.app.ui.activity.setting;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.AddressResponse;

public interface SettingsIView extends MvpView {

    void onSuccessAddress(Object object);

    void onLanguageChanged(Object object);

    void onSuccess(AddressResponse address);

    void onError(Throwable e);
}
