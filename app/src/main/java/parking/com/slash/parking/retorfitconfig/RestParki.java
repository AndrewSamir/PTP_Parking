package parking.com.slash.parking.retorfitconfig;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import parking.com.slash.parking.utlities.Constant;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestParki
{

    private static RestParki instance;
    private static ApiCall apiService;
    //public final String BASE_URL = "http://192.168.1.222/";
    private static Context mcontext;
    // public String apiValue = "9c4a06e4dddceb70722de4f3fda4f2c7";
    public String apiKey = "ApiKey";
    public String Authorization = "Authorization";

    private RestParki()
    {


        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.SECONDS);


        //coment start


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);

        //comment end
        //  httpClient.addInterceptor(interceptor).build();


        builder.addInterceptor(new Interceptor()
        {
            @Override
            public Response intercept(Chain chain) throws IOException
            {

                Request request = chain.request();
                Request newRequest;

                //  String firebasetoken = "";

                newRequest = request.newBuilder()

                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Authorization", "bearer Q1mYjBdHt-mGLllRjrPFLGfu20so2XRtp0fYk4h7SXiy8bFt9Xqrn_9bQkaceL0OYkc9p5VSpDtUazeRdblyeVBrjbkqeqk4N4EsrwXNYYtySr5kwqTEd07nvOTZTQoiZWzklHFUng-e_M8XBHnOGtqF7Z_n06yuyaNPSvibrnBjueChUAfQek2Niu-3nLHh_atixYfHdevC_GDEBKlMW7Wym119GzVdR3ntWJTmhbzzmCVh3bTiACWY-9mhHvFcrrBFGOIxRebHKxhzkEtDZQFcwcm4XRzNfkoxCntyDm6oEgI0HxvLplerB-Vm3_IsIOV23rdQ2k9EMe7b1eEV8o2lGnsGb_MvTb-jxMsFcRw")
//                        .header("Content-Length", "247")
                        .method(request.method(), request.body())
                        .build();
                return chain.proceed(newRequest);


            }
        });

        OkHttpClient httpClient = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        apiService = retrofit.create(ApiCall.class);
    }

    public static RestParki getInstance(Context context)
    {
        mcontext = context;
        if (instance == null)
        {
            instance = new RestParki();
        }
        return instance;
    }

    public ApiCall getClientService()
    {

        return apiService;
    }
}