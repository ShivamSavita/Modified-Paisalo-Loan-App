package com.softeksol.paisalo.jlgsourcing.retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public static final String BASE_URL = "https://agra.seil.in:8444/PLServicev82/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient(String BASE_URL) {
        if (retrofit==null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }




    public static  Retrofit getClientdynamic(String deviceId,String databaseName) {
        Retrofit retrofit = null;
        if (retrofit==null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                                          @Override
                                          public Response intercept(Chain chain) throws IOException {
                                              Request original = chain.request();

                                              Request request = original.newBuilder()
                                                      .header("procname", "")
                                                      .header("Content-Encoding", "gzip,deflate,compress")
                                                      .header("imeino", "354690572942373")
                                                      .header("devid", deviceId)
                                                      .header("dbname", databaseName)
                                                      .method(original.method(), original.body())
                                                      .build();

                                              return chain.proceed(request);
                                          }
                                      });

                    OkHttpClient client = httpClient.build();
//            OkHttpClient httpClient = new OkHttpClient();
//            httpClient.networkInterceptors().add(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request.Builder requestBuilder = chain.request().newBuilder();
//                    requestBuilder.header("accept", "application/json");
//                    requestBuilder.header("access", "application/json");
//                    requestBuilder.header("procname", "");
//                    requestBuilder.header("Content-Encoding", "gzip,deflate,compress");
//                    requestBuilder.header("imeino", "354690572942373");
//                    requestBuilder.header("devid", "8519713985787243");
//                    requestBuilder.header("dbname", "SBIPDLCOL");
//                    return chain.proceed(requestBuilder.build());
//                }
//            });

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
