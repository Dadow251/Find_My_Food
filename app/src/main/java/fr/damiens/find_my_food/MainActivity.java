package fr.damiens.find_my_food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Appelée lors d'un appui sur le bouton searchButton */
    public void search(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ListActivity.class);
        /* Utilisation du texte contenu dans la searchBar
        EditText editText = (EditText) findViewById(R.id.searchBar);
        String message = editText.getText().toString();
        intent.putExtra(Intent.EXTRA_TEXT, message);
        */
        startActivity(intent);

    }
}