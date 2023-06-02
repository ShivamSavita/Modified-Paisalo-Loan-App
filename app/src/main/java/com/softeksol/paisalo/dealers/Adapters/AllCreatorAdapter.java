package com.softeksol.paisalo.dealers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.softeksol.paisalo.dealers.Models.BrandData;
import com.softeksol.paisalo.dealers.Models.CreatorAllResponse;
import com.softeksol.paisalo.dealers.OEM_Onboarding;
import com.softeksol.paisalo.jlgsourcing.R;

import java.util.Collections;
import java.util.List;

public class AllCreatorAdapter extends ArrayAdapter {
    Context context;
    List<CreatorAllResponse> creatorAllResponseList;
    public AllCreatorAdapter(Context context, List<CreatorAllResponse> creatorAllResponseList) {
        super(context,0,creatorAllResponseList);
        this.context =context;
        this.creatorAllResponseList=creatorAllResponseList;
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
        CreatorAllResponse currentItem =  creatorAllResponseList.get(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            textViewName.setText(currentItem.getCreator());
        }
        return convertView;
    }
}
