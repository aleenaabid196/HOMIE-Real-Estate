package com.example.graphproject;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ProfileAdApadter extends RecyclerView.Adapter<ProfileAdApadter.ViewHolder> {

    private ArrayList<Advertisement> mAds = new ArrayList<>();
    private Context mContext;
    private AdAdapterInterface1 adapterInterface1;

    public ProfileAdApadter(Context context, ArrayList<Advertisement> ads, AdAdapterInterface1 adapterInterface1) {
        mAds = ads;
        mContext = context;
        this.adapterInterface1 = adapterInterface1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_small_layout_for_profile, parent, false);
        return new ViewHolder(view);
        //return new ViewHolder(view , adAdapterInterface1);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {

//        Picasso.with(mContext).load(mAds.get(position).getImages().get(0)).fit().centerCrop().into(holder.image);
        String uri = mAds.get(position).getImages(0);
       // Picasso.with(mContext).load(uri).fit().centerCrop().into(holder.image);
        Picasso.get().load(uri).fit().centerCrop().into(holder.image);
        holder.name.setText(mAds.get(position).getAdTitle());
        holder.price.setText(mAds.get(position).getAdPrice().toString()+" PKR");

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.adTitlePicImgView);
            name = itemView.findViewById(R.id.adTitleTextView);
            price = itemView.findViewById(R.id.adPriceTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (adapterInterface1 != null)
                    {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION)
                        {
                            adapterInterface1.onItemCLick(pos);
                        }

                    }
                }
            });
        }
    }
}