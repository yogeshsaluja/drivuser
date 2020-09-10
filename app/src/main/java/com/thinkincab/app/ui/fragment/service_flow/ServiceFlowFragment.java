package com.thinkincab.app.ui.fragment.service_flow;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.thinkincab.app.MvpApplication;
import com.thinkincab.app.R;
import com.thinkincab.app.base.BaseFragment;
import com.thinkincab.app.chat.ChatActivity;
import com.thinkincab.app.common.CancelRequestInterface;
import com.thinkincab.app.data.SharedHelper;
import com.thinkincab.app.data.network.model.DataResponse;
import com.thinkincab.app.data.network.model.Datum;
import com.thinkincab.app.data.network.model.Message;
import com.thinkincab.app.data.network.model.Provider;
import com.thinkincab.app.data.network.model.ProviderService;
import com.thinkincab.app.data.network.model.ServiceType;
import com.thinkincab.app.ui.activity.main.MainActivity;
import com.thinkincab.app.ui.activity.payment.PaymentActivity;
import com.thinkincab.app.ui.fragment.cancel_ride.CancelRideDialogFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.thinkincab.app.MvpApplication.DATUM;
import static com.thinkincab.app.MvpApplication.RIDE_REQUEST;
import static com.thinkincab.app.MvpApplication.showOTP;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.CARD_ID;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.CARD_LAST_FOUR;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.PAYMENT_MODE;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.SRC_LAT;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.SRC_LONG;
import static com.thinkincab.app.common.Constants.Status.ARRIVED;
import static com.thinkincab.app.common.Constants.Status.PICKED_UP;
import static com.thinkincab.app.common.Constants.Status.STARTED;
import static com.thinkincab.app.data.SharedHelper.key.SOS_NUMBER;
import static com.thinkincab.app.ui.activity.payment.PaymentActivity.PICK_PAYMENT_METHOD;

