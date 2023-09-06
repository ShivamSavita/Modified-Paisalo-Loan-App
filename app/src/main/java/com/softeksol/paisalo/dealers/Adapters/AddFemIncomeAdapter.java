package com.softeksol.paisalo.dealers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.softeksol.paisalo.jlgsourcing.ABFActivities.FiFormSecond;
import com.softeksol.paisalo.jlgsourcing.R;

public class AddFemIncomeAdapter extends RecyclerView.Adapter<AddFemIncomeAdapter.FemIncomeViewHolder> {
    Context  context;
    int size;
    public AddFemIncomeAdapter(Context  context, int size) {
        this.context=context;
        this.size=size;
    }

    @NonNull
    @Override
    public FemIncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.member_income_card_item,parent,false);
        return new FemIncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FemIncomeViewHolder holder, int position) {
        holder.FamMemNumberTxt.setText("Family member "+String.valueOf(position+1));
        holder.femMemRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size--;
                notifyDataSetChanged();
                Toast.makeText(context, "One Member removed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class FemIncomeViewHolder extends RecyclerView.ViewHolder {

        TextView FamMemNumberTxt;
        ImageButton femMemRemoveBtn;
        public FemIncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            FamMemNumberTxt=itemView.findViewById(R.id.FamMemNumberTxt);
            femMemRemoveBtn=itemView.findViewById(R.id.femMemRemoveBtn);
        }
    }
}
