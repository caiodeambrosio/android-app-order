package com.example.desafioshipp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.desafioshipp.DAO.OrderDAO;
import com.example.desafioshipp.R;
import com.example.desafioshipp.adapter.PlacesAdapter;
import com.example.desafioshipp.helper.Permissions;
import com.example.desafioshipp.helper.RecyclerItemClickListener;
import com.example.desafioshipp.model.Order;
import com.example.desafioshipp.model.Place;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private PlacesClient placesClient;
    private AutocompleteSessionToken token;
    private RectangularBounds bounds;
    private FindAutocompletePredictionsRequest request;
    private Location currentLocation;
    private LocationManager locationManager;
    private RecyclerView recyclerPlaces;
    private Order order;
    private EditText fieldPlaces;
    private OrderDAO orderDAO;
    private List<Place> places = new ArrayList<>();
    private String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        connectLayout();

        order = new Order();
        orderDAO = new OrderDAO(this);

        setGeolocationListener();
        getCurrentGeolocation();
        setGooglePlacesConfig();
        setFieldSearchListener();
        setRecyclerPlacesConfig();

    }

    private void connectLayout(){
        fieldPlaces = findViewById(R.id.fieldPlaces);
        recyclerPlaces = findViewById(R.id.recyclerPlaces);
    }

    private void setGeolocationListener(){
        Permissions.validatePermissions(permissions, this, 1);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    private void getCurrentGeolocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    100,
                    location -> {
                        if(currentLocation == null){
                            currentLocation = location;
                            fieldPlaces.setEnabled(true);
                        }
                    }
            );

        }
    }

    private void setGooglePlacesConfig() {
        Places.initialize(this, getString(R.string.places_api_key));
        placesClient = Places.createClient(this);
        token = AutocompleteSessionToken.newInstance();

        LatLng latLng1 = new LatLng(-33.880490, 151.184363);
        LatLng latLng2 = new LatLng(-33.858754, 151.229596);

        bounds = RectangularBounds.newInstance(latLng1,latLng2);
    }

    private void setFieldSearchListener() {
        fieldPlaces.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(currentLocation != null && charSequence.length() > 3){
                    getGooglePlacesPredictionList(charSequence);
                }else{
                    places.removeAll(places);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void getGooglePlacesPredictionList(CharSequence charSequence){
        request = FindAutocompletePredictionsRequest.builder()
//                        .setLocationRestriction(bounds)
                .setOrigin(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .setCountry("BR")
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setSessionToken(token)
                .setQuery(charSequence.toString())
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            places.removeAll(places);
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                Place place = new Place(
                        prediction.getPlaceId(),
                        prediction.getPrimaryText(null).toString(),
                        prediction.getSecondaryText(null).toString()
                );
                places.add(place);
            }

            setRecyclerPlacesAdapter();
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Toast.makeText(MainActivity.this, "Place not found: ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerPlacesConfig(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerPlaces.setLayoutManager(layoutManager);
        recyclerPlaces.addOnItemTouchListener(
            new RecyclerItemClickListener(
                getApplicationContext(),
                recyclerPlaces,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Place place = places.get(position);

                        order.setUserLat(currentLocation.getAltitude());
                        order.setUserLong(currentLocation.getLongitude());
                        order.setPlaceId(place.getId());

                        orderDAO.clear();
                        orderDAO.insert(order);

                        Intent i = new Intent(MainActivity.this, OrderDescriptionActivity.class);
                        i.putExtra("place", place);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
            )
        );
    }

    private void setRecyclerPlacesAdapter(){
        PlacesAdapter adapter = new PlacesAdapter(places);
        recyclerPlaces.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissionResult : grantResults) {

            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                validationPermissionAlert();
            } else if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                getCurrentGeolocation();
            }
        }
    }

    private void validationPermissionAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}