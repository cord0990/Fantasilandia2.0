package GUI;

import java.awt.Font;
import java.util.List;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;


import java.awt.Window;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import fantasilandia.Fantasilandia;
import fantasilandia.Atraccion;
import fantasilandia.HorariosAtraccion; // ajusta si difiere

//=======SIA 2.3 VENTANA SWING PARA MENU HORARIOS DE ATRACCION=======
public class MenuHorariosAtraccionGui extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTable tableHorarios;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    private Fantasilandia parque;
    private Atraccion atrSeleccionada;
    private Window parent; // ← para Volver

    private JTextField txtBuscar;

    // === Constructor original ===
    public MenuHorariosAtraccionGui(Fantasilandia parque, Atraccion atrSeleccionada) {
        this.parque = parque;
        this.atrSeleccionada = atrSeleccionada;
        construirUI();
    }

    // === Nuevo con parent para “Volver” - (Otra tecnica para volver ademas de las otras implementadas) ===
    public MenuHorariosAtraccionGui(Window parent, Fantasilandia parque, Atraccion atrSeleccionada) {
        this(parque, atrSeleccionada);
        this.parent = parent;
        setLocationRelativeTo(parent);
    }

    private void construirUI() {
        setTitle("Horarios: " + atrSeleccionada.getNombre() + " (" + atrSeleccionada.getCodigoAtraccion() + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 780, 520);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lbl = new JLabel("Gestionar horarios de: " + atrSeleccionada.getNombre());
        lbl.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 26));
        lbl.setBounds(10, 5, 740, 30);
        contentPane.add(lbl);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setBounds(520, 10, 60, 20);
        contentPane.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(580, 8, 170, 26);
        contentPane.add(txtBuscar);

        model = new DefaultTableModel(new Object[] { "Código Atracción", "Inicio", "Fin" }, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableHorarios = new JTable(model);
        sorter = new TableRowSorter<>(model);
        tableHorarios.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(tableHorarios);
        scroll.setBounds(10, 60, 460, 400);
        contentPane.add(scroll);

        JButton btnAgregar = new JButton("Agregar Horario");
        btnAgregar.addActionListener((ActionEvent e) -> agregarHorarioAtraccionDirecto());
        btnAgregar.setBounds(500, 90, 240, 40);
        contentPane.add(btnAgregar);

        JButton btnModificar = new JButton("Modificar Horario");
        btnModificar.addActionListener((ActionEvent e) -> modificarHorarioAtraccionDirecto());
        btnModificar.setBounds(500, 150, 240, 40);
        contentPane.add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar Horario");
        btnEliminar.addActionListener((ActionEvent e) -> eliminarHorarioAtraccionDirecto());
        btnEliminar.setBounds(500, 210, 240, 40);
        contentPane.add(btnEliminar);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener((ActionEvent e) -> dispose());
        btnCerrar.setBounds(500, 270, 240, 40);
        contentPane.add(btnCerrar);

        // === Botón Volver ===
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> volverAlPadre());
        btnVolver.setBounds(500, 330, 240, 40);
        contentPane.add(btnVolver);

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

        cargarHorariosDeSeleccionada();
    }

    //=========METODOS=======
    //Para volver al menu anterior
    private void volverAlPadre() {
        if (parent != null && !parent.isVisible()) { parent.setVisible(true); parent.toFront(); }
        dispose();
    }

    //Carga los horarios de la atraccion seleccionada en el menu previo
    private void cargarHorariosDeSeleccionada() {
        model.setRowCount(0);

        List<HorariosAtraccion> hs = atrSeleccionada.getHorariosEstandares();
        for (HorariosAtraccion h : hs) {
            model.addRow(new Object[] {
                atrSeleccionada.getCodigoAtraccion(),
                h.getHoraInicio(),
                h.getHoraFin()
            });
        }
        if (model.getRowCount() > 0) tableHorarios.setRowSelectionInterval(0, 0);
    }

    //Permite agregar un nuevo horario estandar a la atraccion
    private void agregarHorarioAtraccionDirecto() {
        String ini = JOptionPane.showInputDialog(this, "Hora inicio (HH:MM):", "Agregar Horario",
                JOptionPane.QUESTION_MESSAGE);
        if (ini == null) return;
        String fin = JOptionPane.showInputDialog(this, "Hora fin (HH:MM):", "Agregar Horario",
                JOptionPane.QUESTION_MESSAGE);
        if (fin == null) return;

        try {
            boolean ok = parque.agregarHorarioAAtraccion(atrSeleccionada.getCodigoAtraccion(),
                    ini.trim(), fin.trim());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Horario agregado.");
                cargarHorariosDeSeleccionada();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar el horario.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Modifica algun horario existente seleccionandolo desde la tabla
    private void modificarHorarioAtraccionDirecto() {
        int viewRow = tableHorarios.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un horario.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int row = tableHorarios.convertRowIndexToModel(viewRow);
        String iniActual = (String) model.getValueAt(row, 1);
        String finActual = (String) model.getValueAt(row, 2);

        String iniNueva = JOptionPane.showInputDialog(this, "Nueva hora inicio (HH:MM):", iniActual);
        if (iniNueva == null) return;
        String finNueva = JOptionPane.showInputDialog(this, "Nueva hora fin (HH:MM):", finActual);
        if (finNueva == null) return;

        try {
            parque.eliminarHorarioDeAtraccion(atrSeleccionada.getCodigoAtraccion(), iniActual, finActual);
            boolean ok = parque.agregarHorarioAAtraccion(atrSeleccionada.getCodigoAtraccion(),
                    iniNueva.trim(), finNueva.trim());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Horario modificado.");
                cargarHorariosDeSeleccionada();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo modificar.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Elimina un horario que hubiese estado seleccionado en la tabla.
    private void eliminarHorarioAtraccionDirecto() {
        int viewRow = tableHorarios.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un horario.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int row = tableHorarios.convertRowIndexToModel(viewRow);
        String ini = (String) model.getValueAt(row, 1);
        String fin = (String) model.getValueAt(row, 2);

        int op = JOptionPane.showConfirmDialog(this,
                "¿Eliminar " + ini + " - " + fin + "?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) return;

        try {
            parque.eliminarHorarioDeAtraccion(atrSeleccionada.getCodigoAtraccion(), ini, fin);
            JOptionPane.showMessageDialog(this, "Horario eliminado.");
            cargarHorariosDeSeleccionada();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
