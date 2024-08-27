package com.digital.paisalo.jlgsourcing.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digital.paisalo.jlgsourcing.R;
import com.digital.paisalo.jlgsourcing.entities.dto.OperationItem;

import java.util.List;


public class AdapterOperation extends ArrayAdapter<OperationItem> {
    private final Context context;
    private String defaultState = "";
    private List<OperationItem> StateArrayList;

    public AdapterOperation(Context context, List<OperationItem> values) {
        super(context, R.layout.operation_item_card, values);
        this.context = context;
        this.StateArrayList = values;
    }

    @SuppressLint("ResourceAsColor")
    public View getCustomView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        OperationViewHolder holder;

        if (view == null) {
            holder = new OperationViewHolder();
            view = inflater.inflate(R.layout.operation_item_card, null);
            holder.textView = view.findViewById(R.id.module_name);
            holder.module_desc = view.findViewById(R.id.module_desc);
            holder.imageView = view.findViewById(R.id.imageModeules);
            holder.kyc_card = view.findViewById(R.id.kyc_card);
            view.setTag(holder);
        } else {
            holder = (OperationViewHolder) view.getTag();
        }

        OperationItem operationItem = getItem(position);
        if (operationItem != null) {
            if (holder.textView != null) {
                // set the documentStore name on the TextView
                holder.textView.setText(operationItem.getOprationName());
                holder.operationItem = operationItem;
                if (operationItem.getOprationName().equals("KYC")){
                    holder.imageView.setImageResource(R.drawable.kycicon);
                    holder.kyc_card.setBackgroundColor(context.getResources().getColor(R.color.kyc_color));
                    holder.module_desc.setText("Check mandatory process of identifying and verifying.");
                }else if (operationItem.getOprationName().equals("Application Form")){
                    holder.imageView.setImageResource(R.drawable.loanapp_icon);
                    holder.kyc_card.setBackgroundColor(context.getResources().getColor(R.color.loan_color));
                    holder.module_desc.setText("Fill all information about Borrower.");
                }else if (operationItem.getOprationName().equals("Collection")){
                    holder.imageView.setImageResource(R.drawable.collection_icon);
                    holder.kyc_card.setBackgroundColor(context.getResources().getColor(R.color.collection_color));
                    holder.module_desc.setText("For EMI collection department.");
                }else if (operationItem.getOprationName().equals("E-Sign")){
                    holder.imageView.setImageResource(R.drawable.esign_ic);
                    holder.kyc_card.setBackgroundColor(context.getResources().getColor(R.color.esign_color));
                    holder.module_desc.setText("Digital verification by Finger Print with Aadhaar.");
                }else if (operationItem.getOprationName().equals("ABF Module")){
                    holder.imageView.setImageResource(R.drawable.dealer_ic);
                    holder.kyc_card.setBackgroundColor(context.getResources().getColor(R.color.vh_color));
                    holder.module_desc.setText("Dealers Pages like On Boarding Document Upload etc.");
                }else if (operationItem.getOprationName().equals("OEM OnBoard")){
                    holder.imageView.setImageResource(R.drawable.oem_ic);
                    holder.kyc_card.setBackgroundColor(context.getResources().getColor(R.color.esign_color));
                    holder.module_desc.setText("Working with OEM data and Their Products");
                }else if (operationItem.getOprationName().equals("Dealer OnBoard")){
                    holder.imageView.setImageResource(R.drawable.dealeronboard_ic);
                    holder.kyc_card.setBackgroundColor(context.getResources().getColor(R.color.vh_color));
                    holder.module_desc.setText("Generating new Dealers and assigning branches to them");
                }else if (operationItem.getOprationName().equals("Dealer Checklist")){
                    holder.imageView.setImageResource(R.drawable.dealercheclist_ic);
                    holder.kyc_card.setBackgroundColor(context.getResources().getColor(R.color.kyc_color));
                    holder.module_desc.setText("Uploading Documents and files in this module");
                }
            }
        }

        return view;
    }

    // It gets a View that displays the data at the specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    class OperationViewHolder {
        private TextView textView,module_desc;
        private ImageView imageView;
        private LinearLayout kyc_card;
        private OperationItem operationItem;
    }
}