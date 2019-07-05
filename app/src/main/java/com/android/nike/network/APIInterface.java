package com.android.nike.network;

import com.android.nike.model.DictionaryDataModel;
import com.android.nike.model.DictionaryObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface APIInterface {

    @Headers({"Content-Type: application/json","X-RapidAPI-Host: mashape-community-urban-dictionary.p.rapidapi.com", "X-RapidAPI-Key: cdbc447229mshdcff5b2aad3ce20p10febajsnad07737bcf8b"})
    @GET("define?term=wat")
    Call<DictionaryObject> doGetDictionaryList();


}
