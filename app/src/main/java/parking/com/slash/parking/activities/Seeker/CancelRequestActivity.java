package parking.com.slash.parking.activities.Seeker;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.MainActivity;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.model.ModelCommonRequest.ModelCommonRequest;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.DataEnum;
import retrofit2.Call;

public class CancelRequestActivity extends Activity implements HandleRetrofitResp
{

    //region fields
    String id;
    //endregion

    //region life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_request);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra(DataEnum.intentRequestId.name());
        HandleCalls.getInstance(this).setonRespnseSucess(this);

    }
    //endregion

    //region clicks
    @OnClick(R.id.btnCancelRequestBackToParking)
    void onClickbtnCancelRequestBackToParking(View view)
    {
        onBackPressed();
    }

    @OnClick(R.id.btnCancelRequestConfirmCancel)
    void onClickbtnCancelRequestConfirmCancel(View view)
    {
        startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
    //endregion

    //region calls

    private void callCancelRequest()
    {
        ModelCommonRequest modelCommonRequest = new ModelCommonRequest();
        modelCommonRequest.setRequestID(id);
        modelCommonRequest.setSeeker(true);
        Call call = HandleCalls.restParki.getClientService().callCancelRequest(modelCommonRequest);
        HandleCalls.getInstance(this).callRetrofit(call, DataEnum.callCancelRequest.name(), true);
    }

    //endregion

    //region call response
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

}
