package com.swiftsynq.journalapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swiftsynq.journalapp.Model.User;
import com.swiftsynq.journalapp.data.DatabaseContract;
import com.swiftsynq.journalapp.data.DatabaseContract.DiaryColumns;
import com.swiftsynq.journalapp.data.DiaryEntry;
import com.swiftsynq.journalapp.data.DiaryUpdateService;
import com.swiftsynq.journalapp.views.DatePickerFragment;
import com.swiftsynq.journalapp.views.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by popoolaadebimpe on 26/06/2018.
 */

public class AddNewDiaryActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener,
        View.OnClickListener{
    private static final String TAG = AddNewDiaryActivity.class.getSimpleName();
    //Selected due date, stored as a timestamp
    private Calendar mDueDate;
    @BindView(R.id.edtDescription)
    EditText edtDescription;
    private int year;
    private int month;
    private int day;
    Uri diaryUri;
    private FirebaseUser user;

    private DatabaseReference mDatabase;
    private DatabaseReference mMessageReference;
    private ValueEventListener mMessageListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        ButterKnife.bind(this);
        diaryUri = getIntent().getData();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mMessageReference = FirebaseDatabase.getInstance().getReference("diary");
        user = FirebaseAuth.getInstance().getCurrentUser();
        setActionTitle(currentdate());
    }
    private void setActionTitle(String title)
    {
        if (this instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) this);
            if (activity.getSupportActionBar() != null) {
                if(diaryUri!=null)
                {
                    String[] projection = {DatabaseContract.DiaryColumns.DESCRIPTION,
                            DatabaseContract.DiaryColumns.IS_FAVOURITE, DatabaseContract.DiaryColumns.DIARY_DATE };


                    Cursor cursor = getContentResolver().query(diaryUri,
                            projection, null, null,
                            null);

                    if (cursor.moveToFirst()) {
                        cursor.moveToFirst();
                        edtDescription.setText(cursor.getString(0));
                        long duedate=cursor.getLong(1);
                        if (duedate!=0)
                            setActionTitle(String.valueOf(DateUtils.getRelativeTimeSpanString(duedate)));
                        cursor.close();
                    } else {

                    }

                }
                else
                {
                    String[] arr=title.split("-");
                    String date=arr[0];
                    String time=arr[1];
                    activity.getSupportActionBar().setTitle(date);
                    activity.getSupportActionBar().setSubtitle(time);
                }


            }
        }
    }
    private String currentdate()
    {
        Date c = Calendar.getInstance().getTime();
        mDueDate=Calendar.getInstance();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM,yyyy-hh:mm");
        String formattedDate = df.format(c);
        return formattedDate;
    }
    public long getDateSelection() {
        return mDueDate.getTimeInMillis();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_diary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SimplifiableIfStatement
        switch (item.getItemId())
        {
            case R.id.action_calendar:
                DatePickerFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "datePicker");
                return true;
            case R.id.action_save:
                if(edtDescription.getText().toString().equals(""))
                    Toast.makeText(this,"Description  Field can't be empty",Toast.LENGTH_LONG).show();
                else
                    saveOrUpdate();
                return true;
            case R.id.action_discard:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void saveOrUpdate()
    {
        if (diaryUri!=null)
            updateItem(diaryUri);
        else
            saveItem();
            addtoFirebase();
    }
    /* Manage the selected date value */
    public void setDateSelection(Calendar selectedTimestamp) {
        mDueDate = selectedTimestamp;
        System.out.println("Current time => " + mDueDate.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM,yyyy-hh:mm");
        String formattedDate = df.format(mDueDate.getTime());
        setActionTitle(String.valueOf(formattedDate));
    }
    private void popTimeDialog()
    {
        TimePickerFragment dialogFragmen = new TimePickerFragment();
        dialogFragmen.show(getSupportFragmentManager(), "datePicker");
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Set to noon on the selected day
        this.year=year;
        this.day=dayOfMonth;
        this.month=month;
        popTimeDialog();
    }


    @Override
    public void onClick(View v) {

    }
    private void addtoFirebase()
    {
        final DiaryEntry entry=new DiaryEntry(edtDescription.getText().toString(),true,getDateSelection());
        // User data change listener
        mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user == null) {
                    Log.e(TAG, "onDataChange: User data is null!");
                    return;
                }

                mMessageReference.setValue(entry);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "onCancelled: Failed to read user!");
            }
        });

    }
    private void saveItem() {
        //Insert a new item
        ContentValues values = new ContentValues(3);
        values.put(DiaryColumns.DESCRIPTION, edtDescription.getText().toString());
        values.put(DiaryColumns.IS_FAVOURITE, 0);
        values.put(DiaryColumns.DIARY_DATE, getDateSelection());
        DiaryUpdateService.insertNewTask(this, values);


        Toast.makeText(this, "Successfully saved", Toast.LENGTH_LONG).show();
        finish();
    }
    private void updateItem(Uri uri) {
        //UPDATE a new item
        ContentValues values = new ContentValues(3);
        values.put(DiaryColumns.DESCRIPTION, edtDescription.getText().toString());
        values.put(DiaryColumns.IS_FAVOURITE, 0);
        values.put(DiaryColumns.DIARY_DATE, getDateSelection());
        DiaryUpdateService.updateTask(this,uri, values);
        Toast.makeText(this, "Successfully Updated", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        setDateSelection(c);
    }
    @Override
    protected void onStop() {
        super.onStop();

        if (mMessageListener != null) {
            mMessageReference.removeEventListener(mMessageListener);
        }
    }
}
