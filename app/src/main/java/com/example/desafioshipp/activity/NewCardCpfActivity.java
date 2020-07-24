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
import android.widget.Toast;

import com.example.desafioshipp.DAO.CardDAO;
import com.example.desafioshipp.R;
import com.example.desafioshipp.model.Card;

import java.util.Objects;

public class NewCardCpfActivity extends AppCompatActivity {
    private Button buttonNext;
    private TextView textViewCardCvv;
    private EditText editTextCardCpf;
    private Card card;
    private CardDAO cardDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_cpf);

        Objects.requireNonNull(getSupportActionBar()).hide();

        textViewCardCvv = findViewById(R.id.textViewCardCvv);
        editTextCardCpf = findViewById(R.id.editTextCardCpf);

        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setEnabled(false);

        cardDAO = new CardDAO(this);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            card  = (Card) bundle.getSerializable("card");
            textViewCardCvv.setText(card.getCvv());
        }

        setEditTextListener();
    }

    private void setEditTextListener(){
        editTextCardCpf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
        card.setCpf(editTextCardCpf.getText().toString());
        long id = cardDAO.insert(card);
        Toast.makeText(this, "Cartão salvo com sucesso", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(NewCardCpfActivity.this, CardsActivity.class);
        startActivity(i);
    }

    public void handleBack(View v){
        onBackPressed();
    }
}