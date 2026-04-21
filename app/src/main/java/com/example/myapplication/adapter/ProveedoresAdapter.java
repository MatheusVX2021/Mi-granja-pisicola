package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.local.entity.Proveedor;
import java.util.ArrayList;
import java.util.List;

public class ProveedoresAdapter extends RecyclerView.Adapter<ProveedoresAdapter.ProveedorViewHolder> {

    private List<Proveedor> proveedores = new ArrayList<>();
    private OnProveedorClickListener listener;

    public interface OnProveedorClickListener {
        void onEditClick(Proveedor proveedor);
        void onDeleteClick(Proveedor proveedor);
        void onWhatsAppClick(Proveedor proveedor);
        void onMapsClick(Proveedor proveedor);
    }

    public ProveedoresAdapter(OnProveedorClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProveedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_proveedor, parent, false);
        return new ProveedorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProveedorViewHolder holder, int position) {
        Proveedor current = proveedores.get(position);
        holder.tvNombre.setText("Nombre: " + current.getNombre());
        holder.tvCelular.setText("Celular: " + current.getCelular());
        holder.tvDireccion.setText("Dirección: " + (current.getDireccion() != null ? current.getDireccion() : ""));

        if ("Huevos".equalsIgnoreCase(current.getTipo())) {
            holder.ivTipo.setImageResource(R.drawable.ic_pez);
        } else {
            holder.ivTipo.setImageResource(R.drawable.ic_estanque);
        }

        holder.btnWhatsApp.setOnClickListener(v -> listener.onWhatsAppClick(current));
        holder.btnMaps.setOnClickListener(v -> listener.onMapsClick(current));
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(current));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(current));
    }

    @Override
    public int getItemCount() {
        return proveedores.size();
    }

    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
        notifyDataSetChanged();
    }

    class ProveedorViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvCelular, tvDireccion;
        private ImageView ivTipo;
        private ImageButton btnEdit, btnDelete, btnWhatsApp, btnMaps;

        public ProveedorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProveedor);
            tvCelular = itemView.findViewById(R.id.tvCelularProveedor);
            tvDireccion = itemView.findViewById(R.id.tvDireccionProveedor);
            ivTipo = itemView.findViewById(R.id.ivTipoProveedor);
            btnEdit = itemView.findViewById(R.id.btnEditProveedor);
            btnDelete = itemView.findViewById(R.id.btnDeleteProveedor);
            btnWhatsApp = itemView.findViewById(R.id.btnWhatsAppProveedor);
            btnMaps = itemView.findViewById(R.id.btnMapsProveedor);
        }
    }
}