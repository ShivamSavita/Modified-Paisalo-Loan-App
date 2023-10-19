package com.softeksol.paisalo.jlgsourcing.adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.activities.ActivityLogin;
import com.softeksol.paisalo.jlgsourcing.activities.onListCReatorInteraction;
import com.softeksol.paisalo.jlgsourcing.entities.CreatorModel;
import com.softeksol.paisalo.jlgsourcing.fragments.FragmentLoanAppList;

import java.util.ArrayList;
import java.util.List;

public class CreatorListAdapter extends RecyclerView.Adapter<CreatorListAdapter.CreatorListViewHolder> {
    Context context;
    List<CreatorModel> list;
    private onListCReatorInteraction mListener;
    Dialog dialog;
    public CreatorListAdapter(Context context, List<CreatorModel> list, Dialog dialog,onListCReatorInteraction listCReatorInteraction) {
        mListener=listCReatorInteraction;
        this.context=context;
        this.dialog=dialog;
        this.list=list;
    }

    @NonNull
    @Override
    public CreatorListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_item_single,parent,false);
        return new CreatorListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreatorListViewHolder holder, int position) {
        holder.tvListItemSingle.setText(list.get(position).getCreator());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListCReatorInteraction(list.get(position).getCreator());
                }
                dialog.dismiss();
            }
        });


    }




    public void filterList(List<CreatorModel> filteredList) {
        list=filteredList;
        Log.d("TAG", "filterList: "+list.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CreatorListViewHolder extends RecyclerView.ViewHolder {
        TextView tvListItemSingle;
        public CreatorListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvListItemSingle=itemView.findViewById(R.id.tvListItemSingle);
        }
    }
}
