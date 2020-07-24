package com.example.desafioshipp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desafioshipp.R;
import com.example.desafioshipp.model.Place;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder> {
    private List<Place> placeList;

    public PlacesAdapter(List<Place> places) {
        this.placeList= places;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_detail, parent, false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Place place = placeList.get(position);

        holder.textPlaceName.setText(place.getName());
        holder.textPlaceAddress.setText(place.getAddress());
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView textPlaceName;
        private TextView textPlaceAddress;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textPlaceName = itemView.findViewById(R.id.textPlaceName);
            textPlaceAddress = itemView.findViewById(R.id.textPlaceAddress);
        }
    }
}
