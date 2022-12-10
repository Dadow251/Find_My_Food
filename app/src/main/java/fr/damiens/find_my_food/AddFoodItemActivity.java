package fr.damiens.find_my_food;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddFoodItemActivity extends AppCompatActivity {

    // Récupération de la base de données en ligne
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-my-food-a85a8-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference dbRef = db.getReference("Aliments");

    // Initialisation des cases de texte sur le layout
    EditText editAliment;
    EditText editDescription;
    EditText editPrice;
    EditText editMarket;
    EditText editURL;

    // Attributs de l'article à ajouter
    String aliment;
    String description;
    Double price;
    String market;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Initialisation des cases de texte sur le layout
        editAliment = findViewById(R.id.editTextAliment);
        editDescription = findViewById(R.id.editTextDescription);
        editPrice = findViewById(R.id.editTextPrice);
        editMarket = findViewById(R.id.editTextMarket);
        editURL = findViewById(R.id.editTextURL);
    }

    public void send(View view) {

        if (editAliment.length() > 0){
            aliment = editAliment.getText().toString().replace(" ", "");
        }
        else{
            Toast.makeText(this, "Des champs obligatoires sont vides", Toast.LENGTH_SHORT).show();
            return;
        }

        if(editDescription.length() > 0) {
            description = editDescription.getText().toString();
        }
        else{
            Toast.makeText(this, "Des champs obligatoires sont vides", Toast.LENGTH_SHORT).show();
            return;
        }

        if(editPrice.length() > 0) {
            price = Double.parseDouble(editPrice.getText().toString());
        }
        else{
            Toast.makeText(this, "Des champs obligatoires sont vides", Toast.LENGTH_SHORT).show();
            return;
        }

        if(editMarket.length() > 0){
            market = editMarket.getText().toString().replace(" ", "");
        }
        else{
            Toast.makeText(this, "Des champs obligatoires sont vides", Toast.LENGTH_SHORT).show();
            return;
        }

        url = editURL.getText().toString();

        // Lecture et écriture dans la base de données
        dbRef.addValueEventListener(new ValueEventListener() {
            String name = aliment + "_0";
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String[] keyWordsSearch = description.toLowerCase().split(" ");

                boolean existsInDatabase = false;
                boolean existentName = false;

                for(DataSnapshot article : dataSnapshot.getChildren()) {
                    boolean articleExists = false;

                    String marketValue = article.child("market").getValue(String.class);
                    String[] keyWordsDescription = article.child("description").getValue(String.class).toLowerCase().split(" ");
                    int matchingWords = 0;
                    if(article.getKey().toLowerCase().contains(aliment.toLowerCase())) {
                        if (marketValue.toLowerCase().equals(market.toLowerCase())) { /// à split pour mots composés
                            for (String s : keyWordsSearch) {
                                for (String r : keyWordsDescription) {
                                    if (s.equals(r)) {
                                        matchingWords += 1;
                                    }
                                }
                            }
                            if (matchingWords == keyWordsDescription.length) {
                                articleExists = true;
                                existsInDatabase = true;
                                name = article.getKey();
                            }
                        }
                    }

                    // Atribution numero d'identification du nouvel aliment à partir des autres aliment de la base de données
                    if(!existsInDatabase) {
                        if (article.getKey().toLowerCase().contains(aliment.toLowerCase())) {
                            existentName = true;
                            int num_result = Integer.parseInt(article.getKey().toLowerCase().split("_")[1]);
                            int num_add = Integer.parseInt(name.split("_")[1]);
                            if(num_result >= num_add)
                                num_add = num_result + 1;
                            name = aliment + "_" + num_add;
                        }
                    }
                }
                addToDatabase(existsInDatabase, name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    void addToDatabase(boolean exists, String name){
        // Création de l'aliment dans la base de donnée
        Log.d(TAG, "!!! addToDatabase : exists = " + exists);
        if(!exists){
            Log.d(TAG, "!!! " + name + " | " + description + " | " + price + " | " + market + " | " + url + " | ");
            dbRef.child(name).child("description").setValue(description);
            dbRef.child(name).child("price").setValue(price);
            dbRef.child(name).child("market").setValue(market);
            dbRef.child(name).child("url").setValue(url);

            Log.d(TAG, "!!! name : "+name);
            Log.d(TAG, "!!! description : "+description);
            Log.d(TAG, "!!! price : "+price);
            Log.d(TAG, "!!! market : "+market);
            Log.d(TAG, "!!! url : "+url);

            // Retour à l'Activity Main et affichage du message de bon déroulement de l'ajout
            String message = name.split("_")[0] + " a bien été ajouté à la base de données";
            Intent intent = new Intent(AddFoodItemActivity.this, MainActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(intent);
        }
        // Demande de modification de l'aliment dans la base de donnée à l'utilisateur
        else{
            Log.d(TAG, "!!! AlertDialog");

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
                            String message = name.split("_")[0] + " a bien été modifié dans la base de données";
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
}
