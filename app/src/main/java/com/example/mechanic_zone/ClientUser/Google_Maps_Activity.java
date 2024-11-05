package com.example.mechanic_zone.ClientUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mechanic_zone.R;
import com.example.mechanic_zone.Model.ServiceProviderModel.locations_M;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Google_Maps_Activity extends AppCompatActivity implements OnMapReadyCallback {


     GoogleMap mMap;
     String name1,title1,phone1,active="0";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CHECK_LOCATION_SETTINGS = 2;
    //private GoogleMap mMap;
   // private Marker currentLocationMarker;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Double latitude,longitude;
    private FusedLocationProviderClient fusedLocationClient;


    // ActivityGoogleMapsBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                    if (currentUser != null) {
                        String uid = currentUser.getUid();
                        locations_M hello1 = new locations_M(latitude, longitude);
                        DatabaseReference locationRef;
                        // Do something with the UID
                        //Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_SHORT).show();

                        reference.child(uid).child("location").setValue(hello1);

                    } else {
                        // User is not logged in
                        Toast.makeText(getApplicationContext(), "sorry", Toast.LENGTH_SHORT).show();

                    }
                    // addMarker(latitude,longitude);

                }
                else Toast.makeText(getApplicationContext(), "sorry Toast", Toast.LENGTH_SHORT).show();

                // 24.067105,90.000590
                locationManager.removeUpdates(this); // Stop receiving location updates after the first one
               // double destinationLatitude = 24.067105; // Replace with the actual destination latitude
                //double destinationLongitude = 90.000590; // Replace with the actual destination longitude
               // launchNavigation(destinationLatitude, destinationLongitude);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

      //  reference.addValueEventListener(new ValueEventListener() {
        //    @Override
          //  public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
            //    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
              //      String uid = snapshot.getKey();
                    // Access the data for each UID here
                    // For example, to retrieve a specific value, you can use:

                   // String uid = snapshot.child("title").getValue(String.class);
              //      Double Latt = snapshot.child("location").child("latt").getValue(Double.class);
                //    Double Longg = snapshot.child("location").child("longg").getValue(Double.class);

                  //  LatLng santosh = new LatLng(Latt, Longg);
                    //mMap.addMarker(new MarkerOptions().position(santosh).title(uid));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(santosh));

                //}
            //}

            //@Override
            //public void onCancelled(DatabaseError error) {
                // Failed to read value
              //  Log.e("Firebase", "Failed to read data", error.toException());
            //}

      //  });

      //  mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
         //      @Override
           // public boolean onMarkerClick(@NonNull Marker marker) {

             //     String uid = marker.getTitle();


               //   reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                 //     @Override
                   //   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          // Handle the retrieved data
                     //     if (dataSnapshot.exists()) {
                              // User data exists for the given UID
                       //        name1 = dataSnapshot.child("name").getValue(String.class);
                         //      title1 = dataSnapshot.child("title").getValue(String.class);
                           //    phone1 = dataSnapshot.child("phone").getValue(String.class);
                             // SharedPreferences sharedPreferences = getSharedPreferences("code", Context.MODE_PRIVATE);
                              //SharedPreferences.Editor editor = sharedPreferences.edit();

                              //editor.putString("name", name1);
                              //editor.putString("title",title1);
                              //editor.putString("phone",phone1);
                              //editor.apply();
                            //  BottomFragment bottomSheetDialog = new BottomFragment();
                          //    bottomSheetDialog.show(getSupportFragmentManager(),"uid");
                        //  }


                          //else {
                              // User data does not exist for the given UID
                              //Log.d("TAG", "User data not found for UID: " + uid);
                          //}
                      //}

                      //@Override
                    //  public void onCancelled(@NonNull DatabaseError databaseError) {
                          // Handle database error
                  //        Log.d("TAG", "Database Error: " + databaseError.getMessage());
                //      }
              //    });


            //      return false;
          //  }
       /// });


       // mMap = map;

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            checkLocationSettings();

            // Enable the My Location layer and move the camera to the current location
            enableMyLocation();
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            checkLocationSettings();
            enableMyLocation();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  checkLocationSettings();
                // Location permission granted, enable My Location layer and move camera
                enableMyLocation();
            }
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            // Request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


        }
    }



    private void checkLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build())
                .addOnCompleteListener(task -> {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        // Location settings are satisfied, proceed with location updates
                        //  startLocationUpdates();
                    } catch (ApiException exception) {
                        int statusCode = exception.getStatusCode();
                        if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                            // Location settings are not satisfied, but can be fixed by showing the user a dialog
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(Google_Maps_Activity.this, REQUEST_CHECK_LOCATION_SETTINGS);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            // Location settings are not satisfied, and the error is not resolvable
                            Toast.makeText(Google_Maps_Activity.this, "Location settings are not satisfied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}