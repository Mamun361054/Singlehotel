package com.app.singlehotel.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.singlehotel.Item.RatingList;
import com.app.singlehotel.R;
import com.github.ornolfr.ratingview.RatingView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Activity activity;
    private List<RatingList> ratingLists;
    private int lastPosition = -1;

    public ReviewAdapter(Activity activity, List<RatingList> ratingLists) {
        this.activity = activity;
        this.ratingLists = ratingLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.review_adapter, parent, false);
        return new ReviewAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textView_name.setText(ratingLists.get(position).getUser_name());
        holder.textView_msg.setText(ratingLists.get(position).getMessage());
        holder.ratingView.setRating(Float.parseFloat(ratingLists.get(position).getRate()));

        if (position > lastPosition) {
            if (position % 2 == 0) {
                holder.relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.background_item_one_review_adapter));
            } else {
                holder.relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.background_item_two_review_adapter));
            }
        }

    }

    @Override
    public int getItemCount() {
        return ratingLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout relativeLayout;
        private RatingView ratingView;
        private TextView textView_name, textView_msg;

        public ViewHolder(View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.relativeLayout_review_adapter);
            ratingView = itemView.findViewById(R.id.ratingBar_review_adapter);
            textView_name = itemView.findViewById(R.id.textView_name_review_adapter);
            textView_msg = itemView.findViewById(R.id.textView_review_adapter);

        }
    }
}
