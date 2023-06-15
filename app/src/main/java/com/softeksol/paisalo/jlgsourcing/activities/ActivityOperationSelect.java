package com.softeksol.paisalo.jlgsourcing.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

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
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.entities.dto.OperationItem;
import com.softeksol.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;
import com.softeksol.paisalo.jlgsourcing.location.GpsTracker;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;
import com.softeksol.paisalo.jlgsourcing.retrofit.CheckCrifData;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityOperationSelect extends AppCompatActivity {
    private AdapterListManager adapterListManager;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    SliderView sliderView;
    SliderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_select);
        drawerLayout = findViewById(R.id.my_drawer_layout);
//        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("com.plcoding.backgroundlocationtracking","com.plcoding.backgroundlocationtracking.MainActivity"));
//        intent.putExtra("userId",IglPreferences.getPrefString(this, SEILIGL.USER_ID, ""));
//        intent.putExtra("deviceId",IglPreferences.getPrefString(this, SEILIGL.DEVICE_ID, ""));
//        intent.putExtra("groupCode",IglPreferences.getPrefString(this, SEILIGL.CREATOR, ""));
//        startActivity(intent);
        getLoginLocation();
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
                getLoginLocation();
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

    private void getLoginLocation(){
        ApiInterface apiInterface= ApiClient.getClient(SEILIGL.NEW_SERVERAPI).create(ApiInterface.class);
        Log.d("TAG", "checkCrifScore: "+getdatalocation());
        Call<JsonObject> call=apiInterface.livetrack(getdatalocation());
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

    private JsonObject getdatalocation() {
        GpsTracker gpsTracker=new GpsTracker(getApplicationContext());
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("userId", IglPreferences.getPrefString(this, SEILIGL.USER_ID, ""));
        jsonObject.addProperty("deviceId", IglPreferences.getPrefString(this, SEILIGL.DEVICE_ID, ""));
        jsonObject.addProperty("groupCode", IglPreferences.getPrefString(this, SEILIGL.CREATOR, ""));
        jsonObject.addProperty("trackAppVersion", BuildConfig.VERSION_NAME);
        jsonObject.addProperty("latitude",gpsTracker.getLatitude()+"");
        jsonObject.addProperty("longitude", gpsTracker.getLongitude()+"");
        jsonObject.addProperty("appInBackground","1");
        return jsonObject;

    }
}
