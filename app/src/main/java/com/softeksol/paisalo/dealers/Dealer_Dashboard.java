package com.softeksol.paisalo.dealers;

import static com.softeksol.paisalo.jlgsourcing.Global.ESIGN_TYPE_TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.smarteist.autoimageslider.SliderView;
import com.softeksol.paisalo.ESign.adapters.SliderAdapter;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.IglPreferences;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.WebOperations;
import com.softeksol.paisalo.jlgsourcing.activities.ActivityManagerSelect;
import com.softeksol.paisalo.jlgsourcing.activities.ActivityOperationSelect;
import com.softeksol.paisalo.jlgsourcing.adapters.AdapterOperation;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.entities.dto.OperationItem;
import com.softeksol.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Dealer_Dashboard extends AppCompatActivity {

    SliderView sliderView;
    SliderAdapter adapter;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_dashboard);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ABF Dashboard");
        sliderView = findViewById(R.id.slider);

        int[] myImageList = new int[]{R.drawable.bannerback, R.drawable.bannerback,R.drawable.bannerback};
        adapter = new SliderAdapter(this, myImageList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        List<OperationItem> operationItems = new ArrayList<>();

            operationItems.add(new OperationItem(1, "OEM OnBoard", R.color.colorMenuKyc, "", ""));
            operationItems.add(new OperationItem(2, "Dealer OnBoard", R.color.colorMenuApplicationForm, "", ""));
            operationItems.add(new OperationItem(3, "Dealer Checklist", R.color.colorMenuApplicationForm, "", ""));

        GridView lv = (GridView) findViewById(R.id.lvcOpSelectDealer);

        lv.setAdapter(new AdapterOperation(Dealer_Dashboard.this, operationItems));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OperationItem operationItem = (OperationItem) parent.getAdapter().getItem(position);
                updateManagers(operationItem);
            }
        });
    }
    private void updateManagers(final OperationItem operationItem) {
        //if (WebOperations.checkSession(this)) {
//        DataAsyncResponseHandler dataAsyncHttpResponseHandler = new DataAsyncResponseHandler(this, "Loan Financing", "Downloading Manager's List") {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String jsonString = new String(responseBody);
//                Log.d("ManageDataLatest", jsonString);
//                Type listType = new TypeToken<List<Manager>>() {
//                }.getType();
//
//                List<Manager> managers = WebOperations.convertToObjectArray(jsonString, listType);
//                Log.d("CheckSizeList",managers.size()+"");
//
//                SQLite.delete().from(Manager.class).query();
//                for (Manager manager : managers){
//                    manager.insert();
//                }


                switch (operationItem.getId()) {
                    case 1:
                        Intent intent2 = new Intent(Dealer_Dashboard.this, SelectOEMpage.class);
                        startActivity(intent2);

                        break;
                    case 2:
                        Intent intent3 = new Intent(Dealer_Dashboard.this, DealersListPage.class);
                        startActivity(intent3);
//                        ArrayList<String> menuOptions = new ArrayList<>();
//                        if (IglPreferences.getPrefString(this, SEILIGL.ALLOW_COLLECTION, "N").contains("L")) {
//                            menuOptions.add("On-boarding");
//                        }
//                        if (IglPreferences.getPrefString(this, SEILIGL.ALLOW_COLLECTION, "N").contains("E")) {
//                            menuOptions.add("Branch Open");
//                        }
//                        if(menuOptions.size()>0){
//                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                            String[] mOptions = new String[menuOptions.size()];
//                            mOptions = menuOptions.toArray(mOptions);
//                            builder.setItems(mOptions, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    if (which==0){
//                                        Intent intent2 = new Intent(Dealer_Dashboard.this, DealerOnBoard.class);
//                                        startActivity(intent2);
//                                    }else{
//                                        Intent intent2 = new Intent(Dealer_Dashboard.this, Dealer_Branch_Open.class);
//                                        startActivity(intent2);
//
//                                    }
//
//
//                                }
//                            });
//                            builder.create().show();
//                        }else{
//                            Utils.alert(this,"eSign Disabled");
//                        }
                        break;
                    case 3:
                      Intent  intent4 = new Intent(Dealer_Dashboard.this, Dealer_CheckList.class);
                        startActivity(intent4);

                        break;

                }



//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.d("failure", String.valueOf(statusCode) + "\n" + (new String(responseBody, StandardCharsets.UTF_8)));
//            }
//        };
//
//        RequestParams params = new RequestParams();
//        params.add("UserId", IglPreferences.getPrefString(this, SEILIGL.USER_ID, ""));
//        params.add("IMEINO", IglPreferences.getPrefString(this, SEILIGL.DEVICE_IMEI, "0"));
//        //params.add("Operation", operationItem.getUrlEndpoint());
//        (new WebOperations()).getEntity(this, operationItem.getUrlController(), operationItem.getUrlEndpoint(), params, dataAsyncHttpResponseHandler);
    }
}