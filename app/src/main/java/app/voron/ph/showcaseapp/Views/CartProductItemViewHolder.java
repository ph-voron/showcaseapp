package app.voron.ph.showcaseapp.Views;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.Utilities.FormatHelper;

/**
 * Created by TJack on 09.11.2017.
 */

public class CartProductItemViewHolder extends RecyclerView.ViewHolder {
    //
    public interface OnInteractionListener{
        void onCartItemClick(View sender, int position);
        void onCartItemRemoveClick(View sender, int position);
    }
    //
    private View mParent = null;
    private TextView mTextTitle = null;
    private Button mButtonRemoveProduct = null;
    private ImageView mPreviewImage = null;
    private int mPosition = -1;
    //
    private OnInteractionListener mOnInteractionListener = null;
    //
    public CartProductItemViewHolder(View itemView) {
        super(itemView);
        mParent = itemView;
        mTextTitle = mParent.findViewById(R.id.text_title);
        mButtonRemoveProduct = mParent.findViewById(R.id.button_remove_product);
        mPreviewImage = mParent.findViewById(R.id.image_peview);
        CardView card = mParent.findViewById(R.id.layout_card_view);
        //
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnInteractionListener != null){
                    mOnInteractionListener.onCartItemClick(view, mPosition);
                }
            }
        });
        mButtonRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnInteractionListener != null){
                    mOnInteractionListener.onCartItemRemoveClick(view, mPosition);
                }
            }
        });

    }
    //
    public void setTitle(String value){
        if(value == null) return;
        mTextTitle.setText(value);
    }
    //
    public void setCost(float value){
        mButtonRemoveProduct.setText(FormatHelper.formatProductCost(value));
    }
    //
    public void setPreviewImage(Bitmap bmp){
        mPreviewImage.setImageBitmap(bmp);
    }
    //
    public ImageView getPreviewImage() {
        return mPreviewImage;
    }
    //
    public void setPosition(int position){
        mPosition = position;
    }
    //
    public void setOnInteractionListener(OnInteractionListener listener){
        mOnInteractionListener = listener;
    }
}
