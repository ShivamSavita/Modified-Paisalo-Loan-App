package com.softeksol.paisalo.dealers.dealerDocsFrags;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerPreDocsFragment extends Fragment {

    private final int PICK_IMAGE_CAMERA = 333, PICK_IMAGE_GALLERY = 111,PICK_PDF_GALLERY = 222;
    RecyclerView kycRecView, kycRecView1,kycRecView2,kycRecView3,kycRecView4,kycRecView5,kycRecView6,kycRecView7,kycRecView8;
    Button ImageUploadBtn,ImageUploadBtn1,ImageUploadBtn2,ImageUploadBtn3,ImageUploadBtn4,ImageUploadBtn5,ImageUploadBtn6,ImageUploadBtn7,ImageUploadBtn8,ImageUploadBtn9;
    Button ImageSaveBtn;
    public ArrayList<File> arrayListImages1 = new ArrayList<>();
    public ArrayList<File> arrayListImages2 = new ArrayList<>();
    public ArrayList<File> arrayListImages3 = new ArrayList<>();
    public ArrayList<File> arrayListImages4 = new ArrayList<>();
    public ArrayList<File> arrayListImages5 = new ArrayList<>();
    public ArrayList<File> arrayListImages6 = new ArrayList<>();
    public ArrayList<File> arrayListImages7 = new ArrayList<>();
    public ArrayList<File> arrayListImages8 = new ArrayList<>();
    public ArrayList<File> arrayListImages9 = new ArrayList<>();
    ImageAdapter imageAdapter,imageAdapter1,imageAdapter2,imageAdapter3,imageAdapter4,imageAdapter5,imageAdapter6,imageAdapter7,imageAdapter8;

    Uri imageURI;
    String currentImagePath=null;

    ApiInterface apiInterface;
    File imageFile=null;

    private String url = "https://www.google.com";
    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";
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
    }



    public int DealerId;

    public DealerPreDocsFragment(int dealerId) {
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
        View view= inflater.inflate(R.layout.fragment_dealer_pre_docs, container, false);

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

        ImageSaveBtn=view.findViewById(R.id.ImageSaveBtn);
        ImageSaveBtn.setVisibility(View.INVISIBLE);

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
        kycRecView.setAdapter(imageAdapter);
        kycRecView1.setAdapter(imageAdapter1);
        kycRecView2.setAdapter(imageAdapter2);
        kycRecView3.setAdapter(imageAdapter3);
        kycRecView4.setAdapter(imageAdapter4);
        kycRecView5.setAdapter(imageAdapter5);
        kycRecView6.setAdapter(imageAdapter6);
        kycRecView7.setAdapter(imageAdapter7);
        kycRecView8.setAdapter(imageAdapter8);

        imageAdapter.notifyDataSetChanged();
        imageAdapter1.notifyDataSetChanged();
        imageAdapter2.notifyDataSetChanged();
        imageAdapter3.notifyDataSetChanged();
        imageAdapter4.notifyDataSetChanged();
        imageAdapter5.notifyDataSetChanged();
        imageAdapter6.notifyDataSetChanged();
        imageAdapter7.notifyDataSetChanged();
        imageAdapter8.notifyDataSetChanged();
        view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                Log.d("TAG", "onSystemUiVisibilityChange: "+visibility);
            }
        });
       kycRecView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
           @Override
           public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

               if (bottom>0){
                   ImageSaveBtn.setVisibility(View.VISIBLE);
               }else {
                   ImageSaveBtn.setVisibility(View.INVISIBLE);

               }

               Log.d("TAG", "onLayoutChange: top "+top);
               Log.d("TAG", "onLayoutChange: left "+left);
               Log.d("TAG", "onLayoutChange: right "+right);
               Log.d("TAG", "onLayoutChange: bottom "+bottom);
               Log.d("TAG", "onLayoutChange: oldLeft "+oldLeft);
               Log.d("TAG", "onLayoutChange: oldTop "+oldTop);
               Log.d("TAG", "onLayoutChange: oldRight "+oldRight);
               Log.d("TAG", "onLayoutChange: oldBottom "+oldBottom);
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
        });



/////////////////////////////////////////Upload Images Data


    ImageSaveBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("UserId", String.valueOf(DealerId))
                    .addFormDataPart("UserType","Dealer")
                    .addFormDataPart("DocId","1");
            for (File image:arrayListImages1) {
                RequestBody surveyBody = RequestBody.create(MediaType.parse("*/*"), image);
                builder.addFormDataPart("Documents",image.getName(),surveyBody);
            }


//
            RequestBody requestBody = builder.build();

            Call<BrandResponse> call=apiInterface.uploadDealerPreDocs(SEILIGL.NEW_TOKEN,requestBody);
            call.enqueue(new Callback<BrandResponse>() {
                @Override
                public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                    BrandResponse brandResponse=response.body();
                    Log.d("TAG", "onResponse: "+brandResponse.getMessage());
                    Log.d("TAG", "onResponse: "+brandResponse.getData());
                    Log.d("TAG", "onResponse: "+brandResponse.getStatusCode());
                }

                @Override
                public void onFailure(Call<BrandResponse> call, Throwable t) {
                    Log.d("TAG", "onFailure: "+t.getMessage());
                }
            });

        }
    });











        return  view;
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
                    dialog.dismiss();
//                    Intent intentPDF = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                    intentPDF.setType("application/pdf");
//                    intentPDF.addCategory(Intent.CATEGORY_OPENABLE);
//                    intentPDF.putExtra(DocumentsContract.EXTRA_INITIAL_URI, true);
//                    startActivityForResult(Intent.createChooser(intentPDF, "Open with"), PICK_PDF_GALLERY+requestCode);
                    DialogProperties properties = new DialogProperties();

                    properties.selection_type = DialogConfigs.FILE_SELECT;
                    properties.extensions = null;
                    FilePickerDialog dialog1 = new FilePickerDialog(getContext(),properties);
                    dialog1.setTitle("Select a File");

                    dialog1.setDialogSelectionListener(new DialogSelectionListener() {
                        @Override
                        public void onSelectedFilePaths(String[] files) {
                            for (String file:files
                                 ) {
                                File file1 = new File(file);
                                arrayListImages1.add(file1);
                            }

                        }
                    });
                    dialog1.show();

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