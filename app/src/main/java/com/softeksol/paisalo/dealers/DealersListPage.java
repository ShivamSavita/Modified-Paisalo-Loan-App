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
import com.softeksol.paisalo.dealers.Adapters.DealerListAdapter;
import com.softeksol.paisalo.dealers.Adapters.DealerNestedAdapeter;
import com.softeksol.paisalo.dealers.Adapters.OEMListAdapter;
import com.softeksol.paisalo.dealers.Models.AllDealersModel;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.dealers.Models.OEMDataModel;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealersListPage extends AppCompatActivity {
    DealerNestedAdapeter adapter;
    RecyclerView recviewDealersList;
    List<String> headerDateList=new ArrayList<>();
    FloatingActionButton fab;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealers_list_page);
        getSupportActionBar().setTitle("Dealer List Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recviewDealersList=findViewById(R.id.recviewDealersList);
        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DealersListPage.this,DealerOnBoard.class));
            }
        });
        recviewDealersList.setLayoutManager(new LinearLayoutManager(this));
        ApiInterface apiInterface=new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);
        Call<BrandResponse> call=apiInterface.getAllDealers(SEILIGL.NEW_TOKEN);
        call.enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {

                Log.d("TAG", "onResponse: "+response.body());
                BrandResponse brandResponse=response.body();
                Gson gson = new Gson();
                AllDealersModel[] allDealersModels=gson.fromJson(brandResponse.getData(),AllDealersModel[].class);
                List<AllDealersModel> allDealersModelslist = Arrays.asList(allDealersModels);
                for (int i = 0; i < allDealersModels.length; i++) {
                    headerDateList.add(allDealersModels[i].getCreator());
                }
                List<String> createrHeaderList = removeDuplicates(headerDateList);
                adapter=new DealerNestedAdapeter(DealersListPage.this,allDealersModelslist,createrHeaderList);
//                Log.d("TAG", "onResponse: "+allDealersModels[0].getFirmName());
//                adapter=new DealerListAdapter(DealersListPage.this,allDealersModels);
                recviewDealersList.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {

            }
        });

    }
    private ArrayList<String> removeDuplicates(List<String> headerDateList) {
        // Create a new ArrayList
        ArrayList<String> newList = new ArrayList<String>();

        // Traverse through the first list
        for (String element : headerDateList) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }
}