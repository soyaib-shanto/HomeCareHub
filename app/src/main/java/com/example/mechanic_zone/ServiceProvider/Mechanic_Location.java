package com.example.mechanic_zone.ServiceProvider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class Mechanic_Location {

    private DatabaseReference locationRef;

    public Mechanic_Location() {
        // Get a reference to the database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        locationRef = firebaseDatabase.getReference("users");
    }

    public void storeLocation(String userId, double latitude, double longitude) {
        // Create a new child node using a timestamp as the key
        DatabaseReference newLocationRef = locationRef.child(userId).child("locations").push();

        // Set the location data
        newLocationRef.setValue(new LocationData(latitude, longitude, ServerValue.TIMESTAMP));
    }

    private static class LocationData {
        private double latitude;
        private double longitude;
        private Object timestamp;

        public LocationData(double latitude, double longitude, Object timestamp) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.timestamp = timestamp;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public Object getTimestamp() {
            return timestamp;
        }
    }

}
