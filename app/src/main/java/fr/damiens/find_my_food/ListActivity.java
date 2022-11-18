package fr.damiens.find_my_food;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<FoodItem> data;
    private RVAdapter rvAdapter;
    private TextView textView;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private ArrayList<QueryDocumentSnapshot> documents;

    void initData(){
        data = new ArrayList<>();
        documents = new ArrayList<>();
        /*
        data.add(new FoodItem("Tomate grappe",1.20, "Carrefour"));
        data.add(new FoodItem("Tomate cerise",1.15, "Auchan"));
        data.add(new FoodItem("Tomate allongée",1.35, "Super U"));
        data.add(new FoodItem("Tomate coeur de boeuf",1.38, "E. Leclerc"));
        data.add(new FoodItem("Tomate verte",1.07, "Intermarché"));
        data.add(new FoodItem("Tomate noire",1.42, "Lidl"));
        data.add(new FoodItem("Tomate cerise allongée",0.96, "Grand frais"));
        data.add(new FoodItem("Tomate ronde",1.03, "Auchan"));
        data.add(new FoodItem("Tomate jaune",1.12, "Super U"));
        */

        database.collection("Aliments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                documents.add(document);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        for(QueryDocumentSnapshot doc : documents)
            Log.d(TAG, doc.getId() + " ==> " + doc.getData());
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
