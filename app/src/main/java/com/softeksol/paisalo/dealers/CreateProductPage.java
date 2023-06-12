package com.softeksol.paisalo.dealers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.softeksol.paisalo.dealers.Adapters.AlgorithmAdapter;
import com.softeksol.paisalo.dealers.Adapters.ModelTypeAdapter;
import com.softeksol.paisalo.dealers.Adapters.ProductListAdapter;
import com.softeksol.paisalo.dealers.Adapters.VehicaalAdapter;
import com.softeksol.paisalo.dealers.Models.BrandData;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.dealers.Models.ModelTypeResponse;
import com.softeksol.paisalo.dealers.Models.ProductDataModel;
import com.softeksol.paisalo.dealers.Models.VehicalDataModel;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.entities.BankData;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateProductPage extends AppCompatActivity {
Spinner spinnerModelType,spinnerVehicalType,spinnerFuelType;
int vehicalType;
int OEMid;
String BrandId;
Button uploadProductCert,btnSaveProduct;
ImageView imageCert,ImageCross;
TextView docName;
File certificateFile=null;
Intent intent;
TextInputEditText Val_MSP,Val_BSP;
ApiInterface apiInterface;
LinearLayout certImageBlok;
int ModelTypeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product_page);
        apiInterface=  new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);

        spinnerModelType=findViewById(R.id.spinnerModelType);
        spinnerFuelType=findViewById(R.id.spinnerFuelType);
        spinnerVehicalType=findViewById(R.id.spinnerVehicalType);
        uploadProductCert=findViewById(R.id.uploadProductCert);
        imageCert=findViewById(R.id.imageCert);
        docName=findViewById(R.id.docName);
        certImageBlok=findViewById(R.id.certImageBlok);
        ImageCross=findViewById(R.id.ImageCross);
        btnSaveProduct=findViewById(R.id.btnSaveProduct);
        Val_BSP=findViewById(R.id.Val_BSP);
        Val_MSP=findViewById(R.id.Val_MSP);









        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allFieldValidate()){
                    RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), certificateFile);
                    MultipartBody.Part signatureimage = MultipartBody.Part.createFormData("Signature",certificateFile.getName(),surveyBody);

                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    builder.addFormDataPart("OEMId", String.valueOf(OEMid))
                            .addFormDataPart("BrandId",BrandId)
                            .addFormDataPart("VehicleTypeId", String.valueOf(vehicalType))
                            .addFormDataPart("ModelId", "2")
                            .addFormDataPart("BSP",Val_BSP.getText().toString())
                            .addFormDataPart("MSP",Val_MSP.getText().toString())
                            .addFormDataPart("FuelType",spinnerFuelType.getSelectedItem().toString());

                    builder.addFormDataPart("Certificate",certificateFile.getName(),surveyBody);

//
                    RequestBody requestBody = builder.build();
                    Call<BrandResponse> call=apiInterface.CreateProduct(SEILIGL.NEW_TOKEN,requestBody);
                    call.enqueue(new Callback<BrandResponse>() {
                        @Override
                        public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                            Log.d("TAG", "onResponse: "+response.body().getMessage());
                            Log.d("TAG", "onResponse: "+response.body().getData());
                            Log.d("TAG", "onResponse: "+response.body().getStatusCode());
                            if (response.body().getStatusCode()==200){
                                Toast.makeText(CreateProductPage.this, "Product Created Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }else
                            {
                                Toast.makeText(CreateProductPage.this, "Some thing went wrong \n"+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<BrandResponse> call, Throwable t) {
                            Log.d("TAG", "onFailure: "+t.getMessage());
                        }
                    });
                }

            }
        });
        uploadProductCert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPDF = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                    intentPDF.setType("application/pdf");
