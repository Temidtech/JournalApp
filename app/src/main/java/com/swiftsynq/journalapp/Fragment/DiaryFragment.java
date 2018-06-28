package com.swiftsynq.journalapp.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.swiftsynq.journalapp.AddNewDiaryActivity;
import com.swiftsynq.journalapp.DiaryDetailsActivity;
import com.swiftsynq.journalapp.MainActivity;
import com.swiftsynq.journalapp.R;
import com.swiftsynq.journalapp.SplashActivity;
import com.swiftsynq.journalapp.Utils.CircleTransform;
import com.swiftsynq.journalapp.data.DatabaseContract;
import com.swiftsynq.journalapp.data.DiaryAdapter;
import com.swiftsynq.journalapp.data.JournalPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.swiftsynq.journalapp.data.DatabaseContract.FAVUORITE_SORT;

/**
 * Created by popoolaadebimpe on 26/06/2018.
 */

public class DiaryFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private DiaryAdapter mAdapter;
    private static final String TAG = DiaryFragment.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;
    @BindView(R.id.imgEmpty)
    ImageView imgEmpty;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.diary_fragment, container, false);
        ButterKnife.bind(this,rootView); // bind butterknife after
        mAdapter = new DiaryAdapter(getActivity());
        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        if(mAdapter!=null)
            recyclerView.setVisibility(View.VISIBLE);
        else
            imgEmpty.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
        return rootView;

    }


    public static DiaryFragment newInstance() {
        DiaryFragment fragment=new DiaryFragment();
        return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("My Diary");
            }
        }
        getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID,null, this);
    }



    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(getActivity()) {

            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getActivity().getContentResolver().query(DatabaseContract.CONTENT_URI,
                            null,
                            null,
                            null,
                            FAVUORITE_SORT);

                } catch (Exception e) {
                    Log.e(TAG, "Ops!! Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
