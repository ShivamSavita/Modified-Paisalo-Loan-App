package com.softeksol.paisalo.dealers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.softeksol.paisalo.jlgsourcing.R;

public class DealerOnBoard extends AppCompatActivity {


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    LinearLayout layout_basicDetails,layout_BusinessDetails,layout_BankDetails;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delear_on_board);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Dealer On-Board");
        layout_basicDetails=findViewById(R.id.layout_basic_details);
        layout_BusinessDetails=findViewById(R.id.layout_BusinessDetails);
        layout_BankDetails=findViewById(R.id.layout_BankDetails);
        layout_BusinessDetails.setVisibility(View.GONE);
        layout_BankDetails.setVisibility(View.GONE);
        Button btnSaveBasic=findViewById(R.id.btnSaveBasic);
        Button btnSaveBusiness=findViewById(R.id.btnSaveBusiness);
        btnSaveBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_basicDetails.setVisibility(View.GONE);
                layout_BusinessDetails.setVisibility(View.VISIBLE);
            }
        });

        btnSaveBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_basicDetails.setVisibility(View.GONE);
                layout_BusinessDetails.setVisibility(View.GONE);
                layout_BankDetails.setVisibility(View.VISIBLE);
            }
        });
    }
}