package com.softeksol.paisalo.dealers;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.softeksol.paisalo.dealers.Adapters.TabLayoutAdapter;
import com.softeksol.paisalo.jlgsourcing.databinding.ActivityProductBankAddPageOemBinding;

public class Product_Bank_addPage_OEM extends AppCompatActivity {

    private ActivityProductBankAddPageOemBinding binding;
    Intent i;
    int oemid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductBankAddPageOemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        i=getIntent();
        oemid=i.getIntExtra("OEMid",0);

        TabLayout tabs = binding.tabs;
        tabs.addTab(tabs.newTab().setText("Product List"));
        tabs.addTab(tabs.newTab().setText("Bank List"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);


        TabLayoutAdapter adapter=new TabLayoutAdapter(this,getSupportFragmentManager(),binding.tabs.getTabCount(),oemid);
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