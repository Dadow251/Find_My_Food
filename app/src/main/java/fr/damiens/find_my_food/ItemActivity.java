package fr.damiens.find_my_food;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {

    String itemDescription;
    double itemPrice;
    String itemMarket;
    String itemURL;

    private TextView descriptionTextView;
    private TextView priceTextView;
    private TextView marketTextView;
    private ImageView imageImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        // Initialisation des éléments graphiques
        descriptionTextView = findViewById(R.id.descriptionDisplay);
        priceTextView = findViewById(R.id.priceDisplay);
        marketTextView = findViewById(R.id.marketDisplay);
        imageImageView = findViewById(R.id.imageView);

        // Récupération des champs du FoodItem donnés sous forme d'une String
        Intent intent = getIntent();
        String fiString = intent.getStringExtra(Intent.EXTRA_TEXT);
        String[] fieldsTab = fiString.split("///");
        itemDescription = fieldsTab[0];
        itemPrice = Double.parseDouble(fieldsTab[1]);
        itemMarket = fieldsTab[2];
        itemURL = fieldsTab[3];

        // Affichage des champs du FoodItem
        descriptionTextView.setText(itemDescription);
        priceTextView.setText(""+itemPrice);
        marketTextView.setText(itemMarket);

        Glide.with(imageImageView)
                .asBitmap()
                .load(itemURL)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(new BitmapImageViewTarget(imageImageView) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        super.onResourceReady(bitmap, transition);
                        assert imageImageView != null;
                        imageImageView.setImageBitmap(bitmap);
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

}
