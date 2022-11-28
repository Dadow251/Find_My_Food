package fr.damiens.find_my_food;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateFoodItemActivity extends AppCompatActivity {

    EditText priceTxt;
    String fiString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        priceTxt = findViewById(R.id.editTextPriceUpdate);
    }

    public void sendUpdate(View view){
        // Récupération de la base de données en ligne
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-my-food-a85a8-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference dbRef = db.getReference("Aliments");

        // Récupération des informations de l'article à mettre à jour
        double priceUpdate = Double.parseDouble(priceTxt.getText().toString());

        // Ecriture dans la base de données
        String name = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        dbRef.child(name).child("price").setValue(priceUpdate);
        Toast.makeText(this, "Vous avez mis à jour le prix d'un article", Toast.LENGTH_SHORT).show();
    }

}