public class ServiceFlowFragment extends BaseFragment
        implements ServiceFlowIView, CancelRequestInterface {

    @BindView(R.id.otp)
    TextView otp;
    @BindView(R.id.add_time)
    TextView add_time;
    @BindView(R.id.tv_two_hour)
    TextView tv_two_hour;
    @BindView(R.id.tv_four_hour)
    TextView tv_four_hour;
    @BindView(R.id.tv_eight_hour)
    TextView tv_eight_hour;

    @BindView(R.id.ll_hours)
    LinearLayout ll_hours;
    @BindView(R.id.ll_label)
    RelativeLayout ll_label;

    @BindView(R.id.avatar)
    CircleImageView avatar;
    @BindView(R.id.first_name)
    TextView firstName;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.rating)
    TextView rating;
    @BindView(R.id.cancel)
    ImageView cancel;
    @BindView(R.id.share_ride)
    ImageView sharedRide;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.service_type_name)
    TextView serviceTypeName;
    @BindView(R.id.service_number)
    TextView serviceNumber;
    @BindView(R.id.service_model)
    TextView serviceModel;
    @BindView(R.id.call)
    ImageView call;
    @BindView(R.id.call_sec)
    ImageView call_sec;
    @BindView(R.id.chat)
    ImageView chat;
    @BindView(R.id.provider_eta)
    TextView providerEta;
    @BindView(R.id.tvTimer)


    TextView tvTimer;
    @BindView(R.id.img_cardd)
    ImageView imgcardss;

    @BindView(R.id.img_cash)
    ImageView imgcash;

    @BindView(R.id.lefttime)
    TextView timeleft;

    private Runnable runnable;
    private Handler handler;
    private int delay = 2 * 60 * 1000;
    public int PERMISSIONS_REQUEST_PHONE = 4;

    private String providerPhoneNumber = null;
    private String shareRideText = "";
    private ServiceFlowPresenter<ServiceFlowFragment> presenter = new ServiceFlowPresenter<>();
    private CancelRequestInterface callback;
    private boolean loaded = false;


    private Handler customHandler = new Handler();
    private long timerInHandler = 0L;

    public ServiceFlowFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_service_flow;
    }

    @Override
    public View initView(View view) {
        ButterKnife.bind(this, view);
        callback = this;
        presenter.attachView(this);


        if (DATUM != null) initView(DATUM);
        return view;
    }

    @Override
    public void onDestroyView() {
        presenter.onDetach();
        super.onDestroyView();
    }

    String paymentMode;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PAYMENT_METHOD && resultCode == Activity.RESULT_OK) {
            RIDE_REQUEST.put(PAYMENT_MODE, data.getStringExtra("payment_mode"));
            paymentMode = data.getStringExtra("payment_mode");

            System.out.println("RRR PAMENT_MODE = " + data.getStringExtra("payment_mode"));

            if (data.getStringExtra("payment_mode").equals("CARD")) {
                RIDE_REQUEST.put(CARD_ID, data.getStringExtra("card_id"));
                RIDE_REQUEST.put(CARD_LAST_FOUR, data.getStringExtra("card_last_four"));
            }
            changecard(data.getStringExtra("card_id"));
        }
    }

    @Override
    public void onError(Throwable throwable) {
        super.onError(throwable);
        hideLoading();
    }

    @OnClick({R.id.sos, R.id.cancel, R.id.share_ride, R.id.call, R.id.call_sec, R.id.chat, R.id.add_time, R.id.tv_two_hour, R.id.tv_four_hour, R.id.tv_eight_hour, R.id.img_cardd, R.id.img_cash})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_cardd:

                startActivityForResult(new Intent(getActivity(), PaymentActivity.class), PICK_PAYMENT_METHOD);
                break;

            case R.id.img_cash:
                changecash();
                break;

            case R.id.sos:
                sos();
                break;
            case R.id.cancel:
                CancelRideDialogFragment cancelRideFragment = new CancelRideDialogFragment(callback);
                cancelRideFragment.show(baseActivity().getSupportFragmentManager(), cancelRideFragment.getTag());
                break;
            case R.id.share_ride:
                sharedRide();
                break;
            case R.id.call:
                callPhoneNumber(providerPhoneNumber);
                break;
            case R.id.call_sec:
                callPhoneNumber(providerPhoneNumber);
                break;
            case R.id.add_time:

                ll_hours.setVisibility(View.VISIBLE);

                break;
            case R.id.tv_two_hour:
                HashMap<String, Object> map = new HashMap<>(RIDE_REQUEST);
                map.put("request_id", DATUM.getId());
                map.put("rental_hours", 15);
                tv_two_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.gradent_shape));
                tv_four_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_dark));

                tv_eight_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_dark));

                presenter.extendTime(map);


                break;
            case R.id.tv_four_hour:
                tv_two_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_dark));
                tv_four_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.gradent_shape));
                tv_eight_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_dark));

                HashMap<String, Object> mapfour = new HashMap<>(RIDE_REQUEST);
                mapfour.put("request_id", DATUM.getId());
                mapfour.put("rental_hours", 30);
                presenter.extendTime(mapfour);

                break;
            case R.id.tv_eight_hour:
                tv_two_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_dark));
                tv_four_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_dark));
                tv_eight_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.gradent_shape));

                HashMap<String, Object> mapeight = new HashMap<>(RIDE_REQUEST);
                mapeight.put("request_id", DATUM.getId());
                mapeight.put("rental_hours", 120);
                presenter.extendTime(mapeight);

                break;
            case R.id.chat:
                if (DATUM != null) {
                    Intent i = new Intent(baseActivity(), ChatActivity.class);
                    i.putExtra("request_id", String.valueOf(DATUM.getId()));
                    startActivity(i);
                }
                break;
        }
    }

    private void changecash() {
        HashMap<String, String> params = new HashMap<>();
        params.put("request_id", DATUM.getId() + "");
        params.put("use_wallet", "0");
        params.put("payment_mode", "CASH");
        params.put("card_id", "");
        presenter.changePayment(params);
        showLoading();
    }


    private void changecard(String carddara) {

        HashMap<String, String> params = new HashMap<>();
        params.put("request_id", DATUM.getId() + "");
        params.put("use_wallet", "0");
        params.put("payment_mode", "CARD");
        params.put("card_id", carddara);
        presenter.changePayment(params);
        showLoading();
    }

    @SuppressLint({"StringFormatInvalid", "RestrictedApi"})
    private void initView(Datum datum) {
        Provider provider = datum.getProvider();
        if (provider != null) {
            firstName.setText(String.format("%s %s", provider.getFirstName(), provider.getLastName()));
            rating.setText("" + Float.parseFloat(provider.getRating()));
            if (provider.getAvatarNew()!=null){
                Glide.with(baseActivity())
                        .load(provider.getAvatar())
                        .apply(RequestOptions
                                .placeholderOf(R.drawable.ic_user_placeholder)
                                .dontAnimate()
                                .error(R.drawable.ic_user_placeholder))
                        .into(avatar);

            }else {
                avatar.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_placeholder));

            }
            providerPhoneNumber = provider.getMobile();
        }

        ServiceType serviceType = datum.getServiceType();
        if (serviceType != null) {
            serviceTypeName.setText(serviceType.getName());
            Glide.with(baseActivity())
                    .load(serviceType.getImage())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_car)
                            .dontAnimate()
                            .error(R.drawable.ic_car))
                    .into(image);
        }

        chat.setVisibility(PICKED_UP.equalsIgnoreCase(datum.getStatus()) ? View.GONE : View.VISIBLE);

        ProviderService providerService = datum.getProviderService();
        if (providerService != null) {
            serviceNumber.setText(providerService.getServiceNumber());
            serviceModel.setText(providerService.getServiceModel());
        }

        otp.setText(getString(R.string.otp_, datum.getOtp()));
        otp.setVisibility(showOTP ? View.VISIBLE : View.GONE);

        switch (datum.getStatus()) {
            case STARTED:
                status.setText(R.string.driver_accepted_your_request);
                break;
            case ARRIVED:
                status.setText(R.string.driver_has_arrived_your_location);
                break;
            case PICKED_UP:
                 status.setText(R.string.you_are_on_ride);
                cancel.setVisibility(View.INVISIBLE);
                sharedRide.setVisibility(View.VISIBLE);
                call.setVisibility(View.GONE);
                ll_label.setVisibility(View.GONE);
                call_sec.setVisibility(View.VISIBLE);
                imgcardss.setVisibility(View.VISIBLE);
                imgcash.setVisibility(View.VISIBLE);

                if (MainActivity.type.equals("RENTAL")) {
                    add_time.setVisibility(View.VISIBLE);
                    tvTimer.setVisibility(View.VISIBLE);
                    timeleft.setVisibility(View.VISIBLE);


                }
                if (!TextUtils.isEmpty(datum.getStartedAt()) && !loaded) {
                    startDate = getTimestampFromdate(datum.getStartedAt());
                    if (count!=null)
                    count.cancel();

                    if (datum.getRentalHours() != null) {

                        timeleft.setVisibility(View.VISIBLE);
                        timeleft.setText("Rent for: " + (Integer.parseInt(datum.getRentalHours()) / 60) + "hrs. ");
                        startDate = Long.parseLong(startDate) + Integer.parseInt(datum.getRentalHours()) * 60 * 1000 + "";

                    } else timeleft.setVisibility(View.GONE);


                    count = new CountDownTimer(1000000000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (!isRemoving()) {
                                if (System.currentTimeMillis() > Long.parseLong(startDate)) {
                                    secondSplitUp(0, tvTimer);
                                } else {
                                    secondSplitUp((Long.parseLong(startDate) - System.currentTimeMillis()) / 1000, tvTimer);
                                }


                            }

                        }

                        @Override
                        public void onFinish() {

                        }
                    };
                    count.start();


                    loaded = true;

                }
                break;
            default:
                break;
        }

        if (STARTED.equalsIgnoreCase(datum.getStatus())) {
            LatLng source = new LatLng(datum.getProvider().getLatitude(), datum.getProvider().getLongitude());
            LatLng destination = new LatLng(datum.getSLatitude(), datum.getSLongitude());
            ((MainActivity) Objects.requireNonNull(getActivity())).drawRoute(source, destination);
        } else {
            LatLng origin = new LatLng(datum.getSLatitude(), datum.getSLongitude());
            LatLng destination = new LatLng(datum.getDLatitude(), datum.getDLongitude());
            ((MainActivity) Objects.requireNonNull(getActivity())).drawRoute(origin, destination);
        }

    }

    CountDownTimer count;
    String startDate;

    public String getTimestampFromdate(String dateandtime) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(dateandtime);
            return String.valueOf(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    private long getUpdatedTime(Date d1, Date d2) {
        long seconds = (d2.getTime() - d1.getTime()) / 1000;

        return seconds;
    }


    private void sos() {
        new AlertDialog.Builder(getContext())
                .setTitle(getContext().getResources().getString(R.string.sos_alert))
                .setMessage(R.string.are_sure_you_want_to_emergency_alert)
                .setCancelable(true)
                .setPositiveButton(getContext().getResources().getString(R.string.yes), (dialog, which) -> callPhoneNumber(SharedHelper.getKey(getContext(), SOS_NUMBER)))
                .setNegativeButton(getContext().getResources().getString(R.string.no), (dialog, which) -> dialog.cancel())
                .show();
    }

    private void callPhoneNumber(String mobileNumber) {
        if (mobileNumber != null && !mobileNumber.isEmpty()) {
            if (ActivityCompat.checkSelfPermission(baseActivity(), Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED)
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNumber)));
            else ActivityCompat.requestPermissions(baseActivity(),
                    new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


   void   updateTime(Datum datum){
        if (!TextUtils.isEmpty(datum.getStartedAt()) && !loaded) {
            startDate = getTimestampFromdate(datum.getStartedAt());


            if (datum.getRentalHours() != null) {

                timeleft.setVisibility(View.VISIBLE);
                timeleft.setText("Rent for: " + (Integer.parseInt(datum.getRentalHours()) / 60) + "hrs. ");
                startDate = Long.parseLong(startDate) + Integer.parseInt(datum.getRentalHours()) * 60 * 1000 + "";

            } else timeleft.setVisibility(View.GONE);


            count = new CountDownTimer(1000000000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (!isRemoving()) {
                        if (System.currentTimeMillis() > Long.parseLong(startDate)) {
                            secondSplitUp(0, tvTimer);
                        } else {
                            secondSplitUp((Long.parseLong(startDate) - System.currentTimeMillis()) / 1000, tvTimer);
                        }


                    }

                }

                @Override
                public void onFinish() {

                }
            };
            count.start();


            loaded = true;

        }

    }


    @Override
    public void onPause() {
        super.onPause();
    }

    private void sharedRide() {
        try {
            if (DATUM != null) {
                String appName = getString(R.string.app_name) + " " + getString(R.string.share_ride);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                LatLng myloc = new LatLng((Double) RIDE_REQUEST.get(SRC_LAT), (Double) RIDE_REQUEST.get(SRC_LONG));

                if (myloc != null) {
                    shareRideText = getString(R.string.app_name) + ": "
                            + DATUM.getUser().getFirstName() + " " + DATUM.getUser().getLastName() + " is traveling with "
                            + DATUM.getServiceType().getName() + ". and the current location "
                            + "http://maps.google.com/maps?saddr=" + myloc.latitude + "," + myloc.longitude + "&daddr=" + DATUM.getDLatitude() + "," + DATUM.getDLongitude() + "";
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareRideText);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, appName);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            }
        } catch (Exception e) {
            Toast.makeText(baseActivity(), "applications not found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_PHONE)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(baseActivity(), "Permission Granted. Try Again!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cancelRequestMethod() {
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        if (handler != null) handler.removeCallbacks(runnable);
        if (count != null) {

            count.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (handler != null) handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {


        if (MainActivity.type.equals("RENTAL")) {
           // tvTimer.setVisibility(View.VISIBLE);
           // timeleft.setVisibility(View.VISIBLE);
        } else {
            tvTimer.setVisibility(View.GONE);
            timeleft.setVisibility(View.GONE);

        }

        System.out.println("RRR ServiceFlowFragment.onResume");
        super.onResume();

        if (!TextUtils.isEmpty(DATUM.getStartedAt()) && DATUM.getStatus().equalsIgnoreCase(PICKED_UP) && !loaded) {
            String startDate = getTimestampFromdate(DATUM.getStartedAt());
            Log.e("TAG", "initView: " + startDate);

            Date myDate = new Date(Long.parseLong(startDate));
            Date dateNew = new Date(System.currentTimeMillis());
            timerInHandler = timerInHandler + getUpdatedTime(myDate, dateNew);
            Log.e("TAG", "initView: " + timerInHandler);


        }

        handler = new Handler();
        runnable = () -> {
            try {
                LatLng src = null;
                LatLng des = null;

                if (DATUM.getStatus().equalsIgnoreCase(STARTED)
                        || DATUM.getStatus().equalsIgnoreCase(ARRIVED)) {
                    src = new LatLng((Double) RIDE_REQUEST.get(SRC_LAT), (Double) RIDE_REQUEST.get(SRC_LONG));
                    des = SharedHelper.getCurrentLocation(getContext());
                } else if (DATUM.getStatus().equalsIgnoreCase(PICKED_UP)) {
                    src = SharedHelper.getCurrentLocation(getContext());
                    des = new LatLng(DATUM.getDLatitude(), DATUM.getDLatitude());
                }

                System.out.println("RRR src = " + src + " dest = " + des);

                GoogleDirection
                        .withServerKey(getString(R.string.google_map_key))
                        .from(src)
                        .to(des)
                        .transportMode(TransportMode.DRIVING)
                        .execute(new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(Direction direction, String rawBody) {
                                if (direction.isOK()) {
                                    Route route = direction.getRouteList().get(0);
                                    if (!route.getLegList().isEmpty()) {
                                        Leg leg = route.getLegList().get(0);
                                        providerEta.setVisibility(View.VISIBLE);
                                        String arrivalTime = String.valueOf(leg.getDuration().getText());
                                        if (arrivalTime.contains("hours"))
                                            arrivalTime = arrivalTime.replace("hours", "h\n");
                                        else if (arrivalTime.contains("hour"))
                                            arrivalTime = arrivalTime.replace("hour", "h\n");
                                        if (arrivalTime.contains("mins"))
                                            arrivalTime = arrivalTime.replace("mins", "min");
                                        providerEta.setText(String.format("ETA : %s", arrivalTime));

                                        System.out.println("RRR src ETA = " + String.format("ETA : %s", arrivalTime));
                                    }
                                }
                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {
                                t.printStackTrace();
                                System.out.println("RRR ServiceFlowFragment.onDirectionFailure");
                            }
                        });
                handler.postDelayed(runnable, delay);
            } catch (Exception e) {
                handler.postDelayed(runnable, 100);
                e.printStackTrace();
            }
        };
        handler.postDelayed(runnable, 100);
    }


    @Override
    public void onSuccess(Object o) {


        hideLoading();
        if (o instanceof Message) {
            if (((Message) o).getMessage().equals("Payment Mode Changed ")) {

                Toast.makeText(getContext(), ((Message) o).getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onSuccessMinutes(DataResponse result) {
        hideLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loaded=false;
                initView(result.getData().get(0));
            }
        },3000);



    }


    public void secondSplitUp(long biggy, TextView tvTimer) {
        int hours = (int) biggy / 3600;
        int sec = (int) biggy - hours * 3600;
        int mins = sec / 60;
        sec = sec - mins * 60;
        tvTimer.setText(String.format("%02d:", hours)
                + String.format("%02d:", mins)
                + String.format("%02d", sec));

    }


}
