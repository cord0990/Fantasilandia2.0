package GUI;

import java.awt.Font;
import java.awt.Window;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

import Excepciones.BloqueMalFormateadoException;
import Excepciones.FechaMalFormateadaException;
import Excepciones.HorarioMalFormateadoException;

import fantasilandia.Atraccion;
import fantasilandia.BloqueDeAtraccion;
import fantasilandia.Cliente;
import fantasilandia.DiasActivosAnuales;
import fantasilandia.Fantasilandia;
import fantasilandia.HorariosAtraccion;



//=======SIA 2.3 VENTANA SWING PARA SUBMENU PERTENECIENTE A MENU DIAS; MENU GESTIONA BLOQUES DE UN DIA ESPECIFICO=======
public class MenuBloquesDeDiaGui extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private Fantasilandia parque;
    private String fechaActual; // YYYY-MM-DD
    private Window parent;      // ← para Volver


    private JTextField txtBuscar;

    // === Constructor Principal ===
    public MenuBloquesDeDiaGui(Fantasilandia parque, String fecha) {
        this.parque = parque;
        this.fechaActual = fecha;
        construirUI();
        cargarBloquesDeLaFecha();
    }

    // === Nuevo con parent para “Volver” ===
    public MenuBloquesDeDiaGui(Window parent, Fantasilandia parque, String fecha) {
        this(parque, fecha);
        this.parent = parent;
        setLocationRelativeTo(parent);
    }

    private void construirUI() {
        setTitle("Bloques del Día " + fechaActual + " - Fantasilandia");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 950, 700);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(8, 8, 8, 8));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lbl = new JLabel("Bloques del Día: " + fechaActual);
        lbl.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 28));
        lbl.setBounds(10, 10, 600, 36);
        contentPane.add(lbl);

        // Tabla y modelo
        tableModel = new DefaultTableModel(new Object[] {
                "Código Bloque", "Fecha", "Atracción", "Horario", "Clientes"
        }, 0) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        //Busca dentro del documento ya existente (CSV) a partir del codigo
        JLabel lblBuscar = new JLabel("Busqueda por codigo:");
        lblBuscar.setBounds(730, 20, 200, 25);
        contentPane.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(730, 45, 200, 26);
        contentPane.add(txtBuscar);

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            private void filtrar() {
                String t = txtBuscar.getText();
                sorter.setRowFilter((t == null || t.trim().isEmpty())
                        ? null
                        : RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(t)));
            }
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10, 60, 700, 580);
        contentPane.add(scroll);

        JButton btnCargar = new JButton("Refrescar");
        btnCargar.setBounds(730, 80, 200, 36);
        btnCargar.addActionListener(e -> cargarBloquesDeLaFecha());
        contentPane.add(btnCargar);

        //==SIA 2.8. Utilizacion de Try Catch: Si no se puede agregar un bloque marca error==
        JButton btnAgregar = new JButton("Agregar Bloque");
        btnAgregar.setBounds(730, 152, 119, 25);
        btnAgregar.addActionListener(e -> {
            try { agregarBloque(); }
            catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        contentPane.add(btnAgregar);

        JButton btnEliminar = new JButton("Eliminar Bloque");
        btnEliminar.setBounds(730, 183, 119, 25);
        btnEliminar.addActionListener(e -> eliminarBloque());
        contentPane.add(btnEliminar);

        //SIA 2.8. Utilizacion de Try-catch para evitar errores durante la modificacion del bloque.
        JButton btnModificar = new JButton("Modificar Bloque");
        btnModificar.setBounds(730, 219, 119, 25);
        btnModificar.addActionListener(e -> {
            try { modificarBloque(); }
            catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        contentPane.add(btnModificar);

        JButton btnAgregarCliente = new JButton("Agregar Cliente");
        btnAgregarCliente.setBounds(730, 298, 119, 25);
        btnAgregarCliente.addActionListener(e -> agregarClienteABloque());
        contentPane.add(btnAgregarCliente);

        JButton btnEliminarCliente = new JButton("Eliminar Cliente");
        btnEliminarCliente.setBounds(730, 371, 119, 25);
        btnEliminarCliente.addActionListener(e -> eliminarClienteDeBloque());
        contentPane.add(btnEliminarCliente);


        JButton btnVerClientes = new JButton("Ver Clientes");
        btnVerClientes.setBounds(730, 334, 119, 25);
        btnVerClientes.addActionListener(e -> verClientesDelBloque());
        contentPane.add(btnVerClientes);

        //Botón Volver
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(805, 604, 119, 36);
        btnVolver.addActionListener(e -> volverAlPadre());
        contentPane.add(btnVolver);

        JLabel lblOpciones = new JLabel("Opciones:");
        lblOpciones.setBounds(730, 129, 200, 25);
        contentPane.add(lblOpciones);

        JLabel lblInsertarEnBloque = new JLabel("Informacion Bloque:");
        lblInsertarEnBloque.setBounds(730, 269, 200, 25);
        contentPane.add(lblInsertarEnBloque);
    }
    // ========================= LÓGICA =========================

    //Utilizado para volver al menu Principal
    private void volverAlPadre() {
        if (parent != null && !parent.isVisible()) { parent.setVisible(true); parent.toFront(); }
        dispose();
    }

    //Carga los bloques nuevamente en caso de que se halla modificado el estado normal de la jTable con todos los bloques (Ejemplo: Buscar un bloque)
    private void cargarBloquesDeLaFecha() {
        if (fechaActual == null || fechaActual.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se ha especificado una fecha válida",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DiasActivosAnuales dia = parque.buscarDiaActivoAnual(fechaActual);
        if (dia == null) {
            JOptionPane.showMessageDialog(this, "No se encontró día activo: " + fechaActual,
                    "No encontrado", JOptionPane.INFORMATION_MESSAGE);
            tableModel.setRowCount(0);
            return;
        }

        tableModel.setRowCount(0);
        List<BloqueDeAtraccion> bloques = dia.getBloques();
        if (bloques == null || bloques.isEmpty()) return;

        for (BloqueDeAtraccion b : bloques) {
            tableModel.addRow(new Object[] {
                    b.getCodigoBloque(),
                    b.getFecha(),
                    (b.getAtraccion() != null
                            ? b.getAtraccion().getNombre() + " (" + b.getAtraccion().getCodigoAtraccion() + ")"
                            : "(sin atracción)"),
                    (b.getHorario() != null ? b.getHorario().toString() : "(sin horario)"),
                    (b.getClientesParticipantes() != null
                            ? b.getClientesParticipantes().size() + " clientes"
                            : "0 clientes")
            });
        }
        if (tableModel.getRowCount() > 0) table.setRowSelectionInterval(0, 0);
    }

    //Agrega un bloque a la coleccion. ==SIA 2.9. Salto de Excepciones===
    private void agregarBloque() throws FechaMalFormateadaException,
            HorarioMalFormateadoException,
            BloqueMalFormateadoException {
        String codBloque = JOptionPane.showInputDialog(this, "Código del Bloque:", "B001");
        if (codBloque == null || codBloque.trim().isEmpty()) return;

        String codigoAtr = JOptionPane.showInputDialog(this, "Código de la Atracción (ej: AD001):");
        if (codigoAtr == null || codigoAtr.trim().isEmpty()) return;

        Atraccion atr = parque.buscarAtraccion(codigoAtr.trim().toUpperCase());
        if (atr == null) {
            JOptionPane.showMessageDialog(this, "No existe atracción con código: " + codigoAtr,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String hi = JOptionPane.showInputDialog(this, "Hora inicio (HH:mm):", "10:00");
        if (hi == null || hi.trim().isEmpty()) return;

        String hf = JOptionPane.showInputDialog(this, "Hora fin (HH:mm):", "11:00");
        if (hf == null || hf.trim().isEmpty()) return;

        parque.getOCrearBloque(fechaActual, codBloque.trim(), atr, new HorariosAtraccion(hi.trim(), hf.trim()));
        JOptionPane.showMessageDialog(this, "Bloque creado/obtenido exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        cargarBloquesDeLaFecha();
    }

    private void eliminarBloque() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un bloque.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int row = table.convertRowIndexToModel(viewRow);

        String codigoBloque = (String) tableModel.getValueAt(row, 0);
        String fecha = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el bloque " + codigoBloque + " del " + fecha + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        DiasActivosAnuales dia = parque.buscarDiaActivoAnual(fecha);
        if (dia != null) {
            BloqueDeAtraccion bloque = dia.buscarBloque(codigoBloque);
            if (bloque != null) {
                dia.getBloques().remove(bloque);
                JOptionPane.showMessageDialog(this, "Bloque eliminado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarBloquesDeLaFecha();
            }
        }
    }

    //Proceso de modificacion de bloque, Sustento a Excepciones de la SIA 2.8
    private void modificarBloque() throws BloqueMalFormateadoException, HorarioMalFormateadoException {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un bloque.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int row = table.convertRowIndexToModel(viewRow);

        String codigoBloque = (String) tableModel.getValueAt(row, 0);
        String fecha = (String) tableModel.getValueAt(row, 1);

        DiasActivosAnuales dia = parque.buscarDiaActivoAnual(fecha);
        if (dia == null) return;

        BloqueDeAtraccion b = dia.buscarBloque(codigoBloque);
        if (b == null) return;

        String codigoAtr = JOptionPane.showInputDialog(this,
                "Código atracción (actual: " +
                        (b.getAtraccion() != null ? b.getAtraccion().getCodigoAtraccion() : "-") + "):",
                b.getAtraccion() != null ? b.getAtraccion().getCodigoAtraccion() : "");
        if (codigoAtr != null && !codigoAtr.trim().isEmpty()) {
            Atraccion a = parque.buscarAtraccion(codigoAtr.trim().toUpperCase());
            if (a != null) {
                b.setAtraccion(a);
            } else {
                JOptionPane.showMessageDialog(this, "Atracción no hallada: " + codigoAtr,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String hi = JOptionPane.showInputDialog(this, "Hora inicio (HH:mm):",
                b.getHorario() != null ? b.getHorario().getHoraInicio() : "10:00");
        if (hi == null) return;

        String hf = JOptionPane.showInputDialog(this, "Hora fin (HH:mm):",
                b.getHorario() != null ? b.getHorario().getHoraFin() : "11:00");
        if (hf == null) return;

        b.setHorario(new HorariosAtraccion(hi.trim(), hf.trim()));

        JOptionPane.showMessageDialog(this, "Bloque modificado correctamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        cargarBloquesDeLaFecha();
    }

    //Agrega Clientes al bloque seleccionado, si no esta seleccionado marca error.
    private void agregarClienteABloque() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un bloque.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int row = table.convertRowIndexToModel(viewRow);

        String codigoBloque = (String) tableModel.getValueAt(row, 0);
        String fecha = (String) tableModel.getValueAt(row, 1);

        String rut = JOptionPane.showInputDialog(this, "RUT del cliente:", "Agregar Cliente",
                JOptionPane.QUESTION_MESSAGE);
        if (rut == null || rut.trim().isEmpty()) return;

        try {
            if (parque.agregarClienteABloquePorRut(fecha, codigoBloque, rut.trim())) {
                JOptionPane.showMessageDialog(this, "Cliente inscrito en el bloque", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarBloquesDeLaFecha();
            } else {
                JOptionPane.showMessageDialog(this, "Cliente o bloque no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void verClientesDelBloque() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un bloque.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int row = table.convertRowIndexToModel(viewRow);

        String codigoBloque = (String) tableModel.getValueAt(row, 0);
        String fecha = (String) tableModel.getValueAt(row, 1);

        DiasActivosAnuales dia = parque.buscarDiaActivoAnual(fecha);
        if (dia == null) return;

        BloqueDeAtraccion bloque = dia.buscarBloque(codigoBloque);
        if (bloque == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Clientes del bloque ").append(codigoBloque).append(" (").append(fecha).append("):\n\n");

        List<Cliente> clientesBloque = bloque.getClientesParticipantes();
        if (clientesBloque == null || clientesBloque.isEmpty()) {
            sb.append("No hay clientes inscritos en este bloque.");
        } else {
            for (int i = 0; i < clientesBloque.size(); i++) {
                Cliente c = clientesBloque.get(i);
                sb.append(i + 1).append(") ").append(c.getNombre()).append(" - ").append(c.getRut()).append("\n");
            }
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Clientes del Bloque", JOptionPane.INFORMATION_MESSAGE);
    }
    // === Eliminar cliente del bloque actual por RUT ===
    private void eliminarClienteDeBloque() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un bloque.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int row = table.convertRowIndexToModel(viewRow);

        String codigoBloque = (String) tableModel.getValueAt(row, 0);
        String fecha = (String) tableModel.getValueAt(row, 1);

        String rut = JOptionPane.showInputDialog(this, "RUT del cliente a eliminar:", "Eliminar Cliente",
                JOptionPane.QUESTION_MESSAGE);
        if (rut == null) return;               // cancelado
        rut = rut.trim();
        if (rut.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un RUT.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            DiasActivosAnuales dia = parque.buscarDiaActivoAnual(fecha);
            if (dia == null) {
                JOptionPane.showMessageDialog(this, "Día no encontrado: " + fecha, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            BloqueDeAtraccion bloque = dia.buscarBloque(codigoBloque);
            if (bloque == null) {
                JOptionPane.showMessageDialog(this, "Bloque no encontrado: " + codigoBloque, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean eliminado = false;

            // 1) Intento directo: método que ya existe en tu modelo
            try {
                eliminado = bloque.removeClientePorRut(rut);
            } catch (Exception ex) {
                // ignoramos y probamos normalizando
            }

            // 2) Fallback con RUT normalizado (sin puntos/guion, K mayúscula)
            if (!eliminado) {
                String target = normalizarRut(rut);
                List<Cliente> lista = bloque.getClientesParticipantes();
                if (lista != null) {
                    for (int i = 0; i < lista.size(); i++) {
                        Cliente c = lista.get(i);
                        String rc = (c.getRut() == null) ? "" : normalizarRut(c.getRut());
                        if (rc.equalsIgnoreCase(target)) {
                            lista.remove(i);
                            eliminado = true;
                            break;
                        }
                    }
                }
            }

            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado del bloque.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarBloquesDeLaFecha(); // refresca la columna "X clientes"
            } else {
                JOptionPane.showMessageDialog(this, "El cliente no existe o no está inscrito en este bloque.", "No encontrado", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper de normalización de RUT
    private String normalizarRut(String rut) {
        if (rut == null) return "";
        return rut.replace(".", "").replace("-", "").replace(" ", "").trim().toUpperCase();
    }

}
