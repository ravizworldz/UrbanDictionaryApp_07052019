package com.android.nike.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.android.nike.UrbanApplication;
import com.android.nike.model.DictionaryDataModel;
import com.android.nike.model.DictionaryObject;
import com.android.nike.network.APIInterface;

import com.android.nike.network.UrbanAPIService;
import com.android.nike.utils.LogUtil;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HTTP;

public class DictionaryViewModel extends AndroidViewModel {
    private static final String TAG = "DictionaryViewModel";
    private MutableLiveData<List<DictionaryDataModel>> dictionaryDataModels;

    @Inject
    APIInterface service;


    public DictionaryViewModel(@NonNull Application application) {
        super(application);
        LogUtil.log(TAG,"DictionaryViewModel constructor called");


    }

    public MutableLiveData<List<DictionaryDataModel>>  getDictionaryData() {
        LogUtil.log(TAG,"DictionaryViewModel getDictionaryData called : " + dictionaryDataModels);
        if (dictionaryDataModels == null) {
            dictionaryDataModels = new MutableLiveData<>();
            loadData();
        }
        return dictionaryDataModels;
    }

    public void  loadData() {
        LogUtil.log(TAG,"DictionaryViewModel loadData called : ");

        UrbanAPIService apiService = ((UrbanApplication)getApplication()).getGitHubService();
        Call<DictionaryObject> call =   apiService.getDataFromRemote();
        call.enqueue(new Callback<DictionaryObject>() {
            @Override
            public void onResponse(Call<DictionaryObject> call, Response<DictionaryObject> response) {
                LogUtil.log(TAG,"getDictionaryData onResponse called");

                if(response != null ){
                    if(response.code() == 200){
                        LogUtil.log(TAG, response.code() + "");
                        LogUtil.log(TAG, response.body().toString() + "");
                        LogUtil.log(TAG, "=====" + response.body().getListDictionary().size());
                        LogUtil.log(TAG, "=====" + response.body().getListDictionary().get(0).getDefinition());
                        dictionaryDataModels.setValue(response.body().getListDictionary());
                    } else {
                        LogUtil.log(TAG, response.code() + "");
                        dictionaryDataModels.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<DictionaryObject> call, Throwable t) {
                LogUtil.log(TAG,"getDictionaryData onFailure called " + t.toString());
                dictionaryDataModels.setValue(null);
                call.cancel();
            }
        });
    }
}
