package com.softeksol.paisalo.ESign.activities;

import static com.softeksol.paisalo.jlgsourcing.Global.BORROWER_TAG;
import static com.softeksol.paisalo.jlgsourcing.Global.ESIGN_TYPE_TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.artifex.mupdfdemo.MuPDFFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.RequestParams;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softeksol.paisalo.ESign.dto.ESingAuthRequestParms;

import com.softeksol.paisalo.jlgsourcing.BuildConfig;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.IglPreferences;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.WebOperations;
import com.softeksol.paisalo.jlgsourcing.activities.ActivityBorrowerKyc;
import com.softeksol.paisalo.jlgsourcing.activities.ActivityManagerSelect;
import com.softeksol.paisalo.jlgsourcing.activities.CrifScore;
import com.softeksol.paisalo.jlgsourcing.entities.Borrower;
import com.softeksol.paisalo.jlgsourcing.entities.Borrower_Table;
import com.softeksol.paisalo.jlgsourcing.entities.ESignBorrower;
import com.softeksol.paisalo.jlgsourcing.entities.ESignBorrower_Table;
import com.softeksol.paisalo.jlgsourcing.entities.ESignGuarantor;
import com.softeksol.paisalo.jlgsourcing.entities.ESignGuarantor_Table;
import com.softeksol.paisalo.jlgsourcing.entities.ESigner;
import com.softeksol.paisalo.jlgsourcing.entities.dto.OperationItem;
import com.softeksol.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;
import com.softeksol.paisalo.jlgsourcing.retrofit.BorrowerData;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class  ActivityESignWithDocumentPL extends AppCompatActivity implements View.OnClickListener {
 //   static final String ACTION_ESIGNRESPONSE = "com.nsdl.egov.esign.rdservice.fp.CAPTURE";
    private static final int BORROWER_FINGERPRINT_REQUEST_CODE = 401;
    private static final int GUARANTOR_FINGERPRINT_REQUEST_CODE = 402;
    private static final int ESIGN_REQUEST_CODE = 403;
    private static final int APK_ESIGN_REQUEST_CODE = 404;
    private static final int OTP_ESIGN_REQUEST_CODE = 405;
    Fragment pdfFragment;
    FragmentManager fm;
    ImageButton imgBtnFingerPrint;
    Button btnProcessEsign;
    byte[] baBorrowerFingerPrintImage, baGuarantorFingerPrintImage;
    //ESingAuthRequestParms eSingParms;
    ESigner eSigner;
    ESignBorrower eSignerborower;
    AlertDialog alertDialog;
    //ESignStore eSignStore;
    private String documentPath;
    private String deviceId;
    private String responseUrl;
    private TextInputLayout tilAadhar;
    private TextInputEditText tietAadhar;
    private int esignType;


    /**
     * get intent that come from ActivityESingList and get all view by findViewById of xml
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esign_pl);
        getSupportActionBar().setTitle(getString(R.string.app_name) + " (" + BuildConfig.VERSION_NAME + ")" + " / " + getString(R.string.e_sign));


        if (savedInstanceState != null) {
            eSigner = (ESigner) savedInstanceState.getSerializable(Global.ESIGNER_TAG);
            esignType = savedInstanceState.getInt(ESIGN_TYPE_TAG, 1);
            eSignerborower = (ESignBorrower) savedInstanceState.getSerializable("ESIGN_BORROWER");
        } else {
            Intent data = getIntent();
            eSigner = (ESigner) data.getSerializableExtra(Global.ESIGNER_TAG);
            eSignerborower = (ESignBorrower) data.getSerializableExtra("ESIGN_BORROWER");
            esignType = data.getIntExtra(ESIGN_TYPE_TAG, 1);
        }


        if (eSigner.docPath != null) {
            fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment frag = MuPDFFragment.newInstance(eSigner.docPath, false);
            ft.add(R.id.pdfview, frag);
            ft.commit();
        }
        ((TextView) findViewById(R.id.tvESignName)).setText(eSigner.Name);
        ((TextView) findViewById(R.id.tvESignGuardian)).setText(eSigner.FatherName);
        ((TextView) findViewById(R.id.tvESignMobile)).setText(String.valueOf(eSigner.MobileNo));
        btnProcessEsign = (Button) findViewById(R.id.btnESignProcessEsign);
        btnProcessEsign.setOnClickListener(this);
    }


    /**
     * handle click event of ESIGN Button
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnESignProcessEsign:
//                Intent intent = new Intent(ActivityESignWithDocumentPL.this, CrifScore.class);
//                intent.putExtra("FIcode",String.valueOf(eSigner.FiCode));
//                intent.putExtra("creator",eSigner.Creator);
//                intent.putExtra("ESignerBorower",eSignerborower);
//                startActivity(intent);
                 showAadharDialog();
                 //processWebESign("419957856512",getString(R.string.consent_1));
                //processApkESign(v);
                break;
        }
    }
    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult: "+resultCode);
        Log.d("TAG", "onActivityResult: "+resultCode);
        Log.d("TAG", "onActivityResult:APK_ESIGN_REQUEST_CODE "+APK_ESIGN_REQUEST_CODE);


        if (requestCode == APK_ESIGN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    String eSignResponse = data.getStringExtra("signedResponse");
                    Log.d("TAG", "onActivityResult: "+eSignResponse);
                    if (eSignResponse.trim().equals("Something went wrong during Esign Processing. Please contact administrator"))
                    {
                        Utils.alert(ActivityESignWithDocumentPL.this,"Something went wrong during Esign Processing. Please contact administrator(NSDL)");
                    }else{
                        sendESign(eSignResponse);
                    }
                }catch (Exception e){
                    Utils.alert(ActivityESignWithDocumentPL.this,"Something went wrong during Esign Processing. Please contact administrator(NSDL)");

                }



            } else {
                Toast.makeText(getBaseContext(),
                        "Nothing Selected", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void updateESignBorrower(ESigner eSigner) {
        SQLite.update(ESignBorrower.class)
                .set(ESignBorrower_Table.ESignSucceed.eq(eSigner.ESignSucceed))
                .where(ESignBorrower_Table.FiCode.eq(eSigner.FiCode))
                .and(ESignBorrower_Table.Creator.eq(eSigner.Creator))
                .execute();
    }

    private void updateESignGuarantor(ESigner eSigner) {
        SQLite.update(ESignGuarantor.class)
                .set(ESignGuarantor_Table.ESignSucceed.eq(eSigner.ESignSucceed))
                .where(ESignGuarantor_Table.FiCode.eq(eSigner.FiCode))
                .and(ESignGuarantor_Table.Creator.eq(eSigner.Creator))
                .and(ESignGuarantor_Table.GuarantorNo.eq(eSigner.GrNo))
                .execute();
    }

    public String getBorrowerUuid(ESigner eSigner) {
        ESignBorrower eSignBorrower = SQLite.select()
                .from(ESignBorrower.class)
                .where(ESignBorrower_Table.FiCode.eq(eSigner.FiCode))
                .and(ESignBorrower_Table.Creator.eq(eSigner.Creator))
                .querySingle();
        return eSignBorrower.KycUuid;
    }


    public String getBorrowerData(ESigner eSigner) {
        Borrower borrowerData = SQLite.select()
                .from(Borrower.class)
                .where(Borrower_Table.FiID.eq(eSigner.FiCode))
                .and(Borrower_Table.Creator.eq(eSigner.Creator))
                .querySingle();
        return borrowerData.aadharid;
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /**
     * call api docsESignPvn or DocESignLoanApplication according to condition and get Response(xmlString , BuildConfig.ESIGN_TYPE , responseUrl) and send this data to another application (NSDL)
     */
    private void processApkESign(String aadhar, String consent) {
        DataAsyncResponseHandler asyncResponseHandler = new DataAsyncResponseHandler(this, "Loan Financing", "Requesting eSignature Data") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(new String(responseBody));
                        String xmlString = jsonObject.getString("ESignXml");
                        responseUrl = Utils.getESignXmlAttribute(xmlString, "responseUrl");
                        Log.d("checkXmlString",xmlString);

                        Intent appStartIntent = new Intent();
                        appStartIntent.setAction("com.nsdl.egov.esign.rdservice.fp.CAPTURE");
                        appStartIntent.putExtra("msg", xmlString); // msg contains esign request xml from ASP.
                        appStartIntent.putExtra("env", "PROD"); //Possible values PREPROD or PROD (case insensative).
                        appStartIntent.putExtra("returnUrl", responseUrl); // your package name where esign response failure/success will be sent.
                        startActivityForResult(appStartIntent, APK_ESIGN_REQUEST_CODE);


                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        ESingAuthRequestParms eSingParms = new ESingAuthRequestParms();
        eSingParms.Creator = eSigner.Creator;
        eSingParms.FICode = eSigner.FiCode;
        eSingParms.UserID = IglPreferences.getPrefString(this, SEILIGL.USER_ID, "");
        eSingParms.AuthMethod = "FP";
        eSingParms.OTPBioMetricData = "NOT APPLICABLE WITH ESIGN";
        eSingParms.CustName = eSigner.Name;

        eSingParms.eKycID = aadhar;
        if (eSigner.GrNo > 0) {
            eSingParms.Reason = "Guarantor";
            eSingParms.GurUUID = eSigner.KycUuid;
            eSingParms.GrNo = eSigner.GrNo;
            eSingParms.CustUUID = getBorrowerUuid(eSigner);
        } else {
            eSingParms.CustUUID = eSigner.KycUuid;
            eSingParms.Reason = "Borrower";

        }
        eSingParms.Concent = consent;

        Log.d("Esign Req Json",WebOperations.convertToJson(eSingParms));
        Log.d("Esign Req Json",esignType+"");
        if (esignType == 1) {
            (new WebOperations()).postEntityESign(this, "docsESignPvn", "signdoc", WebOperations.convertToJson(eSingParms), asyncResponseHandler);
        } else {
            (new WebOperations()).postEntityESign(this, "DocESignLoanApplication", "signdoc", WebOperations.convertToJson(eSingParms), asyncResponseHandler);
        }
        //(new WebOperations()).postEntity(this, "https://agra.seil.in:8444/SeilESignProd/api/docsESignPvn/signdoc", WebOperations.convertToJson(eSingParms), asyncResponseHandler);
    }

    /**
    * Sending data to server after Esigning is done form NSDL
    */
    private void sendESign(String xml) {
        DataAsyncResponseHandler asyncResponseHandler = new DataAsyncResponseHandler(this, "Finalising eSignature") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(responseBody));
                        if (jsonObject.getBoolean("isSuccess")) {
                            showApprovalDialog(jsonObject);
                        } else {
                            Utils.alert(ActivityESignWithDocumentPL.this, jsonObject.getString("ErrMsg"));
                        }
                    } catch (Exception je) {
                        Utils.alert(ActivityESignWithDocumentPL.this, "Something went wrong in ESigning OR response is not correct");
                        je.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    if (!jsonObject.getBoolean("isSuccess")) {
                        Utils.alert(ActivityESignWithDocumentPL.this, jsonObject.getString("ErrMsg"));
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                    Utils.alert(ActivityESignWithDocumentPL.this, "Failed ESign Response");

                }
            }
        };
        //Log.d("XML",xml);
        RequestParams params = new RequestParams();
        params.add("msg", xml);
        params.add("obj", "999999999999");
        (new WebOperations()).postEntityEsign(this, responseUrl, params, asyncResponseHandler);
    }


    /**
     * open Dailog of term and condition and select checkbox and click proceed to call api processApkESign
     */
    private void showAadharDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name) + " (" + BuildConfig.VERSION_NAME + ") ");


        LayoutInflater layoutInflater = this.getLayoutInflater();
        View alertLayout = layoutInflater.inflate(R.layout.layout_aadhar_consent, null);
        //alertLayout

        tietAadhar = (TextInputEditText) alertLayout.findViewById(R.id.tiet_aadhar);
        tilAadhar = (TextInputLayout) alertLayout.findViewById(R.id.til_aadhar);
        if (eSigner.AadharNo != null && eSigner.AadharNo.length() == 12) {
            tilAadhar.setVisibility(View.VISIBLE);
            tietAadhar.setText(eSigner.AadharNo);
            tietAadhar.setEnabled(false);
        } else if (eSigner.AadharNo == null || eSigner.AadharNo.trim().equals("")) {
            tilAadhar.setVisibility(View.GONE);
        }
        tietAadhar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateAadhar();
            }
        });
        AppCompatCheckBox consentCheckbox = (AppCompatCheckBox) alertLayout.findViewById(R.id.chkAadharConsent);
        consentCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                validateAadhar();
            }
        });

        builder.setView(alertLayout);
        DialogInterface.OnClickListener onDialogSubmitListner = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog alertDialog = (AlertDialog) dialog;
                dialog.dismiss();
                TextInputEditText textInputEditTextAadhar = (TextInputEditText) (alertDialog).findViewById(R.id.tiet_aadhar);
                AppCompatCheckBox consentCheckbox = (AppCompatCheckBox) (alertDialog).findViewById(R.id.chkAadharConsent);
                processApkESign(textInputEditTextAadhar.getText().toString(), consentCheckbox.getText().toString());
                //processWebESign(textInputEditTextAadhar.getText().toString(), consentCheckbox.getText().toString());
            }
        };

        builder.setPositiveButton("Proceed", onDialogSubmitListner);
        //builder.setNeutralButton("Closed",null );
        builder.setNegativeButton("Cancel", null);

        alertDialog = builder.create();

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        /*ScrollView containerButtons = (ScrollView) alertDialog.findViewById(R.id.buttonPanel);
        Log.d("Child Count",""+containerButtons.getChildCount());
        View v=new Space(this);
        ButtonBarLayout buttonBarLayout=(ButtonBarLayout)containerButtons.getChildAt(0);
        View vSpace=buttonBarLayout.getChildAt(1);
        v.setLayoutParams(vSpace.getLayoutParams());
        buttonBarLayout.addView(v,3);*/
    }

    private void validateAadhar() {
        //TextInputEditText tietVisitDate = (TextInputEditText) (alertDialog).findViewById(R.id.tiet_aadhar);
        AppCompatCheckBox consentCheckbox = (AppCompatCheckBox) (alertDialog).findViewById(R.id.chkAadharConsent);
        if (consentCheckbox.isChecked() && tilAadhar.getVisibility() == View.GONE) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        } else
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(consentCheckbox.isChecked() && tilAadhar.getVisibility() == View.VISIBLE
                    && tietAadhar.getText().length() == 12 && Long.parseLong(tietAadhar.getText().toString().substring(8)) > 0);
    }

    private void showApprovalDialog(final JSONObject jsonObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name + " (" + BuildConfig.VERSION_NAME + ")");
        builder.setMessage("eSignature Stamping");

        DialogInterface.OnClickListener onDialogSubmitListner = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Button",""+which);
                final DialogInterface dlg = dialog;
                final int whichButton = which;
                JSONObject jo = new JSONObject();
                try {
                    jo.put("CustCode", jsonObject.get("CustCode"));
                    jo.put("Creator", jsonObject.get("Creator"));
                    jo.put("GrNo", eSigner.GrNo);
                    jo.put("TxnID", jsonObject.get("TxnID"));
                    jo.put("isAccepted", whichButton == DialogInterface.BUTTON_POSITIVE);

                    /*if(whichButton == DialogInterface.BUTTON_NEGATIVE){
                        eSigner.ESignSucceed="BLK";
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                DataAsyncResponseHandler asyncResponseHandler = new DataAsyncResponseHandler(ActivityESignWithDocumentPL.this, "Sending Esign") {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //Log.d("Final Esing Response", new String(responseBody));

                        if (statusCode == 200 && whichButton == DialogInterface.BUTTON_POSITIVE) {
                            eSigner.ESignSucceed = "Y";
                            updateESigner(eSigner);
                            setResult(RESULT_OK);
//                            finish();
//                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityESignWithDocumentPL.this);
//                            builder.setTitle("Generate CRIF Score");
//                            builder.setMessage("Do you want to generate CRIF Score?");
//                            builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(ActivityESignWithDocumentPL.this, CrifScore.class);
                                    intent.putExtra("FIcode",String.valueOf(eSigner.FiCode));
                                    intent.putExtra("creator",eSigner.Creator);
                                    intent.putExtra("ESignerBorower",eSignerborower);
                                    startActivity(intent);
                                    dlg.dismiss();
                                    finish();

//                                }
//                            });
//                            builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                   finish();
//                                }
//                            });
//                            builder.create().show();
                        }
                    }
                };
                (new WebOperations()).postEntityESignSubmit(ActivityESignWithDocumentPL.this, "docsESignPvn", "AcceptESign", jo.toString(), asyncResponseHandler);

            }
        };
        builder.setCancelable(false);
        builder.setPositiveButton("Accept", onDialogSubmitListner);
        builder.setNeutralButton("Reject", onDialogSubmitListner);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void getChrifScore() {

    }

    private void updateESigner(ESigner eSigner) {
        if (eSigner.GrNo > 0) {
            updateESignGuarantor(eSigner);
        } else {
            updateESignBorrower(eSigner);
        }

    }

    private void openDiaLog() {
        AlertDialog alertDialog = new AlertDialog.Builder(ActivityESignWithDocumentPL.this)
                .setTitle("App Not Found in Device")
                .setMessage("The Require app is not found in your Device. \n Please Install app first")
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
        final Button btnPositive = alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityESignWithDocumentPL.this, "Please Install", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
