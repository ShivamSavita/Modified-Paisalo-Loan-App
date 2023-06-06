package com.softeksol.paisalo.dealers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.softeksol.paisalo.dealers.Adapters.ImageAdapter;
import com.softeksol.paisalo.jlgsourcing.BuildConfig;
import com.softeksol.paisalo.jlgsourcing.R;

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

public class Dealer_CheckList extends AppCompatActivity {
    private final int PICK_IMAGE_CAMERA = 333, PICK_IMAGE_GALLERY = 111,PICK_PDF_GALLERY = 222;
RecyclerView kycRecView, kycRecView1,kycRecView2,kycRecView3,kycRecView4,kycRecView5,kycRecView6,kycRecView7,kycRecView8;
Button ImageUploadBtn,ImageUploadBtn1,ImageUploadBtn2,ImageUploadBtn3,ImageUploadBtn4,ImageUploadBtn5,ImageUploadBtn6,ImageUploadBtn7,ImageUploadBtn8,ImageUploadBtn9;
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
    File imageFile=null;

    private String url = "https://www.google.com";
    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";
    @Override
    protected void onStart() {
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
    protected void onStop() {
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_check_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Document");
        ImageUploadBtn=findViewById(R.id.ImageUploadBtn);
        ImageUploadBtn1=findViewById(R.id.ImageUploadBtn1);
        ImageUploadBtn2=findViewById(R.id.ImageUploadBtn2);
        ImageUploadBtn3=findViewById(R.id.ImageUploadBtn3);
        ImageUploadBtn4=findViewById(R.id.ImageUploadBtn4);
        ImageUploadBtn5=findViewById(R.id.ImageUploadBtn5);
        ImageUploadBtn6=findViewById(R.id.ImageUploadBtn6);
        ImageUploadBtn7=findViewById(R.id.ImageUploadBtn7);
        ImageUploadBtn8=findViewById(R.id.ImageUploadBtn8);
        kycRecView=findViewById(R.id.kycRecView);
        kycRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        kycRecView1=findViewById(R.id.kycRecView1);
        kycRecView1.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));


