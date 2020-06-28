package com.thinkincab.app.ui.activity.notification_manager;

import com.thinkincab.app.base.MvpPresenter;

public interface NotificationManagerIPresenter<V extends NotificationManagerIView> extends MvpPresenter<V> {
    void getNotificationManager();
}
