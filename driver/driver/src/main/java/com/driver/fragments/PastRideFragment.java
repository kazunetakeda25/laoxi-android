package com.driver.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.driver.R;
import com.driver.activity.HomeActivity;
import com.driver.adapter.PastRideAdapter;
import com.driver.dao.ParsingHelper;
import com.driver.pojo.GenericResponse;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.pojo.upcomingRide.UpcomingRide;
import com.driver.retrofit.APIRequestManager;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.driver.util.AppHelper;
import com.driver.util.ConstantClass;
import com.driver.util.DebugLog;
import com.driver.util.OnItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;


/**
 * Created by hlink54 on 29/3/16.
 */
public class PastRideFragment extends BaseFragment implements OnItemClickListener, APIRequestManager.ResponseListener<ArrayList<UpcomingRide>> {
    @Bind(R.id.recycleViewList)
    RecyclerView recycleViewList;
    private boolean isPagination = true;
    private int page = 1;
    private List<UpcomingRide> listDeals;
    private PastRideAdapter dealListAdapter;
    private LinearLayoutManager layoutManager;
    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_recycleview, container, false);
        ButterKnife.bind(this, view);
        callPastRideWs(page);
        //initialze adapter and recycle view
        //initRecycleView();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeActivity) {
            homeActivity = (HomeActivity) context;
        }
    }

    private void callPastRideWs(int pg) {

        String strToken = AppHelper.getInstance().getToken();
        String strUserId = AppHelper.getInstance().getDriverId();
        String strPage = "" + pg;

        DebugLog.i("Call PastRide Request");
        ApiService api = RetroClient.getApiService();
        Call<ArrayList<UpcomingRide>> call = api.callPastRide(strToken, strUserId, "driver", strPage);

        APIRequestManager<ArrayList<UpcomingRide>> apiRequestManager = new APIRequestManager<>(this, call);
        apiRequestManager.execute();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void initRecycleView(List<UpcomingRide> list) {

        if (dealListAdapter == null) {
            listDeals = new ArrayList<>();
            listDeals.addAll(list);
            dealListAdapter = new PastRideAdapter(getContext(), listDeals, this);
            recycleViewList.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recycleViewList.setLayoutManager(layoutManager);
            recycleViewList.setAdapter(dealListAdapter);
        } else {
            listDeals.addAll(list);
            dealListAdapter.notifyDataSetChanged();
        }


        //Add this for pagination
        recycleViewList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleChildCount = layoutManager.getChildCount();
                    int totalChild = layoutManager.getItemCount();
                    int firstVisibleChildPosition = layoutManager.findFirstVisibleItemPosition();
                    if (isPagination) {
                        if (visibleChildCount + firstVisibleChildPosition == totalChild) {
                            isPagination = false;
                            callPastRideWs(page);
                        }
                    }
                }
            }
        });
    }

 /*   public void loadDataOnAdapter(final List<Ride> listDeals) {
        this.listDeals.addAll(listDeals);
        dealListAdapter.notifyDataSetChanged();
    }*/

    //Click event for ride item click
    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void getUpdateLatLongWsResponseFromService(TrackRide pojo) {

    }

    @Override
    public void drawerLock() {
    }

    @Override
    public void onResponse(retrofit2.Response<ArrayList<UpcomingRide>> response) {
        if (response.isSuccessful()) {
            DebugLog.i("Past Ride Response Success Code :" + response.code());

            if (response.code() == ConstantClass.RESPONSE_CODE_200) {
                isPagination = true;
                List<UpcomingRide> list = response.body();

                initRecycleView(list);
                page += 1;
            }
        } else {
            DebugLog.i("Past Ride Response Failed Code :" + response.code());
            try {
                String s = response.errorBody().string();
                if ( response.code() == ConstantClass.RESPONSE_CODE_401 ) {
                    homeActivity.callLogoutWS();
                } else if ( response.code() == ConstantClass.RESPONSE_CODE_404 ) {
                    GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
//                    homeActivity.showSnackbar(responseBody.getMessage());
                    isPagination = false;
                } else {
                    GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
//                    homeActivity.showSnackbar(responseBody.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        DebugLog.e("PastRide Request Failed Msg : " + throwable.getMessage());
        homeActivity.showSnackbar(getString(R.string.bad_server));
    }
}
