package com.softeksol.paisalo.jlgsourcing.ABFActivities;

import static com.softeksol.paisalo.jlgsourcing.Utilities.CameraUtils.REQUEST_TAKE_PHOTO;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softeksol.paisalo.jlgsourcing.BuildConfig;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.AadharUtils;
import com.softeksol.paisalo.jlgsourcing.Utilities.CameraUtils;
import com.softeksol.paisalo.jlgsourcing.Utilities.DateUtils;
import com.softeksol.paisalo.jlgsourcing.Utilities.IglPreferences;
import com.softeksol.paisalo.jlgsourcing.Utilities.MyTextWatcher;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.Utilities.Verhoeff;
import com.softeksol.paisalo.jlgsourcing.WebOperations;
import com.softeksol.paisalo.jlgsourcing.activities.ActivityBorrowerKyc;
import com.softeksol.paisalo.jlgsourcing.activities.KYC_Form_New;
import com.softeksol.paisalo.jlgsourcing.adapters.AdapterListRange;
import com.softeksol.paisalo.jlgsourcing.entities.AadharData;
import com.softeksol.paisalo.jlgsourcing.entities.Borrower;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerExtra;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerExtraBank;
import com.softeksol.paisalo.jlgsourcing.entities.DocumentStore;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory_Table;
import com.softeksol.paisalo.jlgsourcing.entities.dto.BorrowerDTO;
import com.softeksol.paisalo.jlgsourcing.entities.dto.DocumentStoreDTO;
import com.softeksol.paisalo.jlgsourcing.enums.EnumApiPath;
import com.softeksol.paisalo.jlgsourcing.enums.EnumFieldName;
import com.softeksol.paisalo.jlgsourcing.enums.EnumImageTags;
import com.softeksol.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;
import com.softeksol.paisalo.jlgsourcing.location.GpsTracker;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import a.a.e.d;
import cz.msebera.android.httpclient.Header;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FiFormActivity extends AppCompatActivity  implements  View.OnClickListener,  CameraUtils.OnCameraCaptureUpdate{
ImageView imgViewScanQR,imgViewAadharPhoto;
    private Borrower borrower;
    private Manager manager;
    ArrayList<RangeCategory> genders;
    SharedPreferences sharedPreferences;
    private DatePickerDialog.OnDateSetListener dateSetListner;
    protected int emailMobilePresent, imageStartIndex, imageEndIndex;
    private Calendar myCalendar;
    private AdapterListRange   rlaIncome, rlaCaste, rlaReligion, rlaMarritalStatus, rla1to9, rla1kto11k, rlaOwner;
    private AdapterListRange rlaBankType, rlaPurposeType, rlaLoanAmount, rlaEarningMember, rlaSchemeType ,rlsOccupation,rlaBussiness;

    SharedPreferences.Editor editor;
    private Uri uriPicture;
    private BorrowerExtra borrowerExtra;
    protected static final byte SEPARATOR_BYTE = (byte)255;
    String borrowerPics;
    protected static final int VTC_INDEX = 15;
    AdapterListRange genderAdapter;
    private TextInputEditText tietAadharId, tietName, tietAge, tietDob, tietGuardian,
            tietAddress1, tietAddress2, tietAddress3, tietCity, tietPinCode, tietMobile,
            tietDrivingLic, tietPanNo, tietVoterId, tietMotherMName,tietMotherFName,tietMotherLName, tietFatherMName,tietFatherFName,tietFatherLName
            ,tietSpouseLName,tietSpouseMName,tietSpouseFName;
    private boolean showSubmitBorrowerMenuItem = true;
    protected ArrayList<String> decodedData;
    private TextWatcher ageTextWatcher;

    String loanDurationData,stateData,genderData;
    Button BtnFinalSaveKYCData;
    private TextView textViewFiDetails,tilPAN_Name,tilVoterId_Name,tilDL_Name;
    private AppCompatSpinner acspGender,acspAadharState,acspRelationship;
    protected String signature,email,mobile;
    TextInputEditText txtFamHeadIncome,txtPlaceOfBirth;
    ApiInterface apiInterface;
    Spinner spinReligion,spinCaste,spinLoanAppPersonalMarritalStatus;
    EditText acspLoanAppFinanceLoanAmount,txtHouseRent;
    AppCompatSpinner acspBusinessDetail,acspLoanReason,acspOccupation,loanDuration,loanbanktype,spinPresentHouseOwner,spinResidingFrom,spinHouseTypespinRoofType,spinHouseType;
    Button voterIdCheckSign,panCheckSign,dLCheckSign;
    String requestforVerification="";
    String ResponseforVerification="";
    String isNameMatched ="0";
    int isPanverify=0;
    int isDLverify=0;
    int isVoterverify=0;
    String bankName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fi_form);

        manager = (Manager) getIntent().getSerializableExtra(Global.MANAGER_TAG);
        sharedPreferences = getSharedPreferences("KYCData",MODE_PRIVATE);
        tietAadharId = findViewById(R.id.tietAadhar);
        tietName = findViewById(R.id.tietName);
        tietAge = findViewById(R.id.tietAge);
        tietDob = findViewById(R.id.tietDob);
        tietGuardian = findViewById(R.id.tietGuardian);
        tietAddress1 = findViewById(R.id.tietAddress1);
        tietAddress2 = findViewById(R.id.tietAddress2);
        tietAddress3 = findViewById(R.id.tietAddress3);
        tietCity = findViewById(R.id.tietCity);
        tietPinCode = findViewById(R.id.tietPinCode);
        tietMobile = findViewById(R.id.tietMobile);
        tietDrivingLic = findViewById(R.id.tietDrivingLlicense);
        tietPanNo = findViewById(R.id.tietPAN);
        tietVoterId=findViewById(R.id.tietVoter);
        tietMotherMName=findViewById(R.id.tietMotherMName);
        tietMotherFName=findViewById(R.id.tietMotherFName);
        tietMotherLName=findViewById(R.id.tietMotherLName);
        tietFatherMName=findViewById(R.id.tietFatherMName);
        tietFatherFName=findViewById(R.id.tietFatherFName);
        tietFatherLName=findViewById(R.id.tietFatherLName);
        tietSpouseLName=findViewById(R.id.tietSpouseLName);
        tietSpouseMName=findViewById(R.id.tietSpouseMName);
        tietSpouseFName=findViewById(R.id.tietSpouseFName);
        //textViewFiDetails = ((TextView) findViewById(R.id.tv_fi_details));
        acspGender = findViewById(R.id.acspGender);
        tietAge = findViewById(R.id.tietAge);
        imgViewAadharPhoto = findViewById(R.id.imgViewAadharPhoto);
        txtFamHeadIncome = findViewById(R.id.txtFamHeadIncome);
        txtPlaceOfBirth = findViewById(R.id.txtPlaceOfBirth);
        spinReligion = findViewById(R.id.spinReligion);
        spinCaste = findViewById(R.id.spinCaste);
        acspLoanAppFinanceLoanAmount = findViewById(R.id.acspLoanAppFinanceLoanAmount);
        txtHouseRent = findViewById(R.id.txtHouseRent);
        acspBusinessDetail = findViewById(R.id.acspBusinessDetail);
        acspLoanReason = findViewById(R.id.acspLoanReason);
        acspOccupation = findViewById(R.id.acspOccupation);
        loanDuration = findViewById(R.id.loanDuration);
        loanbanktype = findViewById(R.id.loanbanktype);
        spinPresentHouseOwner = findViewById(R.id.spinPresentHouseOwner);
        spinResidingFrom = findViewById(R.id.spinResidingFrom);
        spinHouseTypespinRoofType = findViewById(R.id.spinRoofType);
        spinLoanAppPersonalMarritalStatus = findViewById(R.id.spinLoanAppPersonalMarritalStatus);
        spinHouseType = findViewById(R.id.spinHouseType);
        BtnFinalSaveKYCData = findViewById(R.id.BtnFinalSaveKYCData);
        voterIdCheckSign = findViewById(R.id.voterIdCheckSign);
        panCheckSign = findViewById(R.id.panCheckSign);
        dLCheckSign = findViewById(R.id.dLCheckSign);
        tilPAN_Name=findViewById(R.id.tilPAN_Name);
        tilVoterId_Name=findViewById(R.id.tilVoterId_Name);
        tilDL_Name=findViewById(R.id.tilDL_Name);




       // apiInterface=ApiClient.getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SEILIGL.NEW_SERVER_BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            apiInterface=retrofit.create(ApiInterface.class);

        rlaMarritalStatus = new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("marrital_status")).queryList(), false);
        spinLoanAppPersonalMarritalStatus.setAdapter(rlaMarritalStatus);
        rlaCaste = new AdapterListRange(this ,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("caste")).queryList(), false);
        rlaReligion = new AdapterListRange(this ,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("religion")).queryList(), false);
        rlaOwner = new AdapterListRange(this ,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("land_owner")).queryList(), false);
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
        rla1to9 = new AdapterListRange(this , Utils.getList(1, 9, 1, 1, null), false);
        spinHouseType.setAdapter(new AdapterListRange(this, RangeCategory.getRangesByCatKey("house-type"), false));
        spinHouseTypespinRoofType.setAdapter(new AdapterListRange(this, RangeCategory.getRangesByCatKey("house-roof-type"), false));

        spinCaste.setAdapter(rlaCaste);
        spinReligion.setAdapter(rlaReligion);
        acspBusinessDetail.setAdapter(rlaBussiness);
        acspLoanReason.setAdapter(rlaPurposeType);
        acspOccupation.setAdapter(rlaBussiness);
        spinPresentHouseOwner.setAdapter(rlaOwner);
        spinResidingFrom.setAdapter(rla1to9);
        loanbanktype.setAdapter(rlaBankType);
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("emailId", "dotnetdev2@paisalo.in");
        jsonObject.addProperty("password", "admin@123");
        jsonObject.addProperty("location", "string");



        Call<JsonObject> call=apiInterface.getTokenForABF(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("TAG", "onResponse: "+response.body());
                SEILIGL.NEW_TOKEN="Bearer "+ response.body().get("token").getAsString();
                Log.d("TAGs", "onCreate: "+SEILIGL.NEW_TOKEN);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAGs", "onFailure: "+t.getMessage());
            }
        });
        imgViewAadharPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ImagePicker.with(FiFormActivity.this)
                        .cameraOnly()
                        .start(REQUEST_TAKE_PHOTO);
            }
        });
        tietAge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((TextInputEditText) v).addTextChangedListener(ageTextWatcher);
                } else {
                    ((TextInputEditText) v).removeTextChangedListener(ageTextWatcher);
                }
            }
        });
        tietAge.addTextChangedListener(new MyTextWatcher(tietAge) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
            }
        });
        dateSetListner = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                tietAge.clearFocus();
                myCalendar.set(year, monthOfYear, dayOfMonth);
                tietAge.setText(String.valueOf(DateUtils.getAge(myCalendar)));
                tietDob.setText(DateUtils.getFormatedDate(myCalendar.getTime(), "dd-MMM-yyyy"));
            }
        };
        genders = new ArrayList<>();

        genders.add(new RangeCategory("Female", "Gender"));
        genders.add(new RangeCategory("Male", "Gender"));
        genders.add(new RangeCategory("Transgender", "Gender"));
        genderAdapter=new AdapterListRange(this, genders, false);
        acspGender.setAdapter(genderAdapter);
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());
        acspRelationship = findViewById(R.id.acspRelationship);
        ArrayList<RangeCategory> relationSips = new ArrayList<>();
        relationSips.add(new RangeCategory("Husband", ""));
        relationSips.add(new RangeCategory("Father", ""));
        relationSips.add(new RangeCategory("Mother", ""));

        acspRelationship.setAdapter(new AdapterListRange(this, relationSips, false));
        acspAadharState = findViewById(R.id.acspAadharState);
        Log.d("TAG", "onCreate: "+ RangeCategory.getRangesByCatKey("state", "RangeCode", false));

        acspAadharState.setAdapter(new AdapterListRange(this, RangeCategory.getRangesByCatKey("state", "DescriptionEn", true), false));

        editor = sharedPreferences.edit();
        borrower = new Borrower(manager.Creator, manager.TAG, manager.FOCode, manager.AreaCd, IglPreferences.getPrefString(FiFormActivity.this, SEILIGL.USER_ID, ""));
        borrowerExtra=new BorrowerExtra();
        borrower.associateExtraBank(new BorrowerExtraBank(manager.Creator, manager.TAG));
        borrower.isAadharVerified = "N";
        imgViewScanQR = (ImageView) findViewById(R.id.imgViewScanQR);

        imgViewScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(FiFormActivity.this);
                scanIntegrator.setOrientationLocked(false);
                scanIntegrator.initiateScan(Collections.singleton("QR_CODE"));
            }
        });
        acspGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RangeCategory rangeCategory= (RangeCategory) adapterView.getSelectedItem();
                Log.d("TAG", "onItemSelected: "+rangeCategory.DescriptionEn);
                genderData=rangeCategory.DescriptionEn;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ageTextWatcher = new TextWatcher() {
            String dtString;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0)
                    dtString = DateUtils.getDobFrmAge(Integer.parseInt(s.toString()));
                myCalendar.setTime(DateUtils.getParsedDate(dtString, "dd-MMM-yyyy"));
                tietDob.setText(dtString);
            }
        };
        tietDob.setOnClickListener(this);
        BtnFinalSaveKYCData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  saveDataOfImages(borrowerPics);
   updateBorrower();



