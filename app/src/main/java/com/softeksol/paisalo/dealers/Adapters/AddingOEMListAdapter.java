package com.softeksol.paisalo.dealers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softeksol.paisalo.dealers.Models.AddOEMModel;
import com.softeksol.paisalo.dealers.OEM_Onboarding;
import com.softeksol.paisalo.jlgsourcing.R;

import java.util.ArrayList;

public class AddingOEMListAdapter extends RecyclerView.Adapter<AddingOEMListAdapter.AddingOemViewHolder> {
    Context context;
    ArrayList<AddOEMModel> addOEMModelArrayList;
    public AddingOEMListAdapter(Context context, ArrayList<AddOEMModel> addOEMModelArrayList) {
        this.context=context;
        this.addOEMModelArrayList=addOEMModelArrayList;
    }

    @NonNull
    @Override
    public AddingOemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.added_oem_item_card,parent,false);
        return new AddingOemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddingOemViewHolder holder, int position) {
        holder.txtCityOEM.setText(addOEMModelArrayList.get(position).getP_City());
        holder.txtDistrictOEM.setText(addOEMModelArrayList.get(position).getP_District());
        holder.txtStateOEM.setText(addOEMModelArrayList.get(position).getP_State());
        holder.txtPinOEM.setText(addOEMModelArrayList.get(position).getP_Pincode());
        holder.txtAddOEM.setText(addOEMModelArrayList.get(position).getP_Address());
        holder.txtPANOEM.setText(addOEMModelArrayList.get(position).getPanno());
        holder.txtAdharOEM.setText(addOEMModelArrayList.get(position).getAadharno());
        holder.txtEmailOEM.setText(addOEMModelArrayList.get(position).getEmail());
        holder.txtPhoneOEM.setText(addOEMModelArrayList.get(position).getPhone());
        holder.txtNameOEM.setText(addOEMModelArrayList.get(position).getName());
        holder.btnDeletOEM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOEMModelArrayList.remove(position);
                notifyDataSetChanged();


            }
        });
    }

    @Override
    public int getItemCount() {
        return addOEMModelArrayList.size();
    }

    public class AddingOemViewHolder extends RecyclerView.ViewHolder{
        TextView txtCityOEM,txtDistrictOEM,txtStateOEM, txtPinOEM, txtAddOEM, txtPANOEM, txtAdharOEM, txtEmailOEM,txtPhoneOEM, txtNameOEM;
        ImageButton btnDeletOEM;
        public AddingOemViewHolder(@NonNull View vi) {
            super(vi);
                    txtCityOEM=vi.findViewById(R.id.txtCityOEM);
                    txtDistrictOEM=vi.findViewById(R.id.txtDistrictOEM);
                    txtStateOEM=vi.findViewById(R.id.txtStateOEM);
                    txtPinOEM=vi.findViewById(R.id.txtPinOEM);
                    txtAddOEM=vi.findViewById(R.id.txtAddOEM);
                    txtPANOEM=vi.findViewById(R.id.txtPANOEM);
                    txtAdharOEM=vi.findViewById(R.id.txtAdharOEM);
                    txtEmailOEM=vi.findViewById(R.id.txtEmailOEM);
                    txtPhoneOEM=vi.findViewById(R.id.txtPhoneOEM);
                    txtNameOEM=vi.findViewById(R.id.txtNameOEM);
            btnDeletOEM=vi.findViewById(R.id.btnDeletOEM);
        }
    }
}
