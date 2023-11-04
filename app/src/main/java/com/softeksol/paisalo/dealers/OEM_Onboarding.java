package com.softeksol.paisalo.dealers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.kyanogen.signatureview.SignatureView;
import com.softeksol.paisalo.jlgsourcing.R;

public class OEM_Onboarding extends AppCompatActivity {
Button clearSignatureBtn,btnSaveAccount,btnSaveBusiness,btnSaveBasic;
SignatureView signatureView;

EditText Val_firmpincode,Val_pincode;
TextInputEditText Val_pancard,bankUserName,Val_address,Val_email,tietOEMName,Val_mobile,brand,bankName,vehicleType,manufacturer,Account_Number,Val_firmaddress,ifscCode,branchName, firmName;
Spinner firm_accountType;
AppCompatSpinner firm_City,firm_District,firm_State,acspAadharCity,acspAadharDistrict,stateSpinnerOEM;
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
        //-------------binding views with id-----------------
        layout_basicDetails=findViewById(R.id.layout_basic_details);
        //-------------binding Bank Details views with id-----------------

        clearSignatureBtn=findViewById(R.id.clearSignatureBtn);
        signatureView=findViewById(R.id.signatureView);
        bankUserName=findViewById(R.id.bankUserName);
        bankName=findViewById(R.id.bankName);
        Account_Number=findViewById(R.id.Account_Number);
        ifscCode=findViewById(R.id.ifscCode);
        branchName=findViewById(R.id.branchName);
        firm_accountType=findViewById(R.id.firm_accountType);
        btnSaveAccount=findViewById(R.id.btnSaveAccount);
        //-------------binding Business Details views with id-----------------

        firmName=findViewById(R.id.firmName);
        Val_firmaddress=findViewById(R.id.Val_firmaddress);
        firm_City=findViewById(R.id.firm_City);
        firm_District=findViewById(R.id.firm_District);
        firm_State=findViewById(R.id.firm_State);
        Val_firmpincode=findViewById(R.id.Val_firmpincode);
        vehicleType=findViewById(R.id.vehicleType);
        manufacturer=findViewById(R.id.manufacturer);
        brand=findViewById(R.id.brand);
        btnSaveBusiness=findViewById(R.id.btnSaveBusiness);

        //-------------binding OEM Basic Details views with id-----------------

        tietOEMName=findViewById(R.id.tietOEMName);
        Val_mobile=findViewById(R.id.Val_mobile);
        Val_email=findViewById(R.id.Val_email);
        Val_address=findViewById(R.id.Val_address);
        Val_pancard=findViewById(R.id.Val_pancard);
        acspAadharCity=findViewById(R.id.acspAadharCity);
        acspAadharDistrict=findViewById(R.id.acspAadharDistrict);
        stateSpinnerOEM=findViewById(R.id.stateSpinnerOEM);
        Val_pincode=findViewById(R.id.Val_pincode);
        btnSaveBasic=findViewById(R.id.btnSaveBasic);

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