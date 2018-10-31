package parking.com.slash.parking.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import parking.com.slash.parking.R;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.model.ModelHistory.ModelHistory;
import parking.com.slash.parking.model.ModelUserProfile.ModelUserProfile;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.DataEnum;
import parking.com.slash.parking.utlities.SharedPrefHelper;
import retrofit2.Call;

public class UserFragment extends BaseFragment implements HandleRetrofitResp {

    //region fields

    //endregion

    //region views

    @BindView(R.id.imgProfile)
    ImageView imgProfile;

    @BindView(R.id.tvProfileName)
    TextView tvProfileName;
    @BindView(R.id.tvProfileBrand)
    TextView tvProfileBrand;
    @BindView(R.id.tvProfilePlateNumber)
    TextView tvProfilePlateNumber;
    //endregion

    //region life cycle

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.profile_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);
        callGetUserDetails();
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
        ModelUserProfile modelUserProfile = gson.fromJson(jsonObject, ModelUserProfile.class);

        adjustView(modelUserProfile);

    }


    @Override
    public void onNoContent(String flag, int code) {

    }

    @Override
    public void onResponseSuccess(String flag, Object o, int position) {

    }

    //endregion

    //region clicks

    @OnClick(R.id.tvProfileEditProfileDetails)
    public void onClicktvProfileEditProfileDetails() {
        // TODO submit data to server...
    }

    @OnClick(R.id.tvProfileChangePassword)
    public void onClicktvProfileChangePassword() {
        // TODO submit data to server...
    }

    @OnClick(R.id.tvProfileNotifications)
    public void onClicktvProfileNotifications() {
        addFragment(NotificationsFragment.init(), true);
    }

    @OnClick(R.id.tvProfileWalletAndPayment)
    public void onClicktvProfileWalletAndPayment() {
        // TODO submit data to server...
    }

    @OnClick(R.id.tvProfileInviteFriends)
    public void onClicktvProfileInviteFriends() {
        // TODO submit data to server...
    }

    @OnClick(R.id.tvProfileHelpAndSupport)
    public void onClicktvProfileHelpAndSupport() {
        // TODO submit data to server...
    }

    @OnClick(R.id.tvProfileYourFeedback)
    public void onClicktvProfileYourFeedback() {
        // TODO submit data to server...
    }

    @OnClick(R.id.tvProfileSignOut)
    public void onClicktvProfileSignOut() {
        // TODO submit data to server...
    }

    //endregion

    //region calls


    private void callGetUserDetails() {

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + SharedPrefHelper.getInstance(getBaseActivity()).getAccessToken());
        Call call = HandleCalls.restParki.getClientService().callGetUserDetails(headerMap);
        HandleCalls.getInstance(getBaseActivity()).callRetrofit(call, DataEnum.callGetUserDetails.name(), true);
    }
    //endregion

    //region functions

    public static UserFragment init() {
        return new UserFragment();
    }

    private void adjustView(ModelUserProfile modelUserProfile) {

        if (modelUserProfile.getModel().getCarimage() != null)
            Picasso.with(getBaseActivity())
                    .load(modelUserProfile.getModel().getCarimage())
                    .into(imgProfile);

        tvProfileName.setText(modelUserProfile.getModel().getName());
        tvProfileBrand.setText(modelUserProfile.getModel().getCarbrand() + "-" + modelUserProfile.getModel().getCarcolor());
        tvProfilePlateNumber.setText(modelUserProfile.getModel().getCarno());

    }
    //endregion

}
