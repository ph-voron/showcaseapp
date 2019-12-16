package app.voron.ph.showcaseapp.Views;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import app.voron.ph.showcaseapp.R;

/**
 * Created by TJack on 06.11.2017.
 */

public class ReviewItemViewHolder extends RecyclerView.ViewHolder {
    //
    private View mParent = null;
    private TextView mTextName = null;
    private TextView mTextReview = null;
    private RatingBar mRating = null;
    //
    public ReviewItemViewHolder(View itemView) {
        super(itemView);
        mParent = itemView;
        mTextName = mParent.findViewById(R.id.text_name);
        mTextReview = mParent.findViewById(R.id.text_review);
        mRating = mParent.findViewById(R.id.widget_rating);
    }
    //
    public void setTextName(String value){
        mTextName.setText(value);
    }
    //
    public void setTextReview(String value){
        mTextReview.setText(value);
    }
    //
    public void setRating(float value){
        mRating.setRating(value);
    }
}
