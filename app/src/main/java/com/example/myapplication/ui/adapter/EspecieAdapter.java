package com.example.myapplication.ui.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.local.entity.Especie;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.List;

public class EspecieAdapter extends RecyclerView.Adapter<EspecieAdapter.EspecieViewHolder> {

    private List<Especie> especieList;
    private OnEspecieClickListener listener;

    public interface OnEspecieClickListener {
        void onEditClick(Especie especie);
        void onDeleteClick(Especie especie);
        void onImageClick(Especie especie);
    }

    public EspecieAdapter(List<Especie> especieList, OnEspecieClickListener listener) {
        this.especieList = especieList;
        this.listener = listener;
    }

    public void setEspecies(List<Especie> especies) {
        this.especieList = especies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EspecieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_especie, parent, false);
        return new EspecieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EspecieViewHolder holder, int position) {
        Especie especie = especieList.get(position);
        holder.tvNombre.setText(especie.getNombre());
        holder.tvMaduracion.setText("Maduración: " + especie.getTm() + " meses");

        if (especie.getImagen() != null && !especie.getImagen().isEmpty()) {
            holder.ivEspecie.setImageURI(Uri.parse(especie.getImagen()));
        } else {
            holder.ivEspecie.setImageResource(R.drawable.ic_estanque);
        }

        holder.ivEspecie.setOnClickListener(v -> listener.onImageClick(especie));
        holder.btnEditar.setOnClickListener(v -> listener.onEditClick(especie));
        holder.btnEliminar.setOnClickListener(v -> listener.onDeleteClick(especie));
    }

    @Override
    public int getItemCount() {
        return especieList.size();
    }

    public static class EspecieViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivEspecie;
        TextView tvNombre, tvMaduracion;
        ImageButton btnEditar, btnEliminar;

        public EspecieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivEspecie = itemView.findViewById(R.id.ivEspecie);
            tvNombre = itemView.findViewById(R.id.tvNombreEspecie);
            tvMaduracion = itemView.findViewById(R.id.tvTiempoMaduracion);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
