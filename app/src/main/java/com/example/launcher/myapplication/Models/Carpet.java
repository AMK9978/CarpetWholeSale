package com.example.launcher.myapplication.Models;

import androidx.annotation.NonNull;

public class Carpet implements Comparable<Carpet>{
    int id, price;
    String path;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int compareTo(@NonNull Carpet o) {
        if (this.getPrice() == o.getPrice())
            return 0;
        else return this.getPrice() > o.getPrice() ? 1 : -1;
    }

    @Override
    public String toString() {
        return "A carpet with price:" + getPrice() + " and its path is:" + getPath();
    }
}
