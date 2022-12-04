package fr.damiens.find_my_food;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<FoodItem> data;
    private RVAdapter rvAdapter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Accès à la base de données Realtime Database (Firebase)
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-my-food-a85a8-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference dbRef = db.getReference("Aliments");

        textView = (TextView) findViewById(R.id.searchTextDisplay);

        // Configuration de la RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.rv_search);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        data = new ArrayList<>();

        rvAdapter = new RVAdapter(this, data);
        recyclerView.setAdapter(rvAdapter);

        // Fonctionnalité de clic sur un item de la RecyclerView : ouvre une page dédiée à l'item
        recyclerView.addOnItemTouchListener(new RVItemTouchListener(this, new RVItemTouchListener.ItemTouchListener() {
                    @Override
                    public void onItemTouch(View view, int position) {
                        String touchedItemName = data.get(position).getName();
                        Intent intent = new Intent(ListActivity.this,ItemActivity.class);
                        intent.putExtra(Intent.EXTRA_TEXT, touchedItemName);
                        intent.putExtra(Intent.EXTRA_COMPONENT_NAME, "AddButtonNeeded");
                        startActivity(intent);
                    }
                }, true, false));

        // Message de recherche
        Intent intent = getIntent();
        String keyWords = intent.getStringExtra(Intent.EXTRA_TEXT);
        String resultDisplay = "Résultats pour " + keyWords;
        textView.setText(resultDisplay);

        // Récupération des données de Realtime database (Firebase)
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                String[] keyWordsSearch = keyWords.toLowerCase().split(" ");
                for(DataSnapshot article : snapshot.getChildren()){
                    String name = article.getKey();
                    String description = article.child("description").getValue(String.class);
                    double price = article.child("price").getValue(double.class);
                    String market = article.child("market").getValue(String.class);
                    String url = article.child("url").getValue(String.class);
                    FoodItem foodItem = new FoodItem(name, description, price, market, url);

                    int nbSim = 0; // nombre de mots similaires entre recherche et description
                    if(keyWords.equals(""))
                        nbSim = Integer.MAX_VALUE; // rien dans la recherche => affiche tout
                    else if(description != null) {
                        String[] keyWordsResult = description.toLowerCase().split(" ");
                        for (String s : keyWordsSearch) {
                            for (String r : keyWordsResult) {
                                if (s.equals(r))
                                    nbSim++;
                            }
                        }
                    }
                    if(nbSim > 0) // sensibilité de la reconnaissance
                        data.add(foodItem); // W : liste vide en dehors de la classe
                }
                rvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
