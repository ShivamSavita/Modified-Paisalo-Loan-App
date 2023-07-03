package com.softeksol.paisalo.ESign.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softeksol.paisalo.ESign.adapters.AdapterListESigner;
import com.softeksol.paisalo.ESign.dto.UnsignedDocRequest;
import com.softeksol.paisalo.jlgsourcing.BuildConfig;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.DateUtils;
import com.softeksol.paisalo.jlgsourcing.Utilities.IglPreferences;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.WebOperations;
import com.softeksol.paisalo.jlgsourcing.entities.ESignBorrower;
import com.softeksol.paisalo.jlgsourcing.entities.ESignBorrower_Table;
import com.softeksol.paisalo.jlgsourcing.entities.ESigner;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.entities.Manager_Table;
import com.softeksol.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;
import com.softeksol.paisalo.jlgsourcing.handlers.FileAsyncResponseHandler;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class ActivityLoanDetails extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView lvLoanDetails;
    private ESignBorrower eSignBorrower;
    private ESigner eSigner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_details);
        getSupportActionBar().setTitle(getString(R.string.app_name) + " (" + BuildConfig.VERSION_NAME + ")" + " / " + getString(R.string.loan_detail));
        Intent data = getIntent();
        eSignBorrower = (ESignBorrower) data.getSerializableExtra(Global.BORROWER_TAG);
        String FOName = SQLite.select(Manager_Table.FOName).from(Manager.class)
                .where(Manager_Table.FOCode.eq(eSignBorrower.FoCode))
                .and(Manager_Table.Creator.eq(eSignBorrower.Creator))
                .querySingle().FOName;
        ((TextView) findViewById(R.id.tvLoanDetailCaseLocation)).setText(eSignBorrower.Creator);
        ((TextView) findViewById(R.id.tvLoanDetailFmName)).setText((FOName == null ? "" : FOName) + "" + eSignBorrower.FoCode);
        ((TextView) findViewById(R.id.tvLoanDetailFiCode)).setText(String.valueOf(eSignBorrower.FiCode));
        ((TextView) findViewById(R.id.tvLoanDetailAmount)).setText(String.valueOf(eSignBorrower.SanctionedAmt));
        ((TextView) findViewById(R.id.tvLoanDetailPeriod)).setText(String.valueOf(eSignBorrower.Period));
        ((TextView) findViewById(R.id.tvLoanDetailInterestRate)).setText(String.valueOf(eSignBorrower.IntRate));

        Button btnDownloadDoc = (Button) findViewById(R.id.btnLoanDetailsDownloadDoc);
        btnDownloadDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                downloadUnsignedDoc(view);
            }
        });
        Button btnRequestDisb = (Button) findViewById(R.id.btnLoanDetailsRequestDisbursement);
        btnRequestDisb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
                RequestDisbursement(view);
            }
        });

        lvLoanDetails = (ListView) findViewById(R.id.lvLoanDetails);
        btnDownloadDoc.setVisibility(eSignBorrower.hasSomeSigned() ? View.VISIBLE : View.GONE);
        btnRequestDisb.setVisibility((eSignBorrower.hasAllESigned() && (eSignBorrower.DisbRequested == null)) ? View.VISIBLE : View.GONE);
        lvLoanDetails.setAdapter(new AdapterListESigner(this, eSignBorrower.getESigners()));
        lvLoanDetails.setOnItemClickListener(this);
    }

    private void RequestDisbursement(View v) {
        final View view = v;
        view.setClickable(false);
        RequestParams params = new RequestParams();
        params.put("Creator", eSignBorrower.Creator);
        params.put("IMEINO", IglPreferences.getPrefString(this, SEILIGL.DEVICE_IMEI, "0"));
        params.put("FiCode", eSignBorrower.FiCode);

        DataAsyncResponseHandler asyncResponseHandler = new DataAsyncResponseHandler(this, "Requesting for Disbursement for " + eSignBorrower.FiCode + "(" + eSignBorrower.Creator + ")") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) { //
                    // TODO : Convert proper time for Disbursement Request Time and uncomment Save and change Toast to Snackbar
                    String dateString = (new String(responseBody)).replace("\"", "");
                    Toast.makeText(getBaseContext(), "Request for Loan Disbursement submitted successfully\n" + dateString, Toast.LENGTH_LONG).show();
                    //Utils.showSnakbar(findViewById(android.R.id.content),R.string.loan_detail_);

                    //Log.d("Request Disbu Time", dateString);
                    eSignBorrower.DisbRequested = DateUtils.getParsedDate(dateString, DateUtils.formatdateTimeStrings[0]);
                    eSignBorrower.save();
                    view.setVisibility(View.GONE);
                }
            }

            /*@Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String errorString="Failure "+String.valueOf(statusCode)+"\n"+error.getMessage();
                Log.d("failure",errorString);
                Utils.showSnakbar(findViewById(android.R.id.content),errorString);
                view.setClickable(true);
            }*/
        };

        (new WebOperations()).getEntity(this, "POSDB", "RequestDisbursement", params, asyncResponseHandler);

    }

    private void downloadUnsignedDoc(View v) {
        final View view = v;
        FileAsyncResponseHandler fileAsyncResponseHandler = new FileAsyncResponseHandler(this, "Loan Financing", "Downloading Document for " + eSignBorrower.FiCode + "(" + eSignBorrower.Creator + ")") {

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
                    //Log.d("file path",file.getPath());
                    eSignBorrower.documentPath = file.getPath();
//                    clientDataDao.updateESignBorrowerOnId(file.getPath(),eSignBorrower.FiCode);

                     eSignBorrower.save();
                    updateEsignerList();
                }
            }
        };

        UnsignedDocRequest unsignedDocRequest = new UnsignedDocRequest();
        unsignedDocRequest.FiCreator = eSignBorrower.Creator;
        unsignedDocRequest.FiCode = eSignBorrower.FiCode;
        unsignedDocRequest.DocName = BuildConfig.DOC_NAME + (IglPreferences.getPrefString(this, SEILIGL.IS_ACTUAL, "").equals("Y") ? "_sample" : "");
        unsignedDocRequest.UserID = IglPreferences.getPrefString(this, SEILIGL.USER_ID, "");
        Log.d("TAG", "downloadUnsignedDoc: "+WebOperations.convertToJson(unsignedDocRequest));
        ( new WebOperations()).postEntityESign(this, "DocsESignPvn", "downloadunsigneddoc", WebOperations.convertToJson(unsignedDocRequest), fileAsyncResponseHandler);
    }

    private void updateEsignerList() {
        eSignBorrower = SQLite.select().from(ESignBorrower.class)
                .where(ESignBorrower_Table.id.eq(eSignBorrower.id))
                .querySingle();
//        eSignBorrower=clientDataDao.getESignBorrower(eSignBorrower.id);
        AdapterListESigner adapterListESigner = (AdapterListESigner) lvLoanDetails.getAdapter();
        adapterListESigner.clear();
//        adapterListESigner.addAll(eSignBorrower.getESigners(clientDataDao));
        adapterListESigner.addAll(eSignBorrower.getESigners());
        adapterListESigner.notifyDataSetChanged();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        eSigner = (ESigner) parent.getAdapter().getItem(position);
        Log.d("TAG", "onItemClick: "+eSigner.docPath+"////"+eSigner.GrNo);
        if (eSigner.docPath == null && eSigner.GrNo == 0) {
            Utils.alert(this, "Please download the Document first");
        } else if (eSigner.GrNo > 0 && !eSignBorrower.ESignSucceed.equals("Y")) {
            Utils.alert(this, "Please eSign for Borrower First");
        } else {
            /*if (eSigner.KycUuid == null || eSigner.KycUuid.trim().length() =

            = 0) {
                Utils.showSnakbar(findViewById(android.R.id.content), R.string.msg_perform_ekyc);

            } else {*/

            if (!eSigner.ESignSucceed.equals("Y")) {
                Intent intent = new Intent(this, ActivityESignWithDocumentPL.class);
                intent.putExtra(Global.ESIGNER_TAG, eSigner);
                startActivityForResult(intent, Global.ESIGN_REQUEST_CODE);
            }

            //}
        }
    }

    @Override
    public void onClick(View view) {

    }
}