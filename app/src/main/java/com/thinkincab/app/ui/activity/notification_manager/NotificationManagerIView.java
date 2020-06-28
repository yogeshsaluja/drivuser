package com.thinkincab.app.ui.activity.notification_manager;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.NotificationManager;

import java.util.List;

public interface NotificationManagerIView extends MvpView {

    void onSuccess(List<NotificationManager> notificationManager);

    void onError(Throwable e);

}