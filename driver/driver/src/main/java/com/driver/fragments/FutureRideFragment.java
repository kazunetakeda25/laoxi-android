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
import com.driver.adapter.FutureRideAdapter;
import com.driver.dao.IAsyncCompleteCallback;
import com.driver.dao.ParsingHelper;
import com.driver.dao.homedao.HomeActivityDAO;
import com.driver.pojo.GenericResponse;
import com.driver.pojo.TrackRidePojo.TrackRide;
import com.driver.pojo.upcomingRide.UpcomingRide;
import com.driver.retrofit.APIRequestManager;
import com.driver.retrofit.ApiService;
import com.driver.retrofit.RetroClient;
import com.driver.util.AlertUtils;
import com.driver.util.AppHelper;
import com.driver.util.ConstantClass;
import com.driver.util.DebugLog;
import com.driver.util.OnItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hlink54 on 29/3/16.
 */
public class FutureRideFragment extends BaseFragment implements OnItemClickListener {
    @Bind(R.id.recycleViewList)
    RecyclerView recycleViewList;
    private boolean isPagination = true;
    private int page = 1;
    private List<UpcomingRide> listDeals;
    private FutureRideAdapter futureRideAdapter;
    private LinearLayoutManager layoutManager;
    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_recycleview, container, false);
        ButterKnife.bind(this, view);
        callUpcomingRideWs(page);
        //initialize adapter and recycle view
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


    /**
     * Call a future ride list api.
     */

    private void callUpcomingRideWs(int pg) {
        AlertUtils.showCustomProgressDialog(getContext());

        String strToken = AppHelper.getInstance().getToken();
        String strUserId = AppHelper.getInstance().getDriverId();
        String strPage = "" + pg;
        DebugLog.i("Past Ride Request");
        ApiService api = RetroClient.getApiService();
        Call<ArrayList<UpcomingRide>> call = api.callUpcomingRide(strToken, strUserId, "driver", strPage);

        call.enqueue(new Callback<ArrayList<UpcomingRide>>() {
            @Override
            public void onResponse(Call<ArrayList<UpcomingRide>> call, retrofit2.Response<ArrayList<UpcomingRide>> response) {
                AlertUtils.dismissDialog();
                if (response.isSuccessful()) {
                    DebugLog.i("Past Ride Response Success Code ::" + response.code());

                    if (response.code() == ConstantClass.RESPONSE_CODE_200) {
                        isPagination = true;
                        List<UpcomingRide> list = response.body();

                        initRecycleView(list);
                        page += 1;
                    }
                } else {
                    DebugLog.i("Past Ride Response Failed Code ::" + response.code());
                    try {
                        String s = response.errorBody().string();
                        if ( response.code() == ConstantClass.RESPONSE_CODE_401 ) {
                            homeActivity.callLogoutWS();
                        } else if ( response.code() == ConstantClass.RESPONSE_CODE_404 ) {
                            isPagination = false;
                            GenericResponse responseBody = ParsingHelper.getInstance().genericResponseParsing(s);
//                    homeActivity.showSnackbar(responseBody.getMessage());
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
            public void onFailure(Call<ArrayList<UpcomingRide>> call, Throwable t) {
                AlertUtils.dismissDialog();
                homeActivity.showSnackbar(getString(R.string.bad_server));
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    /**
     * Initialize a recycler view .
     */

    public void initRecycleView(List<UpcomingRide> list) {

        if (futureRideAdapter == null) {
            listDeals = new ArrayList<>();
            listDeals.addAll(list);
            futureRideAdapter = new FutureRideAdapter(getContext(), listDeals, this);
            recycleViewList.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recycleViewList.setLayoutManager(layoutManager);
            recycleViewList.setAdapter(futureRideAdapter);
        } else {
            listDeals.addAll(list);
            futureRideAdapter.notifyDataSetChanged();
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
                            callUpcomingRideWs(page);
                        }
                    }
                }
            }
        });
    }

/*    public void loadDataOnAdapter(final List<Ride> listDeals) {
        this.listDeals.addAll(listDeals);
        futureRideAdapter.notifyDataSetChanged();
    }*/

    //Click event for ride item click
    @Override
    public void onItemClick(int position) {

    }

    /* public void getDealList() {
         List<Ride> list = new ArrayList<>();

         String profile = String.valueOf(R.drawable.profile);
         String rideId = getString(R.string.pastride_textview_text_rideid);
         String date = getString(R.string.pastride_textview_text_date);
         String pickup = getString(R.string.pastride_textview_text_Pickup);
         String dropoff = getString(R.string.pastride_textview_text_dropoff);
         String detail = getString(R.string.pastride_textview_text_Detail);

         Ride ride = new Ride(profile
                 , rideId
                 , date
                 , pickup
                 , dropoff
                 , detail
                 , Constants.COMPLETE
                 , "$20.00");
         list.add(ride);

         ride = new Ride(profile
                 , rideId
                 , date
                 , pickup
                 , dropoff
                 , detail
                 , Constants.CANCELED
                 , "");
         list.add(ride);

         ride = new Ride(profile
                 , rideId
                 , date
                 , pickup
                 , dropoff
                 , detail
                 , Constants.COMPLETE
                 , "$22.00");
         list.add(ride);

         ride = new Ride(profile
                 , rideId
                 , date
                 , pickup
                 , dropoff
                 , detail
                 , Constants.COMPLETE
                 , "$24.00");
         list.add(ride);

         ride = new Ride(profile
                 , rideId
                 , date
                 , pickup
                 , dropoff
                 , detail
                 , Constants.COMPLETE
                 , "$25.00");
         list.add(ride);

         loadDataOnAdapter(list);
     }
 */
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
}
