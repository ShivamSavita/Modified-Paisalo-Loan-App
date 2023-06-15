package com.softeksol.paisalo.jlgsourcing.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.RequestParams;
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
import com.softeksol.paisalo.jlgsourcing.adapters.AdapterListRange;
import com.softeksol.paisalo.jlgsourcing.adapters.AdapterRecViewListDocuments;
import com.softeksol.paisalo.jlgsourcing.entities.AadharData;
import com.softeksol.paisalo.jlgsourcing.entities.Borrower;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerExtra;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerExtraBank;
import com.softeksol.paisalo.jlgsourcing.entities.DocumentStore;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory_Table;
import com.softeksol.paisalo.jlgsourcing.entities.dto.BorrowerDTO;
import com.softeksol.paisalo.jlgsourcing.entities.dto.OldFIById;
import com.softeksol.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;
import com.softeksol.paisalo.jlgsourcing.location.GpsTracker;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.softeksol.paisalo.jlgsourcing.Utilities.Verhoeff.validateCaseCode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ActivityBorrowerKyc extends AppCompatActivity  implements View.OnClickListener,AdapterRecViewListDocuments.ItemListener, CameraUtils.OnCameraCaptureUpdate { //, CameraUtils.OnCameraCaptureUpdate

    private final AppCompatActivity activity = this;
    AdapterListRange rlaMarritalStatus;
    private Borrower borrower;
    private BorrowerExtra borrowerExtra;
    private DocumentStore documentStore;
    //private BorrowerExtraBank borrowerExtraBank;
    private Uri uriPicture;
    private ImageView imgViewScanQR;
    private Manager manager;
    private long borrower_id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private CheckBox chkTvTopup;
    private AppCompatSpinner acspGender, acspAadharState,acspRelationship;
    private TextInputEditText tietAadharId, tietName, tietAge, tietDob, tietGuardian,
            tietAddress1, tietAddress2, tietAddress3, tietCity, tietPinCode, tietMobile,
            tietDrivingLic, tietPanNo, tietVoterId, tietMotherMName,tietMotherFName,tietMotherLName, tietFatherMName,tietFatherFName,tietFatherLName
            ,tietSpouseLName,tietSpouseMName,tietSpouseFName;
    private SearchView svOldCase;
    private TextView textViewFiDetails,tilPAN_Name,tilVoterId_Name,tilDL_Name;
    private TextWatcher ageTextWatcher;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener dateSetListner;
    Button voterIdCheckSign,panCheckSign,dLCheckSign;
    private MyTextWatcher aadharTextChangeListner;
    private AdapterRecViewListDocuments adapterRecViewListDocuments;
    private DocumentStore documentPic;
    private boolean showSubmitBorrowerMenuItem = true;
    private LinearLayoutCompat llTopupCode;
    AdapterListRange genderAdapter;
    protected static final byte SEPARATOR_BYTE = (byte)255;
    protected static final int VTC_INDEX = 15;
    protected int emailMobilePresent, imageStartIndex, imageEndIndex;
    protected ArrayList<String> decodedData;
    protected String signature,email,mobile;
    ArrayList<RangeCategory> genders;
    String loanDurationData,stateData,genderData;
    boolean aadharNumberentry=false;
    String isAdhaarEntry ="M";
    String isNameMatched ="0";
    String bankName;
    String PANHolderName, VoterIdName, BankAccountHolderName;

    CardView cardView_SpouseFirstName;
    LinearLayout linearLayout433;

    TextView textView35;

    Button BtnNextOnFirstKyc;

    Spinner spinnerMarritalStatus;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_entry);
        BtnNextOnFirstKyc=findViewById(R.id.BtnSaveKYCData);
        BtnNextOnFirstKyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBorrower();
            }
        });
        manager = (Manager) getIntent().getSerializableExtra(Global.MANAGER_TAG);

// Storing data into SharedPreferences
        sharedPreferences = getSharedPreferences("KYCData",MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
        editor = sharedPreferences.edit();
        //borrower = new Borrower();
        borrower = new Borrower(manager.Creator, manager.TAG, manager.FOCode, manager.AreaCd, IglPreferences.getPrefString(ActivityBorrowerKyc.this, SEILIGL.USER_ID, ""));
        borrowerExtra=new BorrowerExtra();
        //borrowerExtraBank=new BorrowerExtraBank(manager.Creator,manager.TAG);
        borrower.associateExtraBank(new BorrowerExtraBank(manager.Creator, manager.TAG));
        //borrower.fi
        borrower.isAadharVerified = "N";
        rlaMarritalStatus = new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("marrital_status")).queryList(), false);
        spinnerMarritalStatus = (Spinner) findViewById(R.id.spinLoanAppPersonalMarritalStatus);
        spinnerMarritalStatus.setAdapter(rlaMarritalStatus);

        Log.d("TAG", "onCreate233: "+DocumentStore.getFiData(222333));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(actionBar.getTitle() + "    Borrower KYC");
        actionBar.setDisplayHomeAsUpEnabled(true);
//        ArrayList<RangeCategory> genders = new ArrayList<>();
//        genders.add(new RangeCategory("Female", "Gender"));

        genders = new ArrayList<>();

        genders.add(new RangeCategory("Female", "Gender"));
        genders.add(new RangeCategory("Male", "Gender"));
        genders.add(new RangeCategory("Transgender", "Gender"));

