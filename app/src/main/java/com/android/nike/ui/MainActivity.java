package com.android.nike.ui;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.nike.R;
import com.android.nike.model.DictionaryDataModel;
import com.android.nike.ui.adapter.DictionaryListAdapter;
import com.android.nike.ui.viewmodel.DictionaryViewModel;

import com.android.nike.utils.LogUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private DictionaryListAdapter adapter;
    private List<DictionaryDataModel> listData;

    @BindView(R.id.tvnoresult)
    TextView tvNoResult;

    @BindView(R.id.etsearch)
    EditText etSearch;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initUI();

    }

    /**
     * Initialize the UI views
     */
    private void initUI(){

            showProgress();
            DictionaryViewModel dictionaryViewModel = ViewModelProviders.of(this).get(DictionaryViewModel.class);
            dictionaryViewModel.getDictionaryData();

            adapter = new DictionaryListAdapter(this);
            final Observer<List<DictionaryDataModel>> dicObserver = new Observer<List<DictionaryDataModel>>() {
                @Override
                public void onChanged(@Nullable final List<DictionaryDataModel> updatedList) {
                    hideProgress();
                    LogUtil.log(TAG,"MainActivity onChanged called : updatedList : " + updatedList);
                    if(updatedList != null && updatedList.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        tvNoResult.setVisibility(View.GONE);
                        adapter.setDataList(updatedList);
                        recyclerView.setAdapter(adapter);
                        MainActivity.this.listData  = updatedList;
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        tvNoResult.setVisibility(View.VISIBLE);
                    }
                }
            };
            dictionaryViewModel.getDictionaryData().observe(this, dicObserver);
            recyclerView = findViewById(R.id.dicListRecyclerView);
            recyclerView.setAdapter(adapter);

            editTextSearch();

    }

    /**
     * for handle the character typed in the search box
     */
    private void editTextSearch() {

        etSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                searchByTitle(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    /**
     * Handle the button click for displaying all data.
     * @param view
     */
    public void sortAll(View view) {
        if(this.listData != null && this.listData.size() > 0) {
            adapter.setDataList(this.listData);
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * Handle the thumbs up button click
     * @param view
     */
    public void sortByThumbsUp(View view) {
        if(this.listData != null && this.listData.size() > 0) {
            List<DictionaryDataModel> listDataSort = new ArrayList<DictionaryDataModel>(this.listData);
            Collections.sort(listDataSort, new ThumbsUpComparator());
            adapter.setDataList(listDataSort);
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * Comparator class to sort the data for thumbs up
     */
    class ThumbsUpComparator implements Comparator<DictionaryDataModel> {
        @Override
        public int compare(DictionaryDataModel o1, DictionaryDataModel o2) {
            return Integer.valueOf( o2.getThumbs_up()).compareTo(o1.getThumbs_up());
        }
    }

    /**
     * Hanlde the thumbs down button click
     * @param view
     */
    public void sortByThumbsDown(View view) {
        if(this.listData != null && this.listData.size() > 0) {
            List<DictionaryDataModel> listDataSort = new ArrayList<DictionaryDataModel>(this.listData);
            Collections.sort(listDataSort, new ThumbsDownComparator());
            adapter.setDataList(listDataSort);
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * comparator class to sort data for thumbs down
     */
    class ThumbsDownComparator implements Comparator<DictionaryDataModel> {
        @Override
        public int compare(DictionaryDataModel o1, DictionaryDataModel o2) {
            return Integer.valueOf( o2.getThumbs_down()).compareTo(o1.getThumbs_down());
        }
    }

    /**
     * search the element from the all list and display on UI
     * @param searchText
     */
    private void searchByTitle(String searchText) {
        if(this.listData != null && this.listData.size() > 0) {
            List<DictionaryDataModel> listDataSearched = new ArrayList<DictionaryDataModel>();
            for(DictionaryDataModel dataModel : listData){
                if(dataModel.getDefinition().contains(searchText)){
                    listDataSearched.add(dataModel);
                }
            }
            if(listDataSearched != null && listDataSearched.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                tvNoResult.setVisibility(View.GONE);
                adapter.setDataList(listDataSearched);
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                tvNoResult.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * to show the progress on UI
     */
    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * to hide the progress bar
     */
    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }



}
