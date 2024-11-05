package com.example.mechanic_zone.ClientUser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.mechanic_zone.R;

public class ImageViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        ImageView fullScreenImageView = findViewById(R.id.fullScreenImageView);

        // Get the image URL or resource ID passed from the previous activity
        String imageUrl = getIntent().getStringExtra("image_url");
        int imageResourceId = getIntent().getIntExtra("image_resource_id", 0);

        if (imageUrl != null) {
            // Load and display an image from a URL using Glide (or any other library)
            Glide.with(this)
                    .load(imageUrl)
                    .into(fullScreenImageView);
        } else if (imageResourceId != 0) {
            // Load and display an image from a resource ID
            fullScreenImageView.setImageResource(imageResourceId);
        } else {
            // Handle the case where no valid image data is provided
            // You can display a placeholder or show an error message
        }

        // Optionally, add gesture support for zooming using a library like PhotoView
    }

}