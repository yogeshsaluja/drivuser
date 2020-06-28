package com.thinkincab.app.ui.activity.wallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appoets.braintreepayment.BrainTreePaymentActivity;
import com.appoets.paytmpayment.PaytmCallback;
import com.appoets.paytmpayment.PaytmObject;
import com.appoets.paytmpayment.PaytmPayment;
import com.thinkincab.app.R;
import com.thinkincab.app.base.BaseActivity;
import com.thinkincab.app.common.Constants;
import com.thinkincab.app.data.SharedHelper;
import com.thinkincab.app.data.network.model.AddWallet;
import com.thinkincab.app.data.network.model.BrainTreeResponse;
import com.thinkincab.app.ui.activity.payment.PaymentActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

import static com.thinkincab.app.MvpApplication.isBraintree;
import static com.thinkincab.app.MvpApplication.isCard;
import static com.thinkincab.app.MvpApplication.isPaytm;
import static com.thinkincab.app.MvpApplication.isPayumoney;
import static com.thinkincab.app.ui.activity.payment.PaymentActivity.PICK_PAYMENT_METHOD;

public class WalletActivity extends BaseActivity implements WalletIView {

    @BindView(R.id.wallet_balance)
    TextView walletBalance;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id._199)
    Button _199;
    @BindView(R.id._599)
    Button _599;
    @BindView(R.id._1099)
    Button _1099;
    @BindView(R.id.add_amount)
    Button addAmount;
    @BindView(R.id.cvAddMoneyContainer)
    CardView cvAddMoneyContainer;
    String regexNumber = "^(\\d{0,9}\\.\\d{1,4}|\\d{1,9})$";
    private WalletPresenter<WalletActivity> presenter = new WalletPresenter<>();

    private static final int BRAINTREE_PAYMENT_REQUEST_CODE = 101;


    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {

        ButterKnife.bind(this);
        presenter.attachView(this);
        // Activity title will be updated after the locale has changed in Runtime
        setTitle(getString(R.string.wallet));

        _199.setText(SharedHelper.getKey(this, "currency") + " " + getString(R.string._199));
        _599.setText(SharedHelper.getKey(this, "currency") + " " + getString(R.string._599));
        _1099.setText(SharedHelper.getKey(this, "currency") + " " + getString(R.string._1099));
        amount.setTag(SharedHelper.getKey(this, "currency"));

        walletBalance.setText(getNumberFormat().format(Double.parseDouble(SharedHelper.getKey(this, "walletBalance", "0"))));

        if (!isCard && !isBraintree && !isPaytm && !isPayumoney) {
            //cvAddMoneyContainer.setVisibility(View.GONE);
            //addAmount.setVisibility(View.GONE);
        }
    }

    private final static int PAYHERE_REQUEST = 11010;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    String amountValue;
    @OnClick({R.id._199, R.id._599, R.id._1099, R.id.add_amount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id._199:
                amount.setText(getString(R.string._199));
                break;
            case R.id._599:
                amount.setText(getString(R.string._599));
                break;
            case R.id._1099:
                amount.setText(getString(R.string._1099));
                break;
            case R.id.add_amount:
                if (!amount.getText().toString().trim().matches(regexNumber)) {
                    Toast.makeText(baseActivity(), getString(R.string.invalid_amount), Toast.LENGTH_SHORT).show();
                    return;
                }
                amountValue=amount.getText().toString();
                online(amount.getText().toString());
//                Intent intent = new Intent(baseActivity(), PaymentActivity.class);
//                intent.putExtra("hideCash", true);
//                startActivityForResult(intent, PICK_PAYMENT_METHOD);
                break;
        }
    }


    private void online(String amount)
    {

        InitRequest req = new InitRequest();
        req.setMerchantId("1212828");       // Your Merchant PayHere ID
        req.setMerchantSecret("8MOG8pHJduU4ub9rrvaE1p4Ob8179cJ5W4eVZmWmTD9s"); // Your Merchant secret (Add your app at Settings > Domains & Credentials, to get this))
        req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
        req.setAmount(Double.parseDouble(amount));             // Final Amount to be charged
        req.setOrderId("230000123");        // Unique Reference ID
        req.setItemsDescription("OCS wallet Amount");  // Item description title
        req.setCustom1("This is the custom message 1");
        req.setCustom2("This is the custom message 2");
        req.getCustomer().setFirstName(SharedHelper.getKey(this, "name"));
        req.getCustomer().setLastName("");
        req.getCustomer().setEmail(SharedHelper.getKey(this, "email"));
        req.getCustomer().setPhone("+94" + SharedHelper.getKey(this, "mobile"));
        req.getCustomer().getAddress().setAddress("No.1, Galle Road");
        req.getCustomer().getAddress().setCity("Colombo");
        req.getCustomer().getAddress().setCountry("Sri Lanka");

        //Optional Params
        //req.getCustomer().getDeliveryAddress().setAddress("No.2, Kandy Road");
        //req.getCustomer().getDeliveryAddress().setCity("Kadawatha");
        //req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");
        //req.getItems().add(new Item(null, "Door bell wireless", 1, 1000.0));

        Intent intent = new Intent(this, PHMainActivity.class);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
        PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
        startActivityForResult(intent, PAYHERE_REQUEST); //unique request ID like private final static int PAYHERE_REQUEST = 11010;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PAYMENT_METHOD && resultCode == Activity.RESULT_OK && data != null)
            switch (data.getStringExtra("payment_mode")) {
                case Constants.PaymentMode.CARD:
                    HashMap<String, Object> map = new HashMap<>();
                    String cardId = data.getStringExtra("card_id");
                    map.put("amount", amount.getText().toString());
                    map.put("payment_mode", "CARD");
                    map.put("card_id", cardId);
                    map.put("user_type", "app");
                    showLoading();
                    presenter.addMoney(map);
                    break;
                case Constants.PaymentMode.BRAINTREE:
                    presenter.getBrainTreeToken();
                    break;
                case Constants.PaymentMode.PAYTM: {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("amount", amount.getText().toString());
                    hashMap.put("payment_mode", Constants.PaymentMode.PAYTM);
                    hashMap.put("user_type", "app");
                    showLoading();
                    presenter.addMoneyPaytm(hashMap);
                    break;
                }
            }
        else if (requestCode == BRAINTREE_PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                HashMap<String, Object> map = new HashMap<>();
                String nonce = data.getStringExtra(BrainTreePaymentActivity.PAYMENT_NONCE);
                map.put("amount", amount.getText().toString());
                map.put("payment_mode", "BRAINTREE");
                map.put("braintree_nonce", nonce);
                map.put("user_type", "app");
                showLoading();
                presenter.addMoney(map);
            } else
                Toasty.error(WalletActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            String msg;
            if (response.isSuccess()) {
                msg = "Activity result:" + response.getData().toString();
                Toast.makeText(WalletActivity.this, " Payment Completed ", Toast.LENGTH_SHORT).show();
                HashMap<String, Object> map = new HashMap<>();
                map.put("amount", Double.parseDouble(amountValue));
                map.put("user_type", "user");
                showLoading();
                presenter.addMoney(map);

            } else {
                msg = "Result:" + response.toString();
                Toast.makeText(WalletActivity.this, "Transaction Failed, try in some time", Toast.LENGTH_SHORT).show();

            }
            //textView.setText(msg);
            //Log.e("payment gateway", String.valueOf(data));
            // Convert String to json object
        }

    }


    @Override
    public void onSuccess(PaytmObject object) {

        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        new PaytmPayment(WalletActivity.this, object, new PaytmCallback() {
            @Override
            public void onPaytmSuccess(String status, String message, String paymentmode, String txid) {
                Toasty.success(WalletActivity.this, "Value added successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPaytmFailure(String errorMessage) {
                Toasty.error(WalletActivity.this,
                        "Failed to add value", Toast.LENGTH_SHORT).show();
            }
        }).startPayment();
    }

    @Override
    public void onSuccess(AddWallet wallet) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if(wallet.getMessage().equals("Transaction Failed")){
            Toast.makeText(this, "\n" +
                    "Card failed or insufficient balance! Please try again.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, wallet.getMessage(), Toast.LENGTH_LONG).show();
        }

        amount.setText("");

//        SharedHelper.putKey(this, "walletBalance", String.valueOf(wallet.getBalance()));
        walletBalance.setText(getNumberFormat().format(Double.parseDouble(SharedHelper.getKey(this, "walletBalance", "0"))));
    }

    @Override
    public void onSuccess(BrainTreeResponse response) {

        if (!response.getToken().isEmpty()) {
            Intent intent = new Intent(WalletActivity.this, BrainTreePaymentActivity.class);
            intent.putExtra(BrainTreePaymentActivity.EXTRAS_TOKEN, response.getToken());
            startActivityForResult(intent, BRAINTREE_PAYMENT_REQUEST_CODE);
        }

    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

}
