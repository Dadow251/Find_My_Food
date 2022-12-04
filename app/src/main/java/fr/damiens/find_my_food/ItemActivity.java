package fr.damiens.find_my_food;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class ItemActivity extends AppCompatActivity {

    private TextView descriptionTextView;
    private TextView priceTextView;
    private TextView marketTextView;
    private ImageView imageImageView;

    FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-my-food-a85a8-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference dbRef = db.getReference("Aliments");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        // Initialisation des éléments graphiques
        descriptionTextView = findViewById(R.id.descriptionDisplay);
        priceTextView = findViewById(R.id.priceDisplay);
        marketTextView = findViewById(R.id.marketDisplay);
        imageImageView = findViewById(R.id.imageView);

        // Récupération du nom (identifiant) du FoodItem
        Intent intent = getIntent();
        String name = intent.getStringExtra(Intent.EXTRA_TEXT);

        // Récupération des données de Realtime database (Firebase)
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot itemSnapshot = snapshot.child(name);
                String itemDescription = itemSnapshot.child("description").getValue(String.class);
                double itemPrice = itemSnapshot.child("price").getValue(double.class);
                String itemMarket = itemSnapshot.child("market").getValue(String.class);
                String itemURL = itemSnapshot.child("url").getValue(String.class);

                // Affichage des champs du FoodItem
                descriptionTextView.setText(itemDescription);
                priceTextView.setText(""+itemPrice);
                marketTextView.setText(itemMarket);
                Glide.with(imageImageView)
                        .asBitmap()
                        .load(itemURL)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .error(R.drawable.no_image_icon)
                        .placeholder(R.drawable.no_image_icon)
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void update(View view){
        // Appel à UpdateFoodItemActivity
        Intent intent = new Intent(this, UpdateFoodItemActivity.class);

        // Ajout du nom de l'aliment en paramètre
        intent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra(Intent.EXTRA_TEXT));

        // Passage à l'Activity suivante (UpdateFoodItemActivity)
        startActivity(intent);
    }

    public void addToBasket(View view){
        String name = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        SharedPreferences savedItems = getSharedPreferences("savedList", MODE_PRIVATE);
        SharedPreferences.Editor editor = savedItems.edit();
        Set<String> items = savedItems.getStringSet("savedBasket",null);
        if(items != null) {
            items.add(name);
            editor.putStringSet("savedBasket", items);
        }
        else{
            Set<String> set = new ArraySet<>();
            set.add(name);
            editor.putStringSet("savedBasket", set);
        }
        editor.commit();
        Toast.makeText(this, name.split("_")[0] + " a été ajouté à votre panier", Toast.LENGTH_SHORT).show();
    }
}