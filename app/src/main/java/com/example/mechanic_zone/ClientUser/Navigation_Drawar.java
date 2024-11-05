package com.example.mechanic_zone.ClientUser;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mechanic_zone.R;
import com.example.mechanic_zone.CommonClass.UserType;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Navigation_Drawar extends AppCompatActivity {


    private EditText searchEditText;
    private TextView searchResultsTextView;
    private Handler searchHandler = new Handler();
    private Runnable searchRunnable;
    private DatabaseReference mechanicReference;


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CHECK_LOCATION_SETTINGS = 2;
    private LocationManager locationManager;


    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    boolean initialLocationReceived = false;



    Double latitude, longitude;
    private DrawerLayout drawerLayout;
    GoogleMap mMap;
    String name1, title1, phone1;
    ImageView imageMenu;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("Mechanic");
    private final HashMap<String, Marker> mechanicMarkers = new HashMap<>();
    ActionBarDrawerToggle toggle;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawar);

        searchEditText = findViewById(R.id.editTextSearch);
        searchResultsTextView = findViewById(R.id.searchResultDisplay);
        // Initialize the Firebase Realtime Database reference for the "Mechanic" node
        mechanicReference = FirebaseDatabase.getInstance().getReference().child("Mechanic");

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                searchHandler.removeCallbacks(searchRunnable);

                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        String searchTerm = charSequence.toString().trim();
                        performNameSearch(searchTerm);
                    }
                };

                searchHandler.postDelayed(searchRunnable, 500);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // String searchTerm = editable.toString().trim();
                //performSearch(searchTerm.toLowerCase());
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100); // Update interval in milliseconds
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {

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

            }
        };


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_View);
        imageMenu = findViewById(R.id.imageMenu);
        toggle = new ActionBarDrawerToggle(Navigation_Drawar.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



// Find the header layout view within the NavigationView


        navigationView.setNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.mSettings:
                    startActivity(new Intent(getApplicationContext(), Settings.class));
                    drawerLayout.closeDrawers();
                    break;

                case R.id.mLogout:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), UserType.class));
                    drawerLayout.closeDrawers();
                    break;

                case R.id.mAbout:
                    Toast.makeText(Navigation_Drawar.this, "About App", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawers();
                    break;

                case R.id.mActiveperson:
                    startActivity(new Intent(getApplicationContext(), ActivePerson.class));
                    drawerLayout.closeDrawers();
                    break;


            }

            return false;
        });

        // App Bar Click Event
        imageMenu = findViewById(R.id.imageMenu);

        imageMenu.setOnClickListener(view -> {
            // Code Here
            drawerLayout.openDrawer(GravityCompat.START);
        });

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mapFragment)
                .commit();

        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;

            // Check for location permission
            if(ContextCompat.checkSelfPermission(Navigation_Drawar.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                ActivityCompat.requestPermissions(Navigation_Drawar.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
              //  mMap.setMyLocationEnabled(true);

            }

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String uid = snapshot.getKey();
                        // Access the data for each UID here
                        // For example, to retrieve a specific value, you can use:

                        Double Latt = snapshot.child("location").child("latt").getValue(Double.class);
                        Double Longg = snapshot.child("location").child("longg").getValue(Double.class);
                        Integer isOnline = snapshot.child("isOnline").getValue(Integer.class);


                        LatLng santosh = new LatLng(Latt, Longg);


                        if(isOnline == 1) {

                            if (mechanicMarkers.containsKey(uid)) {
                                // Update the position of the existing marker
                                Marker marker = mechanicMarkers.get(uid);
                                 assert marker != null;
                                 marker.setPosition(santosh);
                            } else {
                                // Create a new marker and add it to the map and the HashMap
                                Marker marker = mMap.addMarker(new MarkerOptions().position(santosh).title(uid).icon(BitmapDescriptorFactory.fromResource(R.mipmap.sign1)));
                                 mechanicMarkers.put(uid, marker);
                            }
                        }

                        else {
                            if (mechanicMarkers.containsKey(uid)) {
                                // Remove the marker from the map and the HashMap
                                Marker marker = mechanicMarkers.get(uid);
                                assert marker != null;
                                marker.remove();
                                mechanicMarkers.remove(uid);
                            }
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.e("Firebase", "Failed to read data", error.toException());
                }

            });

            //  zoomToLocation();

            mMap.setOnMarkerClickListener(marker -> {

                String uid = marker.getTitle();

                assert uid != null;
                reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Handle the retrieved data
                        if (dataSnapshot.exists()) {
                            // User data exists for the given UID
                            name1 = dataSnapshot.child("name").getValue(String.class);
                            title1 = dataSnapshot.child("title").getValue(String.class);
                            phone1 = dataSnapshot.child("phone").getValue(String.class);

                            SharedPreferences sharedPreferences = getSharedPreferences("code", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("name", name1);
                            editor.putString("title", title1);
                            editor.putString("phone", phone1);
                            editor.apply();

                            BottomFragment bottomSheetDialog = new BottomFragment();
                            bottomSheetDialog.show(getSupportFragmentManager(), "uid");

                        } else {
                            // User data does not exist for the given UID
                            Log.d("TAG", "User data not found for UID: " + uid);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Failed to read value
                        Log.e("Firebase", "Failed to read data", databaseError.toException());
                    }
                });
                return false;
            });

        });

    }



    private void performNameSearch(String searchTerm) {
        // Create a list to store the results

        if (searchTerm.isEmpty()) {
            // Clear the search results and set an empty text in the TextView
            searchResultsTextView.setText("Search by name and title");
        }

        else{

        Set<String> results = new HashSet<>(); // Using Set to avoid duplicates

        // Query by name
        Query queryByName = mechanicReference.orderByChild("name").startAt(searchTerm).endAt(searchTerm + "\uf8ff");
        queryByName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    // Convert the name to lowercase for case-insensitive comparison
                    if (name != null) {
                        String name1 = name.toLowerCase();
                        if (name1.contains(searchTerm)) {
                            String uid = snapshot.getKey(); // Get the UID associated with the name
                            results.add(uid);
                        }
                    }
                }
                // Continue searching for other fields
                performTitleSearch(searchTerm, results);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                searchResultsTextView.setText("Search failed. Please try again later.");
            }
        });

    }
    }


    private void performTitleSearch(String searchTerm, Set<String> results) {
        // Query by phone

        Query queryByTitle = mechanicReference.orderByChild("title").startAt(searchTerm).endAt(searchTerm + "\uf8ff");
        queryByTitle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String Titlename = snapshot.child("title").getValue(String.class);
                    // Convert the name to lowercase for case-insensitive comparison
                    if (Titlename != null) {
                        String phoneLower = Titlename.toLowerCase();
                        if (phoneLower.contains(searchTerm)) {
                            String uid = snapshot.getKey(); // Get the UID associated with the name
                            results.add(uid);
                        }
                    }
                }
                // Display the combined search results
                displaySearchResults(new ArrayList<>(results));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                searchResultsTextView.setText("Search failed. Please try again later.");
            }
        });
    }

    private void displaySearchResults(List<String> results) {
        if (results.isEmpty()) {
            searchResultsTextView.setText("No results found.");
        } else {
            SpannableStringBuilder resultText = new SpannableStringBuilder();
            final int[] startIndex = {0}; // Start index for each row

            for (String uid : results) {
                // Retrieve data associated with the UID
                mechanicReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String title = dataSnapshot.child("title").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String phone = dataSnapshot.child("phone").getValue(String.class);

                        // Create a row
                        String row = "Name: " + name + ",  Title: " + title + ",  Email: " + email + ",  Phone: " + phone;

                        // Append the row to the resultText
                        resultText.append(row).append("\n\n");

                        // Create a ClickableSpan for the entire row
                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                // Handle the click action here
                                // For example, you can open the SearchResult activity
                                Intent intent = new Intent(Navigation_Drawar.this, ResultOfSearch.class);

                                String message = uid;
                                intent.putExtra("code", message);

                                startActivity(intent);
                            }
                        };

                        // Set the ClickableSpan for the entire row
                        resultText.setSpan(clickableSpan, startIndex[0], startIndex[0] + row.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        // Update the startIndex for the next row
                        startIndex[0] += row.length() + 1; // +1 for the newline character

                        // Update the searchResultsTextView
                        searchResultsTextView.setText(resultText);
                        searchResultsTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        searchResultsTextView.setText("Search failed. Please try again later.");
                    }
                });
            }
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted
                checkLocationSettings();
                startLocationUpdates();
                if (ContextCompat.checkSelfPermission(Navigation_Drawar.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Permission already granted
                    mMap.setMyLocationEnabled(true);
                }


            } else {
                // Location permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void zoomToLocation1() {

        LatLng currentLocation = new LatLng(latitude, longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12), 400, null);
        // }
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
                                resolvable.startResolutionForResult(Navigation_Drawar.this, REQUEST_CHECK_LOCATION_SETTINGS);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            // Location settings are not satisfied, and the error is not resolvable
                            Toast.makeText(Navigation_Drawar.this, "Location settings are not satisfied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void zoomToLocation(double latitudee, double longitudee) {
        LatLng currentLocation = new LatLng(latitudee, longitudee);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12)); // Adjust the zoom level as desired (15 in this example)
    }


}