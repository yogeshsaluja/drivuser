package com.thinkincab.app.ui.activity.passbook;

import com.thinkincab.app.base.BasePresenter;
import com.thinkincab.app.data.network.APIClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WalletHistoryPresenter<V extends WalletHistoryIView> extends BasePresenter<V> implements WalletHistoryIPresenter<V> {

    @Override
    public void wallet() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .wallet()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }
}
