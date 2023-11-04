package com.softeksol.paisalo.ESign.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.softeksol.paisalo.ESign.viewHolder.ESignBorrowerViewHolder;

import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.entities.ESignBorrower;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by sachindra on 2016-10-08.
 */
public class AdapterListESignBorrower extends ArrayAdapter<ESignBorrower> {
    Context context;
    int resourecId;
    List<ESignBorrower> eSignBorrowers = null;
    ArrayList<ESignBorrower> arraylistEsign;

    public AdapterListESignBorrower(Context context, @LayoutRes int resource, @NonNull List<ESignBorrower> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourecId = resource;
        this.eSignBorrowers = objects;
       // this.arraylistEsign= new ArrayList<ESignBorrower>();
       // this.arraylistEsign.addAll(eSignBorrowers);
    }

    @Override
    public int getCount() {
        return eSignBorrowers.size();
    }

    @Override
    public ESignBorrower getItem(int position) {
        return  eSignBorrowers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ESignBorrowerViewHolder holder;
        if (v == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            v = inflater.inflate(resourecId, parent, false);

            holder = new ESignBorrowerViewHolder();

            holder.tvBorrowerName = (TextView) v.findViewById(R.id.itemLayoutCustomerName);
            holder.tvBorrowerGuardian = (TextView) v.findViewById(R.id.itemLayoutCustomerFather);
            holder.tvCreator = (TextView) v.findViewById(R.id.itemLayoutCustomerCreator);
            holder.tvFiCode = (TextView) v.findViewById(R.id.itemLayoutCustomerFiId);
            holder.tvMobile = (TextView) v.findViewById(R.id.itemLayoutCustomerMobile);
            holder.tvAddress = (TextView) v.findViewById(R.id.itemLayoutCustomerAddress);
            holder.layout_overAll = (LinearLayout) v.findViewById(R.id.layout_overAll);
         // holder.tvAadhar = (TextView) v.findViewById(R.id.itemLayoutCustomer);

            v.setTag(holder);
        } else {
            holder = (ESignBorrowerViewHolder) v.getTag();
        }

        ESignBorrower eSignBorrower = eSignBorrowers.get(position);

        holder.tvBorrowerName.setText(eSignBorrower.PartyName);
        holder.tvBorrowerGuardian.setText(eSignBorrower.FatherName);
        holder.tvCreator.setText(eSignBorrower.Creator);
        holder.tvFiCode.setText(String.valueOf(eSignBorrower.FiCode));
        holder.tvMobile.setText(eSignBorrower.MobileNo);
        holder.tvAddress.setText(eSignBorrower.Address);
//      holder.tvAadhar.setText(eSignBorrower.AadharNo);

        if (eSignBorrower.ESignSucceed.equals("BLK")) {
            holder.layout_overAll.setBackgroundColor(Color.RED);
        }else {
            holder.layout_overAll.setBackgroundColor(Color.WHITE);
        }
        return v;
    }

    public void filter(String charText) {
        Log.e("DATA--- ",charText);
        charText = charText.toLowerCase(Locale.getDefault());
        eSignBorrowers.clear();
        if (charText.length() ==0) {
            Log.e("DATA0--- ",charText);
            eSignBorrowers.addAll(arraylistEsign);
        } else {
            Log.e("DATAELSE--- ",arraylistEsign.size()+"");
            for (ESignBorrower wp : arraylistEsign) {

                if (String.valueOf(wp.FiCode).contains(charText)) {
                    eSignBorrowers.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void addArraylist(List<ESignBorrower> eSignedList) {
        this.arraylistEsign= new ArrayList<ESignBorrower>();
        this.arraylistEsign.addAll(eSignedList);

    }

}
