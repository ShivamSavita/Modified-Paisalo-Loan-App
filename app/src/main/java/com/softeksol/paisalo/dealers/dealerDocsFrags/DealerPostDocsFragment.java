package com.softeksol.paisalo.dealers.dealerDocsFrags;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.google.gson.Gson;
import com.softeksol.paisalo.dealers.Adapters.DocImageAddViewAdapter;
import com.softeksol.paisalo.dealers.Adapters.ImageAdapter;
import com.softeksol.paisalo.dealers.Adapters.VehicaalAdapter;
import com.softeksol.paisalo.dealers.CreateProductPage;
import com.softeksol.paisalo.dealers.ImageClickListener;
import com.softeksol.paisalo.dealers.Models.ABFDocsModel;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.dealers.Models.VehicalDataModel;
import com.softeksol.paisalo.jlgsourcing.BuildConfig;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerPostDocsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int PICK_IMAGE_CAMERA = 333, PICK_IMAGE_GALLERY = 111,PICK_PDF_GALLERY = 222;
    RecyclerView kycRecView, kycRecView1,kycRecView2,kycRecView3,kycRecView4,kycRecView5,kycRecView6,kycRecView7,kycRecView8,kycRecView9;
    Button ImageUploadBtn,ImageUploadBtn1,ImageUploadBtn2,ImageUploadBtn3,ImageUploadBtn4,ImageUploadBtn5,ImageUploadBtn6,ImageUploadBtn7,ImageUploadBtn8,ImageUploadBtn9;
    Button ImageSaveBtn,ImageSaveBtn1,ImageSaveBtn2,ImageSaveBtn3,ImageSaveBtn4,ImageSaveBtn5,ImageSaveBtn6,ImageSaveBtn7,ImageSaveBtn8,ImageSaveBtn9;
    public ArrayList<File> arrayListImages1 = new ArrayList<>();
    public ArrayList<File> arrayListImages2 = new ArrayList<>();
    public ArrayList<File> arrayListImages3 = new ArrayList<>();
    public ArrayList<File> arrayListImages4 = new ArrayList<>();
    public ArrayList<File> arrayListImages5 = new ArrayList<>();
    public ArrayList<File> arrayListImages6 = new ArrayList<>();
    public ArrayList<File> arrayListImages7 = new ArrayList<>();
    public ArrayList<File> arrayListImages8 = new ArrayList<>();
    public ArrayList<File> arrayListImages9 = new ArrayList<>();
    public ArrayList<File> arrayListImages10 = new ArrayList<>();
    ImageAdapter imageAdapter,imageAdapter1,imageAdapter2,imageAdapter3,imageAdapter4,imageAdapter5,imageAdapter6,imageAdapter7,imageAdapter8,imageAdapter9;
    FilePickerDialog dialog1;
    Uri imageURI;
    String currentImagePath=null;

    ApiInterface apiInterface;
    File imageFile=null;

    private String url = "https://www.google.com";
    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";
    private int documentID;

    @Override
    public void onStart() {
        super.onStart();
        if (imageAdapter!=null){
            imageAdapter.notifyDataSetChanged();
        }
        if (imageAdapter1!=null){
            imageAdapter1.notifyDataSetChanged();
        }
        if (imageAdapter2!=null){
            imageAdapter2.notifyDataSetChanged();
        }
        if (imageAdapter3!=null){
            imageAdapter3.notifyDataSetChanged();
        }
        if (imageAdapter4!=null){
            imageAdapter4.notifyDataSetChanged();
        }
        if (imageAdapter5!=null){
            imageAdapter5.notifyDataSetChanged();
        }
        if (imageAdapter6!=null){
            imageAdapter6.notifyDataSetChanged();
        }
        if (imageAdapter7!=null){
            imageAdapter7.notifyDataSetChanged();
        }
        if (imageAdapter8!=null){
            imageAdapter8.notifyDataSetChanged();
        }
        if (imageAdapter9!=null){
            imageAdapter9.notifyDataSetChanged();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (imageAdapter!=null){
            imageAdapter.notifyDataSetChanged();
        }
        if (imageAdapter1!=null){
            imageAdapter1.notifyDataSetChanged();
        }
        if (imageAdapter2!=null){
            imageAdapter2.notifyDataSetChanged();
        }
        if (imageAdapter3!=null){
            imageAdapter3.notifyDataSetChanged();
        }
        if (imageAdapter4!=null){
            imageAdapter4.notifyDataSetChanged();
        }
        if (imageAdapter5!=null){
            imageAdapter5.notifyDataSetChanged();
        }
        if (imageAdapter6!=null){
            imageAdapter6.notifyDataSetChanged();
        }
        if (imageAdapter7!=null){
            imageAdapter7.notifyDataSetChanged();
        }
        if (imageAdapter8!=null){
            imageAdapter8.notifyDataSetChanged();
        }
        if (imageAdapter9!=null){
            imageAdapter9.notifyDataSetChanged();
        }
    }


    int DealerId;
    public DealerPostDocsFragment(int dealerId) {
        this.DealerId=dealerId;
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dealer_post_docs, container, false);
        Log.d("TAG", "onCreateView: dealr id in pre doc frag"+DealerId);
        ImageUploadBtn=view.findViewById(R.id.ImageUploadBtn);
        ImageUploadBtn1=view.findViewById(R.id.ImageUploadBtn1);
        ImageUploadBtn2=view.findViewById(R.id.ImageUploadBtn2);
        ImageUploadBtn3=view.findViewById(R.id.ImageUploadBtn3);
        ImageUploadBtn4=view.findViewById(R.id.ImageUploadBtn4);
        ImageUploadBtn5=view.findViewById(R.id.ImageUploadBtn5);
        ImageUploadBtn6=view.findViewById(R.id.ImageUploadBtn6);
        ImageUploadBtn7=view.findViewById(R.id.ImageUploadBtn7);
        ImageUploadBtn8=view.findViewById(R.id.ImageUploadBtn8);
        ImageUploadBtn9=view.findViewById(R.id.ImageUploadBtn9);

        ImageSaveBtn=view.findViewById(R.id.ImageSaveBtn);
        ImageSaveBtn1=view.findViewById(R.id.ImageSaveBtn1);
        ImageSaveBtn2=view.findViewById(R.id.ImageSaveBtn2);
        ImageSaveBtn3=view.findViewById(R.id.ImageSaveBtn3);
        ImageSaveBtn4=view.findViewById(R.id.ImageSaveBtn4);
        ImageSaveBtn5=view.findViewById(R.id.ImageSaveBtn5);
        ImageSaveBtn6=view.findViewById(R.id.ImageSaveBtn6);
        ImageSaveBtn7=view.findViewById(R.id.ImageSaveBtn7);
        ImageSaveBtn8=view.findViewById(R.id.ImageSaveBtn8);
        ImageSaveBtn9=view.findViewById(R.id.ImageSaveBtn9);

        ImageSaveBtn.setVisibility(View.INVISIBLE);
        ImageSaveBtn1.setVisibility(View.INVISIBLE);
        ImageSaveBtn2.setVisibility(View.INVISIBLE);
        ImageSaveBtn3.setVisibility(View.INVISIBLE);
        ImageSaveBtn4.setVisibility(View.INVISIBLE);
        ImageSaveBtn5.setVisibility(View.INVISIBLE);
        ImageSaveBtn6.setVisibility(View.INVISIBLE);
        ImageSaveBtn7.setVisibility(View.INVISIBLE);
        ImageSaveBtn8.setVisibility(View.INVISIBLE);
        ImageSaveBtn9.setVisibility(View.INVISIBLE);

        apiInterface=new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);
        kycRecView=view.findViewById(R.id.kycRecView);
        kycRecView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        kycRecView1=view.findViewById(R.id.kycRecView1);
        kycRecView1.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));


        kycRecView2=view.findViewById(R.id.kycRecView2);
        kycRecView2.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));


        kycRecView3=view.findViewById(R.id.kycRecView3);
        kycRecView3.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));


        kycRecView4=view.findViewById(R.id.kycRecView4);
        kycRecView4.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));


        kycRecView5=view.findViewById(R.id.kycRecView5);
        kycRecView5.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));


        kycRecView6=view.findViewById(R.id.kycRecView6);
        kycRecView6.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));


        kycRecView7=view.findViewById(R.id.kycRecView7);
        kycRecView7.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));


        kycRecView8=view.findViewById(R.id.kycRecView8);
        kycRecView8.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        kycRecView9=view.findViewById(R.id.kycRecView8);
        kycRecView9.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        String[] a={"1","2","3","4","1","2","3","4","1","2","3","4","1","2","3","4"};
        imageAdapter=new ImageAdapter(getContext(),arrayListImages1);
        imageAdapter1=new ImageAdapter(getContext(),arrayListImages2);
        imageAdapter2=new ImageAdapter(getContext(),arrayListImages3);
        imageAdapter3=new ImageAdapter(getContext(),arrayListImages4);
        imageAdapter4=new ImageAdapter(getContext(),arrayListImages5);
        imageAdapter5=new ImageAdapter(getContext(),arrayListImages6);
        imageAdapter6=new ImageAdapter(getContext(),arrayListImages7);
        imageAdapter7=new ImageAdapter(getContext(),arrayListImages8);
        imageAdapter8=new ImageAdapter(getContext(),arrayListImages9);
        imageAdapter9=new ImageAdapter(getContext(),arrayListImages10);
        kycRecView.setAdapter(imageAdapter);
        kycRecView1.setAdapter(imageAdapter1);
        kycRecView2.setAdapter(imageAdapter2);
        kycRecView3.setAdapter(imageAdapter3);
        kycRecView4.setAdapter(imageAdapter4);
        kycRecView5.setAdapter(imageAdapter5);
        kycRecView6.setAdapter(imageAdapter6);
        kycRecView7.setAdapter(imageAdapter7);
        kycRecView8.setAdapter(imageAdapter8);
        kycRecView9.setAdapter(imageAdapter9);

        imageAdapter.notifyDataSetChanged();
        imageAdapter1.notifyDataSetChanged();
        imageAdapter2.notifyDataSetChanged();
        imageAdapter3.notifyDataSetChanged();
        imageAdapter4.notifyDataSetChanged();
        imageAdapter5.notifyDataSetChanged();
        imageAdapter6.notifyDataSetChanged();
        imageAdapter7.notifyDataSetChanged();
        imageAdapter8.notifyDataSetChanged();
        imageAdapter9.notifyDataSetChanged();

        kycRecView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (bottom>0){
                    ImageSaveBtn.setVisibility(View.VISIBLE);
                }else {
                    ImageSaveBtn.setVisibility(View.INVISIBLE);

                }

            }
        });
        kycRecView1.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (bottom>0){
                    ImageSaveBtn1.setVisibility(View.VISIBLE);
                }else {
                    ImageSaveBtn1.setVisibility(View.INVISIBLE);

                }

            }
        });

        kycRecView2.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (bottom>0){
                    ImageSaveBtn2.setVisibility(View.VISIBLE);
                }else {
                    ImageSaveBtn2.setVisibility(View.INVISIBLE);

                }

            }
        });   kycRecView3.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (bottom>0){
                    ImageSaveBtn3.setVisibility(View.VISIBLE);
                }else {
                    ImageSaveBtn3.setVisibility(View.INVISIBLE);

                }

            }
        });   kycRecView4.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (bottom>0){
                    ImageSaveBtn4.setVisibility(View.VISIBLE);
                }else {
                    ImageSaveBtn4.setVisibility(View.INVISIBLE);

                }

            }
        });   kycRecView5.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (bottom>0){
                    ImageSaveBtn5.setVisibility(View.VISIBLE);
                }else {
                    ImageSaveBtn5.setVisibility(View.INVISIBLE);

                }

            }
        });   kycRecView6.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (bottom>0){
                    ImageSaveBtn6.setVisibility(View.VISIBLE);
                }else {
                    ImageSaveBtn6.setVisibility(View.INVISIBLE);

                }

            }
        });   kycRecView7.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (bottom>0){
                    ImageSaveBtn7.setVisibility(View.VISIBLE);
                }else {
                    ImageSaveBtn7.setVisibility(View.INVISIBLE);

                }

            }
        });   kycRecView8.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (bottom>0){
                    ImageSaveBtn8.setVisibility(View.VISIBLE);
                }else {
                    ImageSaveBtn8.setVisibility(View.INVISIBLE);

                }

            }
        });  kycRecView9.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (bottom>0){
                    ImageSaveBtn9.setVisibility(View.VISIBLE);
                }else {
                    ImageSaveBtn9.setVisibility(View.INVISIBLE);

                }

            }
        });

        ImageUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(0);

            }
        });
        ImageUploadBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(1);

            }
        });
        ImageUploadBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(2);

            }
        });
        ImageUploadBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(3);

            }
        });
        ImageUploadBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(4);

            }
        });
        ImageUploadBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(5);

            }
        });
        ImageUploadBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(6);
            }
        });
        ImageUploadBtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(7);
            }
        });
        ImageUploadBtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                selectImage(8);
            }
        });  ImageUploadBtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                selectImage(9);
            }
        });



