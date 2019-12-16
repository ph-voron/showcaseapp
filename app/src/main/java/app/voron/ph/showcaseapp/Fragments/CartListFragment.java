package app.voron.ph.showcaseapp.Fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import app.voron.ph.showcaseapp.Adapters.CartListAdapter;
import app.voron.ph.showcaseapp.Models.ShowcaseItemDataModel;
import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.Services.ImagePreparingQueueService;

public class CartListFragment
        extends Fragment
        implements  CartListAdapter.OnAdapterInteractionListener{

    public interface OnFragmentInteractionListener {
        List<ShowcaseItemDataModel> getCartData();
        void beginRemoveItemFromCart(ShowcaseItemDataModel item);
        void onCartItemCLick(View sender, ShowcaseItemDataModel item);
        void onSubmitOrder();
    }
    //
    private OnFragmentInteractionListener mListener;
    //
    private RecyclerView mListCartData = null;
    private CartListAdapter mAdapter = null;
    private FloatingActionButton mButtonSubmit = null;
    //
    private ImagePreparingQueueService mService = null;
    private boolean mServiceConnected = false;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ImagePreparingQueueService.LocalBinder binder = (ImagePreparingQueueService.LocalBinder) iBinder;
            mService = binder.getService();
            mServiceConnected = true;
            notifyDataSetChanged();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mServiceConnected = false;
        }
    };
    //
    private void showRemoveFromCartAskingDialog(final ShowcaseItemDataModel item){
        AlertDialog dlg = new AlertDialog.Builder(getContext())
                .setMessage(R.string.remove_from_cart_notification)
                .setPositiveButton(R.string.common_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(mListener != null){
                            mListener.beginRemoveItemFromCart(item);
                            mAdapter.removeItemById(item.id);
                        }
                    }
                })
                .setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .create();
        dlg.show();
    }
    //

    public CartListFragment() {
    }

    public static CartListFragment newInstance() {
        CartListFragment fragment = new CartListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void notifyDataSetChanged(){
        if(mListener != null) {
            mAdapter.setData(mListener.getCartData());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_list, container, false);
        mListCartData = view.findViewById(R.id.list_cart_data);
        mButtonSubmit = view.findViewById(R.id.button_submit);
        mAdapter = new CartListAdapter(this);
        mListCartData.setLayoutManager(new LinearLayoutManager(getContext()));
        mListCartData.setAdapter(mAdapter);
        //
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onSubmitOrder();
                }
            }
        });
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
    public void onBeginRequestPreviewImage(String imageName, ImageView receiver) {
        if(mServiceConnected){
            mService.addJob(imageName, receiver, true);
        }
    }

    @Override
    public void onCartItemClick(View sender, ShowcaseItemDataModel item) {
        if(mListener != null){
            mListener.onCartItemCLick(sender, item);
        }
    }

    @Override
    public void onCartItemRemoveClick(View sender, ShowcaseItemDataModel item) {
        showRemoveFromCartAskingDialog(item);
    }


}
