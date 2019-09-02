package com.example.launcher.myapplication;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Navigation extends Fragment implements OnMapReadyCallback {

    public static CharSequence TITLE = "مسیریابی";
    private GoogleMap mMap;
    int graph_size = 93, matrix[][];
    MapPoint[] mapPoints = new MapPoint[graph_size];
    LinkedList<Integer> adjListArray[];
    int finished = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation, container, false);
//        MapView mapView = view.findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this);
//        mapView.onResume();
        return view;
    }

    private void preProcess() {
        // TODO: Complete this method in order to allocate markets and make the graph to using algorithms
        matrix = new int[graph_size][graph_size];
        adjListArray = new LinkedList[graph_size];
        for (int i = 0; i < graph_size; i++) {
            adjListArray[i] = new LinkedList<>();
        }
        readFile();
    }

    private void readFile() {
        File file = new File("assets/coordinates.txt");
        int i = 0;
        try {
            InputStream inputStream = getContext().getAssets().open(file.getName());
            StringBuilder stringBuilder = new StringBuilder();
            int ch;
            while ((ch = inputStream.read()) != -1) {
                stringBuilder.append((char) ch);
                if ((char) ch == '\n') {
                    Log.e("TAG", i + ", " + stringBuilder.toString());
                    String line[] = stringBuilder.toString().trim().split(",");
                    boolean isBranch = line[0].trim().length() > 2;
                    String name = line[1].trim();
                    Double latitude = Double.valueOf(line[2].trim());
                    Double longitude = Double.valueOf(line[3].trim());
                    MapPoint mapPoint = new MapPoint(i, new LatLng(latitude, longitude), isBranch);
                    if (name.length() > 2) {
                        mapPoint.name = name;
                    }
                    mapPoints[i] = mapPoint;
                    String arr = stringBuilder.toString().trim().replaceAll(".*\\[", "");
                    arr = arr.replaceAll("].*", "");
                    String[] neighbours = arr.split(",");
                    for (int j = 0; j < neighbours.length; j++) {
                        matrix[i][Integer.parseInt(neighbours[j])] = 1;
                        matrix[Integer.parseInt(neighbours[j])][i] = 1;
                        adjListArray[i].add(Integer.valueOf(neighbours[j]));
                        adjListArray[Integer.parseInt(neighbours[j])].add(i);
                    }
                    stringBuilder.setLength(0);
                    i++;
                }
            }
            inputStream.close();
        } catch (Exception e) {
            Log.e("TAG", String.valueOf(i));
            e.printStackTrace();
        }


    }

    private void getBestWay(double latitude, double longitude) {
        double min = 99999999;
        int index = 0;
        MapPoint mapPoint = new MapPoint(99, new LatLng(latitude, longitude), false);
        for (int i = 0; i < mapPoints.length; i++) {
            double x = Math.sqrt(Math.pow((mapPoints[i].coordinates.latitude - mapPoint.coordinates.latitude), 2)
                    + Math.pow(mapPoint.coordinates.longitude - mapPoints[i].coordinates.longitude, 2));
            if (min > x) {
                min = x;
                index = i;
            }
        }
        DrawLine(mapPoints[index].coordinates, mapPoint.coordinates);
        finished = 0;
        BFS(mapPoints[index], new ArrayList<MapPoint>());
    }

    int counter = 0;

    private void DrawLine(LatLng coordinates, LatLng coordinates1) {
        if (counter % 2 == 0) {
            mMap.addPolyline(new PolylineOptions().add(coordinates, coordinates1).width(5).color(Color.GREEN));
        } else {
            mMap.addPolyline(new PolylineOptions().add(coordinates, coordinates1).width(5).color(Color.RED));
        }
    }

    private void DrawAllLines() {
        for (int j = 0; j < matrix.length; j++) {
            for (int k = 0; k < matrix[0].length; k++) {
                if (matrix[j][k] == 1) {
                    mMap.addPolyline(new PolylineOptions().add(new LatLng
                                    (mapPoints[j].coordinates.latitude, mapPoints[j].coordinates.longitude),
                            new LatLng(mapPoints[k].coordinates.latitude, mapPoints[k].coordinates.longitude))
                            .width(5).color(Color.BLUE));
                }
            }
        }
    }

    class MapPoints {
        MapPoint mapPoint;
        ArrayList<MapPoint> mapPoints;

        MapPoints(MapPoint mapPoint, ArrayList<MapPoint> mapPoints) {
            this.mapPoint = mapPoint;
            this.mapPoints = mapPoints;
        }
    }

    private void BFS(MapPoint mapPoint, ArrayList<MapPoint> arrayList) {
        Queue<MapPoints> queue = new LinkedList<>();
        ArrayList<Integer> visited = new ArrayList<>();
        queue.add(new MapPoints(mapPoint, arrayList));
        while (!queue.isEmpty()) {
            MapPoints points = queue.poll();
            visited.add(points.mapPoint.index);
            arrayList = new ArrayList<>(points.mapPoints);
            arrayList.add(points.mapPoint);
            if (points.mapPoint.isBranch) {
                for (int i = 0; i < arrayList.size() - 1; i++) {
                    Log.i("points", arrayList.get(i) + " >> " + arrayList.get(i + 1));
                    DrawLine(arrayList.get(i).coordinates, arrayList.get(i + 1).coordinates);
                }
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(arrayList.get(arrayList.size() - 1).coordinates).title("مقصد");
                mMap.addMarker(markerOptions);
//                DrawAllLines();
                return;
            } else {
                int past = queue.size();
                for (int i = 0; i < matrix[points.mapPoint.index].length; i++) {
                    if (visited.contains(i)) {
                        continue;
                    }
                    if (matrix[points.mapPoint.index][i] == 1) {
                        MapPoints points1 = new MapPoints(mapPoints[i], arrayList);
                        queue.add(points1);
                    } else if (matrix[i][points.mapPoint.index] == 1) {
                        MapPoints points1 = new MapPoints(mapPoints[i], arrayList);
                        queue.add(points1);
                    }
                }
                if (past == queue.size()) {
                    arrayList.remove(points.mapPoint);
                }
            }
        }
        Log.i("points", "Not finished");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        // Add a marker in Isfahan and move the camera
        LatLng isfahan = new LatLng(32.65246, 51.67462);
        mMap.addMarker(new MarkerOptions().position(isfahan).title("Marker in Isfahan"));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(isfahan, 15));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("مبدا");
                markerOptions.draggable(true);
                mMap.clear();
                mMap.addMarker(markerOptions);
                getBestWay(markerOptions.getPosition().latitude, markerOptions.getPosition().longitude);
            }
        });
        preProcess();

    }

    class MapPoint {
        int index;
        LatLng coordinates;
        boolean isBranch;
        String name;

        MapPoint(int index, LatLng coordinates, boolean isBranch) {
            this.index = index;
            this.coordinates = coordinates;
            this.isBranch = isBranch;
        }

        @Override
        public String toString() {
            return "MapPoint{" +
                    "index=" + index +
                    ", coordinates=" + coordinates +
                    ", isBranch=" + isBranch +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
