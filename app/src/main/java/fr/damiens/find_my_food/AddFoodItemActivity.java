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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

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

        // Récupération des informations de l'article à ajouter
        String Aliment = editAliment.getText().toString();
        String Description = editDescription.getText().toString();
        Double Price = Double.parseDouble(editPrice.getText().toString());
        String Market = editMarket.getText().toString();

        // Ecriture dans la base de données
        dbRef.child(Aliment).child("description").setValue(Description);
        dbRef.child(Aliment).child("price").setValue(Price);
        dbRef.child(Aliment).child("market").setValue(Market);

        // Affichage sur le layout de l'aliment ajouté à la base de données
        Toast.makeText(AddFoodItemActivity.this,Aliment+" ajouté à la base de données" , Toast.LENGTH_SHORT).show();

    }

}
