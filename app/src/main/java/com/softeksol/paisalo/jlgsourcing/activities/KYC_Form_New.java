package com.softeksol.paisalo.jlgsourcing.activities;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.loopj.android.http.ResponseHandlerInterface;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softeksol.paisalo.ESign.activities.ActivityESignWithDocumentPL;
import com.softeksol.paisalo.jlgsourcing.BuildConfig;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.IglPreferences;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.WebOperations;
import com.softeksol.paisalo.jlgsourcing.adapters.AdapterListRange;
import com.softeksol.paisalo.jlgsourcing.entities.Borrower;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerExtra;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerExtraBank;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory_Table;
import com.softeksol.paisalo.jlgsourcing.entities.dto.BorrowerDTO;
import com.softeksol.paisalo.jlgsourcing.entities.dto.OperationItem;
import com.softeksol.paisalo.jlgsourcing.fragments.FragmentKycSubmit;
import com.softeksol.paisalo.jlgsourcing.handlers.AsyncResponseHandler;
import com.softeksol.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;
import com.softeksol.paisalo.jlgsourcing.retrofit.CheckCrifData;
import com.softeksol.paisalo.jlgsourcing.retrofit.ScrifData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KYC_Form_New extends AppCompatActivity {
TextInputEditText tietAgricultureIncome,tietFutureIncome,tietExpenseMonthly,tietIncomeMonthly,tietOtherIncome,EditEarningMemberIncome,tietPensionIncome,tietInterestIncome;
Spinner loanbanktype,loanDuration,acspOccupation,acspLoanReason,acspBusinessDetail,earningMemberTypeSpin;
EditText acspLoanAppFinanceLoanAmount;
    private Manager manager;
Button BtnSaveKYCData;
Borrower borrower;
    CheckCrifData checkCrifData=new CheckCrifData();
private AdapterListRange rlaBankType, rlaPurposeType, rlaLoanAmount, rlaEarningMember, rlaSchemeType ,rlsOccupation,rlaBussiness;
Intent i;
ProgressBar progressBar;
CardView cardviewForProgBar;
    AlertDialog alertDialog;
String FatherFName, FatherLName,FatherMName, MotherFName,MotherLName, MotherMName,SpouseLName,SpouseMName,SpouseFName,PanHolderName,voterHolderName,DLholderName;

TextView textViewTotalAnnualIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc_form_new);
        i=getIntent();
