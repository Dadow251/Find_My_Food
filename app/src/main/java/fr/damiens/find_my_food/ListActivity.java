package fr.damiens.find_my_food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<FoodItem> data;
    RVAdapter rvAdapter;
    TextView textView;

    void initData(){
        data = new ArrayList<FoodItem>();
        data.add(new FoodItem("Tomate",1.20, "Carrefour"));
        data.add(new FoodItem("Tomate",1.15, "Auchan"));
        data.add(new FoodItem("Tomate",1.35, "Super U"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Récupère le message de l'intent à l'origine du démarrage de ListActivity
        Intent intent = getIntent();
        String message = intent.getStringExtra(Intent.EXTRA_TEXT);

        // Affichage de la recherche sur la ListActivity
        String resultDisplay = "Résultats pour " + message;
        textView = (TextView) findViewById(R.id.searchTextDisplay);
        textView.setText(resultDisplay);

        // Préparation de la liste d'items
        initData();

        // Création de la RV Adapter à partir des data
        rvAdapter = new RVAdapter(data);

        recyclerView = (RecyclerView) findViewById(R.id.rv_search);

        // Implantation du RVAdapter à la RV
        recyclerView.setAdapter(rvAdapter);

        // Définition du layout de la RV (vertical list)
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Fixation de la taille de la RV (indépendante de la quantité d'items)
        recyclerView.hasFixedSize();

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }
}
