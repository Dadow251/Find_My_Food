package fr.damiens.find_my_food;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ItemViewHolder> {

    ArrayList<FoodItem> dataList;
    Context context;

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTxt;
        TextView priceTxt;
        TextView marketTxt;
        ImageView productImage;

        public ItemViewHolder(View itemView){
            super(itemView);
            descriptionTxt = itemView.findViewById(R.id.descriptionTextView);
            priceTxt = itemView.findViewById(R.id.priceTextView);
            marketTxt = itemView.findViewById(R.id.marketTextView);
            productImage = itemView.findViewById(R.id.foodImageView);
        }
    }

    public RVAdapter(Context context, ArrayList<FoodItem> data){
        this.dataList = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.rv_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position){
        FoodItem foodItem = dataList.get(position);
        holder.descriptionTxt.setText(foodItem.getDescription());
        holder.priceTxt.setText(""+foodItem.getPrice()+" €");
        holder.marketTxt.setText(foodItem.getMarket());

        Glide.with(holder.productImage)
                .asBitmap()
                .load(foodItem.getUrl())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(new BitmapImageViewTarget(holder.productImage) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        super.onResourceReady(bitmap, transition);
                        assert holder.productImage != null;
                        holder.productImage.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onLoadFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                    }
                });
    }

    @Override
    public int getItemCount(){
        return dataList.size();
    }

    public void removeItem(int position){
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    public void swapItems(int firstPosition, int secondPosition){
        Collections.swap(dataList, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }
}