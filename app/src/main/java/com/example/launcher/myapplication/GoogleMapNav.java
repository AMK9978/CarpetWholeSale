package com.example.launcher.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.SharedPreferences;

import com.example.launcher.myapplication.API.APIClient;
import com.example.launcher.myapplication.API.APIInterface;
import com.example.launcher.myapplication.MapUtils.LocationModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.launcher.myapplication.Intro.IntroActivity;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class GoogleMapNav extends Fragment implements
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMarkerClickListener,
        LocationListener {


    private static final int PERMISSIONS_REQUEST_LOCATION = 100;
    private CircleOptions circleOptions = new CircleOptions();
    public static CharSequence TITLE = "مسیریابی";
    private LocationManager locationManager;
    private GoogleMap map;
    private Location MYlocation;
    public static final String TAG = "TAG";
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Circle circle;

    private SharedPreferences sharedPreferences;
    private Marker mPositionMarker;
    private
    List<LocationModel> locationModels = new ArrayList<>();


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Log.i(TAG, "Map loaded in google fragment");

            }
        });
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(false);
        map.setMinZoomPreference(10.0f);
        map.setMaxZoomPreference(20.0f);
        map.setOnMarkerClickListener(this);
        map.setOnCameraIdleListener(this);
        LatLng latLng;
        if (!isAdded()){
            return;
        }
        SharedPreferences myPrefs = getContext().getSharedPreferences("pref", MODE_PRIVATE);
        double longitude = Double.parseDouble(myPrefs.getString("longitude", "51.6677818"));
        double latitude = Double.parseDouble(myPrefs.getString("latitude", "32.6580852"));
        latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        onCameraIdle();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation, container, false);
        MapView mapView = view.findViewById(R.id.mapView);
        ImageView imageView = view.findViewById(R.id.imageButton);
        if (!checkGooglePlayServices()) {
            return view;
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                Log.i(TAG, "click");
                CreateMarkerMap();
            }
        });
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.onResume();

        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                    10, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000,
                    10, this);
//        Log.i(TAG, String.valueOf(MYlocation.getLongitude()));
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        MYlocation = location;
                    }
                }
            };
        } else {
            Toast.makeText(getContext(), "دسترسی باید وارد شود", Toast.LENGTH_SHORT).show();
        }
    }

    private void drawCircle(LatLng point) {

        // Instantiating CircleOptions to draw a circle around the marker
        // Specifying the center of the circle
        circleOptions.center(point);
        if (circle != null) {
            circle.remove();

        }
        // Radius of the circle
        circleOptions.radius(500);

        // Fill color of the circle
        circleOptions.strokeWidth(0)
                .strokeColor(Color.parseColor("#22463CF5"))
                .fillColor(Color.parseColor("#22463CF5"));
        // Adding the circle to the GoogleMap
        circle = map.addCircle(circleOptions);
        Log.i("TAGGGGGGGGGGG", "In drawing");

    }

    private boolean checkGooglePlayServices() {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        if (status != ConnectionResult.SUCCESS) {
            Log.e(TAG, GooglePlayServicesUtil.getErrorString(status));

            // ask user to update google play services.
            SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
            alertDialog.setContentText("گوگل پلی شما آپدیت نیست!");
            alertDialog.setTitle("خطا در Google play");
            alertDialog.show();
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getContext(), 1);
            dialog.show();
            return false;
        } else {
            Log.i(TAG, GooglePlayServicesUtil.getErrorString(status));
            // google play services is updated.
            //your code goes here...
            return true;
        }
    }

    /**
     * check is permission granted or not
     *
     * @return true or false.
     */
    private boolean isPermissionGranted() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    /**
     * for tracking gps we most create locationManager Class.
     * this method do for us
     */
    private void CreateLocationManger() {
//        if (locationManager == null) {
//            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//            locationRequest = Utility.createLocationRequest();
//        }
    }

    /**
     * check is gps on or not
     */
    private boolean isGPSOn() {
        CreateLocationManger();
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param location variable for change camera map position
     */
    private void MoveCameratoLocation(Location location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
    }
    private void MoveCameratoLocation(LatLng location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }


    private void CreateMarkerMap() {
        map.clear();
        locationModels = new ArrayList<>();
        if (MYlocation != null) {
            map.clear();
            drawCircle(new LatLng(MYlocation.getLatitude(), MYlocation.getLongitude()));
            BitmapDrawable bitmapdraw = (BitmapDrawable) getActivity().getResources().getDrawable(R.drawable.location_curs);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(MYlocation.getLatitude(), MYlocation.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            map.addMarker(markerOptions);
            Log.i("TAG", "Marker added");
        }
        APIInterface apiInterface = APIClient.getRetrofit().create(APIInterface.class);
        Call<List<LocationModel>> call = apiInterface.getLocations();
        call.enqueue(new Callback<List<LocationModel>>() {
            @Override
            public void onResponse(Call<List<LocationModel>> call, Response<List<LocationModel>> response) {
                locationModels = response.body();
                for (int i = 0; i < locationModels.size(); i++) {
                    LocationModel locationModel = locationModels.get(i);
                    locationModel.setLng(new LatLng(locationModel.getLatLang().get(0),locationModel.getLatLang().get(1)));
                    Log.i("TAGOOO", locationModels.get(i).getName() + " "  + locationModels.get(i).getLng().longitude);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(locationModel.getLng());
                    markerOptions.title(locationModel.getName());
                    map.addMarker(markerOptions);
                }
            }

            @Override
            public void onFailure(Call<List<LocationModel>> call, Throwable t) {
                Log.i("TAG", t.getMessage());
            }
        });

    }


    private void getLocation() {
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 5, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, this);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        MYlocation = location;

        Log.i(TAG, String.valueOf(location.getLongitude()));
        if (location == null || MYlocation == null || !isAdded() || map == null)
            return;

        if (mPositionMarker == null) {
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.location_curs);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(MYlocation.getLatitude(), MYlocation.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            map.addMarker(markerOptions);
            Log.i("TAG", "Marker added");
        }
        // animation
        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
                location.getLongitude())));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onCameraIdle() {
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!isGPSOn()) {
            final SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
            alertDialog.setTitle("خطا در دریافت مکان");
            alertDialog.setContentText("لطفا GPS و دسترسی به اینترنت خود را روشن کنید");
            alertDialog.setConfirmText("باشه");
            alertDialog.show();
            alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if (isGPSOn()) {
                        alertDialog.dismiss();
                    }
                }
            });
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new
                    String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                    10, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000,
                    10, this);
//        Log.i(TAG, String.valueOf(MYlocation.getLongitude()));
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        MYlocation = location;
                    }
                }
            };
        }
    }

}
