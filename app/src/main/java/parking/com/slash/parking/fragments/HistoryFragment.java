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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import parking.com.slash.parking.R;
import parking.com.slash.parking.adapters.AdapterHistory;
import parking.com.slash.parking.customViews.CustomSegment;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.model.ModelHistory.Model;
import parking.com.slash.parking.model.ModelHistory.ModelHistory;
import parking.com.slash.parking.model.ModelLoginResponse.ModelLoginResponse;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.DataEnum;
import parking.com.slash.parking.utlities.SharedPrefHelper;
import retrofit2.Call;

public class HistoryFragment extends BaseFragment implements CustomSegment.OnSegmentChangedListener, HandleRetrofitResp
{

    //region fields
    AdapterHistory adapterHistory;
    List<Model> modelList;
    AdapterHistory adapterHistoryLeaving;
    List<Model> modelListLEaving;
    //endregion

    //region views

    @BindView(R.id.scvHistory)
    CustomSegment scvHistory;
    @BindView(R.id.rvHistorySeeking)
    RecyclerView rvHistorySeeking;
    @BindView(R.id.rvHistoryLeaving)
    RecyclerView rvHistoryLeaving;
    //endregion

    //region life cycle

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.history_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);
        scvHistory.setOnSegmentChangedListener(this);
        modelList = new ArrayList<>();
        adapterHistory = new AdapterHistory(modelList, getBaseActivity());
        rvHistorySeeking.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        rvHistorySeeking.setAdapter(adapterHistory);

        modelListLEaving = new ArrayList<>();
        adapterHistoryLeaving = new AdapterHistory(modelListLEaving, getBaseActivity());
        rvHistoryLeaving.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        rvHistoryLeaving.setAdapter(adapterHistoryLeaving);

        callGetUserHistory();
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        HandleCalls.getInstance(getBaseActivity()).setonRespnseSucess(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
//        appHeader.setRight(0, 0, 0);
    }

    //endregion

    //region parent methods
    @Override
    protected boolean canShowAppHeader()
    {
        return false;
    }

    @Override
    protected boolean canShowBottomBar()
    {
        return false;
    }

    @Override
    protected boolean canShowBackArrow()
    {
        return false;
    }

    @Override
    protected String getTitle()
    {
        return null;
    }

    @Override
    public int getSelectedMenuId()
    {
        return 0;
    }

    //endregion

    //region calls response
    @Override
    public void onResponseSuccess(String flag, Object o)
    {

        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
        ModelHistory modelHistory = gson.fromJson(jsonObject, ModelHistory.class);

        for (Model model : modelHistory.getModel())
        {
            if (model.getType() == 1)
                adapterHistoryLeaving.addItem(model);
            else
                adapterHistory.addItem(model);
        }
//        adapterHistory.addAll(modelHistory.getModel());
    }

    @Override
    public void onNoContent(String flag, int code)
    {

    }

    @Override
    public void onResponseSuccess(String flag, Object o, int position)
    {

    }

    //endregion

    //region clicks


    //endregion

    //region calls

    private void callGetUserHistory()
    {

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + SharedPrefHelper.getInstance(getBaseActivity()).getAccessToken());
        Call call = HandleCalls.restParki.getClientService().callGetUserHistory(headerMap);
        HandleCalls.getInstance(getBaseActivity()).callRetrofit(call, DataEnum.GetUserHistory.name(), true);
    }
    //endregion

    //region functions

    public static HistoryFragment init()
    {
        return new HistoryFragment();
    }

    @Override
    public void onSegmentChanged(int newSelectedIndex)
    {

        if (newSelectedIndex == 0)
        {
            rvHistorySeeking.setVisibility(View.VISIBLE);
            rvHistoryLeaving.setVisibility(View.GONE);
        } else
        {
            rvHistorySeeking.setVisibility(View.GONE);
            rvHistoryLeaving.setVisibility(View.VISIBLE);
        }
    }
    //endregion

}
