package app.voron.ph.showcaseapp.Fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import app.voron.ph.showcaseapp.Activities.ParentActivity;
import app.voron.ph.showcaseapp.Adapters.ShowcaseAdapter;
import app.voron.ph.showcaseapp.Models.ShowcaseFilteringOptionsModel;
import app.voron.ph.showcaseapp.Models.ShowcaseItemDataModel;
import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.Services.ImagePreparingQueueService;
import app.voron.ph.showcaseapp.System.Providers.OnResultListener;


public class ShowcaseCategoryFragment
        extends Fragment
        implements
        SwipeRefreshLayout.OnRefreshListener,
        ShowcaseAdapter.OnAdapterInteractionListener{

    //
    private final static String PARAM_CATEGORY_TAG = "PARAM_CATEGORY_TAG";
    //
    public interface OnFragmentInteractionListener extends ParentActivity {
        void onShowcaseItemClick(View sender, ShowcaseItemDataModel item);
        void onShowcaseItemOrderClick(View sender, ShowcaseItemDataModel item);
        void onShowcaseItemRemoveFromCartClick(View sender, ShowcaseItemDataModel item);
        void onShowcaseCategoryFragmentRequestsItems(String categoryTag, OnResultListener<List<ShowcaseItemDataModel>> listener);
    }

    private OnFragmentInteractionListener mListener;
    private String mCategoryTag = null;
    //
    private RecyclerView mShowcaseList = null;
    private LinearLayoutManager mLayoutManager = null;
    private ShowcaseAdapter mDataAdapter = null;
    private SwipeRefreshLayout mSwipeToRefresh = null;
    //
    private ImagePreparingQueueService mService = null;
    private boolean mServiceConnected = false;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ImagePreparingQueueService.LocalBinder binder = (ImagePreparingQueueService.LocalBinder) iBinder;
            mService = binder.getService();
            mServiceConnected = true;
            onRefresh();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mServiceConnected = false;
        }
    };
    //
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        //
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    //
    private void beginRequestShowcaseItems(){
        if(mListener != null){
            mSwipeToRefresh.setRefreshing(true);
            mListener.onShowcaseCategoryFragmentRequestsItems(getCategoryTag(),
                    new OnResultListener<List<ShowcaseItemDataModel>>() {
                        @Override
                        public void onResult(List<ShowcaseItemDataModel> showcaseItemDataModels,
                                             Exception exception) {
                            mSwipeToRefresh.setRefreshing(false);
                            if(exception == null){
                                showcaseItemDataModels = showcaseItemDataModels == null ?
                                        new ArrayList<ShowcaseItemDataModel>() : showcaseItemDataModels;
                                mDataAdapter.setData(showcaseItemDataModels);
                                runLayoutAnimation(mShowcaseList);
                            } else {
                                mListener.showToast(R.string.error_showcase_items);
                            }
                        }
                    });
        }
    }/**/
    //

    public ShowcaseCategoryFragment() {
    }

    public String getCategoryTag(){
        return mCategoryTag;
    }

    public static ShowcaseCategoryFragment newInstance(String categoryTag) {
        ShowcaseCategoryFragment fragment = new ShowcaseCategoryFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CATEGORY_TAG, categoryTag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategoryTag = getArguments().getString(PARAM_CATEGORY_TAG);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showcase_category, container, false);
        mShowcaseList = view.findViewById(R.id.showcase_list);
        mSwipeToRefresh = view.findViewById(R.id.swipe_refresh);
        //
        mLayoutManager = new LinearLayoutManager(getContext());
        mDataAdapter = new ShowcaseAdapter(this);
        mShowcaseList.setLayoutManager(mLayoutManager);
        mShowcaseList.setAdapter(mDataAdapter);
        mSwipeToRefresh.setOnRefreshListener(this);
        //
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getContext(), ImagePreparingQueueService.class);
        getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mServiceConnected) {
            getActivity().unbindService(mServiceConnection);
            mServiceConnected = false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mShowcaseList != null){
            //runLayoutAnimation(mShowcaseList);
        }
    }

    @Override
    public void onRefresh() {
        mDataAdapter.clear();
        beginRequestShowcaseItems();
    }

    @Override
    public void onItemClick(View sender, ShowcaseItemDataModel item) {
        if(mListener != null)
            mListener.onShowcaseItemClick(sender, item);
    }

    @Override
    public void onItemOrderClick(View sender, ShowcaseItemDataModel item) {
        if(mListener != null)
            mListener.onShowcaseItemOrderClick(sender, item);
    }

    @Override
    public void onItemRemoveFromCartClick(View sender, ShowcaseItemDataModel item) {
        if(mListener != null)
            mListener.onShowcaseItemRemoveFromCartClick(sender, item);
    }

    @Override
    public void onBeginRequestShowcasePreviewImage(String imageName, ImageView receiver) {
        if(mServiceConnected){
            mService.addJob(imageName, receiver, true);
        }
    }



}
