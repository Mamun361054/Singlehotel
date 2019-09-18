package com.app.singlehotel.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.singlehotel.InterFace.InterstitialAdView;
import com.app.singlehotel.Item.RoomList;
import com.app.singlehotel.R;
import com.app.singlehotel.Util.Method;
import com.github.ornolfr.ratingview.RatingView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by admin on 08-08-2017.
 */

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    private Activity activity;
    private List<RoomList> roomLists;
    private Method method;
    private int columnWidth;

    public RoomAdapter(Activity activity, List<RoomList> roomLists, InterstitialAdView interstitialAdView) {
        this.activity = activity;
        this.roomLists = roomLists;
        method = new Method(activity, interstitialAdView);
        columnWidth = method.getScreenWidth();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.room_adapter, parent, false);

        return new RoomAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.imageView_room.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnWidth / 2));
        holder.view.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnWidth / 2));

        Picasso.get().load(roomLists.get(position).getRoom_image_thumb())
                .placeholder(R.drawable.placeholder_landscap).into(holder.imageView_room, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                holder.progressBar.setVisibility(View.GONE);
            }


        });
        holder.textView_roomName.setText(roomLists.get(position).getRoom_name());
        holder.textView_price.setText(roomLists.get(position).getRoom_price());
        holder.textView_totalRate.setText("(" + roomLists.get(position).getTotal_rate() + ")");
        holder.ratingBar.setRating(Float.parseFloat(roomLists.get(position).getRate_avg()));

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                method.interstitialAdShow(position);
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                method.interstitialAdShow(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return roomLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView imageView_room;
        private RatingView ratingBar;
        private TextView textView_roomName, textView_price, textView_totalRate;
        private RelativeLayout relativeLayout;
        private View view;
        private Button button;
        private SmoothProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.relativeLayout_room_adapter);
            button = itemView.findViewById(R.id.button_room_adapter);
            imageView_room = itemView.findViewById(R.id.imageView_room_adapter);
            view = itemView.findViewById(R.id.view_room_adapter);
            textView_roomName = itemView.findViewById(R.id.textView_roomName_room_adapter);
            textView_price = itemView.findViewById(R.id.textView_roomPrice_room_adapter);
            textView_totalRate = itemView.findViewById(R.id.textView_totalRate_room_adapter);
            ratingBar = itemView.findViewById(R.id.ratingBar_room_adapter);
            progressBar = itemView.findViewById(R.id.smoothProgressBar_room_adapter);

        }
    }
}
