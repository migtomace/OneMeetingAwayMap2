package com.example.lmnop.onemeetingawaymap1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lmnop.onemeetingawaymap1.Adapters.ListMeetingsAdapter;
import com.example.lmnop.onemeetingawaymap1.DataBase.DataSource;
import com.example.lmnop.onemeetingawaymap1.DataBase.MeetingsTable;
import com.example.lmnop.onemeetingawaymap1.model.DataItemMeetings;
import com.example.lmnop.onemeetingawaymap1.sample.SampleDataProvider;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{

    private static final String TAG = "MapsActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermissionsGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15;

    public List<DataItemMeetings> dataItemMeetingsList;
    DataSource mDataSource;

    public String Meeting = "2807 w viewmont way w, settle wa 98199";
    public String Name = "My old house";
    public GoogleMap mMap;


    public FusedLocationProviderClient fusedLocationProviderClient;


    private RecyclerView listRecyclerView;
    private ListMeetingsAdapter listAdapter;
    public ArrayList<DataItemMeetings> meetingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_meetings_list);
        meetingList= new ArrayList<>();


        listRecyclerView=(RecyclerView) findViewById(R.id.recyclerview);
        listRecyclerView.setHasFixedSize(true);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ListMeetingsAdapter(this, meetingList);
        listRecyclerView.setAdapter(listAdapter);
        /*setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dataItemMeetingsList = SampleDataProvider.dataItemMeetingsList;
        mDataSource = new DataSource(this);
        mDataSource.open();
        mDataSource.seedDataBase(dataItemMeetingsList);

        getLocationPermission();
        */
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }

        new LongRunningTask().execute();



    }

//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//        if (hasCapture){
//
//            if (dataItemMeetingsList.size() > 0){
//                //iterate through meetings arraylist
//                for (int i = 0; i < dataItemMeetingsList.size(); i++){
//
//                    //set meeting
//                    DataItemMeetings dataItemMeetings = dataItemMeetingsList.get(i);
//
//                    //set marker
//                    LatLng marker = new LatLng(Double.parseDouble(dataItemMeetings.getLat()), Double.parseDouble(dataItemMeetings.getLng()));
//
//                    findMarkers(marker, dataItemMeetings);
//
//                }
//            }
//
//        }
//
//    }



    private class LongRunningTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //start loading icon
            //let user know something will happen
            Log.d(TAG, "onPreExecute: Before Task");
        }


        @Override
        protected Void doInBackground(Void... voids) {
            //AWS network call
            Log.d(TAG, "doInBackground: AWS CALL");

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //your code
                    // Add a marker in Sydney and move the camera
                    LatLng seattle = new LatLng(47.608013, -122.335167);
                    mMap.addMarker(new MarkerOptions().position(seattle).title("Marker in Seattle"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(seattle));

                    List<DataItemMeetings> meet = mDataSource.getPins();
                    Location location = new Location("target");
                    LatLng camPos = mMap.getCameraPosition().target;
                    location.setLatitude(camPos.latitude);
                    location.setLongitude(camPos.longitude);
                    Location target = new Location("target");
                    for (int i = 0; i < meet.size(); i++) {
                        String slat = meet.get(i).getLat();
                        String slng = meet.get(i).getLng();
                        double lat = Double.parseDouble(slat);
                        double lng = Double.parseDouble(slng);
                        target.setLatitude(lat);
                        target.setLongitude(lng);
//                        if (location.distanceTo(target) < 10000) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lng, lat))
                                    .title(meet.get(i).getAddress()));
//                        }

                    }

//                    mMap.setOnCameraMoveListener(MapsActivity.this);


                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //update UI
            Log.d(TAG, "onPostExecute: After Task");
        }
    }


//    //check if LatLng is withing view
//    private void findMarkers(LatLng marker, DataItemMeetings dataItemMeetings) {
//        Location location = new Location("target");
//        LatLng camPos = mMap.getCameraPosition().target;
//        location.setLatitude(camPos.latitude);
//        location.setLongitude(camPos.longitude);
//        Location target = new Location("target");
//        target.setLatitude(marker.latitude);
//        target.setLongitude(marker.longitude);
//        if (location.distanceTo(target) < 2000) {
//            mMap.addMarker(new MarkerOptions().position(marker).title(dataItemMeetings.getMeetingName()));
//        }
//    }


    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {

                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }


    //method to move the camera
    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION,
                COURSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    //initialize map
                    initMap();
                }
            }
        }
    }






//    @Override
//    public void onCameraMove() {
//
//        if (dataItemMeetingsList.size() > 0){
//            //iterate through meetings arraylist
//            for (int i = 0; i < dataItemMeetingsList.size(); i++){
//
//                //set meeting
//                DataItemMeetings dataItemMeetings = dataItemMeetingsList.get(i);
//
//                //set marker
//                LatLng marker = new LatLng(Double.parseDouble(dataItemMeetings.getLat()), Double.parseDouble(dataItemMeetings.getLng()));
//
//                findMarkers(marker, dataItemMeetings);
//
//            }
//        }
//  }


}
