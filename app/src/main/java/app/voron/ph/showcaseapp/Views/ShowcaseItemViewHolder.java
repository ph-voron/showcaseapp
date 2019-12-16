package app.voron.ph.showcaseapp.Views;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.Utilities.AnimationHelper;
import app.voron.ph.showcaseapp.Utilities.FormatHelper;

/**
 * Created by TJack on 11.10.2017.
 */

public class ShowcaseItemViewHolder extends RecyclerView.ViewHolder {
    //
    public interface OnInteractionListener{
        void onItemClick(View sender, int position);
        void onItemOrderClick(View sender, int position);
        void onItemRemoveFromCart(View sender, int position);
    }
    //
    private View mParent = null;
    private TextView mTitle = null;
    private TextView mDescription = null;
    private Button mButtonOrder = null;
    private Button mButtonRemoveProduct = null;
    private ImageView mPreviewImage = null;
    //
    private OnInteractionListener mListener = null;
    private int mPosition = -1;
    //
    public ShowcaseItemViewHolder(View itemView) {
        super(itemView);
        mParent = itemView;
        mTitle = mParent.findViewById(R.id.text_title);
        mDescription = mParent.findViewById(R.id.text_description);
        mPreviewImage = mParent.findViewById(R.id.image_peview);
        mButtonOrder = mParent.findViewById(R.id.button_order);
        mButtonRemoveProduct = mParent.findViewById(R.id.button_remove_product);
        CardView card = mParent.findViewById(R.id.layout_card_view);
        //
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onItemClick(view, mPosition);
                }
            }
        });
        mButtonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationHelper.animateViewSwapZoomAndAppear(mButtonOrder, mButtonRemoveProduct);
                if(mListener != null){
                    mListener.onItemOrderClick(view, mPosition);
                }
            }
        });
        mButtonRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationHelper.animateViewSwapZoomAndAppear(mButtonRemoveProduct, mButtonOrder);
                if(mListener != null){
                    mListener.onItemRemoveFromCart(view, mPosition);
                }
            }
        });
    }
    //
    public void setPosition(int position){
        mPosition = position;
    }
    //
    public void setPreviewImage(Bitmap bmp){
        mPreviewImage.setImageBitmap(bmp);
    }
    //
    public ImageView getPreviewImage() {
        return mPreviewImage;
    }

    public void setTitle(String value){
        if(value == null) return;
        mTitle.setText(value);
    }

    public void setDescription(String value){
        if(value == null) return;
        mDescription.setText(value);
    }

    public void setCost(float value){
        String costText = FormatHelper.formatProductCost(value);
        mButtonOrder.setText(costText);
        mButtonRemoveProduct.setText(costText);
    }

    public void setItemInCart(boolean value){
        mButtonOrder.setVisibility(value ? View.GONE : View.VISIBLE);
        mButtonRemoveProduct.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    public void setOnInteractionListener(OnInteractionListener listener){
        mListener = listener;
    }
}
