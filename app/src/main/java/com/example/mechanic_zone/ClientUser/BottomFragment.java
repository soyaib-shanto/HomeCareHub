package com.example.mechanic_zone.ClientUser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mechanic_zone.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomFragment extends BottomSheetDialogFragment {



    TextView name,title,phone;
    Button confirmBtn;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public BottomFragment() {

    }


    public static BottomFragment newInstance(String param1, String param2) {
        BottomFragment fragment = new BottomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom, container, false);

        name = view.findViewById(R.id.namee_mm);
        title = view.findViewById(R.id.titleeM);
        phone = view.findViewById(R.id.phoneeM);
        confirmBtn = view.findViewById(R.id.ConfirmBtn);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("code", Context.MODE_PRIVATE);
        String nm = sharedPreferences.getString("name", null);
        String title1 = sharedPreferences.getString("title", null);
        String phone1 = sharedPreferences.getString("phone", null);


            name.setText(nm);
            title.setText(title1);
            phone.setText(phone1);

            confirmBtn.setOnClickListener(v -> {

                if (phone1 != null) {
                    // Check for permission at runtime on Android 6.0 and later
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // Request the permission
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                            return;
                        }
                    }

                    // Permission is granted, initiate the call
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phone1));
                    startActivity(intent);
                }
            });

        return view;
    }
}