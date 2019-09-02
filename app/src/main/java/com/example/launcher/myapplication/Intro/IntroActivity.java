package com.example.launcher.myapplication.Intro;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

import com.example.launcher.myapplication.MainActivity;
import com.example.launcher.myapplication.R;
import com.example.launcher.myapplication.Util;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntroActivity extends AppIntro {


    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        if (myPrefs.getBoolean("isShowedIntroApp", false)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("فروشگاه آنلاین فرش!");
        sliderPage.setDescription("با این اپ هم میتونی فرش بفروشی،هم ببینی،هم بخری :)");
        sliderPage.setImageDrawable(R.drawable.carpet);
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        sliderPage.setBgColor(getResources().getColor(R.color.analogous));
        sliderPage.setTitleTypeface("fonts/iran_sans_normal.ttf");
        sliderPage.setDescTypeface("fonts/iran_sans_normal.ttf");
        addSlide(AppIntroFragment.newInstance(sliderPage));
        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("هم برای خریدار");
        sliderPage2.setDescription("تو این اپ فرش دلخواهت رو پیدا میکنی :)");
        sliderPage2.setImageDrawable(R.drawable.target);
        sliderPage2.setBgColor(getResources().getColor(R.color.triadic_sec));
        sliderPage2.setTitleTypeface("fonts/iran_sans_normal.ttf");
        sliderPage2.setTitleColor(getResources().getColor(R.color.analogous));
        sliderPage2.setDescColor(getResources().getColor(R.color.analogous));
        sliderPage2.setDescTypeface("fonts/iran_sans_normal.ttf");
        addSlide(AppIntroFragment.newInstance(sliderPage2));
        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("هم برای فروشنده");
        sliderPage3.setDescription("فرش هات رو اضافه کن و بفروش و مدیریت کن :)");
        sliderPage3.setTitleTypeface("fonts/iran_sans_normal.ttf");
        sliderPage3.setDescTypeface("fonts/iran_sans_normal.ttf");
        sliderPage3.setImageDrawable(R.drawable.online_store);
        sliderPage3.setBgColor(getResources().getColor(R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage3));
        setSlideOverAnimation();
        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(getResources().getColor(R.color.analogous_sec));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(false);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        sharedPreferences.edit().putBoolean("isShowedIntroApp", true).commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        sharedPreferences.edit().putBoolean("isShowedIntroApp", true).commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
