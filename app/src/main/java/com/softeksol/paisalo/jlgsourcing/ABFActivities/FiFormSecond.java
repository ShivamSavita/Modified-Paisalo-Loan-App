package com.softeksol.paisalo.jlgsourcing.ABFActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Toast;

import com.softeksol.paisalo.dealers.Adapters.AddFemIncomeAdapter;
import com.softeksol.paisalo.jlgsourcing.R;
import com.softeksol.paisalo.jlgsourcing.entities.ReceiptData;

public class FiFormSecond extends AppCompatActivity {
Button addFemIncomeBtn;
RecyclerView addFemIncomeRecView;
int size=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fi_form_second);
        addFemIncomeBtn=findViewById(R.id.addFemIncomeBtn);
        addFemIncomeRecView=findViewById(R.id.addFemIncomeRecView);
        addFemIncomeRecView.setLayoutManager(new LinearLayoutManager(this));



        addFemIncomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size++;
                addFemIncomeRecView.setAdapter(new AddFemIncomeAdapter(FiFormSecond.this,size));
                new AddFemIncomeAdapter(FiFormSecond.this,size).notifyDataSetChanged();
                Toast.makeText(FiFormSecond.this, "New Member Added!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}