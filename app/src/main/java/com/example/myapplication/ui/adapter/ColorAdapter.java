package com.example.myapplication.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    private final List<String> colores;
    private final OnColorClickListener listener;
    private int selectedPos = -1;

    public interface OnColorClickListener {
        void onColorClick(String colorHex);
    }

    public ColorAdapter(List<String> colores, OnColorClickListener listener) {
        this.colores = colores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selector_color, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String colorHex = colores.get(position);
        
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.parseColor(colorHex));
        
        if (selectedPos == position) {
            shape.setStroke(4, Color.BLACK);
        } else {
            shape.setStroke(0, Color.TRANSPARENT);
        }
        
        holder.viewColor.setBackground(shape);

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPos;
            selectedPos = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPos);
            listener.onColorClick(colorHex);
        });
    }

    @Override
    public int getItemCount() {
        return colores.size();
    }

    public void setSelectedColor(String colorHex) {
        for (int i = 0; i < colores.size(); i++) {
            if (colores.get(i).equalsIgnoreCase(colorHex)) {
                selectedPos = i;
                notifyDataSetChanged();
                break;
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View viewColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewColor = itemView.findViewById(R.id.viewColorSelector);
        }
    }
}