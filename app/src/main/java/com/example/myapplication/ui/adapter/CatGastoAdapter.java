package com.example.myapplication.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.local.entity.CatGasto;

import java.util.List;

public class CatGastoAdapter extends RecyclerView.Adapter<CatGastoAdapter.ViewHolder> {

    private List<CatGasto> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CatGasto cat);
        void onDeleteClick(CatGasto cat);
    }

    public CatGastoAdapter(List<CatGasto> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public void setLista(List<CatGasto> lista) {
        this.lista = lista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cat_gasto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CatGasto cat = lista.get(position);
        holder.tvNombre.setText(cat.getNombre());
        
        // Configurar color de fondo del card (o un círculo detrás del icono)
        try {
            int color = Color.parseColor(cat.getColor());
            holder.cardCatGasto.setCardBackgroundColor(color);
        } catch (Exception e) {
            holder.cardCatGasto.setCardBackgroundColor(Color.LTGRAY);
        }

        // Configurar icono
        int resId = holder.itemView.getContext().getResources().getIdentifier(cat.getImagen(), "drawable", holder.itemView.getContext().getPackageName());
        if (resId != 0) {
            holder.ivIcon.setImageResource(resId);
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_pez);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(cat));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(cat));
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        ImageView ivIcon;
        ImageButton btnDelete;
        CardView cardCatGasto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvCatNombre);
            ivIcon = itemView.findViewById(R.id.ivCatIcon);
            btnDelete = itemView.findViewById(R.id.btnDeleteCat);
            cardCatGasto = itemView.findViewById(R.id.cardCatGasto);
        }
    }
}