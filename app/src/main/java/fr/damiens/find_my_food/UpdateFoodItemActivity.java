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
    EditText urlTxt;
    String fiString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        priceTxt = findViewById(R.id.editTextPriceUpdate);
        urlTxt = findViewById(R.id.editTextURLUpdate);
    }

    public void sendUpdate(View view){
        // Récupération de la base de données en ligne
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://find-my-food-a85a8-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference dbRef = db.getReference("Aliments");

        // Récupération des informations de l'article à mettre à jour
        boolean priceIsEmpty = false;
        boolean urlIsEmpty = false;
        double priceUpdate = 0;
        String urlUpdate = "";
        if(priceTxt.length() > 0){
            priceUpdate = Double.parseDouble(priceTxt.getText().toString());
            if(Double.parseDouble(priceTxt.getText().toString()) < 0) {
                Toast.makeText(this, "Le prix ne peut pas être négatif", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else{
            priceIsEmpty = true;
        }

        if(urlTxt.length() > 0)
            urlUpdate = urlTxt.getText().toString();
        else{
            urlIsEmpty = true;
        }

        if(priceIsEmpty && urlIsEmpty){
            Toast.makeText(this, "Les deux champs sont vides", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ecriture dans la base de données
        String name = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        if(!priceIsEmpty) {
            dbRef.child(name).child("price").setValue(priceUpdate);
            Toast.makeText(this, "Vous avez mis à jour le prix d'un article", Toast.LENGTH_SHORT).show();
        }
        if(!urlIsEmpty) {
            dbRef.child(name).child("url").setValue(urlUpdate);
            Toast.makeText(this, "Vous avez mis à jour le prix d'un article", Toast.LENGTH_SHORT).show();
        }
    }
}
