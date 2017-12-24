package app.voron.ph.showcaseapp.Adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import app.voron.ph.showcaseapp.Models.ShowcaseItemDataModel;
import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.Views.ShowcaseItemViewHolder;

/**
 * Created by TJack on 11.10.2017.
 */

public class ShowcaseAdapter
        extends
        RecyclerView.Adapter<ShowcaseItemViewHolder>
        implements
        ShowcaseItemViewHolder.OnInteractionListener {

    //
    public interface OnAdapterInteractionListener{
        void onItemClick(View sender, ShowcaseItemDataModel item);
        void onItemOrderClick(View sender, ShowcaseItemDataModel item);
        void onItemRemoveFromCartClick(View sender, ShowcaseItemDataModel item);
        void onBeginRequestShowcasePreviewImage(String imageName, ImageView receiver);
    }
    //
    private List<ShowcaseItemDataModel> mData = new ArrayList<>();
    private OnAdapterInteractionListener mInteractionListener = null;
    //
    public ShowcaseAdapter(OnAdapterInteractionListener listener){
        mInteractionListener = listener;
    }
    //
    @Override
    public ShowcaseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_showcase_item, parent, false);
        ShowcaseItemViewHolder holder = new ShowcaseItemViewHolder(view);
        holder.setOnInteractionListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ShowcaseItemViewHolder holder, int position) {
        ShowcaseItemDataModel item = mData.get(position);
        holder.setTitle(item.title);
        holder.setDescription(item.description);
        holder.setCost(item.cost);
        holder.setItemInCart(item.itemsInCart > 0);
        //
        holder.setPosition(position);
        holder.setPreviewImage(null);
        if(mInteractionListener != null){
            mInteractionListener.onBeginRequestShowcasePreviewImage(
                    item.previewImageName, holder.getPreviewImage());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onItemClick(View sender, int position) {
        if(mInteractionListener != null){
            if(position > -1 && mData.size() > position){
                mInteractionListener.onItemClick(sender, mData.get(position));
            }
        }
    }

    @Override
    public void onItemOrderClick(View sender, int position) {
        if(mInteractionListener != null){
            if(position > -1 && mData.size() > position){
                ShowcaseItemDataModel item = mData.get(position);
                mInteractionListener.onItemOrderClick(sender, item);
                item.itemsInCart++;
                mData.set(position, item);
            }
        }
    }

    @Override
    public void onItemRemoveFromCart(View sender, int position) {
        if(mInteractionListener != null){
            if(position > -1 && mData.size() > position){
                ShowcaseItemDataModel item = mData.get(position);
                mInteractionListener.onItemRemoveFromCartClick(sender, item);
                item.itemsInCart = 0;
                mData.set(position, item);
            }
        }
    }


    public void setData(List<ShowcaseItemDataModel> data){
        mData = data;
        notifyDataSetChanged();
    }

    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }

}
