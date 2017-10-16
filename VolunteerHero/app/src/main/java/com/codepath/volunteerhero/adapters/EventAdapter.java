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
import com.codepath.volunteerhero.models.Event;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dharinic on 10/13/17.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Event> mEventsList;
    private Context mContext;

    public EventAdapter(Context context, List<Event> events) {
        mContext = context;
        mEventsList = events;
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
        notifyDataSetChanged();
    }

    public void addAll(List<Event> tweets) {
        mEventsList.addAll(tweets);
        notifyDataSetChanged();
    }

    public class EventView extends RecyclerView.ViewHolder {

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
        }

        public void bind(Event event) {
            if (event.carrier != null) {
                tvOrgName.setText(event.carrier.name);
            } else {
                tvOrgName.setText(event.title);
            }
            tvLocation.setText(event.getLocation());
            tvTopics.setText(event.getTopics());

            Glide.with(mContext).load(R.drawable.ic_upload_cover_photo)
                    .into(ivOrgPic);

        }
    }
}
