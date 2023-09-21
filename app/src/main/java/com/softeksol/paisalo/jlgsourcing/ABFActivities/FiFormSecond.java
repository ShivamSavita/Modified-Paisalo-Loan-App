package com.softeksol.paisalo.jlgsourcing.ABFActivities;

import static com.softeksol.paisalo.jlgsourcing.Utilities.CameraUtils.REQUEST_TAKE_PHOTO;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.DataAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softeksol.paisalo.dealers.Adapters.AddFemIncomeAdapter;
import com.softeksol.paisalo.jlgsourcing.ABFActivities.Adapter.OEMByDealerAdapter;
import com.softeksol.paisalo.jlgsourcing.ABFActivities.Model.BrandResponse;
import com.softeksol.paisalo.jlgsourcing.ABFActivities.Model.OEMByCreatorModel;
import com.softeksol.paisalo.jlgsourcing.BuildConfig;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.AadharUtils;
import com.softeksol.paisalo.jlgsourcing.Utilities.CameraUtils;
import com.softeksol.paisalo.jlgsourcing.Utilities.DateUtils;
import com.softeksol.paisalo.jlgsourcing.Utilities.MyTextWatcher;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.Utilities.Verhoeff;
import com.softeksol.paisalo.jlgsourcing.WebOperations;
import com.softeksol.paisalo.jlgsourcing.activities.ActivityManagerSelect;
import com.softeksol.paisalo.jlgsourcing.activities.ActivityOperationSelect;
import com.softeksol.paisalo.jlgsourcing.adapters.AdapterListRange;
import com.softeksol.paisalo.jlgsourcing.entities.AadharData;
import com.softeksol.paisalo.jlgsourcing.entities.BankData;
import com.softeksol.paisalo.jlgsourcing.entities.Borrower;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerExtra;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerFamilyExpenses;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerFamilyMember;
import com.softeksol.paisalo.jlgsourcing.entities.DocumentStore;
import com.softeksol.paisalo.jlgsourcing.entities.Guarantor;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory_Table;
import com.softeksol.paisalo.jlgsourcing.entities.dto.BorrowerDTO;
import com.softeksol.paisalo.jlgsourcing.entities.dto.DocumentStoreDTO;
import com.softeksol.paisalo.jlgsourcing.entities.dto.OperationItem;
import com.softeksol.paisalo.jlgsourcing.enums.EnumApiPath;
import com.softeksol.paisalo.jlgsourcing.enums.EnumFieldName;
import com.softeksol.paisalo.jlgsourcing.enums.EnumImageTags;
import com.softeksol.paisalo.jlgsourcing.handlers.AsyncResponseHandler;
import com.softeksol.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import cz.msebera.android.httpclient.Header;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FiFormSecond extends AppCompatActivity implements View.OnClickListener{
Button addFemIncomeBtn;
String borrowerProfilePic,nomineeProfilePic;
    private TextWatcher ageTextWatcher;

    String FatherFName,FatherLName,FatherMName,MotherFName,MotherLName,MotherMName,SpouseLName,SpouseMName,SpouseFName,VoterIdName,PANName,DLName,AadharName,spinReligion,spinCaste,spinPresentHouseOwner,spinResidingFrom,spinHouseType,spinRoofType,PlaceOfBirth,FamHeadIncome,Occupation,HouseRent;
    ApiInterface apiInterface;

    private DatePickerDialog.OnDateSetListener dateSetListner;
    private Uri uriPicture;
    private Calendar myCalendar;

AppCompatSpinner acspGenderNominee,acspRelationshipNominee,acspSelectOem,acspSelectDelear,acspSelectProduct;

ImageView imgViewAadharPhotoNominee,imgViewScanQRNominee;

TextView howOldAccount,tilBankAccountName,tvLoanAppFinanceBankName,tvLoanAppFinanceBankBranch;
RecyclerView addFemIncomeRecView;
Button BtnFinalSaveKYCDataABF,checkBankAccountNuber;
    protected int emailMobilePresent, imageStartIndex, imageEndIndex;

    ArrayList<BorrowerFamilyMember> borrowerFamilyMembersList =new ArrayList<>();

TextInputEditText tietDobNominee, tietAgeNominee,tietNameNominee,tietAadharNominee,textProductPrice, tietVoterNominee,tietMobileNominee, tietGuardianNominee,etLoanAppFinanceBankIfsc,etLoanAppFinanceBankAccountNo, tietIncomeMonthly,tietRentalIncome,tietFutureIncome,tietAgriIncome,tietIntrestIncome,tietOtherIncome,tietRentExpense,tietFoodExpense,tietEduExpense,tietHealthExpense,tietTravelExpense,tietSOcietyExpense,tietUtilExpense,tietOtherExpense;

    protected static final byte SEPARATOR_BYTE = (byte)255;
    protected ArrayList<String> decodedData;
    protected static final int VTC_INDEX = 15;
    protected String signature,email,mobile;
    Manager manager;
    Borrower borrower;
    Intent i;
int size=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fi_form_second);
        i=getIntent();
        manager = (Manager) getIntent().getSerializableExtra("manager");
        borrower = (Borrower) getIntent().getSerializableExtra("borrower");

        FatherFName=i.getStringExtra("FatherFName");
        FatherLName=i.getStringExtra("FatherLName");
        FatherMName=i.getStringExtra("FatherMName");
        MotherFName=i.getStringExtra("MotherFName");
        MotherLName=i.getStringExtra("MotherLName");
        MotherMName=i.getStringExtra("MotherMName");
        SpouseLName=i.getStringExtra("SpouseLName");
        SpouseMName=i.getStringExtra("SpouseMName");
        SpouseFName=i.getStringExtra("SpouseFName");
        VoterIdName=i.getStringExtra("VoterIdName");
        spinReligion=i.getStringExtra("spinReligion");
        spinCaste=i.getStringExtra("spinCaste");
        spinPresentHouseOwner=i.getStringExtra("spinPresentHouseOwner");
        spinResidingFrom=i.getStringExtra("spinResidingFrom");
        spinHouseType=i.getStringExtra("spinHouseType");
        spinRoofType=i.getStringExtra("spinRoofType");
        PlaceOfBirth=i.getStringExtra("PlaceOfBirth");
        FamHeadIncome=i.getStringExtra("FamHeadIncome");
        Occupation=i.getStringExtra("Occupation");
        HouseRent=i.getStringExtra("HouseRent");
        PANName=i.getStringExtra("PANName");
        DLName=i.getStringExtra("DLName");
        AadharName=i.getStringExtra("AadharName");


        Log.d("TAG", "onCreate: FatherFName"+FatherFName);
        Log.d("TAG", "onCreate: FatherLName"+FatherLName);
        Log.d("TAG", "onCreate: FatherMName"+FatherMName);
        Log.d("TAG", "onCreate: MotherFName"+MotherFName);
        Log.d("TAG", "onCreate: MotherLName"+MotherLName);
        Log.d("TAG", "onCreate: MotherMName"+MotherMName);
        Log.d("TAG", "onCreate: SpouseLName"+SpouseLName);
        Log.d("TAG", "onCreate: SpouseMName"+SpouseMName);
        Log.d("TAG", "onCreate: SpouseFName"+SpouseFName);
        Log.d("TAG", "onCreate: VoterIdName"+VoterIdName);
        Log.d("TAG", "onCreate: PANName"+PANName);
        Log.d("TAG", "onCreate: DLName"+DLName);
        Log.d("TAG", "onCreate: AadharName"+AadharName);
        Log.d("TAG", "onCreate: spinReligion"+spinReligion);
        Log.d("TAG", "onCreate: spinCaste"+spinCaste);
        Log.d("TAG", "onCreate: spinPresentHouseOwner"+spinPresentHouseOwner);
        Log.d("TAG", "onCreate: spinResidingFrom"+spinResidingFrom);
        Log.d("TAG", "onCreate: spinHouseType"+spinHouseType);
        Log.d("TAG", "onCreate: spinRoofType"+spinRoofType);
        Log.d("TAG", "onCreate: PlaceOfBirth"+PlaceOfBirth);
        Log.d("TAG", "onCreate: FamHeadIncome"+FamHeadIncome);
        Log.d("TAG", "onCreate: Occupation"+Occupation);
        Log.d("TAG", "onCreate: HouseRent"+HouseRent);




        addFemIncomeBtn=findViewById(R.id.addFemIncomeBtn);
        addFemIncomeRecView=findViewById(R.id.addFemIncomeRecView);
        addFemIncomeRecView.setLayoutManager(new LinearLayoutManager(this));
        tietIncomeMonthly=findViewById(R.id.tietIncomeMonthly);
        tietRentalIncome=findViewById(R.id.tietRentalIncome);
        tietFutureIncome=findViewById(R.id.tietFutureIncome);
        tietAgriIncome=findViewById(R.id.tietAgriIncome);
        tietIntrestIncome=findViewById(R.id.tietIntrestIncome);
        tietOtherIncome=findViewById(R.id.tietOtherIncome);
        tietRentExpense=findViewById(R.id.tietRentExpense);
        tietFoodExpense=findViewById(R.id.tietFoodExpense);
        tietEduExpense=findViewById(R.id.tietEduExpense);
        tietHealthExpense=findViewById(R.id.tietHealthExpense);
        tietTravelExpense=findViewById(R.id.tietTravelExpense);
        tietSOcietyExpense=findViewById(R.id.tietSOcietyExpense);
        tietUtilExpense=findViewById(R.id.tietUtilExpense);
        tietOtherExpense=findViewById(R.id.tietOtherExpense);
        acspRelationshipNominee=findViewById(R.id.acspRelationshipNominee);
        acspGenderNominee=findViewById(R.id.acspGenderNominee);
        imgViewScanQRNominee=findViewById(R.id.imgViewScanQRNominee);
        imgViewAadharPhotoNominee=findViewById(R.id.imgViewAadharPhotoNomineeNominee);
        tilBankAccountName=findViewById(R.id.tilBankAccountName);
        howOldAccount=findViewById(R.id.howOldAccount);
        tvLoanAppFinanceBankName=findViewById(R.id.tvLoanAppFinanceBankName);
        tvLoanAppFinanceBankBranch=findViewById(R.id.tvLoanAppFinanceBankBranch);
        tietVoterNominee=findViewById(R.id.tietVoterNominee);
        tietMobileNominee=findViewById(R.id.tietMobileNominee);
        tietGuardianNominee=findViewById(R.id.tietGuardianNominee);
        etLoanAppFinanceBankIfsc=findViewById(R.id.etLoanAppFinanceBankIfsc);
        etLoanAppFinanceBankAccountNo=findViewById(R.id.etLoanAppFinanceBankAccountNo);
        acspSelectOem=findViewById(R.id.acspSelectOem);
        acspSelectDelear=findViewById(R.id.acspSelectDelear);
        acspSelectProduct=findViewById(R.id.acspSelectProduct);
        textProductPrice=findViewById(R.id.textProductPrice);
        BtnFinalSaveKYCDataABF=findViewById(R.id.BtnFinalSaveKYCDataABF);
        tietAadharNominee=findViewById(R.id.tietAadharNominee);
        tietNameNominee=findViewById(R.id.tietNameNominee);
        tietAgeNominee=findViewById(R.id.tietAgeNominee);
        tietDobNominee=findViewById(R.id.tietDobNominee);
        checkBankAccountNuber=findViewById(R.id.checkBankAccountNuber);
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
      //  apiInterface=new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());
        List<RangeCategory> genders = new ArrayList<>();
        Log.d("TAG", "onCreate: "+manager.Creator+"///"+SEILIGL.NEW_TOKEN);
        Call<BrandResponse> getCreatorByOem=apiInterface.getOEMbyCreator(SEILIGL.NEW_TOKEN, "VHDELHI");
        tietAgeNominee.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((TextInputEditText) v).addTextChangedListener(ageTextWatcher);
                } else {
                    ((TextInputEditText) v).removeTextChangedListener(ageTextWatcher);
                }
            }
        });
        tietDobNominee.setOnClickListener(this);
        dateSetListner = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                tietAgeNominee.clearFocus();
                myCalendar.set(year, monthOfYear, dayOfMonth);
                tietAgeNominee.setText(String.valueOf(DateUtils.getAge(myCalendar)));
                tietDobNominee.setText(DateUtils.getFormatedDate(myCalendar.getTime(), "dd-MMM-yyyy"));
            }
        };
        tietAgeNominee.addTextChangedListener(new MyTextWatcher(tietAgeNominee) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
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
                tietDobNominee.setText(dtString);
            }
        };
        getCreatorByOem.enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                Log.d("TAG", "onResponse: "+response.body());
                BrandResponse brandResponse=response.body();
                Gson gson = new Gson();
                Log.d("TAG", "onResponse: +"+brandResponse.getData());
                OEMByCreatorModel[] nameList = gson.fromJson(brandResponse.getData(), OEMByCreatorModel[].class);
                acspSelectOem.setAdapter(new OEMByDealerAdapter(FiFormSecond.this,nameList));
            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {

            }
        });
        howOldAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(FiFormSecond.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                String dateInString = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                try {
                                    Date date = formatter.parse(dateInString);
                                    Log.e("DATATIMEhowOldAccountSTRING",formatter.format(date));
                                    howOldAccount.setText(formatter.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                // Log.e("DATATIMEhowOldAccountNEWWDATA",DateUtils.getsmallDate(howOldAccount.getText().toString(),"dd-MM-yyyy")+"");


                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();




            }
        });
        imgViewAadharPhotoNominee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ImagePicker.with(FiFormSecond.this)
                        .cameraOnly()
                        .start(REQUEST_TAKE_PHOTO);
            }
        });

        genders.add(new RangeCategory("Female", "Gender"));
        genders.add(new RangeCategory("Male", "Gender"));
        genders.add(new RangeCategory("Transgender", "Gender"));

        acspGenderNominee.setAdapter(new AdapterListRange(this, genders, false));

        ArrayList<RangeCategory> relationSips = new ArrayList<>();
        relationSips.add(new RangeCategory("Husband", ""));
        relationSips.add(new RangeCategory("Father", ""));
        relationSips.add(new RangeCategory("Mother", ""));

        acspRelationshipNominee.setAdapter(new AdapterListRange(this, relationSips, false));
        checkBankAccountNuber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etLoanAppFinanceBankAccountNo.getText().toString().equals("") && !etLoanAppFinanceBankIfsc.getText().toString().equals("")){
                    cardValidate(etLoanAppFinanceBankAccountNo.getText().toString().trim(),etLoanAppFinanceBankIfsc.getText().toString().trim());
                }else{
                    Toast.makeText(FiFormSecond.this, "Please enter account number and IFCS code Properly", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addFemIncomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                showFamilyMemberDialog();


                addFemIncomeRecView.setAdapter(new AddFemIncomeAdapter(FiFormSecond.this,borrowerFamilyMembersList));
                new AddFemIncomeAdapter(FiFormSecond.this,borrowerFamilyMembersList).notifyDataSetChanged();
                Toast.makeText(FiFormSecond.this, "New Member Added!!", Toast.LENGTH_SHORT).show();
            }
        });
        etLoanAppFinanceBankIfsc.addTextChangedListener(new MyTextWatcher(etLoanAppFinanceBankIfsc) {
            @Override
            public void validate(EditText editText, String text) {
                validateIFSC(editText, text);
            }
        });
        imgViewScanQRNominee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(FiFormSecond.this);
                scanIntegrator.setOrientationLocked(false);
                scanIntegrator.initiateScan(Collections.singleton("QR_CODE"));
            }
        });

        BtnFinalSaveKYCDataABF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (validateAllFields())
                {
                    if (borrower != null) {

                        borrower.bank_ac_no = Utils.getNotNullText(etLoanAppFinanceBankAccountNo);
                        borrower.BankAcOpenDt = DateUtils.getParsedDate(howOldAccount.getText().toString(),"dd-MM-yyyy");

                        borrower.Enc_Property = Utils.getNotNullText(etLoanAppFinanceBankIfsc);
//            borrower.fiExtraBank.setBankCif(Utils.getNotNullText(etCIF));
                        borrower.BankName = Utils.getNotNullText((TextView) findViewById(R.id.tvLoanAppFinanceBankName));
                        borrower.Bank_Address = Utils.getNotNullText((TextView) findViewById(R.id.tvLoanAppFinanceBankBranch));
                        borrower.Oth_Prop_Det = null;
                        borrower.save();
                        borrower.fiExtraBank.setMotherName(MotherFName);
                        borrower.fiExtraBank.setFatherName(FatherFName);
                        borrower.Enc_Property=etLoanAppFinanceBankAccountNo.getText().toString().trim();
                        borrower.bank_ac_no=etLoanAppFinanceBankAccountNo.getText().toString().trim();


//                       String occCode = Utils.getSpinnerStringValue(acspOccupation);
                        borrower.fiExtraBank.setCkycOccupationCode(Occupation);
                        borrower.associateExtraBank(borrower.fiExtraBank);
                        borrower.fiExtraBank.save();
                        BorrowerDTO borrowerDTO = new BorrowerDTO(borrower);
                        borrowerDTO.fiFamExpenses = null;
                        borrowerDTO.fiExtra = null;
                        Log.d("TAG", "onSuccess1: "+borrower.fiExtraBank.getActivityType());
                        Log.d("TAG", "onSuccess1: "+borrower.fiExtraBank.getCode());
                        Log.d("TAG", "onSuccess1: "+borrower.fiExtraBank);
                        Log.d("TAG", "onSuccess1: "+WebOperations.convertToJson(borrower.fiExtraBank));

                        AsyncResponseHandler dataAsyncResponseHandler = new AsyncResponseHandler(FiFormSecond.this, "Loan Financing\nSubmittiong Loan Application", "Submitting Borrower Information") {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String jsonString = new String(responseBody);
                                Log.d("Response Data",jsonString);
                                try {

                                    JSONObject jo = new JSONObject(jsonString);
                                    long FiCode = jo.getLong("FiCode");
                                    borrower.updateFiCode(FiCode, borrower.Tag);
                                    borrower.Oth_Prop_Det = null;
                                    BorrowerFamilyExpenses expense=new BorrowerFamilyExpenses();
                                    expense.setRent(Integer.valueOf(tietRentExpense.getText().toString()));
                                    expense.setFooding(Integer.valueOf(tietFoodExpense.getText().toString()));
                                    expense.setEducation(Integer.valueOf(tietEduExpense.getText().toString()));
                                    expense.setHealth(Integer.valueOf(tietHealthExpense.getText().toString()));
                                    expense.setTravelling(Integer.valueOf(tietTravelExpense.getText().toString()));
                                    expense.setEntertainment(0);
                                    expense.setOthers(Integer.valueOf(tietOtherExpense.getText().toString()));
                                    expense.setHomeType(spinHouseType);
                                    expense.setHomeRoofType(spinRoofType);
                                    expense.setToiletType("");
                                    expense.setLivingWSpouse("");
                                    borrower.associateBorrowerFamilyExpenses(expense);
                                    expense.save();
                                    borrower.fiGuarantors=borrower.getFiGuarantors();
                                    borrower.save();

//                                    fiDocGeoLoc=new FiDocGeoLoc(FiCode,borrower.Creator,isAdhaarEntry,isNameMatched);
//                                    fiDocGeoLoc.save();


                                    BorrowerExtra borrowerExtra=new BorrowerExtra(FiCode,manager.Creator,Utils.getNotNullInt(tietIncomeMonthly),Utils.getNotNullInt(tietFutureIncome),Utils.getNotNullText(tietAgriIncome),Utils.getNotNullText(tietOtherIncome),"",0,MotherFName,MotherLName,MotherMName, FatherFName,FatherLName, FatherMName,borrower.Tag,SpouseLName,SpouseMName,SpouseFName,0,Utils.getNotNullInt(tietIntrestIncome),PlaceOfBirth);

//                                    BorrowerExtraBank borrowerExtraBank=new BorrowerExtraBank(manager.Creator,manager.TAG,FiCode);

//                                    borrower.associateExtraBank(borrowerExtraBank);

                                    borrower.fiExtra=borrowerExtra;


                                    for (BorrowerFamilyMember b:borrowerFamilyMembersList) {
                                        b.associateBorrower(borrower);
                                        b.save();
                                    }



                                    Log.d("TAG", "onSuccess borrower fiid: "+borrower.FiID);
                                    Log.d("TAG", "onSuccess borrower fiid: "+borrower.getFiFamilyMembers().size());


                                    Log.d("TAG", "onSuccess: famMemDetails "+WebOperations.convertToJson(borrower.fiFamMems));


                                    borrower.fiExtra.save();
//
                                    borrower.save();
                                    Log.d("TAG", "onSuccess: "+borrower.fiExtra);
                                    Log.d("TAG", "onSuccess: "+WebOperations.convertToJson(borrower.fiExtraBank));
                                    Log.d("TAG", "onSuccess: "+WebOperations.convertToJson(borrower.fiGuarantors));
                                    Log.d("TAG", "onSuccess: "+WebOperations.convertToJson(borrower));


                                    AsyncResponseHandler dataAsyncResponseHandlerUpdateFI = new AsyncResponseHandler(FiFormSecond.this, "Loan Financing\nSubmittiong Loan Application","Submitting Borrower Information") {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            String jsonString = new String(responseBody);
                                            //Log.d("Response Data",jsonString);
                                            Utils.showSnakbar(findViewById(android.R.id.content), "Borrower Loan Application Saved");

                                            try {
                                                JSONObject jo = new JSONObject(jsonString);

                                                Guarantor guarantor=new Guarantor();
                                                guarantor.setName(tietNameNominee.getText().toString());
                                                guarantor.setAadharid(tietAadharNominee.getText().toString());
                                                guarantor.setAge(Integer.parseInt(tietAgeNominee.getText().toString()));
                                                guarantor.setFi_Code(borrower.Code);

                                                guarantor.setCreator(borrower.Creator);

                                                guarantor.setDOB(myCalendar.getTime());
                                                guarantor.setGender(Utils.getSpinnerStringValue(acspGenderNominee));
                                                guarantor.setGurName(tietGuardianNominee.getText().toString());
                                                guarantor.setRelation(Utils.getSpinnerStringValue(acspRelationshipNominee));
                                                guarantor.setVoterid(tietVoterNominee.getText().toString());
                                                guarantor.setPicture(nomineeProfilePic);
                                                guarantor.associateBorrower(borrower);
                                                List<Guarantor> guarantorArrayList=new ArrayList<>();
                                                guarantorArrayList.add(guarantor);
                                                Log.d("CHeckJsonFinancing",jo+"");
                                                Log.d("CHeckJsonFinancing1",jsonString+"");

                                                saveDataOfGuarntor(guarantorArrayList,borrower);

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


                                    (new WebOperations()).postEntity(FiFormSecond.this, "posfi", "updatefi", WebOperations.convertToJson(borrower), dataAsyncResponseHandlerUpdateFI);


                                    AlertDialog.Builder builder = new AlertDialog.Builder(FiFormSecond.this);
                                    builder.setTitle("Borrower KYC");
                                    builder.setCancelable(false);
                                    builder.setMessage("KYC Saved with " + manager.Creator + " / " + FiCode + "\nPlease capture / scan documents");
                                    builder.setPositiveButton("Want to E-Sign", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            OperationItem operationItem=new OperationItem(6, "E-Sign", R.color.colorMenuPremature, "POSDB", "Getmappedfo");

                                            Intent intent = new Intent(FiFormSecond.this, ActivityManagerSelect.class);
                                            intent.putExtra(Global.OPTION_ITEM, operationItem);
                                            intent.putExtra("Title", operationItem.getOprationName());
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent=new Intent(FiFormSecond.this, ActivityOperationSelect.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    builder.create().show();

                                    UpdatefiVerificationDocName(FiCode,manager.Creator);

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

                    }else{
                        Utils.alert(FiFormSecond.this, "There is at least one errors in the above data");



                    }

                }else  {
                    Utils.alert(FiFormSecond.this, "There is at least one errors in the above data");





                }

            }
        });
    }

    private void saveDataOfImages(Borrower borrower, String borrowerProfilePic,String imgTag) {
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

        documentStore.Creator = borrower.Creator;
        documentStore.ficode = borrower.Code;
        documentStore.fitag = borrower.Tag;

        documentStore.remarks = "Picture";
        documentStore.checklistid = 0;
        documentStore.userid = borrower.UserID;
        documentStore.latitude = 0;
        documentStore.longitude = 0;
        documentStore.DocId = 0;
        documentStore.FiID =0;
        documentStore.updateStatus = false;
        //documentStore.imagePath = mDocumentStore.imagePath;
        //documentStore.imagePath = "file:" + mDocumentStore.imagePath;
        if (imgTag.equals("B")){
            documentStore.GuarantorSerial = 0;
            documentStore.imageTag = EnumImageTags.Borrower.getImageTag();
            documentStore.fieldname = EnumFieldName.Borrower.getFieldName();
            documentStore.apiRelativePath = EnumApiPath.BorrowerApiJson.getApiPath();
        }else{
            documentStore.GuarantorSerial = 1;
            documentStore.imageTag = EnumImageTags.Guarantor.getImageTag();
            documentStore.fieldname = EnumFieldName.Guarantor.getFieldName();
            documentStore.apiRelativePath = EnumApiPath.GuarantorApi.getApiPath();
        }


        documentStore.imagePath = borrowerProfilePic;
        Toast.makeText(this, documentStore.imagePath+"", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "saveDataOfImages: "+documentStore.imagePath);



        DataAsyncResponseHandler responseHandler = new DataAsyncResponseHandler(FiFormSecond.this, "Loan Financing", "Uploading " + DocumentStore.getDocumentName(documentStore.checklistid)) {
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
       // Log.d("Document Json",jsonString);
        String apiPath = documentStore.checklistid == 0 ? "/api/uploaddocs/savefipicjson" : "/api/uploaddocs/savefidocsjson";
        (new WebOperations()).postEntity(FiFormSecond.this, BuildConfig.BASE_URL + apiPath, jsonString, responseHandler);










    }

    private void saveDataOfGuarntor(List<Guarantor> guarantorArrayList ,Borrower borrower) {
        final AsyncResponseHandler guarantorAsyncResponseHandler = new AsyncResponseHandler(FiFormSecond.this, "Loan Financing\nSubmittiong Loan Application", "Updating Guarantor Information") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String jsonString = new String(responseBody);
                Log.d("Response Data",jsonString);
                Utils.showSnakbar(findViewById(android.R.id.content), "Guarantors saved with Loan Application Saved");

                saveDataOfImages(borrower,borrower.getPicture().getPath(),"B");
                saveDataOfImages(borrower,nomineeProfilePic,"N");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                Log.d("TAG", "onFailure: "+error.getMessage()+responseBody);
            }
        };
        Log.d("TAG", "onClick: "+ WebOperations.convertToJson(guarantorArrayList));
        (new WebOperations()).postEntity(FiFormSecond.this, "posguarantor", "saveguarantors", WebOperations.convertToJson(guarantorArrayList), guarantorAsyncResponseHandler);

    }

    private void UpdatefiVerificationDocName(long fiCode, String creator) {
        ApiInterface apiInterface= ApiClient.getClient(SEILIGL.NEW_SERVERAPI).create(ApiInterface.class);
        Log.d("TAG", "checkCrifScore: "+getJsonOfDocName(fiCode, creator));
        Call<JsonObject> call=apiInterface.getDocNameDate(getJsonOfDocName(fiCode,creator));
        call.enqueue(new Callback<JsonObject>(){
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response){
                Log.d("TAG", "onResponse: "+response.body());
                if(response.body() != null){

                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());

            }
        });
    }
    private JsonObject getJsonOfDocName(long fiCode, String creator) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("type","basic");
        jsonObject.addProperty("pan_Name",PANName);
        jsonObject.addProperty("voterId_Name",VoterIdName);
        jsonObject.addProperty("aadhar_Name",AadharName);
        jsonObject.addProperty("drivingLic_Name",DLName);
        jsonObject.addProperty("bankAcc_Name","");
        jsonObject.addProperty("bank_Name","");
        jsonObject.addProperty("fiCode",fiCode+"");
        jsonObject.addProperty("creator",creator);
        return jsonObject;
    }

    private boolean validateAllFields() {
         if (Objects.requireNonNull(tietIncomeMonthly.getText()).length()<1){
             tietIncomeMonthly.setError("Please enter personal income");
             return false;
         }else if (Objects.requireNonNull(tietRentalIncome.getText()).length()<1){
             tietRentalIncome.setError("Please enter rental income");
             return false;
         }else if (Objects.requireNonNull(tietFutureIncome.getText()).length()<1){
             tietFutureIncome.setError("Please enter future income");
             return false;
         }else  if (Objects.requireNonNull(tietAgriIncome.getText()).length()<1){
             tietAgriIncome.setError("Please enter agriculture income");
             return false;
         }else if (Objects.requireNonNull(tietIntrestIncome.getText()).length()<1){
             tietIntrestIncome.setError("Please enter interest income");
             return false;
         }else if (Objects.requireNonNull(tietOtherIncome.getText()).length()<1){
             tietOtherIncome.setError("Please enter other income");
             return false;
         }else if (Objects.requireNonNull(tietRentExpense.getText()).length()<1){
             tietRentExpense.setError("Please enter rent expense");
             return false;
         }else if (Objects.requireNonNull(tietFoodExpense.getText()).length()<1){
             tietFoodExpense.setError("Please enter food expense");
             return false;
         }else if (Objects.requireNonNull(tietEduExpense.getText()).length()<1){
             tietEduExpense.setError("Please enter education expense");
             return false;
         }else if (Objects.requireNonNull(tietHealthExpense.getText()).length()<1){
             tietHealthExpense.setError("Please enter health expense");
             return false;
         }else if (Objects.requireNonNull(tietUtilExpense.getText()).length()<1){
             tietUtilExpense.setError("Please enter utility expense");
             return false;
         }else if (Objects.requireNonNull(tietOtherExpense.getText()).length()<1){
             tietOtherExpense.setError("Please enter other expense");
             return false;
         }else if (Objects.requireNonNull(etLoanAppFinanceBankAccountNo.getText()).length()<1){
             etLoanAppFinanceBankAccountNo.setError("Please enter bank account number");
             return false;
         }else if (Objects.requireNonNull(etLoanAppFinanceBankIfsc.getText()).length()<1){
             etLoanAppFinanceBankIfsc.setError("Please enter bank IFSC");
             return false;
         }else if (Objects.requireNonNull(tietAadharNominee.getText()).length()<1){
             tietAadharNominee.setError("Please enter nominee aadhaar id");
             return false;
         }else if (Objects.requireNonNull(tietNameNominee.getText()).length()<1){
             tietNameNominee.setError("Please enter nominee name");
             return false;
         }else if (Objects.requireNonNull(tietAgeNominee.getText()).length()<1){
             tietAgeNominee.setError("Please enter nominee age");
             return false;
         }else if (Objects.requireNonNull(tietDobNominee.getText()).length()<1){
             tietDobNominee.setError("Please enter nominee DOB");
             return false;
         }else if (Objects.requireNonNull(tietGuardianNominee.getText()).length()<1){
             tietGuardianNominee.setError("Please enter nominee Father Name");
             return false;
         }else if (Objects.requireNonNull(tietMobileNominee.getText()).length()<1){
             tietMobileNominee.setError("Please enter nominee mobile number");
             return false;
         }else if (nomineeProfilePic==null ){
            Toast.makeText(getApplicationContext(), "Please Capture Borrower Picture First!!", Toast.LENGTH_SHORT).show();
             return false;
        }

        return true;

    }

    private void validateIFSC(final EditText editText, String text) {
        editText.setError(null);
        if (editText.length() != 11) {
            editText.setError("Must be 11 Character");
        } else if (!Verhoeff.validateIFSC(text)) {
            editText.setError("Please input a valid IFSC. " + " " + text + "is not a valid IFSC");
        } else {
            DataAsyncHttpResponseHandler dataAsyncHttpResponseHandler = new DataAsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String jsonString = new String(responseBody);
                    try {
                        TextView tvBankName = (TextView) findViewById(R.id.tvLoanAppFinanceBankName);
                        TextView tvBankBranch = (TextView) findViewById(R.id.tvLoanAppFinanceBankBranch);
                        tvBankName.setText("");
                        tvBankBranch.setText("");
                        BankData bankData = WebOperations.convertToObject(jsonString, BankData.class);
                        if (bankData.BANK == null) {
                            editText.setError("This IFSC not available");
                        } else
                          //  if (borrower != null)
                            {
                          //  borrower.BankName = bankData.BANK.replace("á", "").replace("┬", "").replace("û", "").replace("ô", "");
                          //  borrower.Bank_Address = bankData.ADDRESS.replace("á", "").replace("┬", "").replace("û", "").replace("ô", "");
                            tvBankName.setText(bankData.BANK.replace("á", "").replace("┬", "").replace("û", "").replace("ô", ""));
                            tvBankBranch.setText(bankData.ADDRESS.replace("á", "").replace("┬", "").replace("û", "").replace("ô", ""));
                        }
                    } catch (NullPointerException ne) {

                    } catch (Exception e) {
                        editText.setError("Try Again");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(FiFormSecond.this, "IFSC not found ", Toast.LENGTH_LONG).show();
                }
            };
            RequestParams params = new RequestParams();
            params.add("IFSC", text);
            (new WebOperations()).getEntity(FiFormSecond.this, "bankname", "getbankname", params, dataAsyncHttpResponseHandler);
        }
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
                    tietDobNominee.setEnabled(retVal);
                }catch (Exception e){
                    editText.setError("Age should be greater than 17");
                }
                break;

        }
        return retVal;
    }

    private void cardValidate(String id,String bankIfsc) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Fetching Details");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Retrofit retrofit = null;

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


            httpClient.connectTimeout(1, TimeUnit.MINUTES);
            httpClient.readTimeout(1,TimeUnit.MINUTES);
            httpClient.addInterceptor(logging);
            retrofit = new Retrofit.Builder()
                    .baseUrl(SEILIGL.NEW_SERVERAPIAGARA)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();


        ApiInterface apiInterface= retrofit.create(ApiInterface.class);
        Call<JsonObject> call=apiInterface.cardValidate(getJsonOfString(id,bankIfsc));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    tilBankAccountName.setVisibility(View.VISIBLE);

                    tilBankAccountName.setText(response.body().get("data").getAsJsonObject().get("full_name").getAsString());
                    checkBankAccountNuber.setBackground(getResources().getDrawable(R.drawable.check_sign_ic_green));
                    checkBankAccountNuber.setEnabled(false);
                }catch (Exception e){
                    tilBankAccountName.setVisibility(View.VISIBLE);
                    tilBankAccountName.setText("Card Holder Name Not Found");
                    checkBankAccountNuber.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));
                    checkBankAccountNuber.setEnabled(true);

                }
                progressDialog.cancel();


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                tilBankAccountName.setText(t.getMessage());
                progressDialog.cancel();
                checkBankAccountNuber.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));



            }
        });
    }


    private JsonObject getJsonOfString(String id, String bankIfsc) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("type","bankaccount");
        jsonObject.addProperty("txtnumber",id);
        jsonObject.addProperty("ifsc",bankIfsc);
        jsonObject.addProperty("userdob","");
        return  jsonObject;
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
        }
        else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {


            if (data != null) {
                uriPicture = data.getData();
                CropImage.activity(uriPicture)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(45, 52)
                        .start(  FiFormSecond.this);
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

                         
                                (new File(uriPicture.getPath())).delete();
                                try {


                                    File croppedImage = CameraUtils.moveCachedImage2Storage(this, tempCroppedImage, true);

//                                    //Log.e("CroppedImageMyBitmap", bitmap+ "");
                                    Log.e("CroppedImageFile1", croppedImage.getPath()+"");
                                    Log.e("CroppedImageFile2", croppedImage.getAbsolutePath()+"");
                                    Log.e("CroppedImageFile3", croppedImage.getCanonicalPath()+"");
                                    Log.e("CroppedImageFile4", croppedImage.getParent()+"");
                                    Log.e("CroppedImageFile5", croppedImage.getParentFile().getCanonicalPath()+"");
                                    Log.e("CroppedImageFile6", croppedImage.getParentFile().getName()+"");

                                    nomineeProfilePic=croppedImage.getPath();
                                    Log.d("TAG", "onActivityResult: "+nomineeProfilePic);

                                    setImagepath(croppedImage);


                                } catch (IOException e) {
                                    e.printStackTrace();
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

    private void setImagepath(File file) {

//        File imgFile = new  File("/sdcard/Images/test_image.jpg");

//        customProgress.hideProgress();
        // Toast.makeText(this, "Checking File: "+file.getAbsolutePath()+"", Toast.LENGTH_SHORT).show();

        if (file.length() != 0) {

            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

            if (myBitmap != null) {
                imgViewAadharPhotoNominee.setImageBitmap(myBitmap);
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

                    return;
                }
                tietAadharNominee.setText(aadharData.AadharId);
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


            tietNameNominee.setText(aadharData.Name);
            tietDobNominee.setText(aadharData.DOB.toString());
            tietAgeNominee.setText(aadharData.Age);
            //borrower.Gender = aadharData.Gender;
            tietGuardianNominee.setText(aadharData.GurName==null?"":aadharData.GurName);


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

            tietAgeNominee.setText(String.valueOf(period.getYears()));
            //System.out.print(period.getYears()+" years "+period.getMonths()+" and "+period.getDays()+" days");
        }



        Utils.setOnFocuseSelect(tietAgeNominee, "0");
        if (date == null) {
            tietDobNominee.setText(DateUtils.getDobFrmAge(Integer.parseInt(tietAgeNominee.getText().toString())));
        } else {
            myCalendar.setTime(date);
            tietDobNominee.setText(DateUtils.getFormatedDate(date, "dd-MMM-yyyy"));
        }


        tietAadharNominee.setText(decodedData.get(2-inc).toString());
       // borrower.Gender = decodedData.get(5-inc);

        if (decodedData.get(5-inc) != null) {
            if (Utils.setSpinnerPosition((AppCompatSpinner)  findViewById(R.id.acspGenderNominee), decodedData.get(5-inc), true) < 0) {
                Utils.alert(this, "Please check Gender, Cannot accept this Aadhar for Loan Application");
            }
        }

        if (decodedData.get(3-inc).equals("")||decodedData.get(3-inc).equals(null)){
            //tietName.setEnabled(true);
        }else{
            tietNameNominee.setText(decodedData.get(3-inc).toString());
        }
        if (decodedData.get(4-inc).equals("")||decodedData.get(4-inc).equals(null)){
            //tietDob.setEnabled(true);
        }else{
            tietDobNominee.setText(DateUtils.getFormatedDate(date, "dd-MMM-yyyy"));
            myCalendar.setTime(date);
           // borrower.DOB = date;
        }
        if (decodedData.get(6-inc).equals("")||decodedData.get(6-inc).equals(null)){

        }else{
            tietGuardianNominee.setText(decodedData.get(6-inc));

        }




        //borrower.P_city = decodedData.get(7);

        // borrower.P_Add1 = decodedData.get(9);
        // borrower.P_add2 = decodedData.get(8);
        // borrower.P_add3 = decodedData.get(10);

     //   setDataToView(this.findViewById(android.R.id.content).getRootView());
      ///  validateBorrower();
        tietAgeNominee.setEnabled(false);
        tietDobNominee.setEnabled(false);
        tietNameNominee.setEnabled(false);
        tietGuardianNominee.setEnabled(false);
     //   acspAadharState.setEnabled(false);
        acspGenderNominee.setEnabled(false);


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
    private void showFamilyMemberDialog() {
        final View dialogView = this.getLayoutInflater().inflate(R.layout.fam_mem_income_dialoge, null);
        Button femDialogdeleteBtn=dialogView.findViewById(R.id.femDialogdeleteBtn);
        AppCompatSpinner acspRelationship=dialogView.findViewById(R.id.acspRelationship);
        AppCompatSpinner acspEducationDetail=dialogView.findViewById(R.id.acspEducationDetail);
        AppCompatSpinner acspBussinessType=dialogView.findViewById(R.id.acspBussinessType);
        AppCompatSpinner spinIncomeFamMemType=dialogView.findViewById(R.id.spinIncomeFamMemType);
        AppCompatSpinner acspGenderFememem=dialogView.findViewById(R.id.acspGenderFememem);
        EditText spinIncomeFamMem=dialogView.findViewById(R.id.acspIncomeFamMem);
        EditText tietFamMemName=dialogView.findViewById(R.id.tietFamMemName);
        EditText txtFemMemBussiness=dialogView.findViewById(R.id.txtFemMemBussiness);
        Button femDialogaddBtn=dialogView.findViewById(R.id.femDialogaddBtn);
        List<RangeCategory> genders = new ArrayList<>();
        genders.add(new RangeCategory("Female", "Gender"));
        genders.add(new RangeCategory("Male", "Gender"));
        genders.add(new RangeCategory("Transgender", "Gender"));

        acspGenderFememem.setAdapter(new AdapterListRange(this, genders, false));
        ArrayList<RangeCategory> relationSips = new ArrayList<>();
        relationSips.add(new RangeCategory("Husband", ""));
        relationSips.add(new RangeCategory("Father", ""));
        relationSips.add(new RangeCategory("Mother", ""));
        acspEducationDetail.setAdapter(new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("education")).queryList(), false));
        acspBussinessType.setAdapter(new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("loan_purpose")).queryList(), false));
        acspRelationship.setAdapter(new AdapterListRange(this, relationSips, false));
        spinIncomeFamMemType.setAdapter(new AdapterListRange(this, RangeCategory.getRangesByCatKey("income-type"), false));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        femDialogdeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        femDialogaddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tietFamMemName.getText().toString().length()<1){
                    tietFamMemName.setError("Please enter member name");

                }else if (txtFemMemBussiness.getText().toString().length()<1){
                    txtFemMemBussiness.setError("Please enter member business");

                }else if (spinIncomeFamMem.getText().toString().length()<1){
                    spinIncomeFamMem.setError("Please enter member income");

                }else  {




                    BorrowerFamilyMember borrowerFamilyMember=new BorrowerFamilyMember();
                    borrowerFamilyMember.setMemName(tietFamMemName.getText().toString());
                    borrowerFamilyMember.setRelationWBorrower(Utils.getSpinnerStringValue(acspRelationship));
                    borrowerFamilyMember.setGender(Utils.getSpinnerStringValue(acspGenderFememem));
                    borrowerFamilyMember.setEducatioin(Utils.getSpinnerStringValue(acspEducationDetail));
                    borrowerFamilyMember.setBusiness(txtFemMemBussiness.getText().toString());
                    borrowerFamilyMember.setBusinessType(Utils.getSpinnerStringValue(acspBussinessType));
                    borrowerFamilyMember.setIncome(Integer.parseInt(spinIncomeFamMem.getText().toString().length()<1?"0":spinIncomeFamMem.getText().toString()));
                    borrowerFamilyMember.setIncomeType(Utils.getSpinnerStringValue(spinIncomeFamMemType));
                    borrowerFamilyMember.setSchoolType("");

                    borrowerFamilyMember.setHealth("");


                    borrowerFamilyMembersList.add(borrowerFamilyMember);
                    Log.d("TAG", "onClick: "+ borrowerFamilyMembersList.size());
                    addFemIncomeRecView.setAdapter(new AddFemIncomeAdapter(FiFormSecond.this,borrowerFamilyMembersList));
                    new AddFemIncomeAdapter(FiFormSecond.this,borrowerFamilyMembersList).notifyDataSetChanged();
                    dialog.dismiss();
                }



            }
        });


        dialog.show();
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
            case R.id.tietDobNominee:
                Date dob = DateUtils.getParsedDate(tietDobNominee.getText().toString(), "dd-MMM-yyyy");
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