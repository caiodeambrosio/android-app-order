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

public class NewCardCvvActivity extends AppCompatActivity {
    private Button buttonNext;
    private TextView textViewCardCvv;
    private EditText editTextCardCvv;
    private Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_cvv);

        Objects.requireNonNull(getSupportActionBar()).hide();

        textViewCardCvv = findViewById(R.id.textViewCardCvv);
        editTextCardCvv = findViewById(R.id.editTextCardCvv);

        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setEnabled(false);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            card  = (Card) bundle.getSerializable("card");
        }

        setEditTextListener();
    }

    private void setEditTextListener(){
        editTextCardCvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textViewCardCvv.setText(charSequence);

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
        card.setCvv(editTextCardCvv.getText().toString());

        Intent i = new Intent(NewCardCvvActivity.this, NewCardCpfActivity.class);
        i.putExtra("card", card);
        startActivity(i);
    }

    public void handleBack(View v){
        onBackPressed();
    }
}