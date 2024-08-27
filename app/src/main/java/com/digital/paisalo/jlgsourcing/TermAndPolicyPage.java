package com.digital.paisalo.jlgsourcing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TermAndPolicyPage extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_policy_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}