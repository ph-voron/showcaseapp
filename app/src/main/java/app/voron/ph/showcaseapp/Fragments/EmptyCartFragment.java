package app.voron.ph.showcaseapp.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import app.voron.ph.showcaseapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmptyCartFragment extends Fragment {


    public static EmptyCartFragment newInstance(){
        return new EmptyCartFragment();
    }

    public EmptyCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empty_cart, container, false);
    }

}
