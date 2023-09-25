package com.softeksol.paisalo.dealers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.entities.HomeVisitFiList;
import com.softeksol.paisalo.jlgsourcing.homevisit.FirstPage;
import com.softeksol.paisalo.jlgsourcing.homevisit.HomeVisitManagerList;

import java.util.ArrayList;
import java.util.List;

public class HomeVisitMnagerAdapter extends RecyclerView.Adapter<HomeVisitMnagerAdapter.HomeVisitViewHolder> {

    Context context;
    private List<HomeVisitFiList> orig;
    List<HomeVisitFiList> homeVisitManagerLists;

    public HomeVisitMnagerAdapter(Context context, List<HomeVisitFiList> homeVisitManagerLists) {
        this.context = context;
        this.homeVisitManagerLists = homeVisitManagerLists;
    }

    @NonNull
    @Override
    public HomeVisitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_loan_app_info, parent, false);
        return new HomeVisitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeVisitViewHolder holder, int position) {
        holder.itemLayoutCustomerAddress.setText(homeVisitManagerLists.get(position).getAddr());
        String name = (homeVisitManagerLists.get(position).getFname() == null ? "" : homeVisitManagerLists.get(position).getFname()) + " " + (homeVisitManagerLists.get(position).getMname() == null ? "" : homeVisitManagerLists.get(position).getMname()) + " " + (homeVisitManagerLists.get(position).getLname() == null ? "" : homeVisitManagerLists.get(position).getLname());
        String FatherName = (homeVisitManagerLists.get(position).getF_fname() == null ? "" : homeVisitManagerLists.get(position).getF_fname()) + " " + (homeVisitManagerLists.get(position).getF_mname() == null ? "" : homeVisitManagerLists.get(position).getF_mname()) + " " + (homeVisitManagerLists.get(position).getF_lname() == null ? "" : homeVisitManagerLists.get(position).getF_lname());
        holder.itemLayoutCustomerName.setText(name);
        holder.itemLayoutCustomerFather.setText(FatherName);
        holder.itemLayoutCustomerFiId.setText(String.valueOf(homeVisitManagerLists.get(position).getCode()));
        holder.itemLayoutCustomerMobile.setText(homeVisitManagerLists.get(position).getP_ph3());
        holder.itemLayoutCustomerCreator.setText(homeVisitManagerLists.get(position).getCreator());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FirstPage.class);
                i.putExtra("ficode", String.valueOf(homeVisitManagerLists.get(position).getCode()));
                i.putExtra("creator", homeVisitManagerLists.get(position).getCreator());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeVisitManagerLists.size();
    }


    public class HomeVisitViewHolder extends RecyclerView.ViewHolder {

        TextView itemLayoutCustomerName, itemLayoutCustomerFather, itemLayoutCustomerFiId, itemLayoutCustomerMobile, itemLayoutCustomerCreator, itemLayoutCustomerAddress;

        public HomeVisitViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayoutCustomerAddress = itemView.findViewById(R.id.itemLayoutCustomerAddress);
            itemLayoutCustomerName = itemView.findViewById(R.id.itemLayoutCustomerName);
            itemLayoutCustomerFather = itemView.findViewById(R.id.itemLayoutCustomerFather);
            itemLayoutCustomerFiId = itemView.findViewById(R.id.itemLayoutCustomerFiId);
            itemLayoutCustomerMobile = itemView.findViewById(R.id.itemLayoutCustomerMobile);
            itemLayoutCustomerCreator = itemView.findViewById(R.id.itemLayoutCustomerCreator);

        }
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<HomeVisitFiList> results = new ArrayList<HomeVisitFiList>();
                if (orig == null)
                    orig = homeVisitManagerLists;
                if (constraint != null) {
                    if (orig != null & orig.size() > 0) {
                        for (final HomeVisitFiList g : orig) {
                            if (String.valueOf(g.getCode()).toLowerCase().contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                homeVisitManagerLists = (ArrayList<HomeVisitFiList>) results.values;
                notifyDataSetChanged();

            }
        };
    }
}
