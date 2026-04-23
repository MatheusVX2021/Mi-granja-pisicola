package com.example.myapplication.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.data.local.database.AppDatabase;
import com.example.myapplication.data.local.entity.Alimento;
import com.example.myapplication.data.local.entity.CatGasto;
import com.example.myapplication.data.local.entity.Especie;
import com.example.myapplication.data.local.entity.Gasto;
import com.example.myapplication.data.local.entity.Lote;
import com.example.myapplication.data.local.entity.RegAlimentacion;
import com.example.myapplication.data.model.EstanqueUI;
import com.example.myapplication.data.model.VentaUI;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResumenFragment extends Fragment {

    private TextView tvPecesTotales, tvPecesVivos, tvPecesSacrificados, tvPecesVendidos;
    private TextView tvGananciaNeta, tvBalanceMes, tvBalanceAnual;
    private BarChart chartPecesEstanque, chartPromedioEstanque, chartConsumoMes, chartStockAlimento, chartVentasEspecie, chartVentasGastos;
    private HorizontalBarChart chartConsumoEstanque;
    private LineChart chartCrecimientoLote;
    private PieChart chartEspeciesPie, chartIngresosClientes, chartGastosCategoria;
    private MaterialButtonToggleGroup togglePromedioEstanque;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resumen, container, false);
        db = AppDatabase.getDatabase(requireContext());
        initViews(view);
        cargarDatos();
        return view;
    }

    private void initViews(View v) {
        tvPecesTotales = v.findViewById(R.id.tvPecesTotales);
        tvPecesVivos = v.findViewById(R.id.tvPecesVivos);
        tvPecesSacrificados = v.findViewById(R.id.tvPecesSacrificados);
        tvPecesVendidos = v.findViewById(R.id.tvPecesVendidos);
        tvGananciaNeta = v.findViewById(R.id.tvGananciaNeta);
        tvBalanceMes = v.findViewById(R.id.tvBalanceMes);
        tvBalanceAnual = v.findViewById(R.id.tvBalanceAnual);

        chartPecesEstanque = v.findViewById(R.id.chartPecesEstanque);
        chartPromedioEstanque = v.findViewById(R.id.chartPromedioEstanque);
        chartCrecimientoLote = v.findViewById(R.id.chartCrecimientoLote);
        chartEspeciesPie = v.findViewById(R.id.chartEspeciesPie);
        chartConsumoMes = v.findViewById(R.id.chartConsumoMes);
        chartConsumoEstanque = v.findViewById(R.id.chartConsumoEstanque);
        chartStockAlimento = v.findViewById(R.id.chartStockAlimento);
        chartIngresosClientes = v.findViewById(R.id.chartIngresosClientes);
        chartVentasEspecie = v.findViewById(R.id.chartVentasEspecie);
        chartGastosCategoria = v.findViewById(R.id.chartGastosCategoria);
        chartVentasGastos = v.findViewById(R.id.chartVentasGastos);

        togglePromedioEstanque = v.findViewById(R.id.togglePromedioEstanque);
        togglePromedioEstanque.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) cargarGraficoPromedioEstanque(checkedId == R.id.btnPesoProm);
        });
        
        configureChartBasic(chartPecesEstanque);
        configureChartBasic(chartPromedioEstanque);
        configureChartBasic(chartConsumoMes);
        configureChartBasic(chartConsumoEstanque);
        configureChartBasic(chartStockAlimento);
        configureChartBasic(chartVentasEspecie);
        configureChartBasic(chartVentasGastos);
    }

    private void configureChartBasic(BarChart chart) {
        chart.getDescription().setEnabled(false);
        chart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setGranularity(1f);
        chart.getAxisRight().setEnabled(false);
    }

    private void cargarDatos() {
        executor.execute(() -> {
            List<Lote> lotes = db.loteDao().getAllLotes();
            List<VentaUI> ventasUI = db.ventaDao().getAllVentasUI();
            List<Gasto> gastos = db.gastoDao().getAllGastos();
            List<EstanqueUI> estanquesUI = db.estanqueDao().getEstanquesUI();
            List<com.example.myapplication.data.local.entity.AlimentoWithProveedor> stockAlimentos = db.alimentoDao().getAllAlimentosStatic();
            List<RegAlimentacion> regAlim = db.regAlimentacionDao().getAllAlimentaciones();
            List<Especie> especies = db.especieDao().getAllEspecies();
            List<CatGasto> categorias = db.catGastoDao().getAllCategorias();

            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                actualizarTarjetasPeces(lotes);
                actualizarFinanzas(ventasUI, gastos);
                cargarGraficoPecesEstanque(estanquesUI);
                cargarGraficoPromedioEstanque(true);
                cargarGraficoEspecies(lotes, especies);
                cargarGraficoStockAlimentoRaw(stockAlimentos);
                cargarGraficoGastosCategoria(gastos, categorias);
                cargarGraficoConsumoMes(regAlim);
                cargarGraficoConsumoEstanque(regAlim, estanquesUI);
                cargarGraficoCrecimientoLote(lotes);
                cargarGraficoIngresosClientes(ventasUI);
                cargarGraficoVentasEspecie(ventasUI, lotes, especies);
                cargarGraficoVentasGastos(ventasUI, gastos);
            });
        });
    }

    private void actualizarTarjetasPeces(List<Lote> lotes) {
        int total = 0, vivos = 0, sac = 0, ven = 0;
        for (Lote l : lotes) {
            total += l.getCant_ini();
            vivos += l.getCant_act();
            sac += l.getCant_sac();
            ven += l.getCant_ven();
        }
        tvPecesTotales.setText(String.valueOf(total));
        tvPecesVivos.setText(String.valueOf(vivos));
        tvPecesSacrificados.setText(String.valueOf(sac));
        tvPecesVendidos.setText(String.valueOf(ven));
    }

    private void actualizarFinanzas(List<VentaUI> ventas, List<Gasto> gastos) {
        double totalVentas = 0;
        for (VentaUI v : ventas) totalVentas += v.getMonto();
        
        double totalGastos = 0;
        for (Gasto g : gastos) totalGastos += g.getMonto();

        tvGananciaNeta.setText(String.format(Locale.getDefault(), "$%,.2f", totalVentas - totalGastos));
        
        Calendar cal = Calendar.getInstance();
        int mesActual = cal.get(Calendar.MONTH);
        int anioActual = cal.get(Calendar.YEAR);
        
        double vMes = 0, gMes = 0, vAnio = 0, gAnio = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (VentaUI v : ventas) {
            try {
                Calendar vCal = Calendar.getInstance();
                vCal.setTime(sdf.parse(v.getFecha()));
                if (vCal.get(Calendar.YEAR) == anioActual) {
                    vAnio += v.getMonto();
                    if (vCal.get(Calendar.MONTH) == mesActual) vMes += v.getMonto();
                }
            } catch (Exception ignored) {}
        }
        for (Gasto g : gastos) {
            try {
                Calendar gCal = Calendar.getInstance();
                gCal.setTime(sdf.parse(g.getFecha()));
                if (gCal.get(Calendar.YEAR) == anioActual) {
                    gAnio += g.getMonto();
                    if (gCal.get(Calendar.MONTH) == mesActual) gMes += g.getMonto();
                }
            } catch (Exception ignored) {}
        }

        tvBalanceMes.setText(String.format(Locale.getDefault(), "$%,.2f", vMes - gMes));
        tvBalanceAnual.setText(String.format(Locale.getDefault(), "$%,.2f", vAnio - gAnio));
    }

    private void cargarGraficoPecesEstanque(List<EstanqueUI> lista) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            entries.add(new BarEntry(i, lista.get(i).getCantidadPeces()));
            labels.add(lista.get(i).getNombre());
        }
        BarDataSet dataSet = new BarDataSet(entries, "Peces Vivos");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        chartPecesEstanque.setData(new BarData(dataSet));
        chartPecesEstanque.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chartPecesEstanque.invalidate();
    }

    private void cargarGraficoPromedioEstanque(boolean esPeso) {
        executor.execute(() -> {
            List<EstanqueUI> lista = db.estanqueDao().getEstanquesUI();
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                ArrayList<BarEntry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<>();
                for (int i = 0; i < lista.size(); i++) {
                    float val = esPeso ? (float) lista.get(i).getPesoPromedio() : lista.get(i).getEdadPromedio();
                    entries.add(new BarEntry(i, val));
                    labels.add(lista.get(i).getNombre());
                }
                BarDataSet dataSet = new BarDataSet(entries, esPeso ? "Peso Promedio (g)" : "Edad Promedio (meses)");
                dataSet.setColor(esPeso ? Color.parseColor("#2196F3") : Color.parseColor("#9C27B0"));
                chartPromedioEstanque.setData(new BarData(dataSet));
                chartPromedioEstanque.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                chartPromedioEstanque.invalidate();
            });
        });
    }

    private void cargarGraficoCrecimientoLote(List<Lote> lotes) {
        ArrayList<Entry> entries = new ArrayList<>();
        // Ordenar lotes por edad para mostrar una tendencia
        Collections.sort(lotes, (l1, l2) -> Integer.compare(l1.getEdad(), l2.getEdad()));
        
        for (int i = 0; i < lotes.size(); i++) {
            entries.add(new Entry(lotes.get(i).getEdad(), (float) lotes.get(i).getPeso_promedio()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Peso (g) vs Edad (meses)");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(9f);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.BLUE);
        dataSet.setFillAlpha(50);

        chartCrecimientoLote.setData(new LineData(dataSet));
        chartCrecimientoLote.getDescription().setEnabled(false);
        chartCrecimientoLote.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartCrecimientoLote.getAxisRight().setEnabled(false);
        chartCrecimientoLote.invalidate();
    }

    private void cargarGraficoEspecies(List<Lote> lotes, List<Especie> especies) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (Lote l : lotes) {
            counts.put(l.getIdEspecie(), counts.getOrDefault(l.getIdEspecie(), 0) + l.getCant_act());
        }

        Map<Integer, String> especieNames = new HashMap<>();
        for (Especie e : especies) especieNames.put(e.getId(), e.getNombre());

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            String name = especieNames.getOrDefault(entry.getKey(), "Desconocido");
            entries.add(new PieEntry(entry.getValue(), name));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);
        chartEspeciesPie.setData(new PieData(dataSet));
        chartEspeciesPie.getDescription().setEnabled(false);
        chartEspeciesPie.setCenterText("Especies");
        chartEspeciesPie.invalidate();
    }

    private void cargarGraficoConsumoMes(List<RegAlimentacion> lista) {
        TreeMap<String, Float> consumoMes = new TreeMap<>();
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdfOutput = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
        
        for (RegAlimentacion r : lista) {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(sdfInput.parse(r.getFecha()));
                String key = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(cal.getTime());
                consumoMes.put(key, consumoMes.getOrDefault(key, 0f) + r.getKilos());
            } catch (Exception ignored) {}
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Float> entry : consumoMes.entrySet()) {
            entries.add(new BarEntry(i++, entry.getValue()));
            try {
                labels.add(sdfOutput.format(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse(entry.getKey())));
            } catch (Exception e) { labels.add(entry.getKey()); }
        }

        BarDataSet dataSet = new BarDataSet(entries, "Consumo Mensual (kg)");
        dataSet.setColor(Color.parseColor("#FF9800"));
        chartConsumoMes.setData(new BarData(dataSet));
        chartConsumoMes.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chartConsumoMes.invalidate();
    }

    private void cargarGraficoConsumoEstanque(List<RegAlimentacion> lista, List<EstanqueUI> estanques) {
        Map<Integer, Float> consumoMap = new HashMap<>();
        for (RegAlimentacion r : lista) {
            consumoMap.put(r.getIdEstanque(), consumoMap.getOrDefault(r.getIdEstanque(), 0f) + r.getKilos());
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < estanques.size(); i++) {
            entries.add(new BarEntry(i, consumoMap.getOrDefault(estanques.get(i).getId(), 0f)));
            labels.add(estanques.get(i).getNombre());
        }

        BarDataSet dataSet = new BarDataSet(entries, "kg por Estanque");
        dataSet.setColor(Color.parseColor("#4CAF50"));
        chartConsumoEstanque.setData(new BarData(dataSet));
        chartConsumoEstanque.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chartConsumoEstanque.invalidate();
    }

    private void cargarGraficoStockAlimentoRaw(List<com.example.myapplication.data.local.entity.AlimentoWithProveedor> lista) {
        Map<String, Float> stock = new HashMap<>();
        for (com.example.myapplication.data.local.entity.AlimentoWithProveedor a : lista) {
            String tipo = a.alimento.getTipo() != null ? a.alimento.getTipo() : "Otros";
            stock.put(tipo, stock.getOrDefault(tipo, 0f) + a.alimento.getPeso());
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Float> entry : stock.entrySet()) {
            entries.add(new BarEntry(i++, entry.getValue()));
            labels.add(entry.getKey());
        }

        BarDataSet dataSet = new BarDataSet(entries, "Stock Alimento (kg)");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        chartStockAlimento.setData(new BarData(dataSet));
        chartStockAlimento.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chartStockAlimento.invalidate();
    }

    private void cargarGraficoIngresosClientes(List<VentaUI> ventas) {
        Map<String, Double> ingresos = new HashMap<>();
        for (VentaUI v : ventas) {
            ingresos.put(v.getNombreCliente(), ingresos.getOrDefault(v.getNombreCliente(), 0.0) + v.getMonto());
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : ingresos.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        chartIngresosClientes.setData(new PieData(dataSet));
        chartIngresosClientes.getDescription().setEnabled(false);
        chartIngresosClientes.invalidate();
    }

    private void cargarGraficoVentasEspecie(List<VentaUI> ventas, List<Lote> lotes, List<Especie> especies) {
        Map<Integer, Integer> loteToEspecie = new HashMap<>();
        for (Lote l : lotes) loteToEspecie.put(l.getId(), l.getIdEspecie());
        
        Map<Integer, String> especieNames = new HashMap<>();
        for (Especie e : especies) especieNames.put(e.getId(), e.getNombre());

        Map<String, Double> ventasEspecie = new HashMap<>();
        for (VentaUI v : ventas) {
            Integer idEspecie = loteToEspecie.get(v.getIdLote());
            String nombre = idEspecie != null ? especieNames.getOrDefault(idEspecie, "Desconocido") : "Desconocido";
            ventasEspecie.put(nombre, ventasEspecie.getOrDefault(nombre, 0.0) + v.getMonto());
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Double> entry : ventasEspecie.entrySet()) {
            entries.add(new BarEntry(i++, entry.getValue().floatValue()));
            labels.add(entry.getKey());
        }

        BarDataSet dataSet = new BarDataSet(entries, "Ventas por Especie ($)");
        dataSet.setColor(Color.parseColor("#2196F3"));
        chartVentasEspecie.setData(new BarData(dataSet));
        chartVentasEspecie.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chartVentasEspecie.invalidate();
    }

    private void cargarGraficoGastosCategoria(List<Gasto> gastos, List<CatGasto> categorias) {
        Map<Integer, String> catNames = new HashMap<>();
        for (CatGasto c : categorias) catNames.put(c.getId(), c.getNombre());

        Map<String, Double> catGastos = new HashMap<>();
        for (Gasto g : gastos) {
            String cat = catNames.getOrDefault(g.getIdCatGasto(), "Otros");
            catGastos.put(cat, catGastos.getOrDefault(cat, 0.0) + g.getMonto());
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : catGastos.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        chartGastosCategoria.setData(new PieData(dataSet));
        chartGastosCategoria.getDescription().setEnabled(false);
        chartGastosCategoria.invalidate();
    }

    private void cargarGraficoVentasGastos(List<VentaUI> ventas, List<Gasto> gastos) {
        TreeMap<String, Double> vMes = new TreeMap<>();
        TreeMap<String, Double> gMes = new TreeMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());

        for (VentaUI v : ventas) {
            try {
                String key = sdf.format(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(v.getFecha()));
                vMes.put(key, vMes.getOrDefault(key, 0.0) + v.getMonto());
            } catch (Exception ignored) {}
        }
        for (Gasto g : gastos) {
            try {
                String key = sdf.format(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(g.getFecha()));
                gMes.put(key, gMes.getOrDefault(key, 0.0) + g.getMonto());
            } catch (Exception ignored) {}
        }

        List<String> allKeys = new ArrayList<>(vMes.keySet());
        for (String k : gMes.keySet()) if (!allKeys.contains(k)) allKeys.add(k);
        Collections.sort(allKeys);

        ArrayList<BarEntry> entriesVentas = new ArrayList<>();
        ArrayList<BarEntry> entriesGastos = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < allKeys.size(); i++) {
            String key = allKeys.get(i);
            entriesVentas.add(new BarEntry(i, vMes.getOrDefault(key, 0.0).floatValue()));
            entriesGastos.add(new BarEntry(i, gMes.getOrDefault(key, 0.0).floatValue()));
            labels.add(key);
        }

        BarDataSet set1 = new BarDataSet(entriesVentas, "Ventas");
        set1.setColor(Color.parseColor("#4CAF50"));
        BarDataSet set2 = new BarDataSet(entriesGastos, "Gastos");
        set2.setColor(Color.parseColor("#F44336"));

        BarData data = new BarData(set1, set2);
        float groupSpace = 0.06f;
        float barSpace = 0.02f;
        float barWidth = 0.45f;
        data.setBarWidth(barWidth);

        chartVentasGastos.setData(data);
        chartVentasGastos.groupBars(0, groupSpace, barSpace);
        chartVentasGastos.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chartVentasGastos.getXAxis().setCenterAxisLabels(true);
        chartVentasGastos.getXAxis().setAxisMinimum(0);
        chartVentasGastos.getXAxis().setAxisMaximum(allKeys.size());
        chartVentasGastos.invalidate();
    }
}
