package com.thinkincab.app.ui.fragment.searching;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.thinkincab.app.MvpApplication;
import com.thinkincab.app.R;
import com.thinkincab.app.base.BaseBottomSheetDialogFragment;
import com.thinkincab.app.data.network.model.DataResponse;
import com.thinkincab.app.data.network.model.Datum;
import com.thinkincab.app.ui.activity.main.MainActivity;
import com.thinkincab.app.ui.adapter.BiddingAdapter;

import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.thinkincab.app.MvpApplication.DATUM;
import static com.thinkincab.app.common.Constants.BroadcastReceiver.INTENT_FILTER;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.DEST_ADD;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.DEST_LAT;
import static com.thinkincab.app.common.Constants.RIDE_REQUEST.DEST_LONG;
import static com.thinkincab.app.common.Constants.Status.EMPTY;

public class SearchingFragment extends BaseBottomSheetDialogFragment implements SearchingIView {

    private SearchingPresenter<SearchingFragment> presenter = new SearchingPresenter<>();
    @BindView(R.id.rv_request)
    RecyclerView bidding;
    public SearchingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_offer;
    }

    @Override
    public void initView(View view) {
        setCancelable(false);
        ButterKnife.bind(this, view);
        presenter.attachView(this);
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {

                                        //  populateAdapter();
                                        }
                                  },
                3*1000, 1000 * 1 );



    }

/*
    private void populateAdapter() {
        DataResponse dataResponse=((MainActivity) Objects.requireNonNull(getContext())).checkStatusResponse;
        if (dataResponse!=null){
            bidding.setAdapter(new BiddingAdapter(dataResponse.getBids(),this));

        }
    }
*/

    public void callAccept(Double amount,Integer id){
        HashMap<String, Object> map = new HashMap<>();
        map.put("request_id", DATUM.getId());

        map.put("provider_id", id);
        map.put("amount", amount);


        presenter.acceptRequest(map);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

   /* @OnClick(R.id.cancel)
    public void onViewClicked() {
        alertCancel();
    }
*/
    private void alertCancel() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.are_sure_you_want_to_cancel_the_request)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    if (DATUM != null) {
                        showLoading();
                        Datum datum = DATUM;
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("request_id", datum.getId());
                        presenter.cancelRequest(map);
                    }
                }).setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                .show();
    }

    @Override
    public void onSuccess(Object object) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        MvpApplication.RIDE_REQUEST.remove(DEST_ADD);
        MvpApplication.RIDE_REQUEST.remove(DEST_LAT);
        MvpApplication.RIDE_REQUEST.remove(DEST_LONG);

        baseActivity().sendBroadcast(new Intent(INTENT_FILTER));
        ((MainActivity) Objects.requireNonNull(getContext())).changeFlow(EMPTY);
        dismissAllowingStateLoss();
    }

    @Override
    public void onSuccessData(DataResponse object) {

    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
        baseActivity().sendBroadcast(new Intent(INTENT_FILTER));
        ((MainActivity) Objects.requireNonNull(getContext())).changeFlow(EMPTY);
    }
}
