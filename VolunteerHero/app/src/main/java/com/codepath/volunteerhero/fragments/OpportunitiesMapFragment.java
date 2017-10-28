package com.codepath.volunteerhero.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.activities.EventDetailActivity;
import com.codepath.volunteerhero.data.DataProvider;
import com.codepath.volunteerhero.data.EventDataProvider;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.storage.LocalStorage;
import com.codepath.volunteerhero.utils.MapUtils;
import com.codepath.volunteerhero.utils.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment with placeholder code for map view
 */
public class OpportunitiesMapFragment extends Fragment implements GoogleMap.OnMapLongClickListener,
        DataProvider.DataChangedListener<Event>, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    SupportMapFragment mapFragment;
    private GoogleMap map;

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
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    protected void loadMap(final GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
//            Toast.makeText(this.getContext(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            map.setOnMapLongClickListener(this);
            map.setOnMarkerClickListener(this);
            map.setOnInfoWindowClickListener(this);
            map.getUiSettings().setMapToolbarEnabled(false);
        } else {
            Toast.makeText(this.getContext(), "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    Map<String, Marker> markersByEventIds = new HashMap<>();

    public void addPin(Event e) {
        Marker m = MapUtils.addPin(map, new LatLng(e.latitude, e.longitude),
                Utils.ellipsize(e.title, 40),
                Utils.ellipsize(e.description, 50));
        m.setTag(e);
        markersByEventIds.put(e.id, m);
    }

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

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void dataChanged(List<Event> data) {
        Log.d("jenda", "data changed ");
        for (Event e: data) {
            Marker m = markersByEventIds.get(e.id);
            if (e.isDeleted) {
                if (m != null) {
                    m.remove();
                    if (selectedMarker != null && m.getId().equals(selectedMarker.getId())) {
                        selectedMarker = null;
                    }
                }
                markersByEventIds.remove(e.id);
            } else {
                if (m != null) {
                    addPin(e);
                }
            }
        }
    }

    @Override
    public void dataAdded(List<Event> data) {
        for(Event e: data) {
            addPin(e);
        }
    }

    @Override
    public void dataRemoved(List<Event> data) {
        Log.d("jenda", "data dataRemoved ");
        // TODO(jenda): Implement.
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d("jenda", "marker " + marker.getId());

        Event event = (Event) marker.getTag();
        Log.d("jenda", "marker " + event.getId());

        getActivity().startActivity(EventDetailActivity.getIntent(getContext(), event));
    }

    Marker selectedMarker;
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (selectedMarker != null) {
            selectedMarker.setIcon(MapUtils.getUnSelectedIcon());
        }
        selectedMarker = marker;
        selectedMarker.setIcon(MapUtils.getSelectedIcon());
        return false;
    }
}
