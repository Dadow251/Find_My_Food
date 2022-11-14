package fr.damiens.find_my_food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Récupère l'intent qui est à l'origine du démarrage de ListActivity
        Intent intent = getIntent();
        String message = intent.getStringExtra(Intent.EXTRA_TEXT);

        // Injecte le nom du produit recherché dans le textView
        TextView textView = findViewById(R.id.resultsTextView);
        String s = "Résultats pour " + message;
        textView.setText(s);
    }
}
