package com.softeksol.paisalo.ESign.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.softeksol.paisalo.ESign.viewHolder.eSignerViewHolder;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.entities.ESigner;

import java.util.List;

public class AdapterListESigner extends ArrayAdapter<ESigner> {
    private final Context context;

    public AdapterListESigner(Context context, List<ESigner> values) {
        super(context, R.layout.layout_esigner, values);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // create a ViewHolder reference
        eSignerViewHolder holder;

        // check to see if the reused view is null or not, if is not null then
        // reuse it
        ESigner eSigner = getItem(position);
        if (view == null) {
            holder = new eSignerViewHolder();
            view = inflater.inflate(R.layout.layout_esigner, null);
            holder.EsignerName = (TextView) view.findViewById(R.id.layoutCustGurName);
            holder.Mobile = (TextView) view.findViewById(R.id.layoutCustGurMobile);
            holder.EsignerGuardian = (TextView) view.findViewById(R.id.layoutCustGurFather);
            holder.Address = (TextView) view.findViewById(R.id.layoutCustGurAddress);
            //holder.AadharId = (TextView) view.findViewById(R.id.layoutCustGurAadharId);
            holder.ESignerAs = (TextView) view.findViewById(R.id.layoutCustGurSignerAs);
            // the setTag is used to store the data within this view
            view.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (eSignerViewHolder) view.getTag();
        }

        if (eSigner != null) {
            if (eSigner.Name != null) {
                holder.EsignerName.setText(eSigner.Name);
                //holder.AadharId.setText(String.valueOf(eSigner.AadharNo));
                holder.EsignerGuardian.setText(eSigner.FatherName);
                holder.Address.setText(eSigner.Address);
                holder.Mobile.setText(eSigner.MobileNo);
                holder.ESignerAs.setText(eSigner.ESignerType);
            }
        }

        if (eSigner.ESignSucceed.equals("Y")) {
            view.setBackgroundColor(Color.GREEN);
        } else {
            if (eSigner.GrNo == 0) {
                view.setBackgroundColor(Color.YELLOW);
            } else {
                view.setBackgroundColor(Color.CYAN);
            }
        }
        return view;
    }

}
