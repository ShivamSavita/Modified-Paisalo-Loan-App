package com.softeksol.paisalo.dealers.AddProductBankFrags;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.softeksol.paisalo.dealers.Adapters.AlgorithmAdapter;
import com.softeksol.paisalo.dealers.Adapters.BankAdapter;
import com.softeksol.paisalo.dealers.AddOemBank;
import com.softeksol.paisalo.dealers.Models.BrandData;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.dealers.Models.OEMBankResponse;
import com.softeksol.paisalo.dealers.OEM_Onboarding;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import java.security.cert.CertPathValidatorException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddBankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FloatingActionButton fab;
    RecyclerView recviewOEMBank;
    int OEMid;
    ApiInterface apiInterface;
    public AddBankFragment(int OEMid) {
        this.OEMid=OEMid;
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
        View view= inflater.inflate(R.layout.fragment_add_bank, container, false);
        fab=view.findViewById(R.id.fabAddBank);
        recviewOEMBank=view.findViewById(R.id.recviewOEMBank);
        apiInterface=  new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);

        recviewOEMBank.setLayoutManager(new LinearLayoutManager(getContext()));
        Call<BrandResponse> call= apiInterface.getBankList(SEILIGL.NEW_TOKEN,OEMid);
        call.enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                Log.d("TAG", "onResponse: "+response.body());
                BrandResponse brandResponse=response.body();
                Gson gson = new Gson();
                OEMBankResponse[] nameList = gson.fromJson(brandResponse.getData(), OEMBankResponse[].class);
                recviewOEMBank.setAdapter(new BankAdapter(getContext(),nameList));

            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), AddOemBank.class);
                i.putExtra("Id",OEMid);
                i.putExtra("type","OEM");
               startActivity(i);
            }
        });
        return view;
    }
}