//                    intentPDF.addCategory(Intent.CATEGORY_OPENABLE);
//                    intentPDF.putExtra(DocumentsContract.EXTRA_INITIAL_URI, true);
//                    startActivityForResult(Intent.createChooser(intentPDF, "Open with"), PICK_PDF_GALLERY+requestCode);
                DialogProperties properties = new DialogProperties();
                properties.selection_mode = DialogConfigs.SINGLE_MODE;
                properties.selection_type = DialogConfigs.FILE_SELECT;
                properties.root = new File("sdcard");
                FilePickerDialog dialog1 = new FilePickerDialog(CreateProductPage.this,properties);
                dialog1.setTitle("Select a File");
                ImageCross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        certificateFile=null;
                        certImageBlok.setVisibility(View.GONE);
                        uploadProductCert.setBackground(getResources().getDrawable(R.color.grey));
                        uploadProductCert.setEnabled(true);
                        uploadProductCert.setText("Upload Certificate");

                    }
                });
                dialog1.setDialogSelectionListener(new DialogSelectionListener() {
                    @Override
                    public void onSelectedFilePaths(String[] files) {
                        for (String file:files) {
                            File file1 = new File(file);
                            certificateFile=file1;
                            docName.setText(certificateFile.getName());
                            try {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                Bitmap bitmap = BitmapFactory.decodeFile(certificateFile.getPath(), options);
                                ExifInterface ei = null;
                                try {
                                    ei = new ExifInterface(certificateFile.getPath());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);

                                Bitmap rotatedBitmap = null;
                                switch(orientation) {

                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        rotatedBitmap = rotateImage(bitmap, 90);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        rotatedBitmap = rotateImage(bitmap, 180);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        rotatedBitmap = rotateImage(bitmap, 270);
                                        break;

                                    case ExifInterface.ORIENTATION_NORMAL:
                                    default:
                                        rotatedBitmap = bitmap;
                                }
                                uploadProductCert.setText("Certificate Uploaded");
                                uploadProductCert.setEnabled(false);
                                if (certificateFile.getName().endsWith(".pdf")){
                                    imageCert.setImageResource(R.drawable.pdf_icon_dummy);
                                    certImageBlok.setVisibility(View.VISIBLE);
                                    uploadProductCert.setBackground(getResources().getDrawable(R.color.green));

                                }else{
                                    imageCert.setImageBitmap(rotatedBitmap);
                                    certImageBlok.setVisibility(View.VISIBLE);
                                    uploadProductCert.setBackground(getResources().getDrawable(R.color.green));

                                }

                            }catch (Exception e){
                                imageCert.setImageResource(R.drawable.pdf_icon_dummy);
                                certImageBlok.setVisibility(View.VISIBLE);
                                uploadProductCert.setBackground(getResources().getDrawable(R.color.green));


                            }

                        }


                    }
                });
                dialog1.show();

            }
        });
        apiInterface=new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);
        intent=getIntent();
        OEMid=intent.getIntExtra("OEMid",0);
        BrandId=intent.getStringExtra("BrandId");
        Log.d("TAG", "onCreate: "+ BrandId);


        Call<BrandResponse> callForGetVehical=apiInterface.getVehicleType(SEILIGL.NEW_TOKEN);
        callForGetVehical.enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                BrandResponse brandResponse=response.body();
                Gson gson = new Gson();
                VehicalDataModel[] nameList = gson.fromJson(brandResponse.getData(), VehicalDataModel[].class);
                spinnerVehicalType.setAdapter(new VehicaalAdapter(CreateProductPage.this,nameList));
            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {

            }
        });
        spinnerModelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ModelTypeResponse vehicalDataModel= (ModelTypeResponse) parent.getSelectedItem();
//                ModelTypeId=vehicalDataModel.getModelId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerVehicalType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VehicalDataModel vehicalDataModel= (VehicalDataModel) parent.getSelectedItem();
                    vehicalType=vehicalDataModel.getId();
                Call<BrandResponse> call=apiInterface.getModelTypeByFuelAndVehical(SEILIGL.NEW_TOKEN,vehicalType,spinnerFuelType.getSelectedItem().toString(),BrandId);
                call.enqueue(new Callback<BrandResponse>() {
                    @Override
                    public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                        BrandResponse brandResponse=response.body();
                        Gson gson = new Gson();
                        ModelTypeResponse[] oemProductModelList=gson.fromJson(brandResponse.getData(),ModelTypeResponse[].class);
                        spinnerModelType.setAdapter(new ModelTypeAdapter(CreateProductPage.this,oemProductModelList));            }

                    @Override
                    public void onFailure(Call<BrandResponse> call, Throwable t) {

                    }
                });
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFuelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Call<BrandResponse> call=apiInterface.getModelTypeByFuelAndVehical(SEILIGL.NEW_TOKEN,vehicalType,spinnerFuelType.getSelectedItem().toString(),BrandId);
                call.enqueue(new Callback<BrandResponse>() {
                    @Override
                    public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                        BrandResponse brandResponse=response.body();
                        Gson gson = new Gson();
                        ModelTypeResponse[] oemProductModelList=gson.fromJson(brandResponse.getData(),ModelTypeResponse[].class);
                        spinnerModelType.setAdapter(new ModelTypeAdapter(CreateProductPage.this,oemProductModelList));            }

                    @Override//9354552962
                    public void onFailure(Call<BrandResponse> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private boolean allFieldValidate() {
        if (Val_BSP.getText().toString().trim().length()<3){
            Val_BSP.setError("Please enter BSP properly");
            return  false;
        }else if (Val_MSP.getText().toString().trim().length()<3){
            Val_MSP.setError("Please enter MSP properly");
            return  false;
        }else if (true){
            try {
                if (certificateFile.exists()){
                    return true;
                }

            }catch (Exception e){
                Utils.alert(this,"Please Upload Product Certificate");
                return false;
            }

        }
        return true;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}