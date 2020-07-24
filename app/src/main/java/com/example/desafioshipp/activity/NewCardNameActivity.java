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
import com.example.desafioshipp.model.Place;

import java.util.Objects;

public class NewCardNameActivity extends AppCompatActivity {
    private Button buttonNext;
    private TextView textViewCardNumber;
    private TextView textViewCardName;
    private Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_name);
        Objects.requireNonNull(getSupportActionBar()).hide();

        textViewCardNumber = findViewById(R.id.textViewCardNumber);
        textViewCardName = findViewById(R.id.textViewCardName);

        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setEnabled(false);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            card  = (Card) bundle.getSerializable("card");
            textViewCardNumber.setText(card.getNumber());
        }

        setEditTextListener();
    }

    private void setEditTextListener(){
        EditText editText = (EditText) findViewById(R.id.editTextCardName);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textViewCardName.setText(charSequence);

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
        EditText editTextCardName = findViewById(R.id.editTextCardName);
        card.setName(editTextCardName.getText().toString());

        Intent i = new Intent(NewCardNameActivity.this, NewCardValidityActivity.class);
        i.putExtra("card", card);
        startActivity(i);
    }

    public void handleBack(View v){
        onBackPressed();
    }
}