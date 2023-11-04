package com.softeksol.paisalo.jlgsourcing.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.activities.ActivityLoanApplication;
import com.softeksol.paisalo.jlgsourcing.adapters.AdapterListRange;
import com.softeksol.paisalo.jlgsourcing.entities.Borrower;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerExtra;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory_Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBorrowerPendingVhData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBorrowerPendingVhData extends AbsFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ActivityLoanApplication activity;
    private BorrowerExtra borrowerExtra;
    private Borrower borrower;
    private OnFragmentBorrowerPendingVHDataInteractionListener mListener;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private long borrowerId;
    private AdapterListRange AGRICULTURAL_INCOME, SOC_ATTR_2_INCOME, ANNUAL_INCOME, OTHER_THAN_AGRICULTURAL_INCOME, MARITAL_STATUS, OCCUPATION_TYPE, RESERVATN_CATEGORY;
    private ArrayAdapter<String> SOC_ATTR_4_SPL_ABLD,SOC_ATTR_5_SPL_SOC_CTG,VISUALLY_IMPAIRED_YN,FORM60_PAN_APPLIED_YN;
    ArrayList<String> items=new ArrayList<String>();
    TextInputEditText tietAgricultureIncome,tietFutureIncome,tietExpenseMonthly,tietIncomeMonthly,tietOtherIncome,EditEarningMemberIncome,tietPensionIncome,tietInterestIncome;

    private ImageView imgViewCal,ImgViewCal2;


    EditText editFORM60_TNX_DT,editFORM60_SUBMISSIONDATE;
    private AdapterListRange rlaEarningMember, rlaEarningMemberOccupation, rlaEarningMemberIncome, rlaEMployerType, rlaShopOwner, rla0to9;

    String newDate;

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    @Override
    public void onClick(View view) {

    }


