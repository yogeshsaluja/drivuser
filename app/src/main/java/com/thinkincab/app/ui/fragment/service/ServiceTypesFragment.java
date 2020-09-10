package com.thinkincab.app.ui.fragment.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.thinkincab.app.MvpApplication;
import com.thinkincab.app.R;
import com.thinkincab.app.base.BaseActivity;
import com.thinkincab.app.base.BaseFragment;
import com.thinkincab.app.common.Constants;
import com.thinkincab.app.common.EqualSpacingItemDecoration;
import com.thinkincab.app.data.network.APIClient;
import com.thinkincab.app.data.network.model.EstimateFare;
import com.thinkincab.app.data.network.model.Provider;
import com.thinkincab.app.data.network.model.Service;
import com.thinkincab.app.data.network.model.UserAddress;
import com.thinkincab.app.ui.activity.location_pick.LocationPickActivity;
import com.thinkincab.app.ui.activity.main.MainActivity;
import com.thinkincab.app.ui.activity.payment.PaymentActivity;
import com.thinkincab.app.ui.adapter.ServiceAdapter;
import com.thinkincab.app.ui.fragment.RateCardFragment;
import com.thinkincab.app.ui.fragment.book_ride.BookRideFragment;
import com.thinkincab.app.ui.fragment.schedule.ScheduleFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thinkincab.app.MvpApplication.DATUM;
import static com.thinkincab.app.MvpApplication.RIDE_REQUEST;
import static com.thinkincab.app.common.Constants.BroadcastReceiver.INTENT_FILTER;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.CARD_ID;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.CARD_LAST_FOUR;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.DEST_ADD;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.DEST_LAT;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.DEST_LONG;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.DISTANCE_VAL;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.PAYMENT_MODE;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.SERVICE_TYPE;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.SRC_ADD;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.SRC_LAT;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.SRC_LONG;
import static com.thinkincab.app.common.Constants.Status.EMPTY;
import static com.thinkincab.app.common.Constants.Status.PICKED_UP;
import static com.thinkincab.app.common.Constants.Status.SERVICE;
import static com.thinkincab.app.data.SharedHelper.getKey;
import static com.thinkincab.app.data.SharedHelper.getProviders;
import static com.thinkincab.app.data.SharedHelper.putKey;
import static com.thinkincab.app.ui.activity.main.MainActivity.CURRENT_STATUS;
import static com.thinkincab.app.ui.activity.payment.PaymentActivity.PICK_PAYMENT_METHOD;

public class ServiceTypesFragment extends BaseFragment implements ServiceTypesIView {

