package com.softeksol.paisalo.dealers.Adapters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.softeksol.paisalo.dealers.AddProductBankFrags.AddBankFragment;
import com.softeksol.paisalo.dealers.AddProductBankFrags.AddProductFragment;

public class TabLayoutAdapterOEM extends FragmentPagerAdapter {

    Context mContext;
    int mTotalTabs;
    int OEMid;
    String BrandId;

    public TabLayoutAdapterOEM(Context context , FragmentManager fragmentManager , int totalTabs, int OEMid,String BrandId) {
        super(fragmentManager);
        mContext = context;
        mTotalTabs = totalTabs;
        this.OEMid=OEMid;
        this.BrandId=BrandId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("asasas" , position + "");
        switch (position) {
            case 0:
                return new AddProductFragment(OEMid,BrandId);
            case 1:
                return new AddBankFragment(OEMid);
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return mTotalTabs;
    }
}