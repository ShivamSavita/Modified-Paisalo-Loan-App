package com.softeksol.paisalo.dealers;

import static com.softeksol.paisalo.jlgsourcing.Utilities.CameraUtils.REQUEST_TAKE_PHOTO;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kyanogen.signatureview.SignatureView;
import com.softeksol.paisalo.dealers.Adapters.BankAdapter;
import com.softeksol.paisalo.dealers.Adapters.BankListAdapter;
import com.softeksol.paisalo.dealers.Models.BankListMaster;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.dealers.Models.CreatorAllResponse;
import com.softeksol.paisalo.dealers.Models.OEMBankResponse;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.CameraUtils;
import com.softeksol.paisalo.jlgsourcing.Utilities.DateUtils;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOemBank extends AppCompatActivity implements CameraUtils.OnCameraCaptureUpdate {
    TextInputEditText branchName,ifscCode,Account_Number,OEMName,tietAccOenDt;
    Spinner bankName;
    Spinner firm_accountType;
    Button btnSaveAccount,clickSignatureBtn;
    ImageView signatureView;
    private Calendar myCalendar;

    BankListAdapter adapter;
    ImageView imgViewCalBank;
    private Uri uriPicture;
    String accountOpeningDate;
    int OEMId;
    File croppedImage = null;
    private Boolean cropState = false;
    String bank;
    String userType;
    private DatePickerDialog.OnDateSetListener dateSetListner;
    Intent i;
    ApiInterface apiInterface,apiInterface1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_oem_bank);
        firm_accountType=findViewById(R.id.firm_accountType);
        i=getIntent();
        apiInterface=  new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);
        apiInterface1=  new ApiClient().getClient2(SEILIGL.NEW_SERVER_BASEURL_BETA).create(ApiInterface.class);

        OEMId=i.getIntExtra("Id",0);
        userType=i.getStringExtra("type");
        branchName=findViewById(R.id.branchName);
        ifscCode=findViewById(R.id.ifscCode);
        Account_Number=findViewById(R.id.Account_Number);
        bankName=findViewById(R.id.bankName);
        OEMName=findViewById(R.id.OEMName);
        btnSaveAccount=findViewById(R.id.btnSaveAccount);
        signatureView=findViewById(R.id.bankSignatueImage);
        tietAccOenDt=findViewById(R.id.tietAccOenDt);
        imgViewCalBank=findViewById(R.id.imgViewCalBank);
        clickSignatureBtn=findViewById(R.id.clickSignatureBtn);
        clickSignatureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cropState = true;
                ImagePicker.with(AddOemBank.this)
                        .cameraOnly()
                        .start(REQUEST_TAKE_PHOTO);


            }
        });
        Call<BrandResponse> call=apiInterface1.getBanks();
        call.enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                BrandResponse brandResponse=response.body();
                Log.d("TAG", "onResponse: "+response.body());
                Gson gson = new Gson();
                BankListMaster[] nameList = gson.fromJson(brandResponse.getData(), BankListMaster[].class);
                adapter=new BankListAdapter(AddOemBank.this,nameList);
                bankName.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {

            }
        });

        bankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BankListMaster bankListMaster= (BankListMaster) adapterView.getSelectedItem();
                bank=bankListMaster.getBankName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());
        tietAccOenDt.setEnabled(false);
        signatureView.setVisibility(View.GONE);
        imgViewCalBank .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddOemBank.this, dateSetListner,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        dateSetListner = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(year, monthOfYear, dayOfMonth);

                tietAccOenDt.setText(DateUtils.getFormatedDate(myCalendar.getTime(), "dd-MM-yyyy"));
                accountOpeningDate=DateUtils.getFormatedDate(myCalendar.getTime(), "yyyy-MM-dd");
            }
        };

        btnSaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (signatureView.isBitmapEmpty()){
//                    Toast.makeText(AddOemBank.this, "Please do your signature", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Bitmap bitmap = signatureView.getSignatureBitmap();
//                    Log.d("TAG", "onClick: "+bitmap);
//                    File dir = new File("//sdcard//Download//");
//
//
//                    File f = new File(dir, "OEMSignature.png");
//                    try {
//                        f.createNewFile();
//                        Log.d("TAG", "file created");
//
//                    } catch (IOException e) {
//                        Log.d("TAG", "onClick: Runtime IO "+e);
//
//                        throw new RuntimeException(e);
//                    }
//
////Convert bitmap to byte array
//
//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
//                    byte[] bitmapdata = bos.toByteArray();
//
////write the bytes in file
//                    FileOutputStream fos = null;
//                    try {
//                        fos = new FileOutputStream(f);
//                        fos.write(bitmapdata);
//                        Log.d("TAG", "file done");
//
//
//                        DownloadManager downloadManager = (DownloadManager) getBaseContext().getSystemService(DOWNLOAD_SERVICE);
//
//                        downloadManager.addCompletedDownload(f.getName(), f.getName(), true, "text/plain",f.getAbsolutePath(),f.length(),true);
//                        fos.flush();
//                        fos.close();
                        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), croppedImage);
                       MultipartBody.Part signatureimage = MultipartBody.Part.createFormData("Signature",croppedImage.getName(),surveyBody);

                        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                        builder.addFormDataPart("UserId", String.valueOf(OEMId))
                        .addFormDataPart("Name",OEMName.getText().toString().trim())
                        .addFormDataPart("BankName",bank)
                        .addFormDataPart("AccountNo",Account_Number.getText().toString().trim())
                        .addFormDataPart("Ifsc",ifscCode.getText().toString().trim())
                        .addFormDataPart("Branch",branchName.getText().toString().trim())
                        .addFormDataPart("AccountType",firm_accountType.getSelectedItem().toString())
                        .addFormDataPart("UserType",userType);

                        builder.addFormDataPart("Signature",croppedImage.getName(),surveyBody);

