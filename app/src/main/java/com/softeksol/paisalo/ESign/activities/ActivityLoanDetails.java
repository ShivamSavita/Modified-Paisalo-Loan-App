package com.softeksol.paisalo.ESign.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softeksol.paisalo.ESign.adapters.AdapterListESigner;
import com.softeksol.paisalo.jlgsourcing.BuildConfig;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.entities.ESignBorrower;
import com.softeksol.paisalo.jlgsourcing.entities.ESigner;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.entities.Manager_Table;

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
//                downloadUnsignedDoc(view);
            }
        });
        Button btnRequestDisb = (Button) findViewById(R.id.btnLoanDetailsRequestDisbursement);
        btnRequestDisb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);
//                RequestDisbursement(view);
            }
        });

        lvLoanDetails = (ListView) findViewById(R.id.lvLoanDetails);
        btnDownloadDoc.setVisibility(eSignBorrower.hasSomeSigned() ? View.VISIBLE : View.GONE);
        btnRequestDisb.setVisibility((eSignBorrower.hasAllESigned() && (eSignBorrower.DisbRequested == null)) ? View.VISIBLE : View.GONE);
        lvLoanDetails.setAdapter(new AdapterListESigner(this, eSignBorrower.getESigners()));
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        eSigner = (ESigner) parent.getAdapter().getItem(position);
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
//                Intent intent = new Intent(this, ActivityESignWithDocumentPL.class);
//                intent.putExtra(Global.ESIGNER_TAG, eSigner);
//                startActivityForResult(intent, Global.ESIGN_REQUEST_CODE);
            }

            //}
        }
    }

    @Override
    public void onClick(View view) {

    }
}