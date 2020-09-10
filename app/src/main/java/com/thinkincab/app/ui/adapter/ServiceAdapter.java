package com.thinkincab.app.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thinkincab.app.MvpApplication;
import com.thinkincab.app.R;
import com.thinkincab.app.common.Constants;
import com.thinkincab.app.data.SharedHelper;
import com.thinkincab.app.data.network.model.EstimateFare;
import com.thinkincab.app.data.network.model.Service;
import com.thinkincab.app.ui.activity.main.MainActivity;
import com.thinkincab.app.ui.activity.past_trip_detail.PastTripDetailActivity;
import com.thinkincab.app.ui.fragment.RateCardFragment;
import com.thinkincab.app.ui.fragment.service.ServiceTypesFragment;

import java.util.List;

import static com.thinkincab.app.base.BaseActivity.getNumberFormat;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    private List<Service> list;
    private TextView capacity;
    private Context context;
    private int lastCheckedPos = 0;
    private Animation zoomIn;
    private ServiceTypesFragment.ServiceListener mListener;
    private EstimateFare estimateFare;
    private boolean canNotifyDataSetChanged = false;

    public ServiceAdapter(Context context, List<Service> list,
                          ServiceTypesFragment.ServiceListener listener,
                          TextView capacity, EstimateFare fare) {
        this.context = context;
        this.list = list;
        this.capacity = capacity;
        this.mListener = listener;
        this.estimateFare = fare;
        zoomIn = AnimationUtils.loadAnimation(this.context, R.anim.zoom_in_animation);
        zoomIn.setFillAfter(true);
    }

    public void setEstimateFare(EstimateFare estimateFare) {
        this.estimateFare = estimateFare;
        canNotifyDataSetChanged = true;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_car, parent, false));
    }

    @SuppressLint({"StringFormatMatches", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {
        Service obj = list.get(position);
        if (obj != null)
            holder.serviceName.setText(obj.getName());
        if (estimateFare != null) {
        //    holder.estimated_fixed.setVisibility(View.VISIBLE);
            //holder.price.setVisibility(View.VISIBLE);
            MvpApplication.estFare=SharedHelper.getKey(context, "currency")+""+Double.parseDouble(String.valueOf(estimateFare.getEstimatedFare()));

            holder.estimated_fixed.setText(SharedHelper.getKey(context, "currency")+""+Double.parseDouble(String.valueOf(estimateFare.getEstimatedFare())));
            if (SharedHelper.getKey(context, "measurementType").equalsIgnoreCase(Constants.MeasurementType.KM)) {
                if (estimateFare.getDistance() > 1 || estimateFare.getDistance() > 1.0) {
                    holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.kms));
                } else {
                    holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.km));
                }
            } else {
                if (estimateFare.getDistance() > 1 || estimateFare.getDistance() > 1.0) {
                    holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.miles));
                } else {
                    holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.mile));
                }
            }
        }
        Glide.with(context)
                .load(obj.getImage())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_car).dontAnimate().error(R.drawable.ic_car))
                .into(holder.image);


        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        holder.image.setColorFilter(filter);

        if (position == lastCheckedPos && canNotifyDataSetChanged) {
            ColorMatrix mat = new ColorMatrix();
            mat.setSaturation(20);

            ColorMatrixColorFilter fil  = new ColorMatrixColorFilter(mat);
            holder.image.setColorFilter(fil);

            canNotifyDataSetChanged = false;
            capacity.setText(String.valueOf(obj.getCapacity()));
            holder.mFrame_service.setBackground(context.getResources().getDrawable(R.drawable.circle_background));
            holder.serviceName.setTextColor(context.getResources().getColor(R.color.colorAccent));
            //holder.price.setVisibility(View.VISIBLE);
            holder.itemView.setAlpha(1);
           // holder.estimated_fixed.setVisibility(View.VISIBLE);
            if (estimateFare != null) {
                if (SharedHelper.getKey(context, "measurementType").equalsIgnoreCase(Constants.MeasurementType.KM)) {
                    if (estimateFare.getDistance() > 1 || estimateFare.getDistance() > 1.0)
                        holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.kms));
                    else
                        holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.km));
                } else {
                    if (estimateFare.getDistance() > 1 || estimateFare.getDistance() > 1.0)
                        holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.miles));
                    else
                        holder.price.setText(estimateFare.getDistance() + " "  + context.getString(R.string.mile));
                }
                holder.estimated_fixed.setText(SharedHelper.getKey(context, "currency")+""+Double.parseDouble(String.valueOf(estimateFare.getEstimatedFare())));

            }
            holder.itemView.startAnimation(zoomIn);
        } else {
            holder.mFrame_service.setBackground(context.getResources().getDrawable(R.drawable.service_bkg));
            holder.serviceName.setTextColor(context.getResources().getColor(R.color.colorPrimaryText));
            holder.itemView.setAlpha((float) 0.7);
            holder.estimated_fixed.setVisibility(View.INVISIBLE);
            holder.price.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(view -> {
            Service object = list.get(position);
            if (object != null) {
                if (view.getId() == R.id.item_view) {
                     Glide.with(context)
                            .load(obj.getImage())
                            .apply(RequestOptions.placeholderOf(R.drawable.ic_car).dontAnimate().error(R.drawable.ic_car))
                            .into(holder.image);
                  //  MvpApplication.estFare=SharedHelper.getKey(context, "currency")+""+Double.parseDouble(String.valueOf(estimateFare.getEstimatedFare()));

                    if (lastCheckedPos == position) {
                        RateCardFragment.SERVICE = object;
                        ((MainActivity) context).changeFragment(new RateCardFragment());

                    }
                    lastCheckedPos = position;
                    canNotifyDataSetChanged=true;
                    notifyDataSetChanged();
                }
                mListener.whenClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
    public Service getSelectedService() {
        if (list.size() > 0) return list.get(lastCheckedPos);
        else return null;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout itemView;
        private TextView serviceName, price, estimated_fixed;
        private ImageView image;
        private FrameLayout mFrame_service;

        MyViewHolder(View view) {
            super(view);
            mFrame_service = view.findViewById(R.id.frame_service);
            estimated_fixed = view.findViewById(R.id.estimated_fixed);
            serviceName = view.findViewById(R.id.service_name);
            price = view.findViewById(R.id.price);
            image = view.findViewById(R.id.image);
            itemView = view.findViewById(R.id.item_view);

        }
    }
}
