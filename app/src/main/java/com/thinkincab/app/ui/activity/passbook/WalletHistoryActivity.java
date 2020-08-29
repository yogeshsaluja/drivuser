package com.thinkincab.app.ui.activity.passbook;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thinkincab.app.R;
import com.thinkincab.app.base.BaseActivity;
import com.thinkincab.app.common.EqualSpacingItemDecoration;
import com.thinkincab.app.data.SharedHelper;
import com.thinkincab.app.data.network.model.Wallet;
import com.thinkincab.app.data.network.model.WalletResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletHistoryActivity extends BaseActivity implements WalletHistoryIView {

    @BindView(R.id.rvWallet)
    RecyclerView rvWallet;
    @BindView(R.id.tvNoWalletData)
    TextView tvNoWalletData;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.textView35)
    TextView title;

    private WalletHistoryPresenter<WalletHistoryActivity> presenter = new WalletHistoryPresenter<>();
    private List<Wallet> walletList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_passbook;
    }

    @Override
    public void initView() {
        presenter.attachView(this);
        ButterKnife.bind(this);
        title.setText(getString(R.string.passbook));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         showLoading();
        presenter.wallet();

    }

    @Override
    public void onSuccess(WalletResponse response) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (!response.getWallets().isEmpty()) {
            tvNoWalletData.setVisibility(View.GONE);
            walletList.clear();
            walletList.addAll(response.getWallets());
            WalletHistoryAdapter mWalletAdapter = new WalletHistoryAdapter(walletList);
            rvWallet.setLayoutManager(new LinearLayoutManager(WalletHistoryActivity.this,
                    LinearLayoutManager.VERTICAL, false));
            rvWallet.setVisibility(View.VISIBLE);
            rvWallet.setItemAnimator(new DefaultItemAnimator());
            rvWallet.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.HORIZONTAL));
            rvWallet.setAdapter(mWalletAdapter);
        } else tvNoWalletData.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    private class WalletHistoryAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<Wallet> mWallet;

        WalletHistoryAdapter(List<Wallet> walletList) {
            this.mWallet = walletList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_wallet_history, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Wallet item = mWallet.get(position);
            holder.tvId.setText(item.getTransactionAlias());
           /* holder.tvAmountVal.setText(String.format("%s %s", SharedHelper.getKey(WalletHistoryActivity.this, "currency"),
                    getNewNumberFormat(item.getAmount())));
            holder.tvBalanceVal.setText(String.format("%s %s", SharedHelper.getKey(WalletHistoryActivity.this, "currency"),
                    getNewNumberFormat(item.getCloseBalance())));*/

            holder.tvAmountVal.setText(SharedHelper.getKey(WalletHistoryActivity.this, "currency")+""+item.getAmount());
            holder.tvBalanceVal.setText(SharedHelper.getKey(WalletHistoryActivity.this, "currency")+""+item.getCloseBalance());
        }

        @Override
        public int getItemCount() {
            return mWallet.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvId, tvAmountVal, tvBalanceVal;

        MyViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvAmountVal = itemView.findViewById(R.id.tvAmountVal);
            tvBalanceVal = itemView.findViewById(R.id.tvBalanceVal);
        }
    }
}
