package com.softeksol.paisalo.dealers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.kyanogen.signatureview.SignatureView;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.DateUtils;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

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

public class AddOemBank extends AppCompatActivity {
    TextInputEditText branchName,ifscCode,Account_Number,bankName,OEMName,tietAccOenDt;
    Spinner firm_accountType;
    Button btnSaveAccount;
    SignatureView signatureView;
    private Calendar myCalendar;
    ImageView imgViewCalBank;
    String accountOpeningDate;
    int OEMId;
    private DatePickerDialog.OnDateSetListener dateSetListner;
    Intent i;
    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_oem_bank);
        firm_accountType=findViewById(R.id.firm_accountType);
        i=getIntent();
        apiInterface=  new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);

        OEMId=i.getIntExtra("OEMId",0);
        branchName=findViewById(R.id.branchName);
        ifscCode=findViewById(R.id.ifscCode);
        Account_Number=findViewById(R.id.Account_Number);
        bankName=findViewById(R.id.bankName);
        OEMName=findViewById(R.id.OEMName);
        btnSaveAccount=findViewById(R.id.btnSaveAccount);
        signatureView=findViewById(R.id.signatureView);
        tietAccOenDt=findViewById(R.id.tietAccOenDt);
        imgViewCalBank=findViewById(R.id.imgViewCalBank);
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());
        tietAccOenDt.setEnabled(false);
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
                if (signatureView.isBitmapEmpty()){
                    Toast.makeText(AddOemBank.this, "Please do your signature", Toast.LENGTH_SHORT).show();
                }else{
                    Bitmap bitmap = signatureView.getSignatureBitmap();
                    Log.d("TAG", "onClick: "+bitmap);
                    File dir = new File("//sdcard//Download//");


                    File f = new File(dir, "OEMSignature.png");
                    try {
                        f.createNewFile();
                        Log.d("TAG", "file created");

                    } catch (IOException e) {
                        Log.d("TAG", "onClick: Runtime IO "+e);

                        throw new RuntimeException(e);
                    }

//Convert bitmap to byte array

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(f);
                        fos.write(bitmapdata);
                        Log.d("TAG", "file done");


                        DownloadManager downloadManager = (DownloadManager) getBaseContext().getSystemService(DOWNLOAD_SERVICE);

                        downloadManager.addCompletedDownload(f.getName(), f.getName(), true, "text/plain",f.getAbsolutePath(),f.length(),true);
                        fos.flush();
                        fos.close();
                        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), f);
                       MultipartBody.Part signatureimage = MultipartBody.Part.createFormData("Signature",f.getName(),surveyBody);
                        RequestBody allOtherData = RequestBody.create(MediaType.parse("multipart/form-data"),getJsonOfData());
                        Call<ResponseBody> call=apiInterface.uploadBankOemDetails(signatureimage,allOtherData);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Log.d("TAG", "onResponse: "+response.body());
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.d("TAG", "onFailure: "+t.getMessage());
                            }
                        });



                    } catch (FileNotFoundException e) {
                        Log.d("TAG", "onClick: file not found"+e);
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        Log.d("TAG", "onClick: IO exception"+e);

                        throw new RuntimeException(e);
                    }

                }


            }
        });





    }

    private String getJsonOfData() {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("UserId",OEMId);
        jsonObject.addProperty("Name",OEMName.getText().toString().trim());
        jsonObject.addProperty("BankName",bankName.getText().toString().trim());
        jsonObject.addProperty("AccountN",Account_Number.getText().toString().trim());
        jsonObject.addProperty("Ifsc",ifscCode.getText().toString().trim());
        jsonObject.addProperty("Branch",branchName.getText().toString().trim());
        jsonObject.addProperty("AccountT",firm_accountType.getSelectedItem().toString());
        jsonObject.addProperty("OpenDate",accountOpeningDate);
        jsonObject.addProperty("UserType","OEM");
        Log.d("TAG", "onClick: "+jsonObject);
        return  jsonObject.toString();
    }
}