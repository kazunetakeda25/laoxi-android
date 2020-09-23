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
import com.rider.pojo.CardInfo;
import com.rider.pojo.GenericResponse;
import com.rider.pojo.User;
import com.rider.retrofit.APIRequestManager;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.DialogDismissOneButton;
import com.rider.utils.DialogDismissTwoButton;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.ParsingHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by chirag on 18/2/16.
 */
public class MultiplCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static int mSelectedItem = -1;

    String type;
    View v;
    List<CardInfo> cardInfos;
    Context context;
    private User user;


    public MultiplCardAdapter(List<CardInfo> cardInfos, Context context) {

        this.cardInfos = cardInfos;
        this.context = context;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_multiple_card, parent, false);


        ViewHolderItem viewHolder = new ViewHolderItem(v);

        String strUserData = DataToPref.getSharedPreferanceData(context.getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        Gson mGson = new Gson();
        user = mGson.fromJson(strUserData, User.class);

        return viewHolder;
    }

    private CardInfo getItem(int position) {
        return cardInfos.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final CardInfo cardInfo = getItem(position);

        final ViewHolderItem viewHolderItem = (ViewHolderItem) holder;

        viewHolderItem.tvCardNumber.setText(getItem(position).getDisplayNumber());
        viewHolderItem.imagViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralStaticAlertDialog.getInstance().createDialogWithTwoButton(context, "Are you sure you want to delete this card?", new DialogDismissTwoButton() {
                    @Override
                    public void onOkClick() {
                        deleteCard(cardInfo.getToken());
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

    public void updateData(List<CardInfo> updated) {
        cardInfos.clear();
        cardInfos.addAll(updated);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return cardInfos.size();
    }

    class ViewHolderItem extends RecyclerView.ViewHolder {
        @Bind(R.id.tvMyCard)
        CustomTextView tvMyCard;
        @Bind(R.id.tvCardNumber)
        CustomTextView tvCardNumber;
        @Bind(R.id.imagViewDelete)
        ImageView imagViewDelete;


        public ViewHolderItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public void deleteCard(String token) {

        AlertUtils.showCustomProgressDialog(context);
        String strToken = DataToPref.getSharedPreferanceData(context.getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.HEADER_TOCKEN);
        String strUserId = DataToPref.getSharedPreferanceData(context.getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.USER_ID);

        ApiService api = RetroClient.getApiService();
        Call<ArrayList<CardInfo>> call = api.doDeleteCard(strToken, strUserId, token);

        APIRequestManager<ArrayList<CardInfo>> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<ArrayList<CardInfo>>() {
            @Override
            public void onResponse(retrofit2.Response<ArrayList<CardInfo>> response) {
                AlertUtils.dismissDialog();
                if ( response.isSuccessful() ) {
                    cardInfos = response.body();
                    DebugLog.e("Delete card data responce ::" + response.body());
                    if (response.code() == 200) {
                        user.setCardInfo(null);
                        cardInfos.clear();

                        user.setCardInfo(cardInfos);

                        String data = new Gson().toJson(user);
                        DataToPref.setSharedPreferanceData(context.getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA, data);
                        notifyDataSetChanged();

                        GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(context,"Card  deleted successfully", new DialogDismissOneButton() {
                            @Override
                            public void onDismiss() {

                            }
                        });
                    }
                } else {
                    if ( response.code() == 400 ) {
                        try {

                            String s = response.errorBody().string();
                            GenericResponse genericResponse = ParsingHelper.getInstance().genericResponseParsing(s);

                            GeneralStaticAlertDialog.getInstance().nativeDialogOneButton(context, genericResponse.getMessage(), null);


                        }  catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                AlertUtils.dismissDialog();
            }
        }, call);
        apiRequestManager.execute();


    }

    public HashMap<String, String> setDeleteCardData(String token) {
        HashMap<String, String> param = new HashMap<>();
        param.put(LaoxiConstant.USER_ID, user.getId());
        param.put(LaoxiConstant.CARD_TOKEN, token);
        return param;
    }


}
