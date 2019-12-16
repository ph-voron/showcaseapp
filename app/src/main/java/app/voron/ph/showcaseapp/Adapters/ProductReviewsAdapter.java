package app.voron.ph.showcaseapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.voron.ph.showcaseapp.Models.ReviewItemDataModel;
import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.Views.ReviewItemViewHolder;

/**
 * Created by TJack on 06.11.2017.
 */

public class ProductReviewsAdapter extends RecyclerView.Adapter<ReviewItemViewHolder> {
    //
    private List<ReviewItemDataModel> mData = new ArrayList<>();

    @Override
    public ReviewItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_review_item, parent, false);
        ReviewItemViewHolder holder = new ReviewItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReviewItemViewHolder holder, int position) {
        ReviewItemDataModel item = mData.get(position);
        holder.setTextName(item.name);
        holder.setTextReview(item.text);
        holder.setRating(item.rate);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<ReviewItemDataModel> data){
        mData = data;
        notifyDataSetChanged();
    }

    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }
}
