package com.softeksol.paisalo.ESign.activities;

import static com.softeksol.paisalo.jlgsourcing.Global.ESIGN_TYPE_TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softeksol.paisalo.ESign.adapters.AdapterListESignBorrower;
import com.softeksol.paisalo.ESign.dto.UnsignedDocRequest;
import com.softeksol.paisalo.jlgsourcing.BuildConfig;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.IglPreferences;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.WebOperations;
import com.softeksol.paisalo.jlgsourcing.entities.ESignBorrower;
import com.softeksol.paisalo.jlgsourcing.entities.ESignBorrower_Table;
import com.softeksol.paisalo.jlgsourcing.entities.ESignGuarantor;
import com.softeksol.paisalo.jlgsourcing.entities.ESigner;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;
import com.softeksol.paisalo.jlgsourcing.handlers.FileAsyncResponseHandler;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
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

public class ActivityESingList extends AppCompatActivity {
    private List<ESignBorrower> eSignBorrowers = new ArrayList<>();
    private AdapterListESignBorrower adapterListESignBorrower;
    private Manager manager;
    private int esignType;
    EditText edt_tvSearchFI;
//    ClientDataDao clientDataDao;

    @Override
    protected void onResume() {
        super.onResume();
//        if (esignType == 0)
            refreshPendingESign();
    }
    private void refreshPendingESign() {
        DataAsyncResponseHandler dataAsyncHttpResponseHandler = new DataAsyncResponseHandler(this, R.string.loan_financing) {

            //TODO : Progressbar
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (((responseBody.length > 10) && statusCode == 200)) { //
                    //pd.hide();
                    Log.d("TAG", "onSuccess: "+responseBody);
                    String responseString = new String(responseBody);
                    Log.d("TAG", "onSuccess: "+responseString);

                    //Log.d("Esigner Data", responseString);
                    if (esignType == 1) {
//                        clientDataDao.deleteESignBorrower();
//                        clientDataDao.deleteESignGuarantor();
                        SQLite.delete().from(ESignBorrower.class).where().execute();
                        SQLite.delete().from(ESignGuarantor.class).where().execute();

                    }
                    try {
                        JSONObject jo = new JSONObject(responseString);
                        Type listType;
//                        if (jo.has("PendingESignFI")) {
//                            listType = new TypeToken<List<ESignBorrower>>() {
//                            }.getType();
//                            List<ESignBorrower> eSignBorrowers = WebOperations.convertToObjectArray(jo.getString("PendingESignFI"), listType);
//                            adapterListESignBorrower.clear();
//                            if (esignType == 1) {
//
//                                for (ESignBorrower eSignBorrower : eSignBorrowers) {
//                                                                        eSignBorrower.insert();
////                                    clientDataDao.insertESignBorrower(eSignBorrower);
//                                }
//                                for (ESignBorrower eSignBorrower : eSignBorrowers) {
//
//                                    adapterListESignBorrower.addAll(eSignBorrower);
//                                }
//
//                            } else {
//                                adapterListESignBorrower.addAll(eSignBorrowers);
//                            }
//
//                            adapterListESignBorrower.notifyDataSetChanged();
//                        }
                        if (jo.has("PendingESignFI")) {
                            listType = new TypeToken<List<ESignBorrower>>() {
                            }.getType();
                            List<ESignBorrower> eSignBorrowers = WebOperations.convertToObjectArray(jo.getString("PendingESignFI"), listType);
                            adapterListESignBorrower.clear();
                            if (esignType == 1) {
                                for (ESignBorrower eSignBorrower : eSignBorrowers) {
                                    eSignBorrower.insert();
                                }
                                adapterListESignBorrower.addAll(getESignBorrowers(manager));
                            } else {
                                adapterListESignBorrower.addAll(eSignBorrowers);
                            }
                            adapterListESignBorrower.addArraylist(eSignBorrowers);
                            adapterListESignBorrower.notifyDataSetChanged();
                        }
//                        if (jo.has("Guarantors")) {
//                            listType = new TypeToken<List<ESignGuarantor>>() {
//                            }.getType();
//                            List<ESignGuarantor> eSignGuarantors = WebOperations.convertToObjectArray(jo.getString("Guarantors"), listType);
//
////                            clientDataDao.insertESignGuarantor(eSignGuarantors);
//                                                        for (ESignGuarantor eSignGuarantor : eSignGuarantors) {
//                                                            eSignGuarantor.insert();
//                                                        }
//                        }

                        if (jo.has("Guarantors")) {

                            listType = new TypeToken<List<ESignGuarantor>>() {
                            }.getType();
                            List<ESignGuarantor> eSignGuarantors = WebOperations.convertToObjectArray(jo.getString("Guarantors"), listType);
                           //Log.d("Guarantorslog", jo.getString("Guarantors"));
                            for (ESignGuarantor eSignGuarantor : eSignGuarantors) {
                                eSignGuarantor.insert();
                            }
                        }
                        //TODO : deletion of completely eSigned fingerprint having no reference with either ESignBorrower or ESignGuarantor
                        Utils.showSnakbar(findViewById(android.R.id.content), "Data Downloaded Successfully");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //String responseString = new String(responseBody);
                Log.d("Error Response", statusCode + " " + error.getMessage());
                Utils.showSnakbar(findViewById(android.R.id.content), statusCode + " " + "" + " " + error.getMessage());
            }
        };
        RequestParams params = new RequestParams();
        params.add("Creator", manager.Creator);
        params.add("FoCode", manager.FOCode);
        params.add("IMEINO", IglPreferences.getPrefString(this, SEILIGL.DEVICE_IMEI, "0"));
        if (esignType == 0) {
            params.add("CityCode", manager.AreaCd);
            Log.d("TAG", "refreshPendingESign: "+params);

            (new WebOperations()).getEntityESign(this, "POSDB", "GetDataForESignCheck", params, dataAsyncHttpResponseHandler);
        } else {
            (new WebOperations()).getEntityESign(this, "POSDB", "GetDataPendingForESign", params, dataAsyncHttpResponseHandler);
        }

    }

//    List<ESignBorrower> getESignBorrowers(Manager manager) {
////        SQLOperator condition=
////        Log.d("Manager",manager.toString());
////                return SQLite.select().from(ESignBorrower.class)
////                        .where(ESignBorrower_Table.Creator.eq(manager.Creator))
////                        .and(ESignBorrower_Table.FoCode.eq(manager.FOCode))
////                        .and(ESignBorrower_Table.CityCode.eq(manager.AreaCd))
////                        .queryList();
////        return clientDataDao.getESignBorrowers(manager.FOCode,manager.Creator,manager.AreaCd);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    List<ESignBorrower> getESignBorrowers(Manager manager) {
        //SQLOperator condition=
        //Log.d("Manager",manager.toString());
        return SQLite.select().from(ESignBorrower.class)
                .where(ESignBorrower_Table.Creator.eq(manager.Creator))
                .and(ESignBorrower_Table.FoCode.eq(manager.FOCode))
                .and(ESignBorrower_Table.CityCode.eq(manager.AreaCd))
                .queryList();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esing_list);
        getSupportActionBar().setTitle(getString(R.string.app_name) + " (" + BuildConfig.VERSION_NAME + ")" + " / ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        manager = (Manager) getIntent().getSerializableExtra(Global.MANAGER_TAG);
        esignType = getIntent().getIntExtra(ESIGN_TYPE_TAG, 1);
        Log.d("TAG", "onCreate: "+esignType);
        ListView lvESign = (ListView) findViewById(R.id.lvSelectListView);
        adapterListESignBorrower = new AdapterListESignBorrower(this, R.layout.layout_item_loan_app_info, eSignBorrowers);
        lvESign.setAdapter(adapterListESignBorrower);
        lvESign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ESignBorrower borrower = (ESignBorrower) adapterView.getAdapter().getItem(i);
                Intent intent;
                Log.d("TAG", "onItemClick: "+borrower.P_State);
                if (esignType == 1) {
                    intent = new Intent(ActivityESingList.this, ActivityLoanDetails.class);
                    Log.d("TAG", "onItemClick: "+borrower.AadharNo);
                    intent.putExtra(Global.BORROWER_TAG, borrower);
                    startActivity(intent);
                } else {
                    if (borrower.ESignSucceed.equals("BLK")) {
                        Utils.showSnakbar(view, "Data Mismatched, contact your Branch...");
                    } else {
                        downloadUnsignedDoc(view, borrower.getESigners().get(0),borrower);
                    }
                }
            }
        });

        edt_tvSearchFI=findViewById(R.id.edt_tvSearchFICode);
        edt_tvSearchFI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (adapterListESignBorrower != null) {
                    adapterListESignBorrower.getFilter().filter(charSequence);
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(adapterListESignBorrower != null) {
                    String text = edt_tvSearchFI.getText().toString();
                    adapterListESignBorrower.filter(text);


                }
            }
        });

    }

    private void downloadUnsignedDoc(View v, final ESigner eSigner, ESignBorrower borrower) {
        final View view = v;
        FileAsyncResponseHandler fileAsyncResponseHandler = new FileAsyncResponseHandler(this, "Loan Financing", "Downloading Document for " + eSigner.FiCode + "(" + eSigner.Creator + ")") {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.d("throwable ", throwable.getMessage());
                Toast.makeText(getBaseContext(), statusCode + "  " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                Utils.showSnakbar(findViewById(android.R.id.content), R.string.loan_detail_doc_download_failed);
                view.setClickable(true);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                if (statusCode == 200) {
                    Utils.showSnakbar(findViewById(android.R.id.content), R.string.loan_detail_doc_download_success);
                    Log.d("TAG", "onSuccess: "+file.getPath());


                //       getVHAccOpenForm(file.getPath(),eSigner,esignType,borrower);
                    eSigner.docPath = file.getPath();
                    Intent intent = new Intent(ActivityESingList.this, ActivityESignWithDocumentPL.class);
                    intent.putExtra(Global.ESIGNER_TAG, eSigner);
                    intent.putExtra(ESIGN_TYPE_TAG, esignType);
                    intent.putExtra("ESIGN_BORROWER", borrower);
                    startActivity(intent);
                    //startActivityForResult(intent, Global.ESIGN_REQUEST_CODE);

                }
            }
        };

        UnsignedDocRequest unsignedDocRequest = new UnsignedDocRequest();
        unsignedDocRequest.FiCreator = eSigner.Creator;
        unsignedDocRequest.FiCode = eSigner.FiCode;
        unsignedDocRequest.DocName = "loan_application" + (IglPreferences.getPrefString(this, SEILIGL.IS_ACTUAL, "").equals("Y") ? "_sample" : "");
        unsignedDocRequest.UserID = IglPreferences.getPrefString(this, SEILIGL.USER_ID, "");
        (new WebOperations()).postEntityESign(this, "DocESignLoanApplication", "downloadunsigneddoc", WebOperations.convertToJson(unsignedDocRequest), fileAsyncResponseHandler);
    }

    private void getVHAccOpenForm(String path, ESigner eSigner, int esignType, ESignBorrower borrower) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1,TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SEILIGL.NEW_SERVERAPISERVISE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();




        ApiInterface apiInterface= retrofit.create(ApiInterface.class);
        Call<JsonObject> call=apiInterface.getFileApplicationFormPdfForVHAccOpen(SEILIGL.NEW_TOKEN,eSigner.Creator,250003);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("TAG", "onResponse: "+response.body());

                byte[] pdfAsBytes = Base64.decode(response.body().get("data").getAsString(), 0);

                File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), eSigner.FiCode+eSigner.Creator+"getVHAccOpenForm.pdf");
                Log.d("TAG", "onResponse: "+filePath.getPath());
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(filePath, true);
                    os.write(pdfAsBytes);
                    os.flush();
                    os.close();
                }  catch (IOException e) {
                    throw new RuntimeException(e);
                }
                eSigner.docPath = filePath.getPath();
               // eSigner.docPath = path+","+filePath.getPath();
                AadharSehmatiFromPdf(eSigner.docPath,eSigner,esignType,borrower);




            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });

    }




    private void AadharSehmatiFromPdf(String path, ESigner eSigner, int esignType, ESignBorrower borrower) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1,TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SEILIGL.NEW_SERVERAPISERVISE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();




        ApiInterface apiInterface= retrofit.create(ApiInterface.class);
        Call<JsonObject> call=apiInterface.getFile(SEILIGL.NEW_TOKEN,eSigner.Creator,eSigner.FiCode);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("TAG", "onResponse: "+response.body());

                byte[] pdfAsBytes = Base64.decode(response.body().get("data").getAsString(), 0);

                File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "AadharSehmatiFromPdf_"+eSigner.FiCode+eSigner.Creator+".pdf");
                Log.d("TAG", "onResponse: "+filePath.getPath());
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(filePath, true);
                    os.write(pdfAsBytes);
                    os.flush();
                    os.close();
                }  catch (IOException e) {
                    throw new RuntimeException(e);
                }
             //   eSigner.docPath = path+","+filePath.getPath();
                    Intent intent = new Intent(ActivityESingList.this, ActivityESignWithDocumentPL.class);
                    intent.putExtra(Global.ESIGNER_TAG, eSigner);
                    intent.putExtra(ESIGN_TYPE_TAG, esignType);
                    intent.putExtra("ESIGN_BORROWER", borrower);
                    startActivity(intent);




            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });

    }

}