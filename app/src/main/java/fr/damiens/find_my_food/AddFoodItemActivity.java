package fr.damiens.find_my_food;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        // Read from the database
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String[] keyWordsSearch = description.toLowerCase().split(" ");
                boolean article_exists = false;
                String name ="";
                for(DataSnapshot article : dataSnapshot.getChildren()) {
                    String value = article.child("market").getValue(String.class);
                    String[] keyWordsResult = article.child("description").getValue(String.class).toLowerCase().split(" ");
                    if(value.toLowerCase().equals(market.toLowerCase())){
                        for (String s : keyWordsSearch) {
                            for (String r : keyWordsResult) {
                                if (s.equals(r)) {
                                    article_exists = true;
                                    name = article.getKey();
                                    break;
                                }
                            }
                        }
                    }
                }
                // Ecriture dans la base de données
                if(!article_exists){
                    dbRef.child(aliment).child("description").setValue(description);
                    dbRef.child(aliment).child("price").setValue(price);
                    dbRef.child(aliment).child("market").setValue(market);
                    if(url.isEmpty()) {
                        dbRef.child(aliment).child("url").setValue("https://www.icone-png.com/png/54/53892.png");
                    }
                    else{
                        dbRef.child(aliment).child("url").setValue(url);
                    }
                    // Retour à l'Activity Main et affichage du message de bon déroulement de l'ajout

                    String message = aliment + " a bien été ajouté à la base de données";
                    Intent intent = new Intent(AddFoodItemActivity.this, MainActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(AddFoodItemActivity.this,"Article "+name+" existe déjà", Toast.LENGTH_SHORT).show();
                    if(!price.isNaN()) {
                        dbRef.child(name).child("price").setValue(price);
                    }
                    if(!url.isEmpty()) {
                        dbRef.child(name).child("url").setValue(url);
                    }

                    String message = name + " a bien été modifié dans la base de données";
                    Intent intent = new Intent(AddFoodItemActivity.this, MainActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }});

    }

}
