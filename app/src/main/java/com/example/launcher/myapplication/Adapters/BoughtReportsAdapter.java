package com.example.launcher.myapplication.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.launcher.myapplication.Models.Carpet;
import com.example.launcher.myapplication.Models.Report;
import com.example.launcher.myapplication.R;

import java.io.File;
import java.util.ArrayList;

public class BoughtReportsAdapter extends RecyclerView.Adapter<BoughtReportsAdapter.Item> {

    private Context context;
    private ArrayList<Report> reports;

    public BoughtReportsAdapter(Context context, ArrayList<Report> reports) {
        this.context = context;
        this.reports = reports;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sold_layout, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        Report report = reports.get(position);
        String str = report.getCarpet().getPrice() + " تومان ";
        holder.amountText.setText(str);
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    class Item extends RecyclerView.ViewHolder {
        TextView amountText,Carpet_id;
        Item(View itemView) {
            super(itemView);
            amountText = itemView.findViewById(R.id.priceText);

        }
    }
}
