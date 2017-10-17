package com.codepath.volunteerhero.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.data.DataProvider;
import com.codepath.volunteerhero.data.EventDataProvider;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.storage.LocalStorage;
import com.codepath.volunteerhero.utils.MapUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment with placeholder code for map view
 */
public class OpportunitiesMapFragment extends Fragment implements GoogleMap.OnMapLongClickListener, DataProvider.DataChangedListener<Event> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    @BindView(R.id.map)

    SupportMapFragment mapFragment;
    private GoogleMap map;

    private OnFragmentInteractionListener mListener;

    public OpportunitiesMapFragment() {
        // Required empty public constructor
        EventDataProvider.getInstance().addDataChangedListener(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OpportunitiesMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OpportunitiesMapFragment newInstance(String param1, String param2) {
        OpportunitiesMapFragment fragment = new OpportunitiesMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            Toast.makeText(this.getContext(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
//            MapDemoActivityPermissionsDispatcher.getMyLocationWithCheck(this);
//            MapDemoActivityPermissionsDispatcher.startLocationUpdatesWithCheck(this);
            map.setOnMapLongClickListener(this);
//            map.setOnMarkerDragListener(this);
//            loadPins(map);
        } else {
            Toast.makeText(this.getContext(), "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addPin(Event e) {
        MapUtils.addPin(map, new LatLng(e.latitude, e.longitude), e.title, e.description);
    }

//    private void loadPins(GoogleMap map) {
//        Log.d("jenda", "loading pins");
//        LocalStorage ls = new LocalStorage(this.getContext());
//        for(Event e: ls.readAllStoredEvents()) {
//            addPin(e);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_opportunities_map, container, false);

        ButterKnife.bind(this, v);


        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {
            Toast.makeText(this.getContext(), "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void dataChanged(List<Event> data) {
        // TODO(jenda): Implement.
    }

    @Override
    public void dataAdded(List<Event> data) {
        for(Event e: data) {
            addPin(e);
        }
    }

    @Override
    public void dataRemoved(List<Event> data) {
        // TODO(jenda): Implement.
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
