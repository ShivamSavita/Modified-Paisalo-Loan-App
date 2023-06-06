package com.softeksol.paisalo.dealers.dealerDocsFrags;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softeksol.paisalo.dealers.Adapters.DocImageAddViewAdapter;
import com.softeksol.paisalo.dealers.Adapters.OEMListAdapter;
import com.softeksol.paisalo.dealers.Dealer_CheckList;
import com.softeksol.paisalo.dealers.ImageClickListener;
import com.softeksol.paisalo.dealers.Models.ABFDocsModel;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.dealers.Models.OEMDataModel;
import com.softeksol.paisalo.dealers.SelectOEMpage;
import com.softeksol.paisalo.jlgsourcing.BuildConfig;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerPreDocsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    RecyclerView recviewDealerPreDocs;
    private String mParam2;

    public int DealerId;
    DocImageAddViewAdapter adapter;

    Uri imageURI;
    String currentImagePath=null;
    File imageFile=null;
    public ArrayList<File> arrayListImages = new ArrayList<>();
    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";
    private final int PICK_IMAGE_CAMERA = 333, PICK_IMAGE_GALLERY = 111,PICK_PDF_GALLERY = 222;
    public DealerPreDocsFragment(int dealerId) {
        this.DealerId=DealerId;
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dealer_pre_docs, container, false);
        recviewDealerPreDocs=view.findViewById(R.id.recviewDealerPreDocs);
        recviewDealerPreDocs.setLayoutManager(new LinearLayoutManager(getContext()));
        ApiInterface apiInterface=new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);
        Call<BrandResponse> call=apiInterface.getABfDocs(SEILIGL.NEW_TOKEN,"pre",DealerId);
        call.enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {

                Log.d("TAG", "onResponse: "+response.body());
                BrandResponse brandResponse=response.body();
                Gson gson = new Gson();
                ABFDocsModel[] nameList = gson.fromJson(brandResponse.getData(), ABFDocsModel[].class);
                adapter=new DocImageAddViewAdapter(getContext(), nameList, new ImageClickListener() {
                    @Override
                    public void onImageClicked() {
                        selectImage(1);
                        Toast.makeText(getContext(), "click on pre docs", Toast.LENGTH_SHORT).show();
                    }
                });
                recviewDealerPreDocs.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {

            }
        });
        return  view;
    }


    private void selectImage(int requestCode) {
//        try {
//            PackageManager pm = getPackageManager();
//            int hasPerm = pm.checkPermission(CAMERA, getPackageName());
//            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
        final CharSequence[] options = {"Take Photo", "Choose Photo From Gallery","Cancel"};
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
                            startActivityForResult(i, 1000);
                        }
                    }
                } else if (options[item].equals("Choose Photo From Gallery")) {
                    dialog.dismiss();
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    getActivity().startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY+requestCode);
                }else if (options[item].equals("Choose Docs From Gallery")) {
                    dialog.dismiss();
                    Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT);
                    pickPhoto.setType("application/pdf");
                    pickPhoto.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(pickPhoto, PICK_PDF_GALLERY+requestCode);
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

                case 1000:
                    Log.d("TAG", "onActivityResult: "+imageFile);
                    arrayListImages.add(imageFile);
                    Toast.makeText(getContext(), "arrayListImages1 "+arrayListImages.size(), Toast.LENGTH_SHORT).show();
                    break;

//////////////////////////////////////////////////////////////////////////////Gallery
                case 0+PICK_IMAGE_GALLERY:

                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages1"+arrayListImages.size(), Toast.LENGTH_SHORT).show();
                    break;

///////////////////////////////////////////Get PDF//////////////////////////

                case 8+PICK_PDF_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages.add(imageFileFromGallery);
                    Toast.makeText(getContext(), "arrayListImages9 "+arrayListImages.size(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }

}