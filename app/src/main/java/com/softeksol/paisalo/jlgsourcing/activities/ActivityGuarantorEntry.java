package com.softeksol.paisalo.jlgsourcing.activities;

import static com.softeksol.paisalo.jlgsourcing.activities.ActivityBorrowerKyc.bytesToHex;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.RequestParams;
import com.raizlabs.android.dbflow.sql.language.SQLite;
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
import com.softeksol.paisalo.jlgsourcing.adapters.AdapterListRange;
import com.softeksol.paisalo.jlgsourcing.entities.AadharData;
import com.softeksol.paisalo.jlgsourcing.entities.Borrower;
import com.softeksol.paisalo.jlgsourcing.entities.Borrower_Table;
import com.softeksol.paisalo.jlgsourcing.entities.DocumentStore;
import com.softeksol.paisalo.jlgsourcing.entities.Guarantor;
import com.softeksol.paisalo.jlgsourcing.entities.Guarantor_Table;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory;
import com.softeksol.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;
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
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import cz.msebera.android.httpclient.Header;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ActivityGuarantorEntry extends AppCompatActivity implements View.OnClickListener, CameraUtils.OnCameraCaptureUpdate {
    private Guarantor guarantor;
    private Borrower borrower;
    int ImageType=0;
    protected String signature,email,mobile;
    private Uri uriPicture;
    private ImageView imageView, imgViewScanQR;
    private Boolean cropState = false;
    private long guarantor_id;
    private long borrower_id;

    private AppCompatSpinner acspGender, acspRelationship, acspAadharState;
    private TextInputEditText tietName, tietAadharId, tietAge, tietDob, tietGuardian,
            tietAddress1, tietAddress2, tietAddress3, tietCity, tietPinCode,
            tietMobile, tietVoter, tietDrivingLic, tietPan;
    private Button btnAdd, btnUpdate, btnCancel, btnDelete;
    private TextWatcher dobTextWatcher, ageTextWatcher;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener dateSetListner;
    private Bitmap bitmap;
    private String ImageString;
    Button BtnSaveKYCData;
    protected ArrayList<String> decodedData;
    protected static final byte SEPARATOR_BYTE = (byte)255;
    protected static final int VTC_INDEX = 15;
    protected int emailMobilePresent, imageStartIndex, imageEndIndex;
    ImageView imgViewCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_guarantor_entry);
        borrower_id = getIntent().getLongExtra(Global.BORROWER_TAG, 0);
        guarantor_id = getIntent().getLongExtra(Global.GUARANTOR_TAG, 0);
        borrower = SQLite.select().from(Borrower.class).where(Borrower_Table.FiID.eq(borrower_id)).querySingle();
        guarantor = getGuarantor(guarantor_id, borrower_id);

        btnAdd = findViewById(R.id.btnGuarantorAdd);
        imgViewCal = findViewById(R.id.imgViewCal);
        imgViewCal.setOnClickListener(this);
        BtnSaveKYCData = findViewById(R.id.BtnSaveKYCData);
        BtnSaveKYCData.setVisibility(View.GONE);
        btnUpdate = findViewById(R.id.btnGuarantorUpdate);
        btnUpdate.setOnClickListener(this);
        btnDelete = findViewById(R.id.btnGuarantorDelete);
        btnDelete.setOnClickListener(this);
        btnAdd.setVisibility(guarantor == null ? View.VISIBLE : View.GONE);
        /*btnUpdate.setVisibility(guarantor!=null?View.VISIBLE:View.GONE);
        btnDelete.setVisibility(guarantor!=null?View.VISIBLE:View.GONE);*/

        ArrayList<RangeCategory> genders = new ArrayList<>();
        genders.add(new RangeCategory("Female", "Gender"));
        genders.add(new RangeCategory("Male", "Gender"));
        genders.add(new RangeCategory("Transgender", "Gender"));

        myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());
        acspGender = (findViewById(R.id.acspGender));
        acspGender.setAdapter(new AdapterListRange(this, genders, false));

        acspRelationship = findViewById(R.id.acspRelationship);
        acspRelationship.setAdapter(new AdapterListRange(this, RangeCategory.getRangesByCatKey("relationship"), false));

        acspAadharState = findViewById(R.id.acspAadharState);
        acspAadharState.setAdapter(new AdapterListRange(this, RangeCategory.getRangesByCatKey("state"), false));

        tietName = findViewById(R.id.tietName);
        tietName.addTextChangedListener(new MyTextWatcher(tietName) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
            }
        });
        tietAadharId = findViewById(R.id.tietAadhar);
        tietAadharId.addTextChangedListener(new MyTextWatcher(tietAadharId) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
            }
        });

        tietAge = findViewById(R.id.tietAge);
        tietAge.setEnabled(false);
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
        tietDob.setFocusable(false);
        tietDob.setEnabled(false);

        tietGuardian = findViewById(R.id.tietGuardian);
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
        tietMobile = findViewById(R.id.tietMobile);
        tietMobile.addTextChangedListener(new MyTextWatcher(tietMobile) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
            }
        });
        tietVoter = findViewById(R.id.tietVoter);
        tietVoter.addTextChangedListener(new MyTextWatcher(tietVoter) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
            }
        });
        TextView Capture_Aadhar=findViewById(R.id.Capture_Aadhar);
        TextView Capture_Aadharback=findViewById(R.id.Capture_Aadharback);

        Capture_Aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageType = 1;
                ImagePicker.with(ActivityGuarantorEntry.this)
                        .cameraOnly()
                        .start(1000);

            }
        });
        Capture_Aadharback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageType=2;
                ImagePicker.with(ActivityGuarantorEntry.this)
                        .cameraOnly()
                        .start(1000);

            }
        });
        tietDrivingLic = findViewById(R.id.tietDrivingLlicense);
        tietDrivingLic.addTextChangedListener(new MyTextWatcher(tietDrivingLic) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
            }
        });
        tietPan = findViewById(R.id.tietPAN);
        tietPan.addTextChangedListener(new MyTextWatcher(tietPan) {
            @Override
            public void validate(EditText editText, String text) {
                validateControls(editText, text);
            }
        });


        imageView = findViewById(R.id.imgViewAadharPhoto);
        imageView.setOnClickListener(this);
        imgViewScanQR = findViewById(R.id.imgViewScanQR);
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
       // tietDob.setOnClickListener(this);
    }

    private void setDataToView(View v) {
        tietAadharId.setText(guarantor.getAadharid());
        tietName.setText(guarantor.getName());

        tietAge.setText(String.valueOf(guarantor.getAge()));
        Utils.setOnFocuseSelect(tietAge, "0");
        if (guarantor.getDOB() == null) {
            tietDob.setText(DateUtils.getDobFrmAge(Integer.parseInt(tietAge.getText().toString())));
        } else {
            myCalendar.setTime(guarantor.getDOB());
            tietDob.setText(DateUtils.getFormatedDate(guarantor.getDOB(), "dd-MMM-yyyy"));
        }

        tietGuardian.setText(guarantor.getGurName());
        tietAddress1.setText(guarantor.getPerAdd1());
        tietAddress2.setText(guarantor.getPerAdd2());
        tietAddress3.setText(guarantor.getPerAdd3());
        tietCity.setText(guarantor.getPerCity());
        tietPinCode.setText(String.valueOf(guarantor.getP_pin()));
        Utils.setOnFocuseSelect(tietPinCode, "0");


        if (guarantor.getGender() != null) {
            Utils.setSpinnerPosition(acspGender, guarantor.getGender().charAt(0), true);
        }
        if (guarantor.getResFax() != null) {
            Utils.setSpinnerPosition(acspRelationship, guarantor.getResFax(), false);
        }
        if (guarantor.getP_StateID() != null) {
            Utils.setSpinnerPosition(acspAadharState, guarantor.getP_StateID());
        }

        tietMobile.setText(guarantor.getPerMob1());
        tietVoter.setText(guarantor.getVoterid());

        tietPan.setText(guarantor.getPANNo());
        tietDrivingLic.setText(guarantor.getDrivinglic());


        if (guarantor.getIsAadharVerified().equals("Q")) {
            tietName.setEnabled(false);
            if (Utils.NullIf(guarantor.getGurName(), "").trim().length() > 0)
                tietGuardian.setEnabled(false);
            if (Utils.NullIf(guarantor.getAge(), 0) > 0) {
                tietAge.setEnabled(false);
                tietDob.setEnabled(false);
            }
            acspGender.setEnabled(false);
            acspAadharState.setEnabled(false);
            if (guarantor.getPerAdd1().trim().length() > 0) tietAddress1.setEnabled(false);
            if (Utils.NullIf(guarantor.getPerAdd2(), "").trim().length() > 0)
                tietAddress2.setEnabled(false);
            if (Utils.NullIf(guarantor.getPerAdd3(), "").trim().length() > 0)
                tietAddress3.setEnabled(false);
            if (Utils.NullIf(guarantor.getPerCity(), "").trim().length() > 0)
                tietCity.setEnabled(false);
            if (Utils.NullIf(guarantor.getP_pin(), 0) > 0) tietPinCode.setEnabled(false);
        }

            showPicture();


    }

    private void getDataFromView(View v) {
        guarantor.setAadharid(Utils.getNotNullText(tietAadharId));
        guarantor.setName(Utils.getNotNullText(tietName));
        guarantor.setAge(Utils.getNotNullInt(tietAge));
        guarantor.setDOB(myCalendar.getTime());

        guarantor.setGurName(Utils.getNotNullText(tietGuardian));
        guarantor.setPerAdd1(Utils.getNotNullText(tietAddress1));
        guarantor.setPerAdd2(Utils.getNotNullText(tietAddress2));
        guarantor.setPerAdd3(Utils.getNotNullText(tietAddress3));
        guarantor.setPerCity(Utils.getNotNullText(tietCity));
        guarantor.setP_pin(Utils.getNotNullInt(tietPinCode));

        guarantor.setGender(((RangeCategory) acspGender.getSelectedItem()).RangeCode.substring(0, 1));
        guarantor.setResFax(((RangeCategory) acspRelationship.getSelectedItem()).RangeCode);
        guarantor.setP_StateID(((RangeCategory) acspAadharState.getSelectedItem()).RangeCode);

        guarantor.setPerMob1(Utils.getNotNullText(tietMobile));

        guarantor.setVoterid(Utils.getNotNullText(tietVoter));
        guarantor.setDrivinglic(Utils.getNotNullText(tietDrivingLic));
        guarantor.setPANNo(Utils.getNotNullText(tietPan));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgViewScanQR:
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.setOrientationLocked(false);
                scanIntegrator.initiateScan(Collections.singleton("QR_CODE"));
                break;
            case R.id.btnGuarantorUpdate:
                updateGuarantor();
//                finish();
                break;
            case R.id.btnGuarantorDelete:
                deleteGuarantor();
                //guarantor = null;
                break;
            case R.id.imgViewAadharPhoto:
                ImageType=0;
                cropState = true;
                ImagePicker.with(this)
                        .cameraOnly()
                        .start(CameraUtils.REQUEST_TAKE_PHOTO);
//                try {
//                    CameraUtils.dispatchTakePictureIntent(this);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                break;
            case R.id.imgViewCal:
                Date dob = DateUtils.getParsedDate(tietDob.getText().toString(), "dd-MMM-yyyy");
                myCalendar.setTime(dob);
                new DatePickerDialog(ActivityGuarantorEntry.this, dateSetListner,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
                break;
            case R.id.tietDob:
                 Toast.makeText(getApplicationContext(), "Click Calendar Icon", Toast.LENGTH_SHORT).show();
                 break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                if (scanFormat != null) {
                    try {
                        setAadharContent(scanContent);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        if (requestCode == CameraUtils.REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {

                if (data!=null){
                    uriPicture = data.getData();
                    CropImage.activity(uriPicture)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(45, 52)
                            .start(this);
                }
                else {
                    Toast.makeText(ActivityGuarantorEntry.this, "Image Data Null", Toast.LENGTH_SHORT).show();
                }
//                CropImage.activity(this.uriPicture)
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setAspectRatio(45, 52)
//                        .start(this);
            } else {
                Utils.alert(this, "Could not take Picture");
            }
        }else if(requestCode == 1000){
            uriPicture = data.getData();
            if(uriPicture!=null){
                CropImage.activity(uriPicture)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAllowFlipping(true)
                        .setMultiTouchEnabled(true)
                        .start(  ActivityGuarantorEntry.this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {
            Exception error = null;

            if (data!=null){
                Uri imageUri = CameraUtils.finaliseImageCropUri(resultCode, data, 300, error, false);
                File tempCroppedImage = new File(imageUri.getPath());

                Log.e("tempCroppedImageGrntr",tempCroppedImage.getAbsolutePath()+"");
                if (error ==null){

                    if (imageUri!=null){
                        if (ImageType==1){
                            //adharFrontImg.setImageBitmap(myBitmap);
                            setDataOfAdhar(tempCroppedImage,"aadharfront","aadhar");
                        }else if (ImageType==2){
                            //adharBackImg.setImageBitmap(myBitmap);
                            setDataOfAdhar(tempCroppedImage,"aadharback","aadhar");
                        }else if(ImageType==0){
                            if (tempCroppedImage.length()>100){
                                if (guarantor != null) {
                                    (new File(this.uriPicture.getPath())).delete();


                                    try {

//                                    if (android.os.Build.VERSION.SDK_INT >= 29) {
//                                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), imageUri));
//                                    } else {
//                                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                                    }
//
//
//                                    ImageString = bitmapToBase64(bitmap);

                                        File croppedImage = CameraUtils.moveCachedImage2Storage(this, tempCroppedImage, true);
                                        Log.e("croppedImageGRN",croppedImage.getAbsolutePath()+"");
                                        Log.e("croppedImageGRN1",croppedImage.getPath()+"");
                                        guarantor.setPicture(croppedImage.getPath());
                                        //guarantor.setPictureGuarantor(croppedImage.getPath(),ImageString);
                                        guarantor.save();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            else {
                                Log.e("tempCroppedImage",tempCroppedImage.length()+"");
                                //Toast.makeText(ActivityGuarantorEntry.this, tempCroppedImage.length()+"", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }else {
                        Log.e("imageUriguarantor",imageUri.toString()+"");
                        //Toast.makeText(ActivityGuarantorEntry.this, imageUri.toString()+"", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Log.e("ErrorGuarantor",error.toString()+"");
                    //Toast.makeText(ActivityGuarantorEntry.this, error.toString()+"", Toast.LENGTH_SHORT).show();
                }
            }else {
                Log.e("CropImageDataGuarantor","Null");
                //Toast.makeText(ActivityGuarantorEntry.this, "CropImage data: NULL" , Toast.LENGTH_SHORT).show();
            }

        }

//        else{
//            Exception error = null;
//
//            if (data!=null){
//                Uri imageUri = CameraUtils.finaliseImageCropUri(resultCode, data, 300, error, false);
//                File tempCroppedImage = new File(imageUri.getPath());
//
//                Log.e("tempCroppedImageGrntr",tempCroppedImage.getAbsolutePath()+"");
//                if (error ==null){
//
//                    if (imageUri!=null){
//                        if (tempCroppedImage.length()>100){
//                            if (guarantor != null) {
//                                (new File(this.uriPicture.getPath())).delete();
//
//
//                                try {
//
////                                    if (android.os.Build.VERSION.SDK_INT >= 29) {
////                                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), imageUri));
////                                    } else {
////                                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
////                                    }
////
////
////                                    ImageString = bitmapToBase64(bitmap);
//
//                                    File croppedImage = CameraUtils.moveCachedImage2Storage(this, tempCroppedImage, true);
//                                    if (ImageType==1){
//                                        //adharFrontImg.setImageBitmap(myBitmap);
//                                        setDataOfAdhar(croppedImage,"aadharfront","aadhar");
//                                    }else if (ImageType==2){
//                                        //adharBackImg.setImageBitmap(myBitmap);
//                                        setDataOfAdhar(croppedImage,"aadharback","aadhar");
//                                    }
//                                    Log.e("croppedImageGRN",croppedImage.getAbsolutePath()+"");
//                                    Log.e("croppedImageGRN1",croppedImage.getPath()+"");
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                        else {
//                            Log.e("tempCroppedImage",tempCroppedImage.length()+"");
//                            //Toast.makeText(ActivityGuarantorEntry.this, tempCroppedImage.length()+"", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }else {
//                        Log.e("imageUriguarantor",imageUri.toString()+"");
//                        //Toast.makeText(ActivityGuarantorEntry.this, imageUri.toString()+"", Toast.LENGTH_SHORT).show();
//                    }
//
//                }else {
//                    Log.e("ErrorGuarantor",error.toString()+"");
//                    //Toast.makeText(ActivityGuarantorEntry.this, error.toString()+"", Toast.LENGTH_SHORT).show();
//                }
//            }else {
//                Log.e("CropImageDataGuarantor","Null");
//                //Toast.makeText(ActivityGuarantorEntry.this, "CropImage data: NULL" , Toast.LENGTH_SHORT).show();
//            }
//        }
    }
    private void setDataOfAdhar(File croppedImage,String imageData,String type) {
        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button.
        progressBar.setMessage("Data Fetching from Aadhaar Please wait...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1,TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SEILIGL.NEW_SERVERAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        RequestBody surveyBody = RequestBody.create(MediaType.parse("*/*"), croppedImage);
        builder.addFormDataPart("file",croppedImage.getName(),surveyBody);


        RequestBody requestBody = builder.build();
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<JsonObject> call=apiInterface.getAdharDataByOCR(imageData,type,requestBody);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("TAG", "onResponsews: "+response.body());

                if (response.body()!=null){
                    if (response.body().get("data")!=null){
                        if (imageData.equals("aadharfront")){
                            if (response.body().get("data").getAsJsonArray().size()>0){
                                if (response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString().trim().length()>2){

                                    String[] borrowerNames=response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString().split(" ");
                                    switch (borrowerNames.length){
                                        case 1:
                                            guarantor.setName(borrowerNames[0]);
                                            break;
                                        case 2:
                                            guarantor.setName(borrowerNames[0]+" "+borrowerNames[1]);

                                            break;
                                        case 3:
                                            guarantor.setName(borrowerNames[0]+" "+borrowerNames[1]+" "+borrowerNames[2]);

                                            break;
                                    }



                                    guarantor.setGender(response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("gender").getAsString().charAt(0)+"");
                                    guarantor.setAadharid(response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("aadharno").getAsString().replace(" ",""));

                                    Date date;
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    try {
                                        date = formatter.parse(response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("dob").getAsString());
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Instant instant = null;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        instant = date.toInstant();
                                        ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                                        LocalDate givenDate = zone.toLocalDate();

                                        Period period = Period.between(givenDate, LocalDate.now());

                                        guarantor.setAge(period.getYears());
                                        System.out.print(period.getYears()+" years "+period.getMonths()+" and "+period.getDays()+" days");
                                    }
                                    guarantor.setDOB(date);
                                    guarantor.setIsAadharVerified("O");
                                    guarantor.save();
                                    setDataToView(findViewById(android.R.id.content).getRootView());

                                }else{
                                    Utils.alert(ActivityGuarantorEntry.this,"Please capture aadhaar front image!!");

                                    Toast.makeText(ActivityGuarantorEntry.this, "Please capture aadhaar front image!!", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Utils.alert(ActivityGuarantorEntry.this,"Please capture aadhaar front image!!");

                            }

                            progressBar.dismiss();
                        }else if(imageData.equals("aadharback")){
                            if (response.body().get("data").getAsJsonArray().size()>0){
                                if (response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("address1").getAsString().trim().length()>2) {

                                    guarantor.setP_pin( Integer.parseInt(response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("pincode").getAsString().trim()));
                                    JsonObject jsonObject=response.body().get("data").getAsJsonArray().get(0).getAsJsonObject();
                                    String fullAddress=jsonObject.get("address1").getAsString().trim()+jsonObject.get("address2").getAsString().trim()+jsonObject.get("address3").getAsString().trim()+jsonObject.get("address4").getAsString().trim();
                                    String[] arrOfAdd=fullAddress.split(",");
                                    String city=arrOfAdd[arrOfAdd.length-2];

                                    guarantor.setPerCity(city);
//                                try {
//
//                                    borrower.P_city = response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("address2").getAsString().split(",")[2].trim();
//                                } catch (Exception e) {
//                                    borrower.P_city = response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("address3").getAsString().split(",")[2].trim();
//
//                                }
                                    if (response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("address4").getAsString().length() > 1) {

                                        guarantor.setPerAdd1(response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("address3").getAsString().trim());
                                    }
                                    guarantor.setPerAdd2(response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("address2").getAsString());


                                    String[] address1 = response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("address1").getAsString().split(",");
                                    for (int i = 0; i < address1.length; i++) {
                                        if (address1[i].toUpperCase().contains("S/O") || address1[i].toUpperCase().contains("D/O") || address1[i].toUpperCase().contains("W/O")){
                                            guarantor.setGurName(address1[i]);
                                            continue;
                                        }
                                        guarantor.setPerAdd1(guarantor.getPerAdd1() + address1[i]);

                                    }
                                    guarantor.setP_StateID(AadharUtils.getStateCode(response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("state").getAsString().trim()));
                                    guarantor.setIsAadharVerified("O");

                                    guarantor.save();
                                    setDataToView(findViewById(android.R.id.content).getRootView());


                                }else{
                                    Utils.alert(ActivityGuarantorEntry.this,"Please capture aadhaar back image!!");
                                    //   Toast.makeText(ActivityBorrowerKyc.this, "Please capture aadhaar back image!!", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Utils.alert(ActivityGuarantorEntry.this,"Please capture aadhaar back image again!!");

                                Toast.makeText(ActivityGuarantorEntry.this, "Please capture aadhaar back image again!!", Toast.LENGTH_SHORT).show();
                            }

                            progressBar.dismiss();
                        }

                        //   borrower.setNames(response.body().get("data").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString());
                    }else{
                        progressBar.dismiss();
                    }
                }else{
                    progressBar.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
                progressBar.dismiss();
            }
        });









    }
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

//    private void setAadharContent(String aadharDataString) {
//        try {
//            AadharData aadharData = AadharUtils.getAadhar(AadharUtils.ParseAadhar(aadharDataString));
//            if (aadharData.Address2 == null) {
//                aadharData.Address2 = aadharData.Address3;
//                aadharData.Address3 = null;
//            } else if (aadharData.Address2.trim().equals("")) {
//                aadharData.Address2 = aadharData.Address3;
//                aadharData.Address3 = null;
//            }
//            if (aadharData.Address1 == null) {
//                aadharData.Address1 = aadharData.Address2;
//                aadharData.Address2 = aadharData.Address3;
//                aadharData.Address3 = null;
//            } else if (aadharData.Address1.trim().equals("")) {
//                aadharData.Address1 = aadharData.Address2;
//                aadharData.Address2 = aadharData.Address3;
//                aadharData.Address3 = null;
//            }
//            guarantor.setIsAadharVerified("Q");
//            guarantor.setName(aadharData.Name);
//            guarantor.setDOB(aadharData.DOB);
//            guarantor.setAge(aadharData.Age);
//            guarantor.setGender(aadharData.Gender);
//            guarantor.setGurName(aadharData.GurName);
//            guarantor.setPerCity(aadharData.City);
//            guarantor.setP_pin(aadharData.Pin);
//            guarantor.setPerAdd1(aadharData.Address1);
//            guarantor.setPerAdd2(aadharData.Address2);
//            guarantor.setPerAdd3(aadharData.Address3);
//            guarantor.setP_StateID(AadharUtils.getStateCode(aadharData.State));
//            if (aadharData.AadharId != null)
//                guarantor.setAadharid(aadharData.AadharId);
//            setDataToView(this.findViewById(android.R.id.content).getRootView());
//        } catch (Exception ex) {
//            Utils.alert(this, ex.getMessage());
//        }
//    }
private void setAadharContent(String aadharDataString) throws Exception {
//        try {
    Log.d("CheckXMLDATA2", "AadharData:->" + aadharDataString);
    if (aadharDataString.toUpperCase().contains("XML")) {

        Log.d("XML printing", "AadharData:->" + aadharDataString);
        //AadharData aadharData = AadharUtils.getAadhar(aadharDataString);
        AadharData aadharData = AadharUtils.getAadhar(AadharUtils.ParseAadhar(aadharDataString));


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

        guarantor.setIsAadharVerified(aadharData.isAadharVerified);
        guarantor.setName(aadharData.Name);
        guarantor.setDOB(aadharData.DOB);
        guarantor.setAge(aadharData.Age);
        guarantor.setGender(aadharData.Gender);
        guarantor.setGurName(aadharData.GurName==null?"":aadharData.GurName);
        guarantor.setPerCity(aadharData.City);
        guarantor.setP_pin( aadharData.Pin);
        guarantor.setPerAdd1(aadharData.Address1);
        guarantor.setPerAdd2(aadharData.Address2);
        guarantor.setPerAdd3(aadharData.Address3);
        guarantor.setP_StateID(AadharUtils.getStateCode(aadharData.State));
        setDataToView(this.findViewById(android.R.id.content).getRootView());

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
    }
//            } catch(Exception ex) {
//            Utils.alert(this, ex.getMessage());
//        }

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

            guarantor.setAge(period.getYears());
            System.out.print(period.getYears()+" years "+period.getMonths()+" and "+period.getDays()+" days");
        }

        guarantor.setAadharid(decodedData.get(2-inc));
        guarantor.setGender(decodedData.get(5-inc));
        if (decodedData.get(13-inc).equals("")||decodedData.get(13-inc).equals(null)){
        }else{
            guarantor.setP_StateID( AadharUtils.getStateCode(decodedData.get(13-inc)));
        }
        if (decodedData.get(3-inc).equals("")||decodedData.get(3-inc).equals(null)){
            //tietName.setEnabled(true);
        }else{
            guarantor.setName(decodedData.get(3-inc));
        }
        if (decodedData.get(4-inc).equals("")||decodedData.get(4-inc).equals(null)){
            //tietDob.setEnabled(true);
        }else{
            guarantor.setDOB(date);
        }
        if (decodedData.get(6-inc).equals("")||decodedData.get(6-inc).equals(null)){

        }else{
            guarantor.setGurName(decodedData.get(6-inc));
        }

        if (decodedData.get(7-inc).equals("")||decodedData.get(7-inc).equals(null)){
            // tietCity.setEnabled(true);
        }else{
            guarantor.setPerCity(decodedData.get(7-inc));
        }

        if (decodedData.get(11-inc).equals("")||decodedData.get(11-inc).equals(null)){
        }else{
            guarantor.setP_pin(Integer.parseInt(decodedData.get(11-inc)));
        }


        if (decodedData.get(10-inc).equals("")||decodedData.get(10-inc).equals(null)){
            //  tietAddress3.setEnabled(true);
        }else{
           guarantor.setPerAdd3(decodedData.get(10-inc));
        }

        try{
            if (decodedData.get(9-inc).equals("")||decodedData.get(9-inc).equals(null)){
                tietAddress2.setEnabled(true);
            }else{

                guarantor.setPerAdd1(decodedData.get(9-inc));
                guarantor.setPerAdd2(decodedData.get(14-inc));
            }
        }catch (Exception e){
            tietAddress2.setEnabled(true);
        }


        //borrower.P_city = decodedData.get(7);

        // borrower.P_Add1 = decodedData.get(9);
        // borrower.P_add2 = decodedData.get(8);
        // borrower.P_add3 = decodedData.get(10);

        setDataToView(this.findViewById(android.R.id.content).getRootView());
        tietAge.setEnabled(false);
        tietDob.setEnabled(false);
        acspAadharState.setEnabled(false);
        acspGender.setEnabled(false);


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
    @Override
    public void cameraCaptureUpdate(Uri uriImage) {
        this.uriPicture = uriImage;
    }

    @Override
    public void onResume() {
        super.onResume();
        showGuarantor();
    }

    private void showPicture() {

//        if (guarantor.getPictureGuarantor()!=null){
//            imageView.setImageBitmap(StringToBitmap(guarantor.getPictureGuarantor()));
//        }
        if (guarantor.getPicture() != null && (new File(guarantor.getPicture().getPath())).length() > 100) {

            setImagepath(new File(guarantor.getPicture().getPath()));
            //Glide.with(this).load(guarantor.getPicture().getPath()).override(Target.SIZE_ORIGINAL, 300).into(imageView);
        } else {
            DocumentStore documentStore = guarantor.getPictureStore();
            if (documentStore != null && documentStore.updateStatus) {
                Glide.with(this).load(R.mipmap.picture_uploaded).override(300, 300).into(imageView);
                imageView.setOnClickListener(null);
            }
        }
    }

    private Bitmap StringToBitmap(String string){
        try {
            byte [] encodebyte  = Base64.decode(string, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodebyte,0,encodebyte.length);
        } catch (Exception e){
            e.getMessage();
            return null;
        }
    }

    private void setImagepath(File file) {

//        File imgFile = new  File("/sdcard/Images/test_image.jpg");

        if(file.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

            if (myBitmap!=null){
                imageView.setImageBitmap(myBitmap);
            }else {
                //Toast.makeText(ActivityGuarantorEntry.this, "Bitmap Null", Toast.LENGTH_SHORT).show();
                Log.e("BitmapImage","Null");
            }
        }else  {
            Toast.makeText(ActivityGuarantorEntry.this, "Filepath Empty", Toast.LENGTH_SHORT).show();

        }
    }

    private Guarantor getGuarantor(long gid, long bid) {
        guarantor = null;
        if (gid > 0) {
            guarantor = SQLite.select().from(Guarantor.class).where(Guarantor_Table.id.eq(gid)).querySingle();
        }
        if (guarantor == null) {
            guarantor = new Guarantor();
            guarantor.setP_StateID(borrower.p_state);
            guarantor.associateBorrower(borrower);
            guarantor.setIsAadharVerified("N");
            //guarantor.setUserID();
        }
        return guarantor;
    }

    private void showGuarantor() {
        if (guarantor != null) {
            setDataToView(this.findViewById(android.R.id.content).getRootView());
        }
    }

    private void updateGuarantor() {
        if (guarantor != null) {
            getDataFromView(this.findViewById(android.R.id.content).getRootView());
            guarantor.associateBorrower(borrower);
            if (guarantor.getPictureStore() == null) {
                Toast.makeText(this,"Guarantor Picture not captured",Toast.LENGTH_SHORT).show();
                //Utils.alert(this, "Guarantor Picture not captured");
            } else {
                if (validateGuarantor()) {
                    guarantor.save();
                    finish();
                } else {
                    Toast.makeText(this,"There is at least one errors in the Guarantor Data",Toast.LENGTH_SHORT).show();
                    //Utils.alert(this, "There is at least one errors in the Guarantor Data");
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateGuarantor();
    }

    @Override
    public void onBackPressed() {
        updateGuarantor();
        finish();
        super.onBackPressed();
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
                }
                break;
            case R.id.tietName:
                if (editText.length() < 3) {
                    editText.setError("Should be more than 3 Characters");
                    retVal = false;
                }
                break;
            case R.id.tietDob:

                if (editText.length() < 3) {
                    editText.setError("Should be more than 3 Characters");
                    retVal = false;
                }
                break;
            case R.id.tietAge:
                if (text.length() == 0) text = "0";
                int age = Integer.parseInt(text);
                if (age < 21) {
                    editText.setError("Age should be greater than 21");
                    retVal = false;
                } else if (age > 57) {
                    editText.setError("Age should be less than 57");
                    retVal = false;
                }
                tietDob.setEnabled(retVal);

                break;
            case R.id.tietAddress1:
                if (editText.getText().toString().trim().length() < 6) {
                    editText.setError("Should be more than 5 Characters");
                    retVal = false;
                }
                break;
            case R.id.tietCity:
                if (editText.getText().toString().trim().length() < 3) {
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

            case R.id.tietVoter:
            case R.id.tietPAN:
            case R.id.tietDrivingLlicense:
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
        }
        return retVal;
    }

    private boolean validateGuarantor() {
        boolean retVal = true;
        retVal &= validateControls(tietAadharId, tietAadharId.getText().toString());
        retVal &= validateControls(tietName, tietName.getText().toString());
        retVal &= validateControls(tietAge, tietAge.getText().toString());
        retVal &= validateControls(tietDob, tietDob.getText().toString());
        retVal &= validateControls(tietAddress1, tietAddress1.getText().toString());
        retVal &= validateControls(tietCity, tietCity.getText().toString());
        retVal &= validateControls(tietPinCode, tietPinCode.getText().toString());
        retVal &= (validateControls(tietVoter, tietVoter.getText().toString())
                || validateControls(tietPan, tietPan.getText().toString())
                || validateControls(tietDrivingLic, tietDrivingLic.getText().toString()));

        return retVal;
    }

    private void deleteGuarantor() {
        DataAsyncResponseHandler asyncResponseHandler = new DataAsyncResponseHandler(this, "Loan Financing", "Fetching Co-Borrower / Guarantor 1 Data") {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String jsonString = new String(responseBody);
                guarantor.delete();
                guarantor = null;
                finish();
            }
        };
        RequestParams params = new RequestParams();
        params.add("FiCode", String.valueOf(borrower.Code));
        params.add("Creator", guarantor.getCreator());
        params.add("GrNo", String.valueOf(guarantor.getGrNo()));

        (new WebOperations()).getEntity(this, "POSGuarantor", "DeleteGuarantor", params, asyncResponseHandler);

    }

}
