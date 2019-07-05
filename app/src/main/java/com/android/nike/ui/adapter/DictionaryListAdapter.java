package com.android.nike.ui.adapter;

import android.content.Context;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.android.nike.R;
import com.android.nike.model.DictionaryDataModel;

import com.android.nike.utils.LogUtil;

import java.util.List;

import javax.inject.Inject;

public class DictionaryListAdapter  extends RecyclerView.Adapter<DictionaryListAdapter.CustomViewHolder> {

    private static final String TAG = "DictionaryListAdapter";
    private List<DictionaryDataModel> dataList;

    private Context context;

    @Inject
    public DictionaryListAdapter(Context context){
        LogUtil.log(TAG,"adapter DictionaryListAdapter constructor called ");
        this.context = context;
    }

    public void setDataList(List<DictionaryDataModel> dataList) {
        LogUtil.log(TAG,"adapter setDataList called : dataList : " + dataList);
        this.dataList = dataList;

    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView title, auther, thumsup,thumsdown;

        public CustomViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            auther = view.findViewById(R.id.address);
            thumsup = view.findViewById(R.id.thumsup);
            thumsdown = view.findViewById(R.id.thumsdown);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtil.log(TAG,"adapter onCreateViewHolder called : ");

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_list, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        LogUtil.log(TAG,"adapter onBindViewHolder called : position : " + position);
        final DictionaryDataModel model = dataList.get(position);

        LogUtil.log(TAG,"adapter onBindViewHolder called : position : " + model.getDefinition());
        LogUtil.log(TAG,"adapter onBindViewHolder called : position : " + model.getAuthor());

        holder.title.setText(model.getDefinition());
        holder.auther.setText("Auther "+model.getAuthor());
        holder.thumsup.setText("Thumbs up "+model.getThumbs_up());
        holder.thumsdown.setText("Thumbs down "+model.getThumbs_down());

        if(position %2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(dataList != null) {
            LogUtil.log(TAG, "adapter getItemCount called : dataList.size() : " + dataList.size());
            return dataList.size();
        }else{
            return 0;
        }
    }



    public void onItemClick(DictionaryDataModel f) {
        Toast.makeText(context, "You clicked " + f.getDefinition(),
                Toast.LENGTH_LONG).show();
    }
}
