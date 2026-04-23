package com.example.myapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.model.RegReubicacionUI;
import java.util.List;

public class RegReubicacionAdapter extends RecyclerView.Adapter<RegReubicacionAdapter.ViewHolder> {

    public interface OnRegReubicacionActionListener {
        void onDelete(RegReubicacionUI item);
    }

    private List<RegReubicacionUI> list;
    private OnRegReubicacionActionListener listener;

    public RegReubicacionAdapter(List<RegReubicacionUI> list, OnRegReubicacionActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void setList(List<RegReubicacionUI> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reg_reubicacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RegReubicacionUI item = list.get(position);
        holder.tvLoteNombre.setText(item.getNombreLote());
        holder.tvFechaReubicacion.setText(item.getFecha());
        holder.tvUnidadesReubicadas.setText(item.getUnidades() + " uds");
        holder.tvEstanqueOrigen.setText(item.getNombreEstanqueOrigen());
        holder.tvEstanqueDestino.setText(item.getNombreEstanqueDestino());

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(item);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLoteNombre, tvFechaReubicacion, tvUnidadesReubicadas, tvEstanqueOrigen, tvEstanqueDestino;
        android.widget.ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLoteNombre = itemView.findViewById(R.id.tvLoteNombre);
            tvFechaReubicacion = itemView.findViewById(R.id.tvFechaReubicacion);
            tvUnidadesReubicadas = itemView.findViewById(R.id.tvUnidadesReubicadas);
            tvEstanqueOrigen = itemView.findViewById(R.id.tvEstanqueOrigen);
            tvEstanqueDestino = itemView.findViewById(R.id.tvEstanqueDestino);
            btnDelete = itemView.findViewById(R.id.btnDeleteReubicacion);
        }
    }
}
