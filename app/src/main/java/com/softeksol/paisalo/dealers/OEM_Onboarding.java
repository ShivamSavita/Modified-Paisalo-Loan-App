package com.softeksol.paisalo.dealers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kyanogen.signatureview.SignatureView;

import com.softeksol.paisalo.dealers.Adapters.AddingOEMListAdapter;
import com.softeksol.paisalo.dealers.Adapters.AlgorithmAdapter;
import com.softeksol.paisalo.dealers.Models.AddOEMModel;
import com.softeksol.paisalo.dealers.Models.BrandData;
import com.softeksol.paisalo.dealers.Models.BrandDataList;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.dealers.Models.PinCodeResponse;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerFamilyMember;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import org.w3c.dom.NameList;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OEM_Onboarding extends AppCompatActivity {
Button clearSignatureBtn,btnSaveAccount,btnSaveBasic,btnSaveBusiness;
SignatureView signatureView;

EditText Val_firmpincode,Val_pincode,acspAadharCity,acspAadharDistrict,stateSpinnerOEM;
TextInputEditText Val_pancard,Val_GST,Val_address,Val_email,tietOEMName,Val_mobile,vehicleType,manufacturer,Account_Number,Val_firmaddress,ifscCode,branchName, firmName;
Spinner firm_accountType,spinnerBrand;
    ApiInterface apiInterface;
EditText firm_City,firm_District,firm_State,firm_SIN;
    ViewGroup insertPoint;
String firmType="";
int selectedBrandId=0;
CheckBox proprietorCheckBox,partnershipCheckBox,privateCheckBox;
RecyclerView RecViewOemPartners;
    JsonObject oemDetailsJson;
    AddingOEMListAdapter adapter;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.menu_item:   //this item has your app icon
                Toast.makeText(this,"Tapped on icon",Toast.LENGTH_SHORT).show();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.oem_save, menu);
        return super.onCreateOptionsMenu(menu);
    }
    LinearLayout layout_basicDetails,layout_BusinessDetails,layout_BankDetails;
    ArrayList<AddOEMModel> addOEMModelArrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oem_onboarding);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("OEM On-Board");
        //-------------binding views with id-----------------
        //layout_basicDetails=findViewById(R.id.layout_basic_details);
        //-------------binding Bank Details views with id-----------------
         apiInterface=new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);

        //clearSignatureBtn=findViewById(R.id.clearSignatureBtn);
        //signatureView=findViewById(R.id.signatureView);
        //bankUserName=findViewById(R.id.bankUserName);
        //bankName=findViewById(R.id.bankName);
        //Account_Number=findViewById(R.id.Account_Number);
        //ifscCode=findViewById(R.id.ifscCode);
        //branchName=findViewById(R.id.branchName);
        //firm_accountType=findViewById(R.id.firm_accountType);
        //btnSaveAccount=findViewById(R.id.btnSaveAccount);
        //-------------binding Business Details views with id-----------------

        firmName=findViewById(R.id.firmName);
        Val_firmaddress=findViewById(R.id.Val_firmaddress);
        firm_City=findViewById(R.id.firm_City);
        firm_District=findViewById(R.id.firm_District);
        firm_State=findViewById(R.id.firm_State);
        Val_firmpincode=findViewById(R.id.Val_firmpincode);
        spinnerBrand=findViewById(R.id.spinnerBrand);
        privateCheckBox=findViewById(R.id.privateCheckBox);
        partnershipCheckBox=findViewById(R.id.partnershipCheckBox);
        proprietorCheckBox=findViewById(R.id.proprietorCheckBox);
        Val_pancard=findViewById(R.id.Val_pancard);
        Val_GST=findViewById(R.id.Val_GST);
        firm_SIN=findViewById(R.id.firm_SIN);
        RecViewOemPartners=findViewById(R.id.RecViewOemPartners);
        RecViewOemPartners.setLayoutManager(new LinearLayoutManager(this));
        adapter=new AddingOEMListAdapter(this,addOEMModelArrayList);
        RecViewOemPartners.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //-------------binding OEM Basic Details views with id-----------------

