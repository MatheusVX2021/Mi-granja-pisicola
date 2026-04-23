package com.example.myapplication.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.model.EstanqueUI;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.List;

public class EstanqueAdapter extends RecyclerView.Adapter<EstanqueAdapter.EstanqueViewHolder> {

    private List<EstanqueUI> estanqueList;
    private OnEstanqueActionListener listener;

    public interface OnEstanqueActionListener {
        void onEdit(EstanqueUI estanque, String nuevoNombre, double nuevaArea);
        void onDelete(EstanqueUI estanque);
        void onAddImage(EstanqueUI estanque);
        void onRemoveImage(EstanqueUI estanque);
        void onAddLote(EstanqueUI estanque);
        void onAlimentar(EstanqueUI estanque);
    }

    public EstanqueAdapter(List<EstanqueUI> estanqueList, OnEstanqueActionListener listener) {
        this.estanqueList = estanqueList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EstanqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estanque, parent, false);
        return new EstanqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EstanqueViewHolder holder, int position) {
        EstanqueUI estanque = estanqueList.get(position);
        holder.tvNombre.setText(estanque.getNombre());
        holder.tvPeces.setText("Peces: " + estanque.getCantidadPeces());
        holder.tvAlimentacion.setText("Alim: " + (estanque.getUltimaAlimentacion() != null ? estanque.getUltimaAlimentacion() : "--/--/--"));
        holder.tvPeso.setText("Peso prom: " + estanque.getPesoPromedio() + "g");
        holder.tvEdad.setText("Edad: " + estanque.getEdadPromedio() + " m");
        holder.tvArea.setText("Área: " + estanque.getArea() + " m²");

        // Cargar imagen si existe
        if (estanque.getImagen() != null && !estanque.getImagen().isEmpty()) {
            try {
                holder.ivEstanque.setImageURI(Uri.parse(estanque.getImagen()));
                holder.btnEliminarImagen.setVisibility(View.VISIBLE);
            } catch (SecurityException e) {
                holder.ivEstanque.setImageResource(R.drawable.ic_estanque);
                holder.btnEliminarImagen.setVisibility(View.GONE);
                e.printStackTrace();
            }
        } else {
            holder.ivEstanque.setImageResource(R.drawable.ic_estanque);
            holder.btnEliminarImagen.setVisibility(View.GONE);
        }

        holder.ivEstanque.setOnClickListener(v -> {
            if (listener != null) listener.onAddImage(estanque);
        });

        holder.btnEliminarImagen.setOnClickListener(v -> {
            if (listener != null) listener.onRemoveImage(estanque);
        });

        holder.btnEditar.setOnClickListener(v -> mostrarDialogoEditar(v.getContext(), estanque));

        holder.btnAnadirLote.setOnClickListener(v -> {
            if (listener != null) listener.onAddLote(estanque);
        });

        holder.btnAlimentar.setOnClickListener(v -> {
            if (listener != null) listener.onAlimentar(estanque);
        });

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(estanque);
        });
    }

    private void mostrarDialogoEditar(Context context, EstanqueUI estanque) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_estanque, null);
        EditText etNombre = view.findViewById(R.id.etNombreEstanque);
        EditText etArea = view.findViewById(R.id.etAreaEstanque);

        etNombre.setText(estanque.getNombre());
        etArea.setText(String.valueOf(estanque.getArea()));

        builder.setView(view);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            String areaStr = etArea.getText().toString().trim();

            if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(areaStr)) {
                try {
                    double area = Double.parseDouble(areaStr);
                    if (listener != null) {
                        listener.onEdit(estanque, nombre, area);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Área inválida", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Ambos campos son obligatorios", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return estanqueList.size();
    }

    public static class EstanqueViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivEstanque;
        ImageButton btnEliminarImagen;
        TextView tvNombre, tvPeces, tvAlimentacion, tvPeso, tvEdad, tvArea;
        ImageButton btnAnadirLote, btnEditar, btnAlimentar, btnEliminar;

        public EstanqueViewHolder(@NonNull View itemView) {
            super(itemView);
            ivEstanque = itemView.findViewById(R.id.ivEstanque);
            btnEliminarImagen = itemView.findViewById(R.id.btnEliminarImagen);
            tvNombre = itemView.findViewById(R.id.tvNombreEstanque);
            tvPeces = itemView.findViewById(R.id.tvCantidadPeces);
            tvAlimentacion = itemView.findViewById(R.id.tvUltimaAlimentacion);
            tvPeso = itemView.findViewById(R.id.tvPesoPromedio);
            tvEdad = itemView.findViewById(R.id.tvEdadPromedio);
            tvArea = itemView.findViewById(R.id.tvArea);
            btnAnadirLote = itemView.findViewById(R.id.btnAnadirLote);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnAlimentar = itemView.findViewById(R.id.btnAlimentar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
