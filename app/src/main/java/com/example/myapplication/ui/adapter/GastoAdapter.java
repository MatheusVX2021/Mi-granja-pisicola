package com.example.myapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.local.entity.GastoWithCategory;

import java.util.List;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {

    private List<GastoWithCategory> listaGastos;
    private OnGastoClickListener listener;

    public interface OnGastoClickListener {
        void onEditClick(GastoWithCategory gasto);
        void onDeleteClick(GastoWithCategory gasto);
    }

    public GastoAdapter(List<GastoWithCategory> listaGastos, OnGastoClickListener listener) {
        this.listaGastos = listaGastos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gasto, parent, false);
        return new GastoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        GastoWithCategory item = listaGastos.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return listaGastos != null ? listaGastos.size() : 0;
    }

    public void setGastos(List<GastoWithCategory> gastos) {
        this.listaGastos = gastos;
        notifyDataSetChanged();
    }

    static class GastoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvCategoria, tvDescripcion, tvFecha, tvMonto;
        ImageButton btnEdit, btnDelete;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivGastoIcon);
            tvCategoria = itemView.findViewById(R.id.tvGastoCategoria);
            tvDescripcion = itemView.findViewById(R.id.tvGastoDescripcion);
            tvFecha = itemView.findViewById(R.id.tvGastoFecha);
            tvMonto = itemView.findViewById(R.id.tvGastoMonto);
            btnEdit = itemView.findViewById(R.id.btnEditGasto);
            btnDelete = itemView.findViewById(R.id.btnDeleteGasto);
        }

        public void bind(GastoWithCategory item, OnGastoClickListener listener) {
            tvMonto.setText(String.format("$ %.2f", item.gasto.getMonto()));
            tvDescripcion.setText(item.gasto.getDescripcion());
            tvFecha.setText(item.gasto.getFecha());

            if (item.category != null) {
                tvCategoria.setText(item.category.getNombre());
                // Aquí podrías usar Glide o una utilidad para cargar el icono según item.category.getImagen()
                // Por ahora usamos el default ic_pez
                ivIcon.setImageResource(R.drawable.ic_pez); 
            } else {
                tvCategoria.setText("Sin categoría");
            }

            btnEdit.setOnClickListener(v -> listener.onEditClick(item));
            btnDelete.setOnClickListener(v -> listener.onDeleteClick(item));
        }
    }
}
