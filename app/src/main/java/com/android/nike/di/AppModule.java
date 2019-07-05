package com.android.nike.di;

import com.android.nike.UrbanApplication;
import com.android.nike.constants.AppConstants;
import com.android.nike.network.UrbanAPIService;
import com.android.nike.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {


    @Provides
    @ApplicationScope
    Gson provideGson(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return gson;
    }

    @Provides
    @ApplicationScope
    Retrofit provideRetrofit(Gson gson){
       final  int  cacheSize =  (5 * 1024 * 1024);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder  httpClient = new OkHttpClient.Builder();

        //cache control
        final CacheControl cacheControl = new CacheControl.Builder()
                .maxStale(2, TimeUnit.DAYS)
                .build();

        //intercepter
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (AppUtils.isNetworkAvailable(UrbanApplication.getAppContext())) {
                    request = request.newBuilder().removeHeader("Pragma").header("Cache-Control", "public, max-age=" + 60).build();
                } else {
                    request = request.newBuilder().removeHeader("Pragma").header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                }

                return chain.proceed(request);
            }
        });

        httpClient.cache(AppUtils.provideCache());
        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

    @Provides
    @ApplicationScope
   UrbanAPIService provideUrbanAPIService(Gson gson, Retrofit retrofit){
        UrbanAPIService urbanAPIService = new UrbanAPIService(gson, retrofit);
        return urbanAPIService;
    }
}
