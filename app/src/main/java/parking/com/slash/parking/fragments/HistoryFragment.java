package parking.com.slash.parking.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import parking.com.slash.parking.R;
import parking.com.slash.parking.customViews.CustomSegment;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.retorfitconfig.HandleCalls;

public class HistoryFragment extends BaseFragment implements CustomSegment.OnSegmentChangedListener, HandleRetrofitResp {

    //region fields

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.history_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);
        scvHistory.setOnSegmentChangedListener(this);
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

    //endregion

    //region functions

    public static HistoryFragment init() {
        return new HistoryFragment();
    }

    @Override
    public void onSegmentChanged(int newSelectedIndex) {

        if (newSelectedIndex == 0) {
            rvHistorySeeking.setVisibility(View.VISIBLE);
            rvHistoryLeaving.setVisibility(View.GONE);
        } else {
            rvHistorySeeking.setVisibility(View.GONE);
            rvHistoryLeaving.setVisibility(View.VISIBLE);
        }
    }
    //endregion

}
