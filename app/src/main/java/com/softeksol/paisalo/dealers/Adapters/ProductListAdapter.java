package com.softeksol.paisalo.dealers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softeksol.paisalo.dealers.Models.ProductDataModel;
import com.softeksol.paisalo.jlgsourcing.R;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {
    Context context;
     ProductDataModel[] oemProductModelList;
    public ProductListAdapter(Context context, ProductDataModel[] oemProductModelList) {
        this.context=context;
        this.oemProductModelList=oemProductModelList;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_list_card,parent,false);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return oemProductModelList.length;
    }

    public class ProductListViewHolder extends RecyclerView.ViewHolder {

        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
