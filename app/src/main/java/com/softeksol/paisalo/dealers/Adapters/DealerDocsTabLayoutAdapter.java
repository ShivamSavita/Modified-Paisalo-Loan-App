package com.softeksol.paisalo.dealers.Adapters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.softeksol.paisalo.dealers.AddProductBankFrags.AddBankFragment;
import com.softeksol.paisalo.dealers.AddProductBankFrags.AddProductFragment;
import com.softeksol.paisalo.dealers.dealerDocsFrags.DealerPostDocsFragment;
import com.softeksol.paisalo.dealers.dealerDocsFrags.DealerPreDocsFragment;

public class DealerDocsTabLayoutAdapter extends FragmentPagerAdapter {

    Context mContext;
    int mTotalTabs;
    int DealerId;

    public DealerDocsTabLayoutAdapter(Context context , FragmentManager fragmentManager , int totalTabs, int DealerId) {
        super(fragmentManager);
        mContext = context;
        mTotalTabs = totalTabs;
        this.DealerId=DealerId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("asasas" , position + "");
        switch (position) {
            case 0:
                return new DealerPreDocsFragment(DealerId);
            case 1:
                return new DealerPostDocsFragment(DealerId);
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return mTotalTabs;
    }
}