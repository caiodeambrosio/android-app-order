package com.example.desafioshipp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.desafioshipp.R;
import com.example.desafioshipp.model.Card;

import java.util.Objects;

public class NewCardValidityActivity extends AppCompatActivity {
    private Button buttonNext;
    private TextView textViewCardNumber;
    private TextView textViewCardName;
    private TextView textViewCardValidity;
    private EditText editTextCardValidity;
    private Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_validity);

        Objects.requireNonNull(getSupportActionBar()).hide();

        textViewCardNumber = findViewById(R.id.textViewCardNumber);
        textViewCardName = findViewById(R.id.textViewCardName);
        textViewCardValidity= findViewById(R.id.textViewCardValidity);

        editTextCardValidity = findViewById(R.id.editTextCardValidity);

        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setEnabled(false);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            card  = (Card) bundle.getSerializable("card");
            textViewCardNumber.setText(card.getNumber());
            textViewCardName.setText(card.getName());
        }

        setEditTextListener();
    }

    private void setEditTextListener(){
        editTextCardValidity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textViewCardValidity.setText(charSequence);

                if(charSequence.length() == 0){
                    buttonNext.setEnabled(false);
                }else{
                    buttonNext.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    public void handleContinue(View v){
        card.setValidity(editTextCardValidity.getText().toString());

        Intent i = new Intent(NewCardValidityActivity.this, NewCardCvvActivity.class);
        i.putExtra("card", card);
        startActivity(i);
    }

    public void handleBack(View v){
        onBackPressed();
    }
}