package fr.damiens.find_my_food;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class BasketActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<FoodItem> data;
    private RVAdapter rvAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        // Récupération de la base de données en ligne
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-my-food-a85a8-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference dbRef = db.getReference("Aliments");

        // Configuration de la RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.rv_basket);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        data = new ArrayList<>(); // à remplir

        rvAdapter = new RVAdapter(this, data);
        recyclerView.setAdapter(rvAdapter);

        SharedPreferences savedItems = getSharedPreferences("savedList", MODE_PRIVATE);
        Set<String> items = savedItems.getStringSet("savedBasket", null);

        if (items != null) {
            data.clear();
            for (String name : items) {
                // Récupération des données de Realtime database (Firebase)
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot article : snapshot.getChildren()) {
                            if (!article.getKey().equals(name)) continue;

                            String description = article.child("description").getValue(String.class);
                            double price = article.child("price").getValue(double.class);
                            String market = article.child("market").getValue(String.class);
                            String url = article.child("url").getValue(String.class);
                            FoodItem foodItem = new FoodItem(name, description, price, market, url);
                            data.add(foodItem);
                        }
                        rvAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
        }
    }
}