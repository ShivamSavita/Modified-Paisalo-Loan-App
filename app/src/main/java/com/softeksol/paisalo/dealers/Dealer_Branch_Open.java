package com.softeksol.paisalo.dealers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.softeksol.paisalo.jlgsourcing.R;

public class Dealer_Branch_Open extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_branch_open);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Open Branch");

    }
}