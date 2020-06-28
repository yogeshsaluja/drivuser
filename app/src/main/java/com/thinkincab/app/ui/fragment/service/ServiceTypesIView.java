package com.thinkincab.app.ui.fragment.service;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.Service;

import java.util.List;

public interface ServiceTypesIView extends MvpView {

    void onSuccess(List<Service> serviceList);

    void onError(Throwable e);

    void onSuccess(Object object);
}
