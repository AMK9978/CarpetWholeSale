package com.example.launcher.myapplication.Basic;

import android.util.Log;

import com.example.launcher.myapplication.Models.Carpet;

import java.util.*;

/**
 * @author Mehrnoush
 */

public class Knapsack {

    private static int KnappSack(int n, int W, Carpet w[], ArrayList<Carpet> values) {
        W = W / 10000;
        int[][] table = new int[n + 1][W + 1];
        for (int i = 0; i <= n; i++) {
            boolean flag = false;
            for (int j = 0; j <= W; j++) {
                if (i == 0 || j == 0)
                    table[i][j] = 0;

                else if (w[i].getPrice() / 10000 <= j) {
                    if (table[i - 1][j - w[i].getPrice() / 10000] + 1 > table[i - 1][j]) {
                        table[i][j] = table[i - 1][j - w[i].getPrice() / 10000] + 1;
                        flag = true;
                    } else {
                        table[i][j] = table[i - 1][j];
                    }

                } else {
                    table[i][j] = table[i - 1][j];
                }

            }
            if (flag)
                values.add(w[i]);
        }
        return table[n][W];
    }

    public Hashtable<Integer, Integer> main(int W, ArrayList<Carpet> arrayList) {
        //TODO: Knapsack method should be checked again
        int n, HowMany;
        n = arrayList.size();

        Carpet[] w = new Carpet[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            w[i] = arrayList.get(i);
        }
        ArrayList<Carpet> values = new ArrayList<>();
        Arrays.sort(w);
        System.out.println("sorted array is: " + Arrays.toString(w));
        HowMany = KnappSack(n - 1, W, w, values);
        Hashtable<Integer, Integer> options = new Hashtable<>();
        Log.i("TAG","the number of carpets you can buy: " + HowMany);
        System.out.println("the whole values which is selected: ");
        for (int i = 0; i < values.size(); i++) {
            System.out.print(values.get(i) + " ");
            if (!options.contains(values.get(i).getPrice()))
                options.put(values.get(i).getPrice(), 1);
            else
                options.put(values.get(i).getPrice(), options.get(values.get(i).getPrice()) + 1);
        }
        System.out.println();
        System.out.print("options: " + options);
        System.out.println();
        return options;
    }

}
