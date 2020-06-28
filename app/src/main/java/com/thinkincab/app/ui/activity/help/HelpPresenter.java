package com.thinkincab.app.ui.activity.help;

import com.thinkincab.app.base.BasePresenter;
import com.thinkincab.app.data.network.APIClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HelpPresenter<V extends HelpIView> extends BasePresenter<V> implements HelpIPresenter<V> {

    @Override
    public void help() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .help()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }
}
