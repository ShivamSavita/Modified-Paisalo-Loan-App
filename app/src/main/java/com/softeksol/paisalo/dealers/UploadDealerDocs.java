package com.softeksol.paisalo.dealers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.softeksol.paisalo.dealers.Adapters.DealerDocsTabLayoutAdapter;
import com.softeksol.paisalo.dealers.Adapters.TabLayoutAdapterOEM;
import com.softeksol.paisalo.jlgsourcing.databinding.ActivityUploadDealerDocsBinding;

public class UploadDealerDocs extends AppCompatActivity {
ActivityUploadDealerDocsBinding binding;
int dealerid;
Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadDealerDocsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent=getIntent();
        dealerid=intent.getIntExtra("dealerId",0);
        TabLayout tabs = binding.tabs;
        tabs.addTab(tabs.newTab().setText("Pre Documents"));
        tabs.addTab(tabs.newTab().setText("Post Documents"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);


        DealerDocsTabLayoutAdapter adapter=new DealerDocsTabLayoutAdapter(this,getSupportFragmentManager(),binding.tabs.getTabCount(),dealerid);
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