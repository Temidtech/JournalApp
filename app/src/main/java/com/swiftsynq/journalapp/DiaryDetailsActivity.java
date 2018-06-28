package com.swiftsynq.journalapp;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.swiftsynq.journalapp.data.DatabaseContract;
import com.swiftsynq.journalapp.data.DiaryUpdateService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by popoolaadebimpe on 27/06/2018.
 */

public class DiaryDetailsActivity extends AppCompatActivity {

   @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.imgFav)
    ImageView imgFav;
    Uri diaryUri;
    //Selected due date, stored as a timestamp
    private long mDueDate = Long.MAX_VALUE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_details);
        ButterKnife.bind(this);
        //Diary entry must be passed to this activity as a valid provider Uri
        diaryUri = getIntent().getData();
        if(diaryUri!=null)
        {
            String[] projection = {DatabaseContract.DiaryColumns.DESCRIPTION,
                    DatabaseContract.DiaryColumns.IS_FAVOURITE, DatabaseContract.DiaryColumns.DIARY_DATE };


            Cursor cursor = getContentResolver().query(diaryUri,
                    projection, null, null,
                    null);

            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                tvDescription.setText(cursor.getString(0));
                imgFav.setImageResource(getPriorityImage(Integer.parseInt(cursor.getString(1))));
                long duedate=cursor.getLong(2);
                if (duedate!=0)
                    tvDate.setText(DateUtils.getRelativeTimeSpanString(duedate));
                cursor.close();
            } else {

            }

        }


    }
    private int getPriorityImage(int priority) {
        int priorityimage = 0;

        switch(priority) {
            case 0: priorityimage=R.drawable.ic_not_priority;
                break;
            case 1:priorityimage= R.drawable.ic_priority;
                break;
            default: break;
        }
        return priorityimage;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_diary, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            DiaryUpdateService.deleteTask(this,diaryUri);
            finish();
            return true;
        }
        else if(id==R.id.action_edit)
        {
            Intent intent=new Intent(DiaryDetailsActivity.this,AddNewDiaryActivity.class);
            intent.setData(diaryUri);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (this instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) this);
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("My Diary");
            }
        }

    }
}
