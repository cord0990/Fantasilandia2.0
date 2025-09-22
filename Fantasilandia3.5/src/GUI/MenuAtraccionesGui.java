package GUI;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

//importar para el reporte de atracciones
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Desktop;
import java.lang.reflect.Method;


// Importar las clases del modelo
import fantasilandia.*;


public class MenuAtraccionesGui extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private Fantasilandia parque;
    private JTextField txtBuscar;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Fantasilandia parque = new Fantasilandia();
                    MenuAtraccionesGui frame = new MenuAtraccionesGui(parque);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MenuAtraccionesGui(Fantasilandia parque) {
        this.parque = parque;
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 950, 700);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        setTitle("Gestión de Atracciones - Fantasilandia");
        setLocationRelativeTo(null);
        
        // Configurar tabla con modelo personalizado
        String[] columnNames = {"Código", "Nombre", "Clasificación", "Activa", "Descripción"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer tabla no editable directamente
            }
        };
        
        table = new JTable(tableModel);
        table.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 60, 600, 520);
        contentPane.add(scrollPane);
        
        JButton btnAgregar = new JButton("Agregar Atracción");
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarAtraccion();
            }
        });
        btnAgregar.setBounds(640, 120, 200, 50);
        contentPane.add(btnAgregar);
        
        JButton btnEliminar = new JButton("Eliminar Atracción");
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarAtraccion();
            }
        });
        btnEliminar.setBounds(640, 180, 200, 50);
        contentPane.add(btnEliminar);
        
        JButton btnModificar = new JButton("Modificar Atracción");
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modificarAtraccion();
            }
        });
        btnModificar.setBounds(640, 240, 200, 50);
        contentPane.add(btnModificar);
        
        JButton btnBuscar = new JButton("Buscar por Código");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarAtraccion();
            }
        });
        btnBuscar.setBounds(640, 530, 200, 50);
        contentPane.add(btnBuscar);
        
        JLabel TituloVentana = new JLabel("Gestión de Atracciones");
        TituloVentana.setHorizontalAlignment(SwingConstants.CENTER);
        TituloVentana.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
        TituloVentana.setBounds(640, 20, 200, 60);
        contentPane.add(TituloVentana);
        
        // Campo de búsqueda
        txtBuscar = new JTextField();
        txtBuscar.setBounds(640, 490, 200, 25);
        contentPane.add(txtBuscar);
        txtBuscar.setColumns(10);
        
        JLabel lblBuscar = new JLabel("Código a buscar:");
        lblBuscar.setBounds(640, 462, 120, 20);
        contentPane.add(lblBuscar);
        
        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarAtracciones();
            }
        });
        btnActualizar.setBounds(640, 301, 200, 40);
        contentPane.add(btnActualizar);
        
        JButton btnMostrarTodas = new JButton("Mostrar Todas");
        btnMostrarTodas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarTodasLasAtracciones();
            }
        });
        btnMostrarTodas.setBounds(640, 352, 200, 40);
        contentPane.add(btnMostrarTodas);
        
        JButton btnVerHorarios = new JButton("Ver Horarios");
        btnVerHorarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verHorariosAtraccion();
            }
        });
        btnVerHorarios.setBounds(640, 403, 200, 40);
        contentPane.add(btnVerHorarios);

        //boton para obtener el reporte de atracciones en formato .txt
        JButton btnReporte = new JButton("Reporte (Escritorio)");
        btnReporte.addActionListener(e -> generarReporteAtraccionesEnEscritorio());
        btnReporte.setBounds(40, 600, 190, 40); // Posición sugerida
        contentPane.add(btnReporte);

        // Etiqueta con información
        JLabel lblInfo = new JLabel("Total de atracciones: 0");
        lblInfo.setBounds(10, 30, 300, 20);
        contentPane.add(lblInfo);
        
        // Cargar datos iniciales
        cargarAtracciones();
        actualizarInfo();
    }
    
    private void cargarAtracciones() {
        // Limpiar tabla
        tableModel.setRowCount(0);
        
        // Obtener lista de atracciones desde Fantasilandia
        for (Atraccion atraccion : parque.getAtracciones()) {
            Object[] row = {
                atraccion.getCodigoAtraccion(),
                atraccion.getNombre(),
                atraccion.getClasificacion(),
                atraccion.isActiva() ? "Sí" : "No",
                atraccion.getDescripcion()
            };
            tableModel.addRow(row);
        }
        actualizarInfo();
    }
    
    private void agregarAtraccion() {
        // Crear campos de entrada
        JTextField txtNombre = new JTextField();
        JComboBox<String> cbClasificacion = new JComboBox<>(new String[]{"INFANTILES", "ADRENALINA", "ACUATICOS", "FAMILIARES"});
        JTextArea txtDescripcion = new JTextArea(3, 20);
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
            String clasificacion = (String) cbClasificacion.getSelectedItem();
            String descripcion = txtDescripcion.getText().trim();
            boolean activa = chkActiva.isSelected();
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Generar código automático
            String codigo = generarCodigoAtraccion(clasificacion);
            
            // Crear y agregar atracción
            Atraccion nuevaAtraccion = new Atraccion(nombre, clasificacion, codigo, activa, descripcion);
            parque.addAtraccion(nuevaAtraccion);
            
            // Actualizar tabla
            cargarAtracciones();
            
            JOptionPane.showMessageDialog(this, 
                "Atracción agregada exitosamente con código: " + codigo, 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void eliminarAtraccion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una atracción de la tabla", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String codigo = (String) tableModel.getValueAt(selectedRow, 0);
        String nombre = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar la atracción " + nombre + " (" + codigo + ")?", 
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
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
    
    private void modificarAtraccion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una atracción de la tabla", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String codigo = (String) tableModel.getValueAt(selectedRow, 0);
        Atraccion atraccion = parque.buscarAtraccion(codigo);
        
        if (atraccion == null) {
            JOptionPane.showMessageDialog(this, "Atracción no encontrada", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Pre-llenar campos con datos actuales
        JTextField txtNombre = new JTextField(atraccion.getNombre());
        JComboBox<String> cbClasificacion = new JComboBox<>(new String[]{"INFANTILES", "ADRENALINA", "ACUATICOS", "FAMILIARES"});
        cbClasificacion.setSelectedItem(atraccion.getClasificacion());
        JTextArea txtDescripcion = new JTextArea(atraccion.getDescripcion(), 3, 20);
        JCheckBox chkActiva = new JCheckBox("Atracción activa", atraccion.isActiva());
        
        Object[] message = {
            "Nombre:", txtNombre,
            "Clasificación:", cbClasificacion,
            "Descripción:", new JScrollPane(txtDescripcion),
            chkActiva,
            "Nota: El código no se puede modificar (" + codigo + ")"
        };
        
        int option = JOptionPane.showConfirmDialog(
            this, message, "Modificar Atracción", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String nuevoNombre = txtNombre.getText().trim();
            String nuevaClasificacion = (String) cbClasificacion.getSelectedItem();
            String nuevaDescripcion = txtDescripcion.getText().trim();
            boolean nuevaActiva = chkActiva.isSelected();
            
            if (!nuevoNombre.isEmpty()) {
                atraccion.setNombre(nuevoNombre);
            }
            atraccion.setClasificacion(nuevaClasificacion);
            atraccion.setDescripcion(nuevaDescripcion);
            atraccion.setActiva(nuevaActiva);
            
            cargarAtracciones();
            JOptionPane.showMessageDialog(this, "Atracción actualizada exitosamente", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void buscarAtraccion() {
        String codigo = txtBuscar.getText().trim().toUpperCase();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un código para buscar", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Atraccion atraccion = parque.buscarAtraccion(codigo);
        if (atraccion == null) {
            JOptionPane.showMessageDialog(this, "No se encontró atracción con código: " + codigo, 
                "No encontrado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Mostrar solo la atracción encontrada
        tableModel.setRowCount(0);
        Object[] row = {
            atraccion.getCodigoAtraccion(),
            atraccion.getNombre(),
            atraccion.getClasificacion(),
            atraccion.isActiva() ? "Sí" : "No",
            atraccion.getDescripcion()
        };
        tableModel.addRow(row);
        
        // Seleccionar la fila
        table.setRowSelectionInterval(0, 0);
    }
    
    private void mostrarTodasLasAtracciones() {
        txtBuscar.setText("");
        cargarAtracciones();
    }
    
    private void verHorariosAtraccion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una atracción de la tabla", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String codigo = (String) tableModel.getValueAt(selectedRow, 0);
        Atraccion atraccion = parque.buscarAtraccion(codigo);
        
        if (atraccion == null) {
            JOptionPane.showMessageDialog(this, "Atracción no encontrada", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        StringBuilder horarios = new StringBuilder();
        horarios.append("Horarios de: ").append(atraccion.getNombre()).append("\n\n");
        
        if (atraccion.getHorariosEstandares().isEmpty()) {
            horarios.append("No tiene horarios configurados.");
        } else {
            int i = 1;
            for (HorariosAtraccion h : atraccion.getHorariosEstandares()) {
                horarios.append(i++).append(") ").append(h.toString()).append("\n");
            }
        }
        
        JOptionPane.showMessageDialog(this, horarios.toString(), 
            "Horarios", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void actualizarInfo() {
        // Actualizar etiqueta de información
        for (java.awt.Component comp : contentPane.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getText().startsWith("Total")) {
                ((JLabel) comp).setText("Total de atracciones: " + parque.contarAtracciones());
                break;
            }
        }
    }
    
    private String generarCodigoAtraccion(String clasificacion) {
        String prefijo = prefijoDeClasificacion(clasificacion);
        int contador = 1;
        String candidato;
        
        do {
            candidato = prefijo + String.format("%03d", contador);
            contador++;
        } while (parque.buscarAtraccion(candidato) != null);
        
        return candidato;
    }
    
    private String prefijoDeClasificacion(String clasificacion) {
        switch (clasificacion.toUpperCase()) {
            case "INFANTILES": return "Z";
            case "ADRENALINA": return "A";
            case "ACUATICOS": return "C";
            case "FAMILIARES": return "D";
            default: return "X";
        }
    }

    // ===== Helpers Escritorio Para generar el .txt=====
    private Path getDesktopPath() {
        String home = System.getProperty("user.home");
        Path desktop = Paths.get(home, "Desktop");
        if (Files.exists(desktop)) return desktop;
        Path alt = Paths.get(home, "Escritorio"); // por si el SO lo nombra en español
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

    // ===== Utilidades para leer campos sin romper compilación =====
    private static String callIfExists(Object obj, String... methodNames) {
        for (String name : methodNames) {
            try {
                Method m = obj.getClass().getMethod(name);
                Object val = m.invoke(obj);
                if (val != null) return String.valueOf(val);
            } catch (Exception ignore) {}
        }
        return ""; // si nada aplica
    }

    private static String safe(String s) { return s == null ? "" : s; }

    // ===== Generar TXT con las atracciones =====
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

                // Total
                try {
                    w.write(String.format("Total de atracciones: %d%s%s",
                            parque.contarAtracciones(), nl, nl));
                } catch (Exception ignore) {
                    w.write(nl); // fallback si no existe el contador
                }

                // Encabezados de columnas
                w.write(String.format("%-30s\t%-14s\t%-14s%s",
                        "Nombre", "Código/ID", "Clasificación", nl));
                w.write("-".repeat(60) + nl);

                // Iterar atracciones
                for (Atraccion a : parque.getAtracciones()) { // asumiendo este getter
                    // Nombre (intenta varios getters comunes)
                    String nombre = callIfExists(a, "getNombre", "getName");

                    // Código o ID (intenta variantes comunes)
                    String codigo = callIfExists(a,
                            "getCodigo", "getCodigoAtraccion", "getIdAtraccion", "getId");

                    // categoría
                    String clasif = callIfExists(a,
                            "getClasificacion", "getCategoria", "getTipo", "getNivel");

                    w.write(String.format("%-30s\t%-14s\t%-14s%s",
                            safe(nombre), safe(codigo), safe(clasif), nl));
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
}