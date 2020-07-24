package com.example.desafioshipp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.desafioshipp.DAO.CardDAO;
import com.example.desafioshipp.DAO.OrderDAO;
import com.example.desafioshipp.R;
import com.example.desafioshipp.model.Card;
import com.example.desafioshipp.model.Order;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class OrderPaymentActivity extends AppCompatActivity {
    private Place place;
    private CardDAO cardDAO;
    private Card currentCard;
    private OrderDAO orderDAO;
    private Order currentOrder;
    private CardView cardViewCard;
    private Button continueButton;
    private TextView textOrderFee;
    private TextView textPlaceName;
    private TextView textOrderValue;
    private TextView textOrderTotal;
    private TextView textPlaceAddress;
    private TextView textViewCardNumber;
    private CardView cardViewSelectCard;
    private TextView textOrderDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_payment);
        Objects.requireNonNull(getSupportActionBar()).hide();

        connectLayout();

        orderDAO = new OrderDAO(this);
        cardDAO = new CardDAO(this);
        currentOrder = orderDAO.select();
        getGooglePlaceDetails();
    }

    private void connectLayout(){
        textOrderFee = findViewById(R.id.textOrderFee);
        cardViewCard = findViewById(R.id.cardViewCard);
        cardViewCard = findViewById(R.id.cardViewCard);
        textPlaceName = findViewById(R.id.textPlaceName);
        continueButton = findViewById(R.id.continueButton);
        textOrderValue = findViewById(R.id.textOrderValue);
        textOrderTotal = findViewById(R.id.textOrderTotal);
        textPlaceAddress = findViewById(R.id.textPlaceAddress);
        textViewCardNumber = findViewById(R.id.textViewCardNumber);
        cardViewSelectCard = findViewById(R.id.cardViewSelectCard);
        cardViewSelectCard = findViewById(R.id.cardViewSelectCard);
        textOrderDescription = findViewById(R.id.textOrderDescription);
    }

    private void getGooglePlaceDetails() {
        Places.initialize(this, getString(R.string.places_api_key));
        PlacesClient placesClient = Places.createClient(this);

        String placeId = currentOrder.getPlaceId();

        List<Place.Field>placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            place = response.getPlace();
            LatLng latLng = place.getLatLng();

            textPlaceName.setText(place.getName());
            textPlaceAddress.setText(place.getAddress());
            textOrderDescription.setText(currentOrder.getDescription());

            currentOrder.setStoreLat(latLng.latitude);
            currentOrder.setStoreLong(latLng.longitude);

            getOrderResume(currentOrder);

            if(currentOrder.getCardId() != 0){
                currentCard = cardDAO.findById(currentOrder.getCardId());
                String cutCardNumber = currentCard.getNumber();
                cutCardNumber.toCharArray();
                textViewCardNumber.setText("•••• "+cutCardNumber.substring(cutCardNumber.length() -4));
                cardViewSelectCard.setVisibility(View.GONE);
                cardViewCard.setVisibility(View.VISIBLE);
                continueButton.setEnabled(true);
            }else{
                cardViewSelectCard.setVisibility(View.VISIBLE);
                cardViewCard.setVisibility(View.GONE);
                continueButton.setEnabled(false);
            }

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Toast.makeText(OrderPaymentActivity.this, "Place not found: ", Toast.LENGTH_SHORT).show();
            }
        });;
    }

    public void handleSelectCardPress(View v){
        Intent i = new Intent(OrderPaymentActivity.this, CardsActivity.class);
        startActivity(i);
    }

    public void getOrderResume(Order currentOrder){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://k12xubei40.execute-api.sa-east-1.amazonaws.com/challenge/v1/order/resume";

        JSONObject object = new JSONObject();

        try {
            object.accumulate("store_latitude", currentOrder.getStoreLat());
            object.accumulate("store_longitude", currentOrder.getStoreLong());
            object.accumulate("user_latitude", currentOrder.getUserLat());
            object.accumulate("user_longitude", currentOrder.getUserLong());
            object.accumulate("value", currentOrder.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(url, object,
                response -> {
                    try {
                        Locale l_BR = new Locale("pt", "BR");

                        String productValue = response.get("product_value").toString();
                        String feeValue =  response.get("fee_value").toString();
                        String totalValue = response.get("total_value").toString();

                        NumberFormat formatter = NumberFormat.getCurrencyInstance(l_BR);

                        textOrderValue.setText("Valor: "+formatter.format(Double.parseDouble(productValue)));
                        textOrderFee.setText("Taxa: R$ "+formatter.format(Double.parseDouble(feeValue)));
                        textOrderTotal.setText("Total: R$ "+ formatter.format(Double.parseDouble(totalValue)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.v("Response Error: ", error.getMessage())
        );

        queue.add(req);
    }

    public void handleContinue(View v){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://k12xubei40.execute-api.sa-east-1.amazonaws.com/challenge/v1/order";

        JSONObject object = new JSONObject();

        try {
            object.accumulate("store_latitude", currentOrder.getStoreLat());
            object.accumulate("store_longitude", currentOrder.getStoreLong());
            object.accumulate("user_latitude", currentOrder.getUserLat());
            object.accumulate("user_longitude", currentOrder.getUserLong());
            object.accumulate("card_number", currentCard.getNumber());
            object.accumulate("cvv", currentCard.getCvv());
            object.accumulate("expiry_date", currentCard.getValidity());
            object.accumulate("value", currentOrder.getValue());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(url, object,
                response -> {
                    try {
                        Intent i = new Intent(OrderPaymentActivity.this, OrderConfirmedActivity.class);

                        i.putExtra("placeName", place.getName());
                        i.putExtra("placeAddress", place.getAddress());
                        i.putExtra("orderDescription", currentOrder.getDescription());
                        i.putExtra("totalValue", response.getString("value"));

                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.v("Response Error: ", error.getMessage())
        );

        queue.add(req);
    }
    public void handleBack(View v){
        onBackPressed();
    }
}