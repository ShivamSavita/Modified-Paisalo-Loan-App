package com.softeksol.paisalo.dealers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kyanogen.signatureview.SignatureView;
import com.softeksol.paisalo.jlgsourcing.R;

public class OEM_Onboarding extends AppCompatActivity {
Button clearSignatureBtn;
SignatureView signatureView;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    LinearLayout layout_basicDetails,layout_BusinessDetails,layout_BankDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oem_onboarding);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("OEM On-Board");
        layout_basicDetails=findViewById(R.id.layout_basic_details);
        clearSignatureBtn=findViewById(R.id.clearSignatureBtn);
        signatureView=findViewById(R.id.signatureView);
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
        clearSignatureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureView.clearCanvas();
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