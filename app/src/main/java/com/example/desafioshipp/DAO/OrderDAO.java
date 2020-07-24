package com.example.desafioshipp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.desafioshipp.helper.Connection;
import com.example.desafioshipp.model.Card;
import com.example.desafioshipp.model.Order;
import com.example.desafioshipp.model.Place;

import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private Connection connection;
    private SQLiteDatabase database;

    public OrderDAO(Context context){
        connection = new Connection(context);
        database = connection.getWritableDatabase();
    }

    public long insert(Order order){
        ContentValues values = new ContentValues();

        values.put("placeId", order.getPlaceId());
        values.put("description", order.getDescription());
        values.put("value", order.getValue());
        values.put("cardId", order.getCardId());
        values.put("storeLat", order.getStoreLat());
        values.put("storeLong", order.getStoreLong());
        values.put("userLat", order.getUserLat());
        values.put("userLong", order.getStoreLong());

        return database.insert("orders", null, values);
    }

    public long update(Order order){
        ContentValues values = new ContentValues();

        values.put("placeId", order.getPlaceId());
        values.put("description", order.getDescription());
        values.put("value", order.getValue());
        values.put("cardId", order.getCardId());
        values.put("storeLat", order.getStoreLat());
        values.put("storeLong", order.getStoreLong());
        values.put("userLat", order.getUserLat());
        values.put("userLong", order.getStoreLong());

        String[] args = {Integer.toString(order.getId())};
        return database.update("orders", values, "id=?", args);
    }

    public Order select(){
        List<Order> orders = new ArrayList<>();
        Cursor cursor = database.query(
                "orders",
                new String[]{"placeId","description", "value", "id", "cardId", "storeLat", "storeLong", "userLat", "userLong"},
                null,
                null,
                null,
                null,
                null,
                null);

        while(cursor.moveToNext()){
            Order order = new Order();

            order.setPlaceId(cursor.getString(0));
            order.setDescription(cursor.getString(1));
            order.setValue(cursor.getDouble(2));
            order.setId(cursor.getInt(3));
            order.setCardId(cursor.getInt(4));
            order.setStoreLat(cursor.getDouble(5));
            order.setStoreLong(cursor.getDouble(6));
            order.setUserLat(cursor.getDouble(7));
            order.setUserLong(cursor.getDouble(8));
            orders.add(order);

        }
        if(orders.size() > 0){
            return orders.get(0);
        }
        return null;
    }

    public void clear(){
        database.execSQL("DELETE FROM orders");
    }
}
