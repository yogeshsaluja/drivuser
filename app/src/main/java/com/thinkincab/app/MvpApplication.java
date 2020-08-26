package com.thinkincab.app;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.stetho.Stetho;
import com.thinkincab.app.common.ConnectivityReceiver;
import com.thinkincab.app.common.LocaleHelper;
import com.thinkincab.app.data.network.model.DataResponse;
import com.thinkincab.app.data.network.model.Datum;
import com.thinkincab.app.data.network.model.PhoneOtpResponse;

import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;

//import com.facebook.stetho.Stetho;

public class MvpApplication extends Application {

    public static String marker;
    public static DataResponse checkResp=null;
    public static String estFare="";
    private static MvpApplication mInstance;

    public static boolean canGoToChatScreen;
    public static boolean isChatScreenOpen;

    
    public static boolean isCash = true;
    public static boolean isCard;
    public static boolean isPayumoney;
    public static boolean isPaypal;
    public static boolean isPaytm;
    public static boolean isPaypalAdaptive;
    public static PhoneOtpResponse Otpresponse;
    public static boolean isBraintree;
    public static boolean openChatFromNotification = true;
    public static String SerName="";
    public static String SerId="";
    public static String calculation="";
    public static String discription="";
    public static String fix="";
    public static String price="";
    public static String countProduct="";
    public static String productTotal="";



    public static String mobileno;





    //TODO ALLAN - Alterações débito na máquina e voucher
    public static boolean isDebitMachine;
    public static boolean isforOthers;
    public static boolean isVoucher;
    public static HashMap<String, Object> RIDE_REQUEST = new HashMap<>();
    public static Datum DATUM = null;
    public static boolean showOTP = true;

    public static synchronized MvpApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        Crashlytics crashlytics = new Crashlytics.Builder().core(core).build();
        Fabric.with(this, crashlytics);

        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this);

        MultiDex.install(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
        MultiDex.install(newBase);
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
