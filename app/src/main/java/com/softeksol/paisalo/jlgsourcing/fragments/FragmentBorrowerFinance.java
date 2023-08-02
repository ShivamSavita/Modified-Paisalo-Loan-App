package com.softeksol.paisalo.jlgsourcing.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.loopj.android.http.DataAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.softeksol.paisalo.jlgsourcing.BuildConfig;
import com.softeksol.paisalo.jlgsourcing.Global;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.SEILIGL;
import com.softeksol.paisalo.jlgsourcing.Utilities.DateUtils;
import com.softeksol.paisalo.jlgsourcing.Utilities.MyTextWatcher;
import com.softeksol.paisalo.jlgsourcing.Utilities.Utils;
import com.softeksol.paisalo.jlgsourcing.Utilities.Verhoeff;
import com.softeksol.paisalo.jlgsourcing.WebOperations;
import com.softeksol.paisalo.jlgsourcing.activities.ActivityLoanApplication;
import com.softeksol.paisalo.jlgsourcing.adapters.AdapterListRange;
import com.softeksol.paisalo.jlgsourcing.entities.BankData;
import com.softeksol.paisalo.jlgsourcing.entities.Borrower;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerExtra;
import com.softeksol.paisalo.jlgsourcing.entities.BorrowerFamilyExpenses;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory;
import com.softeksol.paisalo.jlgsourcing.entities.RangeCategory_Table;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiClient;
import com.softeksol.paisalo.jlgsourcing.retrofit.ApiInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentBorrowerFinance.OnFragmentBorrowerFinanceInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentBorrowerFinance#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBorrowerFinance extends AbsFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    View v;
    private long borrowerId;
    private OnFragmentBorrowerFinanceInteractionListener mListener;

    private BorrowerFamilyExpenses expense;

    private AdapterListRange rlaBankType, rlaPurposeType, rlaLoanAmount, rlaFinanceDuration, rlaSchemeType;
    private EditText etIFSC, etBaoDtate, etBankAccount, etCIF;
    TextView  howOldAccount;
    private ActivityLoanApplication activity;
    private Borrower borrower;
    private BorrowerExtra borrowerExtra;
    private Spinner spinnerPurpose, spinnerLoanAmount, spinnerLoanDuration, spinnerBankAcType, spinnerSchemeType;
    //private TextWatcher IFSCTextWatcher;
    private TextInputEditText tietRent, tietFooding, tietEducation, tietHealth, tietTravelling, tietEntertainment, tietOthers,tietRentalIncome;
    private AppCompatSpinner acspHomeType, acspHomeRoofType, acspToiletType, acspLiveingWithSpouse;
    TextView tilBankAccountName;
    Button checkBankAccountNuber;
    public FragmentBorrowerFinance() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param borrowerId Parameter.
     * @return A new instance of fragment FragmentBorrowerAadhar.
     */
    public static FragmentBorrowerFinance newInstance(long borrowerId) {
        FragmentBorrowerFinance fragment = new FragmentBorrowerFinance();
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

        rlaBankType = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("bank_ac_type")).queryList(), false);

        rlaSchemeType = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("DISBSCH")).queryList(), false);

        rlaPurposeType = new AdapterListRange(this.getContext(),
                SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("loan_purpose"))
                        .orderBy(RangeCategory_Table.SortOrder, true).queryList(), true);

        //rlaLoanAmount = new AdapterListRange(this.getContext(), Utils.getList(16, 20, 1, 5000, "Rupees"), false);
        //Commented for E-Vehicle and above line added just for e-vehicle
        if (BuildConfig.APPLICATION_ID.equals("com.softeksol.paisalo.jlgsourcing")) {
            rlaLoanAmount = new AdapterListRange(this.getContext(), Utils.getList(16, 20, 1, 5000, "Rupees"), false);

        }
        else {
            rlaLoanAmount = new AdapterListRange(this.getContext(),
                    SQLite.select().from(RangeCategory.class).where(RangeCategory_Table.cat_key.eq("loan_amt")).queryList(), false);
        }

          rlaFinanceDuration = new AdapterListRange(this.getContext(), Utils.getList(4, 8, 1, 3, "Months"), true);
        //rlaFinanceDuration = new AdapterListRange(this.getContext(), Utils.getList(5, 6, 1, 3, "Months"), true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_borrower_finance, container, false);

        ImageView imgViewLeft = (ImageView) v.findViewById(R.id.btnNavLeft);
        activity.setNavOnClikListner(imgViewLeft);
        ImageView imgViewRight = (ImageView) v.findViewById(R.id.btnNavRight);
        activity.setNavOnClikListner(imgViewRight);
        checkBankAccountNuber=v.findViewById(R.id.checkBankAccountNuber);
        tilBankAccountName=v.findViewById(R.id.tilBankAccountName);
        spinnerPurpose = (Spinner) v.findViewById(R.id.spinLoanAppFinancePurposePrimary);
        spinnerPurpose.setAdapter(rlaBankType);
        spinnerBankAcType = (Spinner) v.findViewById(R.id.spinLoanAppFinanceAccountType);
        spinnerBankAcType.setAdapter(rlaPurposeType);
        spinnerLoanAmount = (Spinner) v.findViewById(R.id.spinLoanAppFinanceLoanAmount);
        spinnerLoanAmount.setAdapter(rlaLoanAmount);
        spinnerLoanDuration = (Spinner) v.findViewById(R.id.spinLoanAppFinanceDuration);
        spinnerLoanDuration.setAdapter(rlaFinanceDuration);


        spinnerSchemeType = (Spinner) v.findViewById(R.id.spinLoanAppFinanceScheme);
        spinnerSchemeType.setAdapter(rlaSchemeType);
        tietRent = (TextInputEditText) v.findViewById(R.id.tietFamilyExpensesRent);
        tietFooding = (TextInputEditText) v.findViewById(R.id.tietFamilyExpensesFood);
        tietEducation = (TextInputEditText) v.findViewById(R.id.tietFamilyExpensesEducation);
        tietHealth = (TextInputEditText) v.findViewById(R.id.tietFamilyExpensesHealth);
        tietTravelling = (TextInputEditText) v.findViewById(R.id.tietFamilyExpensesTravelling);
        tietEntertainment = (TextInputEditText) v.findViewById(R.id.tietFamilyExpensesEntertainment);
        tietOthers = (TextInputEditText) v.findViewById(R.id.tietFamilyExpensesOthers);
        tietRentalIncome = (TextInputEditText) v.findViewById(R.id.tietRentalIncome);

        acspHomeType = (AppCompatSpinner) v.findViewById(R.id.acspHomeType);
        acspHomeRoofType = (AppCompatSpinner) v.findViewById(R.id.acspHomeRoofType);
        acspToiletType = (AppCompatSpinner) v.findViewById(R.id.acspToiletType);
        acspLiveingWithSpouse = (AppCompatSpinner) v.findViewById(R.id.acspLivigWithSpouse);

        acspHomeType.setAdapter(new AdapterListRange(getContext(), RangeCategory.getRangesByCatKey("house-type"), false));
        acspHomeRoofType.setAdapter(new AdapterListRange(getContext(), RangeCategory.getRangesByCatKey("house-roof-type"), false));
        acspToiletType.setAdapter(new AdapterListRange(getContext(), RangeCategory.getRangesByCatKey("toilet-type"), false));
        acspLiveingWithSpouse.setAdapter(new AdapterListRange(this.getContext(), Utils.getList(1, 6, 1, 1, "Year(s)"), true));

        etIFSC = (EditText) v.findViewById(R.id.etLoanAppFinanceBankIfsc);
