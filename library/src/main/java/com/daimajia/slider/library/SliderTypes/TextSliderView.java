package com.daimajia.slider.library.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.R;

/**
 * This is a slider with a description TextView.
 */
public class TextSliderView extends BaseSliderView {


    public TextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_text, null);

        ImageView imageView_background = v.findViewById(R.id.imageView_background_render_type_text);

        bindEventAndShow(v, imageView_background);

        return v;
    }
}
