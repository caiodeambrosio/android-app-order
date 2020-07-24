package com.example.desafioshipp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.desafioshipp.DAO.OrderDAO;
import com.example.desafioshipp.R;
import com.example.desafioshipp.model.Order;
import com.example.desafioshipp.model.Place;

import java.util.Objects;

public class OrderDescriptionActivity extends AppCompatActivity {
    private TextView textPlaceName;
    private TextView textPlaceAddress;
    private Order order;
    private OrderDAO orderDAO;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_description);

        Objects.requireNonNull(getSupportActionBar()).hide();

        Bundle bundle = getIntent().getExtras();

        orderDAO = new OrderDAO(this);
        order = orderDAO.select();

        if(bundle != null){
            place = (Place) bundle.getSerializable("place");

            textPlaceName = findViewById(R.id.textPlaceName);
            textPlaceName.setText(place.getName());

            textPlaceAddress = findViewById(R.id.textPlaceAddress);
            textPlaceAddress.setText(place.getAddress());
        }
    }

    public void handleContinue(View v){
        EditText description = (EditText)findViewById(R.id.editTextCardNumber);
        order.setDescription(description.getText().toString());

        orderDAO.update(order);

        Intent i = new Intent(OrderDescriptionActivity.this, OrderValueActivity.class);
        i.putExtra("place", place);
        startActivity(i);
    }

    public void handleBack(View v){
        onBackPressed();
    }
}