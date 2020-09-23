package com.rider.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.siyamed.shapeimageview.ImageViewPastRide;
import com.rider.R;
import com.rider.customclasses.CustomTextView;
import com.rider.pojo.RidesDataList;
import com.rider.utils.DateTimeClass;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chirag on 18/2/16.
 */
public class NewPastRideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static int mSelectedItem = -1;

    String type;
    View v;
    List<RidesDataList> orderDataLists;
    Context context;

    public NewPastRideAdapter(List<RidesDataList> orderDataLists, Context context) {

        this.orderDataLists = orderDataLists;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_past_ride, parent, false);


        ViewHolderItem viewHolder = new ViewHolderItem(v);
        return viewHolder;

    }

    private RidesDataList getItem(int position) {
        return orderDataLists.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        RidesDataList ridesDataList = getItem(position);

        final ViewHolderItem viewHolderItem = (ViewHolderItem) holder;

        if (ridesDataList.getStatus().equalsIgnoreCase("Cancelled")) {
            viewHolderItem.imgStatus.setBackground(null);
            viewHolderItem.imgStatus.setBackgroundResource(R.drawable.cancel_sign);
            viewHolderItem.txtDetail.setText("Cancelled");
        } else if (ridesDataList.getStatus().equalsIgnoreCase("Completed")) {
            viewHolderItem.imgStatus.setBackground(null);
            viewHolderItem.imgStatus.setBackgroundResource(R.drawable.right_sign);
            viewHolderItem.txtDetail.setText("Distance " + ridesDataList.getDistance()+"km, Time"+ ridesDataList.getTripDuration()+"min");
        } else {
            viewHolderItem.imgStatus.setBackground(null);
        }

        viewHolderItem.txtRideId.setText("ID : " + ridesDataList.getId());
        viewHolderItem.txtDate.setText(DateTimeClass.getInstance().utcToLocalTime(ridesDataList.getTripdatetime()));
        // viewHolderItem.textViewCompleted.setText(ridesDataList.getStatus());
        viewHolderItem.txtPickupAddress.setText(ridesDataList.getPickupAddress());
        viewHolderItem.txtDropoffAddress.setText(ridesDataList.getDeliveryAddress());

        viewHolderItem.txtAmount.setText("₭" + ridesDataList.getTotalAmount());

        Picasso.with(context)
                .load(context.getResources().getString(R.string.MAINURL_FOR_DRIVER_IMAGE) + ridesDataList.getDriverProfile()).error(R.drawable.pic_default)
                .fit()
                .placeholder(R.drawable.avatar_icon)
                .into(viewHolderItem.imageViewProfile);
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public void updateData(List<RidesDataList> updated) {
        orderDataLists.addAll(updated);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return orderDataLists.size();
    }

    class ViewHolderItem extends RecyclerView.ViewHolder {

        // TextView textViewOrderNO, textViewDate, textViewCompleted, textViewPickUp, textViewDroppOff, textViewDetails, txtAmount;
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


        public ViewHolderItem(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

          /*  textViewOrderNO = (TextView) itemView.findViewById(R.id.txtRideId);
            textViewCompleted = (TextView) itemView.findViewById(R.id.txtStatus);
            textViewDate = (TextView) itemView.findViewById(R.id.txtDate);
            textViewPickUp = (TextView) itemView.findViewById(R.id.txtPickupAddress);
            textViewDroppOff = (TextView) itemView.findViewById(R.id.txtDropoffAddress);
            textViewDetails = (TextView) itemView.findViewById(R.id.txtDetail);
            txtAmount = (TextView) itemView.findViewById(R.id.txtAmount);*/

        }
    }

}
