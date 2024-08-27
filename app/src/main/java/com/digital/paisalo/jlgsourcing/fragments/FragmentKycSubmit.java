package com.digital.paisalo.jlgsourcing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.digital.paisalo.jlgsourcing.BuildConfig;
import com.digital.paisalo.jlgsourcing.Global;
import com.digital.paisalo.jlgsourcing.R;
import com.digital.paisalo.jlgsourcing.Utilities.Utils;
import com.digital.paisalo.jlgsourcing.WebOperations;
import com.digital.paisalo.jlgsourcing.adapters.AdapterListDocuments;
import com.digital.paisalo.jlgsourcing.entities.Borrower;
import com.digital.paisalo.jlgsourcing.entities.Borrower_Table;
import com.digital.paisalo.jlgsourcing.entities.DocumentStore;
import com.digital.paisalo.jlgsourcing.entities.DocumentStore_Table;
import com.digital.paisalo.jlgsourcing.entities.dto.DocumentStoreDTO;
import com.digital.paisalo.jlgsourcing.handlers.DataAsyncResponseHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentKycSubmit.OnListFragmentKycScanInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentKycSubmit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentKycSubmit extends Fragment implements AdapterView.OnItemClickListener {
    private List<DocumentStore> documentStores = new ArrayList<>();
    JsonObject jsonObject;
    private Borrower borrower;


    private OnListFragmentKycScanInteractionListener mListener;
    private AdapterListDocuments adapterListDocuments;
    private ListView listView;
    //private Activity activity;
    private DocumentStore mDocumentStore;
    private long borrowerId;
    //private AsyncHttpClient client=new AsyncHttpClient();


    public FragmentKycSubmit() {
        // Required empty public constructor
    }

    public static FragmentKycSubmit newInstance(long borrowerId) {

        Log.e("BorrowerID123",borrowerId+"");
        FragmentKycSubmit fragment = new FragmentKycSubmit();
        Bundle args = new Bundle();
        args.putLong(Global.BORROWER_TAG, borrowerId);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity= getActivity();

        if (getArguments() != null) {
            borrowerId = getArguments().getLong(Global.BORROWER_TAG, 0);
            Log.e("BorrowerIDDDD",borrowerId+"");
            if (borrowerId > 0) {
                borrower = SQLite.select().from(Borrower.class).where(Borrower_Table.FiID.eq(borrowerId)).querySingle();
            }

        }
        adapterListDocuments = new AdapterListDocuments(getActivity(), R.layout.layout_item_loan_app_kyc_capture, documentStores);
        jsonObject=new JsonObject();
        setHasOptionsMenu(true);
        /* client.setThreadPool(Executors.newSingleThreadExecutor());
        client.setTimeout(70000);
        WebOperations.setHttpHeadersJson(getContext(),client, true); */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_list_view, container, false);
        listView = (ListView) view.findViewById(R.id.lvList);
        listView.setAdapter(adapterListDocuments);
        listView.setOnItemClickListener(this);
        Log.e("KYCSUBMIT","DONE");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_kyc_submit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_submit_kyc:
                Utils.showSnakbar(getView(), "Option Selectd");
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentKycScanInteractionListener) {
            mListener = (OnListFragmentKycScanInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        documentStores.clear();
//        Log.e("DOCUMENTStoreChecking2",borrower.getPicture()+"");
        if (borrower == null) {
            documentStores.addAll(SQLite.select().from(DocumentStore.class)
                    .where(DocumentStore_Table.updateStatus.eq(false))
                    .and(DocumentStore_Table.ficode.greaterThan(0))
                    .queryList());
        } else {
            documentStores.addAll(SQLite.select().from(DocumentStore.class)
                    .where(DocumentStore_Table.updateStatus.eq(false))
                    .and(DocumentStore_Table.FiID.eq(borrower.FiID))
                    .and(DocumentStore_Table.ficode.greaterThan(0))
                    .queryList());
        }
        adapterListDocuments.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mDocumentStore = (DocumentStore) adapterView.getItemAtPosition(i);
        File file = new File(mDocumentStore.imagePath);
        //Log.d("Image Path", mDocumentStore.imagePath+ (file.exists()?"Exists":"does not exist")+ "  FileSize :" + file.length());

        /*if(mDocumentStore.fieldname.equals("documentpic")){
            uploadKycJson(mDocumentStore,view,client);
            //uploadKycJsonEntity(mDocumentStore,view,client);
        }else {*/


        DocumentStore documentStore = new DocumentStore();

        documentStore.Creator = mDocumentStore.Creator;
        documentStore.ficode = mDocumentStore.ficode;
        documentStore.fitag = mDocumentStore.fitag;
        documentStore.imageTag = mDocumentStore.imageTag;
        documentStore.GuarantorSerial = mDocumentStore.GuarantorSerial;
        documentStore.remarks = mDocumentStore.remarks;
        documentStore.checklistid = mDocumentStore.checklistid;
        documentStore.userid = mDocumentStore.userid;
        documentStore.fieldname = mDocumentStore.fieldname;
        documentStore.creationDate = mDocumentStore.creationDate;
        documentStore.latitude = mDocumentStore.latitude;
        documentStore.longitude = mDocumentStore.longitude;
        documentStore.DocId = mDocumentStore.DocId;
        documentStore.FiID = mDocumentStore.FiID;
        documentStore.apiRelativePath = mDocumentStore.apiRelativePath;
        documentStore.updateStatus = mDocumentStore.updateStatus;

//        Log.d("TAG", "onResume: "+  Borrower.getBorrowerIsNameVerifyEntryType(mDocumentStore.ficode, mDocumentStore.Creator));
//        Log.d("TAG", "onResume: "+  Borrower.getBorrowerIsAdharEntryEntryType(mDocumentStore.ficode, mDocumentStore.Creator));

        if (mDocumentStore.imagePath!=null){
            Log.e("KycSubmitImagePath",mDocumentStore.imagePath+"");
            Toast.makeText(getContext(), mDocumentStore.imagePath+"", Toast.LENGTH_SHORT).show();
            //documentStore.imagePath = mDocumentStore.imagePath;
            //documentStore.imagePath = "file:" + mDocumentStore.imagePath;
            documentStore.imagePath = mDocumentStore.imagePath;
            jsonObject.addProperty("FiCode",mDocumentStore.ficode);
            jsonObject.addProperty("Creator",mDocumentStore.Creator);
            jsonObject.addProperty("IsAadhaarEntry",Borrower.getBorrowerIsAdharEntryEntryType(mDocumentStore.ficode, mDocumentStore.Creator)==null?"M":Borrower.getBorrowerIsAdharEntryEntryType(mDocumentStore.ficode, mDocumentStore.Creator));
            jsonObject.addProperty("IsNameVerify",Borrower.getBorrowerIsNameVerifyEntryType(mDocumentStore.ficode, mDocumentStore.Creator)==null?"0":Borrower.getBorrowerIsNameVerifyEntryType(mDocumentStore.ficode, mDocumentStore.Creator));


            uploadKycJson(documentStore, view);
        }else {
            Toast.makeText(getContext(), "ImagePath_Null", Toast.LENGTH_SHORT).show();
        }
        //}
    }
    public interface OnListFragmentKycScanInteractionListener {
        void onListFragmentKycScanInteraction(Borrower borrower);
    }
    private void uploadKycJson(final DocumentStore documentStore, final View view) {

        DataAsyncResponseHandler responseHandler = new DataAsyncResponseHandler(getContext(), "Loan Financing", "Uploading " + DocumentStore.getDocumentName(documentStore.checklistid)) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);
                Utils.showSnakbar(getActivity().findViewById(android.R.id.content).getRootView(), responseString);
                //if(responseString.equals("")) {

                switch (documentStore.remarks){
                    case "AadharFront":
                        jsonObject.addProperty("Aadhaar_Latitude_front",documentStore.latitude);
                        jsonObject.addProperty("Aadhaar_Longitude_front",documentStore.longitude);
                        break;

                    case "AadharBack":
                        jsonObject.addProperty("Aadhaar_Latitude_Back",documentStore.latitude);
                        jsonObject.addProperty("Aadhaar_Longitude_Back",documentStore.longitude);
                        break;


                    case "VoterFront":
                        jsonObject.addProperty("Voterid_Latitude_front",documentStore.latitude);
                        jsonObject.addProperty("Voterid_Longitude_front",documentStore.longitude);
                        break;

                    case "VoterBack":
                        jsonObject.addProperty("Voterid_Latitude_Back",documentStore.latitude);
                        jsonObject.addProperty("Voterid_Longitude_Back",documentStore.longitude);
                        break;

                    case "PANCard":
                        jsonObject.addProperty("Pan_Latitude_front",documentStore.latitude);
                        jsonObject.addProperty("Pan_Longitude_front",documentStore.longitude);
                        break;

                    case "PassbookFirst":
                        jsonObject.addProperty("PassBook_Latitude_front",documentStore.latitude);
                        jsonObject.addProperty("PassBook_Longitude_front",documentStore.longitude);
                        break;

                    case "PassbookLast":
                        jsonObject.addProperty("PassBook_Latitude_Last",documentStore.latitude);
                        jsonObject.addProperty("PassBook_Longitude_Last",documentStore.longitude);
                        break;

                }
                Log.d("TAG", "onSuccess: "+jsonObject);
                view.setEnabled(false);
                view.setOnClickListener(null);
                view.setActivated(false);
                view.setClickable(false);
                documentStore.updateStatus = true;
               // documentStore.imageshow = "";
                documentStore.update();
                (new File(documentStore.imagePath)).delete();
                onResume();
                Log.d("TAG", "onSuccess: "+jsonObject);
                if (documentStores.size()==0){
                    Log.d("TAG", "onSuccess: "+jsonObject);
                    // TODO: 22-02-2023 Add api here for lat long of documents
                    sendLatLongOfDocsOnServer(jsonObject);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                view.setEnabled(true);
                view.setClickable(true);
                view.setActivated(true);
            }
        };
        DocumentStoreDTO documentStore1=documentStore.getDocumentDTO();
        documentStore1.Document="";
        Log.d("TAG", "uploadKycJson: "+WebOperations.convertToJson(documentStore1));

        String jsonString = WebOperations.convertToJson(documentStore.getDocumentDTO());
        Log.d("Document Json",jsonString);
        String apiPath = documentStore.checklistid == 0 ? "/api/uploaddocs/savefipicjson" : "/api/uploaddocs/savefidocsjson";
        (new WebOperations()).postEntity(getContext(), BuildConfig.BASE_URL + apiPath, jsonString, responseHandler);
    }

    private void sendLatLongOfDocsOnServer(JsonObject jsonObject) {
        DataAsyncResponseHandler responseHandlerForLatLong = new DataAsyncResponseHandler(getContext(), "Loan Financing", "Uploading Lat Long of Docs "){

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("TAG", "onSuccess: "+responseBody);
                Log.d("TAG", "onSuccess: Data Uploaded");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                Log.d("TAG", "onFailure: "+error.getMessage());
            }
        };

        String apiPath =  "/api/FIDocLatLong/SaveDocLatLong";
        Log.d("TAG", "sendLatLongOfDocsOnServer: "+jsonObject.toString());
        (new WebOperations()).postEntity(getContext(), "FIDocLatLong", "SaveDocLatLong" ,jsonObject.toString(), responseHandlerForLatLong);

    }

    /*private void submitAllKyc(){
        for(int i=0)
        View view=        adapterListDocuments.
    }*/
    /*private void uploadKyc(final DocumentStore documentStore, final View view){

        DataAsyncResponseHandler responseHandler=new DataAsyncResponseHandler(getContext(),"Loan Financing","Uploading KYC Document") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString=new String(responseBody);
                Utils.showSnakbar(getView(),responseString);
                //if(responseString.equals("")) {
                    view.setEnabled(false);
                    view.setOnClickListener(null);
                    view.setActivated(false);
                    view.setClickable(false);
                    documentStore.updateStatus = true;
                    documentStore.update();
                    (new File(documentStore.imagePath)).delete();
                    onResume();
                //}

            }
        };
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams requestParams=documentStore.configureClient(getContext(),client);
        //Log.d("Url", IglPreferences.getPrefString(getContext(), SEILIGL.BASE_URL,"")+documentStore.apiRelativePath+ "  ");
        client.post(getContext(),IglPreferences.getPrefString(getContext(), SEILIGL.BASE_URL,"")+documentStore.apiRelativePath,requestParams,responseHandler);
    }*/
}
