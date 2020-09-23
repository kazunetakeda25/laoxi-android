package com.rider.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.rider.R;
import com.rider.adapter.NewFutureRideAdapter;
import com.rider.dialog.AlertUtils;
import com.rider.pojo.RidesDataList;
import com.rider.pojo.User;
import com.rider.retrofit.APIRequestManager;
import com.rider.retrofit.ApiService;
import com.rider.retrofit.RetroClient;
import com.rider.utils.DataToPref;
import com.rider.utils.DebugLog;
import com.rider.utils.LaoxiConstant;
import com.rider.utils.OnItemClickListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by hlink54 on 29/3/16.
 */
public class FutureRideFragment extends BaseFragment implements OnItemClickListener {
    @Bind(R.id.recycleViewList)
    RecyclerView recycleViewList;
    private User user;
    private LinearLayoutManager mLayoutManager;
    int page = 1;
    Boolean firstPage;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager linearLayoutManager;
    private boolean loading;


    private NewFutureRideAdapter newFutureRideAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_recycleview, container, false);
        ButterKnife.bind(this, view);
        mLayoutManager = new LinearLayoutManager(getContext());

        String strUserData = DataToPref.getSharedPreferanceData(getActivity().getApplicationContext(), LaoxiConstant.STORED_USER, LaoxiConstant.DATA);
        Gson mGson = new Gson();
        user = mGson.fromJson(strUserData, User.class);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        callAsync(String.valueOf(page));


        /**
         * Call a api of future ride when scrolling a recycler view data.
         */

        recycleViewList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;/*
                            Toast.makeText(getActivity(), "Load other record", Toast.LENGTH_SHORT).show();*/
                            page += 1;
                            callAsync(String.valueOf(page));

                        }
                    }
                }
            }
        });

    }


    //Click event for ride item click
    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void callAsync(String page) {
        if (page.equals("1"))
            firstPage = true;
        pastRide(page);
    }

    /**
     * Call a future ride list api.
     */

    public void pastRide(String pages) {

        String strToken = user.getToken();
        String strUserId = user.getId();

        DebugLog.i("PastRide Request");
        ApiService api = RetroClient.getApiService();
        Call<ArrayList<RidesDataList>> call = api.doFutureOrder(strToken, strUserId, "user", pages);

        APIRequestManager<ArrayList<RidesDataList>> apiRequestManager = new APIRequestManager<>(new APIRequestManager.ResponseListener<ArrayList<RidesDataList>>() {
            @Override
            public void onResponse(retrofit2.Response<ArrayList<RidesDataList>> response) {
                AlertUtils.dismissDialog();

                if ( response.isSuccessful() ) {
                    DebugLog.i("PastRide Success Response");

                    if (response.code() == 200) {
                        List<RidesDataList> ridesDataLists = response.body();

                        if (ridesDataLists.size() >= 0) {
                            if (page == 1) {
                                newFutureRideAdapter = new NewFutureRideAdapter(ridesDataLists, getActivity());
                                recycleViewList.setLayoutManager(linearLayoutManager);
                                recycleViewList.setAdapter(newFutureRideAdapter);
                                loading = true;
                            } else {
                                newFutureRideAdapter.updateData(ridesDataLists);
                                newFutureRideAdapter.notifyDataSetChanged();
                                loading = true;
                            }
                        }
                    } else {
                        DebugLog.e("PastRide Failed Response");
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                AlertUtils.dismissDialog();
                DebugLog.e("PastRide Request Failed Msg :" + throwable.getMessage());
            }
        }, call);
        apiRequestManager.execute();


    }

    /**
     * Set a parameter for call a future ride list api.
     */

    public HashMap<String, String> setPastRideData(String page) {
        HashMap<String, String> param = new HashMap<>();

        param.put(LaoxiConstant.USER_ID, user.getId());
        param.put(LaoxiConstant.USER_TYPE, "user");
        param.put(LaoxiConstant.PAGE, page);

        return param;
    }

    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }
}
