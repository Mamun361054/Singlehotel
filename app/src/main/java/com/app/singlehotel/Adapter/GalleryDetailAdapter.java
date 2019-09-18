package com.app.singlehotel.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.app.singlehotel.Item.GalleryDetailList;
import com.app.singlehotel.R;
import com.app.singlehotel.Util.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 09-08-2017.
 */

public class GalleryDetailAdapter extends PagerAdapter {

    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<GalleryDetailList> galleryDetailLists;

    public GalleryDetailAdapter(Activity activity, List<GalleryDetailList> galleryDetailLists) {
        this.activity = activity;
        this.galleryDetailLists = galleryDetailLists;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.gallery_detail_adapter, container, false);

        TouchImageView imageView_gallery = view.findViewById(R.id.imageView_gallery_detail_adapter);
        final ProgressBar progressBar = view.findViewById(R.id.ProgressBar_gallery_adapter);

        Picasso.get().load(galleryDetailLists.get(position).getWallpaper_image())
                .placeholder(R.drawable.placeholder_portable)
                .into(imageView_gallery, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.GONE);
                    }

                });

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return galleryDetailLists.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
