package com.softeksol.paisalo.jlgsourcing.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.smarteist.autoimageslider.SliderView;
import com.softeksol.paisalo.ESign.adapters.SliderAdapter;
import com.softeksol.paisalo.dealers.Dealer_Dashboard;
import com.softeksol.paisalo.jlgsourcing.BuildConfig;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.IglPreferences;
import com.softeksol.paisalo.jlgsourcing.WebOperations;
import com.softeksol.paisalo.jlgsourcing.adapters.AdapterListManager;
import com.softeksol.paisalo.jlgsourcing.adapters.AdapterOperation;
import com.softeksol.paisalo.jlgsourcing.entities.DataEMI;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.entities.ProcessingEmiData;
import com.softeksol.paisalo.jlgsourcing.entities.dto.OperationItem;
import com.softeksol.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;
import com.softeksol.paisalo.jlgsourcing.location.GpsTracker;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityOperationSelect extends AppCompatActivity {
    private AdapterListManager adapterListManager;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    SliderView sliderView;
    SliderAdapter adapter;
    GpsTracker gpsTracker;
    List<DataEMI> ProcessingEmi_data=new ArrayList<>();
    boolean itemsChecked=true;
    String address="";
    //boolean[] itemsChecked = new boolean[items.length];




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_select);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        gpsTracker=new GpsTracker(getApplicationContext());
//        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("com.plcoding.backgroundlocationtracking","com.plcoding.backgroundlocationtracking.MainActivity"));
//        intent.putExtra("userId",IglPreferences.getPrefString(this, SEILIGL.USER_ID, ""));
//        intent.putExtra("deviceId",IglPreferences.getPrefString(this, SEILIGL.DEVICE_ID, ""));
//        intent.putExtra("groupCode",IglPreferences.getPrefString(this, SEILIGL.CREATOR, ""));
//        startActivity(intent);
         if(gpsTracker.getGPSstatus()==false){
             showSettingsAlert();
         }else{
             getLoginLocation("LOGIN","");

         }

        sliderView = findViewById(R.id.slider);
        int[] myImageList = new int[]{R.drawable.bannerback, R.drawable.bannerback,R.drawable.bannerback};
         adapter = new SliderAdapter(this, myImageList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close){
            public void onDrawerClosed(View view)
            {
                Log.d("drawerToggle", "Drawer closed");
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); //Creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView)
            {
                Log.d("drawerToggle", "Drawer opened");
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();


        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);
        getSupportActionBar().setElevation(0.0f);
        getSupportActionBar().setTitle(Html.fromHtml("<center><font color=\"black\">" + getString(R.string.app_name) + "</font></center>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        List<OperationItem> operationItems = new ArrayList<>();
        if (IglPreferences.getPrefString(this, SEILIGL.ALLOW_COLLECTION, "N").contains("S")) {
            operationItems.add(new OperationItem(1, "KYC", R.color.colorMenuKyc, "POSDB", "Getmappedfo"));
            operationItems.add(new OperationItem(2, "Application Form", R.color.colorMenuApplicationForm, "POSDB", "Getmappedfo"));
        }
        if (IglPreferences.getPrefString(this, SEILIGL.ALLOW_COLLECTION, "N").contains("C")) {
            operationItems.add(new OperationItem(3, "Collection", R.color.colorMenuCollection, "POSDB", "Getmappedfoforcoll"));
        }
        if (IglPreferences.getPrefString(this, SEILIGL.ALLOW_COLLECTION, "N").contains("D")) {
            operationItems.add(new OperationItem(4, "Deposit", R.color.colorMenuDeposit, "POSDB", "Getmappedfofordepo"));
        }
        if (IglPreferences.getPrefString(this, SEILIGL.ALLOW_COLLECTION, "N").contains("P")) {
            operationItems.add(new OperationItem(5, "Premature", R.color.colorMenuPremature, "casepremature", "FOForPreclosure"));
        }
        if (IglPreferences.getPrefString(this, SEILIGL.ALLOW_COLLECTION, "N").contains("C")) {
            operationItems.add(new OperationItem(6, "E-Sign", R.color.colorMenuPremature, "POSDB", "Getmappedfo"));
        }
//        if (IglPreferences.getPrefString(this, SEILIGL.ALLOW_COLLECTION, "N").contains("C")) {
//            operationItems.add(new OperationItem(7, "ABF Module", R.color.colorMenuPremature, "", ""));
//        }

        GridView lv = (GridView) findViewById(R.id.lvcOpSelect);

        lv.setAdapter(new AdapterOperation(ActivityOperationSelect.this, operationItems));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OperationItem operationItem = (OperationItem) parent.getAdapter().getItem(position);
                if (operationItem.getId()==7){
                   Intent intent = new Intent(ActivityOperationSelect.this, Dealer_Dashboard.class);
                   startActivity(intent);
                }else {
                    updateManagers(operationItem);
                }

            }
        });
       // getProcessingFee();
        LinearLayout layout_profile=findViewById(R.id.layout_profile);
        layout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder alertDialog = new
