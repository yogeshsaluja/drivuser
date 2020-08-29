package com.thinkincab.app.ui.activity.notification_manager;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkincab.app.R;
import com.thinkincab.app.base.BaseActivity;
import com.thinkincab.app.data.network.model.NotificationManager;
import com.thinkincab.app.ui.adapter.NotificationAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationManagerActivity extends BaseActivity implements NotificationManagerIView {

    @BindView(R.id.rvNotificationManager)
    RecyclerView rvNotificationManager;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.textView35)
    TextView title;
    private NotificationManagerPresenter<NotificationManagerActivity> presenter = new NotificationManagerPresenter<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_notification_manager;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
         title.setText(getString(R.string.notification_manager));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        presenter.getNotificationManager();
    }

    @Override
    public void onSuccess(List<NotificationManager> managers) {
        rvNotificationManager.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvNotificationManager.setAdapter(new NotificationAdapter(managers));
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
