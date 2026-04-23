package com.example.myapplication.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.utils.FileUtils;
import com.example.myapplication.R;
import com.example.myapplication.ui.adapter.EstanqueAdapter;
import com.example.myapplication.data.local.entity.Estanque;
import com.example.myapplication.data.model.EstanqueUI;
import com.example.myapplication.data.repository.EstanqueRep;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.myapplication.data.local.entity.Alimento;
import com.example.myapplication.data.local.entity.AlimentoWithProveedor;
import com.example.myapplication.data.local.entity.Especie;
import com.example.myapplication.data.local.entity.Lote;
import com.example.myapplication.data.local.entity.Proveedor;
import com.example.myapplication.data.local.entity.RegAlimentacion;
import com.example.myapplication.data.repository.AlimentoRep;
import com.example.myapplication.data.repository.EspecieRep;
import com.example.myapplication.data.repository.LoteRep;
import com.example.myapplication.data.repository.ProveedorRep;
import com.example.myapplication.data.repository.RegAlimentacionRep;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EstanqueFragment extends Fragment implements EstanqueAdapter.OnEstanqueActionListener {

    private RecyclerView rvEstanques;
    private EstanqueAdapter adapter;
    private List<EstanqueUI> estanqueList = new ArrayList<>();
    private FloatingActionButton fabAddEstanque;
    private EstanqueRep estanqueRep;
    private LoteRep loteRep;
    private EspecieRep especieRep;
    private ProveedorRep proveedorRep;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private EstanqueUI estanqueSeleccionadoParaImagen;

    private final ActivityResultLauncher<String[]> openDocument = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            uri -> {
                if (uri != null && estanqueSeleccionadoParaImagen != null) {
                    executor.execute(() -> {
                        String localUri = FileUtils.saveImageToInternalStorage(requireContext(), uri);
                        if (localUri != null) {
                            actualizarImagenEstanque(estanqueSeleccionadoParaImagen.getId(), localUri);
                        }
                    });
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estanque, container, false);
        
        rvEstanques = view.findViewById(R.id.rvEstanques);
        rvEstanques.setLayoutManager(new LinearLayoutManager(getContext()));
        
        fabAddEstanque = view.findViewById(R.id.fabAddEstanque);

        estanqueRep = new EstanqueRep(getActivity().getApplication());
        loteRep = new LoteRep(getActivity().getApplication());
        especieRep = new EspecieRep(getActivity().getApplication());
        proveedorRep = new ProveedorRep(getActivity().getApplication());
        
        adapter = new EstanqueAdapter(estanqueList, this);
        rvEstanques.setAdapter(adapter);

        fabAddEstanque.setOnClickListener(v -> mostrarDialogoNuevoEstanque());
        
        return view;
    }

    private void mostrarDialogoNuevoEstanque() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_estanque, null);
        EditText etNombre = view.findViewById(R.id.etNombreEstanque);
        EditText etArea = view.findViewById(R.id.etAreaEstanque);

        builder.setView(view);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            String areaStr = etArea.getText().toString().trim();

            if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(areaStr)) {
                try {
                    double area = Double.parseDouble(areaStr);
                    Estanque nuevo = new Estanque(nombre, area, null);
                    estanqueRep.insert(nuevo, this::cargarDatosReales);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Área inválida", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Ambos campos son obligatorios", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onEdit(EstanqueUI estanqueUI, String nuevoNombre, double nuevaArea) {
        executor.execute(() -> {
            Estanque estanque = estanqueRep.getEstanqueById(estanqueUI.getId());
            if (estanque != null) {
                estanque.setNombre(nuevoNombre);
                estanque.setArea(nuevaArea);
                estanqueRep.update(estanque, this::cargarDatosReales);
            }
        });
    }

    @Override
    public void onDelete(EstanqueUI estanqueUI) {
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Estanque")
                .setMessage("¿Estás seguro de eliminar " + estanqueUI.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    executor.execute(() -> {
                        Estanque estanque = estanqueRep.getEstanqueById(estanqueUI.getId());
                        if (estanque != null) {
                            estanqueRep.delete(estanque, this::cargarDatosReales);
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onAddImage(EstanqueUI estanque) {
        estanqueSeleccionadoParaImagen = estanque;
        openDocument.launch(new String[]{"image/*"});
    }

    @Override
    public void onRemoveImage(EstanqueUI estanque) {
        actualizarImagenEstanque(estanque.getId(), null);
    }

    private void actualizarImagenEstanque(int id, String uri) {
        executor.execute(() -> {
            Estanque estanque = estanqueRep.getEstanqueById(id);
            if (estanque != null) {
                estanque.setImagen(uri);
                estanqueRep.update(estanque, this::cargarDatosReales);
            }
        });
    }

    @Override
    public void onAddLote(EstanqueUI estanque) {
        mostrarDialogoNuevoLote(estanque);
    }

    private void mostrarDialogoNuevoLote(EstanqueUI estanqueUI) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_lote, null);
        TextInputEditText etNombre = view.findViewById(R.id.etNombreLote);
        Spinner spinnerEspecie = view.findViewById(R.id.spinnerEspecie);
        Spinner spinnerEstanque = view.findViewById(R.id.spinnerEstanque);
        Spinner spinnerProveedor = view.findViewById(R.id.spinnerProveedor);
        TextInputEditText etCantIni = view.findViewById(R.id.etCantIni);
        TextInputEditText etEdad = view.findViewById(R.id.etEdad);
        TextInputEditText etPeso = view.findViewById(R.id.etPeso);

        view.findViewById(R.id.layoutCantIni).setVisibility(View.VISIBLE);
        view.findViewById(R.id.layoutEdad).setVisibility(View.VISIBLE);
        view.findViewById(R.id.layoutCantSac).setVisibility(View.GONE);

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
                    ArrayAdapter<Especie> espAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, especies);
                    espAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEspecie.setAdapter(espAdapter);

                    ArrayAdapter<Estanque> estAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, estanques);
                    estAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEstanque.setAdapter(estAdapter);

                    ArrayAdapter<Proveedor> provAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, proveedores);
                    provAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerProveedor.setAdapter(provAdapter);

                    // Pre-seleccionar y bloquear el estanque
                    for (int i = 0; i < estanques.size(); i++) {
                        if (estanques.get(i).getId() == estanqueUI.getId()) {
                            spinnerEstanque.setSelection(i);
                            spinnerEstanque.setEnabled(false);
                            break;
                        }
                    }
                });
            }
        });

        builder.setView(view);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            Especie esp = (Especie) spinnerEspecie.getSelectedItem();
            Proveedor prov = (Proveedor) spinnerProveedor.getSelectedItem();

            if (TextUtils.isEmpty(nombre)) {
                Toast.makeText(getContext(), "El nombre del lote es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (esp == null || esp.getNombre().contains("Seleccionar")) {
                Toast.makeText(getContext(), "Debe seleccionar una especie", Toast.LENGTH_SHORT).show();
                return;
            }

            Integer idEspecie = esp.getId();
            Integer idEstanque = estanqueUI.getId();
            Integer idProveedor = (prov == null || prov.getNombre().contains("Ninguno")) ? null : prov.getId();

            int cIni = Integer.parseInt(etCantIni.getText().toString().isEmpty() ? "0" : etCantIni.getText().toString());
            int edad = Integer.parseInt(etEdad.getText().toString().isEmpty() ? "0" : etEdad.getText().toString());
            double peso = Double.parseDouble(etPeso.getText().toString().isEmpty() ? "0" : etPeso.getText().toString());

            executor.execute(() -> {
                String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                Lote nuevo = new Lote(nombre, idEspecie, idEstanque, idProveedor, cIni, cIni, peso, fecha);
                nuevo.setEdad(edad);
                nuevo.setCant_sac(0);
                nuevo.setCant_ven(0);
                loteRep.insert(nuevo);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Lote añadido con éxito", Toast.LENGTH_SHORT).show();
                        cargarDatosReales();
                    });
                }
            });
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onAlimentar(EstanqueUI estanque) {
        mostrarDialogoAlimentar(estanque);
    }

    private void mostrarDialogoAlimentar(EstanqueUI estanqueUI) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etKilos = new EditText(getContext());
        etKilos.setHint("Kilos usados");
        etKilos.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layout.addView(etKilos);

        final Spinner spAlimento = new Spinner(getContext());
        layout.addView(spAlimento);

        AlimentoRep alimentoRep = new AlimentoRep(requireActivity().getApplication());
        executor.execute(() -> {
            List<AlimentoWithProveedor> alimentosWP = alimentoRep.getAlimentoDao().getAllAlimentosStatic(); // We need a static list
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    List<String> nombres = new ArrayList<>();
                    for (AlimentoWithProveedor a : alimentosWP) {
                        nombres.add(a.alimento.getNombre() + " (Stock: " + a.alimento.getPeso() + "kg)");
                    }
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombres);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spAlimento.setAdapter(spinnerAdapter);
                });
            }
        });

        builder.setView(layout);
        builder.setPositiveButton("Alimentar", (dialog, which) -> {
            String kilosStr = etKilos.getText().toString();
            int selectedPos = spAlimento.getSelectedItemPosition();
            if (kilosStr.isEmpty() || selectedPos == -1) return;

            int kilos = Integer.parseInt(kilosStr);
            executor.execute(() -> {
                List<AlimentoWithProveedor> alimentosWP = alimentoRep.getAlimentoDao().getAllAlimentosStatic();
                Alimento alimento = alimentosWP.get(selectedPos).alimento;

                if (kilos > alimento.getPeso()) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "No hay suficiente stock (" + alimento.getPeso() + "kg)", Toast.LENGTH_SHORT).show());
                    }
                    return;
                }

                // Guardar Registro
                RegAlimentacion reg = new RegAlimentacion(
                        estanqueUI.getId(),
                        alimento.getId(),
                        kilos,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date())
                );
                RegAlimentacionRep regRep = new RegAlimentacionRep(requireActivity().getApplication());
                regRep.insert(reg);

                // Actualizar Stock Alimento
                alimento.setPeso(alimento.getPeso() - kilos);
                alimentoRep.update(alimento);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Alimentación registrada con éxito", Toast.LENGTH_SHORT).show();
                        cargarDatosReales();
                    });
                }
            });
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatosReales();
    }

    private void cargarDatosReales() {
        executor.execute(() -> {
            List<EstanqueUI> lista = estanqueRep.getEstanquesUI();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    estanqueList.clear();
                    estanqueList.addAll(lista);
                    adapter.notifyDataSetChanged();
                });
            }
        });
    }
}