package com.thinkincab.app.ui.activity.add_card;

import com.thinkincab.app.base.MvpPresenter;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
interface AddCardIPresenter<V extends AddCardIView> extends MvpPresenter<V> {
    void card(String stripeToken);
}
