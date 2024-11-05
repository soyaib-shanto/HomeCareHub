package com.example.mechanic_zone.ClientUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mechanic_zone.R;
import com.example.mechanic_zone.ClientUser.ResultOfSearch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PartialSearch extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private TextView searchResultsTextView;
    private DatabaseReference mechanicReference;

    private Handler searchHandler = new Handler();
    private Runnable searchRunnable;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partial_search);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        searchResultsTextView = findViewById(R.id.searchResultsTextView);

        // Initialize the Firebase Realtime Database reference for the "Mechanic" node
        mechanicReference = FirebaseDatabase.getInstance().getReference().child("Mechanic");

        // Attach a TextWatcher to the searchEditText for real-time updates
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                searchHandler.removeCallbacks(searchRunnable);

                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        String searchTerm = charSequence.toString().trim();
                        String hintText = searchEditText.getHint().toString().trim();

                        if (searchTerm.isEmpty() && hintText.isEmpty()) {

                            searchResultsTextView.setText("data Not found");
                            // The search bar is initially empty (contains only the hint text)
                            // You can handle this case here
                        } else {
                            performSearch(searchTerm);
                            // The search bar is currently empty (no hint text)
                            // You can handle this case here
                        }
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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = searchEditText.getText().toString().trim();
                performSearch(searchTerm.toLowerCase());
            }
        });
    }

    private void performSearch(String searchTerm) {
        // Check if the search term is empty
        if (searchTerm.isEmpty()) {
            // Clear the search results and set an empty text in the TextView
            searchResultsTextView.setText("hello");
        } else {
            // Create a list to store the results
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
                            String phoneLower = name.toLowerCase();
                            if (phoneLower.contains(searchTerm)) {
                                String uid = snapshot.getKey(); // Get the UID associated with the name
                                results.add(uid);
                            }
                        }
                    }
                    // Continue searching for other fields
                    performEmailSearch(searchTerm, results);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    searchResultsTextView.setText("Search failed. Please try again later.");
                }
            });
        }
    }


    private void performEmailSearch(String searchTerm, Set<String> results) {
        // Query by email

        Query queryByEmail = mechanicReference.orderByChild("email").startAt(searchTerm).endAt(searchTerm + "\uf8ff");
        queryByEmail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String emailName = snapshot.child("email").getValue(String.class);
                    // Convert the name to lowercase for case-insensitive comparison
                    if (emailName != null) {
                        String phoneLower = emailName.toLowerCase();
                        if (phoneLower.contains(searchTerm)) {
                            String uid = snapshot.getKey(); // Get the UID associated with the name
                            results.add(uid);
                        }
                    }
                }
                // Continue searching for other fields
                performPhoneSearch(searchTerm, results);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                searchResultsTextView.setText("Search failed. Please try again later.");
            }
        });
    }

    private void performPhoneSearch(String searchTerm, Set<String> results) {
        // Query by phone

        Query queryByPhone = mechanicReference.orderByChild("phone").startAt(searchTerm).endAt(searchTerm + "\uf8ff");
        queryByPhone.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String Phonename = snapshot.child("phone").getValue(String.class);
                    // Convert the name to lowercase for case-insensitive comparison
                    if (Phonename != null) {
                        String phoneLower = Phonename.toLowerCase();
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
                                Intent intent = new Intent(PartialSearch.this, ResultOfSearch.class);

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

        results.clear();
    }



}