//        tietOEMName=findViewById(R.id.tietOEMName);
//        Val_mobile=findViewById(R.id.Val_mobile);
//        Val_email=findViewById(R.id.Val_email);
//        Val_address=findViewById(R.id.Val_address);
//        Val_pancard=findViewById(R.id.Val_pancard);
//        acspAadharCity=findViewById(R.id.acspAadharCity);
//        acspAadharDistrict=findViewById(R.id.acspAadharDistrict);
//        stateSpinnerOEM=findViewById(R.id.stateSpinnerOEM);
//        Val_pincode=findViewById(R.id.Val_pincode);
//        btnSaveBasic=findViewById(R.id.btnSaveBasic);

        layout_BusinessDetails=findViewById(R.id.layout_BusinessDetails);
//        layout_BankDetails=findViewById(R.id.layout_BankDetails);
        layout_BusinessDetails.setVisibility(View.VISIBLE);
//        layout_basicDetails.setVisibility(View.GONE);

       btnSaveBusiness=findViewById(R.id.btnSaveBusinessOEM);

//        btnSaveBasic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (validateOEMBasicDetails()){
//
//
////                    layout_basicDetails.setVisibility(View.GONE);
//                layout_BusinessDetails.setVisibility(View.VISIBLE);
//                }
//            }
//        });


        btnSaveBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (validateOEMBussinessDetails())
                         addOEMPartner();
//                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                ViewGroup parent = (ViewGroup)findViewById(R.id.dynamicOEMDataView);
//                inflater.inflate(R.layout.layout_dialog_add_oem, parent);
//                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View view = vi.inflate(R.layout.spinner_card_orange, null);

// fill in any details dynamically here

// insert into main view

//                if (validateOEMBussinessDetails()){
//                    Log.d("TAG", "onClick: "+saveBasicDetailsInJson());
//
//                    Call<ResponseBody> responseBodyCall= apiInterface.saveOEmDetails("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJJZCI6IjEiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiYWRtaW5AcGFpc2Fsby5pbiIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2VtYWlsYWRkcmVzcyI6ImFkbWluQHBhaXNhbG8uaW4iLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjEiLCJSb2xlIjpbIkFETUlOIiwiQURNSU4iXSwiQnJhbmNoQ29kZSI6IjAwMSIsIkNyZWF0b3IiOiJBR1JBIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9leHBpcmF0aW9uIjoiTWF5IFRodSAwNCAyMDIzIDA1OjAzOjIwIEFNIiwibmJmIjoxNjgzMDkwMjAwLCJleHAiOjE2ODMxNTY4MDAsImlzcyI6Imh0dHBzOi8vbG9jYWxob3N0OjcxODgiLCJhdWQiOiJodHRwczovL2xvY2FsaG9zdDo3MTg4In0.49Kz4R89gT4i7umarNA249zHubU7-_rMvupwg1dE6X8",saveBasicDetailsInJson());
//                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            Log.d("TAG", "onResponse: "+response.body());
//                            Toast.makeText(OEM_Onboarding.this, "Bussiness Deatails saved", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            Log.d("TAG", "onFailure: "+t.getMessage());
//                            Toast.makeText(OEM_Onboarding.this, "Bussiness Deatails not saved", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//
//
//                }
            }
        });
//        Val_pincode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length()>5){
//                    getDistrictState(s.toString().trim(),"basic");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        Val_firmpincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>5){
                    getDistrictState(s.toString().trim(),"firm");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

      Call<BrandResponse> brandResponseCall=apiInterface.getBrands("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJJZCI6IjEiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiYWRtaW5AcGFpc2Fsby5pbiIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2VtYWlsYWRkcmVzcyI6ImFkbWluQHBhaXNhbG8uaW4iLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjEiLCJSb2xlIjpbIkFETUlOIiwiQURNSU4iXSwiQnJhbmNoQ29kZSI6IjAwMSIsIkNyZWF0b3IiOiJBR1JBIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9leHBpcmF0aW9uIjoiTWF5IFRodSAwNCAyMDIzIDA1OjAzOjIwIEFNIiwibmJmIjoxNjgzMDkwMjAwLCJleHAiOjE2ODMxNTY4MDAsImlzcyI6Imh0dHBzOi8vbG9jYWxob3N0OjcxODgiLCJhdWQiOiJodHRwczovL2xvY2FsaG9zdDo3MTg4In0.49Kz4R89gT4i7umarNA249zHubU7-_rMvupwg1dE6X8");
      brandResponseCall.enqueue(new Callback<BrandResponse>() {
          @Override
          public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
//              Log.d("TAG", "onResponse: "+response.body());
//              BrandResponse brandResponse=response.body();
//              Log.d("TAG", "onResponse: "+brandResponse.getData());
//              Gson gson = new Gson();
//              BrandData[] nameList = gson.fromJson(brandResponse.getData(), BrandData[].class);
//              Log.d("TAG", "onResponse: "+nameList.length);
//              spinnerBrand.setAdapter(new AlgorithmAdapter(OEM_Onboarding.this,nameList));

          }

          @Override
          public void onFailure(Call<BrandResponse> call, Throwable t) {
              Log.e("TAG", "onFailure: "+t.getMessage() );
          }
      });

