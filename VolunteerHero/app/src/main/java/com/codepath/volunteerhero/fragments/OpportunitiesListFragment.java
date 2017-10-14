package com.codepath.volunteerhero.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.adapters.EventAdapter;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment to list the volunteer opportunities
 */
public class OpportunitiesListFragment extends Fragment {

    public static final String TAG = OpportunitiesListFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.rvEventList)
    RecyclerView rvEventList;

    @BindView(R.id.swipeRefreshContainer)
    SwipeRefreshLayout srSwipeContainer;

    private Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Event> mEventsList;
    private EventAdapter mEventAdapter;

    private EndlessRecyclerViewScrollListener mScrollListener;

    public OpportunitiesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OpportunitiesListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OpportunitiesListFragment newInstance(String param1, String param2) {
        OpportunitiesListFragment fragment = new OpportunitiesListFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_opportunities_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvEventList.setLayoutManager(manager);
        mEventsList = new ArrayList<>();
        mEventAdapter = new EventAdapter(getActivity(), mEventsList);
        rvEventList.setAdapter(mEventAdapter);
        mScrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Toast.makeText(getActivity(), "Loading more events", Toast.LENGTH_SHORT).show();
            }
        };

        srSwipeContainer.setOnRefreshListener(()-> {
            mEventsList.clear();
            mEventAdapter.clear();
            mScrollListener.resetState();
            Toast.makeText(getActivity(), "Refreshing", Toast.LENGTH_SHORT).show();
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvEventList.getContext(),
                manager.getOrientation());
        rvEventList.addItemDecoration(dividerItemDecoration);


        rvEventList.addOnScrollListener(mScrollListener);

        populateEventList();
    }

    public void populateEventList() {
        List<Event> testEvents = new ArrayList<Event>(20);
        for (int i = 0; i < 20; ++i) {
            testEvents.add(new Event());
        }
        mEventAdapter.addAll(testEvents);
        Log.d(TAG, "successfully loaded dummy data");
        srSwipeContainer.setRefreshing(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
