package com.example.myapplication.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.local.entity.Especie;
import com.example.myapplication.data.local.entity.Estanque;
import com.example.myapplication.data.local.entity.RegReubicacion;
import com.example.myapplication.data.local.entity.Estanque;
import com.example.myapplication.data.local.entity.Lote;
import com.example.myapplication.data.repository.EstanqueRep;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.example.myapplication.data.local.entity.Proveedor;
import com.example.myapplication.data.model.LoteUI;
import com.example.myapplication.data.repository.EspecieRep;
import com.example.myapplication.data.repository.EstanqueRep;
import com.example.myapplication.data.repository.LoteRep;
import com.example.myapplication.data.repository.ProveedorRep;
import com.example.myapplication.ui.adapter.LoteAdapter;
import com.example.myapplication.ui.adapter.SpinnerWithIconAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LotesFragment extends Fragment implements LoteAdapter.OnLoteActionListener {

    private RecyclerView rvLotes;
    private LoteAdapter adapter;
    private List<LoteUI> loteList = new ArrayList<>();
    private FloatingActionButton fabAddLote;
    private LoteRep loteRep;
    private EspecieRep especieRep;
    private EstanqueRep estanqueRep;
    private ProveedorRep proveedorRep;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lotes, container, false);

        rvLotes = view.findViewById(R.id.rvLotes);
        rvLotes.setLayoutManager(new LinearLayoutManager(getContext()));

        fabAddLote = view.findViewById(R.id.fabAddLote);

        loteRep = new LoteRep(getActivity().getApplication());
        especieRep = new EspecieRep(getActivity().getApplication());
        estanqueRep = new EstanqueRep(getActivity().getApplication());
        proveedorRep = new ProveedorRep(getActivity().getApplication());

        adapter = new LoteAdapter(loteList, this);
        rvLotes.setAdapter(adapter);

        fabAddLote.setOnClickListener(v -> {
            mostrarDialogoLote(null);
        });

        return view;
    }

    private void mostrarDialogoLote(@Nullable LoteUI loteUI) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_lote, null);
        TextInputEditText etNombre = view.findViewById(R.id.etNombreLote);
        Spinner spinnerEspecie = view.findViewById(R.id.spinnerEspecie);
        Spinner spinnerEstanque = view.findViewById(R.id.spinnerEstanque);
        Spinner spinnerProveedor = view.findViewById(R.id.spinnerProveedor);
        TextInputEditText etCantIni = view.findViewById(R.id.etCantIni);
        TextInputEditText etEdad = view.findViewById(R.id.etEdad);
        TextInputEditText etCantSac = view.findViewById(R.id.etCantSac);
        TextInputEditText etPeso = view.findViewById(R.id.etPeso);

        View layoutCantIni = view.findViewById(R.id.layoutCantIni);
        View layoutEdad = view.findViewById(R.id.layoutEdad);
        View layoutCantSac = view.findViewById(R.id.layoutCantSac);

        if (loteUI != null) {
            spinnerEspecie.setEnabled(true); // Permitir ver/cambiar en edición
            spinnerEstanque.setEnabled(true);
            spinnerProveedor.setEnabled(true);
            etCantIni.setEnabled(false); // Cantidad inicial no se edita generalmente
            layoutEdad.setVisibility(View.GONE); // La edad inicial no se edita
            layoutCantSac.setVisibility(View.VISIBLE); // Mostrar para editar

            etNombre.setText(loteUI.getNombre());
            etCantSac.setText(String.valueOf(loteUI.getCant_sac()));
            etPeso.setText(String.valueOf(loteUI.getPeso_promedio()));
        } else {
            layoutCantIni.setVisibility(View.VISIBLE);
            layoutEdad.setVisibility(View.VISIBLE);
            layoutCantSac.setVisibility(View.GONE);
            etCantSac.setText("0");
        }

        executor.execute(() -> {
            List<Especie> especies = new ArrayList<>();
            especies.add(new Especie("Seleccionar Especie...", null, 0));
            especies.addAll(especieRep.getAllEspecies());

            List<Estanque> estanques = new ArrayList<>();
            estanques.add(new Estanque("Seleccionar Estanque...", 0, null));
            estanques.addAll(estanqueRep.getAllEstanques());

            List<Proveedor> proveedores = new ArrayList<>();
            proveedores.add(new Proveedor("Ninguno (Opcional)", null, null, null));
            
            // Filtrar solo proveedores de tipo "Huevos"
            List<Proveedor> todosProv = proveedorRep.getAllProveedores();
            for (Proveedor p : todosProv) {
                if (p.getTipo() != null && p.getTipo().equalsIgnoreCase("Huevos")) {
                    proveedores.add(p);
                }
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    SpinnerWithIconAdapter<Especie> espAdapter = new SpinnerWithIconAdapter<>(getContext(), especies, R.drawable.nic_especie);
                    spinnerEspecie.setAdapter(espAdapter);

                    SpinnerWithIconAdapter<Estanque> estAdapter = new SpinnerWithIconAdapter<>(getContext(), estanques, R.drawable.ic_estanque);
                    spinnerEstanque.setAdapter(estAdapter);

                    SpinnerWithIconAdapter<Proveedor> provAdapter = new SpinnerWithIconAdapter<>(getContext(), proveedores, R.drawable.ic_persona);
                    spinnerProveedor.setAdapter(provAdapter);

                    if (loteUI != null) {
                        for (int i = 0; i < especies.size(); i++) {
                            if (loteUI.getIdEspecie() != null && especies.get(i).getId() == loteUI.getIdEspecie()) {
                                spinnerEspecie.setSelection(i);
                                break;
                            }
                        }
                        for (int i = 0; i < estanques.size(); i++) {
                            if (loteUI.getIdEstanque() != null && estanques.get(i).getId() == loteUI.getIdEstanque()) {
                                spinnerEstanque.setSelection(i);
                                break;
                            }
                        }
                        for (int i = 0; i < proveedores.size(); i++) {
                            if (loteUI.getIdProveedor() != null && proveedores.get(i).getId() == loteUI.getIdProveedor()) {
                                spinnerProveedor.setSelection(i);
                                break;
                            }
                        }
                    }
                });
            }
        });

        builder.setView(view);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            Especie esp = (Especie) spinnerEspecie.getSelectedItem();
            Estanque est = (Estanque) spinnerEstanque.getSelectedItem();
            Proveedor prov = (Proveedor) spinnerProveedor.getSelectedItem();
            
            if (TextUtils.isEmpty(nombre)) {
                Toast.makeText(getContext(), "El nombre del lote es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (esp == null || esp.getNombre().contains("Seleccionar")) {
                Toast.makeText(getContext(), "Debe seleccionar una especie", Toast.LENGTH_SHORT).show();
                return;
            }

            if (est == null || est.getNombre().contains("Seleccionar")) {
                Toast.makeText(getContext(), "Debe seleccionar un estanque", Toast.LENGTH_SHORT).show();
                return;
            }

            Integer idEspecie = esp.getId();
            Integer idEstanque = est.getId();
            Integer idProveedor = (prov == null || prov.getNombre().contains("Ninguno")) ? null : prov.getId();
            
            int cIni = Integer.parseInt(etCantIni.getText().toString().isEmpty() ? "0" : etCantIni.getText().toString());
            int edad = Integer.parseInt(etEdad.getText().toString().isEmpty() ? "0" : etEdad.getText().toString());
            int cSac = Integer.parseInt(etCantSac.getText().toString().isEmpty() ? "0" : etCantSac.getText().toString());
            double peso = Double.parseDouble(etPeso.getText().toString().isEmpty() ? "0" : etPeso.getText().toString());

            executor.execute(() -> {
                if (loteUI == null) {
                    String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    Lote nuevo = new Lote(nombre, idEspecie, idEstanque, idProveedor, cIni, cIni, peso, fecha);
                    nuevo.setEdad(edad);
                    nuevo.setCant_sac(0);
                    nuevo.setCant_ven(0);
                    loteRep.insert(nuevo);
                } else {
                    Lote edit = loteRep.getLoteById(loteUI.getId());
                    if (edit != null) {
                        edit.setNombre(nombre);
                        edit.setIdEspecie(idEspecie);
                        edit.setIdEstanque(idEstanque);
                        edit.setIdProveedor(idProveedor);
                        edit.setCant_sac(cSac);
                        edit.setPeso_promedio(peso);
                        loteRep.update(edit);
                    }
                }
                cargarDatos();
            });
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onEdit(LoteUI lote) {
        mostrarDialogoLote(lote);
    }

    @Override
    public void onDelete(LoteUI lote) {
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Lote")
                .setMessage("¿Estás seguro de eliminar el lote " + lote.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    executor.execute(() -> {
                        Lote l = loteRep.getLoteById(lote.getId());
                        if (l != null) {
                            loteRep.delete(l);
                            cargarDatos();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onReubicar(LoteUI loteUI) {
        mostrarDialogoReubicacion(loteUI);
    }

    private void mostrarDialogoReubicacion(LoteUI loteUI) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        EstanqueRep estanqueRep = new EstanqueRep(requireActivity().getApplication());
        
        executor.execute(() -> {
            List<Estanque> estanques = estanqueRep.getAllEstanques();
            List<Estanque> estanquesDestino = new ArrayList<>();
            for (Estanque e : estanques) {
                if (e.getId() != loteUI.getIdEstanque()) {
                    estanquesDestino.add(e);
                }
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    LinearLayout layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(50, 40, 50, 10);

                    TextView tvInfo = new TextView(getContext());
                    tvInfo.setText("Peces actuales: " + loteUI.getCant_act());
                    tvInfo.setPadding(0, 0, 0, 20);
                    layout.addView(tvInfo);

                    TextView tvLabelEst = new TextView(getContext());
                    tvLabelEst.setText("Estanque de destino:");
                    layout.addView(tvLabelEst);

                    Spinner spEstanque = new Spinner(getContext());
                    ArrayAdapter<Estanque> adapterEst = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, estanquesDestino);
                    adapterEst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spEstanque.setAdapter(adapterEst);
                    layout.addView(spEstanque);

                    EditText etCant = new EditText(getContext());
                    etCant.setHint("Unidades a retirar de este lote");
                    etCant.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                    layout.addView(etCant);

                    builder.setView(layout);
                    builder.setPositiveButton("Aceptar", (dialog, which) -> {
                        String cantStr = etCant.getText().toString();
                        if (cantStr.isEmpty()) return;
                        
                        int cantReubicar = Integer.parseInt(cantStr);
                        if (cantReubicar <= 0 || cantReubicar >= loteUI.getCant_act()) {
                            Toast.makeText(getContext(), "La cantidad debe ser mayor a 0 y menor a " + loteUI.getCant_act(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Estanque destino = (Estanque) spEstanque.getSelectedItem();
                        if (destino != null) {
                            ejecutarReubicacion(loteUI, destino, cantReubicar);
                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    builder.show();
                });
            }
        });
    }

    private void ejecutarReubicacion(LoteUI loteOrigenUI, Estanque estanqueDestino, int cantidad) {
        executor.execute(() -> {
            Lote loteOrigen = loteRep.getLoteById(loteOrigenUI.getId());
            loteOrigen.setCant_act(loteOrigen.getCant_act() - cantidad);
            loteRep.update(loteOrigen);

            List<Lote> lotesEnDestino = loteRep.getLotesByEstanque(estanqueDestino.getId());
            Lote loteCompatible = null;
            for (Lote l : lotesEnDestino) {
                if (l.getIdEspecie().equals(loteOrigen.getIdEspecie()) && l.getEdad() == loteOrigen.getEdad()) {
                    loteCompatible = l;
                    break;
                }
            }

            if (loteCompatible != null) {
                loteCompatible.setCant_act(loteCompatible.getCant_act() + cantidad);
                loteRep.update(loteCompatible);
            } else {
                String nuevoNombre = generarNombreTeich();
                Lote nuevoLote = new Lote(
                    nuevoNombre,
                    loteOrigen.getIdEspecie(),
                    estanqueDestino.getId(),
                    loteOrigen.getIdProveedor(),
                    cantidad,
                    cantidad,
                    loteOrigen.getPeso_promedio(),
                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())
                );
                nuevoLote.setEdad(loteOrigen.getEdad());
                loteRep.insert(nuevoLote);
            }

            RegReubicacion reg = new RegReubicacion(
                loteOrigen.getId(),
                loteOrigen.getIdEstanque(),
                estanqueDestino.getId(),
                cantidad,
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date())
            );
            com.example.myapplication.data.local.database.AppDatabase.getDatabase(requireContext()).regReubicacionDao().insert(reg);
            
            cargarDatos();
        });
    }

    private String generarNombreTeich() {
        List<Lote> todos = loteRep.getAllLotes();
        int max = 0;
        for (Lote l : todos) {
            if (l.getNombre().startsWith("Teich")) {
                try {
                    int num = Integer.parseInt(l.getNombre().replace("Teich", ""));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return "Teich" + (max + 1);
    }

    @Override
    public void onSacrificar(LoteUI loteUI) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sacrificar, null);
        TextInputEditText etCant = view.findViewById(R.id.etCantSacrificar);
        TextView tvTitulo = view.findViewById(R.id.tvTitulo);
        tvTitulo.setText("Sacrificar - " + loteUI.getNombre());

        builder.setView(view);
        builder.setPositiveButton("Confirmar", (dialog, which) -> {
            String cantStr = etCant.getText().toString();
            if (TextUtils.isEmpty(cantStr)) return;

            int cantASacrificar = Integer.parseInt(cantStr);
            if (cantASacrificar > loteUI.getCant_act()) {
                Toast.makeText(getContext(), "No puedes sacrificar más de lo que hay en existencia (" + loteUI.getCant_act() + ")", Toast.LENGTH_SHORT).show();
                return;
            }

            executor.execute(() -> {
                Lote lote = loteRep.getLoteById(loteUI.getId());
                if (lote != null) {
                    lote.setCant_act(lote.getCant_act() - cantASacrificar);
                    lote.setCant_sac(lote.getCant_sac() + cantASacrificar);
                    loteRep.update(lote);
                    cargarDatos();
                }
            });
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onShowExtra(LoteUI lote, String option) {
        Toast.makeText(getContext(), option + " de: " + lote.getNombre(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatos();
    }

    private void cargarDatos() {
        executor.execute(() -> {
            List<LoteUI> lista = loteRep.getAllLotesUI();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapter.setLoteList(lista);
                });
            }
        });
    }
}