//        if (BuildConfig.APPLICATION_ID.equals("com.softeksol.paisalo.jlgsourcing")) {
//            genders.add(new RangeCategory("Female", "Gender"));
//            genders.add(new RangeCategory("Male", "Gender"));
//            genders.add(new RangeCategory("Transgender", "Gender"));
//        } else if (BuildConfig.APPLICATION_ID.equals("net.softeksol.seil.groupfin.sbicolending")){
//            genders.add(new RangeCategory("Female", "Gender"));
//            genders.add(new RangeCategory("Male", "Gender"));
//            genders.add(new RangeCategory("Transgender", "Gender"));
//        }else {
//            genders.add(new RangeCategory("Female", "Gender"));
//        }

        /*switch (BuildConfig.APPLICATION_ID) {
            case "net.softeksol.seil.groupfin":
            case "net.softeksol.seil.groupfin.sbi":
                genders.add(new RangeCategory("Female", "Gender"));
                break;
            default:
                genders.add(new RangeCategory("Male", "Gender"));
                genders.add(new RangeCategory("Female", "Gender"));
                genders.add(new RangeCategory("Transgender", "Gender"));
        }*/

        cardView_SpouseFirstName = findViewById(R.id.cardView_SpouseFirstName);
        tietSpouseLName = findViewById(R.id.tietSpouseLName);
        tietSpouseMName = findViewById(R.id.tietSpouseMName);
        tietSpouseFName = findViewById(R.id.tietSpouseFName);
        textView35 = findViewById(R.id.textView35);
        textView35.setVisibility(View.GONE);
        cardView_SpouseFirstName.setVisibility(View.GONE);
        linearLayout433 = findViewById(R.id.linearLayout433);
        linearLayout433.setVisibility(View.GONE);
        panCheckSign = findViewById(R.id.panCheckSign);
        dLCheckSign = findViewById(R.id.dLCheckSign);

        voterIdCheckSign = findViewById(R.id.voterIdCheckSign);
        acspGender = findViewById(R.id.acspGender);
        tilPAN_Name = findViewById(R.id.tilPAN_Name);
        tilVoterId_Name = findViewById(R.id.tilVoterId_Name);
        tilDL_Name = findViewById(R.id.tilDL_Name);
        tilPAN_Name.setVisibility(View.GONE);
        tilDL_Name.setVisibility(View.GONE);
        tilVoterId_Name.setVisibility(View.GONE);
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
        //acspRelationship.setVisibility(View.GONE);
        //findViewById(R.id.llUidRelationship).setVisibility(View.GONE);

        findViewById(R.id.imgViewAadharPhoto).setVisibility(View.VISIBLE);

        acspAadharState = findViewById(R.id.acspAadharState);
        Log.d("TAG", "onCreate: "+RangeCategory.getRangesByCatKey("state", "DescriptionEn", true));
        acspAadharState.setAdapter(new AdapterListRange(this, RangeCategory.getRangesByCatKey("state", "DescriptionEn", true), false));

        tietName = findViewById(R.id.tietName);
        tietName.addTextChangedListener(new MyTextWatcher(tietName) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
            }
        });
        llTopupCode = findViewById(R.id.llTopupCode);
        llTopupCode.setVisibility(View.INVISIBLE);

        svOldCase = findViewById(R.id.svTopupCaseNumber);
        svOldCase.setVisibility(View.GONE);
        svOldCase.setIconified(false);
        svOldCase.setOnClickListener(this);
        /*svOldCase.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Query Click", svOldCase.getQuery().toString());
            }
        });*/
        svOldCase.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Query Submit", query);

                if (validateCaseCode(query)) {
                    fetchTopupDetails(svOldCase.getQuery().toString());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("QueryTextChange", newText);
                boolean validationStatus = validateCaseCode(newText);
                svOldCase.setBackgroundResource(validationStatus ? android.R.color.transparent : R.color.colorLightRed);
                return validationStatus;
            }
        });
        spinnerMarritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                RangeCategory rangeCategory;
                adapterView.getId();
                rangeCategory = (RangeCategory) adapterView.getSelectedItem();

                Spinner spinnerMaritalStatus = (Spinner) findViewById(R.id.spinMARITAL_STATUS);
                ;
                if (rangeCategory.RangeCode.equals("Unmarried")) {
                    linearLayout433.setVisibility(View.GONE);
                    cardView_SpouseFirstName.setVisibility(View.GONE);
                    textView35.setVisibility(View.GONE);
                } else {
                    linearLayout433.setVisibility(View.VISIBLE);
                    cardView_SpouseFirstName.setVisibility(View.VISIBLE);
                    textView35.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        chkTvTopup = findViewById(R.id.chkTopup);
        chkTvTopup.setChecked(false);
        chkTvTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svOldCase.setVisibility(chkTvTopup.isChecked() ? View.VISIBLE : View.GONE);
                svOldCase.setQuery("", false);
                tietAadharId.setEnabled(true);
                borrower.OldCaseCode = null;

            }
        });

        tietAadharId = findViewById(R.id.tietAadhar);
