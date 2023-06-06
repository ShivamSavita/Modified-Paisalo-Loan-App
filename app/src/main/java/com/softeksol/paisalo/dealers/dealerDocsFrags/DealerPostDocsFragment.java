package com.softeksol.paisalo.dealers.dealerDocsFrags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softeksol.paisalo.dealers.Adapters.DocImageAddViewAdapter;
import com.softeksol.paisalo.dealers.Adapters.VehicaalAdapter;
import com.softeksol.paisalo.dealers.CreateProductPage;
import com.softeksol.paisalo.dealers.ImageClickListener;
import com.softeksol.paisalo.dealers.Models.ABFDocsModel;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.dealers.Models.VehicalDataModel;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerPostDocsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    RecyclerView recviewDealerPosDocs;
    private String mParam2;

    DocImageAddViewAdapter adapter;
int DealerId;
    public DealerPostDocsFragment(int dealerId) {
        this.DealerId=DealerId;
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dealer_post_docs, container, false);
        recviewDealerPosDocs=view.findViewById(R.id.recviewDealerPosDocs);
        recviewDealerPosDocs.setLayoutManager(new LinearLayoutManager(getContext()));
        ApiInterface apiInterface=new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);
        Call<BrandResponse> call=apiInterface.getABfDocs(SEILIGL.NEW_TOKEN,"post",DealerId);
        call.enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {

                Log.d("TAG", "onResponse: "+response.body());
                BrandResponse brandResponse=response.body();
                Gson gson = new Gson();
                ABFDocsModel[] nameList = gson.fromJson(brandResponse.getData(), ABFDocsModel[].class);
//                adapter=new DocImageAddViewAdapter(getContext(), nameList, new ImageClickListener() {
//                    @Override
//                    public void onImageClicked() {
//                        Toast.makeText(getContext(), "Clicked on Post docs", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                    recviewDealerPosDocs.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {

            }
        });
        return  view;
    }
}