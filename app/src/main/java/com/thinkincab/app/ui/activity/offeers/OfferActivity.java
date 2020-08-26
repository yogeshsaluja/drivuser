package com.thinkincab.app.ui.activity.offeers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.widget.TextView;

import com.thinkincab.app.MvpApplication;
import com.thinkincab.app.R;
import com.thinkincab.app.base.BaseActivity;
import com.thinkincab.app.data.network.model.DataResponse;
import com.thinkincab.app.ui.activity.main.MainActivity;
import com.thinkincab.app.ui.adapter.BiddingAdapter;
import com.thinkincab.app.ui.fragment.searching.SearchingFragment;
import com.thinkincab.app.ui.fragment.searching.SearchingIView;
import com.thinkincab.app.ui.fragment.searching.SearchingPresenter;

import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.thinkincab.app.MvpApplication.DATUM;

public class OfferActivity extends BaseActivity implements SearchingIView {
    private SearchingPresenter<OfferActivity> presenter = new SearchingPresenter<>();
    @BindView(R.id.rv_request)
    RecyclerView bidding;
    @BindView(R.id.textView32)
    TextView pickup;

    @BindView(R.id.tv_amount)
    TextView amount;
    @BindView(R.id.textView34)
    TextView drop;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_offer;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        presenter.getRequest();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onSuccess(Object object) {
        finish();

    }

    @Override
    public void onSuccessData(DataResponse object) {

        pickup.setText(DATUM.getSAddress());
        drop.setText(DATUM.getDAddress());
        amount.setText("");

        populateAdapter(object);

        SpannableString string = new SpannableString(MvpApplication.estFare);

        string.setSpan(new StrikethroughSpan(), 0, string.length(), 0);

        amount.setText(string);

    }

    private void populateAdapter(DataResponse dataResponse) {

        if (dataResponse != null) {
            bidding.setAdapter(new BiddingAdapter(dataResponse.getBids()));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.getRequest();
                }
            }, 3000);

        }
    }

    public void callAccept(Double amount, Integer providerId) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("request_id", DATUM.getId());
        map.put("provider_id", providerId);
        map.put("amount", amount);


        presenter.acceptRequest(map);
    }
}