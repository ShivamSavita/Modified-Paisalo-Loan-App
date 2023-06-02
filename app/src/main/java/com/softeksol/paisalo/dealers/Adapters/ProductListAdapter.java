package com.softeksol.paisalo.dealers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_card,parent,false);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        holder.productBrandType.setText(oemProductModelList[position].getBrand().trim()+"/"+oemProductModelList[position].getVtype());
        holder.productBSPMSP.setText(oemProductModelList[position].getBsp()+"/"+oemProductModelList[position].getMsp());
        holder.productFuelType.setText(oemProductModelList[position].getFuelType());
        holder.productModel.setText(oemProductModelList[position].getModel());
    }

    @Override
    public int getItemCount() {
        return oemProductModelList.length;
    }

    public class ProductListViewHolder extends RecyclerView.ViewHolder {
        TextView productBrandType,productModel,productBSPMSP,productFuelType;
        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);
            productBrandType=itemView.findViewById(R.id.productBrandType);
            productModel=itemView.findViewById(R.id.productModel);
            productBSPMSP=itemView.findViewById(R.id.productBSPMSP);
            productFuelType=itemView.findViewById(R.id.productFuelType);
        }
    }
}