//                Intent intent=new Intent(FiFormActivity.this,FiFormSecond.class);
//                intent.putExtra(Global.MANAGER_TAG, manager);
//                startActivity(intent);
            }
        });

        panCheckSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tietPanNo.getText().toString().trim().length() == 10) {
                    cardValidate(tietPanNo.getText().toString().trim(),"pancard","","");
                } else {
                    tilPAN_Name.setVisibility(View.GONE);

                    tietPanNo.setError("Enter PAN");
                }
            }
        });
        dLCheckSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tietDob.getText().toString().trim().length()>9){
                    try {
                        String Dob=formatDate(tietDob.getText().toString().trim(),"dd-MMM-yyyy","yyyy-MM-dd");
                        if (tietDrivingLic.getText().toString().trim().length() >5) {
                            Log.d("TAG", "onClick: "+tietDob.getText().toString()+"/////"+Dob);
                            cardValidate(tietDrivingLic.getText().toString().trim(),"drivinglicense","",Dob);
                        } else {
                            tilDL_Name.setVisibility(View.GONE);
                            tietDrivingLic.setError("Enter Driving License");
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "Please enter Date of Birth", Toast.LENGTH_SHORT).show();
                }

            }
        });
        voterIdCheckSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tietVoterId.getText().toString().trim().length() > 5) {
                    cardValidate(tietVoterId.getText().toString().trim(),"voterid","","");
                }else {
                    tilVoterId_Name.setVisibility(View.GONE);
                    tietVoterId.setError("Enter Voter Id");
                }
            }
        });


        acspAadharState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RangeCategory rangeCategory= (RangeCategory) adapterView.getSelectedItem();
                Log.d("TAG", "onItemSelected: "+rangeCategory.DescriptionEn);
                stateData=rangeCategory.DescriptionEn;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        acspGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RangeCategory rangeCategory= (RangeCategory) adapterView.getSelectedItem();
                Log.d("TAG", "onItemSelected: "+rangeCategory.DescriptionEn);
                genderData=rangeCategory.DescriptionEn;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    private void  updateBorrower() {
        if(stateData.equalsIgnoreCase("APO Address")){
            Toast.makeText(getApplicationContext(), "Select State Name", Toast.LENGTH_SHORT).show();
        }else if (borrower.getPicture()==null ){
            Toast.makeText(getApplicationContext(), "Please Capture Borrower Picture First!!", Toast.LENGTH_SHORT).show();

        }else{
            if (borrower != null) {
                getDataFromView(this.findViewById(android.R.id.content).getRootView());

                if ((tietPanNo.getText().toString().equals("") || tietPanNo.getText().toString().equals(null)) && (tietDrivingLic.getText().toString().equals("") || tietDrivingLic.getText().toString().equals(null)) &&(tietVoterId.getText().toString().equals("") || tietVoterId.getText().toString().equals(null))){
                    Utils.showSnakbar(findViewById(android.R.id.content),"Please enter anyone in PAN number, Driving License or Voter ID");
                }else{
                    if (validateBorrower()) {
                        Map<String, String> messages = borrower.validateKyc(this);
                        if (messages.size() > 0) {
                            String combineMessage = Arrays.toString(messages.values().toArray());
                            combineMessage = combineMessage.replace("[", "->").replace(", ", "\n->").replace("]", "");
                            Log.e("combineMessage",combineMessage);
                            Utils.alert(this, combineMessage);
                        } else {
                            invalidateOptionsMenu();
                          //  if (!chkTvTopup.isChecked()) borrower.OldCaseCode = null;
                            borrower.Oth_Prop_Det = null;
                            borrower.save();
                            borrower.associateExtraBank(borrower.fiExtraBank);
                            borrower.fiExtraBank.save();

                            BorrowerDTO borrowerDTO = new BorrowerDTO(borrower);
                            borrowerDTO.fiFamExpenses = null;
                            borrowerDTO.fiExtra = null;
                            //Log.d("Borrower Json",WebOperations.convertToJson(borrower));
                            String borrowerJsonString = WebOperations.convertToJson(borrowerDTO);
                            //Log.d("Borrower Json", borrowerJsonString);
                            Log.d("TAG", "updateBorrower: "+borrowerJsonString);

                            if (!tietPanNo.getText().toString().equals("") && !tietVoterId.getText().toString().equals("") && !tietDrivingLic.getText().toString().equals("")){
                                if (tilPAN_Name.getText().toString().trim().equals("") || tilVoterId_Name.getText().toString().trim().equals("") || tilDL_Name.getText().toString().trim().equals("")){
                                    Toast.makeText(getApplicationContext(), "Please Verify PAN Card, Voter ID and Driving License", Toast.LENGTH_SHORT).show();
                                }else{
                                    if(!tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilPAN_Name.getText().toString().trim().split(" ")[0]) || !tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilVoterId_Name.getText().toString().trim().split(" ")[0]) || !tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilDL_Name.getText().toString().trim().split(" ")[0])){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(FiFormActivity.this);
                                        builder.setTitle("Caution!!");
                                        builder.setMessage("want to save  data without PAN Card name, Driving License Name, Voter Id Name and Aadhaar Name matching");
                                        builder.setPositiveButton("Save data Forcefully", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(getApplicationContext(), "At this time please enter correct details", Toast.LENGTH_SHORT).show();
                                                sendingDataToNewPage();

                                            }
                                        });
                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Toast.makeText(getApplicationContext(), "Kindly verify all details", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        builder.create().show();

                                    }else{
                                        sendingDataToNewPage();

                                    }
                                }
                            }else{
                                if(!tilPAN_Name.getText().toString().trim().equals("") ||!tilVoterId_Name.getText().toString().trim().equals("") ||!tilDL_Name.getText().toString().trim().equals("") ){
                                    if((!tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilPAN_Name.getText().toString().trim().split(" ")[0]) && !tilPAN_Name.getText().toString().trim().equals("")) || (!tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilVoterId_Name.getText().toString().trim().split(" ")[0]) && !tilVoterId_Name.getText().toString().trim().equals("") )|| (!tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilDL_Name.getText().toString().trim().split(" ")[0]) && !tilDL_Name.getText().toString().trim().equals(""))){

                                        AlertDialog.Builder builder = new AlertDialog.Builder(FiFormActivity.this);
                                        builder.setTitle("Caution!!");
                                        builder.setMessage("want to save  data without Matching Documents Name and Aadhaar Name matching");
                                        builder.setPositiveButton("Save data Forcefully", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(getApplicationContext(), "At this time please enter correct details", Toast.LENGTH_SHORT).show();
                                                sendingDataToNewPage();

                                            }
                                        });
                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Toast.makeText(getApplicationContext(), "Kindly verify all details", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        builder.create().show();
                                    }else{
                                        sendingDataToNewPage();

                                    }
                                }else if (!tietPanNo.getText().toString().equals("")){
                                    if (tilPAN_Name.getText().toString().trim().equals("")){
                                        Toast.makeText(getApplicationContext(), "Please Verify the PAN Card", Toast.LENGTH_SHORT).show();
                                    }else {
                                        if (!tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilPAN_Name.getText().toString().trim().split(" ")[0])) {


                                            AlertDialog.Builder builder = new AlertDialog.Builder(FiFormActivity.this);
                                            builder.setTitle("Caution!!");
                                            builder.setMessage("want to save  data without PAN card Name and Aadhaar Name matching");
                                            builder.setPositiveButton("Save data Forcefully", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(getApplicationContext(), "At this time please enter correct details", Toast.LENGTH_SHORT).show();
                                                    sendingDataToNewPage();

                                                }
                                            });
                                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(getApplicationContext(), "Kindly verify all details", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            builder.create().show();
                                        }
                                        else{
                                            sendingDataToNewPage();

                                        }
                                    }
                                }
                                else if(!tietVoterId.getText().toString().equals("")){
                                    if(tilVoterId_Name.getText().toString().trim().equals("")){
                                        Toast.makeText(getApplicationContext(), "Please verify the Voter ID", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        if (!tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilVoterId_Name.getText().toString().trim().split(" ")[0])) {


                                            AlertDialog.Builder builder = new AlertDialog.Builder(FiFormActivity.this);
                                            builder.setTitle("Caution!!");
                                            builder.setMessage("want to save  data without Voter Id Name and Aadhaar Name matching");
                                            builder.setPositiveButton("Save data Forcefully", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(getApplicationContext(), "At this time please enter correct details", Toast.LENGTH_SHORT).show();
                                                    sendingDataToNewPage();

                                                }
                                            });
                                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(getApplicationContext(), "Kindly verify all details", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            builder.create().show();
                                        }
                                        else{
                                            sendingDataToNewPage();

                                        }
                                    }

                                }
                                else if(!tietDrivingLic.getText().toString().equals("")){
                                    if (tilDL_Name.getText().toString().trim().equals("")){
                                        Toast.makeText(getApplicationContext(), "Please Verify the Driving License ", Toast.LENGTH_SHORT).show();
                                    }else{
                                        if (!tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilDL_Name.getText().toString().trim().split(" ")[0])) {


                                            AlertDialog.Builder builder = new AlertDialog.Builder(FiFormActivity.this);
                                            builder.setTitle("Caution!!");
                                            builder.setMessage("want to save  data without Driving License Name and Aadhaar Name matching");
                                            builder.setPositiveButton("Save data Forcefully", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Toast.makeText(activity, "At this time please enter correct details", Toast.LENGTH_SHORT).show();
                                                    sendingDataToNewPage();

                                                }
                                            });
                                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(getApplicationContext(), "Kindly verify all details", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            builder.create().show();
                                        }
                                        else{
                                            sendingDataToNewPage();

                                        }
                                    }


                                }

                            }

                        }
                    } else {
                        Utils.alert(this, "There is at least one errors in the Aadhar Data");
                    }
                }

            }
        }
    }



    private void saveDataOfImages( String borrowerProfilePic) {
        DocumentStore documentStore = new DocumentStore();
//        {"ChecklistID":0,"Creator":"AGRA","DocRemark":"Picture","Document":"",
//                "FICode":272664,"GrNo":0,"ImageTag":"CUSTIMG",
//                "Tag":"CLAG","UserID":"GRST000223","latitude":0.0,
//                "longitude":0.0,"timestamp":"01-Jul-1996"}

//        {"ChecklistID":0,"Creator":"AGRA","DocRemark":"Picture","Document":"","FICode":272678,
//                "GrNo":1,"ImageTag":"GUARPIC","Tag":"CLAG",
//                "UserID":"GRST000223","latitude":0.0,"longitude":0.0,"timestamp":"01-Jul-1996"}

//        {"ChecklistID":0,"Creator":"AGRA","DocRemark":"Picture","Document":"",
//                "FICode":266173,"GrNo":1,"ImageTag":"GUARPIC",
//                "Tag":"CLAG","UserID":"","latitude":0.0,"longitude":0.0,"timestamp":"01-Jul-1996"}

        documentStore.Creator = "Agra";
        documentStore.ficode = 272654;
        documentStore.fitag = "CLAG";

        documentStore.remarks = "Picture";
        documentStore.checklistid = 0;
        documentStore.userid = "";
        documentStore.latitude = 0;
        documentStore.longitude = 0;
        documentStore.DocId = 0;
        documentStore.FiID =0;
        documentStore.updateStatus = false;
        //documentStore.imagePath = mDocumentStore.imagePath;
        //documentStore.imagePath = "file:" + mDocumentStore.imagePath;
//        if (imgTag.equals("B")){
//            documentStore.GuarantorSerial = 0;
//            documentStore.imageTag = EnumImageTags.Borrower.getImageTag();
//            documentStore.fieldname = EnumFieldName.Borrower.getFieldName();
//            documentStore.apiRelativePath = EnumApiPath.BorrowerApiJson.getApiPath();
//        }else{
//            documentStore.GuarantorSerial = 1;
//            documentStore.imageTag = EnumImageTags.Guarantor.getImageTag();
//            documentStore.fieldname = EnumFieldName.Guarantor.getFieldName();
//            documentStore.apiRelativePath = EnumApiPath.GuarantorApi.getApiPath();
//        }

        documentStore.GuarantorSerial = 1;
        documentStore.imageTag = EnumImageTags.Guarantor.getImageTag();
        documentStore.fieldname = EnumFieldName.Guarantor.getFieldName();
        documentStore.apiRelativePath = EnumApiPath.GuarantorApiJson.getApiPath();


        documentStore.imagePath = borrowerProfilePic;
        Toast.makeText(this, documentStore.imagePath+"", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "saveDataOfImages: "+documentStore.imagePath);



        DataAsyncResponseHandler responseHandler = new DataAsyncResponseHandler(FiFormActivity.this, "Loan Financing", "Uploading " + DocumentStore.getDocumentName(documentStore.checklistid)) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);
                //Utils.showSnakbar( findViewById(android.R.id.content).getRootView(), responseString);
                //if(responseString.equals("")) {


                Log.d("TAG", "onSuccess: "+responseString);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);

            }
        };
        DocumentStoreDTO documentStore1=documentStore.getDocumentDTO();
        documentStore1.Document="";
        Log.d("TAG", "uploadKycJson: "+WebOperations.convertToJson(documentStore1));

        String jsonString = WebOperations.convertToJson(documentStore.getDocumentDTO());
        Log.d("Document Json",jsonString);
        String apiPath = documentStore.checklistid == 0 ? "/api/uploaddocs/savefipicjson" : "/api/uploaddocs/savefidocsjson";
        (new WebOperations()).postEntity(FiFormActivity.this, BuildConfig.BASE_URL + apiPath, jsonString, responseHandler);










    }









    private void getDataFromView(View v) {
        GpsTracker gpsTracker=new GpsTracker(FiFormActivity.this);
        borrower.aadharid = Utils.getNotNullText(tietAadharId);
        borrower.setNames(Utils.getNotNullText(tietName));
        borrower.Age = Utils.getNotNullInt(tietAge);
        borrower.DOB = myCalendar.getTime();
        borrower.setGuardianNames(Utils.getNotNullText(tietGuardian));
        borrower.P_Add1 = Utils.getNotNullText(tietAddress1);
        borrower.P_add2 = Utils.getNotNullText(tietAddress2);
        borrower.P_add3 = Utils.getNotNullText(tietAddress3);
        borrower.P_city = Utils.getNotNullText(tietCity);
        borrower.p_pin = Utils.getNotNullInt(tietPinCode);
        Log.d("TAG", "getDataFromView: "+gpsTracker.getLongitude()+"////"+gpsTracker.getLatitude());
        borrower.Latitude= (float) gpsTracker.getLatitude();
        borrower.Longitude= (float) gpsTracker.getLongitude();
        borrower.Gender = ((RangeCategory) acspGender.getSelectedItem()).RangeCode.substring(0, 1);
        borrower.p_state = ((RangeCategory) acspAadharState.getSelectedItem()).RangeCode;
        borrower.P_ph3 = Utils.getNotNullText(tietMobile);
        borrower.PanNO = Utils.getNotNullText(tietPanNo);
        borrower.drivinglic = Utils.getNotNullText(tietDrivingLic);
        borrower.voterid = Utils.getNotNullText(tietVoterId);
        borrower.IsNameVerify=isNameMatched;
        borrower.isAdhaarEntry="";
        borrower.isMarried = Utils.getSpinnerStringValue((Spinner) v.findViewById(R.id.spinLoanAppPersonalMarritalStatus));
        borrowerExtra.save();
        borrower.fiExtra=null;
        borrower.BankName= bankName;

        borrower.Business_Detail=Utils.getSpinnerStringValue(acspBusinessDetail);
        borrower.Loan_Duration=loanDuration.getSelectedItem().toString().trim();
        borrower.Loan_Reason=Utils.getSpinnerStringValue(acspLoanReason);
        borrower.Loan_Amt=Utils.getNotNullInt(acspLoanAppFinanceLoanAmount);
      //  borrower.BankName=Utils.getSpinnerStringValueDesc(loanbanktype);
        borrower.T_ph3=Utils.getSpinnerStringValueDesc(loanbanktype);
        borrower.Approved=null;

        borrower.save();


    }


    private void sendingDataToNewPage() {


        if(txtPlaceOfBirth.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(), "Enter Place of Birth", Toast.LENGTH_SHORT).show();
        }else if(txtFamHeadIncome.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(), "Enter business of Head of family", Toast.LENGTH_SHORT).show();

        }else if(acspLoanAppFinanceLoanAmount.getText().toString().length()<3){
            Toast.makeText(getApplicationContext(), "Enter Loan amount", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(FiFormActivity.this, FiFormSecond.class);



            intent.putExtra("FatherFName", tietFatherFName.getText().toString());
            intent.putExtra("FatherLName", tietFatherLName.getText().toString());
            intent.putExtra("FatherMName", tietFatherMName.getText().toString());
            intent.putExtra("MotherFName", tietMotherFName.getText().toString());
            intent.putExtra("MotherLName", tietMotherLName.getText().toString());
            intent.putExtra("MotherMName", tietMotherMName.getText().toString());
            intent.putExtra("SpouseLName", tietSpouseLName.getText().toString());
            intent.putExtra("SpouseMName", tietSpouseMName.getText().toString());
            intent.putExtra("SpouseFName", tietSpouseFName.getText().toString());
            intent.putExtra("VoterIdName", tilVoterId_Name.getText().toString());
            intent.putExtra("PANName", tilPAN_Name.getText().toString());
            intent.putExtra("DLName", tilDL_Name.getText().toString());
            intent.putExtra("AadharName", tietName.getText().toString());
            intent.putExtra("manager", manager);
            intent.putExtra("borrower", borrower);
            intent.putExtra("spinReligion", ((RangeCategory)spinReligion.getSelectedItem()).RangeCode);
            intent.putExtra("spinCaste", ((RangeCategory)spinCaste.getSelectedItem()).RangeCode);
            intent.putExtra("spinPresentHouseOwner", ((RangeCategory)spinPresentHouseOwner.getSelectedItem()).RangeCode);
            intent.putExtra("spinResidingFrom", ((RangeCategory)spinResidingFrom.getSelectedItem()).RangeCode);
            intent.putExtra("spinHouseType", ((RangeCategory)spinHouseType.getSelectedItem()).RangeCode);
            intent.putExtra("spinRoofType", ((RangeCategory)spinHouseTypespinRoofType.getSelectedItem()).RangeCode);
            intent.putExtra("PlaceOfBirth", txtPlaceOfBirth.getText().toString());
            intent.putExtra("FamHeadIncome", txtFamHeadIncome.getText().toString());
            intent.putExtra("Occupation",  Utils.getSpinnerStringValue(acspOccupation));
            intent.putExtra("HouseRent",txtHouseRent.getText().toString());
            startActivity(intent);
        }
    }




    @Override
    public void cameraCaptureUpdate(Uri imagePath) {
        uriPicture = imagePath;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            //Log.d("QR Scan","Executed");
            if (scanningResult != null) {
                //we have a result
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                Log.d("CheckXMLDATA3","AadharData:->" + scanContent);
                if (scanFormat != null) {
                    try {
                        setAadharContent(scanContent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {


            if (data != null) {
                uriPicture = data.getData();
                CropImage.activity(uriPicture)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(45, 52)
                        .start(  FiFormActivity.this);
            } else {
                Log.e("ImageData","Null");
                Toast.makeText(this, "Image Data Null", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Exception error = null;


            if (data != null) {
                Uri imageUri = CameraUtils.finaliseImageCropUri(resultCode, data, 300, error, false);
                //Toast.makeText(activity, imageUri.toString(), Toast.LENGTH_SHORT).show();
                File tempCroppedImage = new File(imageUri.getPath());
                Log.e("tempCroppedImage",tempCroppedImage.getPath()+"");


                if (error == null) {

                    if (imageUri != null) {

                        if (tempCroppedImage.length() > 100) {

                            if (borrower != null) {
                                (new File(uriPicture.getPath())).delete();
                                try {


//                                    if (android.os.Build.VERSION.SDK_INT >= 29) {
//                                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getActivity().getContentResolver(), imageUri));
//                                    } else {
//                                        bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);
//                                    }
//
//
//                                    ImageString = bitmapToBase64(bitmap);

                                    File croppedImage = CameraUtils.moveCachedImage2Storage(this, tempCroppedImage, true);
//                                    if (android.os.Build.VERSION.SDK_INT >= 29) {
//                                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getActivity().getContentResolver(), imageUri));
//                                    } else {
//                                        bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);
//                                    }
//                                    //Log.e("CroppedImageMyBitmap", bitmap+ "");
                                    Log.e("CroppedImageFile1", croppedImage.getPath()+"");
                                    Log.e("CroppedImageFile2", croppedImage.getAbsolutePath()+"");
                                    Log.e("CroppedImageFile3", croppedImage.getCanonicalPath()+"");
                                    Log.e("CroppedImageFile4", croppedImage.getParent()+"");
                                    Log.e("CroppedImageFile5", croppedImage.getParentFile().getCanonicalPath()+"");
                                    Log.e("CroppedImageFile6", croppedImage.getParentFile().getName()+"");
//
//                                    ImageString = bitmapToBase64(bitmap);

//                                    borrower.setPicture(croppedImage.getPath(),ImageString);
                                    borrower.setPicture(croppedImage.getPath());
                                    borrowerPics=croppedImage.getPath();
                                    borrower.Oth_Prop_Det = null;
                                    borrower.save();
                                    showPicture(borrower);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else {
                            Toast.makeText(this, "CroppedImage FIle Length:"+tempCroppedImage.length() + "", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, imageUri.toString() + "", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, error.toString() + "", Toast.LENGTH_SHORT).show();
                }
            } else {
//                Log.e("Error",data.getData()+"");
                Toast.makeText(this, "CropImage data: NULL", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void showPicture(Borrower borrower) {


//        Log.e("CHeckingNewCondition",borrower.getPictureborrower()+"");
        if (borrower != null) {

            Log.d("TAG", "showPicture: "+borrower.toString());
//            if (borrower.getPictureborrower()!=null){
//                imageView.setImageBitmap(StringToBitmap(borrower.getPictureborrower()));
//            }

            //Log.d("BorrowerImagePath",borrower.getPicture().getPath());
            if (borrower.getPicture() != null && (new File(borrower.getPicture().getPath())).length() > 100) {
                Log.d("BorrowerImagePath1",borrower.getPicture().getPath());
                Toast.makeText(this, "BorrowerPicture: " + borrower.getPicture().getPath() + "", Toast.LENGTH_SHORT).show();

                setImagepath(new File(borrower.getPicture().getPath()));
                //imageView.setImageBitmap(StringToBitmap(borrower.getPictureborrower()));
                //Glide.with(activity).load(borrower.getPicture().getPath()).override(Target.SIZE_ORIGINAL, 300).into(imageView);
            }
        }
    }
    private void setImagepath(File file) {

//        File imgFile = new  File("/sdcard/Images/test_image.jpg");

//        customProgress.hideProgress();
       // Toast.makeText(this, "Checking File: "+file.getAbsolutePath()+"", Toast.LENGTH_SHORT).show();

        if (file.length() != 0) {

            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

            if (myBitmap != null) {
                imgViewAadharPhoto.setImageBitmap(myBitmap);
                Log.e("CHeckingmyBitmap22",myBitmap+"");
            } else {
                Toast.makeText(this, "Bitmap Null", Toast.LENGTH_SHORT).show();
                Log.e("BitmapImage", "Null");
            }
        } else {
            Toast.makeText(this, "Filepath Empty", Toast.LENGTH_SHORT).show();

        }
    }

    private void setAadharContent(String aadharDataString) throws Exception {

        Log.d("CheckXMLDATA2", "AadharData:->" + aadharDataString);
        if (aadharDataString.toUpperCase().contains("XML")) {

            Log.d("XML printing", "AadharData:->" + aadharDataString);

            AadharData aadharData = AadharUtils.getAadhar(AadharUtils.ParseAadhar(aadharDataString));

            Log.d("TAG", "setAadharContent: "+aadharData.isAadharVerified);
            if (aadharData.AadharId != null) {

                Borrower borrower1 = Borrower.getBorrower(aadharData.AadharId);
                if (borrower1 != null) {
                    borrower = borrower1;
                   // setDataToView(activity.findViewById(android.R.id.content).getRootView());
                   // tietAadharId.setEnabled(false);
                    return;
                }
//                Log.d("TAG", "setAadharContent: done1");
//                if (chkTvTopup.isChecked()) {
//                    if (tietAadharId.getText().toString().equals(aadharData.AadharId)) {
//                        Utils.alert(this, "Aadhar ID did not match with Topup Case");
//                        return;
//                    }
//                }
                borrower.aadharid = aadharData.AadharId;
                Log.d("TAG", "setAadharContent: done2");

            }

            if (aadharData.Address2 == null) {
                aadharData.Address2 = aadharData.Address3;
                aadharData.Address3 = null;
            } else if (aadharData.Address2.trim().equals("")) {
                aadharData.Address2 = aadharData.Address3;
                aadharData.Address3 = null;
            }
            if (aadharData.Address1 == null) {
                aadharData.Address1 = aadharData.Address2;
                aadharData.Address2 = aadharData.Address3;
                aadharData.Address3 = null;
            } else if (aadharData.Address1.trim().equals("")) {
                aadharData.Address1 = aadharData.Address2;
                aadharData.Address2 = aadharData.Address3;
                aadharData.Address3 = null;
            }                    Log.d("TAG", "setAadharContent: done3");

            borrower.isAadharVerified = aadharData.isAadharVerified;
            borrower.setNames(aadharData.Name);
            borrower.DOB = aadharData.DOB;
            borrower.Age = aadharData.Age;
            borrower.Gender = aadharData.Gender;
            borrower.setGuardianNames(aadharData.GurName==null?"":aadharData.GurName);
            borrower.P_city = aadharData.City;
            borrower.p_pin = aadharData.Pin;
            borrower.P_Add1 = aadharData.Address1;
            borrower.P_add2 = aadharData.Address2;
            borrower.P_add3 = aadharData.Address3;
            borrower.p_state = AadharUtils.getStateCode(aadharData.State);
            setDataToView(this.findViewById(android.R.id.content).getRootView());
            validateBorrower();
//            tietAge.setEnabled(false);
//            tietDob.setEnabled(false);
//            aadharNumberentry=true;

        }  else {

            final BigInteger bigIntScanData = new BigInteger(aadharDataString, 10);
            Log.e("testbigin======", "AadharData:->" + bigIntScanData);

            final byte byteScanData[] = bigIntScanData.toByteArray();


            final byte[] decompByteScanData = decompressData(byteScanData);


            List<byte[]> parts = separateData(decompByteScanData);

            Log.e("Parts======11======> ", "part data =====> " + parts.toString());
            decodeData(parts);
            decodeSignature(decompByteScanData);
            decodeMobileEmail(decompByteScanData);

        }


    }


    // 20/11/2022 ========================================
    protected byte[] decompressData(byte[] byteScanData) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(byteScanData.length);
        ByteArrayInputStream bin = new ByteArrayInputStream(byteScanData);
        GZIPInputStream gis = null;

        try {
            gis = new GZIPInputStream(bin);
        } catch (IOException e) {
            Log.e("Exception", "Decompressing QRcode, Opening byte stream failed: " + e.toString());
            // throw new QrCodeException("Error in opening Gzip byte stream while decompressing QRcode",e);
        }

        int size = 0;
        byte[] buf = new byte[1024];
        while (size >= 0) {
            try {
                size = gis.read(buf,0,buf.length);
                if(size > 0){
                    bos.write(buf,0,size);
                }
            } catch (IOException e) {
                Log.e("Exception", "Decompressing QRcode, writing byte stream failed: " + e.toString());
                // throw new QrCodeException("Error in writing byte stream while decompressing QRcode",e);
            }
        }

        try {
            gis.close();
            bin.close();
        } catch (IOException e) {
            Log.e("Exception", "Decompressing QRcode, closing byte stream failed: " + e.toString());
            // throw new QrCodeException("Error in closing byte stream while decompressing QRcode",e);
        }

        return bos.toByteArray();
    }

    protected List<byte[]> separateData(byte[] source) {
        List<byte[]> separatedParts = new LinkedList<>();
        int begin = 0;

        for (int i = 0; i < source.length; i++) {
            if(source[i] == SEPARATOR_BYTE){
                // skip if first or last byte is separator
                if(i != 0 && i != (source.length -1)){
                    separatedParts.add(Arrays.copyOfRange(source, begin, i));
                }
                begin = i + 1;
                // check if we have got all the parts of text data
                if(separatedParts.size() == (VTC_INDEX + 1)){
                    // this is required to extract image data
                    imageStartIndex = begin;
                    break;
                }
            }
        }
        return separatedParts;
    }

    protected void decodeSignature(byte[] decompressedData){
        // extract 256 bytes from the end of the byte array
        int startIndex = decompressedData.length - 257,
                noOfBytes = 256;
        try {
            signature = new String (decompressedData,startIndex,noOfBytes,"ISO-8859-1");
            Log.e("signature======>","signature===> "+signature);
        } catch (UnsupportedEncodingException e) {
            Log.e("Exception", "Decoding Signature of QRcode, ISO-8859-1 not supported: " + e.toString());
            // throw new QrCodeException("Decoding Signature of QRcode, ISO-8859-1 not supported",e);
        }

    }


    protected void decodeData(List<byte[]> encodedData) throws ParseException {
        Iterator<byte[]> i = encodedData.iterator();
        decodedData = new ArrayList<String>();
        while(i.hasNext()){
            decodedData.add(new String(i.next(), StandardCharsets.ISO_8859_1));
        }
        // set the value of email/mobile present flag
        Log.e("Parts======2======> ","part data =====> "+decodedData.toString());
        //emailMobilePresent = Integer.parseInt(decodedData[0]);

        Log.e("Parts======3======> ","part data =====> "+decodedData.get(1));
        Log.e("Parts======3======> ","part data =====> "+decodedData.get(2));
        Log.e("Parts======3======> ","part data =====> "+decodedData.get(3));
        Log.e("Parts======3======> ","part data =====> "+decodedData.get(4));
        Log.e("Parts======3======> ","part data =====> "+decodedData.get(5));
        Log.e("Parts======3======> ","part data =====> "+decodedData.get(6));
        Log.e("Parts======3======> ","part data =====> "+decodedData.get(7));
        Log.e("Parts======3======> ","part data =====> "+decodedData.get(8));
        Log.e("Parts======3======> ","part data =====> "+decodedData.get(14));
        Log.e("Parts======3======> ","part data =====> "+decodedData.get(10));
        Log.e("Parts======3======> ","part data =====> "+decodedData.get(11));
        Log.e("Parts======3======> ","part data =====> "+decodedData.get(12));
        Log.e("Parts======3======> ","part data =====> "+decodedData.get(13));
        Log.e("Parts======3======> ","part data =====> "+decodedData.get(14));
        Log.e("Parts======3======> ","part data =====> "+decodedData.get(15));

        int inc=0;
        Log.d("TAG", "decodeData: "+decodedData.get(0).startsWith("V")+"/////"+decodedData.get(0));
        if (decodedData.get(0).startsWith("V")){
            inc=0;
        }else {
            inc=1;
        }
        // populate decoded data
        SimpleDateFormat sdt = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdt = new SimpleDateFormat("dd-MM-YYYY");

        }
        Date result = null;
        try {
            result = sdt.parse(decodedData.get(4));


        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = formatter.parse(decodedData.get(4-inc));

        Instant instant = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            instant = date.toInstant();
            ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
            LocalDate givenDate = zone.toLocalDate();

            Period period = Period.between(givenDate, LocalDate.now());

            borrower.Age = period.getYears();
            System.out.print(period.getYears()+" years "+period.getMonths()+" and "+period.getDays()+" days");
        }
        borrower.isAadharVerified="Q";
        borrower.aadharid=decodedData.get(2-inc);
        borrower.Gender = decodedData.get(5-inc);
        if (decodedData.get(13-inc).equals("")||decodedData.get(13-inc).equals(null)){
        }else{
            borrower.p_state = AadharUtils.getStateCode(decodedData.get(13-inc));
        }
        if (decodedData.get(3-inc).equals("")||decodedData.get(3-inc).equals(null)){
            //tietName.setEnabled(true);
        }else{
            borrower.setNames(decodedData.get(3-inc));
        }
        if (decodedData.get(4-inc).equals("")||decodedData.get(4-inc).equals(null)){
            //tietDob.setEnabled(true);
        }else{
            borrower.DOB = date;
        }
        if (decodedData.get(6-inc).equals("")||decodedData.get(6-inc).equals(null)){

        }else{
            borrower.setGuardianNames(decodedData.get(6-inc));
        }

        if (decodedData.get(7-inc).equals("")||decodedData.get(7-inc).equals(null)){
            // tietCity.setEnabled(true);
        }else{
            borrower.P_city = decodedData.get(7-inc);
        }

        if (decodedData.get(11-inc).equals("")||decodedData.get(11-inc).equals(null)){
        }else{
            borrower.p_pin = Integer.parseInt(decodedData.get(11-inc));
        }


        if (decodedData.get(10-inc).equals("")||decodedData.get(10-inc).equals(null)){
            //  tietAddress3.setEnabled(true);
        }else{
            borrower.P_add3 = decodedData.get(10-inc);
        }

        try{
            if (decodedData.get(9-inc).equals("")||decodedData.get(9-inc).equals(null)){
                tietAddress2.setEnabled(true);
            }else{

                borrower.P_Add1 = decodedData.get(9-inc);
                borrower.P_add2 = decodedData.get(14-inc);
            }
        }catch (Exception e){
            tietAddress2.setEnabled(true);
        }


        //borrower.P_city = decodedData.get(7);

        // borrower.P_Add1 = decodedData.get(9);
        // borrower.P_add2 = decodedData.get(8);
        // borrower.P_add3 = decodedData.get(10);

        setDataToView(this.findViewById(android.R.id.content).getRootView());
        validateBorrower();
        tietAge.setEnabled(false);
        tietDob.setEnabled(false);
        acspAadharState.setEnabled(false);
        acspGender.setEnabled(false);


    }

    protected void decodeMobileEmail(byte[] decompressedData){
        int mobileStartIndex = 0,mobileEndIndex = 0,emailStartIndex = 0,emailEndIndex = 0;
        switch (emailMobilePresent){
            case 3:
                // both email mobile present
                mobileStartIndex = decompressedData.length - 289; // length -1 -256 -32
                mobileEndIndex = decompressedData.length - 257; // length -1 -256
                emailStartIndex = decompressedData.length - 322;// length -1 -256 -32 -1 -32
                emailEndIndex = decompressedData.length - 290;// length -1 -256 -32 -1

                mobile = bytesToHex (Arrays.copyOfRange(decompressedData,mobileStartIndex,mobileEndIndex+1));
                email = bytesToHex (Arrays.copyOfRange(decompressedData,emailStartIndex,emailEndIndex+1));
                // set image end index, it will be used to extract image data
                imageEndIndex = decompressedData.length - 323;
                break;

            case 2:
                // only mobile
                email = "";
                mobileStartIndex = decompressedData.length - 289; // length -1 -256 -32
                mobileEndIndex = decompressedData.length - 257; // length -1 -256

                mobile = bytesToHex (Arrays.copyOfRange(decompressedData,mobileStartIndex,mobileEndIndex+1));
                // set image end index, it will be used to extract image data
                imageEndIndex = decompressedData.length - 290;

                break;

            case 1:
                // only email
                mobile = "";
                emailStartIndex = decompressedData.length - 289; // length -1 -256 -32
                emailEndIndex = decompressedData.length - 257; // length -1 -256

                email = bytesToHex (Arrays.copyOfRange(decompressedData,emailStartIndex,emailEndIndex+1));
                // set image end index, it will be used to extract image data
                imageEndIndex = decompressedData.length - 290;
                break;

            default:
                // no mobile or email
                mobile = "";
                email = "";
                // set image end index, it will be used to extract image data
                imageEndIndex = decompressedData.length - 257;
        }

        Log.e("email mobile======> ","Data=====>"+email +"   "+mobile);

    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    //=================================================
    private boolean validateBorrower() {
        boolean retVal = true;
        retVal &= validateControls(tietAadharId, tietAadharId.getText().toString());
        retVal &= validateControls(tietName, tietName.getText().toString());
        retVal &= validateControls(tietGuardian, tietGuardian.getText().toString());
        retVal &= validateControls(tietAge, tietAge.getText().toString());
        retVal &= validateControls(tietDob, tietDob.getText().toString());
        retVal &= validateControls(tietAddress1, tietAddress1.getText().toString());
        retVal &= validateControls(tietCity, tietCity.getText().toString());
        retVal &= validateControls(tietPinCode, tietPinCode.getText().toString());
        retVal &= validateControls(tietMobile, tietMobile.getText().toString());
//      retVal &= validateControls(tietVoterId, tietVoterId.getText().toString()) || validateControls(tietPanNo, tietPanNo.getText().toString());
        retVal &= validateControls(tietDrivingLic, tietDrivingLic.getText().toString());
        retVal &= validateControls(tietMotherFName, tietMotherFName.getText().toString());
        retVal &= validateControls(tietFatherFName, tietFatherFName.getText().toString());
        return retVal;
    }
    private boolean validateControls(EditText editText, String text) {
        boolean retVal = true;
        editText.setError(null);
        switch (editText.getId()) {
            case R.id.tietAadhar:
                if (editText.length() != 12) {
                    editText.setError("Should be of 12 Characters");
                    retVal = false;
                } else if (!Verhoeff.validateVerhoeff(editText.getText().toString())) {
                    editText.setError("Aadhar is not Valid");
                    retVal = false;
                }
                break;
            case R.id.tietDob:
            case R.id.tietGuardian:
            case R.id.tietName:
                if (editText.length() < 3) {
                    editText.setError("Should be more than 3 Characters");
                    retVal = false;
                }
                break;

            case R.id.tietAge:
                try {
                    if (text.length() == 0) text = "0";
                    int age = Integer.parseInt(text);
                    if (age < 18) {
                        editText.setError("Age should be greater than 17");
                        retVal = false;
                    } else if (age > 65) {
                        editText.setError("Age should be less than 66");
                        retVal = false;
                    }
                    tietDob.setEnabled(retVal);
                }catch (Exception e){
                    editText.setError("Age should be greater than 17");
                }
                break;
            case R.id.tietAddress1:
                String character=editText.getText().toString().trim();
                if (editText.getText().toString().trim().length() < 1) {
                    editText.setError("Should be more than 5 Characters");
                    retVal = false;
                }else{
                    if (character.matches(".*[A-Za-z].*") && character.matches(".*[0-9].*")) {
                    } else {
                        tietAddress1.setEnabled(true);
                        editText.setError("Only number not allowed.");
                        retVal = false;
                    }
                }
                break;
            case R.id.tietCity:
                if (editText.getText().toString().trim().length() < 1) {
                    editText.setError("Should be more than 2 Characters");
                    retVal = false;
                }
                break;
            case R.id.tietPinCode:
                if (editText.getText().toString().trim().length() != 6) {
                    editText.setError("Should be of 6 digits");
                    retVal = false;
                }
                break;

            case R.id.tietDrivingLlicense:
                if (editText.getText().toString().trim().length() > 0) {
                    if (editText.getText().toString().trim().length() < 1) {
                        editText.setError("Enter Driving License");
                        retVal = false;
                    }
                } else {
                    retVal = true;
                    editText.setError(null);
                }
                break;
            case R.id.tietBankCIF:
                if (editText.getText().toString().trim().length() > 0) {
                    if (editText.getText().toString().trim().length() < 10) {
                        editText.setError("Should be more than 9 Characters");
                        retVal = false;
                    }
                } else {
                    retVal = true;
                    editText.setError(null);
                }
                break;
            case R.id.tietMobile:
                if (editText.getText().toString().trim().length() > 0) {
                    if (editText.getText().toString().trim().length() != 10) {
                        editText.setError("Should be of 10 digits");
                        retVal = false;
                    }
                } else {
                    retVal = true;
                    editText.setError(null);
                }
                break;

            case R.id.tietMotherFName:
                if (editText.getText().toString().trim().length() < 1) {

                    editText.setError("Please Enter Mother Name");
                    retVal = false;

                } else {
                    retVal = true;
                    editText.setError(null);
                }
                break;

            case R.id.tietFatherFName:
                if (editText.getText().toString().trim().length() < 1) {

                    editText.setError("Please Enter Father Name");
                    retVal = false;

                } else {
                    retVal = true;
                    editText.setError(null);
                }
                break;
        }
        return retVal;
    }

    private void setDataToView(View v) {
        if (borrower.Gender != null) {
            if (Utils.setSpinnerPosition((AppCompatSpinner) v.findViewById(R.id.acspGender), borrower.Gender.charAt(0), true) < 0) {
                Utils.alert(this, "Please check Gender, Cannot accept this Aadhar for Loan Application");
                return;
            }
        }

        tietAadharId.setText(borrower.aadharid);



        tietName.setText(borrower.getBorrowerName());

        tietAge.setText(String.valueOf(borrower.Age));
        Utils.setOnFocuseSelect(tietAge, "0");
        if (borrower.DOB == null) {
            tietDob.setText(DateUtils.getDobFrmAge(Integer.parseInt(tietAge.getText().toString())));
        } else {
            myCalendar.setTime(borrower.DOB);
            tietDob.setText(DateUtils.getFormatedDate(borrower.DOB, "dd-MMM-yyyy"));
        }

        tietGuardian.setText(borrower.getGurName());
        tietAddress1.setText(borrower.P_Add1);
        tietAddress2.setText(borrower.P_add2);
        tietAddress3.setText(borrower.P_add3);
        tietCity.setText(borrower.P_city);
        tietPinCode.setText(String.valueOf(borrower.p_pin));
        Utils.setOnFocuseSelect(tietPinCode, "0");



        if (borrower.RelationWBorrower != null) {
            Utils.setSpinnerPosition(acspRelationship, borrower.RelationWBorrower, false);
        }
        if (borrower.p_state != null) {
            Utils.setSpinnerPosition(acspAadharState, borrower.p_state);
        }


        if (borrower.isAadharVerified.equals("Q")) {

            tietName.setEnabled(false);
            if (Utils.NullIf(borrower.getGurName(), "").trim().length() > 0)
                tietGuardian.setEnabled(false);
            if (Utils.NullIf(borrower.Age, 0) > 0) {
                tietAge.setEnabled(false);
                tietDob.setEnabled(false);
            }
            acspGender.setEnabled(false);

            if (borrower.P_Add1.trim().length() > 0) tietAddress1.setEnabled(false);
            if (Utils.NullIf(borrower.P_add2.trim(), "").trim().length() > 2)
                tietAddress2.setEnabled(false);
            if (Utils.NullIf(borrower.P_add3.trim(), "").trim().length() > 2)
                tietAddress3.setEnabled(false);
            if (Utils.NullIf(borrower.P_city, "").trim().length() > 0) tietCity.setEnabled(false);
            if (Utils.NullIf(borrower.p_pin, 0) > 0) tietPinCode.setEnabled(false);
        }else{
            tietName.setEnabled(false);
            if (Utils.NullIf(borrower.getGurName(), "").trim().length() > 0)
                tietGuardian.setEnabled(false);
            if (Utils.NullIf(borrower.Age, 0) > 0) {
                tietAge.setEnabled(false);
                tietDob.setEnabled(false);
            }
            acspGender.setEnabled(false);
            if (borrower.P_Add1.trim().length() > 0) tietAddress1.setEnabled(false);
            if (Utils.NullIf(borrower.P_add2, "").trim().length() > 2)
                tietAddress2.setEnabled(false);
            if (Utils.NullIf(borrower.P_add3, "").trim().length() > 2)
                tietAddress3.setEnabled(false);
            if (Utils.NullIf(borrower.P_city, "").trim().length() > 0) tietCity.setEnabled(false);
            if (Utils.NullIf(borrower.p_pin, 0) > 0) tietPinCode.setEnabled(false);

        }

        tietMobile.setText(borrower.P_ph3);
        tietPanNo.setText(borrower.PanNO);
        tietVoterId.setText(borrower.voterid);
        tietDrivingLic.setText(borrower.drivinglic);


        if (borrower.Code > 0) {
            textViewFiDetails.setVisibility(View.VISIBLE);
           // textViewFiDetails.setText(borrower.Creator + " / " + borrower.Code);
            tietAadharId.setEnabled(false);
            imgViewScanQR.setVisibility(View.GONE);
            showSubmitBorrowerMenuItem = false;
            invalidateOptionsMenu();

        }

    }


    private void cardValidate(String id,String type,String bankIfsc,String dob) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Fetching Details");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ApiInterface apiInterface= getClientPan(SEILIGL.NEW_SERVERAPIAGARA).create(ApiInterface.class);
        Log.d("TAG", "checkCrifScore: "+getJsonOfString(id,type,bankIfsc,dob));
        requestforVerification= String.valueOf(getJsonOfString(id,type,bankIfsc,dob));
        Call<JsonObject> call=apiInterface.cardValidate(getJsonOfString(id,type,bankIfsc,dob));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                ResponseforVerification= String.valueOf(response.body().get("data"));
                saveVerficationLogs(IglPreferences.getPrefString(getApplicationContext(), SEILIGL.USER_ID, ""),type,requestforVerification,ResponseforVerification);
                if (type.equals("pancard")){
                    try {
                        tilPAN_Name.setVisibility(View.VISIBLE);
                        tilPAN_Name.setText(response.body().get("data").getAsJsonObject().get("name").getAsString());
                        panCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic_green));
                        panCheckSign.setEnabled(false);
                        isNameMatched="1";
                        isPanverify=1;
                    }catch (Exception e){
                        tilPAN_Name.setVisibility(View.VISIBLE);
                        tilPAN_Name.setText("Card Holder Name Not Found");
                        panCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));
                        panCheckSign.setEnabled(true);
                        isPanverify=0;
                    }
                    progressDialog.cancel();
                }else if(type.equals("voterid")){
                    try {
                        tilVoterId_Name.setVisibility(View.VISIBLE);
                        tilVoterId_Name.setText(response.body().get("data").getAsJsonObject().get("name").getAsString());
                        voterIdCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic_green));
                        voterIdCheckSign.setEnabled(false);
                        isNameMatched="1";
                        isVoterverify=1;
                    }catch (Exception e){
                        tilVoterId_Name.setVisibility(View.VISIBLE);
                        tilVoterId_Name.setText("Card Holder Name Not Found");
                        voterIdCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));
                        voterIdCheckSign.setEnabled(true);
                        isVoterverify=0;

                    }
                    progressDialog.cancel();

                }else if(type.equals("drivinglicense")){
                    try {
                        tilDL_Name.setVisibility(View.VISIBLE);
                        tilDL_Name.setText(response.body().get("data").getAsJsonObject().get("name").getAsString());
                        dLCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic_green));
                        dLCheckSign.setEnabled(false);
                        isNameMatched="1";
                        isDLverify=1;
                    }catch (Exception e){
                        tilDL_Name.setVisibility(View.VISIBLE);
                        tilDL_Name.setText("Card Holder Name Not Found");
                        dLCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));
                        dLCheckSign.setEnabled(true);
                        isDLverify=0;
                    }
                    progressDialog.cancel();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (type.equals("pancard")){
                    tilPAN_Name.setText(t.getMessage());
                    panCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));
                    progressDialog.cancel();

                }else{
                    tilVoterId_Name.setText(t.getMessage());
                    progressDialog.cancel();
                    voterIdCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));

                }
            }
        });
    }

    private JsonObject getJsonOfString(String id, String type,String bankIfsc,String userDOB) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("type",type);
        jsonObject.addProperty("txtnumber",id);
        jsonObject.addProperty("ifsc",bankIfsc);
        jsonObject.addProperty("userdob",userDOB);
        return  jsonObject;
    }

    private void saveVerficationLogs(String id,String type,String request,String response) {
        ApiInterface apiInterface= getClientPan(SEILIGL.NEW_SERVERAPI).create(ApiInterface.class);
        Call<JsonObject> call=apiInterface.kycVerficationlog(getJsonOfKyCLogs(id,type,request,response));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("TAG", "checkCrifScore: "+response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    private JsonObject getJsonOfKyCLogs(String id, String type,String bankIfsc,String userDOB) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("Type",type);
        jsonObject.addProperty("Userid",id);
        jsonObject.addProperty("Request",bankIfsc);
        jsonObject.addProperty("Response",userDOB);
        return  jsonObject;
    }
    public static Retrofit getClientPan(String BASE_URL) {
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

    public static String formatDate (String date, String initDateFormat, String endDateFormat) throws ParseException {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            /*
            case R.id.btnSubmitKyc:
                updateBorrower();
                break;
            case R.id.btnCancel:
                finish();
                break;
            case R.id.btnCapturePhoto:
                launchScanning();
                break;
                */
            case R.id.tietDob:
                Date dob = DateUtils.getParsedDate(tietDob.getText().toString(), "dd-MMM-yyyy");
                try{
                    if (!dob.equals(null)){
                        myCalendar.setTime(dob);
                    }
                }catch (Exception e){

                }

                new DatePickerDialog(this, dateSetListner,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
                break;
        }
    }
}