package com.thinkincab.app.ui.activity.help;

import com.thinkincab.app.base.MvpView;
import com.thinkincab.app.data.network.model.Help;

public interface HelpIView extends MvpView {

    void onSuccess(Help help);

    void onError(Throwable e);
}
