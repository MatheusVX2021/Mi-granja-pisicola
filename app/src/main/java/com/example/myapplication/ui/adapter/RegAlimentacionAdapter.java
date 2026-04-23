package com.example.myapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.local.entity.RegAlimentacion;
import java.util.ArrayList;
import java.util.List;

public class RegAlimentacionAdapter extends RecyclerView.Adapter<RegAlimentacionAdapter.ViewHolder> {

    private List<com.example.myapplication.data.model.RegAlimentacionUI> alimentacionList = new ArrayList<>();

    public interface OnRegAlimentacionActionListener {
        void onDelete(com.example.myapplication.data.model.RegAlimentacionUI item);
    }

    private OnRegAlimentacionActionListener listener;

    public RegAlimentacionAdapter(OnRegAlimentacionActionListener listener) {
        this.listener = listener;
    }

    public void setList(List<com.example.myapplication.data.model.RegAlimentacionUI> list) {
        this.alimentacionList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reg_alimentacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        com.example.myapplication.data.model.RegAlimentacionUI item = alimentacionList.get(position);
        holder.tvNombreAlimento.setText(item.getNombreAlimento());
        holder.tvFecha.setText(item.getFecha());
        holder.tvEstanque.setText("Estanque: " + item.getNombreEstanque());
        holder.tvKilos.setText("Cantidad: " + item.getKilos() + " kg");

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(item);
        });
    }

    @Override
    public int getItemCount() {
        return alimentacionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreAlimento, tvFecha, tvEstanque, tvKilos;
        android.widget.ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreAlimento = itemView.findViewById(R.id.tvRegAlimentoNombre);
            tvFecha = itemView.findViewById(R.id.tvRegAlimentoFecha);
            tvEstanque = itemView.findViewById(R.id.tvRegAlimentoEstanque);
            tvKilos = itemView.findViewById(R.id.tvRegAlimentoKilos);
            btnDelete = itemView.findViewById(R.id.btnDeleteAlimentacion);
        }
    }
}
