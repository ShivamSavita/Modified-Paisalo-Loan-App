package com.softeksol.paisalo.dealers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.softeksol.paisalo.dealers.Adapters.AddingOEMListAdapter;
import com.softeksol.paisalo.dealers.Adapters.AlgorithmAdapter;
import com.softeksol.paisalo.dealers.Adapters.AllCreatorAdapter;
import com.softeksol.paisalo.dealers.Adapters.OEMByDealerAdapter;
import com.softeksol.paisalo.dealers.Models.AddOEMModel;
import com.softeksol.paisalo.dealers.Models.BrandData;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.dealers.Models.CreatorAllResponse;
import com.softeksol.paisalo.dealers.Models.OEMByCreatorModel;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerOnBoard extends AppCompatActivity {


    EditText dealerVal_firmpincode;
    TextInputEditText dealerVal_Mobile,dealerVal_EmailOEM,dealerVal_pancard,dealerVal_GST,dealerVal_firmaddress, dealerfirmName;
    Spinner dealerspinnerBrand,dealerspinnerCreator,dealerspinnerChooseOEM;
    ApiInterface apiInterface;
    EditText dealerfirm_City,dealerfirm_District,dealerfirm_State,dealerfirm_SIN;
    ViewGroup insertPoint;
    String firmType="";
    int selectedBrandId=0;
    int brandNameDealer;
    String creatorName;
    int OemId;
    Button btnAddDealerPartner;
    CheckBox dealerproprietorCheckBox,dealerpartnershipCheckBox,dealerprivateCheckBox;
    RecyclerView RecViewDealerPartners;
    JsonObject oemDetailsJson;
    AddingOEMListAdapter adapter;
    ArrayList<AddOEMModel> addDealerModelArrayList=new ArrayList<>();


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.menu_item:   //this item has your app icon
                if (addDealerModelArrayList.size()<1){
                    Toast.makeText(this, "Please add at least one Dealer", Toast.LENGTH_SHORT).show();
                }else{

                    if (validateDealerBussinessDetails()){
                        Log.d("TAG", "onOptionsItemSelected: "+saveBasicDetailsInJson());
                        Call<BrandResponse> call=apiInterface.createDealer(SEILIGL.NEW_TOKEN,saveBasicDetailsInJson());
                        call.enqueue(new Callback<BrandResponse>() {
                            @Override
                            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                                Log.d("TAG", "onResponse: "+response.body());
                                BrandResponse brandResponse=response.body();
                                if (brandResponse.getStatusCode()==200 ){

                                    Toast.makeText(DealerOnBoard.this, "Dealer Created Created Succesfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else if(brandResponse.getStatusCode()==-1){
                                    Toast.makeText(DealerOnBoard.this, "Dealer Already Exist for this creator", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BrandResponse> call, Throwable t) {
                                Log.d("TAG", "onFailure: "+t.getMessage());
                                Toast.makeText(DealerOnBoard.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });



                    }
                }

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
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delear_on_board);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Dealer On-Board");
        apiInterface=new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);
        dealerfirmName=findViewById(R.id.dealerfirmName);
        dealerVal_firmaddress=findViewById(R.id.dealerVal_firmaddress);
        dealerfirm_City=findViewById(R.id.dealerfirm_City);
        dealerfirm_District=findViewById(R.id.dealerfirm_District);
        dealerfirm_State=findViewById(R.id.dealerfirm_State);
        dealerVal_firmpincode=findViewById(R.id.dealerVal_firmpincode);
        dealerspinnerBrand=findViewById(R.id.dealerspinnerBrand);
        dealerspinnerCreator=findViewById(R.id.dealerspinnerCreator);
        dealerprivateCheckBox=findViewById(R.id.dealerprivateCheckBox);
        dealerpartnershipCheckBox=findViewById(R.id.dealerpartnershipCheckBox);
        dealerproprietorCheckBox=findViewById(R.id.dealerproprietorCheckBox);
        dealerVal_pancard=findViewById(R.id.dealerVal_pancard);
        dealerVal_GST=findViewById(R.id.dealerVal_GST);
        dealerfirm_SIN=findViewById(R.id.dealerfirm_SIN);
        RecViewDealerPartners=findViewById(R.id.RecViewDealerPartners);
        dealerspinnerChooseOEM=findViewById(R.id.dealerspinnerChooseOEM);
        dealerVal_EmailOEM=findViewById(R.id.dealerVal_EmailOEM);
        btnAddDealerPartner=findViewById(R.id.btnAddDealerPartner);
        dealerVal_Mobile=findViewById(R.id.dealerVal_Mobile);
        RecViewDealerPartners.setLayoutManager(new LinearLayoutManager(this));
        adapter=new AddingOEMListAdapter(this,addDealerModelArrayList);
        RecViewDealerPartners.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Call<List<CreatorAllResponse>> call=apiInterface.getAllCreator();
        call.enqueue(new Callback<List<CreatorAllResponse>>() {
            @Override
            public void onResponse(Call<List<CreatorAllResponse>> call, Response<List<CreatorAllResponse>> response) {
                List<CreatorAllResponse> creatorAllResponseList=response.body();
                dealerspinnerCreator.setAdapter(new AllCreatorAdapter(DealerOnBoard.this,creatorAllResponseList));
            }

            @Override
            public void onFailure(Call<List<CreatorAllResponse>> call, Throwable t) {

            }
        });



        dealerproprietorCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dealerproprietorCheckBox.isChecked()){
                    dealerpartnershipCheckBox.setChecked(false);
                    dealerprivateCheckBox.setChecked(false);
                    firmType="Proprietor";
                }else {
                    firmType="";
                }
            }
        });

        dealerpartnershipCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dealerpartnershipCheckBox.isChecked()){
                    dealerproprietorCheckBox.setChecked(false);
                    dealerprivateCheckBox.setChecked(false);
                    firmType="Partnership";

                }else {
                    firmType="";
                }
            }
        });

        dealerprivateCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dealerprivateCheckBox.isChecked()){
                    dealerproprietorCheckBox.setChecked(false);
                    dealerpartnershipCheckBox.setChecked(false);
                    firmType="Private Limited";

                }else {
                    firmType="";
                }
            }
        });
        dealerspinnerCreator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CreatorAllResponse creatorAllResponse= (CreatorAllResponse) parent.getSelectedItem();
                creatorName=creatorAllResponse.getCreator();

                Call<BrandResponse> getCreatorByOem=apiInterface.getOEMbyCreator(SEILIGL.NEW_TOKEN,creatorName);
                getCreatorByOem.enqueue(new Callback<BrandResponse>() {
                    @Override
                    public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                        BrandResponse brandResponse=response.body();
                        Gson gson = new Gson();
                        OEMByCreatorModel[] nameList = gson.fromJson(brandResponse.getData(), OEMByCreatorModel[].class);
                        dealerspinnerChooseOEM.setAdapter(new OEMByDealerAdapter(DealerOnBoard.this,nameList));
                    }

                    @Override
                    public void onFailure(Call<BrandResponse> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dealerspinnerChooseOEM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OEMByCreatorModel brandData= (OEMByCreatorModel) parent.getItemAtPosition(position);
                 OemId = brandData.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddDealerPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDealerPartner();
            }
        });
        dealerspinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BrandData brandData= (BrandData) parent.getItemAtPosition(position);
                brandNameDealer=brandData.getId();
                Log.d("TAG", "onItemSelected: "+brandNameDealer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Call<BrandResponse> brandResponseCall=apiInterface.getBrands(SEILIGL.NEW_TOKEN);
        brandResponseCall.enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                BrandResponse brandResponse=response.body();
                Gson gson = new Gson();
                BrandData[] nameList = gson.fromJson(brandResponse.getData(), BrandData[].class);
                dealerspinnerBrand.setAdapter(new AlgorithmAdapter(DealerOnBoard.this,nameList));

            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: "+t.getMessage() );
            }
        });


    }
    private void addDealerPartner( ) {
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

                    addDealerModelArrayList.add(addOEMModel);
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
    public boolean validateDealerBussinessDetails() {
        if (dealerfirmName.getText().toString().trim().length()<3){
            dealerfirmName.setError("Enter Valid Firm Name");
            return false;
        }else if (dealerVal_firmaddress.getText().toString().trim().length()<6){
            dealerVal_firmaddress.setError("Enter Valid Firm Address");
            return false;
        }else if (firmType.length()<5){
            Toast.makeText(DealerOnBoard.this, "Please Select any one Firm Type", Toast.LENGTH_SHORT).show();
            return false;
        }else if (dealerVal_firmpincode.getText().toString().trim().length()<6){
            dealerVal_firmpincode.setError("Enter Valid Firm Pincode");
            return false;
        }else if (dealerfirm_State.getText().toString().trim().length()<3){
            dealerfirm_State.setError("Enter Valid Firm State");
            return false;
        }else if (dealerfirm_City.getText().toString().trim().length()<3){
            dealerfirm_City.setError("Enter Valid Firm State");
            return false;
        }else if (dealerfirm_District.getText().toString().trim().length()<3){
            dealerfirm_District.setError("Enter Valid Firm State");
            return false;
        }else if(dealerVal_pancard.getText().toString().trim().length()!=10){
            dealerVal_pancard.setError("Please enter correct Firm PAN number");
            return false;
        } else if(dealerVal_GST.getText().toString().trim().length()<2){
            dealerVal_GST.setError("Please enter correct GST number");
            return false;
        }else if(dealerVal_Mobile.getText().toString().trim().length()<10){
            dealerVal_Mobile.setError("Please enter correct Mobile number");
            return false;
        }else if(dealerfirm_SIN.getText().toString().length()<1){
            dealerfirm_SIN.setError("Please enter SIn number");
            return false;
        }else if (((BrandData)dealerspinnerBrand.getSelectedItem()).getId()==0){
            Toast.makeText(DealerOnBoard.this, "Brand type not selected", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }

    }
    private JsonArray getJsonOfOem(ArrayList<AddOEMModel> addOEMModelArrayList) {
        JsonArray jsonArray=new JsonArray();
        for (AddOEMModel addOem:addOEMModelArrayList) {
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("userid",0);
            jsonObject.addProperty("userType","");
            jsonObject.addProperty("name",addOem.getName());
            jsonObject.addProperty("phone",addOem.getPhone());
            jsonObject.addProperty("email",addOem.getEmail());
            jsonObject.addProperty("p_Address",addOem.getP_Address());
            jsonObject.addProperty("panno",addOem.getPanno());
            jsonObject.addProperty("aadharno",addOem.getAadharno());
            jsonObject.addProperty("p_Pincode",addOem.getP_Pincode());
            jsonObject.addProperty("p_City",addOem.getP_City());
            jsonObject.addProperty("p_District",addOem.getP_District());
            jsonObject.addProperty("p_State",addOem.getP_State());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
    private JsonObject saveBasicDetailsInJson() {
        oemDetailsJson=new JsonObject();
        oemDetailsJson.addProperty(  "oemId",String.valueOf(OemId));
        oemDetailsJson.addProperty(  "firm_Name",dealerfirmName.getText().toString().trim());
        oemDetailsJson.addProperty(  "firm_Type",firmType);
        oemDetailsJson.addProperty(  "brand",brandNameDealer);
        oemDetailsJson.addProperty(  "creator",creatorName);
        oemDetailsJson.addProperty(  "panno",dealerVal_pancard.getText().toString().trim());
        oemDetailsJson.addProperty(  "gstno",dealerVal_GST.getText().toString().trim());
        oemDetailsJson.addProperty(  "phone",dealerVal_Mobile.getText().toString().trim());
        oemDetailsJson.addProperty(  "email",dealerVal_EmailOEM.getText().toString().trim());
        oemDetailsJson.addProperty(  "o_Address",dealerVal_firmaddress.getText().toString().trim());
        oemDetailsJson.addProperty(  "o_Pincode",dealerVal_firmpincode.getText().toString().trim());
        oemDetailsJson.addProperty(  "o_City",dealerfirm_City.getText().toString().trim());
        oemDetailsJson.addProperty(  "o_District",dealerfirm_District.getText().toString().trim());
        oemDetailsJson.addProperty(  "groupCode","002");
        oemDetailsJson.addProperty(  "o_State",dealerfirm_State.getText().toString().trim());
        oemDetailsJson.addProperty(  "sinno",dealerfirm_SIN.getText().toString().trim());
        oemDetailsJson.addProperty(  "type",firmType);

        oemDetailsJson.add(  "partnerDetails",getJsonOfOem(addDealerModelArrayList));


        return oemDetailsJson;
    }
}