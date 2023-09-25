package com.softeksol.paisalo.jlgsourcing.homevisit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.softeksol.paisalo.dealers.Adapters.HomeVisitMnagerAdapter;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.IglPreferences;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.WebOperations;
import com.softeksol.paisalo.jlgsourcing.entities.HomeVisitFiList;
import com.softeksol.paisalo.jlgsourcing.entities.HomeVisitListModel;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import a.a.e.d;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeVisitManagerList extends AppCompatActivity {

    RecyclerView recViewHVManagerList;
    HomeVisitMnagerAdapter adapter;
    private Manager manager;
    EditText edt_tvSearchGroupHV;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_visit_manager_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recViewHVManagerList=findViewById(R.id.recViewHVManagerList);
        recViewHVManagerList.setLayoutManager(new LinearLayoutManager(this));

        edt_tvSearchGroupHV=findViewById(R.id.edt_tvSearchGroupHV);
        edt_tvSearchGroupHV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (adapter != null) {
                    adapter.getFilter().filter(charSequence);
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(adapter != null) {
                    String text = edt_tvSearchGroupHV.getText().toString().toLowerCase(Locale.getDefault());
                    adapter.getFilter().filter(text);
                }
            }
        });
        manager = (Manager) getIntent().getSerializableExtra(Global.MANAGER_TAG);
        Log.d("TAG", "onCreate: "+ WebOperations.convertToJson(manager));
        getToken();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1,TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SEILIGL.NEW_SERVERAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<HomeVisitListModel> call=apiInterface.getHVManagerList(manager.Creator,manager.FOCode,manager.AreaCd, IglPreferences.getPrefString(this, SEILIGL.DEVICE_IMEI, "0"));
        call.enqueue(new Callback<HomeVisitListModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<HomeVisitListModel> call, Response<HomeVisitListModel> response) {
                Log.d("TAG", "onResponse of hv manager: "+response.body().getData());
                List<HomeVisitFiList> homeVisitManagerLists=response.body().getData();
                if (homeVisitManagerLists.size()>1){
                        adapter=new HomeVisitMnagerAdapter(HomeVisitManagerList.this,homeVisitManagerLists);
                        recViewHVManagerList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                }else{
                    Utils.alert(HomeVisitManagerList.this,"You Have not any case in this branch");
                }

            }

            @Override
            public void onFailure(Call<HomeVisitListModel> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());

            }
        });
    }

    private void getToken() {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("emailId", "dotnetdev2@paisalo.in");
        jsonObject.addProperty("password", "admin@123");
        jsonObject.addProperty("location", "string");

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1,TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://erpservice.paisalo.in:980/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<JsonObject> call=apiInterface.getTokenForABF(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("TAG", "onResponse: "+response.body());
                SEILIGL.NEW_TOKEN="Bearer "+ response.body().get("token").getAsString();
                Log.d("TAGs", "onCreate: "+SEILIGL.NEW_TOKEN);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAGs", "onFailure: "+t.getMessage());
            }
        });
    }
}