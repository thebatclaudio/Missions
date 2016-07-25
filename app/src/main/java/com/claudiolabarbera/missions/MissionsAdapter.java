package com.claudiolabarbera.missions;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by labar on 23/07/2016.
 */
public class MissionsAdapter extends RecyclerView.Adapter<MissionsAdapter.ViewHolder> {
    private ArrayList<Mission> missions;
    private final OnItemClickListener listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView missionTitle;
        public TextView missionPlace;
        public ViewHolder(View v) {
            super(v);
            missionTitle = (TextView)v.findViewById(R.id.mission_title);
            missionPlace = (TextView)v.findViewById(R.id.mission_place);
        }

        public void bind(final Mission item, final OnItemClickListener listener) {
            missionTitle.setText(item.getTitle());
            missionPlace.setText(item.getPlace());

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Mission item);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MissionsAdapter(ArrayList<Mission> myDataset, OnItemClickListener listener) {
        this.missions = myDataset;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MissionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.missionTitle.setText(missions.get(position).getTitle());
        holder.missionPlace.setText(missions.get(position).getPlace());
        holder.bind(missions.get(position), listener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return missions.size();
    }

}
