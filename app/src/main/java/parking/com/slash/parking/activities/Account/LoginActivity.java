package parking.com.slash.parking.activities.Account;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;

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
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.model.ModelCommonRequest.ModelCommonRequest;
import parking.com.slash.parking.model.ModelLoginRequest.ModelLoginRequest;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.DataEnum;
import retrofit2.Call;

public class LoginActivity extends BaseActivity implements Validator.ValidationListener, HandleRetrofitResp {


    //region fields
    Validator validator;
    //endregion

    //region views

    @Email
    @BindView(R.id.edtLoginEmail)
    EditText edtLoginEmail;

    @Password
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
                showMessage(message);
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
        Call call = HandleCalls.restSha3er.getClientService().callLogin(modelLoginRequest);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callCheckExist.name(), true);
    }

    //endregion
}
