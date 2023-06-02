package com.softeksol.paisalo.dealers.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softeksol.paisalo.dealers.DealerOnBoard;
import com.softeksol.paisalo.dealers.Dealer_Branch_Open;
import com.softeksol.paisalo.dealers.Dealer_Dashboard;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.dealers.Models.OEMDataModel;
import com.softeksol.paisalo.dealers.Product_Bank_addPage_OEM;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.IglPreferences;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;

import java.util.ArrayList;

import retrofit2.Callback;

public class OEMListAdapter extends RecyclerView.Adapter<OEMListAdapter.OEMListViewHolder> {
    Context context;
    OEMDataModel[] oemDataModels;
    public OEMListAdapter(Context context, OEMDataModel[] oemDataModels) {
        this.context=context;
        this.oemDataModels=oemDataModels;
    }

    @NonNull
    @Override
    public OEMListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_oem_card,parent,false);
        return new OEMListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OEMListViewHolder holder, int position) {
            holder.layoutCustGurName.setText(oemDataModels[position].getName());
            holder.layoutOEmAdd.setText("Address: "+oemDataModels[position].getOAddress());
            holder.layoutOEMMobile.setText(oemDataModels[position].getPhone());
            holder.layoutOEMFirm.setText(oemDataModels[position].getFirmName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context, Product_Bank_addPage_OEM.class);
                    i.putExtra("OEMid",oemDataModels[position].getId());
                    context.startActivity(i);
                }
            });
    }

    @Override
    public int getItemCount() {
        return oemDataModels.length;
    }

    public class OEMListViewHolder extends RecyclerView.ViewHolder {
        TextView layoutCustGurName,layoutOEmAdd,layoutOEMMobile,layoutOEMFirm;
        public OEMListViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutCustGurName=itemView.findViewById(R.id.layoutCustGurName);
            layoutOEmAdd=itemView.findViewById(R.id.layoutOEmAdd);
            layoutOEMMobile=itemView.findViewById(R.id.layoutOEMMobile);
            layoutOEMFirm=itemView.findViewById(R.id.layoutOEMFirm);
        }
    }
}
