package GUI;

//import java.awt.EventQueue;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


import fantasilandia.*;

//=======SIA 2.3 VENTANA SWING PARA MENU DIAS=======
public class MenuDiasGui extends JFrame {

    private final JFrame owner;
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private Fantasilandia parque;
    private JTextField txtBuscarCodigo;
    private JButton btnBuscarCodigo;
    private JButton btnLimpiarBusqueda;

    //Crea el JFrame
    public MenuDiasGui(Fantasilandia parque, JFrame owner) {
        this.parque = parque;
        this.owner  = owner;
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 850, 650);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        setTitle("Gestión de Días Activos y Horarios - Fantasilandia");
        setLocationRelativeTo(null);
        
        String[] columnNames = {"Fecha", "Cantidad de Bloques", "Estado"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
  
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L; //Para evitar Warnings

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 60, 500, 520);
        contentPane.add(scrollPane);
        
        JButton btnAgregarDia = new JButton("Agregar Día Activo");
        btnAgregarDia.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarDiaActivo();
            }
        });
        btnAgregarDia.setBounds(540, 113, 250, 40);
        contentPane.add(btnAgregarDia);
        
        JButton btnEliminarDia = new JButton("Eliminar Día Activo");
        btnEliminarDia.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarDiaActivo();
            }
        });
        btnEliminarDia.setBounds(540, 164, 250, 40);
        contentPane.add(btnEliminarDia);
        
        JButton btnModificarDia = new JButton("Modificar Día Activo");
        btnModificarDia.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modificarDiaActivo();
            }
        });
        btnModificarDia.setBounds(540, 215, 250, 40);
        contentPane.add(btnModificarDia);
        
        //=======SIA 2.3 ACCESO  A SUB MENU BLOQUES DE DIA=======
        JButton btnGestionarBloquesDe = new JButton("Gestionar Bloques del Día");
        btnGestionarBloquesDe.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gestionarBloquesDelDia();
            }
        });
        btnGestionarBloquesDe.setBounds(540, 266, 250, 40);
        contentPane.add(btnGestionarBloquesDe);

        // Boton Volver
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
            if (owner != null) owner.setVisible(true);
            dispose();
        });
        btnVolver.setBounds(540, 540, 250, 40);
        contentPane.add(btnVolver);

        
        JLabel TituloVentana = new JLabel("Días Activos Anuales");
        TituloVentana.setHorizontalAlignment(SwingConstants.CENTER);
        TituloVentana.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 32));
        TituloVentana.setBounds(540, 42, 250, 60);
        contentPane.add(TituloVentana);
        
        JLabel lblInfo = new JLabel("Total de días activos: 0");
        lblInfo.setBounds(10, 30, 300, 20);
        contentPane.add(lblInfo);
        
     // === Búsqueda por código (global en todos los días) ===
        JLabel lblBuscar = new JLabel("Buscar bloque por código:");
        lblBuscar.setBounds(540, 317, 200, 20);
        contentPane.add(lblBuscar);

        txtBuscarCodigo = new JTextField();
        txtBuscarCodigo.setBounds(540, 348, 110, 22);
        contentPane.add(txtBuscarCodigo);

        btnBuscarCodigo = new JButton("Buscar");
        btnBuscarCodigo.setBounds(540, 381, 90, 26);
        btnBuscarCodigo.addActionListener(e -> buscarBloquePorCodigoGlobal());
        contentPane.add(btnBuscarCodigo);

        btnLimpiarBusqueda = new JButton("Limpiar");
        btnLimpiarBusqueda.setBounds(640, 381, 90, 26);
        btnLimpiarBusqueda.addActionListener(e -> {
            txtBuscarCodigo.setText("");
            cargarDiasActivos();
        });
        contentPane.add(btnLimpiarBusqueda);


        // Al cerrar con la X, re-mostrar el principal
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosed(java.awt.event.WindowEvent e) {
                if (owner != null) owner.setVisible(true);
            }
        });

        cargarDiasActivos();
        actualizarInfo();
        setLocationRelativeTo(owner);
    }
    
    //Gestionar bloques del día seleccionado
    private void gestionarBloquesDelDia() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un día de la tabla para gestionar sus bloques", 
                "Sin selección", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Obtener la fecha del día seleccionado
        String fechaSeleccionada = (String) tableModel.getValueAt(selectedRow, 0);
        
        // Verificar que el día existe
        DiasActivosAnuales dia = parque.buscarDiaActivoAnual(fechaSeleccionada);
        if (dia == null) {
            JOptionPane.showMessageDialog(this, 
                "El día seleccionado no existe en el sistema", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Abrir el menú de bloques de ese día específico
        MenuBloquesDeDiaGui ventanaBloques = new MenuBloquesDeDiaGui(parque, fechaSeleccionada);
        ventanaBloques.setVisible(true);
    }
    
    //Actualiza la tabla por si se realizo una busqueda.
    private void cargarDiasActivos() {
        tableModel.setRowCount(0);
        Map<String, DiasActivosAnuales> dias = parque.getDiasActivosAnuales();
        
        if (dias != null && !dias.isEmpty()) {
            for (DiasActivosAnuales dia : dias.values()) {
                Object[] row = {
                    dia.getFecha(),
                    dia.getBloques().size(),
                    dia.getBloques().size() > 0 ? "Con bloques" : "Sin bloques"
                };
                tableModel.addRow(row);
            }
        }
        actualizarInfo();
    }
    
    
    private void agregarDiaActivo() {
        String fecha = JOptionPane.showInputDialog(this, 
            "Ingrese la fecha del día activo (YYYY-MM-DD):", 
            "Agregar Día Activo", JOptionPane.QUESTION_MESSAGE);
        
        if (fecha != null && !fecha.trim().isEmpty()) {
            fecha = fecha.trim();
            if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, 
                    "Formato de fecha inválido. Use YYYY-MM-DD", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (parque.agregarDiaActivoAnual(fecha)) {
                cargarDiasActivos();
                JOptionPane.showMessageDialog(this, 
                    "Día activo agregado exitosamente: " + fecha, 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Ya existe un día activo para esa fecha", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    //Permite seleccionar un item de la tabla, y apartir de este poder eliminar el elemento.
    private void eliminarDiaActivo() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un día activo de la tabla", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String fecha = (String) tableModel.getValueAt(selectedRow, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar el día activo " + fecha + "?", 
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (parque.eliminarDiaActivoAnual(fecha)) {
                cargarDiasActivos();
                JOptionPane.showMessageDialog(this, "Día activo eliminado exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar día activo", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    //Permite modificar un dia activo.
    private void modificarDiaActivo() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un día activo de la tabla", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String fechaActual = (String) tableModel.getValueAt(selectedRow, 0);
        String nuevaFecha = JOptionPane.showInputDialog(this, 
            "Ingrese la nueva fecha (YYYY-MM-DD):", fechaActual);
        
        if (nuevaFecha != null && !nuevaFecha.trim().isEmpty()) {
            if (parque.modificarDiaActivoAnual(fechaActual, nuevaFecha.trim())) {
                cargarDiasActivos();
                JOptionPane.showMessageDialog(this, "Fecha modificada exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al modificar fecha", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // === Buscar bloque por código en TODOS los días ===
    private void buscarBloquePorCodigoGlobal() {
        String q = (txtBuscarCodigo != null) ? txtBuscarCodigo.getText() : null;
        if (q == null || q.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el código de bloque a buscar.", 
                "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        q = q.trim();

        // Limpiar tabla y cargar solo los días que contienen el bloque
        tableModel.setRowCount(0);

        Map<String, DiasActivosAnuales> dias = parque.getDiasActivosAnuales();
        int encontrados = 0;
        if (dias != null && !dias.isEmpty()) {
          for (DiasActivosAnuales dia : dias.values()) {
            java.util.List<BloqueDeAtraccion> bloques = dia.getBloques();
            if (bloques == null || bloques.isEmpty()) continue;

            boolean match = false;
            for (BloqueDeAtraccion b : bloques) {
              String cod = b.getCodigoBloque();
              if (cod == null) continue;
              if (cod.equalsIgnoreCase(q) || cod.toLowerCase().contains(q.toLowerCase())) {
                  match = true; break;
              }
            }
            if (match) {
              Object[] row = {
                dia.getFecha(),
                dia.getBloques().size(),
                dia.getBloques().size() > 0 ? "Con bloques" : "Sin bloques"
              };
              tableModel.addRow(row);
              encontrados++;
            }
          }
        }

        if (encontrados == 0) {
            JOptionPane.showMessageDialog(this, 
                "No se encontraron días con un bloque de código: " + q, 
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
        actualizarInfo();
    }

   
    //Actualiza la tabla
    private void actualizarInfo() {
        for (java.awt.Component comp : contentPane.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getText().startsWith("Total")) {
                ((JLabel) comp).setText("Total de días activos: " + parque.getDiasActivosAnuales().size());
                break;
            }
        }
    }
}