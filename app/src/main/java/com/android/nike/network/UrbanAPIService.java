package com.android.nike.network;

import android.database.Observable;

import com.android.nike.model.DictionaryObject;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class UrbanAPIService {

    private Gson gson;
    private Retrofit retrofit;

    public UrbanAPIService(Gson gson, Retrofit retrofit) {
        this.gson = gson;
        this.retrofit = retrofit;
    }
    public Call<DictionaryObject> getDataFromRemote(){
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<DictionaryObject> observableRepo = apiInterface.doGetDictionaryList();
        return observableRepo;
    }
}
