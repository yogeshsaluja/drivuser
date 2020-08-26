package com.thinkincab.app.ui.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.thinkincab.app.BuildConfig;
import com.thinkincab.app.MvpApplication;
import com.thinkincab.app.R;
import com.thinkincab.app.base.BaseActivity;
import com.thinkincab.app.common.Constants;
import com.thinkincab.app.common.CustomDialog;
import com.thinkincab.app.common.LocaleHelper;
import com.thinkincab.app.data.SharedHelper;
import com.thinkincab.app.data.network.model.DataResponse;
import com.thinkincab.app.data.network.model.SettingsResponse;
import com.thinkincab.app.data.network.model.User;
import com.thinkincab.app.ui.activity.coupon.CouponActivity;
import com.thinkincab.app.ui.activity.help.HelpActivity;
import com.thinkincab.app.ui.activity.invite_friend.InviteFriendActivity;
import com.thinkincab.app.ui.activity.main.MainActivity;
import com.thinkincab.app.ui.activity.payment.PaymentActivity;
import com.thinkincab.app.ui.activity.profile.ProfileActivity;
import com.thinkincab.app.ui.activity.your_trips.YourTripActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.thinkincab.app.MvpApplication.RIDE_REQUEST;
import static com.thinkincab.app.MvpApplication.isCard;
import static com.thinkincab.app.MvpApplication.isCash;
import static com.thinkincab.app.MvpApplication.isDebitMachine;
import static com.thinkincab.app.MvpApplication.isVoucher;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.PAYMENT_MODE;
import static com.thinkincab.app.data.SharedHelper.getKey;
import static com.thinkincab.app.data.SharedHelper.key.PROFILE_IMG;

public class HomePageActivity   extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, CategoriesView {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private DataResponse checkStatusResponse = new DataResponse();
    private CategoriesPresenter<HomePageActivity> presenter = new CategoriesPresenter<>();
    CustomDialog dialog;

    private CircleImageView picture;
    private TextView name;
    private TextView sub_name;

    @BindView(R.id.menu)
    ImageView menu;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_page;
    }

    @Override
    protected void initView() {

        ButterKnife.bind(this);
        presenter.attachView(this);
        presenter.getUserInfo();
        navigationView = findViewById(R.id.nav_view);
        dialog=new CustomDialog(this);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        picture = headerView.findViewById(R.id.picture);
        name = headerView.findViewById(R.id.name);
        sub_name = headerView.findViewById(R.id.sub_name);
        headerView.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, picture, ViewCompat.getTransitionName(picture));
            startActivity(new Intent(this, ProfileActivity.class), options.toBundle());
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                presenter.getNavigationSettings();
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });





    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_payment:
                startActivity(new Intent(this, PaymentActivity.class));
                break;
            case R.id.nav_your_trips:
                startActivity(new Intent(this, YourTripActivity.class));
                break;
            case R.id.nav_coupon:
                startActivity(new Intent(this, CouponActivity.class));
                break;
//            case R.id.nav_wallet:
//                startActivity(new Intent(this, WalletActivity.class));
//                break;
//            case R.id.nav_passbook:
//                startActivity(new Intent(this, WalletHistoryActivity.class));
//                break;
//            case R.id.nav_settings:
//                startActivity(new Intent(this, SettingsActivity.class));
//                break;
            case R.id.nav_help:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case R.id.nav_share:
                shareApp();
                break;
