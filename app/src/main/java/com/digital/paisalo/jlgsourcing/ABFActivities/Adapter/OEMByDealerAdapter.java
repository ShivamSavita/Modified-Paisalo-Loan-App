package com.digital.paisalo.jlgsourcing.ABFActivities.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.digital.paisalo.jlgsourcing.ABFActivities.Model.OEMByCreatorModel;
import com.digital.paisalo.jlgsourcing.R;

public class OEMByDealerAdapter extends ArrayAdapter {
    Context context;
    OEMByCreatorModel[] nameList;

    public OEMByDealerAdapter(Context context, OEMByCreatorModel[] nameList) {
        super(context,0,nameList);
        this.context=context;
        this.nameList=nameList;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable
    View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
    View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_card_orange, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.text_cname);
        OEMByCreatorModel currentItem =  nameList[position];

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            textViewName.setText(currentItem.getFirmName());
        }
        return convertView;
    }
}