//

        proprietorCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proprietorCheckBox.isChecked()){
                    partnershipCheckBox.setChecked(false);
                    privateCheckBox.setChecked(false);
                    firmType="Proprietor";
                }else {
                    firmType="";
                }
            }
        });

        partnershipCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (partnershipCheckBox.isChecked()){
                    proprietorCheckBox.setChecked(false);
                    privateCheckBox.setChecked(false);
                    firmType="Partnership";

                }else {
                    firmType="";
                }
            }
        });

        privateCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (privateCheckBox.isChecked()){
                    proprietorCheckBox.setChecked(false);
                    partnershipCheckBox.setChecked(false);
                    firmType="Private Limited";

                }else {
                    firmType="";
                }
            }
        });




    }


    private void addOEMPartner( ) {
        final View dialogView = this.getLayoutInflater().inflate(R.layout.layout_dialog_add_oem, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AppCompatButton acbAddMember = (AppCompatButton) dialogView.findViewById(R.id.acbtnAddmember);
        TextInputEditText tietOEMName=dialogView.findViewById(R.id.tietOEMName);
        TextInputEditText tietOEMPan=dialogView.findViewById(R.id.tietOEMPan);
        EditText tietOEMPhone=dialogView.findViewById(R.id.tietOEMPhone);
        EditText tietOEMEmail=dialogView.findViewById(R.id.tietOEMEmail);
        EditText tietOEMAdhaar=dialogView.findViewById(R.id.tietOEMAdhaar);
        EditText tietOEMAddress=dialogView.findViewById(R.id.tietOEMAddress);
        EditText tietOEMPincode=dialogView.findViewById(R.id.tietOEMPincode);
        EditText tietOEMState=dialogView.findViewById(R.id.tietOEMState);
        EditText tietOEMDistrict=dialogView.findViewById(R.id.tietOEMDistrict);
        EditText tietOEMCity=dialogView.findViewById(R.id.tietOEMCity);

        final AlertDialog dialog = builder.create();

        acbAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addingOEMValidate()){


                    AddOEMModel addOEMModel=new AddOEMModel();
                    addOEMModel.setP_City(tietOEMCity.getText().toString());
                    addOEMModel.setP_District(tietOEMDistrict.getText().toString());
                    addOEMModel.setP_State(tietOEMState.getText().toString());
                    addOEMModel.setP_Pincode(tietOEMPincode.getText().toString());
                    addOEMModel.setP_Address(tietOEMAddress.getText().toString());
                    addOEMModel.setPanno(tietOEMPan.getText().toString());
                    addOEMModel.setAadharno(tietOEMAdhaar.getText().toString());
                    addOEMModel.setEmail(tietOEMEmail.getText().toString());
                    addOEMModel.setPhone(tietOEMPhone.getText().toString());
                    addOEMModel.setName(tietOEMName.getText().toString());

                    addOEMModelArrayList.add(addOEMModel);
                    adapter.notifyDataSetChanged();
//                    insertPoint.addView(vi, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    dialog.hide();
                }

            }

            private boolean addingOEMValidate() {
                //Regular Expression
                String regex = "^(.+)@(.+)$";
                //Compile regular expression to get the pattern
                Pattern pattern = Pattern.compile(regex);
                if (tietOEMName.getText().toString().trim().length()<2){
                    tietOEMName.setError("Please enter correct OEM Name");
                    return false;
                }else if(tietOEMPhone.getText().toString().trim().length()!=10){
                    tietOEMPhone.setError("Please enter correct phone of OEM");
                    return false;
                }else if(tietOEMEmail.getText().toString().trim().length()<3 && !pattern.matcher(tietOEMEmail.getText().toString().trim()).matches()){
                    tietOEMEmail.setError("Please Enter correct Email");
                    return false;
                }else if(tietOEMAdhaar.getText().toString().trim().length()!=12){
                    tietOEMAdhaar.setError("Please enter correct Adhaar Number");
                    return false;
                }else if(tietOEMPan.getText().toString().trim().length()<10){
                    tietOEMPan.setError("Please enter correct PAN Number of OEM");
                    return false;

                }else if(tietOEMAddress.getText().toString().trim().length()<6){
                    tietOEMAddress.setError("Please Enter correct Address of OEM");
                    return false;
                }else if(tietOEMPincode.getText().toString().trim().length()!=6){
                    tietOEMPincode.setError("Please Enter correct PIN code");
                    return false;
                }else if(tietOEMState.getText().toString().trim().length()<3){
                    tietOEMState.setError("Please Enter correct State of OEM");
                    return false;
                }else if(tietOEMDistrict.getText().toString().trim().length()<3){
                    tietOEMDistrict.setError("Please enter correct District of OEM");
                    return false;
                }else if(tietOEMCity.getText().toString().trim().length()<3){
                    tietOEMCity.setError("Please enter correct city of OEM");
                }else{
                    return true;
                }

                return false;
            }
        });


        dialog.show();
    }
    private JsonObject saveBasicDetailsInJson() {
        oemDetailsJson=new JsonObject();
        oemDetailsJson.addProperty(  "name",tietOEMName.getText().toString().trim());
        oemDetailsJson.addProperty(  "phone",Val_mobile.getText().toString().trim());
        oemDetailsJson.addProperty(  "email",Val_email.getText().toString().trim());
        oemDetailsJson.addProperty(  "p_Address",Val_address.getText().toString().trim());
        oemDetailsJson.addProperty(  "panno",Val_pancard.getText().toString().trim());
        oemDetailsJson.addProperty(  "p_Pincode",Val_pincode.getText().toString().trim());
        oemDetailsJson.addProperty(  "p_City",acspAadharCity.getText().toString().trim());
        oemDetailsJson.addProperty(  "p_District",acspAadharDistrict.getText().toString().trim());
        oemDetailsJson.addProperty(  "p_State",stateSpinnerOEM.getText().toString().trim());
        oemDetailsJson.addProperty(  "firm_Name",firmName.getText().toString().trim());
        oemDetailsJson.addProperty(  "firm_Type",firmType.trim());
        oemDetailsJson.addProperty(  "o_Address",Val_firmaddress.getText().toString().trim());
        oemDetailsJson.addProperty(  "o_Pincode",Val_firmpincode.getText().toString().trim());
        oemDetailsJson.addProperty(  "o_City",firm_City.getText().toString().trim());
        oemDetailsJson.addProperty(  "o_District",firm_District.getText().toString().trim());
        oemDetailsJson.addProperty(  "o_State",firm_State.getText().toString().trim());
        oemDetailsJson.addProperty(  "vehicle_Type","");
        oemDetailsJson.addProperty(  "manufacturer","");
        oemDetailsJson.addProperty(  "brand",((BrandData)spinnerBrand.getSelectedItem()).getId());

        return oemDetailsJson;
    }

    private boolean validateOEMBussinessDetails() {
        if (firmName.getText().toString().trim().length()<3){
            firmName.setError("Enter Valid Firm Name");
            return false;
        }else if (Val_firmaddress.getText().toString().trim().length()<6){
            Val_firmaddress.setError("Enter Valid Firm Address");
            return false;
        }else if (firmType.length()<5){
            Toast.makeText(this, "Please Select any one Firm Type", Toast.LENGTH_SHORT).show();
            return false;
        }else if (Val_firmpincode.getText().toString().trim().length()<6){
            Val_firmpincode.setError("Enter Valid Firm Pincode");
            return false;
        }else if (firm_State.getText().toString().trim().length()<3){
            firm_State.setError("Enter Valid Firm State");
            return false;
        }else if (firm_City.getText().toString().trim().length()<3){
            firm_City.setError("Enter Valid Firm State");
            return false;
        }else if (firm_District.getText().toString().trim().length()<3){
            firm_District.setError("Enter Valid Firm State");
            return false;
        }else if(Val_pancard.getText().toString().trim().length()!=10){
            Val_pancard.setError("Please enter correct Firm PAN number");
            return false;
        } else if(Val_GST.getText().toString().trim().length()<2){
            Val_GST.setError("Please enter correct GST number");
            return false;
        }else if(firm_SIN.getText().toString().length()>1){
            firm_SIN.setError("Please enter SIn number");
            return false;
        }else if (((BrandData)spinnerBrand.getSelectedItem()).getId()==0){
            Toast.makeText(this, "Brand type not selected", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }

    }

    private boolean validateOEMBasicDetails() {
        if (tietOEMName.getText().toString().trim().length()<2){
            tietOEMName.setError("Enter valid OEM Name");
            return false;
        }else if (Val_mobile.getText().toString().trim().length()<10){
            Val_mobile.setError("Enter valid OEM Mobile");
            return false;

        }else  if (Val_email.getText().toString().trim().length()<7){
            Val_email.setError("Enter valid OEM Email");
            return false;

        }else if (Val_address.getText().toString().trim().length()<10){
            Val_address.setError("Enter valid OEM Address");
            return false;

        }else if (Val_pancard.getText().toString().trim().length()<10){
            Val_pancard.setError("Enter valid OEM PAN Number");
            return false;

        }else if (Val_pincode.getText().toString().trim().length()<6){
            Val_pincode.setError("Enter valid OEM Pincode");
            return false;

        }else  if (stateSpinnerOEM.getText().toString().trim().length()<2){
            stateSpinnerOEM.setError("Enter valid OEM State");
            return false;

        }else if (acspAadharDistrict.getText().toString().trim().length()<3){
            acspAadharDistrict.setError("Enter valid OEM District");
            return false;

        }else if (acspAadharCity.getText().toString().trim().length()<3){
            acspAadharCity.setError("Enter valid OEM City");
            return false;

        }else {
            return true;
        }
    }

    private void getDistrictState(String s,String type) {
        Call<BrandResponse> brandResponseCall=apiInterface.getPINData(Integer.valueOf(s),"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJJZCI6IjEiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiYWRtaW5AcGFpc2Fsby5pbiIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2VtYWlsYWRkcmVzcyI6ImFkbWluQHBhaXNhbG8uaW4iLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjEiLCJSb2xlIjpbIkFETUlOIiwiQURNSU4iXSwiQnJhbmNoQ29kZSI6IjAwMSIsIkNyZWF0b3IiOiJBR1JBIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9leHBpcmF0aW9uIjoiTWF5IFRodSAwNCAyMDIzIDA1OjAzOjIwIEFNIiwibmJmIjoxNjgzMDkwMjAwLCJleHAiOjE2ODMxNTY4MDAsImlzcyI6Imh0dHBzOi8vbG9jYWxob3N0OjcxODgiLCJhdWQiOiJodHRwczovL2xvY2FsaG9zdDo3MTg4In0.49Kz4R89gT4i7umarNA249zHubU7-_rMvupwg1dE6X8");
        brandResponseCall.enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                Log.d("TAG", "onResponse: "+response.body());
                BrandResponse brandResponse=response.body();
                Log.d("TAG", "onResponse: "+brandResponse.getData());
                Gson gson = new Gson();
                PinCodeResponse[] nameList = gson.fromJson(brandResponse.getData(), PinCodeResponse[].class);
              try {
                  if (type.equals("basic")){
                      acspAadharCity.setText(nameList[0].getPostOffice().get(0).getName());
                      acspAadharDistrict.setText(nameList[0].getPostOffice().get(0).getDistrict());
                      stateSpinnerOEM.setText(nameList[0].getPostOffice().get(0).getState());
                  }else if(type.equals("firm")){
                      firm_City.setText(nameList[0].getPostOffice().get(0).getName());
                      firm_District.setText(nameList[0].getPostOffice().get(0).getDistrict());
                      firm_State.setText(nameList[0].getPostOffice().get(0).getState());
                  }

              }catch (Exception e){
                  Val_pincode.setError("Please Enter correct Pincode");
              }


            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: "+t.getMessage() );
            }
        });
    }
}