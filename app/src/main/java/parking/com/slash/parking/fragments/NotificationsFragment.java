package parking.com.slash.parking.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import parking.com.slash.parking.R;
import parking.com.slash.parking.adapters.AdapterNotifications;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.model.ModelHistory.ModelHistory;
import parking.com.slash.parking.model.ModelNotifications.Model;
import parking.com.slash.parking.model.ModelNotifications.ModelNotifications;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.DataEnum;
import parking.com.slash.parking.utlities.SharedPrefHelper;
import retrofit2.Call;

public class NotificationsFragment extends BaseFragment implements HandleRetrofitResp {

    //region fields

    List<Model> modelList;
    AdapterNotifications adapterNotifications;
    //endregion

    //region views

    @BindView(R.id.rvNotifications)
    RecyclerView rvNotifications;
    //endregion

    //region life cycle

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.notifications, container, false);

        unbinder = ButterKnife.bind(this, view);
        modelList = new ArrayList<>();
        adapterNotifications = new AdapterNotifications(modelList, getBaseActivity());
        rvNotifications.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        rvNotifications.setAdapter(adapterNotifications);

        callGetUserNotifications();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        HandleCalls.getInstance(getBaseActivity()).setonRespnseSucess(this);
    }

    @Override
    public void onStop() {
        super.onStop();
//        appHeader.setRight(0, 0, 0);
    }

    //endregion

    //region parent methods
    @Override
    protected boolean canShowAppHeader() {
        return false;
    }

    @Override
    protected boolean canShowBottomBar() {
        return false;
    }

    @Override
    protected boolean canShowBackArrow() {
        return false;
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    public int getSelectedMenuId() {
        return 0;
    }

    //endregion

    //region calls response
    @Override
    public void onResponseSuccess(String flag, Object o) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
        ModelNotifications modelNotifications = gson.fromJson(jsonObject, ModelNotifications.class);
        adapterNotifications.addAll(modelNotifications.getModel());
    }

    @Override
    public void onNoContent(String flag, int code) {

    }

    @Override
    public void onResponseSuccess(String flag, Object o, int position) {

    }

    //endregion

    //region clicks


    //endregion

    //region calls

    private void callGetUserNotifications() {

        Call call = HandleCalls.restParki.getClientService().callGetUserNotifications(SharedPrefHelper.getInstance(getBaseActivity()).getAccessToken());
        HandleCalls.getInstance(getBaseActivity()).callRetrofit(call, DataEnum.callGetUserNotifications.name(), true);
    }
    //endregion

    //region functions

    public static NotificationsFragment init() {
        return new NotificationsFragment();
    }
    //endregion

}
