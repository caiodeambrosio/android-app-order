package com.example.desafioshipp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.desafioshipp.helper.Connection;
import com.example.desafioshipp.model.Card;

import java.util.ArrayList;
import java.util.List;

public class CardDAO {
    private Connection connection;
    private SQLiteDatabase database;

    public CardDAO(Context context){
        connection = new Connection(context);
        database = connection.getWritableDatabase();
    }

    public long insert(Card card){
        ContentValues values = new ContentValues();
        values.put("number", card.getNumber());
        values.put("name", card.getName());
        values.put("validity", card.getValidity());
        values.put("cvv", card.getCvv());
        values.put("cpf", card.getCpf());

        return database.insert("cards", null, values);
    }

    public List<Card> select(){
        List<Card> cards = new ArrayList<>();
        Cursor cursor = database.query(
                "cards",
                new String[]{"name","number","cvv","cpf", "validity", "id"},
                null,
                null,
                null,
                null,
                null,
                null);

        while(cursor.moveToNext()){
            Card card = new Card();
            card.setName(cursor.getString(0));
            card.setNumber(cursor.getString(1));
            card.setCvv(cursor.getString(2));
            card.setCpf(cursor.getString(3));
            card.setValidity(cursor.getString(4));
            card.setId(cursor.getInt(5));
            cards.add(card);
        }
        return cards;
    }

    public Card findById(int id){
        List<Card> cards = new ArrayList<>();

        Cursor cursor = database.rawQuery("" +
                "select " +
                "id," +
                "name," +
                "number," +
                "cvv," +
                "cpf," +
                "validity" +
                " from cards where id = " + id,
                null);

        while(cursor.moveToNext()){
            Card card = new Card();
            card.setId(cursor.getInt(0));
            card.setName(cursor.getString(1));
            card.setNumber(cursor.getString(2));
            card.setCvv(cursor.getString(3));
            card.setCpf(cursor.getString(4));
            card.setValidity(cursor.getString(5));
            cards.add(card);
        }

        if(cards.size() > 0){
            return cards.get(0);
        }
        return null;
    }
}
