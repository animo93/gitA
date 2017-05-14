package com.example.animo.gita;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by animo on 10/5/17.
 */

public class ApiClient {

    private static Retrofit retrofit = null;

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY
    );

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Constants.ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {

        if(!TextUtils.isEmpty(Constants.AUTH_TOKEN)) {

            /*Authenticator authenticator = new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    Log.e("ApiClient","Token "+Constants.AUTH_TOKEN);

                    return response.request().newBuilder()
                            .header("Authorization",Constants.AUTH_TOKEN)
                            .build();
                }
            };*/

            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization"," token "+Constants.AUTH_TOKEN)
                            .build();
                    return chain.proceed(request);
                }
            };
            httpClient.addInterceptor(loggingInterceptor);

            //httpClient.authenticator(authenticator);
            httpClient.addInterceptor(interceptor);

            builder.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }

    public static Retrofit getClient() {

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.ROOT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }


}