//        tietAadharId.addTextChangedListener(aadharTextChangeListner);
//        aadharTextChangeListner = new MyTextWatcher(tietAadharId) {
//            @Override
//            public void validate(EditText editText, String text) {
//                String aadharId = editText.getText().toString();
//                if (validateControls(editText, text)) {
//                    llTopupCode.setVisibility(View.VISIBLE);
//                    Borrower borrower1 = Borrower.getBorrower(aadharId);
//                    if (borrower1 != null) {
//                        borrower = borrower1;
//                        setDataToView(activity.findViewById(android.R.id.content).getRootView());
//                        aadharNumberentry=false;
//                    } else {
//                        fetchAadharDetails(aadharId);
//                    }
//                } else {
//                    llTopupCode.setVisibility(View.INVISIBLE);
//                }
//            }
//        };

        tietAge = findViewById(R.id.tietAge);
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

        tietDob = findViewById(R.id.tietDob);
        tietMotherMName=findViewById(R.id.tietIncomeMonthly);
        tietMotherFName=findViewById(R.id.tietMotherFName);
        tietMotherLName=findViewById(R.id.tietMotherLName);
        tietFatherMName=findViewById(R.id.tietFatherMName);
        tietFatherFName=findViewById(R.id.tietFatherFName);
        tietFatherLName=findViewById(R.id.tietFatherLName);

        tietGuardian = findViewById(R.id.tietGuardian);
        tietGuardian.addTextChangedListener(new MyTextWatcher(tietGuardian) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
            }
        });
        tietAddress1 = findViewById(R.id.tietAddress1);
        tietAddress1.addTextChangedListener(new MyTextWatcher(tietAddress1) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
            }
        });

        tietAddress2 = findViewById(R.id.tietAddress2);
        tietAddress3 = findViewById(R.id.tietAddress3);
        tietCity = findViewById(R.id.tietCity);
        tietCity.addTextChangedListener(new MyTextWatcher(tietCity) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
            }
        });
        tietPinCode = findViewById(R.id.tietPinCode);
        tietPinCode.addTextChangedListener(new MyTextWatcher(tietPinCode) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
            }
        });
        ImageView imageView = ((ImageView) findViewById(R.id.imgViewAadharPhoto));
        imageView.setVisibility(View.GONE);
        imageView.setOnClickListener(this);
        imgViewScanQR = (ImageView) findViewById(R.id.imgViewScanQR);
        imgViewScanQR.setOnClickListener(this);
        imgViewScanQR.setVisibility(View.VISIBLE);

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
        tietDob.setOnClickListener(this);

        tietMobile = findViewById(R.id.tietMobile);
        tietMobile.addTextChangedListener(new MyTextWatcher(tietMobile) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
            }
        });

        tietPanNo = findViewById(R.id.tietPAN);

        tietVoterId = findViewById(R.id.tietVoter);

        tietDrivingLic = findViewById(R.id.tietDrivingLlicense);
        tietDrivingLic.addTextChangedListener(new MyTextWatcher(tietDrivingLic) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
            }
        });

        textViewFiDetails = ((TextView) findViewById(R.id.tv_fi_details));
        textViewFiDetails.setVisibility(View.GONE);


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
                if (tietDob.getText().toString().trim().length()>10){
                    try {
                        String Dob=formatDate(tietDob.getText().toString().trim(),"dd-MMM-yyyy","yyyy-MM-dd");
                        if (tietDrivingLic.getText().toString().trim().length() >1) {
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
                    Toast.makeText(activity, "Please enter Date of Birth", Toast.LENGTH_SHORT).show();
                }

            }
        });
        voterIdCheckSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tietVoterId.getText().toString().trim().length() == 10) {
                    cardValidate(tietVoterId.getText().toString().trim(),"voterid","","");
                }else {
                    tilVoterId_Name.setVisibility(View.GONE);
                    tietVoterId.setError("Enter Voter Id");
                }
            }
        });



        tietPanNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                panCheckSign.setEnabled(true);
                panCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));
                tilPAN_Name.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        tietDrivingLic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dLCheckSign.setEnabled(true);
                dLCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));
                tilDL_Name.setText("");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        tietVoterId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                voterIdCheckSign.setEnabled(true);
                voterIdCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));
                tilVoterId_Name.setText("");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    public static String formatDate (String date, String initDateFormat, String endDateFormat) throws ParseException {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }
    private void setDataToView(View v) {
        if (borrower.Gender != null) {
            if (Utils.setSpinnerPosition((AppCompatSpinner) v.findViewById(R.id.acspGender), borrower.Gender.charAt(0), true) < 0) {
                Utils.alert(this, "Please check Gender, Cannot accept this Aadhar for Loan Application");
                return;
            }
        }
//        tietAadharId.removeTextChangedListener(aadharTextChangeListner);
        tietAadharId.setText(borrower.aadharid);
//        tietAadharId.addTextChangedListener(aadharTextChangeListner);


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
            imgViewScanQR.setVisibility(View.GONE);
            tietAadharId.setEnabled(false);
            tietName.setEnabled(false);
            if (Utils.NullIf(borrower.getGurName(), "").trim().length() > 0)
                tietGuardian.setEnabled(false);
            if (Utils.NullIf(borrower.Age, 0) > 0) {
                tietAge.setEnabled(false);
                tietDob.setEnabled(false);
            }
            acspGender.setEnabled(false);
//            acspAadharState.setEnabled(false);
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
//          acspAadharState.setEnabled(false);
            if (borrower.P_Add1.trim().length() > 0) tietAddress1.setEnabled(false);
            if (Utils.NullIf(borrower.P_add2, "").trim().length() > 2)
                tietAddress2.setEnabled(false);
            if (Utils.NullIf(borrower.P_add3, "").trim().length() > 2)
                tietAddress3.setEnabled(false);
            if (Utils.NullIf(borrower.P_city, "").trim().length() > 0) tietCity.setEnabled(false);
            if (Utils.NullIf(borrower.p_pin, 0) > 0) tietPinCode.setEnabled(false);

        }  //sir call pick karo
        //showPicture();
        tietMobile.setText(borrower.P_ph3);
        tietPanNo.setText(borrower.PanNO);
        tietVoterId.setText(borrower.voterid);
        tietDrivingLic.setText(borrower.drivinglic);


        if (borrower.Code > 0) {
            textViewFiDetails.setVisibility(View.VISIBLE);
            textViewFiDetails.setText(borrower.Creator + " / " + borrower.Code);
            tietAadharId.setEnabled(false);
            imgViewScanQR.setVisibility(View.GONE);
            showSubmitBorrowerMenuItem = false;
            invalidateOptionsMenu();
//            showScanDocs();
        }
    }

    private void getDataFromView(View v) {
        GpsTracker gpsTracker=new GpsTracker(ActivityBorrowerKyc.this);
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
        borrower.isAdhaarEntry=isAdhaarEntry;
        borrower.isMarried = Utils.getSpinnerStringValue((Spinner) v.findViewById(R.id.spinLoanAppPersonalMarritalStatus));
        borrowerExtra.save();
        borrower.fiExtra=null;
        borrower.BankName= bankName;

        borrower.save();

        editor.clear();
        editor.apply();
        editor.putString("Adhaar",tietAadharId.getText().toString());
        editor.putString("Name", Utils.getNotNullText(tietName));
        editor.putString("Age", String.valueOf(Utils.getNotNullInt(tietAge)));
        editor.putString("Gur", Utils.getNotNullText(tietGuardian));
        editor.putString("Address", Utils.getNotNullText(tietAddress1)+Utils.getNotNullText(tietAddress2)+Utils.getNotNullText(tietAddress3));
        editor.putString("City", Utils.getNotNullText(tietCity));
        editor.putString("PIN", tietPinCode.getText().toString());
        editor.putString("LoanAmount", String.valueOf(borrower.Loan_Amt));
        editor.putString("State",stateData);
        editor.putString("Mobile",tietMobile.getText().toString());
        editor.putString("PAN",tietPanNo.getText().toString());
        editor.putString("Gender", genderData);
        editor.putString("Bank", "");
        editor.putString("Income", "");
        editor.putString("Expense", "");
        editor.putString("Duration", loanDurationData);
        editor.putString("VoterId", Utils.getNotNullText(tietVoterId));
        editor.putString("DOB", parseDateToddMMyyyy(tietDob.getText().toString()));
        editor.putString("LoanReason", borrower.Loan_Reason);
        editor.apply();

    }
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd-MMM-yyyy";
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
        return str;
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgViewScanQR:
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.setOrientationLocked(false);
                scanIntegrator.initiateScan(Collections.singleton("QR_CODE"));
                break;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        isAdhaarEntry ="S";
                        setAadharContent(scanContent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            if (requestCode == CameraUtils.REQUEST_TAKE_PHOTO) {
                if (resultCode == RESULT_OK) {
                    if (documentPic.checklistid == 0) {
                        CropImage.activity(this.uriPicture)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(45, 52)
                                .start(this);
                    } else {
                        CropImage.activity(this.uriPicture)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(this);
                    }
                } else {
                    Utils.alert(this, "Could not take Picture");
                }
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                Exception error = null;
                int maxDimentions = (documentPic.checklistid == 0 ? 300 : 1000);
                Uri imageUri = CameraUtils.finaliseImageCropUri(resultCode, data, maxDimentions, error, false);
                File tempCroppedImage = new File(imageUri.getPath());

                if (tempCroppedImage.length() > 100) {
                    if (borrower != null) {
                        (new File(this.uriPicture.getPath())).delete();
                        try {
                            File croppedImage = CameraUtils.moveCachedImage2Storage(this, tempCroppedImage, true);
                            documentPic.imagePath = croppedImage.getPath();
                            documentPic.save();
                            if (documentPic.checklistid == 1) putExifData(documentPic);
                            adapterRecViewListDocuments.updateList(getDocumentStore(borrower));
                            //borrower.setPicture(croppedImage.getPath());
                            //borrower.save();
                            //borrower_id = borrower.FiID;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void setAadharContent(String aadharDataString) throws Exception {
//        try {
        Log.d("CheckXMLDATA2", "AadharData:->" + aadharDataString);
        if (aadharDataString.toUpperCase().contains("XML")) {

            Log.d("XML printing", "AadharData:->" + aadharDataString);
            //AadharData aadharData = AadharUtils.getAadhar(aadharDataString);
            AadharData aadharData = AadharUtils.getAadhar(AadharUtils.ParseAadhar(aadharDataString));

            if (aadharData.AadharId != null) {

                Borrower borrower1 = Borrower.getBorrower(aadharData.AadharId);
                if (borrower1 != null) {
                    borrower = borrower1;
                    setDataToView(activity.findViewById(android.R.id.content).getRootView());
                    tietAadharId.setEnabled(false);
                    return;
                }
                Log.d("TAG", "setAadharContent: done1");
                if (chkTvTopup.isChecked()) {
                    if (tietAadharId.getText().toString().equals(aadharData.AadharId)) {
                        Utils.alert(this, "Aadhar ID did not match with Topup Case");
                        return;
                    }
                }
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
            tietAge.setEnabled(false);
            tietDob.setEnabled(false);
            aadharNumberentry=true;

        }  else {

            final BigInteger bigIntScanData = new BigInteger(aadharDataString, 10);
            Log.e("testbigin======", "AadharData:->" + bigIntScanData);
            // 2. Convert BigInt to Byte Array
            final byte byteScanData[] = bigIntScanData.toByteArray();

            // 3. Decompress Byte Array
            final byte[] decompByteScanData = decompressData(byteScanData);

            // 4. Split the byte array using delimiter
            List<byte[]> parts = separateData(decompByteScanData);
            // Throw error if there are no parts
            Log.e("Parts======11======> ", "part data =====> " + parts.toString());
            decodeData(parts);
            decodeSignature(decompByteScanData);
            decodeMobileEmail(decompByteScanData);
            aadharNumberentry=true;
        }
//            } catch(Exception ex) {
//            Utils.alert(this, ex.getMessage());
//        }

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


    protected void decodeData(List<byte[]> encodedData) throws ParseException{
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


    @Override
    public void cameraCaptureUpdate(Uri uriImage) {
        this.uriPicture = uriImage;
    }

    private void  updateBorrower() {
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
                        if (!chkTvTopup.isChecked()) borrower.OldCaseCode = null;
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
                                Toast.makeText(activity, "Please Verify PAN Card, Voter ID and Driving License", Toast.LENGTH_SHORT).show();
                            }else{
                                if(!tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilPAN_Name.getText().toString().trim().split(" ")[0]) || !tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilVoterId_Name.getText().toString().trim().split(" ")[0]) || !tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilDL_Name.getText().toString().trim().split(" ")[0])){


                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBorrowerKyc.this);
                                    builder.setTitle("Caution!!");
                                    builder.setMessage("want to save  data without PAN Card name, Driving License Name, Voter Id Name and Aadhaar Name matching");
                                    builder.setPositiveButton("Save data Forcefully", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(activity, "At this time please enter correct details", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ActivityBorrowerKyc.this, KYC_Form_New.class);
                                            intent.putExtra("FatherFName", tietFatherFName.getText().toString());
                                            intent.putExtra("FatherLName", tietFatherLName.getText().toString());
                                            intent.putExtra("FatherMName", tietFatherMName.getText().toString());
                                            intent.putExtra("MotherFName", tietMotherFName.getText().toString());
                                            intent.putExtra("MotherLName", tietMotherLName.getText().toString());
                                            intent.putExtra("MotherMName", tietMotherMName.getText().toString());
                                            intent.putExtra("SpouseLName", tietSpouseLName.getText().toString());
                                            intent.putExtra("SpouseMName", tietSpouseMName.getText().toString());
                                            intent.putExtra("SpouseFName", tietSpouseFName.getText().toString());
                                            intent.putExtra("manager", manager);
                                            intent.putExtra("borrower", borrower);
                                            startActivity(intent);
                                        }
                                    });
                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(activity, "Kindly verify all details", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    builder.create().show();

                                }else{
                                    Intent intent=new Intent(ActivityBorrowerKyc.this,KYC_Form_New.class);
                                    intent.putExtra("FatherFName",tietFatherFName.getText().toString());
                                    intent.putExtra("FatherLName",tietFatherLName.getText().toString());
                                    intent.putExtra("FatherMName",tietFatherMName.getText().toString());
                                    intent.putExtra("MotherFName",tietMotherFName.getText().toString());
                                    intent.putExtra("MotherLName",tietMotherLName.getText().toString());
                                    intent.putExtra("MotherMName",tietMotherMName.getText().toString());

                                    intent.putExtra("SpouseLName",tietSpouseLName.getText().toString());
                                    intent.putExtra("SpouseMName",tietSpouseMName.getText().toString());
                                    intent.putExtra("SpouseFName",tietSpouseFName.getText().toString());


                                    intent.putExtra("manager", manager);
                                    intent.putExtra("borrower", borrower);
                                    startActivity(intent);
                                }
                            }
                        }else{
                            if(!tilPAN_Name.getText().toString().trim().equals("") ||!tilVoterId_Name.getText().toString().trim().equals("") ||!tilDL_Name.getText().toString().trim().equals("") ){
                                    if((!tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilPAN_Name.getText().toString().trim().split(" ")[0]) && !tilPAN_Name.getText().toString().trim().equals("")) || (!tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilVoterId_Name.getText().toString().trim().split(" ")[0]) && !tilVoterId_Name.getText().toString().trim().equals("") )|| (!tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilDL_Name.getText().toString().trim().split(" ")[0]) && !tilDL_Name.getText().toString().trim().equals(""))){

                                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBorrowerKyc.this);
                                        builder.setTitle("Caution!!");
                                        builder.setMessage("want to save  data without Matching Documents Name and Aadhaar Name matching");
                                        builder.setPositiveButton("Save data Forcefully", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(activity, "At this time please enter correct details", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ActivityBorrowerKyc.this, KYC_Form_New.class);
                                                intent.putExtra("FatherFName", tietFatherFName.getText().toString());
                                                intent.putExtra("FatherLName", tietFatherLName.getText().toString());
                                                intent.putExtra("FatherMName", tietFatherMName.getText().toString());
                                                intent.putExtra("MotherFName", tietMotherFName.getText().toString());
                                                intent.putExtra("MotherLName", tietMotherLName.getText().toString());
                                                intent.putExtra("MotherMName", tietMotherMName.getText().toString());
                                                intent.putExtra("SpouseLName", tietSpouseLName.getText().toString());
                                                intent.putExtra("SpouseMName", tietSpouseMName.getText().toString());
                                                intent.putExtra("SpouseFName", tietSpouseFName.getText().toString());
                                                intent.putExtra("manager", manager);
                                                intent.putExtra("borrower", borrower);
                                                startActivity(intent);
                                            }
                                        });
                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Toast.makeText(activity, "Kindly verify all details", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        builder.create().show();
                                    }else{
                                        Intent intent=new Intent(ActivityBorrowerKyc.this,KYC_Form_New.class);
                                        intent.putExtra("FatherFName",tietFatherFName.getText().toString());
                                        intent.putExtra("FatherLName",tietFatherLName.getText().toString());
                                        intent.putExtra("FatherMName",tietFatherMName.getText().toString());
                                        intent.putExtra("MotherFName",tietMotherFName.getText().toString());
                                        intent.putExtra("MotherLName",tietMotherLName.getText().toString());
                                        intent.putExtra("MotherMName",tietMotherMName.getText().toString());

                                        intent.putExtra("SpouseLName",tietSpouseLName.getText().toString());
                                        intent.putExtra("SpouseMName",tietSpouseMName.getText().toString());
                                        intent.putExtra("SpouseFName",tietSpouseFName.getText().toString());


                                        intent.putExtra("manager", manager);
                                        intent.putExtra("borrower", borrower);
                                        startActivity(intent);
                                    }
                        }else if (!tietPanNo.getText().toString().equals("")){
                                if (tilPAN_Name.getText().toString().trim().equals("")){
                                    Toast.makeText(activity, "Please Verify the PAN Card", Toast.LENGTH_SHORT).show();
                                }else {
                                    if (!tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilPAN_Name.getText().toString().trim().split(" ")[0])) {


                                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBorrowerKyc.this);
                                        builder.setTitle("Caution!!");
                                        builder.setMessage("want to save  data without PAN card Name and Aadhaar Name matching");
                                        builder.setPositiveButton("Save data Forcefully", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(activity, "At this time please enter correct details", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ActivityBorrowerKyc.this, KYC_Form_New.class);
                                                intent.putExtra("FatherFName", tietFatherFName.getText().toString());
                                                intent.putExtra("FatherLName", tietFatherLName.getText().toString());
                                                intent.putExtra("FatherMName", tietFatherMName.getText().toString());
                                                intent.putExtra("MotherFName", tietMotherFName.getText().toString());
                                                intent.putExtra("MotherLName", tietMotherLName.getText().toString());
                                                intent.putExtra("MotherMName", tietMotherMName.getText().toString());
                                                intent.putExtra("SpouseLName", tietSpouseLName.getText().toString());
                                                intent.putExtra("SpouseMName", tietSpouseMName.getText().toString());
                                                intent.putExtra("SpouseFName", tietSpouseFName.getText().toString());
                                                intent.putExtra("manager", manager);
                                                intent.putExtra("borrower", borrower);
                                                startActivity(intent);
                                            }
                                        });
                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Toast.makeText(activity, "Kindly verify all details", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        builder.create().show();
                                    }
                                    else{
                                        Intent intent=new Intent(ActivityBorrowerKyc.this,KYC_Form_New.class);
                                        intent.putExtra("FatherFName",tietFatherFName.getText().toString());
                                        intent.putExtra("FatherLName",tietFatherLName.getText().toString());
                                        intent.putExtra("FatherMName",tietFatherMName.getText().toString());
                                        intent.putExtra("MotherFName",tietMotherFName.getText().toString());
                                        intent.putExtra("MotherLName",tietMotherLName.getText().toString());
                                        intent.putExtra("MotherMName",tietMotherMName.getText().toString());

                                        intent.putExtra("SpouseLName",tietSpouseLName.getText().toString());
                                        intent.putExtra("SpouseMName",tietSpouseMName.getText().toString());
                                        intent.putExtra("SpouseFName",tietSpouseFName.getText().toString());


                                        intent.putExtra("manager", manager);
                                        intent.putExtra("borrower", borrower);
                                        startActivity(intent);
                                    }
                                }
                            }
                            else if(!tietVoterId.getText().toString().equals("")){
                                if(tilVoterId_Name.getText().toString().trim().equals("")){
                                    Toast.makeText(activity, "Please verify the Voter ID", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    if (!tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilVoterId_Name.getText().toString().trim().split(" ")[0])) {


                                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBorrowerKyc.this);
                                        builder.setTitle("Caution!!");
                                        builder.setMessage("want to save  data without Voter Id Name and Aadhaar Name matching");
                                        builder.setPositiveButton("Save data Forcefully", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(activity, "At this time please enter correct details", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ActivityBorrowerKyc.this, KYC_Form_New.class);
                                                intent.putExtra("FatherFName", tietFatherFName.getText().toString());
                                                intent.putExtra("FatherLName", tietFatherLName.getText().toString());
                                                intent.putExtra("FatherMName", tietFatherMName.getText().toString());
                                                intent.putExtra("MotherFName", tietMotherFName.getText().toString());
                                                intent.putExtra("MotherLName", tietMotherLName.getText().toString());
                                                intent.putExtra("MotherMName", tietMotherMName.getText().toString());
                                                intent.putExtra("SpouseLName", tietSpouseLName.getText().toString());
                                                intent.putExtra("SpouseMName", tietSpouseMName.getText().toString());
                                                intent.putExtra("SpouseFName", tietSpouseFName.getText().toString());
                                                intent.putExtra("manager", manager);
                                                intent.putExtra("borrower", borrower);
                                                startActivity(intent);
                                            }
                                        });
                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Toast.makeText(activity, "Kindly verify all details", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        builder.create().show();
                                    }
                                    else{
                                        Intent intent=new Intent(ActivityBorrowerKyc.this,KYC_Form_New.class);
                                        intent.putExtra("FatherFName",tietFatherFName.getText().toString());
                                        intent.putExtra("FatherLName",tietFatherLName.getText().toString());
                                        intent.putExtra("FatherMName",tietFatherMName.getText().toString());
                                        intent.putExtra("MotherFName",tietMotherFName.getText().toString());
                                        intent.putExtra("MotherLName",tietMotherLName.getText().toString());
                                        intent.putExtra("MotherMName",tietMotherMName.getText().toString());

                                        intent.putExtra("SpouseLName",tietSpouseLName.getText().toString());
                                        intent.putExtra("SpouseMName",tietSpouseMName.getText().toString());
                                        intent.putExtra("SpouseFName",tietSpouseFName.getText().toString());


                                        intent.putExtra("manager", manager);
                                        intent.putExtra("borrower", borrower);
                                        startActivity(intent);
                                    }
                                }

                            }
                            else if(!tietDrivingLic.getText().toString().equals("")){
                                if (tilDL_Name.getText().toString().trim().equals("")){
                                    Toast.makeText(activity, "Please Verify the Driving License ", Toast.LENGTH_SHORT).show();
                                }else{
                                    if (!tietName.getText().toString().trim().split(" ")[0].equalsIgnoreCase(tilDL_Name.getText().toString().trim().split(" ")[0])) {


                                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBorrowerKyc.this);
                                        builder.setTitle("Caution!!");
                                        builder.setMessage("want to save  data without Driving License Name and Aadhaar Name matching");
                                        builder.setPositiveButton("Save data Forcefully", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(activity, "At this time please enter correct details", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ActivityBorrowerKyc.this, KYC_Form_New.class);
                                                intent.putExtra("FatherFName", tietFatherFName.getText().toString());
                                                intent.putExtra("FatherLName", tietFatherLName.getText().toString());
                                                intent.putExtra("FatherMName", tietFatherMName.getText().toString());
                                                intent.putExtra("MotherFName", tietMotherFName.getText().toString());
                                                intent.putExtra("MotherLName", tietMotherLName.getText().toString());
                                                intent.putExtra("MotherMName", tietMotherMName.getText().toString());
                                                intent.putExtra("SpouseLName", tietSpouseLName.getText().toString());
                                                intent.putExtra("SpouseMName", tietSpouseMName.getText().toString());
                                                intent.putExtra("SpouseFName", tietSpouseFName.getText().toString());
                                                intent.putExtra("manager", manager);
                                                intent.putExtra("borrower", borrower);
                                                startActivity(intent);
                                            }
                                        });
                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Toast.makeText(activity, "Kindly verify all details", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        builder.create().show();
                                    }
                                    else{
                                        Intent intent=new Intent(ActivityBorrowerKyc.this,KYC_Form_New.class);
                                        intent.putExtra("FatherFName",tietFatherFName.getText().toString());
                                        intent.putExtra("FatherLName",tietFatherLName.getText().toString());
                                        intent.putExtra("FatherMName",tietFatherMName.getText().toString());
                                        intent.putExtra("MotherFName",tietMotherFName.getText().toString());
                                        intent.putExtra("MotherLName",tietMotherLName.getText().toString());
                                        intent.putExtra("MotherMName",tietMotherMName.getText().toString());

                                        intent.putExtra("SpouseLName",tietSpouseLName.getText().toString());
                                        intent.putExtra("SpouseMName",tietSpouseMName.getText().toString());
                                        intent.putExtra("SpouseFName",tietSpouseFName.getText().toString());


                                        intent.putExtra("manager", manager);
                                        intent.putExtra("borrower", borrower);
                                        startActivity(intent);
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

    private void fetchTopupDetails(final String oldCaseCode) {

        DataAsyncResponseHandler dataAsyncResponseHandler = new DataAsyncResponseHandler(this, "Borrower KYC", "Fetching Topup Details") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String jsonString = new String(responseBody);
                    Log.d("Response", jsonString);
                    JSONObject jo = null;
                    try {
                        jo = (new JSONArray(jsonString)).getJSONObject(0);
                        int noInst = jo.getInt("Total_Installments");
                        int lastInstR = jo.getInt("LastInst_Recd");
                        String aadharId = jo.getString("aadharid");

                        if (aadharId.length() == 12) {
                            if (tietAadharId.getText().length() == 12) {
                                if (!tietAadharId.getText().toString().equals(aadharId)) {
                                    Utils.alert(ActivityBorrowerKyc.this, "Aadhar IDs did not match");
                                    return;
                                }
                            }
                                /*if (tietAadharId.getText().length() == 0 || tietAadharId.getText().length() != 12) {
                                    tietAadharId.setText(aadharId);
                                    tietAadharId.setEnabled(false);
                                }*/
                            borrower.OldCaseCode = jo.getString("CODE");
                            //tietName.setText(jo.getString("SUBS_NAME"));
                            if (lastInstR >= ((noInst / 2) - 1)) {

                            } else {
                                Utils.alert(ActivityBorrowerKyc.this, "Minimum installment not received for topup");
                            }
                        } else {
                            Utils.alert(ActivityBorrowerKyc.this, "Please get Aadhar ID updated for Topup Case");
                        }


                    } catch (JSONException je) {
                        if (("[]").equals(jsonString))
                            Utils.alert(ActivityBorrowerKyc.this, "No record found for Loan A/c " + oldCaseCode);
                        else {

                        }
                    }
                }
            }
        };
        RequestParams params = new RequestParams();
        params.add("SmCode", oldCaseCode);
        (new WebOperations()).getEntity(this, "posfi", "gettopupcasedtls", params, dataAsyncResponseHandler);

    }

    private void fetchAadharDetails(String aadharId) {

        DataAsyncResponseHandler dataAsyncResponseHandler = new DataAsyncResponseHandler(this, "Borrower KYC", "Fetching Previous Applications") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String jsonString = new String(responseBody);
                    Log.d("Response", jsonString);
                    //JSONObject jo = null;
                    //Type listType = new TypeToken<List<OldFIById>>() {}.getType();
                    List<OldFIById> oldFIByIds = WebOperations.convertToObjectArray(jsonString);


                    /*try {
                        jo = (new JSONArray(jsonString)).getJSONObject(0);
                        int noInst = jo.getInt("Total_Installments");
                        int lastInstR = jo.getInt("LastInst_Recd");
                        String aadharId = jo.getString("AadharID");
                        if (lastInstR >= ((noInst / 2) - 1)) {
                            if (aadharId.length() == 12) {
                                if (tietAadharId.getText().length() == 12) {
                                    if (!tietAadharId.getText().toString().equals(aadharId)) {
                                        Utils.alert(ActivityBorrowerKyc.this, "Aadhar IDs did not match");
                                        return;
                                    }
                                }
                                if (tietAadharId.getText().length() == 0 || tietAadharId.getText().length() != 12) {
                                    tietAadharId.setText(aadharId);
                                    tietAadharId.setEnabled(false);
                                }
                                borrower.OldCaseCode = jo.getString("CODE");
                                tietName.setText(jo.getString("SUBS_NAME"));

                            } else {
                                Utils.alert(ActivityBorrowerKyc.this, "Please get Aadhar ID updated for Topup Case");
                            }

                        } else {
                            Utils.alert(ActivityBorrowerKyc.this, "Minimum installment not received for topup");
                        }

                    } catch (JSONException je) {

                    }*/
                }
            }
        };
        RequestParams params = new RequestParams();
        params.add("Aadharid", aadharId);
        (new WebOperations()).getEntity(this, "posdb", "getaadharstatus", params, dataAsyncResponseHandler);

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
                if (editText.getText().toString().trim().length() < 1) {
                    editText.setError("Should be more than 5 Characters");
                    retVal = false;
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

    private void cardValidate(String id,String type,String bankIfsc,String dob) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Fetching Details");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ApiInterface apiInterface= ApiClient.getClient("https://agra.paisalo.in:8462/creditmatrix/api/").create(ApiInterface.class);
        Log.d("TAG", "checkCrifScore: "+getJsonOfString(id,type,bankIfsc,dob));
        Call<JsonObject> call=apiInterface.cardValidate(getJsonOfString(id,type,bankIfsc,dob));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (type.equals("pancard")){
                    try {
                        tilPAN_Name.setVisibility(View.VISIBLE);
                        tilPAN_Name.setText(response.body().get("data").getAsJsonObject().get("name").getAsString());
                        panCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic_green));
                        panCheckSign.setEnabled(false);
                    }catch (Exception e){
                        tilPAN_Name.setVisibility(View.VISIBLE);
                        tilPAN_Name.setText("Card Holder Name Not Found");
                        panCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));
                        panCheckSign.setEnabled(true);

                    }
                    progressDialog.cancel();
                }else if(type.equals("voterid")){
                    try {
                        tilVoterId_Name.setVisibility(View.VISIBLE);

                        tilVoterId_Name.setText(response.body().get("data").getAsJsonObject().get("name").getAsString());
                        voterIdCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic_green));
                        voterIdCheckSign.setEnabled(false);
                    }catch (Exception e){
                        tilVoterId_Name.setVisibility(View.VISIBLE);
                        tilVoterId_Name.setText("Card Holder Name Not Found");
                        voterIdCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));
                        voterIdCheckSign.setEnabled(true);

                    }
                    progressDialog.cancel();

                }else if(type.equals("drivinglicense")){
                    try {
                        tilDL_Name.setVisibility(View.VISIBLE);

                        tilDL_Name.setText(response.body().get("data").getAsJsonObject().get("name").getAsString());
                        dLCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic_green));
                        dLCheckSign.setEnabled(false);
                    }catch (Exception e){
                        tilDL_Name.setVisibility(View.VISIBLE);
                        tilDL_Name.setText("Card Holder Name Not Found");
                        dLCheckSign.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));
                        dLCheckSign.setEnabled(true);

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
//        retVal &= validateControls(tietVoterId, tietVoterId.getText().toString()) || validateControls(tietPanNo, tietPanNo.getText().toString());
        retVal &= validateControls(tietDrivingLic, tietDrivingLic.getText().toString());
        retVal &= validateControls(tietMotherFName, tietMotherFName.getText().toString());
        retVal &= validateControls(tietFatherFName, tietFatherFName.getText().toString());
        return retVal;
    }

    private List<DocumentStore> getDocumentStore(Borrower borrower) {
        List<DocumentStore> documentStores = new ArrayList<>();
        DocumentStore documentStore = new DocumentStore();
        documentStores.add(documentStore.getDocumentStore(borrower, 0, "Picture"));
        if (!Utils.NullIf(borrower.isAadharVerified, "N").equals("Y")) {
            documentStores.add(documentStore.getDocumentStore(borrower, 1, "AadharFront"));
            documentStores.add(documentStore.getDocumentStore(borrower, 1, "AadharBack"));
        }

        if (BuildConfig.APPLICATION_ID == "com.softeksol.paisalo.jlgsourcing") {
            documentStores.add(documentStore.getDocumentStore(borrower, 5, "VoterFront"));
            documentStores.add(documentStore.getDocumentStore(borrower, 5, "VoterBack"));

            if (borrower.PanNO.length() == 10) {
                documentStores.add(documentStore.getDocumentStore(borrower, 6, "PANCard"));
            }
        } else {
            if (borrower.voterid.length() > 9) {
                documentStores.add(documentStore.getDocumentStore(borrower, 3, "VoterFront"));
                documentStores.add(documentStore.getDocumentStore(borrower, 3, "VoterBack"));
            }
            if (borrower.PanNO.length() == 10) {
                documentStores.add(documentStore.getDocumentStore(borrower, 4, "PANCard"));
            }
        }
        if (borrower.drivinglic.length() > 9) {
            documentStores.add(documentStore.getDocumentStore(borrower, 15, "DrivingLicFront"));
            documentStores.add(documentStore.getDocumentStore(borrower, 15, "DrivingLicBack"));
        }

        //documentStores.add(documentStore.getDocumentStore(borrower,2,"PassbookFirst"));
        //documentStores.add(documentStore.getDocumentStore(borrower,2,"PassbookLast"));
        //documentStores.add(documentStore.getDocumentStore(borrower,4,"Mandate"));
        Collections.sort(documentStores, DocumentStore.sortByUploadStatus);
        return documentStores;
    }

    @Override
    public void onKycCapture(DocumentStore item) {
        documentPic = item;
        Boolean cropState = true;
        try {
            CameraUtils.dispatchTakePictureIntent(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onKycUpload(final DocumentStore documentStore, final View view) {
        DataAsyncResponseHandler responseHandler = new DataAsyncResponseHandler(this, "Loan Financing", DocumentStore.getDocumentName(documentStore.checklistid)) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);
                Utils.showSnakbar(activity.findViewById(android.R.id.content).getRootView(), responseString);
                //if(responseString.equals("")) {
                documentStore.updateStatus = true;
                documentStore.update();
                (new File(documentStore.imagePath)).delete();
//                showScanDocs();
            }
        };

        String jsonString = WebOperations.convertToJson(documentStore.getDocumentDTO());
        String apiPath = documentStore.checklistid == 0 ? "/api/uploaddocs/savefipicjson" : "/api/uploaddocs/savefidocsjson";
        (new WebOperations()).postEntity(activity, BuildConfig.BASE_URL + apiPath, jsonString, responseHandler);
    }

    public void showScanDocs() {
        List<DocumentStore> documentStoreList = getDocumentStore(borrower);
        adapterRecViewListDocuments.updateList(documentStoreList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void putExifData(DocumentStore documentStore) {
        try {
            ExifInterface newExif = new ExifInterface(documentStore.imagePath);
            newExif.setAttribute(ExifInterface.TAG_USER_COMMENT, documentStore.Creator + "_" + documentStore.ficode);
            SecretKey secretKey = Utils.generateKey(documentStore.Creator + "#" + documentStore.ficode);
            String id = new String(Utils.encryptMsg(borrower.aadharid, secretKey));
            newExif.setAttribute(ExifInterface.TAG_IMAGE_UNIQUE_ID, id);
            newExif.saveAttributes();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | InvalidKeySpecException | IllegalBlockSizeException | InvalidParameterSpecException e) {
            e.printStackTrace();
        }
    }

}
