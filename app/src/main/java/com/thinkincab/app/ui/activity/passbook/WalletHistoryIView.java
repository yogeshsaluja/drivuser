package com.thinkincab.app.ui.activity.passbook;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.WalletResponse;

public interface WalletHistoryIView extends MvpView {
    void onSuccess(WalletResponse response);

    void onError(Throwable e);
}
