package fr.damiens.find_my_food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Intent.EXTRA_TEXT);
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
}