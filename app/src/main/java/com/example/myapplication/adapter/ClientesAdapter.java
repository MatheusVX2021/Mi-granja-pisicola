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
import com.example.myapplication.data.local.entity.Cliente;
import java.util.ArrayList;
import java.util.List;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ClienteViewHolder> {

    private List<Cliente> clientes = new ArrayList<>();
    private OnClienteClickListener listener;

    public interface OnClienteClickListener {
        void onEditClick(Cliente cliente);
        void onDeleteClick(Cliente cliente);
        void onWhatsAppClick(Cliente cliente);
        void onMapsClick(Cliente cliente);
    }

    public ClientesAdapter(OnClienteClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente current = clientes.get(position);
        holder.tvNombre.setText("Nombre: " + current.getNombre());
        holder.tvTelefono.setText("Teléfono: " + current.getTelefono());
        holder.tvDireccion.setText("Dirección: " + (current.getDireccion() != null ? current.getDireccion() : ""));

        holder.ivFoto.setImageResource(R.drawable.ic_persona);

        holder.btnWhatsApp.setOnClickListener(v -> listener.onWhatsAppClick(current));
        holder.btnMaps.setOnClickListener(v -> listener.onMapsClick(current));
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(current));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(current));
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
        notifyDataSetChanged();
    }

    class ClienteViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvTelefono, tvDireccion;
        private ImageView ivFoto;
        private ImageButton btnEdit, btnDelete, btnWhatsApp, btnMaps;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreCliente);
            tvTelefono = itemView.findViewById(R.id.tvTelefonoCliente);
            tvDireccion = itemView.findViewById(R.id.tvDireccionCliente);
            ivFoto = itemView.findViewById(R.id.ivFotoCliente);
            btnEdit = itemView.findViewById(R.id.btnEditCliente);
            btnDelete = itemView.findViewById(R.id.btnDeleteCliente);
            btnWhatsApp = itemView.findViewById(R.id.btnWhatsAppCliente);
            btnMaps = itemView.findViewById(R.id.btnMapsCliente);
        }
    }
}