//            case R.id.nav_become_driver:
//                alertBecomeDriver();
//                break;
//            case R.id.nav_notification:
//                startActivity(new Intent(this, NotificationManagerActivity.class));
//                break;
            case R.id.nav_invite_friend:
                startActivity(new Intent(this, InviteFriendActivity.class));
                break;
            case R.id.nav_logout:
                ShowLogoutPopUp();
                break;
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onSuccess(DataResponse dataResponse) {
        this.checkStatusResponse = dataResponse;
        updatePaymentEntities();

        if (!Objects.requireNonNull(dataResponse.getData()).isEmpty()) {
            if (!dataResponse.getData().get(0).getStatus().equals("EMPTY")) {



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                         startActivity(new Intent(HomePageActivity.this, MainActivity.class));
                    }
                }, 2500);


            } else
                dialog.dismiss();
        } else
            dialog.dismiss();

    }

    public void updatePaymentEntities() {
        if (checkStatusResponse != null) {
          isCash = checkStatusResponse.getCash() == 1;
            isCard = checkStatusResponse.getCard() == 1;
            isDebitMachine = checkStatusResponse.getDebitMachine() == 1;
            isVoucher = checkStatusResponse.getVoucher() == 1;

            MvpApplication.isPayumoney = checkStatusResponse.getPayumoney() == 1;
            MvpApplication.isPaypal = checkStatusResponse.getPaypal() == 1;
            MvpApplication.isBraintree = checkStatusResponse.getBraintree() == 1;
            MvpApplication.isPaypalAdaptive = checkStatusResponse.getPaypal_adaptive() == 1;
            MvpApplication.isPaytm = checkStatusResponse.getPaytm() == 1;

            SharedHelper.putKey(this, "currency", checkStatusResponse.getCurrency());
            if (isCash) RIDE_REQUEST.put(PAYMENT_MODE, Constants.PaymentMode.CASH);
            else if (isCard) RIDE_REQUEST.put(PAYMENT_MODE, Constants.PaymentMode.CARD);
            else if (isDebitMachine)
                RIDE_REQUEST.put(PAYMENT_MODE, Constants.PaymentMode.DEBIT_MACHINE);
            else if (isVoucher) RIDE_REQUEST.put(PAYMENT_MODE, Constants.PaymentMode.VOUCHER);
        }
    }

    @Override
    public void onSuccess(@NonNull User user) {
        String dd = LocaleHelper.getLanguage(this);
        String userLanguage = (user.getLanguage() == null) ? Constants.Language.ENGLISH : user.getLanguage();


        SharedHelper.putKey(this, "lang", user.getLanguage());
        SharedHelper.putKey(this, "stripe_publishable_key", user.getStripePublishableKey());
        SharedHelper.putKey(this, "currency", user.getCurrency());
        SharedHelper.putKey(this, "measurementType", user.getMeasurement());
        SharedHelper.putKey(this, "walletBalance", String.valueOf(user.getWalletBalance()));
        SharedHelper.putKey(this, "userInfo", printJSON(user));

        SharedHelper.putKey(this, "name", user.getFirstName());
        SharedHelper.putKey(this, "email", user.getEmail());
        SharedHelper.putKey(this, "mobile", user.getMobile());

        SharedHelper.putKey(this, "referral_code", user.getReferral_unique_id());
        SharedHelper.putKey(this, "referral_count", user.getReferral_count());
        SharedHelper.putKey(this, "referral_text", user.getReferral_text());
        SharedHelper.putKey(this, "referral_total_text", user.getReferral_total_text());

        name.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
        sub_name.setText(user.getEmail());
        SharedHelper.putKey(this, PROFILE_IMG, user.getPicture());
        Glide.with(this)
                .load(BuildConfig.BASE_IMAGE_URL + user.getPicture())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder)
                        .dontAnimate()
                        .error(R.drawable.ic_user_placeholder))
                .into(picture);
        MvpApplication.showOTP = user.getRide_otp().equals("1");
//        if(user.getWalletBalance() <= 0){
//            startActivity(new Intent(this, WalletActivity.class));
//        }
    }


    @Override
    public void onDestinationSuccess(Object object) {

    }


    @Override
    public void onSuccess(SettingsResponse response) {
        if (response.getReferral().getReferral().equalsIgnoreCase("1")) navMenuVisibility(true);
        else navMenuVisibility(false);
    }

    private void navMenuVisibility(boolean visibility) {
        navigationView.getMenu().findItem(R.id.nav_invite_friend).setVisible(visibility);
    }

    @Override
    public void onSettingError(Throwable e) {
        navMenuVisibility(false);
    }





    public void ShowLogoutPopUp() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(getString(R.string.are_sure_you_want_to_logout)).setCancelable(false)
                .setPositiveButton(getString(R.string.yes), (dialog, id) -> presenter.logout(getKey(this, "user_id")))
                .setNegativeButton(getString(R.string.no), (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @SuppressLint("WrongConstant")
    @OnClick({R.id.menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu:
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else {
                    User user = new Gson().fromJson(getKey(this, "userInfo"), User.class);
                    if (user != null) {
                        name.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
                        sub_name.setText(user.getEmail());
                        SharedHelper.putKey(this, PROFILE_IMG, user.getPicture());
                        Glide.with(this)
                                .load(BuildConfig.BASE_IMAGE_URL + user.getPicture())
                                .apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.ic_user_placeholder))
                                .into(picture);
                    }
                    drawerLayout.openDrawer(Gravity.START);
                }

                break;


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.checkStatus();
    }

    public void mainAct(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("type","NORMAL");
        startActivity(intent);    }
    public void mainRental(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("type","RENTAL");
        startActivity(intent);
    }
    public void mainMoto(View view) {

        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("type","DELIVERY");
        startActivity(intent);
    }
}