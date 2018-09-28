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


public class RestSha3er
{

    private static RestSha3er instance;
    private static ApiCall apiService;
    //public final String BASE_URL = "http://192.168.1.222/";
    private static Context mcontext;
    // public String apiValue = "9c4a06e4dddceb70722de4f3fda4f2c7";
    public String apiKey = "ApiKey";
    public String Authorization = "Authorization";

    private RestSha3er()
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

    public static RestSha3er getInstance(Context context)
    {
        mcontext = context;
        if (instance == null)
        {
            instance = new RestSha3er();
        }
        return instance;
    }

    public ApiCall getClientService()
    {

        return apiService;
    }
}