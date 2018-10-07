package parking.com.slash.parking.activities.Account;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.BaseActivity;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.model.ModelCommonRequest.ModelCommonRequest;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.DataEnum;
import retrofit2.Call;

public class SignUpAccountDetailsActivity extends BaseActivity implements Validator.ValidationListener, HandleRetrofitResp {

    //region fields
    Validator validator;
    //endregion

    //region views
    @NotEmpty
    @BindView(R.id.edtRegisterName)
    EditText edtRegisterName;

    @NotEmpty
    @BindView(R.id.edtRegisterPhone)
    EditText edtRegisterPhone;

    @Email
    @BindView(R.id.edtRegisterMail)
    EditText edtRegisterMail;

    @Password
    @BindView(R.id.edtRegisterPassword)
    EditText edtRegisterPassword;

    @ConfirmPassword
    @BindView(R.id.edtRegisterConfirmPassword)
    EditText edtRegisterConfirmPassword;

    //endregion

    //region lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_account_details);

        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);

        HandleCalls.getInstance(this).setonRespnseSucess(this);
    }

    //endregion

    //region clicks

    @OnClick(R.id.btnRegisterNext)
    public void onClickbtnRegisterNext() {
        validator.validate();
    }

    //endregion

    //region validation
    @Override
    public void onValidationSucceeded() {
        callCheckExist();
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
    private void callCheckExist() {

        ModelCommonRequest modelCommonRequest = new ModelCommonRequest();
        modelCommonRequest.setEmail(edtRegisterMail.getText().toString());
        modelCommonRequest.setMobile(edtRegisterPhone.getText().toString());
        Call call = HandleCalls.restParki.getClientService().callCheckExist(modelCommonRequest);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callCheckExist.name(), true);
    }

    //endregion
}
