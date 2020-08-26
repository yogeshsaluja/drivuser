package com.thinkincab.app.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thinkincab.app.R;
import com.thinkincab.app.data.network.model.BidModel;
import com.thinkincab.app.data.network.model.NotificationManager;
import com.thinkincab.app.ui.activity.offeers.OfferActivity;
import com.thinkincab.app.ui.fragment.searching.SearchingFragment;

import java.util.List;

public class BiddingAdapter extends RecyclerView.Adapter<BiddingAdapter.MyViewHolder> {

    private List<BidModel> notifications;
    private Context mContext;
    SearchingFragment searchingFragment;
    private int pos=-1;


    public BiddingAdapter(List<BidModel> notifications) {
        this.notifications = notifications;
     }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_offers, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         Glide.with(mContext)
                .load(notifications.get(position).getProvider().getAvatar())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_document_placeholder)
                        .dontAnimate().error(R.drawable.ic_document_placeholder))
                .into(holder.img);
        holder.tv_username.setText(notifications.get(position).getProvider().getFirstName());
        holder.tv_price.setText("driv : "+notifications.get(position).getAmount());
        holder.tv_address.setText(notifications.get(position).getProvider().getCity());
        //holder.tv_dis.setText(notifications.get(position).getProvider().ge());
        holder.tvRate.setText(notifications.get(position).getProvider().getRating());
        if (pos==position){
            holder.tv_accept.setBackground(mContext.getResources().getDrawable(R.drawable.shape_button_grad));
        }else {
            holder.tv_accept.setBackground(mContext.getResources().getDrawable(R.drawable.shape_button));


        }



    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView tv_accept,tv_price,tv_dis,tv_time,tv_address,tv_username;
        private TextView tvRate;

        MyViewHolder(View view) {
            super(view);

            tv_username = view.findViewById(R.id.tv_username);
            tv_address = view.findViewById(R.id.tv_address);
            tv_time = view.findViewById(R.id.tv_time);
            tv_dis = view.findViewById(R.id.tv_dis);
            tv_accept = view.findViewById(R.id.tv_accept);
            tv_price = view.findViewById(R.id.tv_price);
            tvRate = view.findViewById(R.id.textView4);

            img = view.findViewById(R.id.img);
            tv_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((OfferActivity)mContext).callAccept(notifications.get(getAdapterPosition()).getAmount(),notifications.get(getAdapterPosition()).getProviderId());
                }
            });


          }
    }
}
