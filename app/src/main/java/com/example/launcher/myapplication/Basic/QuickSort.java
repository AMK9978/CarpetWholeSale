package com.example.launcher.myapplication.Basic;

import com.example.launcher.myapplication.SearchingCarpets;

// Java program for implementation of QuickSort
public class QuickSort {
    /* This function takes last element as pivot,
    places the pivot element at its correct
    position in sorted array, and places all
    smaller (smaller than pivot) to left of
    pivot and all greater elements to right
    of pivot */
    private int partition(SearchingCarpets.Bitmap_Likeness_Pair[] arr, int low, int high) {
        int pivot = arr[high].likeness;
        int i = (low - 1); // index of smaller element
        for (int j = low; j < high; j++) {
            // If current element is smaller than or
            // equal to pivot
            if (arr[j].likeness <= pivot) {
                i++;
                // swap arr[i] and arr[j]
                int temp = arr[i].likeness;
                arr[i].likeness = arr[j].likeness;
                arr[j].likeness = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        SearchingCarpets.Bitmap_Likeness_Pair temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }


    /* The main function that implements QuickSort()
    arr[] --> Array to be sorted,
    low --> Starting index,
    high --> Ending index */
    private void sort(SearchingCarpets.Bitmap_Likeness_Pair[] arr, int low, int high) {
        if (low < high) {
			/* pi is partitioning index, arr[pi] is
			now at right place */
            int pi = partition(arr, low, high);

            // Recursively sort elements before
            // partition and after partition
            sort(arr, low, pi - 1);
            sort(arr, pi + 1, high);
        }
    }

    public SearchingCarpets.Bitmap_Likeness_Pair[]  main(SearchingCarpets.Bitmap_Likeness_Pair[] arr) {
        int n = arr.length;
        QuickSort ob = new QuickSort();
        ob.sort(arr, 0, n - 1);
        return arr;
    }
}
