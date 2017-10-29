package com.codepath.volunteerhero.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
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
import com.codepath.volunteerhero.activities.EventDetailActivity;
import com.codepath.volunteerhero.adapters.EventAdapter;
import com.codepath.volunteerhero.data.DataProvider;
import com.codepath.volunteerhero.data.EventDataProvider;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.settings.Filter;
import com.codepath.volunteerhero.settings.FilterSettings;
import com.codepath.volunteerhero.utils.EndlessRecyclerViewScrollListener;
import com.codepath.volunteerhero.utils.VolunteerHeroConstants;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment to list the volunteer opportunities
 */
public class OpportunitiesListFragment extends Fragment implements DataProvider.DataChangedListener<Event>, EventAdapter.OnItemClickListener {

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
    private Handler mHandler;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Event> mEventsList;
    private EventAdapter mEventAdapter;

    private EndlessRecyclerViewScrollListener mScrollListener;

    public OpportunitiesListFragment() {
        // Required empty public constructor
        EventDataProvider.getInstance().addDataChangedListener(this);
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

    public void addEvent(Event e) {
        mEventsList.add(0,  e);
        mEventAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mHandler = new Handler();
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
        mEventAdapter.setOnItemClickListener(this);
        rvEventList.setAdapter(mEventAdapter);
        mScrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                Toast.makeText(getActivity(), "Loading more events", Toast.LENGTH_SHORT).show();
//                populateEventList(page);
                loadMoreData();

            }
        };

        srSwipeContainer.setOnRefreshListener(()-> {
            mEventsList.clear();
            mEventAdapter.clear();
            mScrollListener.resetState();
            Toast.makeText(getActivity(), "Refreshing", Toast.LENGTH_SHORT).show();
//            populateEventList(0);

            startDataLoad();
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvEventList.getContext(),
                manager.getOrientation());
        rvEventList.addItemDecoration(dividerItemDecoration);


        rvEventList.addOnScrollListener(mScrollListener);
//        populateEventList(0);

        startDataLoad();

    }

    public void startDataLoad() {
        Filter filter = FilterSettings.getInstance(this.getContext()).retrieveFilter();
        EventDataProvider.getInstance().startDataLoad(filter);
    }

    public void loadMoreData() {
        Filter filter = FilterSettings.getInstance(this.getContext()).retrieveFilter();
        EventDataProvider.getInstance().loadMoreData(filter);
    }

    /*public void populateEventList2(int page) {
        if (page == 0) {
            // READ from DB first;

            LocalStorage ls = new LocalStorage(this.getContext());
            Log.d("jenda", "ls.readAllStoredEvents() " + ls.readAllStoredEvents());
            mEventAdapter.addAll(ls.readAllStoredEvents());
        }
        BetterPlaceClient client = BetterPlaceClient.getInstance();
        client.getEvents(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to get a network response");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    mHandler.post(() -> {
                        BetterPlaceEventResponse b = BetterPlaceEventResponse.parseJSON(responseData);
                        Log.d(TAG, "b.eventList.size " + b.data.size() );

                        mEventAdapter.addAll(b.data);
                        Log.d(TAG, "successfully loaded dummy data");
                        srSwipeContainer.setRefreshing(false);
                        //update adapter
                    });
                } catch (JSONException e) {

                }
            }
        }, page);
    }*/

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

    @Override
    public void dataChanged(List<Event> data) {
        mEventAdapter.updateAll(data);
        if (srSwipeContainer != null) {
            srSwipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void dataAdded(List<Event> data) {
        Log.d("jenda", "dataAdded " + data);
        mEventAdapter.addAll(data);
        if (srSwipeContainer != null) {
            srSwipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void dataRemoved(List<Event> data) {
        mEventAdapter.removeAll(data);
        if (srSwipeContainer != null) {
            srSwipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(View itemView, Event event) {
        // open detail activity
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra(VolunteerHeroConstants.EXTRA_EVENT, Parcels.wrap(event));
        Pair<View, String> p1 = Pair.create((View)itemView.findViewById(R.id.ivOrgPic), getString(R.string.eventImage));
        Pair<View, String> p2 = Pair.create((View)itemView.findViewById(R.id.tvOrgName), getString(R.string.orgName));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2);

        getActivity().startActivity(intent, options.toBundle());
    }

    @Override
    public void onItemLongClick(View itemView, Event event) {
        // Do nothing
    }
}
