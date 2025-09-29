package GUI;

import fantasilandia.Fantasilandia;
import fantasilandia.Atraccion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.Desktop;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * MenuReportes (sin Flujo + botón "Generar reporte")
 * --------------------------------------------------
 * - Filtro por ESTADO: Todos / Activas / No Activas.
 * - Botón "Generar reporte": crea un TXT en el Escritorio
 *   con el listado y resumen del filtro actual.
 */
public class MenuReportes extends JFrame {

    private static final long serialVersionUID = 1L;

    // UI principal
    private JPanel contentPane;
    private JTable table;
    private JLabel lblTituloSeccion;
    private JLabel lblResumen;

    // Filtros
    private JComboBox<String> cboEstado;

    // Referencia
    private final Fantasilandia parque;

    // Constructor
    public MenuReportes(Fantasilandia parque) {
        this.parque = parque;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 900, 560);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(null);
        setContentPane(contentPane);
        setLocationRelativeTo(null);
        setTitle("Reportes y Filtros - Atracciones");

        // === Tabla (izquierda) ===
        JLabel lblEstadisticas = new JLabel("Estadísticas / Resultados");
        lblEstadisticas.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        lblEstadisticas.setBounds(10, 5, 300, 26);
        contentPane.add(lblEstadisticas);

        table = new JTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10, 36, 600, 430);
        contentPane.add(scroll);

        lblResumen = new JLabel(" ");
        lblResumen.setBounds(10, 470, 600, 30);
        contentPane.add(lblResumen);

        // === Panel derecho ===
        lblTituloSeccion = new JLabel("Filtros de Atracciones");
        lblTituloSeccion.setFont(new Font("Comic Sans MS", Font.PLAIN, 17));
        lblTituloSeccion.setBounds(640, 5, 220, 26);
        contentPane.add(lblTituloSeccion);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(640, 45, 200, 20);
        contentPane.add(lblEstado);

        // Orden solicitado: Todos → Activas → No Activas
        cboEstado = new JComboBox<>(new String[]{"Todos", "Activas", "No Activas"});
        cboEstado.setBounds(640, 70, 220, 28);
        contentPane.add(cboEstado);

        JButton btnAplicarEstado = new JButton("Aplicar filtro");
        btnAplicarEstado.setBounds(640, 105, 220, 36);
        btnAplicarEstado.addActionListener(e -> aplicarFiltroEstado());
        contentPane.add(btnAplicarEstado);

        // === Botón "Generar reporte" (zona verde) ===
        JButton btnGenerar = new JButton("Generar reporte");
        btnGenerar.setBounds(640, 160, 220, 36);
        btnGenerar.addActionListener(e -> generarReporteTxt());
        contentPane.add(btnGenerar);

        // Volver
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(720, 470, 140, 36);
        btnVolver.addActionListener(e -> {
            try {
                MenuPrincipal mp = new MenuPrincipal(parque);
                mp.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo volver al menú principal:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
        });
        contentPane.add(btnVolver);

        // Estado inicial
        if (parque != null) {
            cboEstado.setSelectedIndex(0); // "Todos"
            aplicarFiltroEstado();
        } else {
            setTabla(new ArrayList<Atraccion>());
            lblResumen.setText("Sin datos (parque == null). Abre este menú desde el principal.");
        }
    }

    // ========================== ACCIONES ==========================

    private void aplicarFiltroEstado() {
        if (parque == null) return;

        String sel = String.valueOf(cboEstado.getSelectedItem());
        Boolean requiereActiva = null; // null = Todos

        if ("Activas".equalsIgnoreCase(sel)) {
            requiereActiva = Boolean.TRUE;
        } else if ("No activas".equalsIgnoreCase(sel) || "No Activas".equalsIgnoreCase(sel)) {
            requiereActiva = Boolean.FALSE;
        }

        List<Atraccion> base = safeAtracciones();
        List<Atraccion> filtradas = filtrarPorEstado(base, requiereActiva);

        // Orden alfabético por nombre
        java.util.Collections.sort(filtradas, new Comparator<Atraccion>() {
            @Override
            public int compare(Atraccion o1, Atraccion o2) {
                String n1 = (o1 != null && o1.getNombre() != null) ? o1.getNombre() : "";
                String n2 = (o2 != null && o2.getNombre() != null) ? o2.getNombre() : "";
                return n1.compareToIgnoreCase(n2);
            }
        });

        setTabla(filtradas);

        // Resumen por estado
        long total = base.size();
        long activas = 0;
        for (Atraccion a : base) if (a != null && a.isActiva()) activas++;
        long noActivas = total - activas;

        String pctAct = (total > 0) ? String.format("%.1f", activas * 100.0 / total) : "0.0";
        String pctNo  = (total > 0) ? String.format("%.1f", noActivas * 100.0 / total) : "0.0";

        lblResumen.setText(String.format(
                "Mostrando %d de %d | Activas: %d (%s%%) | No activas: %d (%s%%)",
                filtradas.size(), total, activas, pctAct, noActivas, pctNo
        ));
    }

    // ========================== REPORTES ==========================

    private void generarReporteTxt() {
        try {
            // 1) Determinar filtro actual
            String sel = String.valueOf(cboEstado.getSelectedItem());
            Boolean requiereActiva = null;
            if ("Activas".equalsIgnoreCase(sel)) {
                requiereActiva = Boolean.TRUE;
            } else if ("No activas".equalsIgnoreCase(sel) || "No Activas".equalsIgnoreCase(sel)) {
                requiereActiva = Boolean.FALSE;
            }

            // 2) Listas (base y filtrada)
            List<Atraccion> base = safeAtracciones();
            List<Atraccion> lista = filtrarPorEstado(base, requiereActiva);

            // 3) Armar archivo en Escritorio
            Path escritorio = getDesktopPath();
            String filtroNombre = (requiereActiva == null) ? "Todos"
                    : (requiereActiva ? "Activas" : "No_Activas");
            String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path archivo = escritorio.resolve("Reporte_Atracciones_" + filtroNombre + "_" + ts + ".txt");

            String nl = System.lineSeparator();
            try (BufferedWriter w = Files.newBufferedWriter(
                    archivo, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                // Encabezado
                w.write("REPORTE DE ATRACCIONES - FANTASILANDIA" + nl);
                w.write("Generado: " + LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + nl);
                w.write("Filtro aplicado: " + (requiereActiva == null ? "Todos"
                        : (requiereActiva ? "Activas" : "No Activas")) + nl);
                w.write("=".repeat(70) + nl + nl);

                // Totales por estado (de la base completa)
                int total = base.size();
                int act = 0;
                for (Atraccion a : base) if (a != null && a.isActiva()) act++;
                int noact = total - act;
                double pAct = total > 0 ? act * 100.0 / total : 0.0;
                double pNo  = total > 0 ? noact * 100.0 / total : 0.0;

                w.write(String.format("Total atracciones: %d%s", total, nl));
                w.write(String.format("- Activas: %d (%.1f%%)%s", act, pAct, nl));
                w.write(String.format("- No Activas: %d (%.1f%%)%s", noact, pNo, nl));
                w.write(nl);

                // Totales por categoría (de la lista filtrada que se mostrará)
                int adrenalina = 0, familiares = 0, zonaKids = 0, destreza = 0;
                for (Atraccion a : lista) {
                    if (a == null || a.getClasificacion() == null) continue;
                    String c = a.getClasificacion().toLowerCase();
                    if (c.contains("adrenalina")) adrenalina++;
                    if (c.contains("famil"))     familiares++;
                    if (c.contains("zona kids") || c.contains("kids") || c.contains("zona")) zonaKids++;
                    if (c.contains("destreza"))  destreza++;
                }
                int totFiltrado = lista.size();
                w.write("Categorías (en listado mostrado):" + nl);
                w.write(String.format("- Adrenalina: %d (%.1f%%)%s", adrenalina, pct(adrenalina, totFiltrado), nl));
                w.write(String.format("- Familiares: %d (%.1f%%)%s", familiares, pct(familiares, totFiltrado), nl));
                w.write(String.format("- Zona Kids: %d (%.1f%%)%s", zonaKids, pct(zonaKids, totFiltrado), nl));
                w.write(String.format("- Destreza: %d (%.1f%%)%s", destreza, pct(destreza, totFiltrado), nl));
                w.write(nl);

                // Encabezado de tabla
                w.write("-".repeat(90) + nl);
                w.write(String.format("%-10s | %-28s | %-18s | %-6s%s",
                        "ID", "Juego", "Categoría", "Activo", nl));
                w.write("-".repeat(90) + nl);

                // Filas
                for (Atraccion a : lista) {
                    if (a == null) continue;
                    w.write(String.format("%-10s | %-28s | %-18s | %-6s%s",
                            safe(a.getCodigoAtraccion()),
                            safe(a.getNombre()),
                            safe(a.getClasificacion()),
                            (a.isActiva() ? "Sí" : "No"),
                            nl));
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Reporte generado en el Escritorio:\n" + archivo.toAbsolutePath(),
                    "Reporte generado", JOptionPane.INFORMATION_MESSAGE);

            abrirEnBlocDeNotas(archivo);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo generar/abrir el reporte: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ========================== HELPERS ==========================

    private static double pct(int parte, int total) {
        return total > 0 ? (parte * 100.0 / total) : 0.0;
    }

    private List<Atraccion> safeAtracciones() {
        List<Atraccion> lst;
        try {
            lst = parque.getAtracciones();
        } catch (Throwable t) {
            lst = null;
        }
        return (lst != null) ? lst : new ArrayList<Atraccion>();
    }

    private List<Atraccion> filtrarPorEstado(List<Atraccion> base, Boolean requiereActiva) {
        List<Atraccion> out = new ArrayList<>();
        for (Atraccion a : base) {
            if (a == null) continue;
            if (requiereActiva == null || a.isActiva() == requiereActiva) {
                out.add(a);
            }
        }
        return out;
    }

    // Tabla: ID | Juego | Categoría | Activo
    private void setTabla(List<Atraccion> lista) {
        String[] cols = {"ID", "Juego", "Categoría", "Activo"};
        Object[][] rows = new Object[lista.size()][cols.length];

        int i = 0;
        for (Atraccion a : lista) {
            rows[i][0] = safe(a != null ? a.getCodigoAtraccion() : null);
            rows[i][1] = safe(a != null ? a.getNombre() : null);
            rows[i][2] = safe(a != null ? a.getClasificacion() : null);
            rows[i][3] = (a != null && a.isActiva()) ? "Sí" : "No";
            i++;
        }

        table.setModel(new DefaultTableModel(rows, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        if (table.getColumnModel().getColumnCount() >= 4) {
            table.getColumnModel().getColumn(0).setPreferredWidth(70);
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.getColumnModel().getColumn(2).setPreferredWidth(120);
            table.getColumnModel().getColumn(3).setPreferredWidth(60);
        }
    }

    private String safe(String s) { return (s == null) ? "" : s; }

    private Path getDesktopPath() {
        String home = System.getProperty("user.home");
        Path desktop = Paths.get(home, "Desktop");
        if (Files.exists(desktop)) return desktop;
        Path alt = Paths.get(home, "Escritorio");
        return Files.exists(alt) ? alt : Paths.get(home);
    }

    private void abrirEnBlocDeNotas(Path archivo) throws java.io.IOException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            new ProcessBuilder("notepad.exe", archivo.toString()).start();
        } else if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(archivo.toFile());
        }
    }
}
