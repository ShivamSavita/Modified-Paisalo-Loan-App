package com.softeksol.paisalo.jlgsourcing.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.softeksol.paisalo.ESign.activities.ActivityESignWithDocumentPL;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.jlgsourcing.BuildConfig;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.WebOperations;
import com.softeksol.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashScreenPage extends AppCompatActivity {
    Button buttonGettignStarted;
    boolean h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_page);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashScreenPage.this, ActivityLogin.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            finish();
                        }
                    }, 1700);
//        Retrofit apiClient=new ApiClient().getClient2(SEILIGL.NEW_SERVER_BASEURL);
//        ApiInterface apiInter=apiClient.create(ApiInterface.class);
//        Call<BrandResponse> call=apiInter.getAppLinkStatus(BuildConfig.VERSION_NAME,"S",1);
//        call.enqueue(new Callback<BrandResponse>() {
//            @Override
//            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
//                BrandResponse brandResponse=response.body();
//                if (brandResponse.getData().length()<5){
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Intent intent = new Intent(SplashScreenPage.this, ActivityLogin.class);
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                            finish();
//                        }
//                    }, 1700);
//                }else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenPage.this);
//                    // below line is the title for our alert dialog.
//                    builder.setTitle("Need Update");
//                    // below line is our message for our dialog
//                    builder.setMessage("You are using older version of this app kindly update this app");
//                    builder.setPositiveButton("Update", (dialog, which) -> {
//                        // this method is called on click on positive button and on clicking shit button
//                        // we are redirecting our user from our app to the settings page of our app.
//                        dialog.cancel();
//                        String url = brandResponse.getData();
//                        Intent i = new Intent(Intent.ACTION_VIEW);
//                        i.setData(Uri.parse(url));
//                        startActivity(i);
//
//                    });
//                    builder.setNegativeButton("Cancel", (dialog, which) -> {
//                        // this method is called when user click on negative button.
//                        dialog.cancel();
//                     finish();
//                    });
//                    // below line is used to display our dialog
//                    builder.show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BrandResponse> call, Throwable t) {
//                Log.d("TAG", "onFailure: "+t.getMessage());
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intent = new Intent(SplashScreenPage.this, ActivityLogin.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//                        finish();
//                    }
//                }, 1700);
//            }
//        });



    }

}