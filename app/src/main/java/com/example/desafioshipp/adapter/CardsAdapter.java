package com.example.desafioshipp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desafioshipp.R;
import com.example.desafioshipp.model.Card;
import com.example.desafioshipp.model.Place;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {
    private List<Card> cardList;

    public CardsAdapter(List<Card> cards) {
        this.cardList= cards;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_detail, parent, false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Card card = cardList.get(position);
        String cutCardNumber = card.getNumber();
        cutCardNumber.toCharArray();
        holder.textCardNumber.setText("•••• "+cutCardNumber.substring(cutCardNumber.length() -4));
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView textCardNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textCardNumber = itemView.findViewById(R.id.textViewCardNumber);
        }
    }
}
