package com.example.launcher.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;

public class Util extends AppCompatActivity {

    public static Typeface iranSans;
    public static Typeface iranSans_bold;
    public static Typeface iranSans_normal;
    public static Typeface iranSans_medium;
    public static Typeface iranSans_fanum;
    public static Typeface iranSans_light;

    public static SharedPreferences sharedPreferences;
    public static Typeface iranSans_ultra_light;
    public static Typeface typeface1;
    public static Typeface typeface2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String PREF_NAME = "pref";
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        typeface1 = getFont(getAssets(), "fonts/AGhasem.ttf");
        iranSans = getFont(getAssets(),  "fonts/iransans.ttf");
        iranSans_fanum = getFont(getAssets(),  "fonts/iran_sans_fanum.ttf");
        typeface2 = getFont(getAssets(), "fonts/iran_sans_fanum.ttf");
        iranSans_normal = getFont(getAssets(), "fonts/iran_sans_normal.ttf");
        iranSans_medium = getFont(getAssets(), "fonts/iran_sans_medium.ttf");
        iranSans_light = getFont(getAssets(), "fonts/iran_sans_light.ttf");
        iranSans_ultra_light = getFont(getAssets(), "fonts/iran_sans_ultra_light.ttf");
        iranSans_bold = getFont(getAssets(),"fonts/iran_sans_bold.ttf");
        FirebaseApp.initializeApp(this);
        Intent intent = new Intent(this, Splash.class);
        startActivity(intent);
    }

    public Typeface getFont(AssetManager assetManager, String path) {
        return Typeface.createFromAsset(assetManager, path);
    }

}
