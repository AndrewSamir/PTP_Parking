package parking.com.slash.parking.activities.Account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.BaseActivity;
import parking.com.slash.parking.activities.MainActivity;
import parking.com.slash.parking.activities.Seeker.SeekerMapsActivity;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.model.ModelGetNearBy.ModelGetNearByResponse;
import parking.com.slash.parking.model.ModelLoginRequest.ModelLoginRequest;
import parking.com.slash.parking.model.ModelLoginResponse.ModelLoginResponse;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.DataEnum;
import parking.com.slash.parking.utlities.SharedPrefHelper;
import retrofit2.Call;

public class LoginActivity extends Activity implements Validator.ValidationListener, HandleRetrofitResp {


    //region fields
    Validator validator;
    //endregion

    //region views

    @Email
    @BindView(R.id.edtLoginEmail)
    EditText edtLoginEmail;

//    @Password
    @BindView(R.id.edtLoginPassword)
    EditText edtLoginPassword;

    //endregion

    //region lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        if (Config.DEBUG) {
            edtLoginEmail.setText("s@s.com");
            edtLoginPassword.setText("123456");
        }
        HandleCalls.getInstance(this).setonRespnseSucess(this);
    }

    //endregion

    //region validation
    @Override
    public void onValidationSucceeded() {
        callLogin();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
//                showMessage(message);
            }
        }
    }

    //endregion

    //region clicks

    @OnClick(R.id.btnLoginLogin)
    public void onClickbtnLoginLogin() {
        validator.validate();
    }

    @OnClick(R.id.btnLoginSignUp)
    public void onClickbtnLoginSignUp() {
        startActivity(new Intent(this, SignUpAccountDetailsActivity.class));
        finish();
    }


    //endregion

    //region calls response
    @Override
    public void onResponseSuccess(String flag, Object o) {
        if (flag.equals(DataEnum.callLogin.name())) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
            ModelLoginResponse modelGetNearByResponse = gson.fromJson(jsonObject, ModelLoginResponse.class);

            SharedPrefHelper.getInstance(this).setUser(modelGetNearByResponse.getModel());
            startActivity(new Intent(this, MainActivity.class));
            finish();

        }
    }

    @Override
    public void onNoContent(String flag, int code) {

    }

    @Override
    public void onResponseSuccess(String flag, Object o, int position) {

    }

    //endregion

    //region calls
    private void callLogin() {

        ModelLoginRequest modelLoginRequest = new ModelLoginRequest();
        modelLoginRequest.setUsername(edtLoginEmail.getText().toString());
        modelLoginRequest.setPassword(edtLoginPassword.getText().toString());
        modelLoginRequest.setDevicetoken(FirebaseInstanceId.getInstance().getToken());
        Call call = HandleCalls.restParki.getClientService().callLogin(modelLoginRequest);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callLogin.name(), true);
    }

    //endregion

    //region paypal test


    @BindView(R.id.tvPayPalTest)
    View tvPayPalTest;
    private int REQUEST_CODE = 305;

    @OnClick(R.id.tvPayPalTest)
    public void onClicktvPayPalTest() {
        onBraintreeSubmit(tvPayPalTest);
    }

    public void onBraintreeSubmit(View v) {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken("eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiIzYjg1NWE5ZGNkNzMxNDU1MjQ0ODU4OGUzOTdmNzViOGQzYTVhZDczZWQwZmQ1YmYxMTczZGVkNTg2YTlkZGMxfGNyZWF0ZWRfYXQ9MjAxOC0xMC0yOFQwNjoxMDo0Ny45NTU1ODU4ODArMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJncmFwaFFMIjp7InVybCI6Imh0dHBzOi8vcGF5bWVudHMuc2FuZGJveC5icmFpbnRyZWUtYXBpLmNvbS9ncmFwaHFsIiwiZGF0ZSI6IjIwMTgtMDUtMDgifSwiY2hhbGxlbmdlcyI6W10sImVudmlyb25tZW50Ijoic2FuZGJveCIsImNsaWVudEFwaVVybCI6Imh0dHBzOi8vYXBpLnNhbmRib3guYnJhaW50cmVlZ2F0ZXdheS5jb206NDQzL21lcmNoYW50cy8zNDhwazljZ2YzYmd5dzJiL2NsaWVudF9hcGkiLCJhc3NldHNVcmwiOiJodHRwczovL2Fzc2V0cy5icmFpbnRyZWVnYXRld2F5LmNvbSIsImF1dGhVcmwiOiJodHRwczovL2F1dGgudmVubW8uc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbSIsImFuYWx5dGljcyI6eyJ1cmwiOiJodHRwczovL29yaWdpbi1hbmFseXRpY3Mtc2FuZC5zYW5kYm94LmJyYWludHJlZS1hcGkuY29tLzM0OHBrOWNnZjNiZ3l3MmIifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=");
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                Log.d("paypal", result.getDeviceData());
                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }
    }
    //endregion
}
