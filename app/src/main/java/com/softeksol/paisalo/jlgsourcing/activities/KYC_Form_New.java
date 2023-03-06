package com.softeksol.paisalo.jlgsourcing.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.IglPreferences;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.adapters.AdapterListRange;
import com.softeksol.paisalo.jlgsourcing.entities.Borrower;
import com.softeksol.paisalo.jlgsourcing.entities.Manager;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory_Table;

public class KYC_Form_New extends AppCompatActivity {
TextInputEditText tietAgricultureIncome,tietFutureIncome,tietExpenseMonthly,tietIncomeMonthly,tietOtherIncome,EditEarningMemberIncome;
Spinner loanbanktype,loanDuration,acspOccupation,acspLoanReason,acspBusinessDetail,earningMemberTypeSpin,acspLoanAppFinanceLoanAmount;
    private Manager manager;
Button BtnSaveKYCData;
Borrower borrower;
private AdapterListRange rlaBankType, rlaPurposeType, rlaLoanAmount, rlaEarningMember, rlaSchemeType ,rlsOccupation;
Intent i;
String FatherFName;
String FatherLName;
String FatherMName;
String MotherFName;
String MotherLName;
String MotherMName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        i=getIntent();
        FatherFName=i.getStringExtra("FatherFName");
        FatherLName=i.getStringExtra("FatherLName");
        FatherMName=i.getStringExtra("FatherMName");
        MotherFName=i.getStringExtra("MotherFName");
        MotherLName=i.getStringExtra("MotherLName");
        MotherMName=i.getStringExtra("MotherMName");
        setContentView(R.layout.activity_kyc_form_new);
        manager = (Manager) i.getSerializableExtra("manager");
        tietAgricultureIncome=findViewById(R.id.tietAgricultureIncome);
        tietFutureIncome=findViewById(R.id.tietFutureIncome);
        tietExpenseMonthly=findViewById(R.id.tietExpenseMonthly);
        tietIncomeMonthly=findViewById(R.id.tietIncomeMonthly);
        tietOtherIncome=findViewById(R.id.tietOtherIncome);
        EditEarningMemberIncome=findViewById(R.id.EditEarningMemberIncome);
        loanbanktype=findViewById(R.id.loanbanktype);
        loanDuration=findViewById(R.id.loanDuration);
        acspOccupation=findViewById(R.id.acspOccupation);
        acspLoanReason=findViewById(R.id.acspLoanReason);
        acspBusinessDetail=findViewById(R.id.acspBusinessDetail);
        earningMemberTypeSpin=findViewById(R.id.earningMemberTypeSpin);
        acspLoanAppFinanceLoanAmount=findViewById(R.id.acspLoanAppFinanceLoanAmount);
        BtnSaveKYCData=findViewById(R.id.BtnSaveKYCData);
        Log.d("TAG", "onCreate: "+manager.AreaName);
        Log.d("TAG", "onCreate: "+FatherFName);
        Log.d("TAG", "onCreate: "+FatherLName);
        Log.d("TAG", "onCreate: "+FatherMName);
        Log.d("TAG", "onCreate: "+MotherFName);
        Log.d("TAG", "onCreate: "+MotherLName);
        Log.d("TAG", "onCreate: "+MotherMName);
//        borrower = new Borrower(manager.Creator, manager.TAG, manager.FOCode, manager.AreaCd, IglPreferences.getPrefString(KYC_Form_New.this, SEILIGL.USER_ID, ""));

        rlaSchemeType = new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("DISBSCH")).queryList(), false);

        rlaPurposeType = new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("loan_purpose"))
                        .orderBy(RangeCategory_Table.SortOrder, true).queryList(), true);

        rlaBankType = new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("banks")).queryList(), false);

        rlaLoanAmount= new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("loan_amt")).queryList(), false);

        rlsOccupation= new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("occupation-type")).queryList(), false);

        rlaEarningMember = new AdapterListRange(this,
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("other_income")).queryList(), false);


        loanbanktype.setAdapter(rlaBankType);
        acspLoanReason.setAdapter(rlaPurposeType);
        acspLoanAppFinanceLoanAmount.setAdapter(rlaLoanAmount);
        acspBusinessDetail.setAdapter(rlsOccupation);
        acspOccupation.setAdapter(rlsOccupation);
        earningMemberTypeSpin.setAdapter(rlaEarningMember);


        BtnSaveKYCData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataFromView();
            }
        });


    }

    private void getDataFromView() {

        borrower.fiExtra.AGRICULTURAL_INCOME = Utils.getNotNullText(tietAgricultureIncome);
        borrower.fiExtra.FutureIncome=Utils.getNotNullInt(tietFutureIncome);
        borrower.fiExtra.ANNUAL_INCOME= String.valueOf(Integer.parseInt(Utils.getNotNullText(tietIncomeMonthly))*12);
        borrower.Expense=Integer.parseInt(Utils.getNotNullText(tietIncomeMonthly))*12;
        borrower.fiExtra.OTHER_THAN_AGRICULTURAL_INCOME=Utils.getNotNullText(tietOtherIncome);
        borrower.fiExtra.FamMonthlyIncome=Utils.getNotNullInt(EditEarningMemberIncome);
        borrower.fiExtra.FamIncomeSource=Utils.getSpinnerStringValue(earningMemberTypeSpin);
        borrower.BankName=Utils.getSpinnerStringValue(loanbanktype);
        borrower.fiExtra.OCCUPATION_TYPE=Utils.getSpinnerStringValue(acspOccupation);
        borrower.Loan_Duration=Utils.getSpinnerStringValue(loanDuration);
        borrower.Loan_Reason=Utils.getSpinnerStringValue(acspLoanReason);
        borrower.Business_Detail=Utils.getSpinnerStringValue(acspBusinessDetail);
        borrower.Loan_Amt=Utils.getSpinnerIntegerValue(acspLoanAppFinanceLoanAmount);

    }
}