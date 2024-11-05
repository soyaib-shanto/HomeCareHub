package com.example.mechanic_zone.ServiceProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.example.mechanic_zone.R;
import com.example.mechanic_zone.CommonClass.UserType;
import com.example.mechanic_zone.Model.ServiceProviderModel.locations_M;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MapsActivity_Mechanic extends AppCompatActivity{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CHECK_LOCATION_SETTINGS = 2;

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Uri imageUri;

    private GoogleMap mMap;
    private Marker currentLocationMarker;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    ImageView imageMenu;
    private FusedLocationProviderClient fusedLocationProviderClient;


    private Double latitude,longitude;
    private DatabaseReference userStatusRef;
    boolean initialLocationReceived = false;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("Mechanic");
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_mechanic);


        //   View extraLayout = LayoutInflater.from(this).inflate(R.layout.drawar_head_layout, null);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100); // Update interval in milliseconds
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                // Get the most recent location from the list
                Location location = locationResult.getLastLocation();

                // Do something with the location
                assert location != null;
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                // Example: Display a toast message with the current coordinates
                // Toast.makeText(MainActivity.this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();

                // Zoom to the current location on the map
                if (!initialLocationReceived) {
                    initialLocationReceived = true;
                    // Zoom to the current location on the map
                    zoomToLocation1();
                }

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    String uid = currentUser.getUid();
                    DatabaseReference locationRef;
                    // Do something with the UID
                    //Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_SHORT).show();
                    locations_M hello1 = new locations_M(latitude,longitude);

                    reference.child(uid).child("location").setValue(hello1);

                }

            }
        };



        //imageView1.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View v) {
              //  chooseImage();
            //}
        //});
      //  TextView uploadTextView = findViewById(R.id.uploadTextView);




        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_View);
        imageMenu = findViewById(R.id.imageMenu);
        toggle = new ActionBarDrawerToggle(MapsActivity_Mechanic.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        View headerView = navigationView.getHeaderView(0);

// Find the ImageView within the header layout
        ImageView headerImageView = headerView.findViewById(R.id.imaage);

        String imagePath = "WIN_20211109_13_24_56_Pro.jpg";
        final Uri[] imageUrl = {null};

        FirebaseStorage.getInstance().getReference().child(imagePath)
                .getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        imageUrl[0] = task.getResult();
                        try {
                            // Define the target dimensions (e.g., 200x200 pixels)

                            Glide.with(MapsActivity_Mechanic.this)
                                    .load(imageUrl[0])
                                    .apply(new RequestOptions()
                                            //.override(targetWidth, targetHeight) // Resize the image
                                            .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable disk caching
                                            .skipMemoryCache(true) // Skip memory caching
                                    )
                                    .into(headerImageView );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Handle the error if image retrieval fails
                        Toast.makeText(MapsActivity_Mechanic.this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
                });


        String uid1 = currentUser.getUid();

        userStatusRef = FirebaseDatabase.getInstance().getReference("Mechanic").child(uid1).child("isOnline");

        // Set the initial value to 1 when the app starts
        userStatusRef.setValue(1);

        // Add a disconnect listener to set the value to 0 when the app goes offline
        userStatusRef.onDisconnect().setValue(0);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {

                    case R.id.mSettings1:
                        Toast.makeText(MapsActivity_Mechanic.this, "About App", Toast.LENGTH_SHORT).show();

                        // startActivity(new Intent(getApplicationContext(), Settings.class));
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.mLogout1:
                        userStatusRef.setValue(0);
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), UserType.class));
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.mAbout1:
                        Toast.makeText(MapsActivity_Mechanic.this, "About App", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;


                }

                return false;
            }
        });

        // App Bar Click Event
        imageMenu = findViewById(R.id.imageMenu);

        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code Here
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.map32, mapFragment)
                .commit();


       // locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;

                // Check for location permission
                if(ContextCompat.checkSelfPermission(MapsActivity_Mechanic.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Permission already granted
                    checkLocationSettings();
                    startLocationUpdates();
                    mMap.setMyLocationEnabled(true);
                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastLocation != null) {
                        double latitude1 = lastLocation.getLatitude();
                        double longitude1 = lastLocation.getLongitude();

                        // Zoom to the current location on the map
                        zoomToLocation(latitude1, longitude1);
                    }
                } else {
                    // Request location permission
                    ActivityCompat.requestPermissions(MapsActivity_Mechanic.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

                }

            }
        });


        FirebaseDatabase.getInstance().getReference(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    userStatusRef.setValue(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });


    }


    @Override
    public void onBackPressed() {
        // Check if the drawer is open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // If drawer is open, close the drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // If drawer is closed, close the app
            finishAffinity();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted
                checkLocationSettings();
                startLocationUpdates();
                if (ContextCompat.checkSelfPermission(MapsActivity_Mechanic.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Permission already granted
                    mMap.setMyLocationEnabled(true);
                }


            } else {
                // Location permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //private void enableMyLocation() {
       // if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
         //       == PackageManager.PERMISSION_GRANTED) {
            //mMap.setMyLocationEnabled(true);
           // if (!initialLocationReceived) {
             //   initialLocationReceived = true;
                // Zoom to the current location on the map
               // zoomToLocation();
            //}

            // Request location updates
            //Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //if (lastLocation != null) {
            //    double latitude1 = lastLocation.getLatitude();
          //      double longitude1 = lastLocation.getLongitude();

                // Zoom to the current location on the map
        //        zoomToLocation(latitude1, longitude1);
      //      }
    //        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        //}
  //  }



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
                                resolvable.startResolutionForResult(MapsActivity_Mechanic.this, REQUEST_CHECK_LOCATION_SETTINGS);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            // Location settings are not satisfied, and the error is not resolvable
                            Toast.makeText(MapsActivity_Mechanic.this, "Location settings are not satisfied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void zoomToLocation1() {
        LatLng currentLocation = new LatLng(latitude, longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15)); // Adjust the zoom level as desired (15 in this example)
    }

    private void zoomToLocation(double latitudee, double longitudee) {
        LatLng currentLocation = new LatLng(latitudee, longitudee);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15)); // Adjust the zoom level as desired (15 in this example)
    }



    private void launchNavigation(double destinationLatitude, double destinationLongitude) {
        String navigationUri = "google.navigation:q=" + destinationLatitude + "," + destinationLongitude;
        Uri gmmIntentUri = Uri.parse(navigationUri);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Google Maps app is not installed", Toast.LENGTH_SHORT).show();
        }
    }


    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
        //    immmg1.setImageURI(imageUri);

            // Upload the image to Firebase Storage
            uploadImageToFirebaseStorage();
        }
    }

    private void uploadImageToFirebaseStorage() {
        if (imageUri != null) {
            //FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            String uidd1 = null;

            if (currentUser != null) {
                // Get the UID of the current user
                uidd1 = currentUser.getUid();
            }

            // FirebaseDatabase Database = FirebaseDatabase.getInstance().getReference("Mechanic");

            // Create a reference to the location where you want to store the image in Firebase Storage
            StorageReference imageReference = storageRef.child( System.currentTimeMillis() + ".jpg");

            // Upload the image to Firebase Storage
            UploadTask uploadTask = imageReference.putFile(imageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Image uploaded successfully, you can now get the download URL
                imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Save the image URL to the Firebase Realtime Database
                    //saveImageUrlToFirebaseDatabase(uri.toString());
                });
            }).addOnFailureListener(e -> {
                // Handle any errors that occurred during the upload process
                Toast.makeText(MapsActivity_Mechanic.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageUrlToFirebaseDatabase(String imageUrl) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Mechanic");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uidd=null;

        if (currentUser != null) {
            // Get the UID of the current user
             uidd = currentUser.getUid();
        }

        databaseReference.child(uidd).child("images").push().setValue(imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // Image URL saved to the database
                    Toast.makeText(MapsActivity_Mechanic.this, "Image URL saved successfully.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occurred during the save operation
                    Toast.makeText(MapsActivity_Mechanic.this, "Failed to save image URL.", Toast.LENGTH_SHORT).show();
                });
    }


    }







