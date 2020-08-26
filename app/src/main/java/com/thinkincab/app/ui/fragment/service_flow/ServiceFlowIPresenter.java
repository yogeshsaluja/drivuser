package com.thinkincab.app.ui.fragment.service_flow;

import com.thinkincab.app.base.MvpPresenter;

import java.util.HashMap;

public interface ServiceFlowIPresenter<V extends ServiceFlowIView> extends MvpPresenter<V> {
    void extendTime(HashMap<String, Object> obj);

}
