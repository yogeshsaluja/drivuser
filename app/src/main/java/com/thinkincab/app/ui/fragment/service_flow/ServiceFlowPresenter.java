package com.thinkincab.app.ui.fragment.service_flow;

import com.thinkincab.app.base.BasePresenter;
import com.thinkincab.app.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ServiceFlowPresenter<V extends ServiceFlowIView> extends BasePresenter<V> implements ServiceFlowIPresenter<V> {
    @Override
    public void extendTime(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .extendTime(obj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }
}
