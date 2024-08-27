package com.digital.paisalo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.digital.paisalo.jlgsourcing.BuildConfig;
import com.digital.paisalo.jlgsourcing.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author fredrik.persson
 */
public class Setup extends Thread {
    public Setup(Context context){
        m_context = context;
        m_sdcard = Environment.getExternalStoragePublicDirectory("Download");
    }

    public void run(){
        File cfgdir = new File(String.valueOf(m_sdcard));
        if(!cfgdir.exists()){
            cfgdir.mkdirs();
        }

        copyResources(R.raw.myapk);

    }
    private void installApkProgramatically() {

        Log.i("TAG", "installApkProgramatically");
        try {
            File path = m_context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

            File file = new File(m_sdcard, "/myapk.apk");

            Uri uri;

            if (file.exists()) {

//                Intent unKnownSourceIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", m_context.getPackageName())));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    Uri fileUri = FileProvider.getUriForFile(m_context, m_context.getApplicationContext().getPackageName() + ".provider", file);
                    Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
                    intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                    intent.setDataAndType(fileUri, "application/vnd.android" + ".package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    m_context.startActivity(intent);


                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    Intent intent1 = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    uri = FileProvider.getUriForFile(m_context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
                    m_context.grantUriPermission("com.softeksol.paisalo", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    m_context.grantUriPermission("com.softeksol.paisalo", uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent1.setDataAndType(uri,
                            "application/*");
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent1.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    m_context.startActivity(intent1);

                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    uri = Uri.fromFile(file);
                    intent.setDataAndType(uri,
                            "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    m_context.startActivity(intent);
                }
            } else {
                File cfgdir = new File(String.valueOf(m_sdcard));
                if(!cfgdir.exists()){
                    cfgdir.mkdirs();
                }

                copyResources(R.raw.myapk);
                Log.i("TAG", " file " + file.getPath() + " does not exist");
            }
        } catch (Exception e) {

            Log.i("TAG", "" + e.getMessage());

        }
    }

    public void copyResources(int resId){

        Log.i("Test", "Setup::copyResources");
        InputStream in = m_context.getResources().openRawResource(resId);
        String filename = m_context.getResources().getResourceEntryName(resId);

        File f = new File(m_sdcard, "/"+filename+".apk");
        Log.i("copyResources", filename);
        if(!f.exists()){
            try {
                OutputStream out = new FileOutputStream(new File(m_sdcard, "/"+filename+".apk"));
                byte[] buffer = new byte[1024];
                int len;
                while((len = in.read(buffer, 0, buffer.length)) != -1){
                    out.write(buffer, 0, len);
                }
                in.close();
                out.close();
                installApkProgramatically();
                Toast.makeText(m_context, "Done", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Log.i("Test", "FileNotFoundException - "+e.getMessage());
            } catch (IOException e) {
                Log.i("Test", "IOException - "+e.getMessage());
            }
        }else {
            installApkProgramatically();
        }
    }

    Context m_context;
    File m_sdcard;
    String m_configdir = "/config";
}