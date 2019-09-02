package com.example.launcher.myapplication.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.launcher.myapplication.Models.Carpet;
import com.example.launcher.myapplication.R;

import java.io.File;
import java.util.ArrayList;

public class AvailableCarpetsAdapter extends RecyclerView.Adapter<AvailableCarpetsAdapter.Item> {

    private Context context;
    private ArrayList<Carpet> carpets;

    public AvailableCarpetsAdapter(Context context, ArrayList<Carpet> carpets) {
        this.context = context;
        this.carpets = carpets;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.available_carpet_view, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        Carpet carpet = carpets.get(position);
        String str = carpet.getPrice() + " تومان ";
        holder.priceText.setText(str);
        try {
            Log.i("TAG", String.valueOf(this.carpets.get(position).getPath()));
            Uri uri = Uri.fromFile(new File(String.valueOf(this.carpets.get(position).getPath())));
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            holder.carpetImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap
                    , dpToPx(165), dpToPx(150), false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return carpets.size();
    }

    class Item extends RecyclerView.ViewHolder {
        TextView priceText;
        ImageView carpetImage;
        Item(View itemView) {
            super(itemView);
            priceText = itemView.findViewById(R.id.priceText);
            carpetImage = itemView.findViewById(R.id.carpetImage);
        }
    }


    private int dpToPx(int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
