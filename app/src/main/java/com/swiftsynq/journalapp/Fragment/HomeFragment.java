package com.swiftsynq.journalapp.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.swiftsynq.journalapp.AddNewDiaryActivity;
import com.swiftsynq.journalapp.Utils.CircleTransform;
import com.swiftsynq.journalapp.data.JournalPreferences;
import com.swiftsynq.journalapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by popoolaadebimpe on 31/03/2018.
 */

public class HomeFragment extends Fragment {
    @BindView(R.id.imgPhoto)
    ImageView imgPhoto;
    @BindView(R.id.tvTitle)
    TextView tvfullName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvlastLogin)
    TextView tvlastLogin;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
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
                .placeholder(R.drawable.ic_person_white_24dp)
                .into(imgPhoto);
        tvEmail.setText(JournalPreferences.getUserDetails(getContext()).getEmail());
        tvfullName.setText(JournalPreferences.getUserDetails(getContext()).getDisplayName());
        tvlastLogin.setText(String.format("Login Date:%s", currentdate()));

    }
    private String currentdate()
    {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM,yyyy-hh:mm");
        return df.format(c);
    }
    @OnClick(R.id.imgAddEntry)
    public void submit(View view) {
        Intent intent=new Intent(getActivity(), AddNewDiaryActivity.class);
        startActivity(intent);


    }
    public static HomeFragment newInstance() {
    return new HomeFragment();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("Home");
            }
        }

    }



    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
