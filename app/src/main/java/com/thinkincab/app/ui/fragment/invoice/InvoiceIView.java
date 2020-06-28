package com.thinkincab.app.ui.fragment.invoice;

import com.appoets.paytmpayment.PaytmObject;
import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.BrainTreeResponse;
import com.thinkincab.app.data.network.model.CheckSumData;
import com.thinkincab.app.data.network.model.Message;

public interface InvoiceIView extends MvpView {
    void onSuccess(Message message);

    void onSuccess(Object o);

    void onSuccessPayment(Object o);

    void onError(Throwable e);

    void onSuccess(BrainTreeResponse response);

    void onPayumoneyCheckSumSucess(CheckSumData checkSumData);

    void onPayTmCheckSumSucess(PaytmObject payTmResponse);
}
