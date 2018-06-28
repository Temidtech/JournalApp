package com.swiftsynq.journalapp.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.support.constraint.solver.widgets.ConstraintTableLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.swiftsynq.journalapp.AddNewDiaryActivity;
import com.swiftsynq.journalapp.DiaryDetailsActivity;
import com.swiftsynq.journalapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.TaskHolder> {

    /* Callback for list item click events */
    public interface OnItemClickListener {
        void onItemClick(View v, int position);

    }

    /* ViewHolder for each task item */
    public class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView dateView;
        public TextView tvDescription;
        public TextView tvTitle;
        public ImageView imgFav;
        @BindView(R.id.letterIcon)
        MaterialLetterIcon letterIcon;
        @BindView(R.id.custom_layout)
        CardView custom_layout;
        public TaskHolder(View itemView) {
            super(itemView);

            dateView = (TextView) itemView.findViewById(R.id.tvDate);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvTitle=(TextView) itemView.findViewById(R.id.tvTitle);
            imgFav=(ImageView)itemView.findViewById(R.id.imgFav);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View v) {
            postItemClick(this);
        }
    }

    private Cursor mCursor;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public DiaryAdapter(Context context) {
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void postItemClick(TaskHolder holder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
        }
    }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.custom_diary, parent, false);

        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskHolder holder, int position) {

        // Indices for the _id, description, and priority columns
        int idIndex = mCursor.getColumnIndex(DatabaseContract.DiaryColumns._ID);
        int descriptionIndex = mCursor.getColumnIndex(DatabaseContract.DiaryColumns.DESCRIPTION);
        final int favouriteIndex = mCursor.getColumnIndex(DatabaseContract.DiaryColumns.IS_FAVOURITE);
        int dateIndex = mCursor.getColumnIndex(DatabaseContract.DiaryColumns.DIARY_DATE);
        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String description = mCursor.getString(descriptionIndex);
        long diarydate = mCursor.getLong(dateIndex);
        int favourite = mCursor.getInt(favouriteIndex);
        //Set values
        holder.itemView.setTag(id);
        holder.tvDescription.setText(description);
        holder.tvTitle.setText(firstWords(description,4)+"...");
        holder.imgFav.setImageResource(getFavouriteImage(favourite));
        holder.dateView.setText(DateUtils.getRelativeTimeSpanString(diarydate));
        holder.letterIcon.setLetter("MD");
        holder.custom_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindDetails(id);
            }
        });

    }
    private void FindDetails(int position)
    {
        String stringId = String.valueOf(position);
        Uri uri = DatabaseContract.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        Intent diaryDetailActivity = new Intent(mContext, DiaryDetailsActivity.class);
        diaryDetailActivity.setData(uri);
        mContext.startActivity(diaryDetailActivity);
    }
    public static String firstWords(String input, int words) {
        for (int i = 0; i < input.length(); i++) {
            // When a space is encountered, reduce words remaining by 1.
            if (input.charAt(i) == ' ') {
                words--;
            }
            // If no more words remaining, return a substring.
            if (words == 0) {
                return input.substring(0, i);
            }
        }
        // Error case.
        return "";
    }
    private int getFavouriteImage(int favourite) {
        int priorityimage = 0;

        switch(favourite) {
            case 0: priorityimage=R.drawable.ic_not_priority;
                break;
            case 1:priorityimage= R.drawable.ic_priority;
                break;
            default: break;
        }
        return priorityimage;
    }
    @Override
    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    /**
     * Retrieve a {@link DiaryEntry} for the data at the given position.
     *
     * @param position Adapter item position.
     *
     * @return A new {@link DiaryEntry} filled with the position's attributes.
     */
    public DiaryEntry getItem(int position) {
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Invalid item position requested");
        }

        return new DiaryEntry(mCursor);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    public void swapCursor(Cursor cursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = cursor;
        notifyDataSetChanged();
    }
}