//
                        RequestBody requestBody = builder.build();
                        if (userType.equals("OEM")){
                            Call<BrandResponse> call=apiInterface.uploadBankOemDetails(SEILIGL.NEW_TOKEN,requestBody);

                            call.enqueue(new Callback<BrandResponse>() {
                                @Override
                                public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                                    BrandResponse brandResponse=response.body();

                                    Toast.makeText(AddOemBank.this, ""+brandResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<BrandResponse> call, Throwable t) {
                                    Log.d("TAG", "onFailure: "+t.getMessage());
                                }
                            });
                        }else if(userType.equals("Dealer")){
                            Call<BrandResponse> call=apiInterface.uploadBankDealerDetails(SEILIGL.NEW_TOKEN,requestBody);

                            call.enqueue(new Callback<BrandResponse>() {
                                @Override
                                public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                                    BrandResponse brandResponse=response.body();

                                    Toast.makeText(AddOemBank.this, ""+brandResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<BrandResponse> call, Throwable t) {
                                    Log.d("TAG", "onFailure: "+t.getMessage());
                                }
                            });

                        }




//                    } catch (FileNotFoundException e) {
//                        Log.d("TAG", "onClick: file not found"+e);
//                        throw new RuntimeException(e);
//                    } catch (IOException e) {
//                        Log.d("TAG", "onClick: IO exception"+e);
//
//                        throw new RuntimeException(e);
//                    }

                }


//            }
        });





    }
    public void open(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //IMAGE CAPTURE CODE
        startActivityForResult(intent, 0);
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            uriPicture = data.getData();
            CropImage.activity(uriPicture)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(45, 20)
                    .start(AddOemBank.this);
        }else{
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Exception error = null;
            Uri imageUri = CameraUtils.finaliseImageCropUri(resultCode, data, 300, error, false);
            Toast.makeText(AddOemBank.this, imageUri.toString(), Toast.LENGTH_SHORT).show();
            File tempCroppedImage = new File(imageUri.getPath());


            try {
                croppedImage = CameraUtils.moveCachedImage2Storage(AddOemBank.this, tempCroppedImage, true);

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
                Bitmap imgBitmap = BitmapFactory.decodeFile(croppedImage.getAbsolutePath());
                signatureView.setVisibility(View.VISIBLE);
            signatureView.setImageBitmap(imgBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }}

//        signatureView.setImageBitmap(bitmap);
    }
    private String getJsonOfData() {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("UserId",OEMId);
        jsonObject.addProperty("Name",OEMName.getText().toString().trim());
        jsonObject.addProperty("BankName",bank);
        jsonObject.addProperty("AccountN",Account_Number.getText().toString().trim());
        jsonObject.addProperty("Ifsc",ifscCode.getText().toString().trim());
        jsonObject.addProperty("Branch",branchName.getText().toString().trim());
        jsonObject.addProperty("AccountT",firm_accountType.getSelectedItem().toString());
        jsonObject.addProperty("OpenDate",accountOpeningDate);
        jsonObject.addProperty("UserType",userType);
        Log.d("TAG", "onClick: "+jsonObject);
        return  jsonObject.toString();
    }

    @Override
    public void cameraCaptureUpdate(Uri uriImage) {
        uriPicture = uriImage;
    }
}