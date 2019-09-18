package com.app.singlehotel.Adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.singlehotel.InterFace.InterstitialAdView;
import com.app.singlehotel.Item.GalleryList;
import com.app.singlehotel.R;
import com.app.singlehotel.Util.Method;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by admin on 09-08-2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Activity activity;
    private List<GalleryList> galleryLists;
    private int columnWidth;
    private Method method;

    public GalleryAdapter(Activity activity, List<GalleryList> galleryLists, InterstitialAdView interstitialAdView) {
        this.activity = activity;
        this.galleryLists = galleryLists;
        method = new Method(activity, interstitialAdView);
        Resources r = activity.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics());
        columnWidth = (int) ((method.getScreenWidth() - ((3 + 1) * padding)) / 2);
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.gallery_adapter, parent, false);

        return new GalleryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.ViewHolder holder, final int position) {

        holder.imageView_Gallery.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnWidth));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(columnWidth, columnWidth / 2);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        holder.view.setLayoutParams(params);

        Picasso.get().load(galleryLists.get(position).getCategory_image_thumb())
                .placeholder(R.drawable.placeholder_portable)
                .into(holder.imageView_Gallery, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        holder.progressBar.setVisibility(View.GONE);
                    }


                });

        holder.textView_galleryName.setText(galleryLists.get(position).getCategory_name());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                method.interstitialAdShow(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return galleryLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView_Gallery;
        private TextView textView_galleryName;
        private View view;
        private RelativeLayout relativeLayout;
        private SmoothProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView_Gallery = itemView.findViewById(R.id.imageView_gallery_adapter);
            view = itemView.findViewById(R.id.view_gallery_adapter);
            relativeLayout = itemView.findViewById(R.id.relativeLayout_gallery_adapter);
            textView_galleryName = itemView.findViewById(R.id.textView_galleryname_gallery_adapter);
            progressBar = itemView.findViewById(R.id.smoothProgressBar_gallery_adapter);

        }
    }
}

