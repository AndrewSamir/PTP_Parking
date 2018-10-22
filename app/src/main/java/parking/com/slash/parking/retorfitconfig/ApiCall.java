package parking.com.slash.parking.retorfitconfig;

import java.util.Map;

import parking.com.slash.parking.model.ModelCommenResponse.ModelCommenResponse;
import parking.com.slash.parking.model.ModelCommonRequest.ModelCommonRequest;
import parking.com.slash.parking.model.ModelGetAddressFromMap.ModelGetAddressFromMap;
import parking.com.slash.parking.model.ModelGetNearBy.ModelGetNearByRequest;
import parking.com.slash.parking.model.ModelLeaverBookRequest.ModelLeaverBookRequest;
import parking.com.slash.parking.model.ModelLoginRequest.ModelLoginRequest;
import parking.com.slash.parking.utlities.Constant;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiCall {

    @GET(Constant.baseUrl + "Account/GetModelsList")
    Call<ModelCommenResponse> callGetModelsList(@Query("BrandID") String brandId);

    @GET(Constant.baseUrl + "Account/GetBrandsList")
    Call<ModelCommenResponse> callGetBrandsList();

    @POST(Constant.baseUrl + "Account/login")
    Call<ModelCommenResponse> callLogin(@Body ModelLoginRequest modelLoginRequest);

    @POST(Constant.baseUrl + "Requests/GetNearby")
    Call<ModelCommenResponse> callGetNearby(@Body ModelGetNearByRequest modelGetNearByRequest, @HeaderMap Map<String, String> headers);

    @POST(Constant.baseUrl + "Requests/SeekerBook")
    Call<ModelCommenResponse> callSeekerBook(@Body ModelCommonRequest modelCommonRequest);

    @POST(Constant.baseUrl + "Requests/CancelRequest")
    Call<ModelCommenResponse> callCancelRequest(@Body ModelCommonRequest modelCommonRequest);

    @POST(Constant.baseUrl + "Requests/ConfirmRequest")
    Call<ModelCommenResponse> callConfirmRequest(@Body ModelCommonRequest modelCommonRequest);

    @POST(Constant.baseUrl + "Account/Register")
    Call<ModelCommenResponse> callRegister(@Query("Email") String email,
                                           @Query("Password") String password,
                                           @Query("Mobile") String mobile,
                                           @Query("Name") String name,
                                           @Query("CarNumber") String carNumber,
                                           @Query("carModelID") String carModelID,
                                           @Query("CarColor ") String carColor,
                                           @Query("IsPaymentVerified") Boolean isPaymentVerified);

    @POST(Constant.baseUrl + "Account/CheckExist")
    Call<ModelCommenResponse> callCheckExist(@Body ModelCommonRequest modelCommonRequest);

    @POST(Constant.baseUrl + "Requests/LeaverBook")
    Call<ModelCommenResponse> callLeaverBook(@Body ModelLeaverBookRequest modelLeaverBookRequest,@HeaderMap Map<String, String> headers);

    @GET("https://maps.googleapis.com/maps/api/geocode/json")
    Call<ModelGetAddressFromMap> callGetAddressFromMap(@Query("latlng") String latlng, @Query("key") String key, @Query("language") String language);

}

