package com.thinkincab.app.ui.activity.register;


import com.thinkincab.app.base.BasePresenter;
import com.thinkincab.app.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegisteMobilerPresenter<V extends com.thinkincab.app.ui.activity.register.RegisterMobileIView>
        extends BasePresenter<V>
        implements com.thinkincab.app.ui.activity.register.RegisterMobileIPresenter<V> {

    @Override
    public void register(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .sendOtp(obj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

}
