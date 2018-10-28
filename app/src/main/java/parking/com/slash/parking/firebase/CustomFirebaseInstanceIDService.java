package parking.com.slash.parking.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.retorfitconfig.HandleCalls;
import parking.com.slash.parking.utlities.SharedPrefHelper;
import retrofit2.Call;


public class CustomFirebaseInstanceIDService extends FirebaseInstanceIdService implements HandleRetrofitResp
{
    private static final String TAG = CustomFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh()
    {
        //Log.e("id","hh");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        SharedPrefUtil.getInstance(getApplicationContext()).write(DataEnum.shFirebaseToken.name(),refreshedToken);
        callUpdateDeviceToken();

        Log.e(TAG, "Token Value: " + refreshedToken);

        // sendTheRegisteredTokenToWebServer(refreshedToken);
    }

    private void sendTheRegisteredTokenToWebServer(final String token)
    {
//deviceStartUp
        // HashMap meMap = new HashMap<String, String>();
        // meMap.put(Constant.apiKey, Constant.apiValue);
        //meMap.put(Constant.DEVICE,token);
        //Device


    }

    private void callUpdateDeviceToken()
    {

        /*ModelRegisterRequest modelRegisterRequest = new ModelRegisterRequest();

        modelRegisterRequest.setDeviceToken(FirebaseInstanceId.getInstance().getToken());
        modelRegisterRequest.setOS("Android");
        HandleCalls.getInstance(getApplicationContext()).setonRespnseSucess(this);
        Call call = HandleCalls.restMalaaby.getClientService().callUpdateDeviceToken(SharedPrefHelper.getInstance(getApplicationContext()).getAccessToken(), modelRegisterRequest);
        HandleCalls.getInstance(getApplicationContext()).callRetrofit(call, "flag", false);
    */}

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
}
