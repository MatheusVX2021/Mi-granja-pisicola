package com.example.myapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.data.model.LoteUI;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import java.util.List;

public class LoteAdapter extends RecyclerView.Adapter<LoteAdapter.LoteViewHolder> {

    private List<LoteUI> loteList;
    private OnLoteActionListener listener;

    public interface OnLoteActionListener {
        void onEdit(LoteUI lote);
        void onDelete(LoteUI lote);
        void onReubicar(LoteUI lote);
        void onSacrificar(LoteUI lote);
        void onShowExtra(LoteUI lote, String option);
    }

    public LoteAdapter(List<LoteUI> loteList, OnLoteActionListener listener) {
        this.loteList = loteList;
        this.listener = listener;
    }

    public void setLoteList(List<LoteUI> newList) {
        this.loteList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lote, parent, false);
        return new LoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoteViewHolder holder, int position) {
        LoteUI lote = loteList.get(position);
        holder.bind(lote, listener);
    }

    @Override
    public int getItemCount() {
        return loteList.size();
    }

    static class LoteViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLote;
        TextView tvNombreLote, tvCantIni, tvCantAct, tvCantSac, tvCantVen, tvFecha, tvEdad, tvPeso;
        TextView tvEspecie, tvEstanque, tvProveedor;
        ImageButton btnEdit, btnReubicar, btnDelete, btnSacrificar;
        ImageButton btnMoreOptions;

        public LoteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLote = itemView.findViewById(R.id.ivLote);
            tvNombreLote = itemView.findViewById(R.id.tvNombreLote);
            tvCantIni = itemView.findViewById(R.id.tvCantIni);
            tvCantAct = itemView.findViewById(R.id.tvCantAct);
            tvCantSac = itemView.findViewById(R.id.tvCantSac);
            tvCantVen = itemView.findViewById(R.id.tvCantVen);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvEdad = itemView.findViewById(R.id.tvEdad);
            tvPeso = itemView.findViewById(R.id.tvPeso);
            tvEspecie = itemView.findViewById(R.id.tvEspecie);
            tvEstanque = itemView.findViewById(R.id.tvEstanque);
            tvProveedor = itemView.findViewById(R.id.tvProveedor);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnReubicar = itemView.findViewById(R.id.btnReubicar);
            btnSacrificar = itemView.findViewById(R.id.btnSacrificar);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnMoreOptions = itemView.findViewById(R.id.btnMoreOptions);
        }

        public void bind(LoteUI lote, OnLoteActionListener listener) {
            tvNombreLote.setText(lote.getNombre());
            tvEspecie.setText(lote.getNombreEspecie());
            tvEstanque.setText(lote.getNombreEstanque());
            tvProveedor.setText(lote.getNombreProveedor());
            
            tvCantIni.setText(String.valueOf(lote.getCant_ini()));
            tvCantAct.setText(String.valueOf(lote.getCant_act()));
            tvCantSac.setText(String.valueOf(lote.getCant_sac()));
            tvCantVen.setText(String.valueOf(lote.getCant_ven()));
            
            tvFecha.setText("F: " + lote.getFecha_entrada());
            tvPeso.setText("P: " + lote.getPeso_promedio() + "g");
            tvEdad.setText("E: " + lote.getEdad() + " m");

            if (lote.getImagenEspecie() != null) {
                Glide.with(itemView.getContext()).load(lote.getImagenEspecie()).into(ivLote);
            } else {
                ivLote.setImageResource(R.drawable.ic_launcher_background);
            }

            btnEdit.setOnClickListener(v -> listener.onEdit(lote));
            btnReubicar.setOnClickListener(v -> listener.onReubicar(lote));
            btnSacrificar.setOnClickListener(v -> listener.onSacrificar(lote));
            btnDelete.setOnClickListener(v -> listener.onDelete(lote));

            btnMoreOptions.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.getMenu().add("Ver Especie");
                popup.getMenu().add("Ver Estanque");
                popup.getMenu().add("Ver Proveedor");
                popup.setOnMenuItemClickListener(item -> {
                    listener.onShowExtra(lote, item.getTitle().toString());
                    return true;
                });
                popup.show();
            });
        }
    }
}
