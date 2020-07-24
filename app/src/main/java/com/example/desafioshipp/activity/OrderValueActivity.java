package com.example.desafioshipp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.desafioshipp.DAO.OrderDAO;
import com.example.desafioshipp.R;
import com.example.desafioshipp.model.Order;
import com.example.desafioshipp.model.Place;

import java.util.Objects;

public class OrderValueActivity extends AppCompatActivity {
    private TextView textPlaceName;
    private TextView textPlaceAddress;
    private TextView textOrderDescription;
    private Order order;
    private OrderDAO orderDAO;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_value);

        Objects.requireNonNull(getSupportActionBar()).hide();

        Bundle bundle = getIntent().getExtras();

        orderDAO = new OrderDAO(this);
        order = orderDAO.select();

        textOrderDescription = findViewById(R.id.textOrderDescription);
        textOrderDescription.setText(order.getDescription());

        if(bundle != null){
            Place place = (Place) bundle.getSerializable("place");

            textPlaceName = findViewById(R.id.textPlaceName);
            textPlaceName.setText(place.getName());

            textPlaceAddress = findViewById(R.id.textPlaceAddress);
            textPlaceAddress.setText(place.getAddress());

        }

    }

    public void handleContinue(View v){
        EditText editTextOrderValue = (EditText)findViewById(R.id.editTextOrderValue);
        Double value = Double.parseDouble(editTextOrderValue.getText().toString());
        order.setValue(value);

        orderDAO.update(order);

        Intent i = new Intent(OrderValueActivity.this, OrderPaymentActivity.class);
        startActivity(i);
    }

    public void handleBack(View v){
        onBackPressed();
    }
}