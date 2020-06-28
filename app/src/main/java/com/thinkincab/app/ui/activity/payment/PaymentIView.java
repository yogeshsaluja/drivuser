package com.thinkincab.app.ui.activity.payment;

import com.appoets.paytmpayment.PaytmObject;
import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.BrainTreeResponse;
import com.thinkincab.app.data.network.model.Card;
import com.thinkincab.app.data.network.model.CheckSumData;

import java.util.List;

public interface PaymentIView extends MvpView {

    void onSuccess(Object card);

    void onSuccess(BrainTreeResponse response);

    void onSuccess(List<Card> cards);

    void onAddCardSuccess(Object cards);

    void onError(Throwable e);

    void onPayumoneyCheckSumSucess(CheckSumData checkSumData);

    void onPayTmCheckSumSuccess(PaytmObject payTmResponse);

}
