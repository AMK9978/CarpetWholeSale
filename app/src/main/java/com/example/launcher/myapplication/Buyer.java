package com.example.launcher.myapplication;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.launcher.myapplication.Adapters.BuyerViewPagerAdapter;

public class Buyer extends AppCompatActivity {

    private TabLayout tabLayout2;
    private BuyerViewPagerAdapter buyerViewPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        tabLayout2 = findViewById(R.id.tabLayout2);
        viewPager = findViewById(R.id.buyer_viewPager);
        buyerViewPagerAdapter = new BuyerViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(buyerViewPagerAdapter);
        tabLayout2.setupWithViewPager(viewPager);
    }
}
