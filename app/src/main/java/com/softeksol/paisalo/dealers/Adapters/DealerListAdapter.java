package com.softeksol.paisalo.dealers.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softeksol.paisalo.dealers.AddBankForDealer;
import com.softeksol.paisalo.dealers.DealerOnBoard;
import com.softeksol.paisalo.dealers.Dealer_Branch_Open;
import com.softeksol.paisalo.dealers.Dealer_Dashboard;
import com.softeksol.paisalo.dealers.DealersListPage;
import com.softeksol.paisalo.dealers.Models.AllDealersModel;
import com.softeksol.paisalo.dealers.UploadDealerDocs;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.IglPreferences;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class DealerListAdapter extends RecyclerView.Adapter<DealerListAdapter.DealerListViewHolder> {
    Context context;
    List<AllDealersModel> allDealersModels;
    public DealerListAdapter(Context context, List<AllDealersModel> allDealersModels) {
        this.context=context;
        this.allDealersModels=allDealersModels;
    }

    @NonNull
    @Override
    public DealerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_oem_card,parent,false);
        return new DealerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealerListViewHolder holder, int position) {
        holder.layoutOEmAdd.setText(allDealersModels.get(position).getOAddress());
        holder.layoutOEMMobile.setText(allDealersModels.get(position).getPhone());
        holder.layoutOEMFirm.setText(allDealersModels.get(position).getFirmName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        ArrayList<String> menuOptions = new ArrayList<>();
                            menuOptions.add("Add Bank account");
                            menuOptions.add("Upload Documents");
                        if(menuOptions.size()>0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            String[] mOptions = new String[menuOptions.size()];
                            mOptions = menuOptions.toArray(mOptions);
                            builder.setItems(mOptions, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which==0){
                                        Intent intent2 = new Intent(context, AddBankForDealer.class);
                                        intent2.putExtra("DealerId",allDealersModels.get(position).getId());
                                        context.startActivity(intent2);
                                    }else{
                                        Intent intent2 = new Intent(context, UploadDealerDocs.class);
                                        intent2.putExtra("dealerId",allDealersModels.get(position).getId());
                                        context.startActivity(intent2);

                                    }


                                }
                            });
                            builder.create().show();
                        }else{
                            Utils.alert(context,"eSign Disabled");
                        }
            }
        });





    }

    @Override
    public int getItemCount() {
        return allDealersModels.size();
    }

    public class DealerListViewHolder extends RecyclerView.ViewHolder {
        TextView layoutCustGurName,layoutOEmAdd,layoutOEMMobile,layoutOEMFirm;
        public DealerListViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutCustGurName=itemView.findViewById(R.id.layoutCustGurName);
            layoutOEmAdd=itemView.findViewById(R.id.layoutOEmAdd);
            layoutOEMMobile=itemView.findViewById(R.id.layoutOEMMobile);
            layoutOEMFirm=itemView.findViewById(R.id.layoutOEMFirm);
        }
    }
}
