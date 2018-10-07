package parking.com.slash.parking.retorfitconfig;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.BaseActivity;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.interfaces.HandleRetrofitRespAdapter;
import parking.com.slash.parking.model.ModelCommenResponse.ModelCommenResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HandleCalls {
    private static Context context;
    private static HandleCalls instance = null;
    public static RestParki restParki;
    private HandleRetrofitResp onRespnse;
    private HandleRetrofitRespAdapter onRespnseAdapter;
    //private HandleNoContent onNoContent;

    public static HandleCalls getInstance(Context context) {

        HandleCalls.context = context;

        if (instance == null) {
            instance = new HandleCalls();
            restParki = RestParki.getInstance(context);
        }
        return instance;
    }

    public void setonRespnseSucess(HandleRetrofitResp onRespnseSucess) {
        this.onRespnse = onRespnseSucess;
    }

    public void setonRespnseSucessApapter(HandleRetrofitRespAdapter onRespnseAdapter) {
        this.onRespnseAdapter = onRespnseAdapter;
    }


    public <T> void callRetrofit(Call<ModelCommenResponse> call, final String flag, final Boolean showLoading) {
        if (showLoading)
            ((BaseActivity) context).showLoading(true);
        call.enqueue(new Callback<ModelCommenResponse>() {
            @Override
            public void onResponse(Call<ModelCommenResponse> call, Response<ModelCommenResponse> response) {
                if (showLoading)
                    ((BaseActivity) context).showLoading(false);

                if (response.code() == 200) {

                    ModelCommenResponse modelCommenResponse = response.body();
                    if (modelCommenResponse.getResponseMessage() != null)
                        ((BaseActivity) context).showMessage(modelCommenResponse.getResponseMessage());
                    if (modelCommenResponse.getData() != null && modelCommenResponse.getStatus().equals(context.getString(R.string.success)))
                        onRespnse.onResponseSuccess(flag, modelCommenResponse.getData());
                    else if (modelCommenResponse.getStatus().equals(context.getString(R.string.success)))
                        onRespnse.onNoContent(flag, response.code());


                } else if (response.code() == 406) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        ((BaseActivity) context).showMessage(jObjError.getString("message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 500) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        ((BaseActivity) context).showMessage(jObjError.getString("message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<ModelCommenResponse> call, Throwable t) {
                if (showLoading)
                    ((BaseActivity) context).showLoading(false);

                ((BaseActivity) context).showMessage(((BaseActivity) context).getString(R.string.internet_connection));

//                HelpMe.getInstance(context).retrofironFailure(t);
            }
        });

    }

}