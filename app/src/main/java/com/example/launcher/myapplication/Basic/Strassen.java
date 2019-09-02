package com.example.launcher.myapplication.Basic;

import android.util.Log;

import java.util.ArrayList;

/**
 * @author Mehrnoush
 */
public class Strassen {

    public static int[][] ijk_algorithm(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = 0;
                for (int k = 0; k < n; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return C;
    }

    public static int[][] add(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }

    public static int[][] subtract(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] - B[i][j];
            }
        }
        return C;
    }

    public static int nextPowerOfTwo(int n) {
        int log2 = (int) Math.ceil(Math.log(n) / Math.log(2));
        return (int) Math.pow(2, log2);
    }

    public static int[][] Strassen(ArrayList<ArrayList<Integer>> A,
                                   ArrayList<ArrayList<Integer>> B) {

        int n = A.size();
        int m = nextPowerOfTwo(n);

        int[][] APrep = new int[m][m];
        int[][] BPrep = new int[m][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                APrep[i][j] = A.get(i).get(j);
                BPrep[i][j] = B.get(i).get(j);
            }
        }

        int[][] CPrep = StrassenR(APrep, BPrep);
        int[][] C = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = CPrep[i][j];
            }
        }

        return C;
    }

    public static int[][] StrassenR(int[][] A, int[][] B) {

        int n = A.length;
        if (n == 2) {
            return ijk_algorithm(A, B);
        } else {
            int newSize = n / 2;
            int[][] a11 = new int[newSize][newSize];
            int[][] a12 = new int[newSize][newSize];
            int[][] a21 = new int[newSize][newSize];
            int[][] a22 = new int[newSize][newSize];

            int[][] b11 = new int[newSize][newSize];
            int[][] b12 = new int[newSize][newSize];
            int[][] b21 = new int[newSize][newSize];
            int[][] b22 = new int[newSize][newSize];

            int[][] aResult = new int[newSize][newSize];
            int[][] bResult = new int[newSize][newSize];

            for (int i = 0; i < newSize; i++) {
                for (int j = 0; j < newSize; j++) {

                    a11[i][j] = A[i][j];//top left
                    a12[i][j] = A[i][j + newSize];//top right
                    a21[i][j] = A[i + newSize][j];//bottom left
                    a22[i][j] = A[i + newSize][j + newSize];//bottom right

                    b11[i][j] = B[i][j];//top left
                    b12[i][j] = B[i][j + newSize];//top right
                    b21[i][j] = B[i + newSize][j];//bottom left
                    b22[i][j] = B[i + newSize][j + newSize];// bottom right
                }
            }

            aResult = subtract(b12, b22);
            int[][] p1 = StrassenR(a11, aResult);

            aResult = add(a11, a12);
            int[][] p2 = StrassenR(aResult, b22);

            aResult = add(a21, a22);
            int[][] p3 = StrassenR(aResult, b11);

            bResult = subtract(b21, b22);
            int[][] p4 = StrassenR(a22, bResult);

            aResult = add(a11, a22);
            bResult = add(b11, b22);
            int[][] p5 = StrassenR(aResult, bResult);

            aResult = subtract(a12, a22);
            bResult = add(b21, b22);
            int[][] p6 = StrassenR(aResult, bResult);

            aResult = subtract(a11, a21);
            bResult = add(b11, b12);
            int[][] p7 = StrassenR(aResult, bResult);

            aResult = add(p5, p4);
            bResult = add(aResult, p6);
            int[][] c11 = subtract(bResult, p2);

            int[][] c12 = add(p1, p2);
            int[][] c21 = subtract(p3, p4);

            aResult = add(p1, p5);
            bResult = add(p3, p7);
            int[][] c22 = subtract(aResult, bResult);

            int[][] C = new int[n][n];
            for (int i = 0; i < newSize; i++) {
                for (int j = 0; j < newSize; j++) {
                    C[i][j] = c11[i][j];
                    C[i][j + newSize] = c12[i][j];
                    C[i + newSize][j] = c21[i][j];
                    C[i + newSize][j + newSize] = c22[i][j];
                }
            }

            return C;
        }

    }

    public int[] main(Integer arr1[][], Integer arr2[][]) {
        int n = arr1.length;
        ArrayList<ArrayList<Integer>> A = new ArrayList<>(n);
        ArrayList<ArrayList<Integer>> B = new ArrayList<>(n);


        for (int i = 0; i < n; i++) {
            ArrayList<Integer> a = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                int num = arr1[i][j];
                a.add(num);
            }
            A.add(a);
        }

        for (int i = 0; i < n; i++) {
            ArrayList<Integer> b = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                int num = arr2[i][j];
                b.add(num);
            }
            B.add(b);
        }


        int[][] C = Strassen(A, B);
        Log.i("TAG", String.valueOf(C[55][89]));
        int arr[] = new int[C.length * C.length];
        for (int i = 0; i < C.length; i++) {
            for (int j = 0; j < C.length; j++) {
                arr[C.length * i + j] = C[i][j];
            }
        }
        return arr;
    }

}
