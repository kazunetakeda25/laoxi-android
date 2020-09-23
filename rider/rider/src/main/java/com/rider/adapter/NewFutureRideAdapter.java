package com.rider.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.rider.R;
import com.rider.customclasses.CustomTextView;
import com.rider.dialog.AlertUtils;
import com.rider.dialog.GeneralStaticAlertDialog;
import com.rider.pojo.GenericResponse;
import com.rider.pojo.RidesDataList;
import com.rider.pojo.User;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DateTimeClass;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissTwoButton;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.ParsingHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by chirag on 18/2/16.
 */
public class NewFutureRideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    View v;
    List<RidesDataList> orderDataLists;
    Context context;
    private User user;
    Gson mGson = new Gson();


    public NewFutureRideAdapter(List<RidesDataList> orderDataLists, Context context) {

        this.orderDataLists = orderDataLists;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_future_ride, parent, false);


        ViewHolderItem viewHolder = new ViewHolderItem(v);


        String strUserData = DataToPref.getSharedPreferanceData(context.getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);

        user = mGson.fromJson(strUserData, User.class);

        return viewHolder;
    }


    private RidesDataList getItem(int position) {
        return orderDataLists.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        RidesDataList ridesDataList = getItem(position);

        final ViewHolderItem viewHolderItem = (ViewHolderItem) holder;

        viewHolderItem.txtRideId.setText("ID : " + ridesDataList.getId());
        viewHolderItem.txtDate.setText(DateTimeClass.getInstance().utcToLocalTime(ridesDataList.getTripdatetime()));
        // viewHolderItem.textViewCompleted.setText(ridesDataList.getStatus());
        viewHolderItem.txtPickupAddress.setText(ridesDataList.getPickupAddress());
        viewHolderItem.txtDropoffAddress.setText(ridesDataList.getDeliveryAddress());


        viewHolderItem.imageViewCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralStaticAlertDialog.getInstance().createDialogWithTwoButton(context, "You wanna cancel trip?", new DialogDismissTwoButton() {
                    @Override
                    public void onOkClick() {
                        if(orderDataLists!=null)
                        {
                            wsCallCancelOrder(position);
                        }
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
            }
        });

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


        @Bind(R.id.txtRideId)
        CustomTextView txtRideId;
        @Bind(R.id.txtDate)
        CustomTextView txtDate;
        @Bind(R.id.txtPickupAddress)
        CustomTextView txtPickupAddress;
        @Bind(R.id.txtDropoffAddress)
        CustomTextView txtDropoffAddress;
        @Bind(R.id.imageViewCross)
        ImageView imageViewCross;

        public ViewHolderItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


    public void wsCallCancelOrder(final int position) {
        String strToken = DataToPref.getSharedPreferanceData(context.getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.HEADER_TOCKEN);
        String strUserId = DataToPref.getSharedPreferanceData(context.getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.USER_ID);

        AlertUtils.showCustomProgressDialog(context);

        ApiService api = RetroClient.getApiService();
        Call<GenericResponse> call = api.doCancelOrder(strToken, strUserId, "user", orderDataLists.get(position).getId(), user.getCardInfo().get(0).getToken());

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, retrofit2.Response<GenericResponse> response) {
                AlertUtils.dismissDialog();
                if ( response.isSuccessful() ){
                    GenericResponse data=response.body();

                    orderDataLists.remove(position);
                    notifyDataSetChanged();
                    DebugLog.e("cancel order data"+data);
                } else {
                    String s = null;
                    try {
                        s = response.errorBody().string();
                        GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);

                        DebugLog.e("cancel order date error:" + genericResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                AlertUtils.dismissDialog();
            }
        });
    }

    public HashMap<String, String> setCancelOrderData(int position) {
        HashMap<String, String> param = new HashMap<>();
       if(user!=null)
       {
           param.put(LaoxiConstant.ID, user.getId());
           param.put(LaoxiConstant.USER_TYPE, "user");
           param.put(LaoxiConstant.ORDER_ID, orderDataLists.get(position).getId());
           param.put(LaoxiConstant.CARD_TOKEN,user.getCardInfo().get(0).getToken());
       }

        return param;
    }


}
