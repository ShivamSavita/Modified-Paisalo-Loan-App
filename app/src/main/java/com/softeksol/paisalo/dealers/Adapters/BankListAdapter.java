package com.softeksol.paisalo.dealers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.softeksol.paisalo.dealers.Models.BankListMaster;
import com.softeksol.paisalo.dealers.Models.CreatorAllResponse;
import com.softeksol.paisalo.jlgsourcing.R;

import java.util.List;

public class BankListAdapter extends ArrayAdapter {
    Context context;
    BankListMaster[] bankListMaster;
    public BankListAdapter(Context context, BankListMaster[] bankListMaster) {
        super(context,0,bankListMaster);
        this.context =context;
        this.bankListMaster=bankListMaster;
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
        BankListMaster currentItem =  bankListMaster[position];

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            textViewName.setText(currentItem.getBankName());
        }
        return convertView;
    }
}
