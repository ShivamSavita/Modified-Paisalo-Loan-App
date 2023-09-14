package com.softeksol.paisalo.dealers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.softeksol.paisalo.jlgsourcing.ABFActivities.FiFormSecond;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerFamilyMember;

import java.util.ArrayList;

public class AddFemIncomeAdapter extends RecyclerView.Adapter<AddFemIncomeAdapter.FemIncomeViewHolder> {
    Context  context;
    ArrayList<BorrowerFamilyMember> list;
    public AddFemIncomeAdapter(Context  context, ArrayList<BorrowerFamilyMember> list) {
        this.context=context;
        this.list=list;
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



        holder.tietMemberName.setText(list.get(position).getMemName());
         holder.tietBusinessFemMe.setText(list.get(position).getBusiness());
        holder.famMemIncome.setText(String.valueOf(list.get(position).getIncome()));
         holder.acspRelationship.setText(list.get(position).getRelationWBorrower());
        holder.acspEduDetail.setText(list.get(position).getEducatioin());
         holder.acspBusinessType.setText(list.get(position).getBusinessType());
        holder.famMemIncomeType.setText(list.get(position).getIncomeType());
        holder.tietMemberGender.setText(list.get(position).getGender());
        holder.femMemRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             list.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "One Member removed!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FemIncomeViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText tietMemberName,tietBusinessFemMe,tietMemberGender;
        EditText acspRelationship,acspEduDetail,acspBusinessType,famMemIncomeType,famMemIncome;
        TextView FamMemNumberTxt;
        ImageButton femMemRemoveBtn;
        public FemIncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            FamMemNumberTxt=itemView.findViewById(R.id.FamMemNumberTxt);
            femMemRemoveBtn=itemView.findViewById(R.id.femMemRemoveBtn);
            tietMemberName=itemView.findViewById(R.id.tietMemberName);
            tietBusinessFemMe=itemView.findViewById(R.id.tietBusinessFemMe);
            famMemIncome=itemView.findViewById(R.id.famMemIncome);
            acspRelationship=itemView.findViewById(R.id.acspRelationship);
            acspEduDetail=itemView.findViewById(R.id.acspEduDetail);
            acspBusinessType=itemView.findViewById(R.id.acspBusinessType);
            famMemIncomeType=itemView.findViewById(R.id.famMemIncomeType);
            tietMemberGender=itemView.findViewById(R.id.tietMemberGender);
        }
    }
}
