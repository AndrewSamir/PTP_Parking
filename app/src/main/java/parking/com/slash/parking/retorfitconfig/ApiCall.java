package parking.com.slash.parking.retorfitconfig;

import parking.com.slash.parking.model.ModelCommenResponse.ModelCommenResponse;
import parking.com.slash.parking.model.ModelCommonRequest.ModelCommonRequest;
import parking.com.slash.parking.model.ModelGetNearBy.ModelGetNearByRequest;
import parking.com.slash.parking.model.ModelLoginRequest.ModelLoginRequest;
import parking.com.slash.parking.utlities.Constant;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiCall
{

    @GET(Constant.baseUrl + "Account/GetModelsList")
    Call<ModelCommenResponse> callGetModelsList(@Query("BrandID") String brandId);

    @GET(Constant.baseUrl + "Account/GetBrandsList")
    Call<ModelCommenResponse> callGetBrandsList();

    @POST(Constant.baseUrl + "Account/login")
    Call<ModelCommenResponse> callLogin(@Body ModelLoginRequest modelLoginRequest);

    @POST(Constant.baseUrl + "Requests/GetNearby")
    Call<ModelCommenResponse> callGetNearby(@Body ModelGetNearByRequest modelGetNearByRequest);

    @POST(Constant.baseUrl + "Account/Register")
    Call<ModelCommenResponse> callRegister(@Query("email") String email,
                                           @Query("password") String password,
                                           @Query("mobile") String mobile,
                                           @Query("name") String name,
                                           @Query("carNumber") String carNumber,
                                           @Query("carModelID") String carModelID,
                                           @Query("carColor") String carColor,
                                           @Query("isPaymentVerified") String isPaymentVerified);

    @POST(Constant.baseUrl + "Account/CheckExist")
    Call<ModelCommenResponse> callCheckExist(@Body ModelCommonRequest modelCommonRequest);


}

