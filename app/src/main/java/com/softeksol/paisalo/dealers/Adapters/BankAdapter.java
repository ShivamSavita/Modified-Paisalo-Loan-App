package com.softeksol.paisalo.dealers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softeksol.paisalo.dealers.Models.OEMBankResponse;
import com.softeksol.paisalo.jlgsourcing.R;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.BankViewHoloder> {
    Context context;
    OEMBankResponse[] nameList;
    public BankAdapter(Context context, OEMBankResponse[] nameList) {
        this.context=context;
        this.nameList=nameList;
    }

    @NonNull
    @Override
    public BankViewHoloder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_oem_card,parent,false);
    return new BankViewHoloder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankViewHoloder holder, int position) {
        holder.layoutOEMBank.setText(nameList[position].getBankName());
        holder.layoutOEMAccountNumber.setText(nameList[position].getAccountNo());
        holder.layoutOEMifsc.setText(nameList[position].getIfsc());
        holder.layoutOEmBankAdd.setText("Address: "+nameList[position].getBranch());
    }

    @Override
    public int getItemCount() {
        return nameList.length;
    }

    public class BankViewHoloder extends RecyclerView.ViewHolder {
            TextView layoutOEmBankAdd,layoutOEMifsc,layoutOEMAccountNumber,layoutOEMBank;
        public BankViewHoloder(@NonNull View itemView) {
            super(itemView);

            layoutOEMBank=itemView.findViewById(R.id.layoutOEMBank);
            layoutOEMAccountNumber=itemView.findViewById(R.id.layoutOEMAccountNumber);
            layoutOEMifsc=itemView.findViewById(R.id.layoutOEMifsc);
            layoutOEmBankAdd=itemView.findViewById(R.id.layoutOEmBankAdd);
        }
    }
}
