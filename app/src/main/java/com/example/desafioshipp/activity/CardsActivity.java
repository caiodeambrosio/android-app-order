package com.example.desafioshipp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.desafioshipp.DAO.CardDAO;
import com.example.desafioshipp.DAO.OrderDAO;
import com.example.desafioshipp.R;
import com.example.desafioshipp.adapter.CardsAdapter;
import com.example.desafioshipp.helper.RecyclerItemClickListener;
import com.example.desafioshipp.model.Card;
import com.example.desafioshipp.model.Order;

import java.util.List;
import java.util.Objects;

public class CardsActivity extends AppCompatActivity {
    private RecyclerView recyclerCards;
    private CardView cardView;
    private CardDAO cardDAO;
    private List<Card> cards;
    private Order order;
    private OrderDAO orderDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        Objects.requireNonNull(getSupportActionBar()).hide();

        cardDAO = new CardDAO(this);
        cards = cardDAO.select();

        orderDAO = new OrderDAO(this);
        order = orderDAO.select();

        if(cards.size() > 0){
            cardView = findViewById(R.id.cardList);
            cardView.setVisibility(View.GONE);
        }

        setRecyclerCardsConfig();
    }

    public void handleAddCardPress(View v){
        Intent i = new Intent(CardsActivity.this, NewCardNumberActivity.class);
        startActivity(i);
    }

    private void setRecyclerCardsConfig(){
        recyclerCards = findViewById(R.id.recyclerCards);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerCards.setLayoutManager(layoutManager);
        recyclerCards.setHasFixedSize(true);

        CardsAdapter cardsAdapter = new CardsAdapter(cards);
        recyclerCards.setAdapter(cardsAdapter);

        recyclerCards.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerCards,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Card card = cards.get(position);

                                order.setCardId(card.getId());
                                orderDAO.update(order);

                                Intent i = new Intent(CardsActivity.this, OrderPaymentActivity.class);
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

    public void handleBack(View v){
        Intent i = new Intent(CardsActivity.this, OrderPaymentActivity.class);
        startActivity(i);
    }
}