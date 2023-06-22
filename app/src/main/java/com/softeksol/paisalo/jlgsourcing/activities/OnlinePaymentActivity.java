package com.softeksol.paisalo.jlgsourcing.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isgpaymediatorbank.ISGPayController;
import com.isgpaymediatorbank.listener.ISGPayControllerInterface;
import com.isgpaymediatorbank.listener.ISGPayTransactionInterface;
import com.isgpaymediatorbank.listener.PaymentResponseListener;
import com.isgpaymediatorbank.request.ISGPayRequest;
import com.softeksol.paisalo.jlgsourcing.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class OnlinePaymentActivity extends AppCompatActivity implements PaymentResponseListener {

    TextView text_tranID;
    LinearLayout successlayout,layout_error;
    Button BtnSaveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_payment);
        Intent intent=getIntent();

        text_tranID=findViewById(R.id.text_tranID);
        successlayout=findViewById(R.id.layout_success);
        layout_error=findViewById(R.id.layout_error);
        BtnSaveData=findViewById(R.id.BtnSaveData);

        int amount=intent.getIntExtra("Price",0);
        ISGPayControllerInterface isgPayControllerInterface = ISGPayController.getInstance();
       // String version =isgPayControllerInterface.getVersionInfo();
        Log.i(TAG, "Amount--  "+ amount);
        Log.i(TAG, "RandomNumberString--  "+ getRandomNumberString());
        ISGPayRequest isgPay = new ISGPayRequest();
        isgPay.setVersion("1");//version.getText().toString().trim()
        isgPay.setTxnRefNo(getRandomNumberString());//TxnRefNo.getText().toString().trim()
        isgPay.setAmount(1+"00");//Amount.getText().toString().trim()
        isgPay.setPassCode("ZHBV7787");//AccessCode.getText().toString().trim() /SVPL4257
        isgPay.setBankId("000004");//BankId.getText().toString().trim()
        isgPay.setTerminalId("10043078");//TerminalId.getText().toString().trim() //10100781
        isgPay.setMerchantId("110000000043078");//MerchantId.getText().toString().trim() --101000000000781
        isgPay.setMCC("6012");//MCC.getText().toString().trim() //4112
        isgPay.setCurrency("356");//Currency.getText().toString().trim()
        isgPay.setPaymentType("Pay");//command.getText().toString().trim()
        isgPay.setOrderInfo("1000713818");//OrderInfo.getText().toString().trim()
        isgPay.setEmail("test@gmail.com");//Email.getText().toString().trim()
        isgPay.setPhone("");//Phone.getText().toString().trim()

       // isgPay.setHashKey("E59CD2BF6F4D86B5FB3897A680E0DD3E");
        isgPay.setHashKey("2D762434302717F62164D76FB2BB24A7");
       // isgPay.setAesKey("5EC4A697141C8CE45509EF485EE7D4B1");
        isgPay.setAesKey("B9E9C368727DEC07631FFE91161EF7FC");
        isgPay.setEnvironment(ISGPayRequest.Environment.UAT);

       // ISGPayControllerInterface isgPayControllerInterface1 = ISGPayController.getInstance();
        ISGPayTransactionInterface isgPayTransactionInterface = isgPayControllerInterface
                .withContext(OnlinePaymentActivity.this)
                .setPaymentResponseListener(OnlinePaymentActivity.this)
                .initialise();
        isgPayTransactionInterface.makePayment(isgPay);

    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999999);
        // this will convert any number sequence into 6 character.
        return String.format("%09d", number);
    }


    @Override
    public void onSuccess(String s) {
        successlayout.setVisibility(View.VISIBLE);
        BtnSaveData.setVisibility(View.VISIBLE);
        layout_error.setVisibility(View.GONE);
        Log.i(TAG, "onSuccess--  "+ s);
        try {
            JSONObject jo = new JSONObject(s);
            String tranID= jo.getString("RetRefNo");
            String OrderInfo= jo.getString("TxnRefNo");
            text_tranID.setText("Payment success "+"Ret Reference No. "+tranID+" "+"TxnRefNo. "+OrderInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFail(String s, String s1) {
        successlayout.setVisibility(View.GONE);
        layout_error.setVisibility(View.VISIBLE);
        BtnSaveData.setVisibility(View.VISIBLE);
        Log.i(TAG, "onFail--  "+ s + " ===="+s1);
    }
}