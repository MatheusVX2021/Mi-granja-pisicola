package com.example.myapplication.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.local.entity.Cliente;
import com.example.myapplication.data.local.entity.Lote;
import com.example.myapplication.data.local.entity.Venta;
import com.example.myapplication.data.model.VentaUI;
import com.example.myapplication.data.repository.ClienteRep;
import com.example.myapplication.data.repository.LoteRep;
import com.example.myapplication.data.repository.VentaRep;
import com.example.myapplication.ui.adapter.VentaAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VentasFragment extends Fragment implements VentaAdapter.OnVentaActionListener {

    private RecyclerView rvVentas;
    private VentaAdapter adapter;
    private List<VentaUI> ventaList = new ArrayList<>();
    private FloatingActionButton fabAddVenta;
    private VentaRep ventaRep;
    private LoteRep loteRep;
    private ClienteRep clienteRep;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ventas, container, false);

        rvVentas = view.findViewById(R.id.rvVentas);
        rvVentas.setLayoutManager(new LinearLayoutManager(getContext()));

        fabAddVenta = view.findViewById(R.id.fabAddVenta);

        ventaRep = new VentaRep(getActivity().getApplication());
        loteRep = new LoteRep(getActivity().getApplication());
        clienteRep = new ClienteRep(getActivity().getApplication());

        adapter = new VentaAdapter(ventaList, this);
        rvVentas.setAdapter(adapter);

        fabAddVenta.setOnClickListener(v -> mostrarDialogoVenta(null));

        cargarDatos();

        return view;
    }

    private void mostrarDialogoVenta(@Nullable VentaUI ventaUI) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_venta, null);
        Spinner spinnerLote = view.findViewById(R.id.spinnerLoteVenta);
        Spinner spinnerCliente = view.findViewById(R.id.spinnerClienteVenta);
        TextInputEditText etUnidades = view.findViewById(R.id.etUnidadesVenta);
        TextInputEditText etPeso = view.findViewById(R.id.etPesoVenta);
        TextInputEditText etMonto = view.findViewById(R.id.etMontoVenta);

        if (ventaUI != null) {
            etUnidades.setText(String.valueOf(ventaUI.getUnidades()));
            etPeso.setText(String.valueOf(ventaUI.getPeso()));
            etMonto.setText(String.valueOf(ventaUI.getMonto()));
        }

        executor.execute(() -> {
            List<Lote> lotes = loteRep.getAllLotes();
            List<Cliente> clientes = clienteRep.getAllClientes();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    ArrayAdapter<Lote> loteAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lotes);
                    loteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLote.setAdapter(loteAdapter);

                    ArrayAdapter<Cliente> clienteAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, clientes);
                    clienteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCliente.setAdapter(clienteAdapter);

                    if (ventaUI != null) {
                        for (int i = 0; i < lotes.size(); i++) {
                            if (lotes.get(i).getId() == ventaUI.getIdLote()) {
                                spinnerLote.setSelection(i);
                                break;
                            }
                        }
                        for (int i = 0; i < clientes.size(); i++) {
                            if (clientes.get(i).getId() == ventaUI.getIdCliente()) {
                                spinnerCliente.setSelection(i);
                                break;
                            }
                        }
                    }
                });
            }
        });

        builder.setView(view);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            Lote lote = (Lote) spinnerLote.getSelectedItem();
            Cliente cliente = (Cliente) spinnerCliente.getSelectedItem();
            String uniStr = etUnidades.getText().toString();
            String pesoStr = etPeso.getText().toString();
            String montoStr = etMonto.getText().toString();

            if (lote == null || cliente == null || TextUtils.isEmpty(uniStr) || TextUtils.isEmpty(pesoStr) || TextUtils.isEmpty(montoStr)) {
                Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            int unidades = Integer.parseInt(uniStr);
            int peso = Integer.parseInt(pesoStr);
            double monto = Double.parseDouble(montoStr);

            executor.execute(() -> {
                Lote loteActualizado = loteRep.getLoteById(lote.getId());
                int limiteSacrificados = loteActualizado.getCant_sac();
                
                // Si es edición, devolvemos temporalmente las unidades anteriores al límite para validar
                if (ventaUI != null) {
                    Venta ventaAnterior = ventaRep.getVentaById(ventaUI.getId());
                    if (ventaAnterior != null && ventaAnterior.getIdLote() == lote.getId()) {
                        limiteSacrificados += ventaAnterior.getUnidades();
                    }
                }

                final int finalLimite = limiteSacrificados;
                if (unidades > limiteSacrificados) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "La cantidad vendida (" + unidades + ") no puede superar los peces sacrificados disponibles (" + finalLimite + ")", Toast.LENGTH_LONG).show();
                        });
                    }
                    return;
                }

                if (ventaUI == null) {
                    // Nueva Venta
                    String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    Venta nueva = new Venta(lote.getId(), cliente.getId(), unidades, peso, monto, fecha);
                    ventaRep.insert(nueva);
                    
                    // Restar de sacrificados y sumar a vendidos en el lote
                    loteActualizado.setCant_sac(loteActualizado.getCant_sac() - unidades);
                    loteActualizado.setCant_ven(loteActualizado.getCant_ven() + unidades);
                    loteRep.update(loteActualizado);
                } else {
                    // Editar Venta
                    Venta edit = ventaRep.getVentaById(ventaUI.getId());
                    if (edit != null) {
                        // Si cambió el lote o la cantidad, debemos ajustar los lotes involucrados
                        if (edit.getIdLote() == lote.getId()) {
                            // Mismo lote, ajustamos la diferencia
                            int diferencia = unidades - edit.getUnidades();
                            loteActualizado.setCant_sac(loteActualizado.getCant_sac() - diferencia);
                            loteActualizado.setCant_ven(loteActualizado.getCant_ven() + diferencia);
                            loteRep.update(loteActualizado);
                        } else {
                            // Lote diferente, revertimos el anterior y afectamos el nuevo
                            Lote loteAnterior = loteRep.getLoteById(edit.getIdLote());
                            if (loteAnterior != null) {
                                loteAnterior.setCant_sac(loteAnterior.getCant_sac() + edit.getUnidades());
                                loteAnterior.setCant_ven(loteAnterior.getCant_ven() - edit.getUnidades());
                                loteRep.update(loteAnterior);
                            }
                            loteActualizado.setCant_sac(loteActualizado.getCant_sac() - unidades);
                            loteActualizado.setCant_ven(loteActualizado.getCant_ven() + unidades);
                            loteRep.update(loteActualizado);
                        }

                        edit.setIdLote(lote.getId());
                        edit.setIdCliente(cliente.getId());
                        edit.setUnidades(unidades);
                        edit.setPeso(peso);
                        edit.setMonto(monto);
                        ventaRep.update(edit);
                    }
                }
                cargarDatos();
            });
        });
        builder.setView(view);
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void cargarDatos() {
        executor.execute(() -> {
            List<VentaUI> lista = ventaRep.getAllVentasUI();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapter.setVentaList(lista);
                });
            }
        });
    }

    @Override
    public void onEdit(VentaUI venta) {
        mostrarDialogoVenta(venta);
    }

    @Override
    public void onDelete(VentaUI venta) {
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Venta")
                .setMessage("¿Estás seguro de eliminar esta venta?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    executor.execute(() -> {
                        Venta v = ventaRep.getVentaById(venta.getId());
                        if (v != null) {
                            ventaRep.delete(v);
                            cargarDatos();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
