package com.app.mygame.userPost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mygame.userPost.responseVo.BannerResponse;
import com.bumptech.glide.Glide;
import com.app.mygame.R;
import java.util.List;

import retrofit2.Callback;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.SliderViewHolder> {
    private Context context;
    private List<BannerResponse.Data> bannerResponses;  // Updated to hold BannerResponse.Data
    private OnImageClickListener listener;

    public interface OnImageClickListener {
        void onImageClick(String url, int position);
    }

    public ImageSliderAdapter(Context context, List<BannerResponse.Data> bannerResponses, OnImageClickListener listener) {
        this.context = context;
        this.bannerResponses = bannerResponses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        String imageUrl = bannerResponses.get(position).imageUrl;
        // Load image using Glide with rounded corners
        Glide.with(context)
                .load(imageUrl)
                .centerCrop()
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onImageClick(imageUrl, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bannerResponses.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImage);
        }
    }
}
