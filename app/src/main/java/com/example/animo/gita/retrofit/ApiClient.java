package com.example.animo.gita.retrofit;

import android.text.TextUtils;

import com.example.animo.gita.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    public static <S> S createService(Class<S> serviceClass , final String authToken) {

        if(!TextUtils.isEmpty(authToken)) {

            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", " token " + authToken)
                            .cacheControl(CacheControl.FORCE_NETWORK)
                            .build();
                    return chain.proceed(request);
                }
            };
            httpClient.addInterceptor(interceptor);
        }

            httpClient.addInterceptor(loggingInterceptor);
            httpClient.retryOnConnectionFailure(false);
            httpClient.readTimeout(1, TimeUnit.SECONDS);
            builder.client(httpClient.build());
            retrofit = builder.build();

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