//

    public interface OnFragmentBorrowerPendingVHDataInteractionListener {
        void onBorrowerPendingVHDataInteraction(Borrower borrower);
    }
    public FragmentBorrowerPendingVhData() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBorrowerPersonal_Additional.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBorrowerPendingVhData newInstance(String param1, String param2) {
        FragmentBorrowerPendingVhData fragment = new FragmentBorrowerPendingVhData();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AbsFragment newInstance(long borrower_id) {
        FragmentBorrowerPendingVhData fragment = new FragmentBorrowerPendingVhData();
        Bundle args = new Bundle();
        args.putLong(Global.BORROWER_TAG, borrower_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            borrowerId = getArguments().getLong(Global.BORROWER_TAG, 0);
        }

        rlaEarningMember = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("other_income")).queryList(), false);
        rlaEarningMemberOccupation = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("other_employment")).queryList(), false);
        rlaEarningMemberIncome = new AdapterListRange(this.getContext(), Utils.getList(1, 11, 1, 1000, getString(R.string.rupees)), true);
        rlaEMployerType = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("employer_type")).queryList(), false);



        activity = (ActivityLoanApplication) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_vhpendingdata, container, false);

        Spinner spinMARITAL_STATUS=v.findViewById(R.id.spinLoanAppPersonalMarritalStatus);
        MARITAL_STATUS = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("marrital_status")).queryList(), false);
        spinMARITAL_STATUS.setAdapter(MARITAL_STATUS);

        Spinner spinOCCUPATION_TYPE=v.findViewById(R.id.acspOccupation);
        OCCUPATION_TYPE = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("other_employment")).queryList(), false);
        spinOCCUPATION_TYPE.setAdapter(OCCUPATION_TYPE);


        Spinner earningMemberTypeSpin=v.findViewById(R.id.earningMemberTypeSpin);
        earningMemberTypeSpin.setAdapter(rlaEarningMember);
        Spinner acspBusinessDetail=v.findViewById(R.id.acspBusinessDetail);
        acspBusinessDetail.setAdapter(rlaEarningMemberOccupation);

               EditText MOTHER_FIRST_NAME= v.findViewById(R.id.tietMotherFName);
               EditText MOTHER_MIDDLE_NAME= v.findViewById(R.id.tietmotherMname);
               EditText MOTHER_LAST_NAME= v.findViewById(R.id.tietMotherLName);
               EditText SPOUSE_FIRST_NAME= v.findViewById(R.id.tietSpouseFName);
               EditText SPOUSE_MIDDLE_NAME= v.findViewById(R.id.tietSpouseMName);
               EditText SPOUSE_LAST_NAME= v.findViewById(R.id.tietSpouseLName);
               EditText FATHER_FIRST_NAME= v.findViewById(R.id.tietFatherFName);
               EditText FATHER_MIDDLE_NAME= v.findViewById(R.id.tietFatherMName);
               EditText FATHER_LAST_NAME= v.findViewById(R.id.tietFatherLName);
               tietAgricultureIncome=v.findViewById(R.id.tietAgricultureIncome);
               tietFutureIncome=v.findViewById(R.id.tietFutureIncome);
               tietExpenseMonthly=v.findViewById(R.id.tietExpenseMonthly);
               tietIncomeMonthly=v.findViewById(R.id.tietIncomeMonthly);
               tietOtherIncome=v.findViewById(R.id.tietOtherIncome);
               tietInterestIncome=v.findViewById(R.id.tietInterestIncome);
               tietPensionIncome=v.findViewById(R.id.tietPensionIncome);
               EditEarningMemberIncome=v.findViewById(R.id.EditEarningMemberIncome);


        return  v;
    }

    public void openDatePickerDialog(final View v) {
        // Get Current Date
        Calendar cal=Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    String selectedDate =year  + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        Date date = dt.parse(selectedDate);
                        newDate= dt.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    switch (v.getId()) {
                        case R.id.imgViewCal:
                            editFORM60_TNX_DT.setText(newDate);
                            break;
                        case R.id.imgViewCal2:
                            editFORM60_SUBMISSIONDATE.setText(newDate);
                            break;
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        datePickerDialog.show();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentBorrowerPendingVHDataInteractionListener) {
            mListener = (OnFragmentBorrowerPendingVHDataInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Log.d("Detaching", "");
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        borrower = activity.getBorrower();
        borrowerExtra = borrower.getBorrowerExtraByFI(borrower.Code);
        Log.d("TAG", "onResume: "+borrowerExtra);
        if (borrowerExtra == null) {
            borrowerExtra = new BorrowerExtra();
            activity.getBorrower().associateExtra(borrowerExtra);
            borrowerExtra.save();
        }
        setDataToView(getView());
    }


    private void setDataToView(View v) {

        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppPersonalMarritalStatus),borrowerExtra.MARITAL_STATUS);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.acspOccupation),borrowerExtra.OCCUPATION_TYPE);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.acspBusinessDetail),borrower.Business_Detail);



        ((EditText) v.findViewById(R.id.tietMotherFName)).setText(borrowerExtra.MOTHER_FIRST_NAME);
        ((EditText) v.findViewById(R.id.tietmotherMname)).setText(borrowerExtra.MOTHER_MIDDLE_NAME);
        ((EditText) v.findViewById(R.id.tietMotherLName)).setText(borrowerExtra.MOTHER_LAST_NAME);
       // ((EditText) v.findViewById(R.id.editMOTHER_MAIDEN_NAME)).setText(borrowerExtra.MOTHER_MAIDEN_NAME);
       // ((EditText) v.findViewById(R.id.editSPOUSE_TITLE)).setText(borrowerExtra.SPOUSE_TITLE);
        ((EditText) v.findViewById(R.id.tietSpouseFName)).setText(borrowerExtra.SPOUSE_FIRST_NAME);
        ((EditText) v.findViewById(R.id.tietSpouseMName)).setText(borrowerExtra.SPOUSE_MIDDLE_NAME);
        ((EditText) v.findViewById(R.id.tietSpouseLName)).setText(borrowerExtra.SPOUSE_LAST_NAME);
       // ((EditText) v.findViewById(R.id.editAPPLICNT_TITLE)).setText(borrowerExtra.APPLICNT_TITLE);
        //((EditText) v.findViewById(R.id.editFATHER_TITLE)).setText(borrowerExtra.FATHER_TITLE);
        ((EditText) v.findViewById(R.id.tietFatherFName)).setText(borrowerExtra.FATHER_FIRST_NAME);
        ((EditText) v.findViewById(R.id.tietFatherMName)).setText(borrowerExtra.FATHER_MIDDLE_NAME);
        ((EditText) v.findViewById(R.id.tietFatherLName)).setText(borrowerExtra.FATHER_LAST_NAME);
        ((TextInputEditText) v.findViewById(R.id.tietIncomeMonthly)).setText(String.valueOf(borrower.Income));
        ((TextInputEditText) v.findViewById(R.id.tietExpenseMonthly)).setText(String.valueOf(borrower.Expense));
        ((TextInputEditText) v.findViewById(R.id.tietFutureIncome)).setText(String.valueOf(borrowerExtra.FutureIncome));
        ((TextInputEditText) v.findViewById(R.id.tietAgricultureIncome)).setText(String.valueOf(borrowerExtra.AGRICULTURAL_INCOME));
        ((TextInputEditText) v.findViewById(R.id.tietPensionIncome)).setText(String.valueOf(borrowerExtra.PensionIncome));
        ((TextInputEditText) v.findViewById(R.id.tietOtherIncome)).setText(String.valueOf(borrowerExtra.OTHER_THAN_AGRICULTURAL_INCOME));
        ((TextInputEditText) v.findViewById(R.id.EditEarningMemberIncome)).setText(String.valueOf(borrowerExtra.FamMonthlyIncome));
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.earningMemberTypeSpin),borrowerExtra.FamIncomeSource);

    }

    @Override
    public void onPause() {
        getDataFromView(getView());
        super.onPause();
    }

    private void getDataFromView(View view) {
        borrower.Expense=Utils.getNotNullInt((TextInputEditText) view.findViewById(R.id.tietExpenseMonthly));
        borrower.Income=Utils.getNotNullInt((TextInputEditText) view.findViewById(R.id.tietIncomeMonthly));
        borrowerExtra.AGRICULTURAL_INCOME=Utils.getNotNullText((TextInputEditText) view.findViewById(R.id.tietAgricultureIncome));;
       // borrowerExtra.AGRICULTURAL_INCOME=Utils.getSpinnerStringValue((Spinner) view.findViewById(R.id.spinAgriIncome));
        borrowerExtra.ANNUAL_INCOME=String.valueOf(Utils.getNotNullInt((TextInputEditText) view.findViewById(R.id.tietIncomeMonthly))*12);
        borrowerExtra.MARITAL_STATUS=Utils.getSpinnerStringValue((Spinner) view.findViewById(R.id.spinLoanAppPersonalMarritalStatus));
        borrowerExtra.OCCUPATION_TYPE=Utils.getSpinnerStringValue((Spinner) view.findViewById(R.id.acspOccupation));
        borrower.Business_Detail=Utils.getSpinnerStringValue((Spinner) view.findViewById(R.id.acspBusinessDetail));
        borrowerExtra.FamIncomeSource=Utils.getSpinnerStringValue((Spinner) view.findViewById(R.id.earningMemberTypeSpin));
        borrowerExtra.FamMonthlyIncome=Utils.getNotNullInt((TextInputEditText) view.findViewById(R.id.EditEarningMemberIncome));
        borrowerExtra.FutureIncome=Utils.getNotNullInt((TextInputEditText) view.findViewById(R.id.tietFutureIncome));
        borrowerExtra.PensionIncome=Utils.getNotNullInt((TextInputEditText) view.findViewById(R.id.tietPensionIncome));
        borrowerExtra.OTHER_THAN_AGRICULTURAL_INCOME=Utils.getNotNullText((TextInputEditText) view.findViewById(R.id.tietOtherIncome));
        borrowerExtra.InterestIncome=Utils.getNotNullInt((TextInputEditText) view.findViewById(R.id.tietInterestIncome));

        //borrowerExtra.MOTHER_TITLE=((EditText) view.findViewById(R.id.editMOTHER_TITLE)).getText().toString();
        borrowerExtra.MOTHER_FIRST_NAME=Utils.getNotNullText((TextInputEditText) view.findViewById(R.id.tietMotherFName));
        borrowerExtra.MOTHER_MIDDLE_NAME=Utils.getNotNullText((TextInputEditText) view.findViewById(R.id.tietmotherMname));
        borrowerExtra.MOTHER_LAST_NAME=Utils.getNotNullText((TextInputEditText) view.findViewById(R.id.tietMotherLName));
//        borrowerExtra.MOTHER_MAIDEN_NAME=((EditText) view.findViewById(R.id.editMOTHER_MAIDEN_NAME)).getText().toString();
//        borrowerExtra.SPOUSE_TITLE=((EditText) view.findViewById(R.id.editSPOUSE_TITLE)).getText().toString();
        borrowerExtra.SPOUSE_FIRST_NAME=Utils.getNotNullText((TextInputEditText) view.findViewById(R.id.tietSpouseFName));
        borrowerExtra.SPOUSE_MIDDLE_NAME=Utils.getNotNullText((TextInputEditText) view.findViewById(R.id.tietSpouseMName));
        borrowerExtra.SPOUSE_LAST_NAME=Utils.getNotNullText((TextInputEditText) view.findViewById(R.id.tietSpouseLName));
//        borrowerExtra.APPLICNT_TITLE=((EditText) view.findViewById(R.id.editAPPLICNT_TITLE)).getText().toString();
//        borrowerExtra.FATHER_TITLE=((EditText) view.findViewById(R.id.editFATHER_TITLE)).getText().toString();
        borrowerExtra.FATHER_FIRST_NAME=Utils.getNotNullText((TextInputEditText) view.findViewById(R.id.tietFatherFName));
        borrowerExtra.FATHER_MIDDLE_NAME=Utils.getNotNullText((TextInputEditText) view.findViewById(R.id.tietFatherMName));
        borrowerExtra.FATHER_LAST_NAME=Utils.getNotNullText((TextInputEditText) view.findViewById(R.id.tietFatherLName));
       Log.d("TAG", "getDataFromView: yha tk chal rha h 1");
        borrowerExtra.save();
        borrower.save();

    }

    @Override
    public String getName() {
        return "Personal Data 2";
    }
    public interface OnFragmentBorrowerPersonal_AddInteractionListener{
        void onFragmentBorrowerPersonalInteraction(Borrower borrower);
    }
}