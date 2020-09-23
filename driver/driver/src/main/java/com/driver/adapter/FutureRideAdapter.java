package com.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.siyamed.shapeimageview.ImageViewPastRide;
import com.driver.R;
import com.driver.customclasses.CustomTextView;
import com.driver.pojo.upcomingRide.UpcomingRide;
import com.driver.util.ConstantClass;
import com.driver.util.DateTimeClass;
import com.driver.util.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hlink54 on 2/3/16.
 */
public class FutureRideAdapter extends RecyclerView.Adapter<FutureRideAdapter.ViewHolder> {


    private List<UpcomingRide> listRide;
    private LayoutInflater inflater;
    private Context context;
    private OnItemClickListener callback;

    public FutureRideAdapter(Context context, List<UpcomingRide> listRide, OnItemClickListener callback) {
        this.context = context;
        this.listRide = listRide;
        this.callback = callback;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.row_new_future_ride, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (listRide == null)
            return 0;
        return listRide.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View lineWriteReview;
        @Bind(R.id.imageViewProfile)
        ImageViewPastRide imageViewProfile;
        @Bind(R.id.txtRideId)
        CustomTextView txtRideId;
        @Bind(R.id.txtDate)
        CustomTextView txtDate;
        @Bind(R.id.txtPickupAddress)
        CustomTextView txtPickupAddress;
        @Bind(R.id.txtDropoffAddress)
        CustomTextView txtDropoffAddress;
        @Bind(R.id.txtDetail)
        CustomTextView txtDetail;
        @Bind(R.id.txtAmount)
        CustomTextView txtAmount;
        @Bind(R.id.imgStatus)
        ImageView imgStatus;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            UpcomingRide ride = listRide.get(position);
            txtRideId.setText("ID:" + ride.getId());
            txtDate.setText(DateTimeClass.getInstance().utcToLocalTime(ride.getStartDatetime()));
            txtPickupAddress.setText(ride.getPickupAddress());
            txtDropoffAddress.setText(ride.getDeliveryAddress());
            txtDetail.setText("Distance " + ride.getDistance());
            txtAmount.setText("$"+ride.getTotalAmount());
            Picasso.with(context)
                    .load(ConstantClass.USER_IMAGE_URL + ride.getUserProfile())
                    .into(imageViewProfile);
        }
    }
}