    @BindView(R.id.service_rv)
    RecyclerView serviceRv;
    @BindView(R.id.ll_hours)
    LinearLayout ll_hours;
    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.view)
    View viewGreyLine;
    @BindView(R.id.main)
    RelativeLayout main;
    @BindView(R.id.tv_title_category)
    TextView tv_title_category;



    @BindView(R.id.ll_work)
    LinearLayout ll_work;
    @BindView(R.id.ll_moto)
    LinearLayout ll_moto;
    @BindView(R.id.ed_note)
    EditText ed_note;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_home)
    TextView tv_home;
    @BindView(R.id.tv_work)
    TextView tv_work;


    @BindView(R.id.tv_two_hour)
    TextView tv_two_hour;
    @BindView(R.id.tv_four_hour)
    TextView tv_four_hour;
    @BindView(R.id.tv_eight_hour)
    TextView tv_eight_hour;
    @BindView(R.id.ride_now)
    Button ride_now;
    @BindView(R.id.source)
    TextView msource;
    @BindView(R.id.destination)
    TextView mdestination;

    @BindView(R.id.llDropLocationContainer)
    LinearLayout destination_layout;


    @BindView(R.id.capacity)
    TextView capacity;
    @BindView(R.id.payment_type)
    TextView paymentType;
    @BindView(R.id.error_layout)
    TextView errorLayout;
    Unbinder unbinder;
    ServiceAdapter adapter;
    List<Service> mServices = new ArrayList<>();
    @BindView(R.id.use_wallet)
    CheckBox useWallet;
    @BindView(R.id.wallet_balance)
    TextView walletBalance;
    @BindView(R.id.surge_value)
    TextView surgeValue;
    @BindView(R.id.tv_demand)
    TextView tvDemand;
    @BindView(R.id.get_pricing)
    Button get_princing;

    private ServiceTypesPresenter<ServiceTypesFragment> presenter = new ServiceTypesPresenter<>();
    private boolean isFromAdapter = true;
    private int servicePos = 0;
    private EstimateFare mEstimateFare;
    private double walletAmount;
    private int surge;

    private ServiceListener mListener = new ServiceListener() {
        @Override
        public void whenClicked(int pos) {
            try {
                MvpApplication.marker = mServices.get(pos).getMarker();
                isFromAdapter = true;
                servicePos = pos;
                String key = mServices.get(pos).getName() + mServices.get(pos).getId();
                RIDE_REQUEST.put(SERVICE_TYPE, mServices.get(pos).getId());
                ServiceTypesFragment.this.showLoading();
                ServiceTypesFragment.this.estimatedApiCall();
                List<Provider> providers = new ArrayList<>();

                for (Provider provider : getProviders(Objects.requireNonNull(ServiceTypesFragment.this.getActivity())))
                    if (provider.getProviderService().getServiceTypeId() == mServices.get(pos).getId())
                        providers.add(provider);

                ((MainActivity) ServiceTypesFragment.this.getActivity()).addSpecificProviders(providers, key);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    private UserAddress home = null, work = null;


    public ServiceTypesFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_service;
    }

    @Override
    public View initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        RIDE_REQUEST.put("rental_hours", "120");
        if (!MainActivity.type.equalsIgnoreCase("RENTAL")) {
            main.setVisibility(View.VISIBLE);

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.addressResponse != null) {
                    home = (MainActivity.addressResponse.getHome().isEmpty()) ? null : (MainActivity.addressResponse.getHome().get(MainActivity.addressResponse.getHome().size() - 1));
                    work = (MainActivity.addressResponse.getWork().isEmpty()) ? null : (MainActivity.addressResponse.getWork().get(MainActivity.addressResponse.getWork().size() - 1));

                    if (!MainActivity.type.equalsIgnoreCase("RENTAL")) {
                        ll_home.setVisibility(View.VISIBLE);

                        if (home != null && home.getLatitude() != 0 && home.getLongitude() != 0)
                            tv_home.setText(home.getAddress());
                        if (work != null && work.getLatitude() != 0 && work.getLongitude() != 0)
                            tv_work.setText(work.getAddress());

                    }


                    if (!MainActivity.address.equalsIgnoreCase("")) {
                        msource.setText(MainActivity.address);
                    }

                    if (MainActivity.type.equalsIgnoreCase("RENTAL")) {
                        destination_layout.setVisibility(View.GONE);
                        ll_home.setVisibility(View.GONE);
                         ll_work.setVisibility(View.GONE);
                        viewGreyLine.setVisibility(View.GONE);

                        presenter.services();

                    }

                }

            }
        }, 2000);


        return view;
    }


    @OnClick({R.id.tv_work, R.id.tv_home, R.id.payment_type, R.id.get_pricing, R.id.source, R.id.destination, R.id.schedule_ride, R.id.ride_now, R.id.tv_two_hour, R.id.tv_four_hour, R.id.tv_eight_hour})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.payment_type:
                ((MainActivity) Objects.requireNonNull(getActivity())).updatePaymentEntities();
                startActivityForResult(new Intent(getActivity(), PaymentActivity.class), PICK_PAYMENT_METHOD);
                break;
            case R.id.tv_home:
                if (home != null) {
                    RIDE_REQUEST.put(DEST_ADD, home.getAddress());
                    RIDE_REQUEST.put(DEST_LAT, home.getLatitude());
                    RIDE_REQUEST.put(DEST_LONG, home.getLongitude());
                    mdestination.setText(home.getAddress());

                    if (!TextUtils.isEmpty(msource.getText().toString())) {
                        presenter.services();
                    }
                }


                break;
            case R.id.tv_work:
                if (work != null) {
                    RIDE_REQUEST.put(DEST_ADD, work.getAddress());
                    RIDE_REQUEST.put(DEST_LAT, work.getLatitude());
                    RIDE_REQUEST.put(DEST_LONG, work.getLongitude());
                    mdestination.setText(work.getAddress());
                    if (!TextUtils.isEmpty(msource.getText().toString())) {
                        presenter.services();
                    }

                }

                break;

            case R.id.get_pricing:
                if (adapter != null) {
                    isFromAdapter = false;
                    Service service = adapter.getSelectedService();
                    if (service != null) {
                        RIDE_REQUEST.put(SERVICE_TYPE, service.getId());
                        if (RIDE_REQUEST.containsKey(SERVICE_TYPE) && RIDE_REQUEST.get(SERVICE_TYPE) != null) {
                            showLoading();
                             estimatedApiCall();
                        }
                    }
                }
                break;


            case R.id.source:
                ((MainActivity) getContext()).CURRENT_STATUS = EMPTY;
                Intent sourceIntent = new Intent(getContext(), LocationPickActivity.class);
                sourceIntent.putExtra("actionName", Constants.LocationActions.SELECT_SOURCE);
                startActivityForResult(sourceIntent, 3);
                break;
            case R.id.destination:
                ((MainActivity) getContext()).CURRENT_STATUS = EMPTY;
                Intent intent = new Intent(getContext(), LocationPickActivity.class);
                intent.putExtra("actionName", Constants.LocationActions.SELECT_DESTINATION);
                startActivityForResult(intent, 3);
                break;
            case R.id.tv_two_hour:
                RIDE_REQUEST.put("rental_hours", "120");

                tv_two_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.gradent_shape));
                tv_four_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_dark));
                tv_eight_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_dark));


                break;
            case R.id.tv_four_hour:
                RIDE_REQUEST.put("rental_hours", "240");

                tv_two_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_dark));
                tv_four_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.gradent_shape));
                tv_eight_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_dark));


                break;
            case R.id.tv_eight_hour:
                RIDE_REQUEST.put("rental_hours", "480");

                tv_two_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_dark));
                tv_four_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_dark));
                tv_eight_hour.setBackground(getActivity().getResources().getDrawable(R.drawable.gradent_shape));


                break;
            case R.id.schedule_ride:
                ((MainActivity) Objects.requireNonNull(getActivity())).changeFragment(new ScheduleFragment());
                break;
            case R.id.ride_now:
                sendRequest();
                break;
            default:
                break;
        }
    }

    private void estimatedApiCall() {
        if (MainActivity.type.equalsIgnoreCase("RENTAL")){
            RIDE_REQUEST.put(DEST_LAT, RIDE_REQUEST.get(SRC_LAT));
            RIDE_REQUEST.put(DEST_LONG, RIDE_REQUEST.get(SRC_LONG));
            RIDE_REQUEST.put(DEST_ADD, RIDE_REQUEST.get(SRC_ADD));


        }

        Call<EstimateFare> call = APIClient.getAPIClient().estimateFare(RIDE_REQUEST);
        call.enqueue(new Callback<EstimateFare>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<EstimateFare> call,
                                   @NonNull Response<EstimateFare> response) {
                if (ServiceTypesFragment.this.isVisible()) {
                    hideLoading();
                    if (response.body() != null) {
                        EstimateFare estimateFare = response.body();

                        RateCardFragment.SERVICE = estimateFare.getService();
                        mEstimateFare = estimateFare;
                        surge = estimateFare.getSurge();
                        walletAmount = estimateFare.getWalletBalance();
                        if (getContext() != null)
                            putKey(getContext(), "wallet", String.valueOf(estimateFare.getWalletBalance()));
                        if (walletAmount == 0) walletBalance.setVisibility(View.GONE);
                        else {
                            walletBalance.setVisibility(View.VISIBLE);
                            walletBalance.setText(getNewNumberFormat(Double.parseDouble(String.valueOf(walletAmount))));
                        }
                        if (surge == 0) {
                            surgeValue.setVisibility(View.GONE);
                            tvDemand.setVisibility(View.GONE);
                        } else {
                            surgeValue.setVisibility(View.VISIBLE);
                            surgeValue.setText(estimateFare.getSurgeValue());
                            tvDemand.setVisibility(View.VISIBLE);
                        }
                        if (isFromAdapter) {
                            mServices.get(servicePos).setEstimatedTime(estimateFare.getTime());
                            RIDE_REQUEST.put(DISTANCE_VAL, estimateFare.getDistance());
                            adapter.setEstimateFare(mEstimateFare);
                            adapter.notifyDataSetChanged();
                            if (mServices.isEmpty()) errorLayout.setVisibility(View.VISIBLE);
                            else errorLayout.setVisibility(View.GONE);
                        } else if (adapter != null) {
                            Service service = adapter.getSelectedService();
                            if (service != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString("service_name", service.getName());
                                bundle.putSerializable("mService", service);
                                bundle.putSerializable("estimate_fare", estimateFare);
                                bundle.putDouble("use_wallet", walletAmount);
                                BookRideFragment bookRideFragment = new BookRideFragment();
                                bookRideFragment.setArguments(bundle);
                                ((MainActivity) Objects.requireNonNull(getActivity())).changeFragment(bookRideFragment);
                            }
                        }
                    } else if (response.raw().code() == 500) try {
                        JSONObject object = new JSONObject(response.errorBody().string());
                        if (object.has("error"))
                            Toast.makeText(baseActivity(), object.optString("error"), Toast.LENGTH_SHORT).show();
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<EstimateFare> call, @NonNull Throwable t) {
                hideLoading();
                onErrorBase(t);
            }
        });
    }

    @Override
    public void onSuccess(List<Service> services) {
        try {
            hideLoading();
            if (services != null && !services.isEmpty()) {
                RIDE_REQUEST.put(SERVICE_TYPE, 1);
                if (MainActivity.type.equalsIgnoreCase("NORMAL")) {
                    ll_hours.setVisibility(View.GONE);
                } else if (MainActivity.type.equalsIgnoreCase("RENTAL")) {
                    ll_home.setVisibility(View.GONE);
                    ll_work.setVisibility(View.GONE);
                    tv_title.setText("Rent By Hours");
                    ll_hours.setVisibility(View.VISIBLE);
                    main.setVisibility(View.VISIBLE);


                } else {
                    tv_title.setText("Moto Express");
                    tv_title.setTextColor(getActivity().getColor(R.color.pink));
                    ll_home.setVisibility(View.GONE);
                    ll_work.setVisibility(View.GONE);
                    ll_hours.setVisibility(View.GONE);
                    serviceRv.setVisibility(View.GONE);
                    ll_moto.setVisibility(View.VISIBLE);

                }
                mServices.clear();
                for (Service service : services) {
                    if (MainActivity.type.equalsIgnoreCase(service.getType())) {
                        mServices.add(service);
                    }
                }
                // mServices.addAll(services);

                try {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            for (Service s : mServices) {
                                String key = s.getName() + s.getId();
                                if (!TextUtils.isEmpty(s.getMarker()))
                                    if (TextUtils.isEmpty(getKey(Objects.requireNonNull(ServiceTypesFragment.this.getActivity()), key))) {
                                        Bitmap b = ((BaseActivity) ServiceTypesFragment.this.getActivity()).getBitmapFromURL(s.getMarker());
                                        Log.e("get image", String.valueOf(b));
                                        if (b != null)
                                            putKey(ServiceTypesFragment.this.getActivity(), key, ((BaseActivity) ServiceTypesFragment.this.getActivity()).encodeBase64(b));
                                    }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (mServices.size() > 0) {
                    ride_now.setVisibility(View.VISIBLE);
                } else ride_now.setVisibility(View.GONE);

                adapter = new ServiceAdapter(getActivity(), mServices, mListener, capacity, mEstimateFare);
                serviceRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                serviceRv.setItemAnimator(new DefaultItemAnimator());
                serviceRv.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.HORIZONTAL));
                serviceRv.setAdapter(adapter);

                if (adapter != null) {
                    Service mService = adapter.getSelectedService();
                    if (mService != null) RIDE_REQUEST.put(SERVICE_TYPE, mService.getId());
                }
                mListener.whenClicked(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable e) {
//        handleError(e);

        get_princing.setVisibility(View.GONE);
        new AlertDialog.Builder(getContext())
                .setTitle("Attention")
                .setCancelable(false)
                .setIcon(R.drawable.ic_checked)
                .setMessage("\n" + "We have no services in this region.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((MainActivity) getActivity()).finish();
                    }
                })
                .show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PAYMENT_METHOD && resultCode == Activity.RESULT_OK) {
            RIDE_REQUEST.put(PAYMENT_MODE, data.getStringExtra("payment_mode"));
            if (data.getStringExtra("payment_mode").equals("CARD")) {
                RIDE_REQUEST.put(CARD_ID, data.getStringExtra("card_id"));
                RIDE_REQUEST.put(CARD_LAST_FOUR, data.getStringExtra("card_last_four"));
            }
            initPayment(paymentType);
        }
        if (requestCode == 3) if (resultCode == Activity.RESULT_OK) {
            if (RIDE_REQUEST.containsKey(SRC_ADD))
                msource.setText(String.valueOf(RIDE_REQUEST.get(SRC_ADD)));
            else msource.setText("");
            if (RIDE_REQUEST.containsKey(DEST_ADD))
                mdestination.setText(String.valueOf(RIDE_REQUEST.get(DEST_ADD)));
            else mdestination.setText("");

            if (RIDE_REQUEST.containsKey(SRC_ADD)
                    && RIDE_REQUEST.containsKey(DEST_ADD)
                    && CURRENT_STATUS.equalsIgnoreCase(EMPTY)) {
                CURRENT_STATUS = SERVICE;
                // ((MainActivity)getContext()).changeFlow(CURRENT_STATUS);
                LatLng origin = new LatLng((Double) RIDE_REQUEST.get(SRC_LAT), (Double) RIDE_REQUEST.get(SRC_LONG));
                LatLng destination = new LatLng((Double) RIDE_REQUEST.get(DEST_LAT), (Double) RIDE_REQUEST.get(DEST_LONG));
                ((MainActivity) getContext()).drawRoute(origin, destination);
            } else if (RIDE_REQUEST.containsKey(DEST_ADD)
                    && !RIDE_REQUEST.get(DEST_ADD).equals("")
                    && CURRENT_STATUS.equalsIgnoreCase(PICKED_UP))
                ((MainActivity) getContext()).extendRide();

            presenter.services();
        }
    }

    private void sendRequest() {
        HashMap<String, Object> map = new HashMap<>(RIDE_REQUEST);
        map.put("use_wallet", useWallet.isChecked() ? 1 : 0);

        if (MainActivity.type.equalsIgnoreCase("RENTAL")) {
            RIDE_REQUEST.put(DEST_LAT, RIDE_REQUEST.get(SRC_LAT));
            RIDE_REQUEST.put(DEST_LONG, RIDE_REQUEST.get(SRC_LONG));
            RIDE_REQUEST.put(DEST_ADD, RIDE_REQUEST.get(SRC_ADD));


        }
        if (MainActivity.type.equalsIgnoreCase("DELIVERY")) {

            map.put("description", ed_note.getText().toString());
            map.remove("rental_hours");
        } else if (MainActivity.type.equalsIgnoreCase("NORMAL")) {
            map.remove("rental_hours");
        }
        showLoading();
        presenter.rideNow(map);
    }

    @Override
    public void onSuccess(@NonNull Object object) {
        try {
            hideLoading();
            baseActivity().sendBroadcast(new Intent(INTENT_FILTER));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        //presenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        initPayment(paymentType);
    }

    public interface ServiceListener {
        void whenClicked(int pos);
    }

}
