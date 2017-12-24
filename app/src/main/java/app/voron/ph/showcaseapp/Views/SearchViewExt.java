package app.voron.ph.showcaseapp.Views;

import android.content.Context;
import android.support.v7.widget.SearchView;

/**
 * Created by TJack on 26.10.2017.
 */

public class SearchViewExt extends SearchView {

    private Runnable mOnExpandedListener = null;
    private Runnable mOnCollapsedListener = null;
    //
    public SearchViewExt(Context context) {
        super(context);
    }
    //
    public void setOnExpandedListener(Runnable listener){
        mOnExpandedListener = listener;
    }
    //
    public void setOnCollapsedListener(Runnable listener){
        mOnCollapsedListener = listener;
    }

    @Override
    public void onActionViewCollapsed() {
        super.onActionViewCollapsed();
        if(mOnCollapsedListener != null)
            mOnCollapsedListener.run();
    }

    @Override
    public void onActionViewExpanded() {
        super.onActionViewExpanded();
        if(mOnExpandedListener != null)
            mOnExpandedListener.run();
    }
}
