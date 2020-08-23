package com.thinkincab.app.ui.activity.register;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


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
    private String countryDialCode = "+60";
    private CountryPicker mCountryPicker;
    @BindView(R.id.countryImage)
    ImageView countryImage;
    @BindView(R.id.countryNumber)
    TextView countryNumber;

    CustomDialog dialog;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_phone;
    }

    private void setListener() {
        mCountryPicker.setListener((name, code, dialCode, flagDrawableResID) -> {
            countryNumber.setText(dialCode);
            countryDialCode = dialCode;
            countryImage.setImageResource(flagDrawableResID);
            mCountryPicker.dismiss();
        });

       // countryImage.setOnClickListener(v -> mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER"));

       // countryNumber.setOnClickListener(v -> mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER"));

      //  getUserCountryInfo();
    }
    private void getUserCountryInfo() {
        Country country = getDeviceCountry(RegisterPhoneActivity.this);
        countryImage.setImageResource(country.getFlag());
        countryNumber.setText(country.getDialCode());
        countryDialCode = country.getDialCode();
    }


    private void setCountryList() {
        mCountryPicker = CountryPicker.newInstance("Select Country");
        List<Country> countryList = Country.getAllCountries();
        Collections.sort(countryList, (s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
        mCountryPicker.setCountriesList(countryList);

        setListener();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        registerPresenter.attachView(this);
        dialog=new CustomDialog(this);
        setCountryList();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                HashMap<String, Object> map = new HashMap<>();
                map.put("device_id", SharedHelper.getKey(RegisterPhoneActivity.this, "device_id"));
                map.put("mobile",phone .getText().toString());
                registerPresenter.register(map);
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
