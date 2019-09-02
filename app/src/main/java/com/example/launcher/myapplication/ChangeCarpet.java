package com.example.launcher.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.launcher.myapplication.Basic.Strassen;

import java.nio.IntBuffer;

/**
 * @author Amir Muhammad
 */

public class ChangeCarpet extends Fragment {

    private static final int GET_IMAGE_CODE_CARPET1 = 200;
    private static final int GET_IMAGE_CODE_CARPET2 = 201;
    public static String TITLE = "تغییر فرش";
    Button combine, choose_carpet, choose_filter;
    ImageView carpet, filter, product;
    Bitmap carpet_bitmap, filter_bitmap, product_bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_carpet, container, false);
        combine = view.findViewById(R.id.combine);
        choose_carpet = view.findViewById(R.id.choosebtn);
        choose_filter = view.findViewById(R.id.choose2btn);
        carpet = view.findViewById(R.id.carpet1image);
        filter = view.findViewById(R.id.carpet2image);

        product = view.findViewById(R.id.carpet3image);
        choose_carpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(1);
            }
        });
        choose_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(2);
            }
        });
        combine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProduct();
            }
        });
        return view;
    }

    private void getImage(int i) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (i == 1) {
            startActivityForResult(intent, GET_IMAGE_CODE_CARPET1);
        } else if (i == 2) {
            startActivityForResult(intent, GET_IMAGE_CODE_CARPET2);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_IMAGE_CODE_CARPET1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    carpet_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    carpet.setImageBitmap(carpet_bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == GET_IMAGE_CODE_CARPET2) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    filter_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    filter.setImageBitmap(filter_bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class WaitingDialog extends AsyncTask<Integer[][], Void, Bitmap> {
        private ProgressDialog dialog;

        @Override
        protected Bitmap doInBackground(Integer[][]... integers) {
            Integer arr1[][] = integers[0];
            Integer arr2[][] = integers[1];
            Strassen strassen = new Strassen();
            int product_arr[] = strassen.main(arr1, arr2);
            return convert_to_bitmap(product_arr);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("لطفا منتظر بمانید...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            product_bitmap = bitmap;
            product.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    private void setProduct() {
        if (carpet_bitmap == null || filter_bitmap == null){
            return;
        }
        prepareCarpets();
        Integer arr1[][] = convert_to_arr(carpet_bitmap);
        Integer arr2[][] = convert_to_arr(filter_bitmap);
        new WaitingDialog().execute(arr1, arr2);
    }

    private void prepareCarpets() {
        if (carpet_bitmap.getWidth() > filter_bitmap.getWidth()) {
            if (carpet_bitmap.getHeight() > filter_bitmap.getHeight()) {
                carpet_bitmap = Bitmap.createScaledBitmap(carpet_bitmap, filter_bitmap.getWidth(),
                        filter_bitmap.getHeight(), false);
            } else {
                filter_bitmap = Bitmap.createScaledBitmap(filter_bitmap, filter_bitmap.getWidth(),
                        carpet_bitmap.getHeight(), false);
                carpet_bitmap = Bitmap.createScaledBitmap(carpet_bitmap, filter_bitmap.getWidth(),
                        carpet_bitmap.getHeight(), false);
            }
        } else {
            if (carpet_bitmap.getHeight() > filter_bitmap.getHeight()) {
                filter_bitmap = Bitmap.createScaledBitmap(filter_bitmap, carpet_bitmap.getWidth(),
                        filter_bitmap.getHeight(), false);
                carpet_bitmap = Bitmap.createScaledBitmap(carpet_bitmap, carpet_bitmap.getWidth(),
                        filter_bitmap.getHeight(), false);
            } else {
                filter_bitmap = Bitmap.createScaledBitmap(filter_bitmap, carpet_bitmap.getWidth(),
                        carpet_bitmap.getHeight(), false);
            }
        }
        if (filter_bitmap.getHeight() > filter_bitmap.getWidth()) {
            filter_bitmap = Bitmap.createScaledBitmap(filter_bitmap, filter_bitmap.getWidth(),
                    filter_bitmap.getWidth(), false);
            carpet_bitmap = Bitmap.createScaledBitmap(carpet_bitmap, carpet_bitmap.getWidth(),
                    carpet_bitmap.getWidth(), false);
        } else {
            filter_bitmap = Bitmap.createScaledBitmap(filter_bitmap, filter_bitmap.getHeight(),
                    filter_bitmap.getHeight(), false);
            carpet_bitmap = Bitmap.createScaledBitmap(carpet_bitmap, carpet_bitmap.getHeight(),
                    carpet_bitmap.getHeight(), false);
        }
    }

    private Bitmap convert_to_bitmap(int[] product_arr) {
        Bitmap bitmap = Bitmap.createBitmap((int) Math.sqrt(product_arr.length),
                (int) Math.sqrt(product_arr.length), Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(product_arr));
        return bitmap;
    }

    private Integer[][] convert_to_arr(Bitmap carpet_bitmap) {
        Integer arr[][] = new Integer[carpet_bitmap.getHeight()][carpet_bitmap.getWidth()];
        for (int i = 0; i < carpet_bitmap.getHeight(); i++) {
            for (int j = 0; j < carpet_bitmap.getWidth(); j++) {
                arr[i][j] = carpet_bitmap.getPixel(j, i);
            }
        }
        return arr;
    }
}
