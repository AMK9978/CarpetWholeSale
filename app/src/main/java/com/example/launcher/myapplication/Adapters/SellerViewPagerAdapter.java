package com.example.launcher.myapplication.Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.launcher.myapplication.AddCarpet;
import com.example.launcher.myapplication.ChangeCarpet;
import com.example.launcher.myapplication.DesignNewCarpets;
import com.example.launcher.myapplication.SoldReports;

public class SellerViewPagerAdapter extends FragmentStatePagerAdapter {

    public SellerViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ChangeCarpet();
        } else if (position == 1) {
            return new DesignNewCarpets();
        } else if (position == 2) {
            return new AddCarpet();
        } else {
            return new SoldReports();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return ChangeCarpet.TITLE;
        } else if (position == 1) {
            return DesignNewCarpets.TITLE;
        } else if (position == 2) {
            return AddCarpet.TITLE;
        } else {
            return SoldReports.TITLE;
        }
    }
}
