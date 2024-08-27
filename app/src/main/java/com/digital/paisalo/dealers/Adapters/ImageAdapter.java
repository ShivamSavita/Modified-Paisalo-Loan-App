package com.digital.paisalo.dealers.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.digital.paisalo.dealers.OpenImage;
import com.digital.paisalo.jlgsourcing.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageAdapter extends  RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    Context context;
    ArrayList<File> arrayList=new ArrayList<>();
    private ScaleGestureDetector scaleGestureDetector;

    public ImageAdapter(Context context, ArrayList<File> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_image_card,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
try {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    Bitmap bitmap = BitmapFactory.decodeFile(arrayList.get(position).getPath(), options);
    ExifInterface ei = null;
    try {
        ei = new ExifInterface(arrayList.get(position).getPath());
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
    holder.mainImageView.setImageBitmap(rotatedBitmap);

}catch (Exception e){
    holder.mainImageView.setImageResource(R.drawable.pdf_icon_dummy);

}

                holder.CrossImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "It will be deleted later", Toast.LENGTH_SHORT).show();
                    }
                });

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(context, OpenImage.class);
        intent.putExtra("image",arrayList.get(position).toString());
        context.startActivity(intent);


    }
});
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView mainImageView,CrossImageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            mainImageView=itemView.findViewById(R.id.imageMain);
            CrossImageView=itemView.findViewById(R.id.ImageCross);

        }
    }
}