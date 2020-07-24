package com.example.desafioshipp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.desafioshipp.R;
import com.google.android.libraries.places.api.model.Place;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class OrderConfirmedActivity extends AppCompatActivity {
    private TextView textPlaceName;
    private TextView textOrderTotal;
    private TextView textPlaceAddress;
    private TextView textOrderDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);
        Objects.requireNonNull(getSupportActionBar()).hide();

        connectLayout();
        setCardInfos();
    }

    public void connectLayout(){
        textPlaceName = findViewById(R.id.textPlaceName);
        textOrderTotal = findViewById(R.id.textOrderTotal);
        textPlaceAddress = findViewById(R.id.textPlaceAddress);
        textOrderDescription = findViewById(R.id.textOrderDescription);
    }

    public void setCardInfos(){
        Bundle bundle = getIntent().getExtras();

        Locale l_BR = new Locale("pt", "BR");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(l_BR);
        String totalValue = bundle.getString("totalValue");

        if(bundle != null){
            textPlaceName.setText(bundle.getString("placeName"));
            textOrderTotal.setText("Valor: " + formatter.format(Double.parseDouble(totalValue)));
            textPlaceAddress.setText(bundle.getString("placeAddress"));
            textOrderDescription.setText(bundle.getString("orderDescription"));
        }
    }

    public void handleContinue(View v){
        Intent i = new Intent(OrderConfirmedActivity.this, MainActivity.class);
        startActivity(i);
    }
}