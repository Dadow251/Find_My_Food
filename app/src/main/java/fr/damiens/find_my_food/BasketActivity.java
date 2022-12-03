package fr.damiens.find_my_food;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

    // Récupération de la base de données en ligne
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-my-food-a85a8-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference dbRef = db.getReference("Aliments");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

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

        dataSetDisplay(items);

        // Fonctionnalité de clic sur un item de la RecyclerView : supprime l'item
        recyclerView.addOnItemTouchListener(new RVItemTouchListener(this, new RVItemTouchListener.ItemTouchListener() {
            @Override
            public void onItemTouch(View view, int position) {
                String touchedItemName = data.get(position).getName();

                SharedPreferences savedItems = getSharedPreferences("savedList", MODE_PRIVATE);
                Set<String> items = savedItems.getStringSet("savedBasket", null);
                items.remove(touchedItemName);
                SharedPreferences.Editor editor = savedItems.edit();
                editor.putStringSet("savedBasket", items);

                dataSetDisplay(items);
            }
        }));
    }

    private void dataSetDisplay(Set<String> items){
        // (Re)Configuration de la RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.rv_basket);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rvAdapter = new RVAdapter(this, data);
        recyclerView.setAdapter(rvAdapter);

        if (items != null) {
            // Suppression des articles qui ne font plus partie du panier
            for(int i = 0; i < data.size(); i++){
                boolean exists = false;
                for(String name : items){
                    if(name.equals(data.get(i).getName()))
                        exists = true;
                }
                if(!exists)
                    data.remove(i);
            }

            for (String name : items) {
                // Cas où le produit est déjà dans la liste
                boolean exists = false;
                for(FoodItem fi : data){
                    if(fi.getName().equals(name)){
                        exists = true;
                        break;
                    }
                }
                if(exists) continue;

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