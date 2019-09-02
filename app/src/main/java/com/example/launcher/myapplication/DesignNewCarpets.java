package com.example.launcher.myapplication;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;

public class DesignNewCarpets extends Fragment {
    public static String TITLE = "طراحی فرش جدید";
    Button submit, submit_neighbouring, set;
    TextView result;
    EditText neighbour1, neighbour2, areas;
    int graph_size = 0, matrix[][];
    int needed_colors = 0;
    LinkedList<Integer> adjListArray[];
    private boolean be_set = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.design_new_carpet, container, false);
        submit = view.findViewById(R.id.submit);
        submit_neighbouring = view.findViewById(R.id.submit_neigbouring);
        result = view.findViewById(R.id.result);
        set = view.findViewById(R.id.set);
        areas = view.findViewById(R.id.areas_count);
        neighbour1 = view.findViewById(R.id.neighbour1);
        neighbour2 = view.findViewById(R.id.neighbour2);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areas.getText().toString().equals("") || areas.getText().toString().equals("0")) {
                    return;
                }
                if (!be_set) {
                    be_set = true;
                    areas.setEnabled(be_set);
                    set.setText("تنظیم");
                }else{
                    be_set = false;
                    set.setText("طرح جدید");
                    areas.setEnabled(be_set);
                }
                graph_size = Integer.parseInt(areas.getText().toString());
                matrix = new int[graph_size][graph_size];
                adjListArray = new LinkedList[graph_size];
                for (int i = 0; i < graph_size; i++) {
                    adjListArray[i] = new LinkedList<>();
                }
            }
        });
        submit_neighbouring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (neighbour1.getText().toString().equals("") || neighbour2.getText().toString().equals("")) {
                    return;
                }
                int node1 = Integer.valueOf(neighbour1.getText().toString());
                int node2 = Integer.valueOf(neighbour2.getText().toString());
                if (node1 >= graph_size || node2 >= graph_size) {
                    return;
                }
                joinNodes(node1, node2);
                Toast.makeText(getContext(),"ناحیه های  " + node1 + " و ناحیه " + node2
                        + " وصل شدند",Toast.LENGTH_SHORT).show();
                neighbour1.setText("");
                neighbour2.setText("");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (graph_size == 0) {
                    return;
                }
                getAnswer();
                be_set = true;
                areas.setEnabled(be_set);
                set.setText("طرح جدید");
                areas.setText("");
            }
        });
        return view;
    }

    private void getAnswer() {
        result.clearComposingText();
        Hashtable<Integer, Integer> hashtable = greedyColoring();
        Hashtable<Integer, String> getColors = new Hashtable<>();
        {
            int i = 0;
            getColors.put(i, "سبز");
            getColors.put(i + 1, "قرمز");
            getColors.put(i + 2, "آبی");
            getColors.put(i + 3, "بنفش");
            getColors.put(i + 4, "صورتی");
            getColors.put(i + 5, "سیاه");
            getColors.put(i + 6, "سفید");
            getColors.put(i + 7, "نارنجی");
            getColors.put(i + 8, "سورمه ای");
            getColors.put(i + 9, "زرد");
        }
        StringBuilder first_line = new StringBuilder("تعداد رنگ مورد نیاز:" + needed_colors + "\n");
        result.setText(first_line.toString());
        for (int i = 0; i < graph_size; i++) {
            first_line.append("ناحیه ").append(i).append(" :").append(getColors.get(hashtable.get(i)))
                    .append("\n");
        }
        result.setText(first_line.toString());
    }

    private void joinNodes(Integer node1, Integer node2) {
        if (!adjListArray[node1].contains(node2))
            adjListArray[node1].add(node2);
        if (!adjListArray[node2].contains(node1))
            adjListArray[node2].add(node1);
        matrix[node1][node2] = matrix[node2][node1] = 1;
    }

    private Hashtable<Integer, Integer> greedyColoring() {
        int result[] = new int[graph_size];
        Hashtable<Integer, Integer> hashtable = new Hashtable<>();
        Arrays.fill(result, -1);
        result[0] = 0;
        boolean available[] = new boolean[graph_size];
        Arrays.fill(available, true);
        for (int u = 1; u < graph_size; u++) {
            for (Integer i : adjListArray[u]) {
                if (result[i] != -1)
                    available[result[i]] = false;
            }
            int cr;
            for (cr = 0; cr < graph_size; cr++) {
                if (available[cr])
                    break;
            }

            result[u] = cr;
            Arrays.fill(available, true);
        }

        // print the result
        HashSet<Integer> colors = new HashSet<>();
        for (int u = 0; u < graph_size; u++) {
            System.out.println("Vertex " + u + " --->  Color " + result[u]);
            hashtable.put(u, result[u]);
            colors.add(result[u]);
        }
        needed_colors = colors.size();
        return hashtable;
    }
}
