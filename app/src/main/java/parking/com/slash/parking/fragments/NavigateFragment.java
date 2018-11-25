package parking.com.slash.parking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.Leaver.LeaverMapsActivity;
import parking.com.slash.parking.activities.Seeker.SeekerMapsActivity;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.SharedPrefHelper;

public class NavigateFragment extends BaseFragment implements HandleRetrofitResp
{

    //region fields

    //endregion

    //region views
    @BindView(R.id.tvNavigationName)
    TextView tvNavigationName;
    //endregion

    //region life cycle

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.navigation_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);

        tvNavigationName.setText("Hi "+ SharedPrefHelper.getInstance(getBaseActivity()).getName());
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
        return R.id.bottomItem_feeds;
    }

    //endregion

    //region calls response
    @Override
    public void onResponseSuccess(String flag, Object o)
    {

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

    @OnClick(R.id.rlNavigationSeek)
    void onClickrlNavigationSeek(View view)
    {

        getBaseActivity().startActivity(new Intent(getBaseActivity(), SeekerMapsActivity.class));
    }

    @OnClick(R.id.rlNavigationLeave)
    void onClickrlNavigationLeave(View view)
    {
        getBaseActivity().startActivity(new Intent(getBaseActivity(), LeaverMapsActivity.class));

    }
    //endregion

    //region calls

    //endregion

    //region functions

    public static NavigateFragment init()
    {
        return new NavigateFragment();
    }
    //endregion

}
