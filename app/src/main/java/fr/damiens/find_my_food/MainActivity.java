package fr.damiens.find_my_food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences savedItems = getSharedPreferences("savedList", MODE_PRIVATE);
        SharedPreferences.Editor editor = savedItems.edit();
        editor.putStringSet("savedBasket", null);
        editor.commit();

        // Récupération du message après ajout d'un item dans la base de données
        Intent intent = getIntent();
        String message = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(message != null)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /** Appelée lors d'un appui sur le bouton searchButton */
    public void search(View view) {
        // Appel à ListActivity
        Intent intent = new Intent(this, ListActivity.class);

        // Utilisation du texte contenu dans la searchBar
        EditText editText = (EditText) findViewById(R.id.searchBar);
        String message = editText.getText().toString();
        intent.putExtra(Intent.EXTRA_TEXT, message);

        // Passage à l'Activity suivante (ListActivity)
        startActivity(intent);
    }

    public void add(View view){
        // Appel à AddFoodItemActivity
        Intent intent = new Intent(this, AddFoodItemActivity.class);

        // Passage à l'Activity suivante (AddFoodItemActivity)
        startActivity(intent);
    }

    public void goToBasket(View view){
        // Appel à BasketActivity
        Intent intent = new Intent(this, BasketActivity.class);

        // Passage à l'Activity suivante (BasketActivity)
        startActivity(intent);
    }
}