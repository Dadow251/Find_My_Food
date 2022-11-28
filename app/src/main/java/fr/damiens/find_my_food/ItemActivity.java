package fr.damiens.find_my_food;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class ItemActivity extends AppCompatActivity {

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

        // Accès à la base de données Realtime Database (Firebase)
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-my-food-a85a8-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference dbRef = db.getReference("Aliments");

        // Récupération des champs du FoodItem donnés sous forme d'une String
        //------------------------------------------------------------------------------------------
        // récupérer juste le "name" pour chercher dans la database => mis à jour systématiquement
        //------------------------------------------------------------------------------------------
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        String fiString = intent.getStringExtra(Intent.EXTRA_TEXT);
//        String[] fieldsTab = fiString.split("///");
//        String itemName = fieldsTab[0];
//        String itemDescription = fieldsTab[1];
//        double itemPrice = Double.parseDouble(fieldsTab[2]);
//        String itemMarket = fieldsTab[3];
//        String itemURL = fieldsTab[4];
    }

    public void update(View view){
        // Appel à UpdateFoodItemActivity
        Intent intent = new Intent(this, UpdateFoodItemActivity.class);

        // Ajout du nom de l'aliment en paramètre
        intent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra(Intent.EXTRA_TEXT));

        // Passage à l'Activity suivante (UpdateFoodItemActivity)
        startActivity(intent);
    }
}
