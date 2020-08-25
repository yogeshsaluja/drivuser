package com.thinkincab.app.ui.activity.register;

import android.content.Intent;
import android.os.Handler;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.thinkincab.app.BuildConfig;
import com.thinkincab.app.MvpApplication;
import com.thinkincab.app.R;
import com.thinkincab.app.base.BaseActivity;
import com.thinkincab.app.data.SharedHelper;
import com.thinkincab.app.data.network.model.ForgotResponse;
import com.thinkincab.app.data.network.model.Token;
import com.thinkincab.app.ui.activity.home.HomePageActivity;
import com.thinkincab.app.ui.activity.login.LoginIView;
import com.thinkincab.app.ui.activity.login.loginPresenter;
import com.thinkincab.app.ui.activity.main.MainActivity;


import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtpVerifyActivity extends BaseActivity implements LoginIView {
    @BindView(R.id.password)
    EditText otp;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.next)
    TextView next;
    private String email;

    private loginPresenter<OtpVerifyActivity> presenter = new loginPresenter();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_otp_verify;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

      /*  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    otp.setText(""+MvpApplication.Otpresponse.getOtp());

                }
            },2000);
        mToolbar.setNavigationOnClickListener(v -> finish());
        presenter.attachView(this);

        next.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(otp.getText().toString())){
                if (MvpApplication.Otpresponse!=null&&MvpApplication.Otpresponse.getOtp()==Integer.parseInt(otp.getText().toString())){
                    if (MvpApplication.Otpresponse.getUser()!=null){
                        login();
                    }else {
                        Intent intent=new Intent(this, RegisterActivity.class);
                        startActivity(intent);
                    }


                }else{
                    Toast.makeText(this, "Otp not matched!", Toast.LENGTH_SHORT).show();
                }
            }else
                Toast.makeText(this, "Can't be empty", Toast.LENGTH_SHORT).show();
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     }

    private void login() {
        try {

            if (SharedHelper.getKey(this, "device_token").isEmpty()) {
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        SharedHelper.putKey(this, "device_token", task.getResult().getToken());
                        Log.d("FCM_TOKEN", task.getResult().getToken());
                    } else Log.w("PasswordActivity", "getInstanceId failed", task.getException());
                });
                return;
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("grant_type", "password");
            map.put("username", MvpApplication.Otpresponse.getUser().getEmail());

            map.put("password",  MvpApplication.Otpresponse.getUser().getMobile());
            map.put("client_secret", BuildConfig.CLIENT_SECRET);
            map.put("client_id", BuildConfig.CLIENT_ID);
            map.put("device_token", SharedHelper.getKey(this, "device_token", "No device"));
            map.put("device_id", SharedHelper.getKey(this, "device_id", "123"));
            map.put("device_type", BuildConfig.DEVICE_TYPE);

            showLoading();
            presenter.login(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(Token token) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String accessToken = token.getTokenType() + " " + token.getAccessToken();
        SharedHelper.putKey(this, "access_token", accessToken);
        SharedHelper.putKey(this, "refresh_token", token.getRefreshToken());
        SharedHelper.putKey(this, "logged_in", true);
        finishAffinity();
        startActivity(new Intent(this, HomePageActivity.class));

    }

    @Override
    public void onSuccess(ForgotResponse object) {

    }


}
