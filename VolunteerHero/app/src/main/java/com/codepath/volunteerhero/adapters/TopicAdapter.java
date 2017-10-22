package com.codepath.volunteerhero.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.volunteerhero.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jan_spidlen on 10/22/17.
 */

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    private final List<String> topics;
    private final Context context;

    private final Set<String> selectedTopics = new HashSet<>();

    public TopicAdapter(Context context, List<String> topics) {
        this.topics = topics;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_topic, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, context, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item.setText(topics.get(position));
        holder.setSelected(selectedTopics.contains(topics.get(position)));
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public void onItemClicked(int position, boolean selected) {
        String topic = topics.get(position);
        if (selected) {
            selectedTopics.add(topic);
        } else {
            selectedTopics.remove(topic);
        }
    }

    public Set<String> getSelectedTopics() {
        return selectedTopics;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener  {

        @BindView(R.id.item)
        TextView item;

        private final Context context;
        private final TopicAdapter topicAdapter;
        boolean isSelected;

        public ViewHolder(View itemView, Context context, TopicAdapter topicAdapter) {
            super(itemView);
            this.context = context;
            this.topicAdapter = topicAdapter;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
            if (selected) {
                item.setBackgroundColor(context.getColor(R.color.see_through_grey));
            } else {
                item.setBackgroundColor(context.getColor(R.color.white));
            }
            topicAdapter.onItemClicked(getAdapterPosition(), selected);
        }

        @Override
        public void onClick(View v) {
            setSelected(!isSelected);
        }
    }
}
