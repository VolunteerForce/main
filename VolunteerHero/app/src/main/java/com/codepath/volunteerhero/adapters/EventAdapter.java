package com.codepath.volunteerhero.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.VolunteerHeroApplication;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.models.User;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dharinic on 10/13/17.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Event> mEventsList;
    private Context mContext;

    public EventAdapter(Context context, List<Event> events) {
        mContext = context;
        mEventsList = events;
        removeDeletedAndSortEvents();
    }

    private OnItemClickListener mListener;

    // Interface for item click
    public interface OnItemClickListener {
        void onItemClick(View itemView, Event event);
        void onItemLongClick(View itemView, Event event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder viewHolder;
        View v = inflater.inflate(R.layout.item_opportunity, parent, false);
        viewHolder = new EventView(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Event e = mEventsList.get(position);
        EventView view = (EventView) holder;
        view.bind(e);
    }

    @Override
    public int getItemCount() {
        return mEventsList.size();
    }

    public void clear() {
        mEventsList.clear();
        removeDeletedAndSortEvents();
    }

    public void addAll(List<Event> events) {
        mEventsList.addAll(events);
        removeDeletedAndSortEvents();
    }

    private void removeDeletedAndSortEvents() {
        mEventsList.removeIf(e -> e.isDeleted);
        // Sorting based on updated date -> this helps with creating our own events.
        Collections.sort(mEventsList, (i1, i2) -> {
            if (i1.updatedAt == null || i2.updatedAt == null) {
                return 0;
            }
            if (i1.updatedAt.getTime() == i2.updatedAt.getTime()) {
                return 0;
            } else if (i1.updatedAt.getTime() < i2.updatedAt.getTime()) {
                return 1;
            }
            return -1;
        });
        notifyDataSetChanged();
    }

    public void removeAll(List<Event> events) {
        mEventsList.removeAll(events);
        removeDeletedAndSortEvents();
    }

    public void updateAll(List<Event> events) {
        for (Event e : events) {
            mEventsList.replaceAll(event -> {
                if (e.id.equals(event.id)) {
                    return e;
                }
                return event;
            });
        }
        removeDeletedAndSortEvents();
    }

    public class EventView extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.tvOrgName)
        TextView tvOrgName;

        @BindView(R.id.tvLocation)
        TextView tvLocation;

        @BindView(R.id.tvTopics)
        TextView tvTopics;

        @BindView(R.id.ivOrgPic)
        ImageView ivOrgPic;

        public EventView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Event event) {
            if (event.carrier != null && event.carrier.name != null) {
                tvOrgName.setText(event.carrier.name);
            } else {
                tvOrgName.setText(event.title);
            }
            tvLocation.setText(event.getLocation());
            tvTopics.setText(event.getTopics());

            if (hasUserSubscribedToEvent(event)) {
                itemView.setBackgroundColor(mContext.getResources().getColor(R.color.lightBlue));
            } else {
                itemView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }

            Glide.with(mContext).load(event.getImageUrl())
                    .into(ivOrgPic);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Event e = mEventsList.get(position);
                mListener.onItemClick(itemView, e);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Event e = mEventsList.get(position);
                mListener.onItemLongClick(itemView, e);
            }
            return false;
        }

        private boolean hasUserSubscribedToEvent(Event event) {
            User currentUser = VolunteerHeroApplication.getLoggedInUser();
            List<Event> userEvents = currentUser.events;
            if (userEvents != null && !userEvents.isEmpty()) {
                for (Event e : userEvents) {
                    if (e.id.equals(event.id)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