//        etCIF = (EditText) v.findViewById(R.id.bankCIF);
        etIFSC.addTextChangedListener(new MyTextWatcher(etIFSC) {
            @Override
            public void validate(EditText editText, String text) {
                validateIFSC(editText, text);
            }
        });
        etBankAccount = (EditText) v.findViewById(R.id.etLoanAppFinanceBankAccountNo);
        howOldAccount = (TextView) v.findViewById(R.id.howOldAccount);
        howOldAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                String dateInString = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                try {
                                    Date date = formatter.parse(dateInString);
                                    Log.e("DATATIMEhowOldAccountSTRING",formatter.format(date));
                                    howOldAccount.setText(formatter.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                               // Log.e("DATATIMEhowOldAccountNEWWDATA",DateUtils.getsmallDate(howOldAccount.getText().toString(),"dd-MM-yyyy")+"");


                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();




            }
        });

        // etBaoDtate=(EditText) v.findViewById(R.id.etLoanAppFinanceBankAcOpenDate);
        // etBaoDtate.setOnClickListener(this);
        borrower = activity.getBorrower();
        borrowerExtra=borrower.getBorrowerExtra();
        expense=borrower.getFiFamilyExpenses();
        setDataToView(v);
        //etIFSC.addTextChangedListener(IFSCTextWatcher);
        checkBankAccountNuber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etBankAccount.getText().toString().equals("") && !etIFSC.getText().toString().equals("")){
                    cardValidate(etBankAccount.getText().toString().trim(),etIFSC.getText().toString().trim());
                }else{
                    Toast.makeText(getContext(), "Please enter account number and IFSC code Properly", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    private void cardValidate(String id,String bankIfsc) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Fetching Details");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ApiInterface apiInterface= getClientPan(SEILIGL.NEW_SERVERAPIAGARA).create(ApiInterface.class);
        Call<JsonObject> call=apiInterface.cardValidate(getJsonOfString(id,bankIfsc));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        if(response.body().get("data").getAsJsonObject().get("account_exists").equals("true")){
                            tilBankAccountName.setVisibility(View.VISIBLE);
                            tilBankAccountName.setText(response.body().get("data").getAsJsonObject().get("full_name").getAsString());
                            //tilBankAccountName.setTextColor(getResources().getColor(R.color.green));
                            checkBankAccountNuber.setBackground(getResources().getDrawable(R.drawable.check_sign_ic_green));
                            checkBankAccountNuber.setEnabled(false);
                        }else{
                            tilBankAccountName.setVisibility(View.VISIBLE);
                            tilBankAccountName.setText("Account Holder Name Not Found");
                            tilBankAccountName.setTextColor(getResources().getColor(R.color.red));
                            checkBankAccountNuber.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));
                            checkBankAccountNuber.setEnabled(true);
                        }

                    }catch (Exception e){
                        tilBankAccountName.setVisibility(View.VISIBLE);
                        tilBankAccountName.setText("Card Holder Name Not Found");
                        checkBankAccountNuber.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));
                        checkBankAccountNuber.setEnabled(true);

                    }
                    progressDialog.cancel();


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                tilBankAccountName.setText(t.getMessage());
                    progressDialog.cancel();
                    checkBankAccountNuber.setBackground(getResources().getDrawable(R.drawable.check_sign_ic));



            }
        });
    }
    private JsonObject getJsonOfString(String id, String bankIfsc) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("type","bankaccount");
        jsonObject.addProperty("txtnumber",id);
        jsonObject.addProperty("ifsc",bankIfsc);
        jsonObject.addProperty("userdob","");
        return  jsonObject;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentBorrowerFinanceInteractionListener) {
            mListener = (OnFragmentBorrowerFinanceInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Log.d("Detaching", "Detach");
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        borrower = activity.getBorrower();
        borrowerExtra=activity.getBorrower().getBorrowerExtra();
        expense = activity.getBorrower().getFiFamilyExpenses();
        if (expense == null) {
            expense = new BorrowerFamilyExpenses();
        }
        if (borrowerExtra==null){
            borrowerExtra=new BorrowerExtra();
        }
        setDataToView(getView());
    }

    @Override
    public void onPause() {
        //etIFSC.removeTextChangedListener(IFSCTextWatcher);
        getDataFromView(getView());
        super.onPause();
    }


    private void setDataToView(View v) {
        try {
            if (borrower.House_Owner.equals("Self")){
                tietRent.setText("0");
                tietRent.setEnabled(false);
            }else {
                tietRent.setEnabled(true);
                tietRent.setText(String.valueOf(expense.getRent()));
            }
        }catch (Exception e){
            tietRent.setEnabled(true);
            tietRent.setText(String.valueOf(expense.getRent()));

        }

        Utils.setSpinnerPosition(spinnerLoanAmount, borrower.Loan_Amt);
        //Commented for E-Vehicle
        spinnerLoanAmount.setEnabled(borrower.Loan_Amt < 10);

        Utils.setSpinnerPosition(spinnerLoanDuration, borrower.Loan_Duration);
        Utils.setSpinnerPosition(spinnerPurpose, borrower.Loan_Reason);
        Utils.setSpinnerPosition(spinnerSchemeType, borrower.T_ph3);

        spinnerPurpose.setEnabled(borrower.Loan_Reason.length() < 3);
        etBankAccount.setText(Utils.NullIf(borrower.bank_ac_no, ""));
        etBankAccount.setEnabled(Utils.NullIf(borrower.bank_ac_no, "").length() < 3);
        howOldAccount.setText(DateUtils.getFormatedDateOpen(borrower.BankAcOpenDt,"dd-MM-yyyy"));
       // howOldAccount.setEnabled(Utils.NullIf(borrower.bank_ac_month, "").length() < 1);

        Utils.setSpinnerPosition(spinnerBankAcType, borrower.BankAcType);
        etIFSC.setText(Utils.NullIf(borrower.Enc_Property, ""));
        ((TextView) v.findViewById(R.id.tvLoanAppFinanceBankName)).setText(Utils.NullIf(borrower.BankName, ""));
        ((TextView) v.findViewById(R.id.tvLoanAppFinanceBankBranch)).setText(Utils.NullIf(borrower.Bank_Address, ""));
        //((EditText) v.findViewById(R.id.etLoanAppFinanceBankAcOpenDate)).setText(DateUtils.getFormatedDate(borrower.BankAcOpenDt,"dd-MMM-yyyy"));


        tietFooding.setText(String.valueOf(expense.getFooding()));
        tietEducation.setText(String.valueOf(expense.getEducation()));
        tietHealth.setText(String.valueOf(expense.getHealth()));
        tietTravelling.setText(String.valueOf(expense.getTravelling()));
        tietEntertainment.setText(String.valueOf(expense.getEducation()));
        tietOthers.setText(String.valueOf(expense.getOthers()));
     //   tietRentalIncome.setText(String.valueOf(borrowerExtra.getExtraDTO().getRentalIncome()));
        Utils.setOnFocuseSelect(tietRent, "0");
        Utils.setOnFocuseSelect(tietFooding, "0");
        Utils.setOnFocuseSelect(tietEducation, "0");
        Utils.setOnFocuseSelect(tietHealth, "0");
        Utils.setOnFocuseSelect(tietTravelling, "0");
        Utils.setOnFocuseSelect(tietEntertainment, "0");
        Utils.setOnFocuseSelect(tietOthers, "0");
        Utils.setSpinnerPosition(acspHomeType, expense.getHomeType());
        Utils.setSpinnerPosition(acspHomeRoofType, expense.getHomeRoofType());
        Utils.setSpinnerPosition(acspToiletType, expense.getToiletType());
        Utils.setSpinnerPosition(acspLiveingWithSpouse, expense.getLivingWSpouse());
    }

    private void getDataFromView(View v) {
        if (v != null) {
            borrower.Loan_Amt = Utils.getSpinnerIntegerValue(spinnerLoanAmount);
            borrower.Loan_Duration = Utils.getSpinnerStringValue(spinnerLoanDuration);
            borrower.Loan_Reason = Utils.getSpinnerStringValue(spinnerPurpose);
            borrower.bank_ac_no = Utils.getNotNullText(etBankAccount);
            borrower.BankAcOpenDt = DateUtils.getParsedDate(howOldAccount.getText().toString(),"dd-MM-yyyy");
            borrower.BankAcType = Utils.getSpinnerStringValue(spinnerBankAcType);
            borrower.Enc_Property = Utils.getNotNullText(etIFSC);
//            borrower.fiExtraBank.setBankCif(Utils.getNotNullText(etCIF));
            borrower.BankName = Utils.getNotNullText((TextView) v.findViewById(R.id.tvLoanAppFinanceBankName));
            borrower.Bank_Address = Utils.getNotNullText((TextView) v.findViewById(R.id.tvLoanAppFinanceBankBranch));
            borrower.T_ph3 = Utils.getSpinnerStringValue(spinnerSchemeType);
            borrowerExtra.RentalIncome= Utils.getNotNullInt(tietRentalIncome);



            expense.setRent(Utils.getNotNullInt(tietRent));
            expense.setFooding(Utils.getNotNullInt(tietFooding));
            expense.setEducation(Utils.getNotNullInt(tietEducation));
            expense.setHealth(Utils.getNotNullInt(tietHealth));
            expense.setTravelling(Utils.getNotNullInt(tietTravelling));
            expense.setEntertainment(Utils.getNotNullInt(tietEntertainment));
            expense.setOthers(Utils.getNotNullInt(tietOthers));

            expense.setHomeType(Utils.getSpinnerStringValue(acspHomeType));
            expense.setHomeRoofType(Utils.getSpinnerStringValue(acspHomeRoofType));
            expense.setToiletType(Utils.getSpinnerStringValue(acspToiletType));
            expense.setLivingWSpouse(Utils.getSpinnerStringValue(acspLiveingWithSpouse));

            activity.getBorrower().associateBorrowerFamilyExpenses(expense);
            borrowerExtra.save();
            expense.save();
            borrower.save();
            //borrower.Business_Detail = borrower.Loan_Reason;
            //borrower.BankAcOpenDt=DateUtils.getParsedDate(((EditText)v.findViewById(R.id.etLoanAppFinanceBankAcOpenDate)).getText().toString(),"dd-MMM-yyyy");
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
        Calendar baoDate = Calendar.getInstance();
        baoDate.set(selectedYear, selectedMonth, selectedDay);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        etBaoDtate.setText(simpleDateFormat.format(baoDate.getTime()));
    }

    @Override
    public void onClick(View view) {
        /*switch (view.getId()){
            case R.id.etLoanAppFinanceBankAcOpenDate:
                Calendar mcurrentDate=(Calendar.getInstance());
                mcurrentDate.add(Calendar.MONTH,-1);
                DateUtils.showDatePicker(getActivity(),this,mcurrentDate,0,0,mcurrentDate.getTimeInMillis());
                break;
        }*/
    }

    @Override
    public String getName() {
        return "Financial Info";
    }

    public interface OnFragmentBorrowerFinanceInteractionListener {
        void onFragmentBorrowerFinanceInteraction(Borrower borrower);
    }

    private void validateIFSC(final EditText editText, String text) {
        editText.setError(null);
        if (editText.length() != 11) {
            editText.setError("Must be 11 Character");
        } else if (!Verhoeff.validateIFSC(text)) {
            editText.setError("Please input a valid IFSC. " + " " + text + "is not a valid IFSC");
        } else {
            DataAsyncHttpResponseHandler dataAsyncHttpResponseHandler = new DataAsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String jsonString = new String(responseBody);
                    try {
                        TextView tvBankName = (TextView) getView().findViewById(R.id.tvLoanAppFinanceBankName);
                        TextView tvBankBranch = (TextView) getView().findViewById(R.id.tvLoanAppFinanceBankBranch);
                        tvBankName.setText("");
                        tvBankBranch.setText("");
                        BankData bankData = WebOperations.convertToObject(jsonString, BankData.class);
                        if (bankData.BANK == null) {
                            editText.setError("This IFSC not available");
                        } else if (borrower != null) {
                            borrower.BankName = bankData.BANK.replace("á", "").replace("┬", "").replace("û", "").replace("ô", "");
                            borrower.Bank_Address = bankData.ADDRESS.replace("á", "").replace("┬", "").replace("û", "").replace("ô", "");
                            tvBankName.setText(borrower.BankName);
                            tvBankBranch.setText(borrower.Bank_Address);
                        }
                    } catch (NullPointerException ne) {

                    } catch (Exception e) {
                        editText.setError("Try Again");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getActivity(), "IFSC not found ", Toast.LENGTH_LONG).show();
                }
            };
            RequestParams params = new RequestParams();
            params.add("IFSC", text);
            (new WebOperations()).getEntity(getActivity(), "bankname", "getbankname", params, dataAsyncHttpResponseHandler);
        }
    }


    public static Retrofit getClientPan(String BASE_URL) {
        Retrofit retrofit = null;
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


}
