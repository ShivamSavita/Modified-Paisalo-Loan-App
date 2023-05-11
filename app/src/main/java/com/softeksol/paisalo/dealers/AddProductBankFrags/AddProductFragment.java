package com.softeksol.paisalo.dealers.AddProductBankFrags;

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
import com.softeksol.paisalo.dealers.Adapters.OEMListAdapter;
import com.softeksol.paisalo.dealers.Adapters.ProductListAdapter;
import com.softeksol.paisalo.dealers.Models.BrandResponse;
import com.softeksol.paisalo.dealers.Models.OEMDataModel;
import com.softeksol.paisalo.dealers.Models.ProductDataModel;
import com.softeksol.paisalo.dealers.SelectOEMpage;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FloatingActionButton fab;
    RecyclerView recViewProductList;

    public AddProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view= inflater.inflate(R.layout.fragment_add_product, container, false);
        fab=view.findViewById(R.id.fabAddProduct);
        recViewProductList=view.findViewById(R.id.recViewProductList);
        recViewProductList.setLayoutManager(new LinearLayoutManager(getContext()));

        ApiInterface apiInterface=  new ApiClient().getClient(SEILIGL.NEW_SERVER_BASEURL).create(ApiInterface.class);
        Call<BrandResponse> call=apiInterface.getProductList("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJJZCI6IjEiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiYWRtaW5AcGFpc2Fsby5pbiIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2VtYWlsYWRkcmVzcyI6ImFkbWluQHBhaXNhbG8uaW4iLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjEiLCJSb2xlIjpbIkFETUlOIiwiQURNSU4iXSwiQnJhbmNoQ29kZSI6IjAwMSIsIkNyZWF0b3IiOiJBR1JBIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9leHBpcmF0aW9uIjoiTWF5IFRodSAwNCAyMDIzIDA1OjAzOjIwIEFNIiwibmJmIjoxNjgzMDkwMjAwLCJleHAiOjE2ODMxNTY4MDAsImlzcyI6Imh0dHBzOi8vbG9jYWxob3N0OjcxODgiLCJhdWQiOiJodHRwczovL2xvY2FsaG9zdDo3MTg4In0.49Kz4R89gT4i7umarNA249zHubU7-_rMvupwg1dE6X8",2);
        call.enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                Log.d("TAG", "onResponse: "+response.body());
                BrandResponse brandResponse=response.body();
                Log.d("TAG", "onResponse: "+brandResponse.getData());
                Gson gson = new Gson();
                ProductDataModel[] oemProductModelList=gson.fromJson(brandResponse.getData(),ProductDataModel[].class);
                recViewProductList.setAdapter(new ProductListAdapter(getContext(),oemProductModelList));


            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {

            }
        });




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return  view;
    }
}