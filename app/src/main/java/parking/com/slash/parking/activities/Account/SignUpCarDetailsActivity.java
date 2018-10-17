package parking.com.slash.parking.activities.Account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.MainActivity;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.model.ModelCarBrand.ModelCarBrand;
import parking.com.slash.parking.model.ModelLoginRequest.ModelLoginRequest;
import parking.com.slash.parking.model.ModelLoginResponse.ModelLoginResponse;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.DataEnum;
import parking.com.slash.parking.utlities.SharedPrefHelper;
import retrofit2.Call;


public class SignUpCarDetailsActivity extends Activity implements HandleRetrofitResp
{
    //region fields
    String brandId, modelId;
    Intent intent;
    //endregion

    //region views
    @BindView(R.id.edtSignUpCarColor)
    EditText edtSignUpCarColor;
    @BindView(R.id.edtSignUpCarPlate)
    EditText edtSignUpCarPlate;
    @BindView(R.id.imgSignUp)
    ImageView imgSignUp;
    @BindView(R.id.tvSignUpConnectPaypal)
    TextView tvSignUpConnectPaypal;
    @BindView(R.id.tvSignUpCarBrand)
    TextView tvSignUpCarBrand;
    @BindView(R.id.tvSignUpCarModel)
    TextView tvSignUpCarModel;
    @BindView(R.id.chSignUpAcceptTerms)
    CheckBox chSignUpAcceptTerms;

    //endregion

    //region life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_car_details);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        intent = getIntent();
        HandleCalls.getInstance(this).setonRespnseSucess(this);
    }

    //endregion

    //region clicks

    @OnClick(R.id.tvSignUpCarBrand)
    void onClicktvSignUpCarBrand(View view)
    {
//        callGetBrandsList();
    }

    @OnClick(R.id.tvSignUpCarModel)
    void onClicktvSignUpCarModel(View view)
    {
        if (brandId != null)
            callGetModelsList(brandId);
    }

    @OnClick(R.id.tvSignUpSelectImage)
    void onClicktvSignUpSelectImage(View view)
    {

    }

    @OnClick(R.id.tvSignUpConnectPaypal)
    void onClicktvSignUpConnectPaypal(View view)
    {

    }

    @OnClick(R.id.tvSignUpTerms)
    void onClicktvSignUpTerms(View view)
    {

    }

    @OnClick(R.id.btnSignUp)
    void onClickbtnSignUp(View view)
    {
        callRegister();
    }


    //endregion

    //region calls

    private void callGetBrandsList()
    {
        Call call = HandleCalls.restParki.getClientService().callGetBrandsList();
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callGetBrandsList.name(), true);
    }

    private void callGetModelsList(String brandId)
    {
        Call call = HandleCalls.restParki.getClientService().callGetModelsList(brandId);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callGetModelsList.name(), true);
    }

    private void callRegister()
    {
        Call call = HandleCalls.restParki.getClientService().callRegister(intent.getStringExtra(DataEnum.regMail.name()),
                intent.getStringExtra(DataEnum.regPassword.name()),
                intent.getStringExtra(DataEnum.regMobile.name()),
                intent.getStringExtra(DataEnum.regName.name()),
                edtSignUpCarPlate.getText().toString(),
                "ee24d362-d932-440c-84d6-550d28307b33",
                edtSignUpCarColor.getText().toString(),
                false);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callRegister.name(), true);
    }

    private void callLogin()
    {

        ModelLoginRequest modelLoginRequest = new ModelLoginRequest();
        modelLoginRequest.setUsername(intent.getStringExtra(DataEnum.regMail.name()));
        modelLoginRequest.setPassword(intent.getStringExtra(DataEnum.regPassword.name()));
        modelLoginRequest.setDevicetoken(FirebaseInstanceId.getInstance().getToken());
        Call call = HandleCalls.restParki.getClientService().callLogin(modelLoginRequest);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callLogin.name(), true);
    }

    //endregion

    //region call response
    @Override
    public void onResponseSuccess(String flag, Object o)
    {


        if (flag.equals(DataEnum.callGetBrandsList.name()))
        {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
            ModelCarBrand modelCarBrand = gson.fromJson(jsonObject, ModelCarBrand.class);

            String[] brandNames = new String[modelCarBrand.getModel().size() + 1];
            String[] brandIds = new String[modelCarBrand.getModel().size() + 1];
       /*     arrLevel[0] = context.getString(R.string.all_services);
            arrId[0] = "0";*/
            for (int i = 0; i < modelCarBrand.getModel().size(); i++)
            {
                brandNames[i] = modelCarBrand.getModel().get(i).getName();
                brandIds[i] = modelCarBrand.getModel().get(i).getId();
            }
            showListDialog(brandNames, brandIds, DataEnum.brand.name());

        } else if (flag.equals(DataEnum.callGetModelsList.name()))
        {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
            ModelCarBrand modelCarBrand = gson.fromJson(jsonObject, ModelCarBrand.class);


            String[] brandNames = new String[modelCarBrand.getModel().size() + 1];
            String[] brandIds = new String[modelCarBrand.getModel().size() + 1];
       /*     arrLevel[0] = context.getString(R.string.all_services);
            arrId[0] = "0";*/
            for (int i = 0; i < modelCarBrand.getModel().size(); i++)
            {
                brandNames[i] = modelCarBrand.getModel().get(i).getName();
                brandIds[i] = modelCarBrand.getModel().get(i).getId();
            }
            showListDialog(brandNames, brandIds, DataEnum.carModel.name());

        } else if (flag.equals(DataEnum.callRegister.name()))
        {
            callLogin();
        } else if (flag.equals(DataEnum.callLogin.name()))
        {
            if (flag.equals(DataEnum.callLogin.name()))
            {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.toJsonTree(o).getAsJsonObject();
                ModelLoginResponse modelGetNearByResponse = gson.fromJson(jsonObject, ModelLoginResponse.class);

                SharedPrefHelper.getInstance(this).setUser(modelGetNearByResponse.getModel());
                startActivity(new Intent(this, MainActivity.class));
                finish();

            }
        }

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

    //region functions

    private void showListDialog(final String[] arrName, final String[] arrId, final String flag)
    {
        List<String> sss = new ArrayList<>();
        sss.add("ads");
        sss.add("ads");
        new MaterialDialog.Builder(SignUpCarDetailsActivity.this)
                .title(flag)
                .items(sss)

                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice()
                {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text)
                    {
                        if (flag.equals(DataEnum.brand.name()))
                        {
                            brandId = arrId[which];
                            tvSignUpCarBrand.setText(arrName[which]);
                        } else if (flag.equals(DataEnum.carModel.name()))
                        {
                            modelId = arrId[which];
                            tvSignUpCarModel.setText(arrName[which]);
                        }

                        dialog.dismiss();
                        return true;
                    }
                })
                .negativeText(this.getString(R.string.cancel))
                .show();


        tvSignUpCarBrand.setText("dcghjkls");
    }
    //endregion
}
