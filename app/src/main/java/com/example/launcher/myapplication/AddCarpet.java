package com.example.launcher.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.launcher.myapplication.Database.CarpetDBManager;
import com.example.launcher.myapplication.Models.Carpet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class AddCarpet extends Fragment {
    public static CharSequence TITLE = "اضافه کردن فرش";

    ImageView selectedCarpet;
    Button addBtn;
    EditText priceText;
    private final int GET_IMAGE_CODE = 101;
    String file_path;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_carpet, container, false);
        selectedCarpet = view.findViewById(R.id.inputCarpet);
        addBtn = view.findViewById(R.id.addBtn);
        priceText = view.findViewById(R.id.priceInput);
        selectedCarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GET_IMAGE_CODE);
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priceText.getText().toString().equals("")) {
                    return;
                }if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (PackageManager.PERMISSION_GRANTED !=
                            ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions((Activity) getContext(), new
                                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        addCarpet();
                    }
                } else {
                    addCarpet();
                }
            }
        });
        return view;
    }

    private void addCarpet() {
        Carpet carpet = new Carpet();
        carpet.setPath(file_path);
        carpet.setPrice(Integer.parseInt(priceText.getText().toString().trim()));
        CarpetDBManager carpetDBManager = new CarpetDBManager(getContext());
        carpetDBManager.open();
        carpetDBManager.addCarpet(carpet);
        carpetDBManager.close();
        Toast.makeText(getContext(),"افزوده شد" ,Toast.LENGTH_SHORT).show();
        priceText.setText("");
        selectedCarpet.setImageDrawable(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_IMAGE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    bitmap = Bitmap.createScaledBitmap(bitmap,169,149,false);
                    selectedCarpet.setImageBitmap(bitmap);
                    file_path = (saveToInternalStorage(bitmap));
                    Log.i("TAGgg", file_path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        String folder_main = "CarpetWholesaleApp";
        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        Random random = new Random();
        String image_path = (random.nextInt(99999)) + ".jpg";
        if (!f.exists()) {
            Log.i("access?", "built");
            f.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            File mypath = new File(f.getAbsolutePath() +"/" + image_path);
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f.getAbsolutePath() + "/" + image_path;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addCarpet();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getContext(), "You must accept this to download files", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
