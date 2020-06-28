package com.thinkincab.app.ui.activity.wallet;

import com.appoets.paytmpayment.PaytmObject;
import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.AddWallet;
import com.thinkincab.app.data.network.model.BrainTreeResponse;

public interface WalletIView extends MvpView {
    void onSuccess(AddWallet object);

    void onSuccess(PaytmObject object);

    void onSuccess(BrainTreeResponse response);
    void onError(Throwable e);
}
