package com.example.launcher.myapplication;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.launcher.myapplication.Adapters.AvailableCarpetsAdapter;
import com.example.launcher.myapplication.Database.CarpetDBManager;
import com.example.launcher.myapplication.Models.Carpet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

public class ShoppingCarpet extends Fragment {
    Button submitMoney;
    RecyclerView recyclerView;
    EditText moneyText;
    TextView resText;
    AvailableCarpetsAdapter availableCarpetsAdapter;
    int money = 0;
    public static final String TITLE = "خرید";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_carpet, container, false);
        submitMoney = view.findViewById(R.id.submitMoney);
        recyclerView = view.findViewById(R.id.recycler);
        resText = view.findViewById(R.id.resText);
        moneyText = view.findViewById(R.id.moneyeditText);
        submitMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moneyText.getText().toString().equals("")) {
                    return;
                }
                money = Integer.valueOf(moneyText.getText().toString());
                displayRecyclerView(money);
            }
        });
        return view;
    }

    private void displayRecyclerView(int money) {
        CarpetDBManager carpetDBManager = new CarpetDBManager(getContext());
        carpetDBManager.open();
        ArrayList<Carpet> carpets = carpetDBManager.getALLCarpets();
        Log.i("TAGG", "");
        for (int i = 0; i < carpets.size(); i++) {
            Log.i("TAGG", String.valueOf(carpets.get(i).getPrice()));
        }
        Collections.sort(carpets);
        Hashtable<Integer, Integer> options = new Hashtable<>();
        int weight = 0;
        for (int i = 0; i < carpets.size(); i++) {
            if (weight + carpets.get(i).getPrice() <= money) {
                weight += carpets.get(i).getPrice();
                if (!options.contains(carpets.get(i))) {
                    options.put(carpets.get(i).getPrice(), 1);
                } else {
                    options.put(carpets.get(i).getPrice(), options.get(carpets.get(i).getPrice()) + 1);
                }
            }
        }
        StringBuilder res = new StringBuilder("");
        ArrayList<Carpet> available = new ArrayList<>();
        for (Integer key : options.keySet()) {
            int counter = 0;
            for (int i = 0; i < carpets.size(); i++) {
                if (carpets.get(i).getPrice() == key) {
                    available.add(carpets.get(i));
                    counter++;
                }
            }
            if (counter != 0) {
                res.append("\n").append(counter).append(" carpet with price: ").append(key);
            }
        }
        String msg = "You can buy " + available.size() + " carpets." + res.toString();
        resText.setText(msg);
        carpetDBManager.close();
        availableCarpetsAdapter = new AvailableCarpetsAdapter(getContext(), available);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setAdapter(availableCarpetsAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        InputMethodManager inputManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = ((Activity) getContext()).getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
