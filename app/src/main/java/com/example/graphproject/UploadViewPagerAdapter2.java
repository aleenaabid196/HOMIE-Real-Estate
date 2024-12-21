package com.example.graphproject;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UploadViewPagerAdapter2 extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> images;

    public UploadViewPagerAdapter2(Context context , ArrayList<String> images)
    {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_upload_img_layout,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.uploadImgViewCustom);
       // Picasso.with(context).load(images.get(position)).fit().centerCrop().into(imageView);
       //imageView.setImageURI(images.get(position));
        ViewPager viewPager =(ViewPager) container;
        viewPager.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
