package com.softeksol.paisalo.dealers;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.softeksol.paisalo.dealers.Adapters.TabLayoutAdapterOEM;
import com.softeksol.paisalo.jlgsourcing.databinding.ActivityProductBankAddPageOemBinding;

public class Product_Bank_addPage_OEM extends AppCompatActivity {

    private ActivityProductBankAddPageOemBinding binding;
    Intent i;
    int oemid;
    String BrandId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductBankAddPageOemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        i=getIntent();
        oemid=i.getIntExtra("OEMid",0);
        BrandId=i.getStringExtra("BrandId");

        TabLayout tabs = binding.tabs;
        tabs.addTab(tabs.newTab().setText("Product List"));
        tabs.addTab(tabs.newTab().setText("Bank List"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);


        TabLayoutAdapterOEM adapter=new TabLayoutAdapterOEM(this,getSupportFragmentManager(),binding.tabs.getTabCount(),oemid,BrandId);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabs));

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}