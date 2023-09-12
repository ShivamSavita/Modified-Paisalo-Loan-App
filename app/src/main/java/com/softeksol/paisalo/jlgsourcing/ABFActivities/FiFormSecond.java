package com.softeksol.paisalo.jlgsourcing.ABFActivities;

import static com.softeksol.paisalo.jlgsourcing.Utilities.CameraUtils.REQUEST_TAKE_PHOTO;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softeksol.paisalo.dealers.Adapters.AddFemIncomeAdapter;
import com.softeksol.paisalo.dealers.DealerOnBoard;
import com.softeksol.paisalo.jlgsourcing.ABFActivities.Adapter.OEMByDealerAdapter;
import com.softeksol.paisalo.jlgsourcing.ABFActivities.Model.BrandResponse;
import com.softeksol.paisalo.jlgsourcing.ABFActivities.Model.OEMByCreatorModel;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.AadharUtils;
import com.softeksol.paisalo.jlgsourcing.Utilities.CameraUtils;
import com.softeksol.paisalo.jlgsourcing.Utilities.DateUtils;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.adapters.AdapterListRange;
import com.softeksol.paisalo.jlgsourcing.entities.AadharData;
import com.softeksol.paisalo.jlgsourcing.entities.Borrower;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerFamilyMember;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory_Table;
import com.softeksol.paisalo.jlgsourcing.entities.ReceiptData;
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
import java.util.zip.GZIPInputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FiFormSecond extends AppCompatActivity {
Button addFemIncomeBtn;
    ApiInterface apiInterface;
    private Calendar myCalendar;

AppCompatSpinner acspGenderNominee,acspRelationshipNominee,acspSelectOem,acspSelectDelear,acspSelectProduct;

ImageView imgViewAadharPhotoNominee,imgViewAadharPhoto,imgViewScanQRNominee;

TextView tilBankAccountName,howOldAccount,tvLoanAppFinanceBankName,tvLoanAppFinanceBankBranch;
RecyclerView addFemIncomeRecView;
Button BtnFinalSaveKYCDataABF;
    protected int emailMobilePresent, imageStartIndex, imageEndIndex;

TextInputEditText tietDobNominee, tietAgeNominee,tietNameNominee,tietAadharNominee,textProductPrice, tietVoterNominee,tietMobileNominee, tietGuardianNominee,etLoanAppFinanceBankIfsc,etLoanAppFinanceBankAccountNo, tietIncomeMonthly,tietRentalIncome,tietFutureIncome,tietAgriIncome,tietIntrestIncome,tietOtherIncome,tietRentExpense,tietFoodExpense,tietEduExpense,tietHealthExpense,tietTravelExpense,tietSOcietyExpense,tietUtilExpense,tietOtherExpense;

    protected static final byte SEPARATOR_BYTE = (byte)255;
    protected ArrayList<String> decodedData;
    protected static final int VTC_INDEX = 15;
    protected String signature,email,mobile;
    Manager manager;
int size=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fi_form_second);
        manager = (Manager) getIntent().getSerializableExtra(Global.MANAGER_TAG);
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
        imgViewAadharPhoto=findViewById(R.id.imgViewAadharPhoto);
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
        apiInterface=new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());
        List<RangeCategory> genders = new ArrayList<>();
        Log.d("TAG", "onCreate: "+manager.Creator+"///"+SEILIGL.NEW_TOKEN);
        Call<BrandResponse> getCreatorByOem=apiInterface.getOEMbyCreator(SEILIGL.NEW_TOKEN, manager.Creator);
        getCreatorByOem.enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {

                BrandResponse brandResponse=response.body();
                Gson gson = new Gson();
                OEMByCreatorModel[] nameList = gson.fromJson(brandResponse.getData(), OEMByCreatorModel[].class);
                acspSelectOem.setAdapter(new OEMByDealerAdapter(FiFormSecond.this,nameList));
            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {

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

        addFemIncomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                showFamilyMemberDialog();

//                size++;
//                addFemIncomeRecView.setAdapter(new AddFemIncomeAdapter(FiFormSecond.this,size));
//                new AddFemIncomeAdapter(FiFormSecond.this,size).notifyDataSetChanged();
//                Toast.makeText(FiFormSecond.this, "New Member Added!!", Toast.LENGTH_SHORT).show();
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
//        else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//
//
//            if (data != null) {
//                uriPicture = data.getData();
//                CropImage.activity(uriPicture)
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setAspectRatio(45, 52)
//                        .start(  FiFormSecond.this);
//            } else {
//                Log.e("ImageData","Null");
//                Toast.makeText(this, "Image Data Null", Toast.LENGTH_SHORT).show();
//            }
//
//        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Exception error = null;


//            if (data != null) {
//                Uri imageUri = CameraUtils.finaliseImageCropUri(resultCode, data, 300, error, false);
//                //Toast.makeText(activity, imageUri.toString(), Toast.LENGTH_SHORT).show();
//                File tempCroppedImage = new File(imageUri.getPath());
//                Log.e("tempCroppedImage",tempCroppedImage.getPath()+"");
//
//
//                if (error == null) {
//
//                    if (imageUri != null) {
//
//                        if (tempCroppedImage.length() > 100) {
//
//                            if (borrower != null) {
//                                (new File(uriPicture.getPath())).delete();
//                                try {
//
//
////                                    if (android.os.Build.VERSION.SDK_INT >= 29) {
////                                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getActivity().getContentResolver(), imageUri));
////                                    } else {
////                                        bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);
////                                    }
////
////
////                                    ImageString = bitmapToBase64(bitmap);
//
//                                    File croppedImage = CameraUtils.moveCachedImage2Storage(this, tempCroppedImage, true);
////                                    if (android.os.Build.VERSION.SDK_INT >= 29) {
////                                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getActivity().getContentResolver(), imageUri));
////                                    } else {
////                                        bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imageUri);
////                                    }
////                                    //Log.e("CroppedImageMyBitmap", bitmap+ "");
//                                    Log.e("CroppedImageFile1", croppedImage.getPath()+"");
//                                    Log.e("CroppedImageFile2", croppedImage.getAbsolutePath()+"");
//                                    Log.e("CroppedImageFile3", croppedImage.getCanonicalPath()+"");
//                                    Log.e("CroppedImageFile4", croppedImage.getParent()+"");
//                                    Log.e("CroppedImageFile5", croppedImage.getParentFile().getCanonicalPath()+"");
//                                    Log.e("CroppedImageFile6", croppedImage.getParentFile().getName()+"");
////
////                                    ImageString = bitmapToBase64(bitmap);
//
////                                    borrower.setPicture(croppedImage.getPath(),ImageString);
//                                    borrower.setPicture(croppedImage.getPath());
//                                    borrower.Oth_Prop_Det = null;
//                                    borrower.save();
//                                    showPicture(borrower);
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                        else {
//                            Toast.makeText(this, "CroppedImage FIle Length:"+tempCroppedImage.length() + "", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(this, imageUri.toString() + "", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(this, error.toString() + "", Toast.LENGTH_SHORT).show();
//                }
//            } else {
////                Log.e("Error",data.getData()+"");
//                Toast.makeText(this, "CropImage data: NULL", Toast.LENGTH_SHORT).show();
//            }

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
        AppCompatSpinner acspBusinessDetail=dialogView.findViewById(R.id.acspBusinessDetail);
        AppCompatSpinner acspLoanReason=dialogView.findViewById(R.id.acspLoanReason);
        AppCompatSpinner spinIncomeFamMemType=dialogView.findViewById(R.id.spinIncomeFamMemType);
        EditText spinIncomeFamMem=dialogView.findViewById(R.id.acspIncomeFamMem);
        Button femDialogaddBtn=dialogView.findViewById(R.id.femDialogaddBtn);
        ArrayList<RangeCategory> relationSips = new ArrayList<>();
        relationSips.add(new RangeCategory("Husband", ""));
        relationSips.add(new RangeCategory("Father", ""));
        relationSips.add(new RangeCategory("Mother", ""));
        acspBusinessDetail.setAdapter(new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("occupation-type")).queryList(), false));
        acspLoanReason.setAdapter(new AdapterListRange(this,
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
                //addUpdateMember(dialogView, familyMember);
                dialog.dismiss();
            }
        });


        dialog.show();
    }
}