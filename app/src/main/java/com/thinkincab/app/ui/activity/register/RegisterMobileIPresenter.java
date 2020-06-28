package com.thinkincab.app.ui.activity.register;



import com.thinkincab.app.base.MvpPresenter;

import java.util.HashMap;

public interface RegisterMobileIPresenter<V extends RegisterMobileIView> extends MvpPresenter<V> {

    void register(HashMap<String, Object> obj);





}
