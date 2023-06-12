package com.softeksol.paisalo.dealers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.softeksol.paisalo.dealers.Adapters.BankAdapter;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.dealers.Models.OEMBankResponse;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBankForDealer extends AppCompatActivity {
RecyclerView recviewDealerBank;
ApiInterface apiInterface;
FloatingActionButton fab;
int DealerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_for_dealer);
        fab=findViewById(R.id.fabAddBank);
        DealerId=getIntent().getIntExtra("DealerId",0);
        recviewDealerBank=findViewById(R.id.recviewDealerBank);
        apiInterface=  new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);

        recviewDealerBank.setLayoutManager(new LinearLayoutManager(this));
        Call<BrandResponse> call= apiInterface.getBankListByDealer(SEILIGL.NEW_TOKEN,DealerId);
        call.enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                Log.d("TAG", "onResponse: "+response.body());
                BrandResponse brandResponse=response.body();
                Gson gson = new Gson();
                OEMBankResponse[] nameList = gson.fromJson(brandResponse.getData(), OEMBankResponse[].class);
                recviewDealerBank.setAdapter(new BankAdapter(AddBankForDealer.this,nameList));

            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AddBankForDealer.this, AddOemBank.class);
                i.putExtra("Id",DealerId);
                i.putExtra("type","Dealer");
                startActivity(i);
            }
        });
    }
}