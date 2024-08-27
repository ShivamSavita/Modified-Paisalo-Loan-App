package com.digital.paisalo.jlgsourcing.activities;

import static com.digital.paisalo.jlgsourcing.Global.ESIGN_TYPE_TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.digital.paisalo.ESign.activities.ActivityESingList;
import com.digital.paisalo.jlgsourcing.ABFActivities.FiFormActivity;
import com.digital.paisalo.jlgsourcing.Global;
import com.digital.paisalo.jlgsourcing.R;
import com.digital.paisalo.jlgsourcing.SEILIGL;
import com.digital.paisalo.jlgsourcing.Utilities.IglPreferences;
import com.digital.paisalo.jlgsourcing.Utilities.Utils;
import com.digital.paisalo.jlgsourcing.WebOperations;
import com.digital.paisalo.jlgsourcing.adapters.AdapterListManager;
import com.digital.paisalo.jlgsourcing.entities.BankAccountData;
import com.digital.paisalo.jlgsourcing.entities.Manager;
import com.digital.paisalo.jlgsourcing.entities.RangeCategory;
import com.digital.paisalo.jlgsourcing.entities.dto.OperationItem;
import com.digital.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;


public class ActivityManagerSelect extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final int FILE_SELECT_CODE = 111;
    private AdapterListManager mla;
    private OperationItem operationItem;
    Intent intent;
    EditText edt_tvSearchGroup;
    //Manager manager;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_with_refresh);
        intent=getIntent();
        operationItem = (OperationItem) getIntent().getSerializableExtra(Global.OPTION_ITEM);
        getSupportActionBar().setTitle("Select Manager");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //FloatingActionButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSelectWithRefresh);
        fab.setOnClickListener(this);
        ListView listViewFM = (ListView) findViewById(R.id.lvSelectWithRefresh);
        edt_tvSearchGroup=findViewById(R.id.edt_tvSearchGroup);
        edt_tvSearchGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mla != null) {
                    mla.getFilter().filter(charSequence);
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(mla != null) {
                    String text = edt_tvSearchGroup.getText().toString().toLowerCase(Locale.getDefault());
                    mla.filter(text);
                }
            }
        });
        mla = new AdapterListManager(this, R.layout.manager_list_card, SQLite.select().from(Manager.class).queryList());
        listViewFM.setAdapter(mla);
        listViewFM.setOnItemClickListener(this);

        Log.d("TAG", "onCreate: bank account details"+SQLite.select().from(BankAccountData.class).queryList());
        if (SQLite.selectCountOf().from(RangeCategory.class).count() <= 0)
            RangeCategory.updateOptions(this);
        if (SQLite.selectCountOf().from(BankAccountData.class).count() <= 0)
            BankAccountData.updateBankAccounts(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manager_select, menu);
        menu.findItem(R.id.action_send_db_backup).setVisible(IglPreferences.getPrefString(this, SEILIGL.ALLOW_COLLECTION, "N").contains("B"));
        menu.findItem(R.id.action_restore_db).setVisible(IglPreferences.getPrefString(this, SEILIGL.ALLOW_COLLECTION, "N").contains("R"));
        menu.findItem(R.id.action_collection_details).setVisible(IglPreferences.getPrefString(this, SEILIGL.ALLOW_COLLECTION, "N").contains("C"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_update_options:
                RangeCategory.updateOptions(this);
                BankAccountData.updateBankAccounts(this);
                //updateOptions();
                break;
            case R.id.action_send_db_backup:
                backupDb();
                break;
            case R.id.action_restore_db:
                restoreDb();
                break;
            case R.id.action_collection_details:
                Intent intent = new Intent(this, ActivityCollectionDetails.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabSelectWithRefresh:
                updateManagers();
                break;
            default:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Manager manager = (Manager) parent.getItemAtPosition(position);
        IglPreferences.setSharedPref(this, Global.MANAGER_TAG, manager);
        Intent intent = null;
        switch (operationItem.getId()) {
            case 1:
                intent = new Intent(ActivityManagerSelect.this, FiFormActivity.class);
//                if (!manager.Creator.toUpperCase().startsWith("VH")){
//                    intent = new Intent(ActivityManagerSelect.this, FiFormActivity.class);
//                }else{
//                    intent = new Intent(ActivityManagerSelect.this, ActivityBorrowerKyc.class);
//                }

                intent.putExtra(Global.MANAGER_TAG, manager);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(ActivityManagerSelect.this, ActivityFinancing.class);
                intent.putExtra(Global.MANAGER_TAG, manager);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(ActivityManagerSelect.this, ActivityCollection.class);
                intent.putExtra(Global.MANAGER_TAG, manager);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(ActivityManagerSelect.this, ActivityDeposit.class);
                intent.putExtra(Global.MANAGER_TAG, manager);
                startActivity(intent);
                break;
            case 5:
                intent = new Intent(ActivityManagerSelect.this, ActivityPreClosure.class);
                intent.putExtra(Global.MANAGER_TAG, manager);
                startActivity(intent);
                break;
            case 6:
                final Intent intent1 = new Intent(this, ActivityESingList.class);
                intent1.putExtra(Global.MANAGER_TAG, manager);

                ArrayList<String> menuOptions = new ArrayList<>();
                if (IglPreferences.getPrefString(this, SEILIGL.ALLOW_COLLECTION, "N").contains("L")) {
                    menuOptions.add("Loan Application");
                }
                if (IglPreferences.getPrefString(this, SEILIGL.ALLOW_COLLECTION, "N").contains("E")) {
                    menuOptions.add("Loan Documentation");
                }
                if(menuOptions.size()>0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    String[] mOptions = new String[menuOptions.size()];
                    mOptions = menuOptions.toArray(mOptions);
                    builder.setItems(mOptions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            intent1.putExtra(ESIGN_TYPE_TAG, which);
                            startActivity(intent1);
                        }
                    });
                    builder.create().show();
                }else{
                    Utils.alert(this,"eSign Disabled");
                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void updateManagers() {
        //if (WebOperations.checkSession(this)) {
        DataAsyncResponseHandler dataAsyncHttpResponseHandler = new DataAsyncResponseHandler(this, "Loan Financing", "Downloading Manager's List") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String jsonString = new String(responseBody);
                Log.d("Manager_Data", jsonString);
                Type listType = new TypeToken<List<Manager>>() {
                }.getType();
                List<Manager> managers = WebOperations.convertToObjectArray(jsonString, listType);

                SQLite.delete().from(Manager.class).query();
                for (Manager manager : managers) {
                    manager.insert();
                }
                mla.clear();
                mla.addAll(SQLite.select().from(Manager.class).queryList());
                mla.notifyDataSetChanged();
                Toast.makeText(ActivityManagerSelect.this, "Managers List Updated", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    Log.d("failure", String.valueOf(statusCode) + "\n" + (new String(responseBody, "UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        };
        RequestParams params = new RequestParams();
        params.add("UserId", IglPreferences.getPrefString(this, SEILIGL.USER_ID, ""));
        params.add("IMEINO", IglPreferences.getPrefString(this, SEILIGL.DEVICE_IMEI, "0"));
        (new WebOperations()).getEntity(this, operationItem.getUrlController(), operationItem.getUrlEndpoint(), params, dataAsyncHttpResponseHandler);

    }

    private void backupDb() {
        AsyncHttpClient client = new AsyncHttpClient();
        DataAsyncResponseHandler responseHandler = new DataAsyncResponseHandler(this, "Loan Financing", "Uploading DB Backup") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);
                Utils.showSnakbar(findViewById(android.R.id.content), "Backup Completed successfully");
            }
        };
        RequestParams requestParams = WebOperations.configureBackupClient(ActivityManagerSelect.this, client, IglPreferences.getPrefString(ActivityManagerSelect.this, SEILIGL.USER_ID, ""));
        //Log.d("Url", IglPreferences.getPrefString(getContext(), SEILIGL.BASE_URL,"")+documentStore.apiRelativePath+ "  ");
        client.post(this, IglPreferences.getPrefString(this, SEILIGL.BASE_URL, "") + "/api/posdb/uploadsqlite", requestParams, responseHandler);
    }

    private void restoreDb() {
        String restorePath = Environment.getExternalStorageDirectory().getPath() + "/test/";
        File storageDir = new File(restorePath);
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Utils.showSnakbar(findViewById(android.R.id.content), "Directory could not be created");
            } else {
                (new File(restorePath, ".nomedia")).mkdirs();
            }
        }
        if (storageDir.isDirectory()) {
            GenericExtFilter filter = new GenericExtFilter(".db");
            File[] files = storageDir.listFiles(filter);
            showFileChooser(Uri.parse(restorePath));
        }
        //Log.d("Path",storageDir.getAbsolutePath());
    }

    private void replaceDB(File restoreDbFile) {
        File dbFile = new File(FlowManager.getContext().getDatabasePath("PAISALO_SRC_DB" + ".db").getPath());
        if (FlowManager.getContext().deleteDatabase("PAISALO_SRC_DB")) {
            //FlowManager.destroy();
            //if(dbFile.delete()){
            //restoreDbFile.renameTo(dbFile);
            try {
                dbFile.createNewFile();
                Utils.copyFile(restoreDbFile, dbFile);
                Utils.showSnakbar(findViewById(android.R.id.content), "Database Restored, Restarting the app");
                finish();
                System.exit(0);
            } catch (IOException ioe) {
                Utils.showSnakbar(findViewById(android.R.id.content), "Database Restored, error occured");
            }
        } else {
            Utils.showSnakbar(findViewById(android.R.id.content), "Cannot delete database");
        }
    }

    public class GenericExtFilter implements FilenameFilter {

        private String ext;

        public GenericExtFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(File dir, String name) {
            return (name.endsWith(ext));
        }
    }

    private void showFileChooser(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType("application/x-sqlite3");
        //intent.setDataAndType(uri, "application/x-sqlite3");
        intent.setDataAndType(uri, "*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    replaceDB(new File(uri.getPath()));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

