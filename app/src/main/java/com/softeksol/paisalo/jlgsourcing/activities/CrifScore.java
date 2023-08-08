package com.softeksol.paisalo.jlgsourcing.activities;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.AadharUtils;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.WebOperations;
import com.softeksol.paisalo.jlgsourcing.adapters.AdapterListRange;
import com.softeksol.paisalo.jlgsourcing.entities.ESignBorrower;
import com.softeksol.paisalo.jlgsourcing.entities.ESigner;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory_Table;
import com.softeksol.paisalo.jlgsourcing.fragments.FragmentCollection;
import com.softeksol.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;
import com.softeksol.paisalo.jlgsourcing.retrofit.BorrowerData;
import com.softeksol.paisalo.jlgsourcing.retrofit.CheckCrifData;
import com.softeksol.paisalo.jlgsourcing.retrofit.ScrifData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrifScore extends AppCompatActivity {

    ProgressBar progressBar,progressBarsmall;
    TextView textView7,textView8,textView13,textView6,text_srifScore,textView5,text_serverMessage,textView_valueEmi,text_wait;
    GifImageView gifImageView;
    String amount="0",emi="0",score,message;
    int scrifScore=0;
    LinearLayout layout_design,layout_design_pending;
    Button btnTryAgain,btnSrifScore,btnSrifScoreSave;
    TextView textView_emi;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Intent i;
    AdapterListRange rlaBankType;
    String ficode,creator;
    CheckCrifData checkCrifData=new CheckCrifData();
    ESignBorrower eSignerborower;
    String stateName;
    Spinner spinner;
    int attempts_left=4;
    TextView attempsTextView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crif_score);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Loan Eligibility");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        i=getIntent();
        Log.d("TAG", "onCreate: "+i.getStringExtra("ficode"));
        ficode=i.getStringExtra("FIcode");
        creator=i.getStringExtra("creator");
        eSignerborower = (ESignBorrower) i.getSerializableExtra("ESignerBorower");
        sharedPreferences = getSharedPreferences("KYCData",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("Bank",eSignerborower.BankName);
        editor.apply();

        progressBar=findViewById(R.id.circular_determinative_pb);
        attempsTextView=findViewById(R.id.attempsTextView);
        progressBarsmall=findViewById(R.id.progressBar);
        textView7=findViewById(R.id.textView7);
        textView_valueEmi=findViewById(R.id.textView_valueEmi);
        textView_emi=findViewById(R.id.textView_emi);
        textView8=findViewById(R.id.textView8);
        gifImageView=findViewById(R.id.gifImageView);
        textView6=findViewById(R.id.textView6);
        text_wait=findViewById(R.id.text_wait);
        textView13=findViewById(R.id.textView13);
        textView5=findViewById(R.id.textView5);
        text_serverMessage=findViewById(R.id.text_serverMessage);
        text_srifScore=findViewById(R.id.text_srifScore);
        layout_design=findViewById(R.id.layout_design);
        layout_design_pending=findViewById(R.id.layout_design_pending);
        btnTryAgain=findViewById(R.id.btnTryAgain);
        layout_design.setVisibility(View.GONE);
        btnTryAgain.setVisibility(View.GONE);
        layout_design_pending.setVisibility(View.VISIBLE);

        Log.e("LOG", eSignerborower.P_State);
        stateName= RangeCategory.getRangesByCatKeyName("state", eSignerborower.P_State, true);

        //  Toast.makeText(this,stateName, Toast.LENGTH_SHORT).show();

        btnSrifScoreSave=findViewById(R.id.btnSrifScoreSave);
        btnSrifScoreSave.setVisibility(View.GONE);
        btnSrifScoreSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogBreEligibility();
            }
        });
        btnSrifScore=findViewById(R.id.btnSrifScore);
        attempsTextView.setText("Only "+attempts_left+" attempt to switch bank");
        attempsTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (attempts_left<1){
                    spinner.setEnabled(false);
                }else{
                    spinner.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnSrifScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnSrifScore.getText().toString().equals("CLOSE")){
                    finish();
                }else{
                    text_wait.setVisibility(View.VISIBLE);
                    text_serverMessage.setText("");
                    layout_design.setVisibility(View.GONE);
                    btnTryAgain.setVisibility(View.GONE);
                    layout_design_pending.setVisibility(View.VISIBLE);
                    layout_design.setVisibility(View.GONE);
//                    getCrifScore(checkCrifData);
                    checkCrifScore();
                }

               /* text_wait.setVisibility(View.VISIBLE);
                text_serverMessage.setText("");
                btnTryAgain.setVisibility(View.GONE);
                progressBarsmall.setVisibility(View.VISIBLE);
                checkCrifScore(borrowerdata);*/

            }
        });

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_wait.setVisibility(View.VISIBLE);
                text_serverMessage.setText("");
                btnTryAgain.setVisibility(View.GONE);
                progressBarsmall.setVisibility(View.VISIBLE);
                checkCrifScore();

            }
        });
        checkCrifScore();


        String[] arraySpinner = new String[] {
                "UCO", "BOB","PNB","SBI"
        };

        rlaBankType = new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("banks")).queryList(), false);


        spinner = (Spinner) findViewById(R.id.spinSelectBank);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(rlaBankType);
        spinner.setSelection(Utils.setSpinnerPosition(spinner,  AadharUtils.getBankCode(eSignerborower.BankName)));


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putString("Bank",((RangeCategory) spinner.getSelectedItem()).DescriptionEn);
                editor.apply();

                btnSrifScore.setText("TRY AGAIN");
                Log.d("TAG", "onItemSelected: "+sharedPreferences.getString("Bank",""));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private JsonObject getJsonForCrif(String ficode, String creator, String amount, String emi,String bank) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("Ficode",ficode);
        jsonObject.addProperty("Creator",creator);
        jsonObject.addProperty("Loan_Amt",amount);
        jsonObject.addProperty("Emi",emi);
        jsonObject.addProperty("Bank",bank);
        Log.e("TAG",jsonObject.toString());
        return jsonObject;
    }

    private void AlertDialogBreEligibility(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CrifScore.this);
        builder.setMessage("Do you want to Proceed to Loan?");
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {



        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
            finish();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void checkCrifScore(){
        //String address=borrowerdata.getTietAddress1()+" "+borrowerdata.getTietAddress2()+" "+borrowerdata.getTietAddress3();
        ApiInterface apiInterface= ApiClient.getClientdynamic(SEILIGL.AGRA_CREDITMATRIX_BASEURL).create(ApiInterface.class);
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
                                getCrifScore(checkCrifData);
                                updateSourcingStatus();
                            }
                        },25000);
                    }else{
                        message=response.body().getMessage();
                        gifImageView.setImageResource(R.drawable.crosssign);
                        textView8.setText("Sorry!!");
                        textView8.setTextColor(ContextCompat.getColor(CrifScore.this,R.color.red));
                        textView7.setText(message);
                        textView13.setVisibility(View.GONE);
                        textView6.setVisibility(View.GONE);
                        textView_valueEmi.setVisibility(View.GONE);
                        textView_emi.setVisibility(View.GONE);
                        layout_design.setVisibility(View.VISIBLE);
                        layout_design_pending.setVisibility(View.GONE);
                        text_srifScore.setText("0");
                        textView5.setText("0");
                        scrifScore=0;
                        btnSrifScoreSave.setVisibility(View.GONE);
                        btnSrifScore.setVisibility(View.VISIBLE);
                        btnSrifScore.setText("TRY AGAIN");
                    }

                }else{
                    layout_design.setVisibility(View.GONE);
                    layout_design_pending.setVisibility(View.VISIBLE);
                    text_serverMessage.setText("Server Error!!.Please try again!!");
                    btnTryAgain.setVisibility(View.VISIBLE);
                    text_wait.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<CheckCrifData> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
                layout_design.setVisibility(View.GONE);
                progressBarsmall.setVisibility(View.GONE);
                layout_design_pending.setVisibility(View.VISIBLE);
                btnTryAgain.setVisibility(View.VISIBLE);
                text_serverMessage.setText(t.getMessage());
                text_wait.setVisibility(View.GONE);
            }
        });
    }


    private void getCrifScore(CheckCrifData checkCrifData) {
        //String address= borrowerdata.getTietAddress1()+" "+borrowerdata.getTietAddress2()+" "+borrowerdata.getTietAddress3();
        ApiInterface apiInterface= new ApiClient().getClient(SEILIGL.AGRA_CREDITMATRIX_BASEURL).create(ApiInterface.class);
        Call<ScrifData> call=apiInterface.getCrifScore(getJSOnOfCheckDataResponse(checkCrifData));
        call.enqueue(new Callback<ScrifData>() {
            @Override
            public void onResponse(Call<ScrifData> call, Response<ScrifData> response) {
                Log.d("TAG", "onResponse: "+response.body());
                if(response.body() != null){
                    attempts_left--;
                    attempsTextView.setText("Only "+attempts_left+" attempt to switch bank");
                    ScrifData scrifData=response.body();
                    String data=scrifData.getData();
                    if(data == null){
                        layout_design.setVisibility(View.GONE);
                        layout_design_pending.setVisibility(View.VISIBLE);
                        text_serverMessage.setText("Not Eligible. Please try Again!!");
                        btnTryAgain.setVisibility(View.VISIBLE);
                        text_wait.setVisibility(View.GONE);
                        progressBarsmall.setVisibility(View.GONE);
                    }else{
                        if (data.equals("0")){
                          /* // Toast.makeText(CrifScore.this, ""+scrifData.getMessage(), Toast.LENGTH_SHORT).show();
                            layout_design.setVisibility(View.GONE);
                            layout_design_pending.setVisibility(View.VISIBLE);
                            text_serverMessage.setText(scrifData.getMessage());
                            btnTryAgain.setVisibility(View.VISIBLE);
                            text_wait.setVisibility(View.GONE);
                            progressBarsmall.setVisibility(View.GONE);*/
                            message=scrifData.getMessage();
                            gifImageView.setImageResource(R.drawable.crosssign);
                            textView8.setText("Sorry!!");
                            textView8.setTextColor(ContextCompat.getColor(CrifScore.this,R.color.red));
                            textView7.setText(message);
                            textView13.setVisibility(View.GONE);
                            textView6.setVisibility(View.GONE);
                            textView_valueEmi.setVisibility(View.GONE);
                            textView_emi.setVisibility(View.GONE);
                            layout_design.setVisibility(View.VISIBLE);
                            layout_design_pending.setVisibility(View.GONE);
                            text_srifScore.setText("0");
                            textView5.setText("0");
                            scrifScore=0;
                            btnSrifScoreSave.setVisibility(View.GONE);
                            btnSrifScore.setVisibility(View.VISIBLE);
                            btnSrifScore.setText("TRY AGAIN");

                        }else{
                            message=scrifData.getMessage();
                            String[] dataSplitString=data.split("_");
                            amount=dataSplitString[0];
                            emi=dataSplitString[1];
                            score=dataSplitString[2];
                            scrifScore=Integer.parseInt(score);
                            text_srifScore.setText(score);
                            textView5.setText(score);



                            if (Double.parseDouble(amount)>0 && response.body().getStatus()==true){
                                gifImageView.setImageResource(R.drawable.checksign);
                                textView8.setText("Congrats!!");
                                textView8.setTextColor(ContextCompat.getColor(CrifScore.this,R.color.green));
                                textView7.setText(message);
                                textView13.setVisibility(View.VISIBLE);
                                textView6.setVisibility(View.VISIBLE);
                                textView_emi.setVisibility(View.VISIBLE);
                                textView_valueEmi.setVisibility(View.VISIBLE);
                                textView6.setText(amount+" ₹");
                                textView_valueEmi.setText(emi+" ₹");
                                btnSrifScoreSave.setVisibility(View.VISIBLE);
                                btnSrifScore.setVisibility(View.GONE);
                                saveBREData();
//                                    spinner.setEnabled(false);

                            }else{
                                gifImageView.setImageResource(R.drawable.crosssign);
                                textView8.setText("Sorry!!");
                                textView8.setTextColor(ContextCompat.getColor(CrifScore.this,R.color.red));
                                textView7.setText(message);
                                textView13.setVisibility(View.GONE);
                                textView6.setVisibility(View.GONE);
                                textView_valueEmi.setVisibility(View.GONE);
                                textView_emi.setVisibility(View.GONE);
                                btnSrifScoreSave.setVisibility(View.GONE);
                                btnSrifScore.setVisibility(View.VISIBLE);
                                btnSrifScore.setText("TRY AGAIN");

                            }


                            progressBar.setMax(1000);
                            progressBar.setProgress(0);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i=0;i<=scrifScore;i++){
                                        progressBar.setProgress(i);
                                        try {
                                            sleep(10);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }).start();
                            layout_design.setVisibility(View.VISIBLE);
                            layout_design_pending.setVisibility(View.GONE);                        }

                    }
                }else{
                    layout_design.setVisibility(View.GONE);
                    layout_design_pending.setVisibility(View.VISIBLE);
                    text_serverMessage.setText("Server Error!!");
                    btnTryAgain.setVisibility(View.VISIBLE);
                    text_wait.setVisibility(View.GONE);
                }



            }

            @Override
            public void onFailure(Call<ScrifData> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
                layout_design.setVisibility(View.GONE);
                progressBarsmall.setVisibility(View.GONE);
                layout_design_pending.setVisibility(View.VISIBLE);
                btnTryAgain.setVisibility(View.VISIBLE);
                text_serverMessage.setText(t.getMessage());
                text_wait.setVisibility(View.GONE);
            }
        });
    }
    private void updateSourcingStatus(){
        ApiInterface apiInterface= ApiClient.getClient(SEILIGL.NEW_SERVERAPI).create(ApiInterface.class);
        Call<JsonObject> call=apiInterface.updateStatus(checkCrifData.getData().getFiCode()+"",checkCrifData.getData().getCreator());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("TAG", "onResponse: "+response.body());

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());

            }
        });
    }
    private void saveBREData() {

        DataAsyncResponseHandler asyncResponseHandler = new DataAsyncResponseHandler(CrifScore.this, "Data Submitting", "Saving Loan Details") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("TAG",statusCode+"");
                if (statusCode == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CrifScore.this);
                    builder.setTitle("Thanks for choosing us!!");
                    builder.setMessage("Your Loan Request has been Submitted");
                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(CrifScore.this, error.getMessage() , Toast.LENGTH_LONG).show();
            }
        };
        (new WebOperations()).postEntity(CrifScore.this, "BreEligibility", "SaveBreEligibility" ,String.valueOf(getJsonForCrif(ficode,creator,amount,emi,sharedPreferences.getString("Bank",""))), asyncResponseHandler);
    }

    private JsonObject getJSOnOfCheckDataResponse(CheckCrifData checkCrifData) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("fiCode",checkCrifData.getData().getFiCode());
        jsonObject.addProperty("creator",checkCrifData.getData().getCreator());
        jsonObject.addProperty("err_code",checkCrifData.getData().getErrCode());
        jsonObject.addProperty("is_success",checkCrifData.getData().getIsSuccess());
        jsonObject.addProperty("message",checkCrifData.getData().getMessage());
        jsonObject.addProperty("bank",sharedPreferences.getString("Bank",""));
        jsonObject.addProperty("duration",checkCrifData.getData().getDuration());
        jsonObject.addProperty("income",checkCrifData.getData().getIncome());
        jsonObject.addProperty("dob",checkCrifData.getData().getDob());
        jsonObject.addProperty("expense",checkCrifData.getData().getExpense());
        jsonObject.addProperty("loan_amount",checkCrifData.getData().getLoanAmount());
        //  Log.e("BANK",sharedPreferences.getString("Bank",""));
        return  jsonObject;
    }



    private JsonObject getJsonOfKyc() {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("ficode",ficode);
        jsonObject.addProperty("full_name",eSignerborower.PartyName);
        jsonObject.addProperty("dob",parseDateToddMMyyyy(eSignerborower.DOB));
        jsonObject.addProperty("co",eSignerborower.FatherName);
        jsonObject.addProperty("address",eSignerborower.Address);
        jsonObject.addProperty("city",eSignerborower.P_City);
        jsonObject.addProperty("state",stateName);
        jsonObject.addProperty("pin",eSignerborower.P_Pin);
        jsonObject.addProperty("loan_amount",eSignerborower.Loan_Amt);
        jsonObject.addProperty("mobile",eSignerborower.MobileNo);
        jsonObject.addProperty("creator",creator);
        jsonObject.addProperty("pancard",eSignerborower.PanNO);
        jsonObject.addProperty("voter_id",eSignerborower.VoterID);
        jsonObject.addProperty("driving_license_no",eSignerborower.drivinglic);
        jsonObject.addProperty("BrCode",eSignerborower.FoCode);
        jsonObject.addProperty("GrpCode",eSignerborower.CityCode);
        jsonObject.addProperty("AadharID",eSignerborower.AadharNo);
        jsonObject.addProperty("Gender",eSignerborower.Gender);
        jsonObject.addProperty("Bank",sharedPreferences.getString("Bank",""));
        jsonObject.addProperty("Income",eSignerborower.Income);
        jsonObject.addProperty("Expense",eSignerborower.Expense);
        jsonObject.addProperty("LoanReason",eSignerborower.Loan_Reason);
        jsonObject.addProperty("Duration",eSignerborower.Loan_Duration);
        return jsonObject;

    }
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
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
    public static String getRandomSixNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public static String getRandomTwoNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(99);

        // this will convert any number sequence into 6 character.
        return String.format("%02d", number);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}