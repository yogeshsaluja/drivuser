package com.thinkincab.app.ui.activity.passbook;

import com.thinkincab.app.base.MvpPresenter;

public interface WalletHistoryIPresenter<V extends WalletHistoryIView> extends MvpPresenter<V> {
    void wallet();
}
