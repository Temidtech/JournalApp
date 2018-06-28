package com.swiftsynq.journalapp.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.swiftsynq.journalapp.R;
import com.swiftsynq.journalapp.Utils.CircleTransform;
import com.swiftsynq.journalapp.data.JournalPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by popoolaadebimpe on 27/06/2018.
 */

public class ProfileFragment extends Fragment {
    @BindView(R.id.profile_image)
    ImageView profile_image;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtFirstName)
    EditText edtFirstName;
    @BindView(R.id.edtLastName)
    EditText edtLastName;
    @BindView(R.id.tvDisplayName)
    TextView tvDisplayName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        ButterKnife.bind(this,rootView); // bind butterknife after
        Init();
        return rootView;

    }
    private void Init()
    {
        Glide
                .with(getActivity())
                .load(Uri.parse(JournalPreferences.getUserDetails(getContext()).getPhotoUrl()))
                .transform(new CircleTransform(getContext()))
                .placeholder(R.mipmap.ic_launcher)
                .into(profile_image);
        Log.d("photo",JournalPreferences.getUserDetails(getContext()).getPhotoUrl());
        UpdateUI();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JournalPreferences.setUserDetails(getContext(),getString(R.string.display_name_values,edtLastName.getText().toString(),edtFirstName.getText().toString()),
                        edtFirstName.getText().toString(),edtLastName.getText().toString(),
                        edtEmail.getText().toString(),JournalPreferences.getUserDetails(getContext()).getId(),Uri.parse(JournalPreferences.getUserDetails(getContext()).getPhotoUrl()));
                UpdateUI();
                Toast.makeText(getContext(), R.string.profile_success,Toast.LENGTH_LONG).show();
            }
        });
    }
    private void UpdateUI(){

        edtEmail.setText(JournalPreferences.getUserDetails(getContext()).getEmail());
        edtFirstName.setText(JournalPreferences.getUserDetails(getContext()).getGivenName());
        edtLastName.setText(JournalPreferences.getUserDetails(getContext()).getFamilyName());
        tvEmail.setText(JournalPreferences.getUserDetails(getContext()).getEmail());
        tvDisplayName.setText(JournalPreferences.getUserDetails(getContext()).getDisplayName());
    }
    public static ProfileFragment newInstance() {
        ProfileFragment fragment=new ProfileFragment();
        return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("Profile");
            }
        }
    }
}
