package com.softeksol.paisalo.jlgsourcing.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softeksol.paisalo.jlgsourcing.BuildConfig;
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
 * Activities that contain this fragment must implement the
 * {@link FragmentBorrowerPersonal.OnFragmentBorrowerPersonalInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentBorrowerPersonal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBorrowerPersonal extends AbsFragment implements AdapterView.OnItemSelectedListener {
    View v;

    private long borrowerId;
    private OnFragmentBorrowerPersonalInteractionListener mListener;
    private AdapterListRange rlaIncome, rlaCaste, rlaReligion, rlaMarritalStatus, rla1to9, rla1kto11k, rlaOwner;
    private ActivityLoanApplication activity;
    private Borrower borrower;
    private AdapterListRange rlaEarningMember, rlaEarningMemberOccupation, rlaEarningMemberIncome, rlaEMployerType, rlaShopOwner, rla0to9;
    private AdapterListRange rlaOtherAgriLandOwnership, rlaOtherBusinessType, rla0to11, rlaOtherAgriLandType, rlaPurposeType, rlaSchoolingChildren,rlaLoanAppExtraDependentAdults,rlaChildren;
    private BorrowerExtra borrowerExtra;
    ArrayList<String> items=new ArrayList<String>();
    ArrayList<String> itemsForEducationalCode=new ArrayList<String>();
    private ArrayAdapter<String> SOC_ATTR_4_SPL_ABLD,SOC_ATTR_5_SPL_SOC_CTG,VISUALLY_IMPAIRED_YN,borrowerHandicap,FORM60_PAN_APPLIED_YN,spinEducationalCodeAdapter;
    private ImageView imgViewCal,ImgViewCal2;
    String newDate;

    EditText editFORM60_TNX_DT,editFORM60_SUBMISSIONDATE;
    public FragmentBorrowerPersonal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param borrowerId Parameter.
     * @return A new instance of fragment FragmentBorrowerAadhar.
     */
    public static FragmentBorrowerPersonal newInstance(long borrowerId) {
        FragmentBorrowerPersonal fragment = new FragmentBorrowerPersonal();
        Bundle args = new Bundle();
        args.putLong(Global.BORROWER_TAG, borrowerId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            borrowerId = getArguments().getLong(Global.BORROWER_TAG, 0);
        }
        activity = (ActivityLoanApplication) getActivity();
        items.add("NO");
        items.add("YES");

        itemsForEducationalCode.add("10th");
        itemsForEducationalCode.add("12th");
        itemsForEducationalCode.add("GRADUATE");
        itemsForEducationalCode.add("POST GRADUATE");

        rlaCaste = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("caste")).queryList(), false);
        rlaReligion = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("religion")).queryList(), false);
        rlaOwner = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("land_owner")).queryList(), false);
        rlaMarritalStatus = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("marrital_status")).queryList(), false);

        //added 30 in end counter for making monthly income upto 30k for only e-vehicle
        rlaIncome = new AdapterListRange(this.getContext(), Utils.getList(1, 30, 1, 1000, "Rupees"), true);

        rla1to9 = new AdapterListRange(this.getContext(), Utils.getList(1, 9, 1, 1, null), false);

        rlaEarningMember = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("other_income")).queryList(), false);
        rlaEarningMemberOccupation = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("other_employment")).queryList(), false);
        rlaEarningMemberIncome = new AdapterListRange(this.getContext(), Utils.getList(1, 11, 1, 1000, getString(R.string.rupees)), true);
        rlaEMployerType = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("employer_type")).queryList(), false);

        rlaShopOwner = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("land_owner")).queryList(), false);
        rlaOtherBusinessType = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("loan_purpose")).queryList(), false);
        rlaOtherAgriLandOwnership = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("agri_land_ownership")).queryList(), false);
        rlaOtherAgriLandType = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("agri_land_type")).queryList(), false);

        rlaPurposeType = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("loan_purpose"))
                        .orderBy(RangeCategory_Table.SortOrder, true).queryList(), true);

        rla0to11 = new AdapterListRange(this.getContext(), Utils.getList(0, 11, 1, 1, getString(R.string.acres)), true);
        rla0to9 = new AdapterListRange(this.getContext(), Utils.getList(0, 9, 1, 1, null), false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_borrower_personal, container, false);
        imgViewCal=v.findViewById(R.id.imgViewCal);
        ImgViewCal2=v.findViewById(R.id.imgViewCal2);
        editFORM60_SUBMISSIONDATE=v.findViewById(R.id.editFORM60_SUBMISSIONDATE);
        editFORM60_TNX_DT=v.findViewById(R.id.editFORM60_TNX_DT);
        editFORM60_SUBMISSIONDATE.setEnabled(false);
        editFORM60_TNX_DT.setEnabled(false);
        ImageView imgViewLeft = (ImageView) v.findViewById(R.id.btnNavLeft);
        activity.setNavOnClikListner(imgViewLeft);
        ImageView imgViewRight = (ImageView) v.findViewById(R.id.btnNavRight);
        activity.setNavOnClikListner(imgViewRight);

        Spinner spinnerIncomeMonthly = (Spinner) v.findViewById(R.id.spinLoanAppPersonalIncomeMonthly);
        spinnerIncomeMonthly.setAdapter(rlaIncome);
        Spinner spinnerCaste = (Spinner) v.findViewById(R.id.spinLoanAppPersonalCaste);
        spinnerCaste.setAdapter(rlaCaste);
        Spinner spinnerReligion = (Spinner) v.findViewById(R.id.spinLoanAppPersonalReligion);
        spinnerReligion.setAdapter(rlaReligion);
        Spinner spinnerHouseOwner = (Spinner) v.findViewById(R.id.spinLoanAppPersonalPresentResidenceOwner);
        spinnerHouseOwner.setAdapter(rlaOwner);
        spinnerHouseOwner.setOnItemSelectedListener(this);
        Spinner spinnerHouseRent = (Spinner) v.findViewById(R.id.spinLoanAppPersonalPresentHouseRent);
        spinnerHouseRent.setAdapter(rlaIncome);
        Spinner spinnerHouseResidingFor = (Spinner) v.findViewById(R.id.spinLoanAppPersonalPresentResidenceDuration);
        spinnerHouseResidingFor.setAdapter(rla1to9);

        Spinner spinSOC_ATTR_4_SPL_ABLD=v.findViewById(R.id.spinSpecialAbility);
        SOC_ATTR_4_SPL_ABLD = new ArrayAdapter<String>(this.getContext(),R.layout.spinner_card_orange,R.id.text_cname, items);
        spinSOC_ATTR_4_SPL_ABLD.setAdapter(SOC_ATTR_4_SPL_ABLD);
        Spinner spinSOC_ATTR_5_SPL_SOC_CTG=v.findViewById(R.id.spinSpecialSocialCategory);
        SOC_ATTR_5_SPL_SOC_CTG = new ArrayAdapter<String>(this.getContext(),R.layout.spinner_card_orange,R.id.text_cname, items);
        spinSOC_ATTR_5_SPL_SOC_CTG.setAdapter(SOC_ATTR_5_SPL_SOC_CTG);
        Spinner spinVISUALLY_IMPAIRED_YN=v.findViewById(R.id.spinVisuallyImpaired);
        VISUALLY_IMPAIRED_YN = new ArrayAdapter<String>(this.getContext(),R.layout.spinner_card_orange,R.id.text_cname, items);
        spinVISUALLY_IMPAIRED_YN.setAdapter(VISUALLY_IMPAIRED_YN);
        Spinner spinBorrowerHandicap=v.findViewById(R.id.spinBorrowerHandicap);
        borrowerHandicap = new ArrayAdapter<String>(this.getContext(),R.layout.spinner_card_orange,R.id.text_cname, items);
        spinBorrowerHandicap.setAdapter(borrowerHandicap);
        Spinner spinFORM60_PAN_APPLIED_YN=v.findViewById(R.id.spinFORM60_PAN_APPLIED_YN);
        FORM60_PAN_APPLIED_YN = new ArrayAdapter<String>(this.getContext(),R.layout.spinner_card_orange,R.id.text_cname, items);
        spinFORM60_PAN_APPLIED_YN.setAdapter(FORM60_PAN_APPLIED_YN);



        Spinner spinEducationalCode=v.findViewById(R.id.spinEducationalCode);

        spinEducationalCodeAdapter = new ArrayAdapter<String>(this.getContext(),R.layout.spinner_card_orange,R.id.text_cname, itemsForEducationalCode);
        spinEducationalCode.setAdapter(spinEducationalCodeAdapter);


        Spinner spinnerIncomeFuture = (Spinner) v.findViewById(R.id.spinLoanAppExtraIncomeFuture);
        spinnerIncomeFuture.setAdapter(rlaIncome);
        Spinner spinnerFamilyMembers = (Spinner) v.findViewById(R.id.spinLoanAppPersonalFamilyMembers);
        spinnerFamilyMembers.setAdapter(rla0to9);
        spinnerFamilyMembers.setOnItemSelectedListener(this);
        Spinner spinnerDependentAdults = (Spinner) v.findViewById(R.id.spinLoanAppExtraDependentAdults);
        spinnerDependentAdults.setAdapter(rla0to9);
        Spinner spinnerChildren = (Spinner) v.findViewById(R.id.spinLoanAppExtraChildren);
        spinnerChildren.setAdapter(rla0to9);
        spinnerChildren.setOnItemSelectedListener(this);

        Spinner spinnerChildrenSpend = (Spinner) v.findViewById(R.id.spinLoanAppExtraChildrenSpending);
        spinnerChildrenSpend.setAdapter(rlaIncome);
        Spinner spinnerOtherEarningMember = (Spinner) v.findViewById(R.id.spinLoanAppExtraEarningMember);
        spinnerOtherEarningMember.setAdapter(rlaEarningMember);
        Spinner spinnerOtherEarningMemberIncome = (Spinner) v.findViewById(R.id.spinLoanAppExtraIncome);
        spinnerOtherEarningMemberIncome.setAdapter(rlaIncome);

        Spinner spinnerOtherEarningMemberOccupation = (Spinner) v.findViewById(R.id.spinLoanAppExtraOccupationType);
        spinnerOtherEarningMemberOccupation.setAdapter(rlaEarningMemberOccupation);
        spinnerOtherEarningMemberOccupation.setOnItemSelectedListener(this);

        ((Spinner) v.findViewById(R.id.spinLoanAppExtraEmployerType)).setAdapter(rlaEMployerType);
        ((Spinner) v.findViewById(R.id.spinLoanAppExtraShopOwner)).setAdapter(rlaShopOwner);
        ((Spinner) v.findViewById(R.id.spinLoanAppExtraBusinessType)).setAdapter(rlaOtherBusinessType);

        ((Spinner) v.findViewById(R.id.spinLoanAppExtraAgricultureLandOwner)).setAdapter(rlaOtherAgriLandOwnership);
        ((Spinner) v.findViewById(R.id.spinLoanAppExtraAgricultureLandType)).setAdapter(rlaOtherAgriLandType);
        ((Spinner) v.findViewById(R.id.spinLoanAppExtraAgricultureArea)).setAdapter(rla0to11);


        imgViewCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(v);
            }
        });

        ImgViewCal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(v);
                //showDate(year, month+1, day);
            }
        });
        return v;
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
        if (context instanceof OnFragmentBorrowerPersonalInteractionListener) {
            mListener = (OnFragmentBorrowerPersonalInteractionListener) context;
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

        if (borrowerExtra == null) {
            borrowerExtra = new BorrowerExtra();
            activity.getBorrower().associateExtra(borrowerExtra);
            borrowerExtra.save();
        }
        setDataToView(getView());
    }

    @Override
    public void onPause() {
        getDataFromView(getView());
        super.onPause();
    }

    private void setDataToView(View v) {
        int spinnerPositionVisuallyImpaired= VISUALLY_IMPAIRED_YN.getPosition(borrowerExtra.VISUALLY_IMPAIRED_YN);
        ((Spinner)v.findViewById(R.id.spinVisuallyImpaired)).setSelection(spinnerPositionVisuallyImpaired);
        int spinnerPositionBorrowerHandicap= borrowerHandicap.getPosition(borrowerExtra.IsBorrowerHandicap);
        ((Spinner)v.findViewById(R.id.spinBorrowerHandicap)).setSelection(spinnerPositionBorrowerHandicap);
        int spinnerPositionSpecialCategory= SOC_ATTR_5_SPL_SOC_CTG.getPosition(borrowerExtra.SOC_ATTR_5_SPL_SOC_CTG);
        ((Spinner)v.findViewById(R.id.spinSpecialSocialCategory)).setSelection(spinnerPositionSpecialCategory);
        if (borrower.PanNO.length()>0){
            ((Spinner)v.findViewById(R.id.spinFORM60_PAN_APPLIED_YN)).setVisibility(View.GONE);
        }
        int spinnerPositionPANApplied= FORM60_PAN_APPLIED_YN.getPosition(borrowerExtra.FORM60_PAN_APPLIED_YN);
        ((Spinner)v.findViewById(R.id.spinFORM60_PAN_APPLIED_YN)).setSelection(spinnerPositionPANApplied);
        int spinnerPositionSpecialAbility= SOC_ATTR_4_SPL_ABLD.getPosition(borrowerExtra.SOC_ATTR_4_SPL_ABLD);
        ((Spinner)v.findViewById(R.id.spinSpecialAbility)).setSelection(spinnerPositionSpecialAbility);
        try{
            int spinnerPositionEducation= spinEducationalCodeAdapter.getPosition(borrowerExtra.EDUCATION_CODE);
            ((Spinner)v.findViewById(R.id.spinEducationalCode)).setSelection(spinnerPositionEducation);
        }catch (Exception e){
            ((Spinner)v.findViewById(R.id.spinEducationalCode)).setSelection(1);
        }
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppPersonalIncomeMonthly), borrower.Income);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppPersonalCaste), borrower.Cast);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppPersonalReligion), borrower.Religion);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppPersonalPresentResidenceOwner), borrower.House_Owner);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppPersonalPresentHouseRent), borrower.Rent_of_House);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppPersonalPresentResidenceDuration), borrower.Live_In_Present_Place);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppPersonalFamilyMembers), borrower.FAmily_member);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraIncomeFuture), borrowerExtra.FutureIncome);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraDependentAdults), borrowerExtra.OtherDependents);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraChildren), borrowerExtra.NoOfChildren);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraChildrenSchooling), borrowerExtra.SchoolingChildren);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraChildrenSpending), borrowerExtra.SpendOnChildren);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraEarningMember), borrowerExtra.FamIncomeSource);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraIncome), borrowerExtra.FamMonthlyIncome);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraOccupationType), borrowerExtra.FamOccupation);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraEmployerType), borrowerExtra.FamJobCompType);
        ((EditText) v.findViewById(R.id.etLoanAppExtraEmployerName)).setText(borrowerExtra.FamJobCompName);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraShopOwner), borrowerExtra.FamBusinessShopType);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraBusinessType), borrowerExtra.FamBusinessType);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraAgricultureLandOwner), borrowerExtra.FamAgriLandOwner);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraAgricultureLandType), borrowerExtra.FamAgriLandType);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraAgricultureArea), borrowerExtra.FamAgriLandArea);
        Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraAgricultureArea), borrowerExtra.FamAgriLandArea);
        //Utils.setSpinnerPosition((Spinner) v.findViewById(R.id.spinLoanAppExtraOtherIncomeType),borrowerExtra.FamOtherIncomeType);
        ((EditText) v.findViewById(R.id.etLoanAppExtraOtherIncomeOther)).setText(borrowerExtra.FamOtherIncomeType);
        ((EditText) v.findViewById(R.id.editMailId)).setText(borrowerExtra.EMAIL_ID);
        ((EditText) v.findViewById(R.id.editPlaceOfBirth)).setText(borrowerExtra.PLACE_OF_BIRTH);
        Log.d("TAG", "setDataToView: "+borrowerExtra.Years_In_Business);
        ((EditText) v.findViewById(R.id.editYearsInBusiness)).setText(String.valueOf(borrowerExtra.Years_In_Business));
        ((EditText) v.findViewById(R.id.editFORM60_TNX_DT)).setText(borrowerExtra.FORM60_TNX_DT);
        ((EditText) v.findViewById(R.id.editFORM60_SUBMISSIONDATE)).setText(borrowerExtra.FORM60_SUBMISSIONDATE);
        ((TextView) v.findViewById(R.id.FatherFirstName)).setText(borrowerExtra.FATHER_FIRST_NAME);
        ((TextView) v.findViewById(R.id.FatherMiddelName)).setText(borrowerExtra.FATHER_MIDDLE_NAME);
        ((TextView) v.findViewById(R.id.FatherLastName)).setText(borrowerExtra.FATHER_LAST_NAME);
        ((TextView) v.findViewById(R.id.MotherFirstName)).setText(borrowerExtra.MOTHER_FIRST_NAME);
        ((TextView) v.findViewById(R.id.MotherMiddelName)).setText(borrowerExtra.MOTHER_MIDDLE_NAME);
        ((TextView) v.findViewById(R.id.MotherLastName)).setText(borrowerExtra.MOTHER_LAST_NAME);
        ((TextView) v.findViewById(R.id.SpouseFirstName)).setText(borrowerExtra.SPOUSE_FIRST_NAME);
        ((TextView) v.findViewById(R.id.SpouseMiddelName)).setText(borrowerExtra.SPOUSE_MIDDLE_NAME);
        ((TextView) v.findViewById(R.id.SpouseLastName)).setText(borrowerExtra.SPOUSE_LAST_NAME);

    }

    private void getDataFromView(View v) {
        borrower.Income = Integer.parseInt(Utils.getSpinnerStringValue((Spinner) v.findViewById(R.id.spinLoanAppPersonalIncomeMonthly)));
            borrower.Cast = Utils.getSpinnerStringValue((Spinner) v.findViewById(R.id.spinLoanAppPersonalCaste));
            borrower.Religion = Utils.getSpinnerStringValue((Spinner) v.findViewById(R.id.spinLoanAppPersonalReligion));
            borrower.House_Owner = Utils.getSpinnerStringValue((Spinner) v.findViewById(R.id.spinLoanAppPersonalPresentResidenceOwner));
        borrower.Live_In_Present_Place = Utils.getSpinnerStringValue((Spinner) v.findViewById(R.id.spinLoanAppPersonalPresentResidenceDuration));
        if (((RangeCategory) ((Spinner) v.findViewById(R.id.spinLoanAppPersonalPresentResidenceOwner)).getSelectedItem()).DescriptionEn.equals("Other")) {
                borrower.Rent_of_House = Utils.getSpinnerIntegerValue((Spinner) v.findViewById(R.id.spinLoanAppPersonalPresentHouseRent));
        } else {
             borrower.Rent_of_House = 0;
        }
        borrower.FAmily_member = Utils.getSpinnerIntegerValue((Spinner) v.findViewById(R.id.spinLoanAppPersonalFamilyMembers));

        borrowerExtra.FutureIncome = Utils.getSpinnerIntegerValue((Spinner) v.findViewById(R.id.spinLoanAppExtraIncomeFuture));
        borrowerExtra.OtherDependents = Utils.getSpinnerIntegerValue((Spinner) v.findViewById(R.id.spinLoanAppExtraDependentAdults));
        borrowerExtra.NoOfChildren = Utils.getSpinnerIntegerValue((Spinner) v.findViewById(R.id.spinLoanAppExtraChildren));

        Spinner childrenSchooling = (Spinner) v.findViewById(R.id.spinLoanAppExtraChildrenSchooling);
        if (childrenSchooling.getVisibility() == View.VISIBLE)
            borrowerExtra.SchoolingChildren = Utils.getSpinnerIntegerValue(childrenSchooling);

        borrowerExtra.SpendOnChildren = Utils.getSpinnerIntegerValue((Spinner) v.findViewById(R.id.spinLoanAppExtraChildrenSpending));

        borrowerExtra.FamIncomeSource = Utils.getSpinnerStringValue((Spinner) v.findViewById(R.id.spinLoanAppExtraEarningMember));
        borrowerExtra.FamMonthlyIncome = Utils.getSpinnerIntegerValue((Spinner) v.findViewById(R.id.spinLoanAppExtraIncome));
        borrowerExtra.FamOccupation = Utils.getSpinnerStringValue((Spinner) v.findViewById(R.id.spinLoanAppExtraOccupationType));

        borrowerExtra.FamJobCompType = Utils.getSpinnerStringValue((Spinner) v.findViewById(R.id.spinLoanAppExtraEmployerType));
        borrowerExtra.FamJobCompName = Utils.getNotNullText((EditText) v.findViewById(R.id.etLoanAppExtraEmployerName));

        borrowerExtra.FamBusinessShopType = Utils.getSpinnerStringValue((Spinner) v.findViewById(R.id.spinLoanAppExtraShopOwner));
        borrowerExtra.FamBusinessType = Utils.getSpinnerStringValue((Spinner) v.findViewById(R.id.spinLoanAppExtraBusinessType));

        borrowerExtra.FamAgriLandOwner = Utils.getSpinnerStringValue((Spinner) v.findViewById(R.id.spinLoanAppExtraAgricultureLandOwner));
        borrowerExtra.FamAgriLandType = Utils.getSpinnerStringValue((Spinner) v.findViewById(R.id.spinLoanAppExtraAgricultureLandType));
        borrowerExtra.FamAgriLandArea = Utils.getSpinnerIntegerValue((Spinner) v.findViewById(R.id.spinLoanAppExtraAgricultureArea));

        borrowerExtra.FamOtherIncomeType = Utils.getNotNullText((EditText) v.findViewById(R.id.etLoanAppExtraOtherIncomeOther));
        borrowerExtra.EMAIL_ID = Utils.getNotNullText((EditText) v.findViewById(R.id.editMailId));
        borrowerExtra.PLACE_OF_BIRTH = Utils.getNotNullText((EditText) v.findViewById(R.id.editPlaceOfBirth));
        borrowerExtra.VISUALLY_IMPAIRED_YN=((Spinner) v.findViewById(R.id.spinVisuallyImpaired)).getSelectedItem().toString();
        try {
            borrowerExtra.IsBorrowerHandicap=((Spinner) v.findViewById(R.id.spinBorrowerHandicap)).getSelectedItem().toString();
        }catch (Exception e){
            borrowerExtra.IsBorrowerHandicap="NO";

        }
        borrowerExtra.SOC_ATTR_5_SPL_SOC_CTG=((Spinner) v.findViewById(R.id.spinSpecialSocialCategory)).getSelectedItem().toString();
        borrowerExtra.SOC_ATTR_4_SPL_ABLD=((Spinner) v.findViewById(R.id.spinSpecialAbility)).getSelectedItem().toString();
                try{
                    borrowerExtra.FORM60_PAN_APPLIED_YN=((Spinner) v.findViewById(R.id.spinFORM60_PAN_APPLIED_YN)).getSelectedItem().toString();
                }catch (Exception e){
                    borrowerExtra.FORM60_PAN_APPLIED_YN="NO";
                }
        borrowerExtra.Years_In_Business=Integer.parseInt(((EditText) v.findViewById(R.id.editYearsInBusiness)).getText().toString());
        borrowerExtra.EDUCATION_CODE=((Spinner) v.findViewById(R.id.spinEducationalCode)).getSelectedItem().toString();
        borrowerExtra.FORM60_TNX_DT=((EditText) v.findViewById(R.id.editFORM60_TNX_DT)).getText().toString();
        borrowerExtra.FORM60_SUBMISSIONDATE=((EditText) v.findViewById(R.id.editFORM60_SUBMISSIONDATE)).getText().toString();
        borrowerExtra.FORM60SUBMISSIONDATE=((EditText) v.findViewById(R.id.editFORM60_SUBMISSIONDATE)).getText().toString();
        borrowerExtra.FATHER_FIRST_NAME=((TextView)v.findViewById(R.id.FatherFirstName)).getText().toString();
        borrowerExtra.FATHER_MIDDLE_NAME=((TextView)v.findViewById(R.id.FatherMiddelName)).getText().toString();
        borrowerExtra.FATHER_LAST_NAME=((TextView)v.findViewById(R.id.FatherLastName)).getText().toString();
        borrowerExtra.MOTHER_FIRST_NAME=((TextView)v.findViewById(R.id.MotherFirstName)).getText().toString();
        borrowerExtra.MOTHER_MIDDLE_NAME=((TextView)v.findViewById(R.id.MotherMiddelName)).getText().toString();
        borrowerExtra.MOTHER_LAST_NAME=((TextView)v.findViewById(R.id.MotherLastName)).getText().toString();
        borrowerExtra.SPOUSE_FIRST_NAME=((TextView)v.findViewById(R.id.SpouseFirstName)).getText().toString();
        borrowerExtra.SPOUSE_MIDDLE_NAME=((TextView)v.findViewById(R.id.SpouseMiddelName)).getText().toString();
        borrowerExtra.SPOUSE_LAST_NAME=((TextView)v.findViewById(R.id.SpouseLastName)).getText().toString();
        borrowerExtra.save();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        RangeCategory rangeCategory;
        switch (adapterView.getId()) {
            case R.id.spinLoanAppPersonalPresentResidenceOwner:
                Boolean bol = ((RangeCategory) adapterView.getSelectedItem()).DescriptionEn.equals("Other");
                getView().findViewById(R.id.spinLoanAppPersonalPresentHouseRent).setVisibility(bol ? View.VISIBLE : View.GONE);
                break;
            case R.id.spinLoanAppExtraOccupationType:
                //Log.d("OnItemSelected","done");
                View v = getView();
                View viewService = v.findViewById(R.id.lyLoanAppExtraIncomeService);
                View viewBusiness = v.findViewById(R.id.lyLoanAppExtraIncomeBusiness);
                View viewAgri = v.findViewById(R.id.lyLoanAppExtraIncomeAgri);
                View viewOther = v.findViewById(R.id.lyLoanAppExtraIncomeOther);
                if (BuildConfig.APPLICATION_ID == "com.softeksol.paisalo.jlgsourcing") {
                    rangeCategory = (RangeCategory) adapterView.getSelectedItem();
                    viewService.setVisibility(rangeCategory.DescriptionEn.equals("Service") ? View.VISIBLE : View.GONE);
                    viewBusiness.setVisibility(rangeCategory.DescriptionEn.equals("Business") ? View.VISIBLE : View.GONE);
                    viewAgri.setVisibility(rangeCategory.DescriptionEn.equals("Agriculture") ? View.VISIBLE : View.GONE);
                    viewOther.setVisibility(rangeCategory.DescriptionEn.equals("Other") ? View.VISIBLE : View.GONE);
                }
                if (BuildConfig.APPLICATION_ID == "net.softeksol.seil.groupfin" || BuildConfig.APPLICATION_ID == "net.softeksol.seil.groupfin.coorigin" || BuildConfig.APPLICATION_ID == "net.softeksol.seil.groupfin.sbicolending" ) {
                    viewService.setVisibility(View.GONE);
                    viewBusiness.setVisibility(View.GONE);
                    viewAgri.setVisibility(View.GONE);
                    viewOther.setVisibility(View.GONE);
                }
                break;
            case R.id.spinLoanAppExtraChildren:
                rangeCategory = (RangeCategory) adapterView.getSelectedItem();
                Spinner spinnerSchoolingChildren = (Spinner) getView().findViewById(R.id.spinLoanAppExtraChildrenSchooling);
                Spinner spinnerMonthlySpendOnChildren = (Spinner) getView().findViewById(R.id.spinLoanAppExtraChildrenSpending);
                if (Integer.parseInt(rangeCategory.RangeCode) > 0) {
                    //Log.d("Children","Schooling Children");
                    rlaSchoolingChildren = new AdapterListRange(this.getContext(), Utils.getList(0, Integer.parseInt(rangeCategory.RangeCode), 1, 1, null), false);
                    spinnerSchoolingChildren.setAdapter(rlaSchoolingChildren);
                    rlaSchoolingChildren.notifyDataSetChanged();
                    Utils.setSpinnerPosition(spinnerSchoolingChildren, borrowerExtra.SchoolingChildren);
                    spinnerSchoolingChildren.setVisibility(View.VISIBLE);
                    spinnerMonthlySpendOnChildren.setVisibility(View.VISIBLE);
                } else {
                    spinnerSchoolingChildren.setVisibility(View.GONE);
                    spinnerMonthlySpendOnChildren.setVisibility(View.GONE);
                }
                break;

                case R.id.spinLoanAppPersonalFamilyMembers:
                rangeCategory = (RangeCategory) adapterView.getSelectedItem();
                Spinner spinnerSchoolGoingChildren = (Spinner) getView().findViewById(R.id.spinLoanAppExtraChildrenSchooling);
                Spinner spinnerMonthSpendOnChildren = (Spinner) getView().findViewById(R.id.spinLoanAppExtraChildren);
                Spinner spinLoanAppExtraDependentAdults = (Spinner) getView().findViewById(R.id.spinLoanAppExtraDependentAdults);
                if (Integer.parseInt(rangeCategory.RangeCode) > 0) {
                    Log.d("Children","Schooling Children");
                    rlaSchoolingChildren = new AdapterListRange(this.getContext(), Utils.getList(0, Integer.parseInt(rangeCategory.RangeCode), 1, 1, null), false);
                    rlaChildren = new AdapterListRange(this.getContext(), Utils.getList(0, Integer.parseInt(rangeCategory.RangeCode), 1, 1, null), false);
                    rlaLoanAppExtraDependentAdults = new AdapterListRange(this.getContext(), Utils.getList(0, Integer.parseInt(rangeCategory.RangeCode), 1, 1, null), false);
                    spinnerSchoolGoingChildren.setAdapter(rlaSchoolingChildren);
                    spinnerMonthSpendOnChildren.setAdapter(rlaChildren);
                    spinLoanAppExtraDependentAdults.setAdapter(rlaLoanAppExtraDependentAdults);
                    rlaSchoolingChildren.notifyDataSetChanged();
                    rlaChildren.notifyDataSetChanged();
                    rlaLoanAppExtraDependentAdults.notifyDataSetChanged();
                    Utils.setSpinnerPosition(spinnerSchoolGoingChildren, borrowerExtra.SchoolingChildren);
                    spinLoanAppExtraDependentAdults.setVisibility(View.VISIBLE);
                    spinnerSchoolGoingChildren.setVisibility(View.VISIBLE);
                    spinnerMonthSpendOnChildren.setVisibility(View.VISIBLE);
                } else {
                    spinnerMonthSpendOnChildren.setVisibility(View.GONE);
                    spinnerSchoolGoingChildren.setVisibility(View.GONE);
                    spinLoanAppExtraDependentAdults.setVisibility(View.GONE);
                }
                break;


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public String getName() {
        return "Personal Details";
    }

    public interface OnFragmentBorrowerPersonalInteractionListener {
        void onFragmentBorrowerPersonalInteraction(Borrower borrower);
    }

}
