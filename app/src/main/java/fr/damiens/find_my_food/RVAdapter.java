package fr.damiens.find_my_food;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Collections;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ItemViewHolder> {

    List<FoodItem> dataList;

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

    public RVAdapter(List<FoodItem> data){
        this.dataList = data;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position){
        holder.descriptionTxt.setText(dataList.get(position).getDescription());
        holder.priceTxt.setText(""+dataList.get(position).getPrice()+" €");
        holder.marketTxt.setText(dataList.get(position).getMarket());
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