package app.voron.ph.showcaseapp.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.Utilities.FormatHelper;

/**
 * Created by TJack on 09.11.2017.
 */

public class CartTotalCostItemViewHolder extends RecyclerView.ViewHolder {
    private View mParent = null;
    private TextView mTextCost = null;
    //
    public CartTotalCostItemViewHolder(View itemView) {
        super(itemView);
        mParent = itemView;
        //
        mTextCost = mParent.findViewById(R.id.text_cost);
    }
    //
    public void setCost(float value){
        mTextCost.setText(FormatHelper.formatProductCost(value));
    }
}