//                AlertDialog.Builder(ActivityOperationSelect.this);
//                View rowList = getLayoutInflater().inflate(R.layout.layout_prossingfee, null);
//                ListView listView = rowList.findViewById(R.id.list_processing);
//                AdapterProcessingFee adapter_list = new AdapterProcessingFee(ActivityOperationSelect.this, android.R.layout.simple_list_item_1, ProcessingEmi_data);
//                listView.setAdapter(adapter_list);
//                adapter.notifyDataSetChanged();
//                alertDialog.setView(rowList);
//                AlertDialog dialog = alertDialog.create();
//                dialog.show();
            }
        });



    }


    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityOperationSelect.this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void updateManagers(final OperationItem operationItem) {
        //if (WebOperations.checkSession(this)) {
        DataAsyncResponseHandler dataAsyncHttpResponseHandler = new DataAsyncResponseHandler(this, "Loan Financing", "Downloading Manager's List") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String jsonString = new String(responseBody);
                Log.d("ManageDataLatest", jsonString);
                Type listType = new TypeToken<List<Manager>>() {
                }.getType();

                List<Manager> managers = WebOperations.convertToObjectArray(jsonString, listType);
                Log.d("CheckSizeList",managers.size()+"");

                SQLite.delete().from(Manager.class).query();
                for (Manager manager : managers){
                    manager.insert();
                }

                Intent intent = null;
                switch (operationItem.getId()) {
                    case 1:
                        intent = new Intent(ActivityOperationSelect.this, ActivityManagerSelect.class);
                        break;
                    case 2:
                        intent = new Intent(ActivityOperationSelect.this, ActivityManagerSelect.class);
                        break;
                    case 3:
                        intent = new Intent(ActivityOperationSelect.this, ActivityManagerSelect.class);
                        break;
                    case 4:
                        intent = new Intent(ActivityOperationSelect.this, ActivityManagerSelect.class);
                        break;
                    case 5:
                        intent = new Intent(ActivityOperationSelect.this, ActivityManagerSelect.class);
                        break;
                    case 6:
                        intent = new Intent(ActivityOperationSelect.this, ActivityManagerSelect.class);
                        break;
                }

                assert intent != null;
                intent.putExtra(Global.OPTION_ITEM, operationItem);
                intent.putExtra("Title", operationItem.getOprationName());
                startActivity(intent);
                gpsTracker=new GpsTracker(getApplicationContext());
               // getLoginLocationText(operationItem.getOprationName());
                getLoginLocation(operationItem.getOprationName(),"");
                Log.d("CLICK",operationItem.getOprationName());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("failure", String.valueOf(statusCode) + "\n" + (new String(responseBody, StandardCharsets.UTF_8)));
            }
        };

        RequestParams params = new RequestParams();
        params.add("UserId", IglPreferences.getPrefString(this, SEILIGL.USER_ID, ""));
        params.add("IMEINO", IglPreferences.getPrefString(this, SEILIGL.DEVICE_IMEI, "0"));
        //params.add("Operation", operationItem.getUrlEndpoint());
        (new WebOperations()).getEntity(this, operationItem.getUrlController(), operationItem.getUrlEndpoint(), params, dataAsyncHttpResponseHandler);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item);
    }


//    private void getLoginLocationText(String login){
//        ApiInterface apiInterface= getClientService(SEILIGL.LOCATION).create(ApiInterface.class);
//        Log.d("TAG", "checkCrifScore: "+getdatalocation(login, address));
//        Call<JsonObject> call=apiInterface.getAppLocation("68cf2bbcdc429377533c9abd34ae457c",gpsTracker.getLatitude()+","+gpsTracker.getLongitude()+"");
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//               // Log.d("TAG", "onResponse: "+response.body());
//                JSONObject jo;
//                try {
//                    jo = new JSONObject(String.valueOf(response.body()));
//                    JSONArray jArray = jo.getJSONArray("data");
//                    JSONObject jsonObject = (JSONObject) jArray.get(0);
//                    address=jsonObject.getString("label");
//                    Log.d("TAG", "onResponse:address "+address);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                getLoginLocation(login,address);
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.d("TAG", "onFailure: "+t.getMessage());
//                getLoginLocation(login,address);
//            }
//        });
//    }
//

    private void getLoginLocation(String login,String address){
//        String bearerString = "";
//        try {
//            bearerString = IglPreferences.getAccesstoken(getApplicationContext()).getString("access_token");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        ApiInterface apiInterface= ApiClient.getClient(SEILIGL.NEW_SERVERAPI).create(ApiInterface.class);
        Log.d("TAG", "checkCrifScore: "+getdatalocation(login, address));
        Call<JsonObject> call=apiInterface.livetrack(getdatalocation(login,address));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("TAG", "onResponse: "+response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());

            }
        });
    }

    private JsonObject getdatalocation(String login, String address) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("userId", IglPreferences.getPrefString(this, SEILIGL.USER_ID, ""));
        jsonObject.addProperty("deviceId", IglPreferences.getPrefString(this, SEILIGL.DEVICE_ID, ""));
        jsonObject.addProperty("Creator", IglPreferences.getPrefString(this, SEILIGL.CREATOR, ""));
        jsonObject.addProperty("trackAppVersion", BuildConfig.VERSION_NAME);
        jsonObject.addProperty("latitude",gpsTracker.getLatitude()+"");
        jsonObject.addProperty("longitude", gpsTracker.getLongitude()+"");
        jsonObject.addProperty("appInBackground",login);
        jsonObject.addProperty("Address",getAddress(gpsTracker.getLatitude(),gpsTracker.getLongitude()));
        return jsonObject;
    }



    public static Retrofit getClientService(String BASE_URL) {
        Retrofit retrofit=null;
        if (retrofit==null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder(

            );
            httpClient.connectTimeout(1, TimeUnit.MINUTES);
            httpClient.readTimeout(1,TimeUnit.MINUTES);
            httpClient.addInterceptor(logging);
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    private String getAddress(double latitude, double longitude) {
        String addrerss="";
        StringBuilder result = new StringBuilder();
        if(latitude != 0.0){
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    result.append(address.getAddressLine(0));
                    // result.append(address.getLocality());
                    // result.append(address.getCountryName());
                    addrerss=result.toString();
                    Log.e("tag", addrerss);
                }
            } catch (IOException e) {
                //  Log.e("tag", e.getMessage());
            }
        }
        return addrerss;
    }


}
