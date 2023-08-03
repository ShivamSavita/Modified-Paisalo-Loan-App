package com.softeksol.paisalo.jlgsourcing.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.ResponseHandlerInterface;
import com.raizlabs.android.dbflow.sql.language.SQLite;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class KYC_Form_New extends AppCompatActivity {
TextInputEditText tietAgricultureIncome,tietFutureIncome,tietExpenseMonthly,tietIncomeMonthly,tietOtherIncome,EditEarningMemberIncome,tietPensionIncome,tietInterestIncome;
Spinner loanbanktype,loanDuration,acspOccupation,acspLoanReason,acspBusinessDetail,earningMemberTypeSpin;
EditText acspLoanAppFinanceLoanAmount;
    private Manager manager;
Button BtnSaveKYCData;
Borrower borrower;
private AdapterListRange rlaBankType, rlaPurposeType, rlaLoanAmount, rlaEarningMember, rlaSchemeType ,rlsOccupation,rlaBussiness;
Intent i;
String FatherFName, FatherLName,FatherMName, MotherFName,MotherLName, MotherMName,SpouseLName,SpouseMName,SpouseFName;

TextView textViewTotalAnnualIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc_form_new);
        i=getIntent();
        FatherFName=i.getStringExtra("FatherFName").trim().length()<1?null:i.getStringExtra("FatherFName").trim();
        FatherLName=i.getStringExtra("FatherLName").trim().length()<1?null:i.getStringExtra("FatherLName").trim();
        FatherMName=i.getStringExtra("FatherMName").trim().length()<1?null:i.getStringExtra("FatherMName").trim();
        MotherFName=i.getStringExtra("MotherFName").trim().length()<1?null:i.getStringExtra("MotherFName").trim();
        MotherLName=i.getStringExtra("MotherLName").trim().length()<1?null:i.getStringExtra("MotherLName").trim();
        MotherMName=i.getStringExtra("MotherMName").trim().length()<1?null:i.getStringExtra("MotherMName").trim();
        SpouseLName=i.getStringExtra("SpouseLName").trim().length()<1?null:i.getStringExtra("SpouseLName").trim();
        SpouseMName=i.getStringExtra("SpouseMName").trim().length()<1?null:i.getStringExtra("SpouseMName").trim();
        SpouseFName=i.getStringExtra("SpouseFName").trim().length()<1?null:i.getStringExtra("SpouseFName").trim();
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
                Log.d("TAG", "onSuccess1: "+borrower.fiExtraBank.getActivityType());
                Log.d("TAG", "onSuccess1: "+borrower.fiExtraBank.getCode());
                Log.d("TAG", "onSuccess1: "+borrower.fiExtraBank);
                Log.d("TAG", "onSuccess1: "+WebOperations.convertToJson(borrower.fiExtraBank));

                        AsyncResponseHandler dataAsyncResponseHandler = new AsyncResponseHandler(this, "Loan Financing\nSubmittiong Loan Application", "Submitting Borrower Information") {
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
                (new WebOperations()).postEntity(getApplicationContext(), "posfi", "savefi", borrowerJsonString, dataAsyncResponseHandler);

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



        try{
    Log.d("TAG", "getDataFromView: "+ borrower.Latitude);
    Log.d("TAG", "getDataFromView: "+ borrower.Longitude);
    Log.d("TAG", "getDataFromView: "+ borrower.Gender);
    Log.d("TAG", "getDataFromView: "+ borrower.p_state);
    Log.d("TAG", "getDataFromView: "+ borrower.P_ph3);
    Log.d("TAG", "getDataFromView: "+ borrower.PanNO);
    Log.d("TAG", "getDataFromView: "+ borrower.drivinglic);
    Log.d("TAG", "getDataFromView: "+ borrower.voterid);
    Log.d("TAG", "getDataFromView: "+ borrower.Income);
    Log.d("TAG", "getDataFromView: "+ borrower.Income);
    Log.d("TAG", "getDataFromView: "+ borrower.Expense);
    Log.d("TAG", "getDataFromView: "+ borrower.Business_Detail);
    Log.d("TAG", "getDataFromView: "+ borrower.Loan_Duration);
    Log.d("TAG", "getDataFromView: "+ borrower.Loan_Reason);
    Log.d("TAG", "getDataFromView: "+ borrower.Loan_Amt);



}catch (Exception e){

}

    }
}