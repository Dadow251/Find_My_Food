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
import android.widget.TextView;
import android.widget.Toast;

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

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = db.getReference();

    void initData() { // crée des données factices pour évaluer l'affichage
        data = new ArrayList<>();

        data.add(new FoodItem("Tomate grappe", 1.20, "Carrefour"));
        data.add(new FoodItem("Tomate cerise", 1.15, "Auchan"));
        data.add(new FoodItem("Tomate allongée", 1.35, "Super U"));
        data.add(new FoodItem("Tomate coeur de boeuf", 1.38, "E. Leclerc"));
        data.add(new FoodItem("Tomate verte", 1.07, "Intermarché"));
        data.add(new FoodItem("Tomate noire", 1.42, "Lidl"));
        data.add(new FoodItem("Tomate cerise allongée", 0.96, "Grand frais"));
        data.add(new FoodItem("Tomate ronde", 1.03, "Auchan"));
        data.add(new FoodItem("Tomate jaune", 1.12, "Super U"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        textView = (TextView) findViewById(R.id.searchTextDisplay);

        recyclerView = (RecyclerView) findViewById(R.id.rv_search);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        data = new ArrayList<>();
        initData(); // données factices

        rvAdapter = new RVAdapter(this, data);
        recyclerView.setAdapter(rvAdapter);

        // Message de recherche
        Intent intent = getIntent();
        String message = intent.getStringExtra(Intent.EXTRA_TEXT);
        String resultDisplay = "Résultats pour " + message;
        textView.setText(resultDisplay);

        // Récupération des données de Realtime database (Firebase)
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    FoodItem foodItem = dataSnapshot.getValue(FoodItem.class);
                    data.add(foodItem);
                }
                rvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
