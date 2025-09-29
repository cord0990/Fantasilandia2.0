package GUI;

import fantasilandia.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//=======SIA 2.3 VENTANA SWING PARA MENU ATRACCIONES=======
public class MenuAtraccionesGui extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JFrame owner;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private Fantasilandia parque;
    private JTextField txtBuscar;


    public MenuAtraccionesGui(Fantasilandia parque,  JFrame owner) {
        this.parque = parque;
        this.owner  = owner;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setBounds(100, 100, 950, 700);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // Título
        JLabel TituloVentana = new JLabel("Gestión de Atracciones");
        TituloVentana.setHorizontalAlignment(SwingConstants.CENTER);
        TituloVentana.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
        TituloVentana.setBounds(640, 20, 200, 60);
        contentPane.add(TituloVentana);

        // Tabla
        String[] columnNames = {"Código", "Nombre", "Clasificación", "Activa", "Descripción"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // no editable en celda
            }
        };
        table = new JTable(tableModel);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 60, 600, 520);
        contentPane.add(scrollPane);

        //======Botones principales ======
        
      //=======SIA 2.3 Acceso a Sub menu de Horarios de atraccion=======
        
        JButton btnVerHorarios = new JButton("Ver Horarios");
        btnVerHorarios.addActionListener(this::verHorariosAtraccion);
        btnVerHorarios.setBounds(640, 60, 200, 50);
        contentPane.add(btnVerHorarios);

        JButton btnAgregar = new JButton("Agregar Atracción");
        btnAgregar.addActionListener(this::agregarAtraccion);
        btnAgregar.setBounds(640, 120, 200, 50);
        contentPane.add(btnAgregar);

        JButton btnEliminar = new JButton("Eliminar Atracción");
        btnEliminar.addActionListener(this::eliminarAtraccion);
        btnEliminar.setBounds(640, 180, 200, 50);
        contentPane.add(btnEliminar);

        JButton btnModificar = new JButton("Modificar Atracción");
        btnModificar.addActionListener(this::modificarAtraccion);
        btnModificar.setBounds(640, 240, 200, 50);
        contentPane.add(btnModificar);

        // Campo de búsqueda por código (misma UI)
        JLabel lblBuscar = new JLabel("Código a buscar:");
        lblBuscar.setBounds(640, 462, 120, 20);
        contentPane.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(640, 490, 200, 25);
        contentPane.add(txtBuscar);
        txtBuscar.setColumns(10);

        JButton btnBuscar = new JButton("Buscar por Código");
        btnBuscar.addActionListener(this::buscarAtraccion);
        btnBuscar.setBounds(640, 530, 200, 50);
        contentPane.add(btnBuscar);

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> cargarAtracciones());
        btnActualizar.setBounds(640, 590, 200, 40);
        contentPane.add(btnActualizar);

        // Reporte en Escritorio (igual a tu lógica)
        JButton btnReporte = new JButton("Reporte (Escritorio)");
        btnReporte.addActionListener(e -> generarReporteAtraccionesEnEscritorio());
        btnReporte.setBounds(40, 600, 190, 40);
        contentPane.add(btnReporte);

        // Volver
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
            if (owner != null) owner.setVisible(true);
            dispose();
        });
        btnVolver.setBounds(240, 600, 120, 40);
        contentPane.add(btnVolver);


        // Etiqueta info total
        JLabel lblInfo = new JLabel("Total de atracciones: 0");
        lblInfo.setBounds(10, 30, 300, 20);
        contentPane.add(lblInfo);

        // al cerrar con la X, volver a mostrar el owner
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosed(java.awt.event.WindowEvent e) {
                if (owner != null) owner.setVisible(true);
            }
        });

        // Carga inicial
        cargarAtracciones();
        actualizarInfo();
        setLocationRelativeTo(null);
    }

    // delega al principal con owner = null
    public MenuAtraccionesGui(Fantasilandia parque) {
        this(parque, null);
    }


    // ==================== LÓGICA ====================
    
    private void cargarAtracciones() {
        tableModel.setRowCount(0);
        List<Atraccion> lista = parque.getAtracciones(); // Basado en la informacion ya almacenada
        if (lista != null) {
            for (Atraccion a : lista) {
                if (a == null) continue;
                Object[] row = {
                        a.getCodigoAtraccion(),
                        a.getNombre(),
                        a.getClasificacion(),
                        a.isActiva() ? "Sí" : "No",
                        a.getDescripcion()
                };
                tableModel.addRow(row);
            }
        }
        actualizarInfo();
    }

    //Refresca la pagina, en caso que se halla buscado algun elemento, vuelve a mostrar todos los elementos existentes
    private void actualizarInfo() {
        int total = 0;
        try {
            java.util.List<Atraccion> l = parque.getAtracciones();
            total = (l != null) ? l.size() : 0;
        } catch (Throwable ignore) { }

        // Actualiza la etiqueta "Total de atracciones: X"
        for (java.awt.Component comp : contentPane.getComponents()) {
            if (comp instanceof javax.swing.JLabel) {
                javax.swing.JLabel lbl = (javax.swing.JLabel) comp;
                String txt = lbl.getText();
                if (txt != null && txt.startsWith("Total")) {
                    lbl.setText("Total de atracciones: " + total);
                    break;
                }
            }
        }
    }

    private void agregarAtraccion(ActionEvent e) {
        JTextField txtNombre = new JTextField();
        //Sistema filtra por clasificacion el codigo de atraccion, dando un numero por defecto y un codigo numerico inicial dependiendo de la clasificacion.
        JComboBox<String> cbClasificacion = new JComboBox<>(
        	    new String[]{"Zona Kids", "Adrenalina", "Juegos de Destreza", "Familiar"}
        	);

        JTextArea txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JCheckBox chkActiva = new JCheckBox("Atracción activa", true);

        Object[] message = {
                "Nombre:", txtNombre,
                "Clasificación:", cbClasificacion,
                "Descripción:", new JScrollPane(txtDescripcion),
                chkActiva
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Agregar Atracción", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String clasificacion = String.valueOf(cbClasificacion.getSelectedItem());
            String descripcion = txtDescripcion.getText().trim();
            boolean activa = chkActiva.isSelected();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generar código automático con la misma lógica
            String codigo = generarCodigoAtraccion(clasificacion);

            // Crear y agregar atracción
            Atraccion nueva = new Atraccion(nombre, clasificacion, codigo, activa, descripcion);
            parque.addAtraccion(nueva);

            cargarAtracciones();
            JOptionPane.showMessageDialog(this,
                    "Atracción agregada exitosamente con código: " + codigo,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    //Modifica  una atraccion ya existente
    private void modificarAtraccion(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una atracción de la tabla",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Si no es seleccionada, no la detecta
        String codigo = (String) tableModel.getValueAt(row, 0);
        Atraccion atr = parque.buscarAtraccion(codigo);
        if (atr == null) {
            JOptionPane.showMessageDialog(this, "Atracción no encontrada",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField txtNombre = new JTextField(atr.getNombre());
        JComboBox<String> cbClasificacion = new JComboBox<>(
        	    new String[]{"Zona Kids", "Adrenalina", "Juegos de Destreza", "Familiar"}
        	);

        cbClasificacion.setSelectedItem(atr.getClasificacion());
        JTextArea txtDescripcion = new JTextArea(atr.getDescripcion(), 3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JCheckBox chkActiva = new JCheckBox("Atracción activa", atr.isActiva());

        Object[] message = {
                "Nombre:", txtNombre,
                "Clasificación:", cbClasificacion,
                "Descripción:", new JScrollPane(txtDescripcion),
                chkActiva
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Modificar Atracción", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String nuevoNombre = txtNombre.getText().trim();
            String nuevaClasificacion = String.valueOf(cbClasificacion.getSelectedItem());
            String nuevaDescripcion = txtDescripcion.getText().trim();
            boolean nuevaActiva = chkActiva.isSelected();

            if (!nuevoNombre.isEmpty()) {
                atr.setNombre(nuevoNombre);
            }
            atr.setClasificacion(nuevaClasificacion);
            atr.setDescripcion(nuevaDescripcion);
            atr.setActiva(nuevaActiva);

            cargarAtracciones();
            JOptionPane.showMessageDialog(this, "Atracción actualizada exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //Elimina la atraccion de la data
    private void eliminarAtraccion(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una atracción de la tabla",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String codigo = (String) tableModel.getValueAt(row, 0);
        String nombre = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar la atracción " + nombre + " (" + codigo + ")?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (parque.eliminarAtraccion(codigo)) {
                cargarAtracciones();
                JOptionPane.showMessageDialog(this, "Atracción eliminada exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar atracción",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Busca atraccion mediante el hashmap atrPorCodigo(codigo);
    private void buscarAtraccion(ActionEvent e) {
        String codigo = txtBuscar.getText().trim().toUpperCase();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un código para buscar",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Atraccion atr = parque.buscarAtraccion(codigo);
        if (atr == null) {
            JOptionPane.showMessageDialog(this, "No se encontró atracción con código: " + codigo,
                    "No encontrado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{
                atr.getCodigoAtraccion(),
                atr.getNombre(),
                atr.getClasificacion(),
                atr.isActiva() ? "Sí" : "No",
                atr.getDescripcion()
        });
        table.setRowSelectionInterval(0, 0);
        actualizarInfo();
    }

    //Accede al sub menu de los horarios de la atraccion seleccionada.
    private void verHorariosAtraccion(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una atracción de la tabla",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String codigo = (String) tableModel.getValueAt(row, 0);
        Atraccion atr = parque.buscarAtraccion(codigo);
        if (atr == null) {
            JOptionPane.showMessageDialog(this, "Atracción no encontrada",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1) Intentar abrir submenú si existe (GUI.MenuHorariosAtraccionGui)
        try {
            Class<?> cls = Class.forName("GUI.MenuHorariosAtraccionGui");
            try {
                java.lang.reflect.Constructor<?> c = cls.getConstructor(Window.class, Fantasilandia.class, Atraccion.class);
                Window w = (Window) c.newInstance(this, parque, atr);
                w.setVisible(true);
                setVisible(false);
                return;
            } catch (NoSuchMethodException ex) {
                java.lang.reflect.Constructor<?> c = cls.getConstructor(Fantasilandia.class, Atraccion.class);
                Window w = (Window) c.newInstance(parque, atr);
                w.setVisible(true);
                setVisible(false);
                return;
            }
        } catch (Throwable ignore) {
            // 2) Si no hay submenú, mostrar horarios básicos en diálogo
            StringBuilder sb = new StringBuilder();
            sb.append("Horarios de: ").append(atr.getNombre()).append("\n\n");
            try {
                // Si tu clase tiene getHorariosEstandares()
                var lista = atr.getHorariosEstandares();
                if (lista == null || lista.isEmpty()) {
                    sb.append("No tiene horarios configurados.");
                } else {
                    int i = 1;
                    for (Object h : lista) {
                        sb.append(i++).append(") ").append(String.valueOf(h)).append("\n");
                    }
                }
            } catch (Throwable t) {
                sb.append("No hay información de horarios disponible.");
            }
            JOptionPane.showMessageDialog(this, sb.toString(),
                    "Horarios", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ============ Generación de código por clasificación ============

    private String generarCodigoAtraccion(String clasificacion) {
        String prefix = prefijoDeClasificacion(clasificacion);

        int max = 0;
        int width = 3; // mínimo 3 dígitos (ej: ZK001)

        java.util.List<Atraccion> lista = null;
        try { lista = parque.getAtracciones(); } catch (Throwable ignore) {}

        if (lista != null) {
            String regex = "^" + java.util.regex.Pattern.quote(prefix) + "(\\d+)$";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex, java.util.regex.Pattern.CASE_INSENSITIVE);
            for (Atraccion a : lista) {
                if (a == null) continue;
                String cod = a.getCodigoAtraccion();
                if (cod == null) continue;
                java.util.regex.Matcher m = p.matcher(cod.trim());
                if (m.matches()) {
                    String num = m.group(1);
                    try {
                        int n = Integer.parseInt(num);
                        if (n > max) {
                            max = n;
                            width = Math.max(width, num.length()); // respeta el ancho existente
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        int next = max + 1;
        String candidato = prefix + String.format("%0" + width + "d", next);

        // Seguridad: por si ya existe, incrementa hasta encontrar uno libre
        try {
            while (parque.buscarAtraccion(candidato) != null) {
                next++;
                candidato = prefix + String.format("%0" + width + "d", next);
            }
        } catch (Throwable ignore) {}

        return candidato;
    }


    /**
     * LOGICA DE PREFIJO:
     * ZONA KIDS -> ZK, ADRENALINA -> AD, JUEGOS DE DESTREZA -> JD, FAMILIARES -> FA
     */
    private String prefijoDeClasificacion(String clasificacion) {
        if (clasificacion == null) return "FA";
        String s = clasificacion.trim().toLowerCase();

        // ZK
        if (s.startsWith("zona") || s.contains("kids") || s.contains("infan")) return "ZK";
        // AD
        if (s.contains("adren")) return "AD";
        // JD
        if (s.contains("destrez") || s.contains("habilid") || s.contains("juego")) return "JD";
        // FA (por defecto también)
        if (s.contains("famil")) return "FA";

        return "FA";
    }


    // ============ Reporte en Escritorio ============

    private void generarReporteAtraccionesEnEscritorio() {
        try {
            Path escritorio = getDesktopPath();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path archivo = escritorio.resolve("ReporteAtracciones_" + timestamp + ".txt");

            String nl = System.lineSeparator();
            try (BufferedWriter w = Files.newBufferedWriter(
                    archivo, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                w.write("REPORTE DE ATRACCIONES - FANTASILANDIA" + nl);
                w.write("Generado: " + LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + nl);
                w.write("=".repeat(60) + nl + nl);

                // --- Total de atracciones y categorías ---
                List<Atraccion> lista = parque.getAtracciones();
                int totalAtracciones = (lista != null) ? lista.size() : 0;

                // Contadores para cada categoría
                int adrenalina = 0;
                int familiares = 0;
                int zonaKids = 0;
                int destreza = 0;

                // Contar las atracciones por categoría
                for (Atraccion a : lista) {
                    if (a != null) {
                        String categoria = a.getClasificacion().toLowerCase();
                        if (categoria.contains("adrenalina")) adrenalina++;
                        if (categoria.contains("familia")) familiares++;
                        if (categoria.contains("zona kids")) zonaKids++;
                        if (categoria.contains("destreza")) destreza++;
                    }
                }

                // Calcular porcentajes
                double pctAdrenalina = (totalAtracciones > 0) ? (adrenalina * 100.0 / totalAtracciones) : 0.0;
                double pctFamiliares = (totalAtracciones > 0) ? (familiares * 100.0 / totalAtracciones) : 0.0;
                double pctZonaKids = (totalAtracciones > 0) ? (zonaKids * 100.0 / totalAtracciones) : 0.0;
                double pctDestreza = (totalAtracciones > 0) ? (destreza * 100.0 / totalAtracciones) : 0.0;

                // --- Encabezado con espacio añadido ---
                w.write(String.format("Total de Atracciones: %d%s", totalAtracciones, nl));
                w.write("Categorias:" + nl);
                w.write(String.format("- Adrenalina: %d (%.1f%%)%s", adrenalina, pctAdrenalina, nl));
                w.write(String.format("- Familiares: %d (%.1f%%)%s", familiares, pctFamiliares, nl));
                w.write(String.format("- Zona Kids: %d (%.1f%%)%s", zonaKids, pctZonaKids, nl));
                w.write(String.format("- Destreza: %d (%.1f%%)%s", destreza, pctDestreza, nl));
                w.write(nl);
                w.write("-".repeat(80) + nl); // Separador

                // --- Encabezado de la tabla ---
                w.write(String.format("%-10s | %-25s | %-15s | %-5s | %s%s",
                        "ID", "Juego", "Categoria", "Activo", "Descripcion", nl)); // Encabezados
                w.write("-".repeat(80) + nl); // Separador

                // --- Filas con datos de atracciones ---
                for (Atraccion a : lista) {
                    if (a == null) continue;
                    w.write(String.format("%-10s | %-25s | %-15s | %-5s | %s%s",
                            a.getCodigoAtraccion(),
                            a.getNombre(),
                            a.getClasificacion(),
                            a.isActiva() ? "Sí" : "No",
                            a.getDescripcion() == null ? "" : a.getDescripcion(),
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

    //Busca el path al escritorio del usuario.
    private Path getDesktopPath() {
        String home = System.getProperty("user.home");
        Path desktop = Paths.get(home, "Desktop");
        if (Files.exists(desktop)) return desktop;
        Path alt = Paths.get(home, "Escritorio"); // por si el SO lo nombra en español
        return Files.exists(alt) ? alt : Paths.get(home);
    }

    //Formato
    private void abrirEnBlocDeNotas(Path archivo) throws java.io.IOException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            new ProcessBuilder("notepad.exe", archivo.toString()).start();
        } else if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(archivo.toFile());
        }
    }
}