/////////////////////////////////////////Upload Images Data
        ImageSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentID=9;
                saveDocsData(arrayListImages1,"9");
            }
        });
        ImageSaveBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentID=10;

                saveDocsData(arrayListImages1,"10");
            }
        });
        ImageSaveBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentID=11;

                saveDocsData(arrayListImages2,"11");
            }
        });


        ImageSaveBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentID=12;

                saveDocsData(arrayListImages4,"12");
            }
        });
        ImageSaveBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentID=13;

                saveDocsData(arrayListImages5,"13");
            }
        });

        ImageSaveBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentID=14;

                saveDocsData(arrayListImages6,"14");
            }
        });
        ImageSaveBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentID=15;

                saveDocsData(arrayListImages7,"15");
            }
        });

        ImageSaveBtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentID=16;

                saveDocsData(arrayListImages8,"16");
            }
        });
        ImageSaveBtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentID=17;

                saveDocsData(arrayListImages9,"17");
            }
        });
        ImageSaveBtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentID=18;

                saveDocsData(arrayListImages10,"18");
            }
        });










        return  view;
    }



    private void saveDocsData(ArrayList<File> arrayImages, String docId) {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("UserId", String.valueOf(DealerId))
                .addFormDataPart("UserType","Dealer")
                .addFormDataPart("DocType","Post")
                .addFormDataPart("DocId",docId);
        for (File image:arrayImages) {
            RequestBody surveyBody = RequestBody.create(MediaType.parse("*/*"), image);
            builder.addFormDataPart("Documents",image.getName(),surveyBody);
        }
        
        RequestBody requestBody = builder.build();

        Call<BrandResponse> call=apiInterface.uploadDealerPreDocs(SEILIGL.NEW_TOKEN,requestBody);
        call.enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                BrandResponse brandResponse=response.body();
                Log.d("TAG", "onResponse: "+brandResponse.getMessage());
                Log.d("TAG", "onResponse: "+brandResponse.getData());
                Log.d("TAG", "onResponse: "+brandResponse.getStatusCode());
                if(brandResponse.getStatusCode()==200){
                    Utils.alert(getContext(),"Document Saved Successfully");

                    switch (documentID)
                    {
                        case 9:
                            arrayListImages1.clear();
                            imageAdapter.notifyDataSetChanged();
                            break;
                             case 10:
                            arrayListImages2.clear();
                            imageAdapter1.notifyDataSetChanged();
                            break;
                             case 11:
                            arrayListImages3.clear();
                            imageAdapter2.notifyDataSetChanged();
                            break;
                             case 12:
                            arrayListImages4.clear();
                            imageAdapter3.notifyDataSetChanged();
                            break;
                             case 13:
                            arrayListImages5.clear();
                            imageAdapter4.notifyDataSetChanged();
                            break;
                             case 14:
                            arrayListImages6.clear();
                            imageAdapter5.notifyDataSetChanged();
                            break;
                             case 15:
                            arrayListImages7.clear();
                            imageAdapter6.notifyDataSetChanged();
                            break;
                             case 16:
                            arrayListImages8.clear();
                            imageAdapter7.notifyDataSetChanged();
                            break;
                             case 17:
                            arrayListImages9.clear();
                            imageAdapter8.notifyDataSetChanged();
                            break;
                             case 18:
                            arrayListImages10.clear();
                            imageAdapter9.notifyDataSetChanged();
                            break;

                    }
                }else{
                    Toast.makeText(getContext(), "Something went wrong. Please try again!!", Toast.LENGTH_SHORT).show();
                }
                    
            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });

    }

    private void selectImage(int requestCode) {
//        try {
//            PackageManager pm = getPackageManager();
//            int hasPerm = pm.checkPermission(CAMERA, getPackageName());
//            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
        final CharSequence[] options = {"Take Photo", "Choose Photo From Gallery","Choose Docs From Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
                Uri imageUri =getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);
                if (options[item].equals("Take Photo")) {
                    dialog.dismiss();
//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                            startActivityForResult(intent, PICK_IMAGE_CAMERA+requestCode);


                    Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (i.resolveActivity(getContext().getPackageManager())!=null) {

                        try {
                            imageFile = getImageFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (imageFile != null) {
//                                            File compressedImage = new Compressor.Builder(UpdateDetailsBioMetric.this)
//                                                    .setMaxWidth(720)
//                                                    .setMaxHeight(720)
//                                                    .setQuality(75)
//                                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                                                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                                                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                                                    .build()
//                                                    .compressToFile(imageFile);

                            imageUri = FileProvider.getUriForFile(requireContext().getApplicationContext(),
                                    BuildConfig.APPLICATION_ID + ".provider", imageFile);
                            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(i, PICK_IMAGE_CAMERA+requestCode);
                        }
                    }
                } else if (options[item].equals("Choose Photo From Gallery")) {
                    dialog.dismiss();
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY+requestCode);
                }else if (options[item].equals("Choose Docs From Gallery")) {
                    Log.d("TAG", "onClick: clicked on pick docs from gallery");

                    dialog.dismiss();
//                    Intent intentPDF = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                    intentPDF.setType("application/pdf");
//                    intentPDF.addCategory(Intent.CATEGORY_OPENABLE);
//                    intentPDF.putExtra(DocumentsContract.EXTRA_INITIAL_URI, true);
//                    startActivityForResult(Intent.createChooser(intentPDF, "Open with"), PICK_PDF_GALLERY+requestCode);
                    DialogProperties properties = new DialogProperties();
                    properties.selection_mode = DialogConfigs.SINGLE_MODE;
                    properties.selection_type = DialogConfigs.FILE_SELECT;
                    properties.root = new File("sdcard");
                     dialog1 = new FilePickerDialog(getContext(),properties);
                    dialog1.setTitle("Select a File");
                    Log.d("TAG", "onClick: clicked on pick docs from gallery");
                    dialog1.setDialogSelectionListener(new DialogSelectionListener() {
                        @Override
                        public void onSelectedFilePaths(String[] files) {
                            switch (requestCode){
                                case 0:
                                    for (String file:files) {
                                        File file1 = new File(file);
                                        arrayListImages1.add(file1);
                                        imageAdapter.notifyDataSetChanged();
                                    }
                                    break;
                                case 1:
                                    for (String file:files) {
                                        File file1 = new File(file);
                                        arrayListImages2.add(file1);
                                        imageAdapter1.notifyDataSetChanged();
                                    }
                                    break;
                                case 2:
                                    for (String file:files) {
                                        File file1 = new File(file);
                                        arrayListImages3.add(file1);
                                        imageAdapter2.notifyDataSetChanged();
                                    }
                                    break;
                                case 3:
                                    for (String file:files) {
                                        File file1 = new File(file);
                                        arrayListImages4.add(file1);
                                        imageAdapter3.notifyDataSetChanged();
                                    }
                                    break;

                                case 4:
                                    for (String file:files) {
                                        File file1 = new File(file);
                                        arrayListImages5.add(file1);
                                        imageAdapter4.notifyDataSetChanged();
                                    }
                                    break;
                                case 5:
                                    for (String file:files) {
                                        File file1 = new File(file);
                                        arrayListImages6.add(file1);
                                        imageAdapter5.notifyDataSetChanged();
                                    }
                                    break;

                                case 6:
                                    for (String file:files) {
                                        File file1 = new File(file);
                                        arrayListImages7.add(file1);
                                        imageAdapter6.notifyDataSetChanged();
                                    }
                                    break;


                                case 7:
                                    for (String file:files) {
                                        File file1 = new File(file);
                                        arrayListImages8.add(file1);
                                        imageAdapter7.notifyDataSetChanged();
                                    }
                                    break;
                                case 8:
                                    for (String file:files) {
                                        File file1 = new File(file);
                                        arrayListImages9.add(file1);
                                        imageAdapter8.notifyDataSetChanged();
                                    }
                                    break;





                            }


                        }
                    });


                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        dialog1.show();
                    } else {
                        // Request the permission if not granted
                        requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT);
                    }

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
//            } else
//                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
    }
    private File getImageFile() throws IOException{
        String timeStamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageName="jpg+"+timeStamp+"_";
        File storageDir=getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile=File.createTempFile(imageName,".jpg",storageDir);

        currentImagePath=imageFile.getAbsolutePath();
        Log.d("TAG", "getImageFile: "+currentImagePath);
        return imageFile;
    }
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data LIKE '%.pdf'";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor
                        .getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }
    public String getRealPathFromURIForGallery(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null,
                null, null);
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        assert false;
        cursor.close();
        return uri.getPath();
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(wallpaperDirectory + File.separator + fileName);
            // create folder if not exists

            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (dialog1 != null) {
                        // Show the dialog if the read permission has been granted
                        dialog1.show();
                    }
                } else {
                    // Permission has not been granted. Notify the user.
                    Toast.makeText(getContext(), "Permission is required for getting the list of files", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }








    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copystream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int copystream(InputStream input, OutputStream output) throws Exception, IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
        }
        return count;
    }


    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File imageFileFromGallery = null;
        try {
            Uri selectedImageUri = data.getData();
            Log.d("TAG", "onActivityResult: "+selectedImageUri.getPath());
            String imagepath = getRealPathFromURIForGallery(selectedImageUri);
            imageFileFromGallery = new File(imagepath);
            Log.d("TAG", "onActivityResult: "+imageFileFromGallery);
        }catch (Exception e){

            // Get the Uri of the selected file
//
//            String uriString = selectedImageUri.toString();
//            File myFile = new File(uriString);
//
//            String path = getFilePathFromURI(Dealer_CheckList.this,selectedImageUri);
//            Log.d("TAG", "onActivityResult: "+path);
//            Log.d("TAG", "onActivityResult: "+myFile.getName());

        }

        if (resultCode==RESULT_OK){



            switch (requestCode){

                case 0+PICK_IMAGE_CAMERA:
                    Log.d("TAG", "onActivityResult: "+imageFile);
                    arrayListImages1.add(imageFile);
                    Toast.makeText(getContext(), "arrayListImages1 "+arrayListImages1.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 1+PICK_IMAGE_CAMERA:
                    arrayListImages2.add(imageFile);
                    Toast.makeText(getContext(), "arrayListImages2 "+arrayListImages2.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 2+PICK_IMAGE_CAMERA:
                    arrayListImages3.add(imageFile);
                    Toast.makeText(getContext(), "arrayListImages3 "+arrayListImages3.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 3+PICK_IMAGE_CAMERA:
                    arrayListImages4.add(imageFile);
                    Toast.makeText(getContext(), "arrayListImages4 "+arrayListImages4.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 4+PICK_IMAGE_CAMERA:
                    arrayListImages5.add(imageFile);
                    Toast.makeText(getContext(), "arrayListImages5 "+arrayListImages5.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 5+PICK_IMAGE_CAMERA:
                    arrayListImages6.add(imageFile);
                    Toast.makeText(getContext(), "arrayListImages6 "+arrayListImages6.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 6+PICK_IMAGE_CAMERA:
                    arrayListImages7.add(imageFile);
                    Toast.makeText(getContext(), "arrayListImages7 "+arrayListImages7.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 7+PICK_IMAGE_CAMERA:
                    arrayListImages8.add(imageFile);
                    Toast.makeText(getContext(), "arrayListImages8 "+arrayListImages8.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 8+PICK_IMAGE_CAMERA:
                    arrayListImages9.add(imageFile);
                    Toast.makeText(getContext(), "arrayListImages9 "+arrayListImages9.size(), Toast.LENGTH_SHORT).show();
                    break;
//////////////////////////////////////////////////////////////////////////////Gallery
                case 0+PICK_IMAGE_GALLERY:

                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages1.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages1"+arrayListImages1.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 1+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages2.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages2 "+arrayListImages2.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 2+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages3.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages3 "+arrayListImages3.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 3+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages4.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages4 "+arrayListImages4.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 4+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages5.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages5 "+arrayListImages5.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 5+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages6.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages6 "+arrayListImages6.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 6+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages7.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages7 "+arrayListImages7.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 7+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages8.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages8 "+arrayListImages8.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 8+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages9.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages9 "+arrayListImages9.size(), Toast.LENGTH_SHORT).show();
                    break;
///////////////////////////////////////////Get PDF//////////////////////////

                case 8+PICK_PDF_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages9.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages9 "+arrayListImages9.size(), Toast.LENGTH_SHORT).show();
                    break;
                case 7+PICK_PDF_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages8.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages8 "+arrayListImages8.size(), Toast.LENGTH_SHORT).show();
                    break;
                case 6+PICK_PDF_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages7.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages7 "+arrayListImages7.size(), Toast.LENGTH_SHORT).show();
                    break;
                case 5+PICK_PDF_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages6.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages6 "+arrayListImages6.size(), Toast.LENGTH_SHORT).show();
                    break;
                case 4+PICK_PDF_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages5.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages5 "+arrayListImages5.size(), Toast.LENGTH_SHORT).show();
                    break;
                case 3+PICK_PDF_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages4.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages4 "+arrayListImages4.size(), Toast.LENGTH_SHORT).show();
                    break;
                case 2+PICK_PDF_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages3.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages3 "+arrayListImages3.size(), Toast.LENGTH_SHORT).show();
                    break;
                case 1+PICK_PDF_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages2.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages2 "+arrayListImages2.size(), Toast.LENGTH_SHORT).show();
                    break;
                case 0+PICK_PDF_GALLERY:
                    Uri uri = data.getData();
                    File file = new File(uri.getPath());
                    arrayListImages1.add(file);
                    Toast.makeText(getContext(), "arrayListImages1 "+arrayListImages1.size(), Toast.LENGTH_SHORT).show();

                    break;
            }
        }
//        if (requestCode == 0 && resultCode == RESULT_OK ) {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            arrayListImages1.add(bitmap);
//            Toast.makeText(this, "arrayListImages1"+arrayListImages1.size(), Toast.LENGTH_SHORT).show();
//
//
//        }else if (requestCode == 1 && resultCode == RESULT_OK ) {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            arrayListImages2.add(bitmap);
//            Toast.makeText(this, "arrayListImages2"+arrayListImages2.size(), Toast.LENGTH_SHORT).show();
//
//
//        }else if (requestCode == 2 && resultCode == RESULT_OK ) {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            arrayListImages3.add(bitmap);
//            Toast.makeText(this, "arrayListImages3"+arrayListImages3.size(), Toast.LENGTH_SHORT).show();
//
//
//        }else  if (requestCode == 3 && resultCode == RESULT_OK ) {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            arrayListImages4.add(bitmap);
//            Toast.makeText(this, "arrayListImages4"+arrayListImages4.size(), Toast.LENGTH_SHORT).show();
//
//
//        }else  if (requestCode == 4 && resultCode == RESULT_OK ) {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            arrayListImages5.add(bitmap);
//            Toast.makeText(this, "arrayListImages5"+arrayListImages5.size(), Toast.LENGTH_SHORT).show();
//
//
//        }else  if (requestCode == 5 && resultCode == RESULT_OK ) {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            arrayListImages6.add(bitmap);
//            Toast.makeText(this, "arrayListImages6"+arrayListImages6.size(), Toast.LENGTH_SHORT).show();
//
//
//        }else  if (requestCode == 6 && resultCode == RESULT_OK ) {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            arrayListImages7.add(bitmap);
//            Toast.makeText(this, "arrayListImages7"+arrayListImages7.size(), Toast.LENGTH_SHORT).show();
//
//
//        }else  if (requestCode == 7 && resultCode == RESULT_OK ) {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            arrayListImages8.add(bitmap);
//            Toast.makeText(this, "arrayListImages8"+arrayListImages8.size(), Toast.LENGTH_SHORT).show();
//
//
//        }else  if (requestCode == 8+PICK_IMAGE_CAMERA && resultCode == RESULT_OK ) {
//
//
//        }else if (requestCode == PICK_IMAGE_GALLERY) {
//
//
//        }
    }

}