        kycRecView2=findViewById(R.id.kycRecView2);
        kycRecView2.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));


        kycRecView3=findViewById(R.id.kycRecView3);
        kycRecView3.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));


        kycRecView4=findViewById(R.id.kycRecView4);
        kycRecView4.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));


        kycRecView5=findViewById(R.id.kycRecView5);
        kycRecView5.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));


        kycRecView6=findViewById(R.id.kycRecView6);
        kycRecView6.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));


        kycRecView7=findViewById(R.id.kycRecView7);
        kycRecView7.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));


        kycRecView8=findViewById(R.id.kycRecView8);
        kycRecView8.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        String[] a={"1","2","3","4","1","2","3","4","1","2","3","4","1","2","3","4"};
        imageAdapter=new ImageAdapter(this,arrayListImages1);
        imageAdapter1=new ImageAdapter(this,arrayListImages2);
        imageAdapter2=new ImageAdapter(this,arrayListImages3);
        imageAdapter3=new ImageAdapter(this,arrayListImages4);
        imageAdapter4=new ImageAdapter(this,arrayListImages5);
        imageAdapter5=new ImageAdapter(this,arrayListImages6);
        imageAdapter6=new ImageAdapter(this,arrayListImages7);
        imageAdapter7=new ImageAdapter(this,arrayListImages8);
        imageAdapter8=new ImageAdapter(this,arrayListImages9);
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


    }

    private void selectImage(int requestCode) {
//        try {
//            PackageManager pm = getPackageManager();
//            int hasPerm = pm.checkPermission(CAMERA, getPackageName());
//            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose Photo From Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Dealer_CheckList.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
                       Uri imageUri =getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        values);
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                            startActivityForResult(intent, PICK_IMAGE_CAMERA+requestCode);


                            Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (i.resolveActivity(getPackageManager())!=null) {

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

                                    imageUri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
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
                        File storageDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        File imageFile=File.createTempFile(imageName,".jpg",storageDir);

                        currentImagePath=imageFile.getAbsolutePath();
                        Log.d("TAG", "getImageFile: "+currentImagePath);
                        return imageFile;
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
        Cursor cursor = this.getContentResolver().query(uri, projection, null,
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    Toast.makeText(this, "arrayListImages1 "+arrayListImages1.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 1+PICK_IMAGE_CAMERA:
                    arrayListImages2.add(imageFile);
                    Toast.makeText(this, "arrayListImages2 "+arrayListImages2.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 2+PICK_IMAGE_CAMERA:
                    arrayListImages3.add(imageFile);
                    Toast.makeText(this, "arrayListImages3 "+arrayListImages3.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 3+PICK_IMAGE_CAMERA:
                    arrayListImages4.add(imageFile);
                    Toast.makeText(this, "arrayListImages4 "+arrayListImages4.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 4+PICK_IMAGE_CAMERA:
                    arrayListImages5.add(imageFile);
                    Toast.makeText(this, "arrayListImages5 "+arrayListImages5.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 5+PICK_IMAGE_CAMERA:
                    arrayListImages6.add(imageFile);
                    Toast.makeText(this, "arrayListImages6 "+arrayListImages6.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 6+PICK_IMAGE_CAMERA:
                    arrayListImages7.add(imageFile);
                    Toast.makeText(this, "arrayListImages7 "+arrayListImages7.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 7+PICK_IMAGE_CAMERA:
                    arrayListImages8.add(imageFile);
                    Toast.makeText(this, "arrayListImages8 "+arrayListImages8.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 8+PICK_IMAGE_CAMERA:
                    arrayListImages9.add(imageFile);
                    Toast.makeText(this, "arrayListImages9 "+arrayListImages9.size(), Toast.LENGTH_SHORT).show();
                    break;
//////////////////////////////////////////////////////////////////////////////Gallery
                case 0+PICK_IMAGE_GALLERY:

                        Log.e("Activity", "Pick from Gallery::>>> ");
                        arrayListImages1.add(imageFileFromGallery);
                        Toast.makeText(this, "arrayListImages1"+arrayListImages1.size(), Toast.LENGTH_SHORT).show();
                        break;

                case 1+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages2.add(imageFileFromGallery);
                    Toast.makeText(this, "arrayListImages2 "+arrayListImages2.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 2+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages3.add(imageFileFromGallery);
                    Toast.makeText(this, "arrayListImages3 "+arrayListImages3.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 3+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages4.add(imageFileFromGallery);
                    Toast.makeText(this, "arrayListImages4 "+arrayListImages4.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 4+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages5.add(imageFileFromGallery);
                    Toast.makeText(this, "arrayListImages5 "+arrayListImages5.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 5+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages6.add(imageFileFromGallery);
                    Toast.makeText(this, "arrayListImages6 "+arrayListImages6.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 6+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages7.add(imageFileFromGallery);
                    Toast.makeText(this, "arrayListImages7 "+arrayListImages7.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 7+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages8.add(imageFileFromGallery);
                    Toast.makeText(this, "arrayListImages8 "+arrayListImages8.size(), Toast.LENGTH_SHORT).show();
                    break;

                case 8+PICK_IMAGE_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages9.add(imageFileFromGallery);
                    Toast.makeText(this, "arrayListImages9 "+arrayListImages9.size(), Toast.LENGTH_SHORT).show();
                    break;
///////////////////////////////////////////Get PDF//////////////////////////

                case 8+PICK_PDF_GALLERY:
                    Log.e("Activity", "Pick from Gallery::>>> ");
                    arrayListImages9.add(imageFileFromGallery);
                    Toast.makeText(this, "arrayListImages9 "+arrayListImages9.size(), Toast.LENGTH_SHORT).show();
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