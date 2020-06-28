package com.thinkincab.app.ui.activity.invite_friend;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.User;

public interface InviteFriendIView extends MvpView {

    void onSuccess(User user);

    void onError(Throwable e);

}
