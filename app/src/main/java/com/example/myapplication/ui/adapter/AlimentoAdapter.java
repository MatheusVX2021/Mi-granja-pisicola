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
import com.example.myapplication.data.local.entity.AlimentoWithProveedor;

import java.util.List;

public class AlimentoAdapter extends RecyclerView.Adapter<AlimentoAdapter.AlimentoViewHolder> {

    private List<AlimentoWithProveedor> listaAlimentos;
    private OnAlimentoClickListener listener;

    public interface OnAlimentoClickListener {
        void onEditClick(AlimentoWithProveedor alimento);
        void onDeleteClick(AlimentoWithProveedor alimento);
    }
    public AlimentoAdapter(List<AlimentoWithProveedor> listaAlimentos, OnAlimentoClickListener listener) {
        this.listaAlimentos = listaAlimentos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlimentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alimento, parent, false);
        return new AlimentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlimentoViewHolder holder, int position) {
        AlimentoWithProveedor item = listaAlimentos.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return listaAlimentos != null ? listaAlimentos.size() : 0;
    }

    public void setAlimentos(List<AlimentoWithProveedor> alimentos) {
        this.listaAlimentos = alimentos;
        notifyDataSetChanged();
    }

    static class AlimentoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvNombre, tvProveedor, tvDetalles;
        ImageButton btnEdit, btnDelete;

        public AlimentoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivAlimentoIcon);
            tvNombre = itemView.findViewById(R.id.tvAlimentoNombre);
            tvProveedor = itemView.findViewById(R.id.tvAlimentoProveedor);
            tvDetalles = itemView.findViewById(R.id.tvAlimentoDetalles);
            btnEdit = itemView.findViewById(R.id.btnEditAlimento);
            btnDelete = itemView.findViewById(R.id.btnDeleteAlimento);
        }

        public void bind(AlimentoWithProveedor item, OnAlimentoClickListener listener) {
            tvNombre.setText(item.alimento.getNombre());
            
            if (item.proveedor != null) {
                tvProveedor.setText("Proveedor: " + item.proveedor.getNombre());
            } else {
                tvProveedor.setText("Proveedor: No asignado");
            }

            String detalles = String.format("Tipo: %s | Unidades: %d | Peso: %d kg", 
                    item.alimento.getTipo(), 
                    item.alimento.getUnidades(), 
                    item.alimento.getPeso());
            tvDetalles.setText(detalles);

            btnEdit.setOnClickListener(v -> listener.onEditClick(item));
            btnDelete.setOnClickListener(v -> listener.onDeleteClick(item));
        }
    }
}
