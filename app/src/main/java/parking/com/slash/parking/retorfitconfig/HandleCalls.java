package parking.com.slash.parking.retorfitconfig;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import developer.mokadim.projectmate.dialog.IndicatorStyle;
import developer.mokadim.projectmate.dialog.ProgressDialog;
import parking.com.slash.parking.R;
import parking.com.slash.parking.activities.BaseActivity;
import parking.com.slash.parking.interfaces.HandleRetrofitResp;
import parking.com.slash.parking.interfaces.HandleRetrofitRespAdapter;
import parking.com.slash.parking.model.ModelCommenResponse.ModelCommenResponse;
import parking.com.slash.parking.utlities.HelpMe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static parking.com.slash.parking.utlities.HelpMe.getMaterialDialogBuilder;


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

    Dialog progressDialog;

    public <T> void callRetrofit(Call<ModelCommenResponse> call, final String flag, final Boolean showLoading) {
        if (showLoading) {
            progressDialog = new ProgressDialog(context, IndicatorStyle.BallBeat).show();
            progressDialog.show();
        }
        call.enqueue(new Callback<ModelCommenResponse>() {
            @Override
            public void onResponse(Call<ModelCommenResponse> call, Response<ModelCommenResponse> response) {
                if (showLoading)
                    progressDialog.dismiss();

                if (response.code() == 200) {

                    ModelCommenResponse modelCommenResponse = response.body();
                    if (modelCommenResponse.getResponseMessage() != null)
                        showMessage(null, modelCommenResponse.getResponseMessage());
                    if (modelCommenResponse.getData() != null && modelCommenResponse.getStatus() == 1)
                        onRespnse.onResponseSuccess(flag, modelCommenResponse.getData());
                    else if (modelCommenResponse.getStatus() == 1)
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
//                        ((BaseActivity) context).showMessage(jObjError.getString("message"));

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
                    progressDialog.dismiss();

                showMessage(null, context.getString(R.string.internet_connection));

//                HelpMe.getInstance(context).retrofironFailure(t);
            }
        });

    }

    public void showMessage(@Nullable String title, @NonNull String message) {

        MaterialDialog.Builder builder = getMaterialDialogBuilder();
        builder.content(message);
        if (title != null) {
            builder.title(title);
        }

        builder.content(message).positiveText(R.string.agree).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).autoDismiss(true).titleGravity(GravityEnum.CENTER).contentGravity(GravityEnum.CENTER).show();

    }

    public static MaterialDialog.Builder getMaterialDialogBuilder() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
//        builder.typeface("TheSansArabic-Bold.otf", "TheSansArabic-Plain.otf");

        return builder;
    }

    public <ModelGetAddressFromMap> void callRetrofitGoogleAPi(Call<ModelGetAddressFromMap> call, final String flag, final Boolean showLoading) {
        progressDialog = new ProgressDialog(context, IndicatorStyle.BallBeat).show();


        call.enqueue(new Callback<ModelGetAddressFromMap>() {
            @Override
            public void onResponse(Call<ModelGetAddressFromMap> call, Response<ModelGetAddressFromMap> response) {
                progressDialog.dismiss();

                if (response.code() == 200) {
                    ModelGetAddressFromMap modelCommenResponse = response.body();
                    onRespnse.onResponseSuccess(flag, modelCommenResponse);
                }
            }

            @Override
            public void onFailure(Call<ModelGetAddressFromMap> call, Throwable t) {
                progressDialog.dismiss();

                onRespnse.onNoContent(flag, 0);
                HelpMe.getInstance(context).retrofironFailure(t);
            }
        });

    }


}