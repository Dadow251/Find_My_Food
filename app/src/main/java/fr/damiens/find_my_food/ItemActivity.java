package fr.damiens.find_my_food;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {

    String itemDescription;
    double itemPrice;
    String itemMarket;

    private TextView descriptionTextView;
    private TextView priceTextView;
    private TextView marketTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        descriptionTextView = findViewById(R.id.descriptionDisplay);
        priceTextView = findViewById(R.id.priceDisplay);
        marketTextView = findViewById(R.id.marketDisplay);

        Intent intent = getIntent();
        String fiString = intent.getStringExtra(Intent.EXTRA_TEXT);
        String[] fieldsTab = fiString.split("/");
        itemDescription = fieldsTab[0];
        itemPrice = Double.parseDouble(fieldsTab[1]);
        itemMarket = fieldsTab[2];

        descriptionTextView.setText(itemDescription);
        priceTextView.setText(""+itemPrice);
        marketTextView.setText(itemMarket);
    }

}
