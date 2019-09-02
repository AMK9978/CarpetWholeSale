package com.example.launcher.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.example.launcher.myapplication.Basic.QuickSort;
import com.example.launcher.myapplication.Database.CarpetDBManager;
import com.example.launcher.myapplication.Models.Carpet;
import java.io.File;
import java.util.ArrayList;

public class SearchingCarpets extends Fragment {
    public static final String TITLE = "جستجو";
    private ImageView input_carpet, output1, output2, output3;
    private int GET_IMAGE_CODE_CARPET1 = 101;
    private CarpetDBManager carpetDBManager;
    private int result = 0;
    private int[] Arr;
    private Bitmap bitmap;
    private int[] bitmap_array;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searching_carpets, container, false);
        Button submit = view.findViewById(R.id.choosebtn);
        input_carpet = view.findViewById(R.id.incarpet1imageV);
        carpetDBManager = new CarpetDBManager(getContext());
        output1 = view.findViewById(R.id.outcarpet3imageV);
        output2 = view.findViewById(R.id.outcarpet4imageV);
        output3 = view.findViewById(R.id.outcarpet2imageV);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
        input_carpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
        return view;
    }

    private void getSimilar() {
        int arr[] = new int[bitmap.getWidth() * bitmap.getHeight()];
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                arr[i * bitmap.getHeight() + j] = bitmap.getPixel(i, j);
            }
        }
        getSimilarImages(arr);
    }

    private void getSimilarImages(int arr[]) {
        carpetDBManager.open();
        ArrayList<Carpet> carpets = carpetDBManager.getALLCarpets();
        ArrayList<Bitmap_Likeness_Pair>arrayList = new ArrayList<>();
        for (int i = 0; i < carpets.size(); i++) {
            Carpet carpet = carpets.get(i);
            Uri uri = Uri.fromFile(new File(carpet.getPath()));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                int[][] bitmap_arr = convert_to_arr(Bitmap.createScaledBitmap(bitmap, 30, 40, false));
                int likeness = compareImages(arr, bitmap_arr);
                arrayList.add(new Bitmap_Likeness_Pair(i,bitmap,likeness));
                Log.i("like", String.valueOf(likeness));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i("TAGG","Size:" + carpets.size());
        Log.i("TAGG","------------");
        for (int i = 0; i < arrayList.size(); i++) {
            Log.i("TAGG", String.valueOf(arrayList.get(i).likeness));
        }
        QuickSort quickSort = new QuickSort();
        Bitmap_Likeness_Pair bitmap_likeness_pair[] = new Bitmap_Likeness_Pair[arrayList.size()];
        for (int i = 0; i < bitmap_likeness_pair.length; i++) {
            bitmap_likeness_pair[i] = arrayList.get(i);
        }
        bitmap_likeness_pair = quickSort.main(bitmap_likeness_pair);
        Log.i("TAGG","--------------");
        for (int i = 0; i < arrayList.size(); i++) {
            Log.i("TAGG", String.valueOf(bitmap_likeness_pair[i].likeness));
        }
        output1.setImageBitmap(bitmap_likeness_pair[bitmap_likeness_pair.length - 1].bitmap);
        output2.setImageBitmap(bitmap_likeness_pair[bitmap_likeness_pair.length - 2].bitmap);
        output3.setImageBitmap(bitmap_likeness_pair[bitmap_likeness_pair.length - 3].bitmap);
        carpetDBManager.close();
    }

    private int compareImages(int[] arr, int[][] bitmap_2darr) {
        int bitmap_arr[] = new int[bitmap_2darr[0].length * bitmap_2darr.length];
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 30; j++) {
                try {
                    bitmap_arr[i * 30 + j]
                            = bitmap_2darr[i][j];
                } catch (Exception e) {
                    Log.i("TAGGG", "i:" + i + ",j:" + j);
                }
            }
        }
        Log.i("MYTAG", arr.length + ",bitmap_arr length: " + bitmap_arr.length);
        Arr = arr;
        bitmap_array = bitmap_arr;
        new Calculator_Helper().execute();
        return result;
    }


    @SuppressLint("StaticFieldLeak")
    class Calculator_Helper extends AsyncTask<Void, Void, Void> {
        private int Opt(int[] arr, int[] bitmap_arr) {
            int memo[][] = new int[30][40];
            memo[0][0] = 0;
            for (int k = 1; k < 40; k++) {
                memo[0][k] = 2 + memo[0][k - 1];
            }
            for (int k = 1; k < 30; k++) {
                memo[k][0] = 2 + memo[k - 1][0];
            }
            int pen = 3;
            for (int k = 1; k < 30; k++) {
                for (int l = 1; l < 40; l++) {
                    if (arr[k] == bitmap_arr[l]) {
                        pen = 0;
                    }
                    memo[k][l] = Math.min(memo[k][l - 1] + 2, Math.min(memo[k - 1][l] + 2,
                            memo[k - 1][l - 1] + pen));
                }
            }
            return memo[29][39];
        }

        @Override
        protected Void doInBackground(Void... voids) {
            result = Opt(Arr, bitmap_array);
            return null;
        }
    }

    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GET_IMAGE_CODE_CARPET1);
    }


    private int[][] convert_to_arr(Bitmap carpet_bitmap) {
        int arr[][] = new int[carpet_bitmap.getHeight()][carpet_bitmap.getWidth()];
        for (int i = 0; i < carpet_bitmap.getHeight(); i++) {
            for (int j = 0; j < carpet_bitmap.getWidth(); j++) {
                arr[i][j] = carpet_bitmap.getPixel(j, i);
            }
        }
        return arr;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_IMAGE_CODE_CARPET1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    input_carpet.setImageBitmap(bitmap);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 30, 40, false);
                    if (bitmap != null)
                        getSimilar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class Bitmap_Likeness_Pair{
        Bitmap bitmap;
        int index;
        public int likeness;
        Bitmap_Likeness_Pair(int i, Bitmap bitmap, int likeness) {
            this.bitmap = bitmap;
            this.index = i;
            this.likeness = likeness;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
}
