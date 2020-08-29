package com.thinkincab.app.ui.activity.register;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.thinkincab.app.MvpApplication;
import com.thinkincab.app.R;
import com.thinkincab.app.base.BaseActivity;
import com.thinkincab.app.common.CustomDialog;
import com.thinkincab.app.data.SharedHelper;
import com.thinkincab.app.data.network.model.PhoneOtpResponse;
import com.thinkincab.app.ui.countrypicker.Country;
import com.thinkincab.app.ui.countrypicker.CountryPicker;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterPhoneActivity extends BaseActivity implements RegisterMobileIView {

    @BindView(R.id.number)
    EditText phone;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.bt_login)
    TextView bt_login;

     private CountryPicker mCountryPicker;

    CustomDialog dialog;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_phone;
    }


    @Override
    protected void initView() {
        ButterKnife.bind(this);
        registerPresenter.attachView(this);
        dialog=new CustomDialog(this);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!TextUtils.isEmpty(phone.getText().toString())){
                dialog.show();

                HashMap<String, Object> map = new HashMap<>();
                map.put("device_id", SharedHelper.getKey(RegisterPhoneActivity.this, "device_id"));
                map.put("mobile",phone.getText().toString());
                registerPresenter.register(map);
                }
                else {
                    Toast.makeText(RegisterPhoneActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!TextUtils.isEmpty(phone.getText().toString())){
                dialog.show();
                HashMap<String, Object> map = new HashMap<>();
                map.put("device_id", SharedHelper.getKey(RegisterPhoneActivity.this, "device_id"));
                map.put("mobile",phone .getText().toString());
                registerPresenter.register(map);
                }
                else {
                    Toast.makeText(RegisterPhoneActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private RegisteMobilerPresenter<RegisterPhoneActivity> registerPresenter = new RegisteMobilerPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onSuccess(PhoneOtpResponse object) {
        dialog.dismiss();
        if (object!=null){

            Intent intent=new Intent(this,OtpVerifyActivity.class);
            MvpApplication.Otpresponse=object;

            MvpApplication.mobileno=phone.getText().toString();
            startActivity(intent);

        }

    }

}
