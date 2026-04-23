package com.example.myapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.model.VentaUI;
import java.util.List;

public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.VentaViewHolder> {

    private List<VentaUI> ventaList;
    private OnVentaActionListener listener;

    public interface OnVentaActionListener {
        void onEdit(VentaUI venta);
        void onDelete(VentaUI venta);
    }

    public VentaAdapter(List<VentaUI> ventaList, OnVentaActionListener listener) {
        this.ventaList = ventaList;
        this.listener = listener;
    }

    public void setVentaList(List<VentaUI> newList) {
        this.ventaList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VentaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta, parent, false);
        return new VentaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VentaViewHolder holder, int position) {
        VentaUI venta = ventaList.get(position);
        holder.bind(venta, listener);
    }

    @Override
    public int getItemCount() {
        return ventaList.size();
    }

    static class VentaViewHolder extends RecyclerView.ViewHolder {
        TextView tvLote, tvCliente, tvMonto, tvUnidades, tvPeso, tvFecha;
        ImageButton btnEdit, btnDelete;

        public VentaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLote = itemView.findViewById(R.id.tvLoteVenta);
            tvCliente = itemView.findViewById(R.id.tvClienteVenta);
            tvMonto = itemView.findViewById(R.id.tvMontoVenta);
            tvUnidades = itemView.findViewById(R.id.tvUnidadesVenta);
            tvPeso = itemView.findViewById(R.id.tvPesoVenta);
            tvFecha = itemView.findViewById(R.id.tvFechaVenta);
            btnEdit = itemView.findViewById(R.id.btnEditVenta);
            btnDelete = itemView.findViewById(R.id.btnDeleteVenta);
        }

        public void bind(VentaUI venta, OnVentaActionListener listener) {
            tvLote.setText(venta.getNombreLote());
            tvCliente.setText(venta.getNombreCliente());
            tvMonto.setText("$ " + String.format("%.2f", venta.getMonto()));
            tvUnidades.setText("Unidades: " + venta.getUnidades());
            tvPeso.setText("Peso: " + venta.getPeso() + "kg");
            tvFecha.setText(venta.getFecha());

            btnEdit.setOnClickListener(v -> listener.onEdit(venta));
            btnDelete.setOnClickListener(v -> listener.onDelete(venta));
        }
    }
}
