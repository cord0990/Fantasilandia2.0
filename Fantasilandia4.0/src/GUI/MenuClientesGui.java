package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Excepciones.FechaMalFormateadaException;
import Excepciones.RutMalFormateadoException;

import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox; // === VIP ===

//importar para el reporte de clientes
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Desktop;
import java.time.LocalDate;
import java.time.Period;

// Importar las clases del modelo
import fantasilandia.*;

//=======SIA 2.3 VENTANA SWING PARA MENU CLIENTES=======
public class MenuClientesGui extends JFrame {

    private final JFrame owner;
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private Fantasilandia parque;
    private JTextField txtBuscar;

    //Crea la tabla
    public MenuClientesGui(Fantasilandia parque, JFrame owner) {
        this.parque = parque;
        this.owner  = owner;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1100, 600); // un poco más ancho por columnas VIP
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        setTitle("Gestión de Clientes - Fantasilandia");
        setLocationRelativeTo(null);

        // === VIP === columnas extra: VIP y Beneficio
        String[] columnNames = {"ID Cliente", "Nombre", "RUT", "Fecha Nacimiento", "VIP", "Beneficio"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 42, 620, 464);
        contentPane.add(scrollPane);

        JButton btnAgregar = new JButton("Agregar Cliente");
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    agregarCliente();
                } catch (RutMalFormateadoException | FechaMalFormateadaException ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnAgregar.setBounds(650, 74, 220, 50);
        contentPane.add(btnAgregar);

        JButton btnEliminar = new JButton("Eliminar Cliente");
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarCliente();
            }
        });
        btnEliminar.setBounds(650, 134, 220, 50);
        contentPane.add(btnEliminar);

        JButton btnModificar = new JButton("Modificar Cliente");
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    modificarCliente();
                } catch (FechaMalFormateadaException ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnModificar.setBounds(650, 194, 220, 50);
        contentPane.add(btnModificar);

        JButton btnBuscar = new JButton("Buscar por RUT");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarCliente();
            }
        });
        btnBuscar.setBounds(650, 424, 220, 50);
        contentPane.add(btnBuscar);

        JLabel TituloVentana = new JLabel("Gestión de Clientes");
        TituloVentana.setHorizontalAlignment(SwingConstants.CENTER);
        TituloVentana.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 32));
        TituloVentana.setBounds(650, 20, 220, 60);
        contentPane.add(TituloVentana);

        // Campo de búsqueda
        txtBuscar = new JTextField();
        txtBuscar.setBounds(650, 388, 220, 25);
        contentPane.add(txtBuscar);
        txtBuscar.setColumns(10);

        JLabel lblBuscar = new JLabel("RUT a buscar:");
        lblBuscar.setBounds(650, 357, 120, 20);
        contentPane.add(lblBuscar);

        JButton btnReporte = new JButton("Reporte (Escritorio)");
        btnReporte.addActionListener(e -> generarReporteClientesEnEscritorio());
        btnReporte.setBounds(10, 510, 190, 40);
        contentPane.add(btnReporte);

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarClientes();
            }
        });
        btnActualizar.setBounds(650, 255, 220, 40);
        contentPane.add(btnActualizar);

        // Etiqueta con información
        JLabel lblInfo = new JLabel("Total de clientes: 0");
        lblInfo.setBounds(10, 11, 300, 20);
        contentPane.add(lblInfo);

        // Cargar datos iniciales
        cargarClientes();
        actualizarInfo();

        // --- VOLVER ---
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
            if (owner != null) owner.setVisible(true);
            dispose();
        });
        btnVolver.setBounds(650, 485, 220, 40);
        contentPane.add(btnVolver);

        // Al cerrar con la X, vuelve a mostrar el principal
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosed(java.awt.event.WindowEvent e) {
                if (owner != null) owner.setVisible(true);
            }
        });
    }

    private void cargarClientes() {
        tableModel.setRowCount(0);
        for (Cliente cliente : parque.getClientes()) {
            // === VIP === detectar y mostrar
            boolean esVip = (cliente instanceof ClienteVIP) && ((ClienteVIP) cliente).isVip();
            String beneficio = (cliente instanceof ClienteVIP) ? ((ClienteVIP) cliente).getBeneficio() : "-";

            Object[] row = {
                    cliente.getIdCliente(),
                    cliente.getNombre(),
                    cliente.getRut(),
                    cliente.getFechaNacimiento(),
                    esVip ? "Sí" : "No",
                    beneficio
            };
            tableModel.addRow(row);
        }
        actualizarInfo();
    }

    //Agrega un cliente (normal o VIP). ==SIA 2.9 excepciones==
    private void agregarCliente() throws RutMalFormateadoException, FechaMalFormateadaException {
        JTextField txtNombre = new JTextField();
        JTextField txtRut = new JTextField();
        JTextField txtFechaNacimiento = new JTextField();
        // === VIP ===
        JCheckBox chkVip = new JCheckBox("VIP");
        JTextField txtBeneficio = new JTextField("20% de descuento por persona");

        Object[] message = {
                "Nombre:", txtNombre,
                "RUT:", txtRut,
                "Fecha de Nacimiento (YYYY-MM-DD):", txtFechaNacimiento,
                // === VIP ===
                chkVip,
                "Beneficio (si VIP):", txtBeneficio
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Agregar Cliente", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String rut = txtRut.getText().trim();
            String fecha = txtFechaNacimiento.getText().trim();
            String id = "CLI" + String.format("%03d", parque.contarClientes() + 1);

            try {
                Cliente nuevoCliente;
                if (chkVip.isSelected()) {
                    String beneficio = txtBeneficio.getText();
                    nuevoCliente = new ClienteVIP(nombre, rut, id, fecha, beneficio);
                } else {
                    nuevoCliente = new Cliente(nombre, rut, id, fecha);
                }
                parque.addCliente(nuevoCliente);
                cargarClientes();
                JOptionPane.showMessageDialog(this,
                        "Cliente agregado exitosamente con ID: " + id,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al agregar cliente: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarCliente() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String rut = (String) tableModel.getValueAt(selectedRow, 2);
        String nombre = (String) tableModel.getValueAt(selectedRow, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al cliente " + nombre + " (" + rut + ")?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (parque.eliminarCliente(rut)) {
                cargarClientes();
                JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar cliente",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Modifica un cliente existente; si es VIP, permite editar beneficio/activar.
// Si NO es VIP, permite convertirlo a VIP manteniendo su mismo RUT e ID.
    private void modificarCliente() throws FechaMalFormateadaException {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String rut = (String) tableModel.getValueAt(selectedRow, 2);
        Cliente cliente = parque.buscarCliente(rut);

        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Campos comunes
        JTextField txtNombre = new JTextField(cliente.getNombre());
        JTextField txtFechaNacimiento = new JTextField(cliente.getFechaNacimiento());

        // Elementos VIP
        boolean esVip = (cliente instanceof ClienteVIP);
        JCheckBox chkVipActivo = new JCheckBox("VIP activo");
        JTextField txtBeneficio = new JTextField();

        // Elementos para conversión a VIP cuando el cliente NO es VIP
        JCheckBox chkConvertirAVip = new JCheckBox("Convertir a VIP");
        JTextField txtBeneficioNuevo = new JTextField("20% de descuento por persona");

        Object[] message;
        if (esVip) {
            ClienteVIP cv = (ClienteVIP) cliente;
            chkVipActivo.setSelected(cv.isVip());
            txtBeneficio.setText(cv.getBeneficio());

            message = new Object[] {
                    "Nombre:", txtNombre,
                    "Fecha de Nacimiento (YYYY-MM-DD):", txtFechaNacimiento,
                    "Nota: El RUT no se puede modificar",
                    chkVipActivo,
                    "Beneficio:", txtBeneficio
            };
        } else {
            message = new Object[] {
                    "Nombre:", txtNombre,
                    "Fecha de Nacimiento (YYYY-MM-DD):", txtFechaNacimiento,
                    "Nota: El RUT no se puede modificar",
                    chkConvertirAVip,
                    "Beneficio (si conviertes a VIP):", txtBeneficioNuevo
            };
        }

        int option = JOptionPane.showConfirmDialog(
                this, message, "Modificar Cliente", JOptionPane.OK_CANCEL_OPTION);

        if (option != JOptionPane.OK_OPTION) return;

        // 1) Actualizar datos comunes
        String nuevoNombre = txtNombre.getText().trim();
        String nuevaFecha = txtFechaNacimiento.getText().trim();

        if (!nuevoNombre.isEmpty()) {
            cliente.setNombre(nuevoNombre);
        }
        if (!nuevaFecha.isEmpty()) {
            try {
                cliente.setFechaNacimiento(nuevaFecha);
            } catch (FechaMalFormateadaException e) {
                JOptionPane.showMessageDialog(this,
                        "Fecha inválida: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 2) Actualizar/convertir VIP
        if (esVip) {
            // Actualizar flags/beneficio del VIP existente
            ClienteVIP cv = (ClienteVIP) cliente;
            cv.setVip(chkVipActivo.isSelected());
            cv.setBeneficio(txtBeneficio.getText());
        } else if (chkConvertirAVip.isSelected()) {
            // Convertir a VIP conservando RUT e ID
            try {
                ClienteVIP vip = new ClienteVIP(
                        cliente.getNombre(),
                        cliente.getRut(),
                        cliente.getIdCliente(),
                        cliente.getFechaNacimiento(),
                        txtBeneficioNuevo.getText()
                );
                // Reemplazo en la capa de datos
                parque.eliminarCliente(cliente.getRut());
                parque.addCliente(vip);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo convertir a VIP: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 3) Refrescar UI
        cargarClientes();
        JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarCliente() {
        String rut = txtBuscar.getText().trim();
        if (rut.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un RUT para buscar",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente cliente = parque.buscarCliente(rut);
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "No se encontró cliente con RUT: " + rut,
                    "No encontrado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);

        boolean esVip = (cliente instanceof ClienteVIP) && ((ClienteVIP) cliente).isVip();
        String beneficio = (cliente instanceof ClienteVIP) ? ((ClienteVIP) cliente).getBeneficio() : "-";

        Object[] row = {
                cliente.getIdCliente(),
                cliente.getNombre(),
                cliente.getRut(),
                cliente.getFechaNacimiento(),
                esVip ? "Sí" : "No",
                beneficio
        };
        tableModel.addRow(row);
        table.setRowSelectionInterval(0, 0);
    }

    private void actualizarInfo() {
        for (java.awt.Component comp : contentPane.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getText().startsWith("Total")) {
                ((JLabel) comp).setText("Total de clientes: " + parque.contarClientes());
                break;
            }
        }
    }

    // -------- Helpers para ruta del Escritorio y abrir Notepad --------
    private Path getDesktopPath() {
        String home = System.getProperty("user.home");
        Path desktop = Paths.get(home, "Desktop");
        if (Files.exists(desktop)) return desktop;
        Path alt = Paths.get(home, "Escritorio"); // por si está en español
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

    // -------- Genera el TXT en el Escritorio y lo abre --------

    private Integer edadSegura(String fechaStr) {
        if (fechaStr == null || fechaStr.isEmpty()) return null;
        try {
            LocalDate fn = LocalDate.parse(fechaStr);
            LocalDate hoy = LocalDate.now();
            if (fn.isAfter(hoy)) return null;
            return Period.between(fn, hoy).getYears();
        } catch (Exception e) {
            return null;
        }
    }

    private void generarReporteClientesEnEscritorio() {
        try {
            Path escritorio = getDesktopPath();
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path archivo = escritorio.resolve("ReporteClientes_" + timestamp + ".txt");

            String nl = System.lineSeparator();

            int total = parque.contarClientes();
            int mayores = 0;
            int menores = 0;
            int sinFechaValida = 0;

            for (Cliente c : parque.getClientes()) {
                Integer edad = edadSegura(c.getFechaNacimiento());
                if (edad == null) {
                    sinFechaValida++;
                } else if (edad >= 18) {
                    mayores++;
                } else {
                    menores++;
                }
            }
            int validos = total - sinFechaValida;
            double pctMayores = (validos > 0) ? (mayores * 100.0 / validos) : 0.0;
            double pctMenores = (validos > 0) ? (menores * 100.0 / validos) : 0.0;

            try (BufferedWriter w = Files.newBufferedWriter(
                    archivo, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                w.write("REPORTE DE CLIENTES - FANTASILANDIA" + nl);
                w.write("Generado: " + LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + nl);
                w.write("=".repeat(60) + nl + nl);

                w.write(String.format("Total de clientes: %d%s", total, nl));
                w.write(String.format("Mayores de edad: %d (%.1f%%)%s", mayores, pctMayores, nl));
                w.write(String.format("Menores de edad: %d (%.1f%%)%s", menores, pctMenores, nl));
                if (sinFechaValida > 0) {
                    w.write(String.format("Sin fecha válida: %d (no considerados en %s)%s",
                            sinFechaValida, "%", nl));
                }
                w.write(nl);

                w.write(String.format("%-30s\t%-12s\t%-16s%s", "Nombre", "RUT", "Fecha Nacimiento.", nl));
                w.write("-".repeat(60) + nl);

                for (Cliente c : parque.getClientes()) {
                    w.write(String.format("%-30s\t%-12s\t%-16s%s",
                            c.getNombre(), c.getRut(), c.getFechaNacimiento(), nl));
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
