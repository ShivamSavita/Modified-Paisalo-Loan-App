package com.softeksol.paisalo.dealers.Adapters;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softeksol.paisalo.dealers.ImageClickListener;
import com.softeksol.paisalo.dealers.Models.ABFDocsModel;
import com.softeksol.paisalo.jlgsourcing.R;

import java.io.File;
import java.util.ArrayList;

public class DocImageAddViewAdapter extends RecyclerView.Adapter<DocImageAddViewAdapter.PrePostDocsViewHolder> {
    Context context;
    ABFDocsModel[] nameList;
    ImageAdapter adapter;

     ArrayList<File> arrayListImages;

    ImageClickListener listener;
    public DocImageAddViewAdapter(Context context, ABFDocsModel[] nameList,ImageClickListener listener) {
        this.listener=listener;
        this.context=context;
        this.nameList=nameList;

    }

    @NonNull
    @Override
    public PrePostDocsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dealer_doc_image_item,parent,false);
        return new PrePostDocsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrePostDocsViewHolder holder, int position) {
        holder.docName.setText(nameList[position].getDocName());
        holder.imageDocsRecView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        adapter=new ImageAdapter(context,arrayListImages);
        holder.imageDocsRecView.setAdapter(adapter);

        adapter.notifyDataSetChanged();




    }

    @Override
    public int getItemCount() {
        return nameList.length;
    }

    public class PrePostDocsViewHolder extends RecyclerView.ViewHolder implements com.softeksol.paisalo.dealers.Adapters.PrePostDocsViewHolder {
        TextView docName;
        Button ImageUploadBtn;
        RecyclerView imageDocsRecView;
        public PrePostDocsViewHolder(@NonNull View itemView) {
            super(itemView);
            docName=itemView.findViewById(R.id.docName);
            imageDocsRecView=itemView.findViewById(R.id.imageDocsRecView);
            ImageUploadBtn=itemView.findViewById(R.id.ImageUploadBtn);
            ImageUploadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onImageClicked();
                }
            });


        }

    }
}
