package com.app.singlehotel.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.singlehotel.R;
import com.app.singlehotel.Util.Method;

/**
 * Created by admin on 09-08-2017.
 */

public class RoomAmenities extends RecyclerView.Adapter<RoomAmenities.ViewHolder> {

    private Activity activity;
    private String[] separated;
    private Method method;

    public RoomAmenities(Activity activity, String[] separated) {
        this.activity = activity;
        this.separated = separated;
        method = new Method(activity);
    }

    @Override
    public RoomAmenities.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.room_amenities_adapter, parent, false);

        return new RoomAmenities.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomAmenities.ViewHolder holder, final int position) {
        holder.textView_amenities.setText(separated[position].trim());
    }

    @Override
    public int getItemCount() {
        return separated.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_amenities;

        public ViewHolder(View itemView) {
            super(itemView);
            textView_amenities = itemView.findViewById(R.id.textView_name_room_amenities_adapter);

        }
    }
}