package com.softeksol.paisalo.dealers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.softeksol.paisalo.dealers.Adapters.AlgorithmAdapter;
import com.softeksol.paisalo.dealers.Adapters.VehicaalAdapter;
import com.softeksol.paisalo.dealers.Models.BrandData;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.dealers.Models.VehicalDataModel;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateProductPage extends AppCompatActivity {
Spinner spinnerModelType,spinnerVehicalType;
ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product_page);
        spinnerModelType=findViewById(R.id.spinnerModelType);
        spinnerVehicalType=findViewById(R.id.spinnerVehicalType);
        apiInterface=new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);

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




    }
}