package fr.damiens.find_my_food;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Locale;

public class AddFoodItemActivity extends AppCompatActivity {
    // nom de l'aliment
    public static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
    }

    public void send(View view){

        // Récupération de la base de données en ligne
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-my-food-a85a8-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference dbRef = db.getReference("Aliments");

        // Initialisation des cases de texte sur le layout
        EditText editAliment = findViewById(R.id.editTextAliment);
        EditText editDescription = findViewById(R.id.editTextDescription);
        EditText editPrice = findViewById(R.id.editTextPrice);
        EditText editMarket = findViewById(R.id.editTextMarket);
        EditText editURL = findViewById(R.id.editTextURL);


        // Récupération des informations de l'article à ajouter
        String aliment = editAliment.getText().toString();
        String description = editDescription.getText().toString();
        Double price = Double.parseDouble(editPrice.getText().toString());
        String market = editMarket.getText().toString();
        String url = editURL.getText().toString();

        // Lecture et écriture dans la base de données
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String[] keyWordsSearch = description.toLowerCase().split(" ");
                boolean article_exists = false;
                name =aliment+"_0";

                for(DataSnapshot article : dataSnapshot.getChildren()) {
                    String value = article.child("market").getValue(String.class);
                    String[] keyWordsResult = article.child("description").getValue(String.class).toLowerCase().split(" ");
                    int mots_desc = 0;
                    if(value.toLowerCase().equals(market.toLowerCase())){
                        for (String s : keyWordsSearch) {
                            for (String r : keyWordsResult) {
                                if (s.equals(r)) {
                                    mots_desc += 1;
                                }
                            }
                        }
                        if(mots_desc == keyWordsResult.length){
                            article_exists = true;
                           name = article.getKey();
                        }
                    }
                    // Atribution numero d'identification du nouvel aliment à partir des autres aliment de la base de données
                    if(!article_exists) {
                        if (article.getKey().toLowerCase().contains(aliment.toLowerCase())) {
                            int num_result = Integer.parseInt(article.getKey().toLowerCase().split("_")[1]);
                            int num_add = num_result + 1;
                            name = aliment + "_" + num_add;
                        }
                    }
                }

                // Création de l'aliment dans la base de donnée
                if(!article_exists){
                    dbRef.child(name).child("description").setValue(description);
                    dbRef.child(name).child("price").setValue(price);
                    dbRef.child(name).child("market").setValue(market);
                    dbRef.child(name).child("url").setValue(url);

                    // Retour à l'Activity Main et affichage du message de bon déroulement de l'ajout
                    String message = name + " a bien été ajouté à la base de données";
                    Intent intent = new Intent(AddFoodItemActivity.this, MainActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(intent);
                }
                // Demande de modification de l'aliment dans la base de donnée à l'utilisateur
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddFoodItemActivity.this);
                    builder.setMessage("Cet aliment existe déjà dans la base de données. Voulez-vous modifier cet aliment?")
                            .setPositiveButton("Oui je veux", new DialogInterface.OnClickListener() {
                                // Modification du prix et de l'image de l'aliment
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(!price.isNaN()) {
                                        dbRef.child(name).child("price").setValue(price);
                                    }
                                    if(!url.isEmpty()) {
                                        dbRef.child(name).child("url").setValue(url);
                                    }

                                    // Retour à l'Activity Main et affichage du message de bon déroulement de l'ajout
                                    String message = name + " a bien été modifié dans la base de données";
                                    Intent intent = new Intent(AddFoodItemActivity.this, MainActivity.class);
                                    intent.putExtra(Intent.EXTRA_TEXT, message);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Non je ne veux pas", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(AddFoodItemActivity.this,"Base de données non modifiée", Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder.create().show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }});

    }

}
