package com.softeksol.paisalo.dealers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softeksol.paisalo.dealers.DealersListPage;
import com.softeksol.paisalo.dealers.Models.AllDealersModel;
import com.softeksol.paisalo.jlgsourcing.R;

import java.util.ArrayList;
import java.util.List;

public class DealerNestedAdapeter extends RecyclerView.Adapter<DealerNestedAdapeter.DealerNestedViewholder> {
    Context context;
    List<AllDealersModel> allDealersModels;

    List<String> createrHeaderList;
    public DealerNestedAdapeter(Context context, List<AllDealersModel> allDealersModels, List<String> createrHeaderList) {
        this.context=context;
        this.allDealersModels=allDealersModels;
        this.createrHeaderList=createrHeaderList;
    }

        @NonNull
        @Override
        public DealerNestedViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dealer_nested_itemcard,parent,false);
            return new DealerNestedViewholder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull DealerNestedViewholder holder, int position) {
            List<AllDealersModel> newAllDealersModels=new ArrayList<>();


            LinearLayoutManager mLayoutManager;
            holder.text_bname.setText(createrHeaderList.get(position));
            for (int i = 0; i < allDealersModels.size(); i++) {
                if (allDealersModels.get(i).getCreator().equals(createrHeaderList.get(position))){
                    newAllDealersModels.add(allDealersModels.get(i));
                }
            }
            mLayoutManager = new LinearLayoutManager(context);
            holder.Recview.setLayoutManager(mLayoutManager);
            holder.Recview.setItemAnimator(new DefaultItemAnimator());
            holder.adapeter=new DealerListAdapter(context,newAllDealersModels);
            holder.Recview.setAdapter(holder.adapeter);
        }

        @Override
        public int getItemCount() {
            return createrHeaderList.size();
        }

        public class DealerNestedViewholder extends RecyclerView.ViewHolder {
            TextView text_bname;
            RecyclerView Recview;
            DealerListAdapter adapeter;
            public DealerNestedViewholder(@NonNull View itemView) {
                super(itemView);
                text_bname=itemView.findViewById(R.id.headedatetxt);
                Recview=itemView.findViewById(R.id.eventHistoryNestedRecview);

            }
        }
    }



