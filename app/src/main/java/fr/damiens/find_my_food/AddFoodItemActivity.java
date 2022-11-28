package fr.damiens.find_my_food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFoodItemActivity extends AppCompatActivity {


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

        // Ecriture dans la base de données
        dbRef.child(aliment).child("description").setValue(description);
        dbRef.child(aliment).child("price").setValue(price);
        dbRef.child(aliment).child("market").setValue(market);
        dbRef.child(aliment).child("url").setValue(url);

        // Retour à l'Activity Main et affichage du message de bon déroulement de l'ajout
        String message = aliment + " a bien été ajouté à la base de données";
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(intent);
    }

}
