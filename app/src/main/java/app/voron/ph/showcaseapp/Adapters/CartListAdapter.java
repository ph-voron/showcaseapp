package app.voron.ph.showcaseapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.voron.ph.showcaseapp.Models.ShowcaseItemDataModel;
import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.Views.CartProductItemViewHolder;
import app.voron.ph.showcaseapp.Views.CartTotalCostItemViewHolder;

/**
 * Created by TJack on 09.11.2017.
 */

public class CartListAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements CartProductItemViewHolder.OnInteractionListener{

    public interface OnAdapterInteractionListener{
        void onBeginRequestPreviewImage(String imageName, ImageView receiver);
        void onCartItemClick(View sender, ShowcaseItemDataModel item);
        void onCartItemRemoveClick(View sender, ShowcaseItemDataModel item);
    }
    //
    private interface BaseItem{
        int getType();
    }
    //
    private class CartItem implements BaseItem {
        public static final int TYPE = 0;
        public ShowcaseItemDataModel item = null;
        @Override
        public int getType() {
            return TYPE;
        }
        //
        public CartItem(ShowcaseItemDataModel item){
            this.item = item;
        }
    }
    //
    private class TotalCostItem implements BaseItem {
        public static final int TYPE = 1;
        public float totalCost = 0;
        @Override
        public int getType() {
            return TYPE;
        }
        //
        public TotalCostItem(float totalCost){
            this.totalCost = totalCost;
        }
    }

    private List<BaseItem> mItemList = new ArrayList<>();
    private OnAdapterInteractionListener mInteractionListener = null;
    //
    private List<BaseItem> getBaseItemsList(List<ShowcaseItemDataModel> data){
        List<BaseItem> result = new ArrayList<>();
        for (ShowcaseItemDataModel dataItem: data) {
            result.add(new CartItem(dataItem));
        }
        result.add(new TotalCostItem(0));
        return result;
    }
    //
    private CartProductItemViewHolder createProductHolder(ViewGroup parent){
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_cart_product_item, parent, false);
        CartProductItemViewHolder holder = new CartProductItemViewHolder(view);
        holder.setOnInteractionListener(this);
        return holder;
    }
    //
    private CartTotalCostItemViewHolder createTotalCostHolder(ViewGroup parent){
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_cart_total_cost_item, parent, false);
        return new CartTotalCostItemViewHolder(view);
    }
    //
    private int getCartItemPositionById(String id){
        for (int i = 0; i < mItemList.size(); i++) {
            BaseItem baseItem = mItemList.get(i);
            int type = baseItem.getType();
            if(type == CartItem.TYPE){
                CartItem cartItem = (CartItem)baseItem;
                if(cartItem.item.id.equals(id)){
                    return i;
                }
            }
        }
        return -1;
    }
    //
    @Nullable
    private TotalCostItem getTotalCostItem(){
        int size = mItemList.size();
        if(size == 0) return null;
        BaseItem lastItem = mItemList.get(mItemList.size()-1);
        if(lastItem.getType() != TotalCostItem.TYPE) return null;
        return (TotalCostItem)lastItem;
    }
    //
    private void recalculateTotalCost(){
        float totalCost = 0;
        for (BaseItem dataItem: mItemList) {
            if(dataItem.getType() == CartItem.TYPE){
                CartItem cartItem = (CartItem)dataItem;
                totalCost += cartItem.item.cost * 1;//cartItem.item.count
            }
        }
        TotalCostItem totalCostItem = getTotalCostItem();
        if(totalCostItem == null) return;
        totalCostItem.totalCost = totalCost;
    }

    public void setData(List<ShowcaseItemDataModel> data){
        mItemList = getBaseItemsList(data);
        recalculateTotalCost();
        notifyDataSetChanged();
    }
    //
    public void removeItemById(String id){
        int position = getCartItemPositionById(id);
        if(position == -1) return;
        mItemList.remove(position);
        recalculateTotalCost();
        notifyDataSetChanged();
    }
    //
    public CartListAdapter(OnAdapterInteractionListener listener){
        mInteractionListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case CartItem.TYPE:
                return createProductHolder(parent);
            case TotalCostItem.TYPE:
                return createTotalCostHolder(parent);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseItem baseItem = mItemList.get(position);
        switch (baseItem.getType()){
            case CartItem.TYPE: {
                CartItem cartItem = (CartItem) baseItem;
                CartProductItemViewHolder itemHolder = (CartProductItemViewHolder) holder;
                itemHolder.setTitle(cartItem.item.title);
                itemHolder.setCost(cartItem.item.cost);
                itemHolder.setPreviewImage(null);
                itemHolder.setPosition(position);
                if(mInteractionListener != null){
                    mInteractionListener.onBeginRequestPreviewImage(
                            cartItem.item.previewImageName, itemHolder.getPreviewImage());
                }
                break;
            }
            case TotalCostItem.TYPE: {
                TotalCostItem totalCostItem = (TotalCostItem) baseItem;
                CartTotalCostItemViewHolder itemHolder = (CartTotalCostItemViewHolder) holder;
                itemHolder.setCost(totalCostItem.totalCost);
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public void onCartItemClick(View sender, int position) {
        BaseItem baseItem = mItemList.get(position);
        if(baseItem.getType() == CartItem.TYPE){
            CartItem cartItem = (CartItem)baseItem;
            if(mInteractionListener != null){
                mInteractionListener.onCartItemClick(sender, cartItem.item);
            }
        }
    }

    @Override
    public void onCartItemRemoveClick(View sender, int position) {
        BaseItem baseItem = mItemList.get(position);
        if(baseItem.getType() == CartItem.TYPE){
            CartItem cartItem = (CartItem)baseItem;
            if(mInteractionListener != null){
                mInteractionListener.onCartItemRemoveClick(sender, cartItem.item);
            }
        }
    }
}
