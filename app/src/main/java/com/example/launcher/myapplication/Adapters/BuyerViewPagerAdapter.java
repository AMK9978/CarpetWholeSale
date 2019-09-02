package com.example.launcher.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.launcher.myapplication.BoughtReports;
import com.example.launcher.myapplication.GoogleMapNav;
import com.example.launcher.myapplication.SearchingCarpets;
import com.example.launcher.myapplication.ShoppingCarpet;

public class BuyerViewPagerAdapter extends FragmentStatePagerAdapter{

    private Activity activity;
    private FragmentManager manager;
    public BuyerViewPagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.activity = activity;

    }

    @Override
    public Fragment getItem(int position) {
        hideKeyboard(activity);
        if (position == 0){
            return new ShoppingCarpet();
        }else if (position == 1){
            return new SearchingCarpets();
        }else if (position == 2){
            return new GoogleMapNav();
        }else{
            return new BoughtReports();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return ShoppingCarpet.TITLE;
        }else if (position == 1){
            return SearchingCarpets.TITLE;
        }else if (position == 2){
            return GoogleMapNav.TITLE;
        }else{
            return BoughtReports.TITLE;
        }
    }
}

