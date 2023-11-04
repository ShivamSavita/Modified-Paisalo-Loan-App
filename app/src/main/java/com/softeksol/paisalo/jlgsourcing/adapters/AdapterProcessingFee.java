package com.softeksol.paisalo.jlgsourcing.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.entities.DataEMI;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.entities.DataEMI;
import com.softeksol.paisalo.jlgsourcing.entities.ViewHolders.ManagerViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AdapterProcessingFee extends ArrayAdapter<DataEMI> {
    Context context;
    int resourecId;
    List<DataEMI> p_emiData;
    ArrayList<DataEMI> arraylist;

    public AdapterProcessingFee(Context context, @LayoutRes int resource, @NonNull List<DataEMI> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourecId = resource;
        this.p_emiData = objects;
        this.arraylist= new ArrayList<DataEMI>();
        this.arraylist.addAll(p_emiData);
    }

    @Override
    public int getCount() {
        return p_emiData.size();
    }

    @Override
    public DataEMI getItem(int position) {
        return  p_emiData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        FeeViewHolder holder;
        if (v == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            v = inflater.inflate(R.layout.item_processing_fee, parent, false);
            holder = new FeeViewHolder();
            holder.tvlabel_emiamount = (TextView) v.findViewById(R.id.label_emiamount);
           // holder.tvManagerCode = (TextView) v.findViewById(R.id.placeAndGroup);
           // holder.tvManagerCreator = (TextView) v.findViewById(R.id.branccodeAndCreator);
           // holder.linearLayout_list = (LinearLayout) v.findViewById(R.id.linearLayout_list);
            v.setTag(holder);
        } else {
            holder = (FeeViewHolder) v.getTag();
        }

        DataEMI manager = p_emiData.get(position);
        holder.tvlabel_emiamount.setText(manager.getProcessingFeeToPay()+"");


        return v;
    }


    public class FeeViewHolder {
        public TextView tvlabel_emiamount;
        public TextView tvManagerCode;

    }

}

