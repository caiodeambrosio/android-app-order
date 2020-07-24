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

public class NewCardNumberActivity extends AppCompatActivity {
    private Button buttonNext;
    private TextView textViewCardNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_number);

        textViewCardNumber = findViewById(R.id.textViewCardNumber);

        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setEnabled(false);

        Objects.requireNonNull(getSupportActionBar()).hide();

        setEditTextListener();
    }

    private void setEditTextListener(){
        EditText editText = (EditText) findViewById(R.id.editTextCardNumber);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textViewCardNumber.setText(charSequence);
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

        EditText editTextCardNumber = (EditText)findViewById(R.id.editTextCardNumber);

        Card card = new Card();
        card.setNumber(editTextCardNumber.getText().toString());

        Intent i = new Intent(NewCardNumberActivity.this, NewCardNameActivity.class);
        i.putExtra("card", card);
        startActivity(i);
    }

    public void handleBack(View v){
        onBackPressed();
    }
}