/**
 * call api docsESignPvn or DocESignLoanApplication according to condition and get Response(xmlString , BuildConfig.ESIGN_TYPE , responseUrl) and send this data to another application (NSDL)
 */
        FatherFName=i.getStringExtra("FatherFName").trim().length()<1?null:i.getStringExtra("FatherFName").trim();
        FatherLName=i.getStringExtra("FatherLName").trim().length()<1?null:i.getStringExtra("FatherLName").trim();
        FatherMName=i.getStringExtra("FatherMName").trim().length()<1?null:i.getStringExtra("FatherMName").trim();
        MotherFName=i.getStringExtra("MotherFName").trim().length()<1?null:i.getStringExtra("MotherFName").trim();
        MotherLName=i.getStringExtra("MotherLName").trim().length()<1?null:i.getStringExtra("MotherLName").trim();
        MotherMName=i.getStringExtra("MotherMName").trim().length()<1?null:i.getStringExtra("MotherMName").trim();
        SpouseLName=i.getStringExtra("SpouseLName").trim().length()<1?null:i.getStringExtra("SpouseLName").trim();
        SpouseMName=i.getStringExtra("SpouseMName").trim().length()<1?null:i.getStringExtra("SpouseMName").trim();
        SpouseFName=i.getStringExtra("SpouseFName").trim().length()<1?null:i.getStringExtra("SpouseFName").trim();
        PanHolderName=i.getStringExtra("PanHolderName").trim().length()<1?null:i.getStringExtra("PanHolderName").trim();
        voterHolderName=i.getStringExtra("voterHolderName").trim().length()<1?null:i.getStringExtra("voterHolderName").trim();
        DLholderName=i.getStringExtra("DLholderName").trim().length()<1?null:i.getStringExtra("DLholderName").trim();
        manager = (Manager) i.getSerializableExtra("manager");
        borrower = (Borrower) i.getSerializableExtra("borrower");
        tietAgricultureIncome=findViewById(R.id.tietAgricultureIncome);
        tietFutureIncome=findViewById(R.id.tietFutureIncome);
        tietExpenseMonthly=findViewById(R.id.tietExpenseMonthly);
        tietIncomeMonthly=findViewById(R.id.tietIncomeMonthly);
        tietOtherIncome=findViewById(R.id.tietOtherIncome);
        tietInterestIncome=findViewById(R.id.tietInterestIncome);
        tietPensionIncome=findViewById(R.id.tietPensionIncome);
        EditEarningMemberIncome=findViewById(R.id.EditEarningMemberIncome);
        loanbanktype=findViewById(R.id.loanbanktype);
        loanDuration=findViewById(R.id.loanDuration);
        progressBar=findViewById(R.id.progressBar);
        cardviewForProgBar=findViewById(R.id.cardviewForProgBar);


        try{
            Log.d("TAG", "getDataFromView: "+ FatherFName);
            Log.d("TAG", "getDataFromView: "+ FatherLName);
            Log.d("TAG", "getDataFromView: "+ FatherMName);
            Log.d("TAG", "getDataFromView: "+ MotherFName);
            Log.d("TAG", "getDataFromView: "+ MotherLName);
            Log.d("TAG", "getDataFromView: "+ MotherMName);
            Log.d("TAG", "getDataFromView: "+ SpouseLName);
            Log.d("TAG", "getDataFromView: "+ SpouseMName);
            Log.d("TAG", "getDataFromView: "+ SpouseFName);
            Log.d("TAG", "getDataFromView: "+ PanHolderName);
            Log.d("TAG", "getDataFromView: "+ voterHolderName);
            Log.d("TAG", "getDataFromView: "+ DLholderName);




        }catch (Exception e){

        }


        acspOccupation=findViewById(R.id.acspOccupation);
        acspLoanReason=findViewById(R.id.acspLoanReason);
        acspBusinessDetail=findViewById(R.id.acspBusinessDetail);
        earningMemberTypeSpin=findViewById(R.id.earningMemberTypeSpin);
        acspLoanAppFinanceLoanAmount=findViewById(R.id.acspLoanAppFinanceLoanAmount);
        textViewTotalAnnualIncome=findViewById(R.id.textViewTotalAnnualIncome);
        BtnSaveKYCData=findViewById(R.id.BtnFinalSaveKYCData);

        rlaSchemeType = new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("DISBSCH")).queryList(), false);

        rlaPurposeType = new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("loan_purpose"))
                        .orderBy(RangeCategory_Table.SortOrder, true).queryList(), true);

        rlaBankType = new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("banks")).queryList(), false);

        rlaLoanAmount= new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("loan_amt")).queryList(), false);

        rlsOccupation= new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("occupation-type")).queryList(), false);

        rlaBussiness= new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("loan_purpose")).queryList(), false);

        rlaEarningMember = new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("other_income")).queryList(), false);

        progressBar.setVisibility(View.GONE);
        cardviewForProgBar.setVisibility(View.GONE);

        loanbanktype.setAdapter(rlaBankType);
        acspLoanReason.setAdapter(rlaPurposeType);
        acspBusinessDetail.setAdapter(rlaBussiness);
        acspOccupation.setAdapter(rlsOccupation);
        earningMemberTypeSpin.setAdapter(rlaEarningMember);

            tietIncomeMonthly.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {

                        int totalAnnualIncome=Integer.parseInt(EditEarningMemberIncome.getText().toString())+Integer.parseInt(tietAgricultureIncome.getText().toString())+Integer.parseInt(tietFutureIncome.getText().toString())+Integer.parseInt(tietOtherIncome.getText().toString())+(12*Integer.parseInt(tietIncomeMonthly.getText().toString())+Integer.parseInt(tietPensionIncome.getText().toString())+Integer.parseInt(tietInterestIncome.getText().toString()));
                        textViewTotalAnnualIncome.setText("Your Total Annual Income: "+String.valueOf(totalAnnualIncome)+" /-");
                        textViewTotalAnnualIncome.setVisibility(View.VISIBLE);
                    }catch (Exception e){

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            tietAgricultureIncome.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {

                        int totalAnnualIncome=Integer.parseInt(EditEarningMemberIncome.getText().toString())+Integer.parseInt(tietAgricultureIncome.getText().toString())+Integer.parseInt(tietFutureIncome.getText().toString())+Integer.parseInt(tietOtherIncome.getText().toString())+(12*Integer.parseInt(tietIncomeMonthly.getText().toString())+Integer.parseInt(tietPensionIncome.getText().toString())+Integer.parseInt(tietInterestIncome.getText().toString()));
                        textViewTotalAnnualIncome.setText("Your Total Annual Income: "+String.valueOf(totalAnnualIncome)+" /-");
                        textViewTotalAnnualIncome.setVisibility(View.VISIBLE);
                    }catch (Exception e){

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            tietFutureIncome.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {

                        int totalAnnualIncome=Integer.parseInt(EditEarningMemberIncome.getText().toString())+Integer.parseInt(tietAgricultureIncome.getText().toString())+Integer.parseInt(tietFutureIncome.getText().toString())+Integer.parseInt(tietOtherIncome.getText().toString())+(12*Integer.parseInt(tietIncomeMonthly.getText().toString())+Integer.parseInt(tietPensionIncome.getText().toString())+Integer.parseInt(tietInterestIncome.getText().toString()));
                        textViewTotalAnnualIncome.setText("Your Total Annual Income: "+String.valueOf(totalAnnualIncome)+" /-");
                        textViewTotalAnnualIncome.setVisibility(View.VISIBLE);
                    }catch (Exception e){

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            tietOtherIncome.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {

                        int totalAnnualIncome=Integer.parseInt(EditEarningMemberIncome.getText().toString())+Integer.parseInt(tietAgricultureIncome.getText().toString())+Integer.parseInt(tietFutureIncome.getText().toString())+Integer.parseInt(tietOtherIncome.getText().toString())+(12*Integer.parseInt(tietIncomeMonthly.getText().toString())+Integer.parseInt(tietPensionIncome.getText().toString())+Integer.parseInt(tietInterestIncome.getText().toString()));
                        textViewTotalAnnualIncome.setText("Your Total Annual Income: "+String.valueOf(totalAnnualIncome)+" /-");
                        textViewTotalAnnualIncome.setVisibility(View.VISIBLE);
                    }catch (Exception e){

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            EditEarningMemberIncome.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {

                        int totalAnnualIncome=Integer.parseInt(EditEarningMemberIncome.getText().toString())+Integer.parseInt(tietAgricultureIncome.getText().toString())+Integer.parseInt(tietFutureIncome.getText().toString())+Integer.parseInt(tietOtherIncome.getText().toString())+(12*Integer.parseInt(tietIncomeMonthly.getText().toString())+Integer.parseInt(tietPensionIncome.getText().toString())+Integer.parseInt(tietInterestIncome.getText().toString()));
                        textViewTotalAnnualIncome.setText("Your Total Annual Income: "+String.valueOf(totalAnnualIncome)+" /-");
                        textViewTotalAnnualIncome.setVisibility(View.VISIBLE);
                    }catch (Exception e){

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        tietPensionIncome.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {

                        int totalAnnualIncome=Integer.parseInt(EditEarningMemberIncome.getText().toString())+Integer.parseInt(tietAgricultureIncome.getText().toString())+Integer.parseInt(tietFutureIncome.getText().toString())+Integer.parseInt(tietOtherIncome.getText().toString())+(12*Integer.parseInt(tietIncomeMonthly.getText().toString())+Integer.parseInt(tietPensionIncome.getText().toString())+Integer.parseInt(tietInterestIncome.getText().toString()));
                        textViewTotalAnnualIncome.setText("Your Total Annual Income: "+String.valueOf(totalAnnualIncome)+" /-");
                        textViewTotalAnnualIncome.setVisibility(View.VISIBLE);
                    }catch (Exception e){

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        tietInterestIncome.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {

                        int totalAnnualIncome=Integer.parseInt(EditEarningMemberIncome.getText().toString())+Integer.parseInt(tietAgricultureIncome.getText().toString())+Integer.parseInt(tietFutureIncome.getText().toString())+Integer.parseInt(tietOtherIncome.getText().toString())+(12*Integer.parseInt(tietIncomeMonthly.getText().toString())+Integer.parseInt(tietPensionIncome.getText().toString())+Integer.parseInt(tietInterestIncome.getText().toString()));
                        textViewTotalAnnualIncome.setText("Your Total Annual Income: "+String.valueOf(totalAnnualIncome)+" /-");
                        textViewTotalAnnualIncome.setVisibility(View.VISIBLE);
                    }catch (Exception e){

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


        BtnSaveKYCData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                updateBorrower();






            }
        });


    }
    public static Retrofit getClientCrif(String BASE_URL) {
        Retrofit retrofit = null;
        if (retrofit==null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder(

            );
            httpClient.connectTimeout(1, TimeUnit.MINUTES);
            httpClient.readTimeout(1,TimeUnit.MINUTES);
            httpClient.addInterceptor(logging);
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "EEE MMM dd HH:mm:ss zzz yyyy";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("TAG",str);
        return str;
    }
    private JsonObject getJsonOfKyc() {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("ficode","1");
        jsonObject.addProperty("full_name",borrower.Fname+" "+borrower.Mname+" "+borrower.Lname);
        jsonObject.addProperty("dob", parseDateToddMMyyyy(String.valueOf(borrower.DOB)));
        jsonObject.addProperty("co",borrower.F_fname+" "+borrower.F_mname+" "+borrower.F_lname);
        jsonObject.addProperty("address",borrower.P_Add1+" "+borrower.P_add2+" "+borrower.P_add3);
        jsonObject.addProperty("city",borrower.P_city);
        jsonObject.addProperty("state",RangeCategory.getRangesByCatKeyName("state", borrower.p_state, true));
        jsonObject.addProperty("pin",String.valueOf(borrower.p_pin));
        jsonObject.addProperty("loan_amount",String.valueOf(borrower.Loan_Amt));
        jsonObject.addProperty("mobile",borrower.P_ph3);
        jsonObject.addProperty("creator",manager.Creator);
        jsonObject.addProperty("pancard",borrower.PanNO);
        jsonObject.addProperty("voter_id",borrower.voterid);
        jsonObject.addProperty("driving_license_no",borrower.drivinglic);
        jsonObject.addProperty("BrCode",manager.FOCode);
        jsonObject.addProperty("GrpCode",manager.AreaCd);
        jsonObject.addProperty("AadharID",borrower.aadharid);
        jsonObject.addProperty("Gender",borrower.Gender);
        jsonObject.addProperty("Bank",borrower.T_ph3);
        jsonObject.addProperty("Income",String.valueOf(borrower.Income));
        jsonObject.addProperty("Expense",String.valueOf(borrower.Expense));
        jsonObject.addProperty("LoanReason",borrower.Loan_Reason);
        jsonObject.addProperty("Duration",borrower.Loan_Duration);
        return jsonObject;

    }
    private void checkCrifScore(BorrowerDTO borrowerDTO){
        //String address=borrowerdata.getTietAddress1()+" "+borrowerdata.getTietAddress2()+" "+borrowerdata.getTietAddress3();
        ApiInterface apiInterface= getClientCrif(SEILIGL.NEW_SERVERAPIAGARA).create(ApiInterface.class);
        Log.d("TAG", "checkCrifScore: "+getJsonOfKyc());
        Call<CheckCrifData> call=apiInterface.checkCrifScore(getJsonOfKyc());
        call.enqueue(new Callback<CheckCrifData>() {
            @Override
            public void onResponse(Call<CheckCrifData> call, Response<CheckCrifData> response) {
                Log.d("TAG", "onResponse: "+response.body());
                if(response.body() != null){
                    if (response.body().getStatus()!=false){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkCrifData=response.body();
                                getCrifScore(checkCrifData,borrowerDTO);

                            }
                        },25000);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        cardviewForProgBar.setVisibility(View.GONE);
                        Utils.alert(KYC_Form_New.this,"Sorry you can not proceed further");


                    }

                }else{
                    progressBar.setVisibility(View.GONE);
                    cardviewForProgBar.setVisibility(View.GONE);
                    Utils.alert(KYC_Form_New.this,"Check you network connection");

                }

            }

            @Override
            public void onFailure(Call<CheckCrifData> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
                Utils.alert(KYC_Form_New.this,"Check you network connection");
                progressBar.setVisibility(View.GONE);
                cardviewForProgBar.setVisibility(View.GONE);
            }
        });
    }

    private JsonObject getJSOnOfCheckDataResponse(CheckCrifData checkCrifData) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("fiCode",checkCrifData.getData().getFiCode());
        jsonObject.addProperty("creator",checkCrifData.getData().getCreator());
        jsonObject.addProperty("err_code",checkCrifData.getData().getErrCode());
        jsonObject.addProperty("is_success",checkCrifData.getData().getIsSuccess());
        jsonObject.addProperty("message",checkCrifData.getData().getMessage());
        jsonObject.addProperty("bank",borrower.T_ph3);
        jsonObject.addProperty("duration",checkCrifData.getData().getDuration());
        jsonObject.addProperty("income",checkCrifData.getData().getIncome());
        jsonObject.addProperty("dob",checkCrifData.getData().getDob());
        jsonObject.addProperty("expense",checkCrifData.getData().getExpense());
        jsonObject.addProperty("loan_amount",checkCrifData.getData().getLoanAmount());
        //  Log.e("BANK",sharedPreferences.getString("Bank",""));
        return  jsonObject;
    }
    private void showApprovalDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Paisalo loan app" + " (" + BuildConfig.VERSION_NAME + ")");
        builder.setMessage(msg);
        DialogInterface.OnClickListener onDialogSubmitListner = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent(KYC_Form_New.this,ActivityOperationSelect.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

            finish();

            }
        };
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", onDialogSubmitListner);

        alertDialog = builder.create();
        alertDialog.show();
    }
    private void getCrifScore(CheckCrifData checkCrifData,BorrowerDTO borrowerDTO) {
        //String address= borrowerdata.getTietAddress1()+" "+borrowerdata.getTietAddress2()+" "+borrowerdata.getTietAddress3();
        ApiInterface apiInterface= getClientCrif(SEILIGL.NEW_SERVERAPIAGARA).create(ApiInterface.class);
        Call<ScrifData> call=apiInterface.getCrifScore(getJSOnOfCheckDataResponse(checkCrifData));
        call.enqueue(new Callback<ScrifData>() {
            @Override
            public void onResponse(Call<ScrifData> call, Response<ScrifData> response) {
                Log.d("TAG", "onResponse: "+response.body());
                if(response.body() != null){

                    ScrifData scrifData=response.body();
                    String data=scrifData.getData();
                    Log.d("TAG", "onResponse: "+data);
                    Log.d("TAG", "onResponse: "+scrifData.getMessage());
                    Log.d("TAG", "onResponse: "+scrifData.getStatus());

                    if (scrifData.getStatus()){
                        AsyncResponseHandler dataAsyncResponseHandler = new AsyncResponseHandler(KYC_Form_New.this, "Loan Financing\nSubmittiong Loan Application", "Submitting Borrower Information") {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String jsonString = new String(responseBody);
                                Log.d("Response Data",jsonString);
                                try {

                                    JSONObject jo = new JSONObject(jsonString);
                                    long FiCode = jo.getLong("FiCode");
                                    borrower.updateFiCode(FiCode, borrower.Tag);
                                    borrower.Oth_Prop_Det = null;
                                    borrower.save();

//                                    fiDocGeoLoc=new FiDocGeoLoc(FiCode,borrower.Creator,isAdhaarEntry,isNameMatched);
//                                    fiDocGeoLoc.save();
                                    BorrowerExtra borrowerExtra=new BorrowerExtra( FiCode,manager.Creator,Utils.getNotNullInt(tietIncomeMonthly),Utils.getNotNullInt(tietFutureIncome),Utils.getNotNullText(tietAgricultureIncome),Utils.getNotNullText(tietOtherIncome),Utils.getSpinnerStringValue(earningMemberTypeSpin),Utils.getNotNullInt(EditEarningMemberIncome),MotherFName,MotherLName,MotherMName, FatherFName,FatherLName, FatherMName,borrower.Tag,SpouseLName,SpouseMName,SpouseFName,Utils.getNotNullInt(tietPensionIncome),Utils.getNotNullInt(tietInterestIncome));
                                    Log.d("TAG", "onCreate: "+FatherFName);
                                    Log.d("TAG", "onCreate: "+FatherLName);
                                    Log.d("TAG", "onCreate: "+FatherMName);
                                    Log.d("TAG", "onCreate: "+MotherFName);
                                    Log.d("TAG", "onCreate: "+MotherLName);
                                    Log.d("TAG", "onCreate: "+MotherMName);
                                    Log.d("TAG", "onCreate: "+SpouseLName);
                                    Log.d("TAG", "onCreate: "+SpouseMName);
                                    Log.d("TAG", "onCreate: "+SpouseFName);
//                                    BorrowerExtraBank borrowerExtraBank=new BorrowerExtraBank(manager.Creator,manager.TAG,FiCode);

//                                    borrower.associateExtraBank(borrowerExtraBank);

                                    borrower.fiExtra=borrowerExtra;
                                    borrower.fiExtra.save();
//                                    borrower.associateExtraBank(borrower.fiExtraBank);
//                                    borrower.fiExtraBank.save();
                                    borrower.save();
                                    Log.d("TAG", "onSuccess: "+borrower.fiExtra);
                                    Log.d("TAG", "onSuccess: "+WebOperations.convertToJson(borrower.fiExtraBank));
                                    Log.d("TAG", "onSuccess: "+WebOperations.convertToJson(borrower));

                                    AsyncResponseHandler dataAsyncResponseHandlerUpdateFI = new AsyncResponseHandler(KYC_Form_New.this, "Loan Financing\nSubmittiong Loan Application","Submitting Borrower Information") {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            String jsonString = new String(responseBody);
                                            //Log.d("Response Data",jsonString);
                                            Utils.showSnakbar(findViewById(android.R.id.content), "Borrower Loan Application Saved");

                                            try {
                                                JSONObject jo = new JSONObject(jsonString);


                                                Log.d("CHeckJsonFinancing",jo+"");
                                                Log.d("CHeckJsonFinancing1",jsonString+"");


                                                borrower.Code = jo.getLong("FiCode");
                                                borrower.Oth_Prop_Det = "U";

                                                borrower.save();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                            super.onFailure(statusCode, headers, responseBody, error);
                                        }
                                    };
                                    Log.d("TAG", "onSuccess: "+WebOperations.convertToJson(borrower));
                                    (new WebOperations()).postEntity(KYC_Form_New.this, "posfi", "updatefi", WebOperations.convertToJson(borrower), dataAsyncResponseHandlerUpdateFI);


                                    AlertDialog.Builder builder = new AlertDialog.Builder(KYC_Form_New.this);
                                    builder.setTitle("Borrower KYC");
                                    builder.setCancelable(false);
                                    builder.setMessage("KYC Saved with " + manager.Creator + " / " + FiCode + "\nPlease capture / scan documents");
                                    builder.setPositiveButton("Want to E-Sign", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            OperationItem operationItem=new OperationItem(6, "E-Sign", R.color.colorMenuPremature, "POSDB", "Getmappedfo");

                                            Intent intent = new Intent(KYC_Form_New.this, ActivityManagerSelect.class);
                                            intent.putExtra(Global.OPTION_ITEM, operationItem);
                                            intent.putExtra("Title", operationItem.getOprationName());
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent=new Intent(KYC_Form_New.this,ActivityOperationSelect.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    builder.create().show();
                                } catch (JSONException jo) {
                                    Log.d("TAG", "onSuccess: "+jo.getMessage());
                                    Utils.showSnakbar(findViewById(android.R.id.content), jo.getMessage());
                                }
                            }
                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                super.onFailure(statusCode, headers, responseBody, error);
                                Log.d("TAG", "onFailure: "+error.getMessage());
                                //btnSubmit.setEnabled(true);
                                invalidateOptionsMenu();
                            }
                        };


                        //Log.d("Borrower Json",WebOperations.convertToJson(borrower));
                        String borrowerJsonString = WebOperations.convertToJson(borrowerDTO);
                        //Log.d("Borrower Json", borrowerJsonString);
                        Log.d("TAG", "updateBorrower: "+borrowerJsonString);
                        progressBar.setVisibility(View.GONE);
                        cardviewForProgBar.setVisibility(View.GONE);
                        (new WebOperations()).postEntity(getApplicationContext(), "posfi", "savefi", borrowerJsonString, dataAsyncResponseHandler);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        cardviewForProgBar.setVisibility(View.GONE);

                        showApprovalDialog(scrifData.getMessage());
                    }

                }else{
                    progressBar.setVisibility(View.GONE);
                    cardviewForProgBar.setVisibility(View.GONE);
                    Utils.alert(KYC_Form_New.this,"Something Went wrong kindly try again");


                }

            }

            @Override
            public void onFailure(Call<ScrifData> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
                Utils.alert(KYC_Form_New.this,"Check you network connection");
            }
        });
    }
















    private void  updateBorrower() {
        int maxLoanAmt=300000;
        String maxLoanAmtStr="Three lacks";
        Log.d("TAG", "updateBorrower: "+manager.Creator);
        if (manager.Creator.toLowerCase().startsWith("vh")){
            maxLoanAmt=1000000;
             maxLoanAmtStr="Ten lacks";
        }
        if (borrower != null) {
            getDataFromView(this.findViewById(android.R.id.content).getRootView());

            if (tietIncomeMonthly.getText().toString().trim().equals("")){
                tietIncomeMonthly.setError("Please Enter Income");
                Utils.showSnakbar(findViewById(android.R.id.content),"Please enter Income");
            }else if(tietExpenseMonthly.getText().toString().trim().equals("")){
                tietExpenseMonthly.setError("Please Enter Expense");
                Utils.showSnakbar(findViewById(android.R.id.content),"Please enter Expense");

            }else if(tietFutureIncome.getText().toString().trim().equals("")){
                tietFutureIncome.setError("Please Enter Future Income");
                Utils.showSnakbar(findViewById(android.R.id.content),"Please enter Future Income");
            }else if(tietAgricultureIncome.getText().toString().trim().equals("")){
                tietAgricultureIncome.setError("Please Enter Agriculture Income");
                Utils.showSnakbar(findViewById(android.R.id.content),"Please enter Agriculture Income");
            }else if(tietOtherIncome.getText().toString().trim().equals("")){
                tietOtherIncome.setError("Please Enter Other Income");
                Utils.showSnakbar(findViewById(android.R.id.content),"Please enter Other Income");
            }else if(EditEarningMemberIncome.getText().toString().trim().equals("") && !Utils.getSpinnerStringValue(earningMemberTypeSpin).equals("None")){
                EditEarningMemberIncome.setError("Please Enter "+Utils.getSpinnerStringValue(earningMemberTypeSpin)+"'s Income");
                Utils.showSnakbar(findViewById(android.R.id.content),"Please Enter "+Utils.getSpinnerStringValue(earningMemberTypeSpin)+"'s Income");
            } else if(tietPensionIncome.getText().toString().trim().equals("")){
                tietPensionIncome.setError("Please Enter Pension Income");
                Utils.showSnakbar(findViewById(android.R.id.content),"Please enter Pension Income");
            }
             else if(tietInterestIncome.getText().toString().trim().equals("")){
                tietInterestIncome.setError("Please Enter Interest Income");
                Utils.showSnakbar(findViewById(android.R.id.content),"Please enter Interest Income");
            } else if(acspLoanAppFinanceLoanAmount.getText().toString().trim().equals("")){
                acspLoanAppFinanceLoanAmount.setError("Please Enter Loan Amount");
                Utils.showSnakbar(findViewById(android.R.id.content),"Please enter Loan Amount");
            } else if(Integer.parseInt(acspLoanAppFinanceLoanAmount.getText().toString().trim())>maxLoanAmt ||Integer.parseInt(acspLoanAppFinanceLoanAmount.getText().toString().trim())<5000){
                acspLoanAppFinanceLoanAmount.setError("Please Enter Loan Amount Less than "+maxLoanAmtStr+" and Greater than 5 thousand");
                Utils.showSnakbar(findViewById(android.R.id.content),"Please enter Loan Amount Less than "+maxLoanAmtStr+" and Greater than 5 thousand");
            }
            else{

                borrower.Oth_Prop_Det = null;
                borrower.save();
                borrower.fiExtraBank.setMotherName(MotherFName);
                borrower.fiExtraBank.setFatherName(FatherFName);
                String occCode = Utils.getSpinnerStringValue(acspOccupation);
                borrower.fiExtraBank.setCkycOccupationCode(occCode);
                borrower.associateExtraBank(borrower.fiExtraBank);
                borrower.fiExtraBank.save();
                BorrowerDTO borrowerDTO = new BorrowerDTO(borrower);
                borrowerDTO.fiFamExpenses = null;
                borrowerDTO.fiExtra = null;




                progressBar.setVisibility(View.VISIBLE);
                cardviewForProgBar.setVisibility(View.VISIBLE);
                if (SEILIGL.USER_ID.trim().startsWith("DL")){
                    checkCrifScore(borrowerDTO);
                }else{
                    AsyncResponseHandler dataAsyncResponseHandler = new AsyncResponseHandler(KYC_Form_New.this, "Loan Financing\nSubmittiong Loan Application", "Submitting Borrower Information") {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String jsonString = new String(responseBody);
                            Log.d("Response Data",jsonString);
                            try {

                                JSONObject jo = new JSONObject(jsonString);
                                long FiCode = jo.getLong("FiCode");
                                borrower.updateFiCode(FiCode, borrower.Tag);
                                borrower.Oth_Prop_Det = null;
                                borrower.save();

//                                    fiDocGeoLoc=new FiDocGeoLoc(FiCode,borrower.Creator,isAdhaarEntry,isNameMatched);
//                                    fiDocGeoLoc.save();
                                BorrowerExtra borrowerExtra=new BorrowerExtra( FiCode,manager.Creator,Utils.getNotNullInt(tietIncomeMonthly),Utils.getNotNullInt(tietFutureIncome),Utils.getNotNullText(tietAgricultureIncome),Utils.getNotNullText(tietOtherIncome),Utils.getSpinnerStringValue(earningMemberTypeSpin),Utils.getNotNullInt(EditEarningMemberIncome),MotherFName,MotherLName,MotherMName, FatherFName,FatherLName, FatherMName,borrower.Tag,SpouseLName,SpouseMName,SpouseFName,Utils.getNotNullInt(tietPensionIncome),Utils.getNotNullInt(tietInterestIncome));

//                                    BorrowerExtraBank borrowerExtraBank=new BorrowerExtraBank(manager.Creator,manager.TAG,FiCode);

//                                    borrower.associateExtraBank(borrowerExtraBank);

                                borrower.fiExtra=borrowerExtra;
                                borrower.fiExtra.save();
//                                    borrower.associateExtraBank(borrower.fiExtraBank);
//                                    borrower.fiExtraBank.save();
                                borrower.save();

                                AsyncResponseHandler dataAsyncResponseHandlerUpdateFI = new AsyncResponseHandler(KYC_Form_New.this, "Loan Financing\nSubmittiong Loan Application","Submitting Borrower Information") {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        String jsonString = new String(responseBody);
                                        //Log.d("Response Data",jsonString);
                                        Utils.showSnakbar(findViewById(android.R.id.content), "Borrower Loan Application Saved");

                                        try {
                                            JSONObject jo = new JSONObject(jsonString);


                                            Log.d("CHeckJsonFinancing",jo+"");
                                            Log.d("CHeckJsonFinancing1",jsonString+"");


                                            borrower.Code = jo.getLong("FiCode");
                                            borrower.Oth_Prop_Det = "U";

                                            borrower.save();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        super.onFailure(statusCode, headers, responseBody, error);
                                    }
                                };
                                Log.d("TAG", "onSuccess: "+WebOperations.convertToJson(borrower));
                                (new WebOperations()).postEntity(KYC_Form_New.this, "posfi", "updatefi", WebOperations.convertToJson(borrower), dataAsyncResponseHandlerUpdateFI);


                                AlertDialog.Builder builder = new AlertDialog.Builder(KYC_Form_New.this);
                                builder.setTitle("Borrower KYC");
                                builder.setCancelable(false);
                                builder.setMessage("KYC Saved with " + manager.Creator + " / " + FiCode + "\nPlease capture / scan documents");
                                builder.setPositiveButton("Want to E-Sign", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        OperationItem operationItem=new OperationItem(6, "E-Sign", R.color.colorMenuPremature, "POSDB", "Getmappedfo");

                                        Intent intent = new Intent(KYC_Form_New.this, ActivityManagerSelect.class);
                                        intent.putExtra(Global.OPTION_ITEM, operationItem);
                                        intent.putExtra("Title", operationItem.getOprationName());
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent=new Intent(KYC_Form_New.this,ActivityOperationSelect.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                builder.create().show();
                            } catch (JSONException jo) {
                                Log.d("TAG", "onSuccess: "+jo.getMessage());
                                Utils.showSnakbar(findViewById(android.R.id.content), jo.getMessage());
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            super.onFailure(statusCode, headers, responseBody, error);
                            Log.d("TAG", "onFailure: "+error.getMessage());
                            //btnSubmit.setEnabled(true);
                            invalidateOptionsMenu();
                        }
                    };



                    String borrowerJsonString = WebOperations.convertToJson(borrowerDTO);
                    //Log.d("Borrower Json", borrowerJsonString);
                    Log.d("TAG", "updateBorrower: "+borrowerJsonString);
                    progressBar.setVisibility(View.GONE);
                    cardviewForProgBar.setVisibility(View.GONE);
                    (new WebOperations()).postEntity(getApplicationContext(), "posfi", "savefi", borrowerJsonString, dataAsyncResponseHandler);

                }


            }

        }
    }

    private void getDataFromView(View view) {
        borrower.Income= Utils.getNotNullInt(tietIncomeMonthly);
        borrower.Expense=Utils.getNotNullInt(tietExpenseMonthly);
        borrower.Business_Detail=Utils.getSpinnerStringValue(acspBusinessDetail);
        borrower.Loan_Duration=loanDuration.getSelectedItem().toString().trim();
        borrower.Loan_Reason=Utils.getSpinnerStringValue(acspLoanReason);
        borrower.Loan_Amt=Utils.getNotNullInt(acspLoanAppFinanceLoanAmount);
        borrower.BankName=Utils.getSpinnerStringValueDesc(loanbanktype);
        borrower.T_ph3=Utils.getSpinnerStringValueDesc(loanbanktype);
        borrower.Approved=null;
    }
}