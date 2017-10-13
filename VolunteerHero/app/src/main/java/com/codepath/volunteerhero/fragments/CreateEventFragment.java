package com.codepath.volunteerhero.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.volunteerhero.R;

import butterknife.ButterKnife;

/**
 * Created by jan_spidlen on 10/12/17.
 */

public class CreateEventFragment extends Fragment {


    public static CreateEventFragment newInstance() {
        CreateEventFragment fragment = new CreateEventFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_create_event,
                container, false);

        ButterKnife.bind(view);

        return view;
    }
}
