package com.softeksol.paisalo.jlgsourcing.homevisit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.client.cache.Resource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstPage extends AppCompatActivity {

    Retrofit retrofit;
    EditText applicantName,maritalStatus,LoanAmount,loanPurpose,applicantStatus,loanDuration,currentOccupation,occupationType,landOwnership,residenceOwnership;
    Button fillForm;
    ImageView aplicantImage;
    CheckBox confirmationBox;
    String creator = "AGRA",Rent_of_House,GroupCode,CityCode,Latitude,FiCode,Longitude;
    String ficode = "250003";
    String bearerToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJJZCI6IjE1MzkiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiZG90bmV0ZGV2MkBwYWlzYWxvLmluIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvZW1haWxhZGRyZXNzIjoiZG90bmV0ZGV2MkBwYWlzYWxvLmluIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvbmFtZWlkZW50aWZpZXIiOiIxNTM5IiwiUm9sZSI6WyJBRE1JTiIsIkFETUlOIl0sIkJyYW5jaENvZGUiOiIiLCJDcmVhdG9yIjoiIiwiVU5hbWUiOiJSQUpBTiBLVU1BUiIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvZXhwaXJhdGlvbiI6IlNlcCBUaHUgMjEgMjAyMyAwNjoyOTozOSBBTSIsIm5iZiI6MTY5NTE5MTM3OSwiZXhwIjoxNjk1MjU3OTc5LCJpc3MiOiJodHRwczovL2xvY2FsaG9zdDo3MTg4IiwiYXVkIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6NzE4OCJ9.lMtcP2gwW0UsBjq4U-iv8TZXnfxKVDe14XB6HVhzSgM";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        ficode=getIntent().getStringExtra("ficode");
        creator=getIntent().getStringExtra("creator");



        retrofit = new Retrofit.Builder()
                .baseUrl("https://erpservice.paisalo.in:980/PDL.FIService.API/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface jsonPlaceholder = retrofit.create(ApiInterface.class);
        String bearerToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJJZCI6IjE1MzkiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiZG90bmV0ZGV2MkBwYWlzYWxvLmluIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvZW1haWxhZGRyZXNzIjoiZG90bmV0ZGV2MkBwYWlzYWxvLmluIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvbmFtZWlkZW50aWZpZXIiOiIxNTM5IiwiUm9sZSI6WyJBRE1JTiIsIkFETUlOIl0sIkJyYW5jaENvZGUiOiIiLCJDcmVhdG9yIjoiIiwiVU5hbWUiOiJSQUpBTiBLVU1BUiIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvZXhwaXJhdGlvbiI6IlNlcCBUaHUgMjEgMjAyMyAwNjoyOTozOSBBTSIsIm5iZiI6MTY5NTE5MTM3OSwiZXhwIjoxNjk1MjU3OTc5LCJpc3MiOiJodHRwczovL2xvY2FsaG9zdDo3MTg4IiwiYXVkIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6NzE4OCJ9.lMtcP2gwW0UsBjq4U-iv8TZXnfxKVDe14XB6HVhzSgM";

        Call<FIDataModel> call = jsonPlaceholder.getDataById(SEILIGL.NEW_TOKEN,creator,ficode);
        call.enqueue(new Callback<FIDataModel>() {
            @Override
            public void onResponse(Call<FIDataModel> call, Response<FIDataModel> response) {

                try {
                    String responseString = response.body().getData();
                    JSONArray jsonArray = new JSONArray(responseString);

                    JSONObject jsonObjectAtIndex0 = jsonArray.getJSONObject(0);

                    String code = jsonObjectAtIndex0.getString("Code");
                    String tag = jsonObjectAtIndex0.getString("Tag");

                    applicantName.setText(jsonObjectAtIndex0.getString("Fname"));
                    maritalStatus.setText(jsonObjectAtIndex0.getString("isMarried"));
                    LoanAmount.setText(jsonObjectAtIndex0.getString("Loan_Amt"));
                    loanPurpose.setText(jsonObjectAtIndex0.getString("Loan_Reason"));
                    applicantStatus.setText("Not Handicapped");
                    loanDuration.setText(jsonObjectAtIndex0.getString("Loan_Duration"));
                    currentOccupation.setText(jsonObjectAtIndex0.getString("Business_Detail"));
          //          occupationType.setText(jsonObjectAtIndex0.getString("Fname"));
         //           landOwnership.setText(jsonObjectAtIndex0.getString("Fname"));
                    residenceOwnership.setText(jsonObjectAtIndex0.getString("House_Owner"));
         //           aplicantImage.setImageBitmap("Picture");

                    Rent_of_House = jsonObjectAtIndex0.getString("Rent_of_House");
                    GroupCode = jsonObjectAtIndex0.getString("GroupCode");
                    CityCode = jsonObjectAtIndex0.getString("CityCode");
                    Latitude =  jsonObjectAtIndex0.getString("Latitude");
                    Longitude = jsonObjectAtIndex0.getString("Longitude");
                    FiCode = jsonObjectAtIndex0.getString("Code");


                    String base64Image = jsonObjectAtIndex0.getString("Picture");

                    byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                    ImageView applicantImage = findViewById(R.id.applicantImage);
                    applicantImage.setImageBitmap(decodedBitmap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Sunny", "onResponse: "+ response.body().getData());

            }
            @Override
            public void onFailure(Call<FIDataModel> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });



        /*EditText*/
        applicantName = findViewById(R.id.applicantName);
        maritalStatus = findViewById(R.id.maritalStatus);
        LoanAmount = findViewById(R.id.loanAmount);
        loanPurpose = findViewById(R.id.purpose);
        loanDuration = findViewById(R.id.duration);
        applicantStatus = findViewById(R.id.applicantStatus);
        currentOccupation = findViewById(R.id.currentOccupation);
        occupationType = findViewById(R.id.occupationType);
        landOwnership = findViewById(R.id.landOwnership);
        residenceOwnership = findViewById(R.id.residenceOwnership);



        /*Image View*/
        aplicantImage = findViewById(R.id.applicantImage);

        /*Check Box*/
        confirmationBox = findViewById(R.id.confirmationBox);

        /*Button*/
        fillForm = findViewById(R.id.fillForm);
        fillForm.setEnabled(false);

        confirmationBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Enable the "Fill Form" button if the checkbox is checked, or disable it if unchecked
                fillForm.setEnabled(isChecked);
                if (isChecked){
                    fillForm.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }else {
                    fillForm.setBackgroundColor(getResources().getColor(R.color.dull_grey));

                }
            }
        });

        fillForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the MainActivity
                Intent intent = new Intent(FirstPage.this, SecondPage.class);

                // Put the strings as extras in the intent
                intent.putExtra("FiCode", FiCode);
                intent.putExtra("Rent_of_House", Rent_of_House);
                intent.putExtra("GroupCode",GroupCode);
                intent.putExtra("CityCode", CityCode);
                intent.putExtra("Latitude", Latitude);
                intent.putExtra("Longitude", Longitude);
                intent.putExtra("Creator", creator);

                startActivity(intent);
            }
        });
       // getFidata();
    }

    private void getFidata(String login,String address){
        ApiInterface apiInterface= ApiClient.getClient(SEILIGL.NEW_SERVERAPI).create(ApiInterface.class);
        Call<FIDataModel> call=apiInterface.getDataById(bearerToken,creator,ficode);
        call.enqueue(new Callback<FIDataModel>() {
            @Override
            public void onResponse(Call<FIDataModel> call, Response<FIDataModel> response) {
                Log.d("TAG", "onResponse: "+response.body());
                try {
                    String responseString = response.body().getData();
                    JSONArray jsonArray = new JSONArray(responseString);

                    JSONObject jsonObjectAtIndex0 = jsonArray.getJSONObject(0);

                    String code = jsonObjectAtIndex0.getString("Code");
                    String tag = jsonObjectAtIndex0.getString("Tag");

                    applicantName.setText(jsonObjectAtIndex0.getString("Fname"));
                    maritalStatus.setText(jsonObjectAtIndex0.getString("isMarried"));
                    LoanAmount.setText(jsonObjectAtIndex0.getString("Loan_Amt"));
                    loanPurpose.setText(jsonObjectAtIndex0.getString("Loan_Reason"));
                    applicantStatus.setText("Not Handicapped");
                    loanDuration.setText(jsonObjectAtIndex0.getString("Loan_Duration"));
                    currentOccupation.setText(jsonObjectAtIndex0.getString("Business_Detail"));
                    //occupationType.setText(jsonObjectAtIndex0.getString("Fname"));
                    //landOwnership.setText(jsonObjectAtIndex0.getString("Fname"));
                    residenceOwnership.setText(jsonObjectAtIndex0.getString("House_Owner"));
                    //aplicantImage.setImageBitmap("Picture");

                    Rent_of_House = jsonObjectAtIndex0.getString("Rent_of_House");
                    GroupCode = jsonObjectAtIndex0.getString("GroupCode");
                    CityCode = jsonObjectAtIndex0.getString("CityCode");
                    Latitude =  jsonObjectAtIndex0.getString("Latitude");
                    Longitude = jsonObjectAtIndex0.getString("Longitude");
                    FiCode = jsonObjectAtIndex0.getString("Code");


                    String base64Image = jsonObjectAtIndex0.getString("Picture");

                    byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                    ImageView applicantImage = findViewById(R.id.applicantImage);
                    applicantImage.setImageBitmap(decodedBitmap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Sunny", "onResponse: "+ response.body().getData());
            }

            @Override
            public void onFailure(Call<FIDataModel> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());

            }
        });
    }
}