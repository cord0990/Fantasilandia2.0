package GUI;

//import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
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

import Excepciones.HorarioMalFormateadoException;


// Importar las clases del modelo
import fantasilandia.*;

public class MenuHorariosGui extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private Fantasilandia parque;
    private JTextField txtFecha;

    public MenuHorariosGui(Fantasilandia parque) {
        this.parque = parque;
        
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
        
        JButton btnAgregarHorario = new JButton("Agregar Horario a Atracción");
        btnAgregarHorario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarHorarioAtraccion();
            }
        });
        btnAgregarHorario.setBounds(540, 296, 250, 40);
        contentPane.add(btnAgregarHorario);
        
        JLabel TituloVentana = new JLabel("Días Activos Anuales");
        TituloVentana.setHorizontalAlignment(SwingConstants.CENTER);
        TituloVentana.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 32));
        TituloVentana.setBounds(540, 20, 250, 60);
        contentPane.add(TituloVentana);
        
        JButton btnListarHorarios = new JButton("Listar Horarios de Atracción");
        btnListarHorarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listarHorariosAtraccion();
            }
        });
        btnListarHorarios.setBounds(540, 347, 250, 40);
        contentPane.add(btnListarHorarios);
        
        txtFecha = new JTextField();
        txtFecha.setBounds(540, 420, 250, 25);
        contentPane.add(txtFecha);
        
        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        lblFecha.setBounds(540, 400, 150, 20);
        contentPane.add(lblFecha);
        
        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarDiasActivos();
            }
        });
        btnActualizar.setBounds(540, 456, 120, 35);
        contentPane.add(btnActualizar);
        
        JButton btnBuscarFecha = new JButton("Buscar");
        btnBuscarFecha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarPorFecha();
            }
        });
        btnBuscarFecha.setBounds(670, 456, 120, 35);
        contentPane.add(btnBuscarFecha);
        
        JLabel lblInfo = new JLabel("Total de días activos: 0");
        lblInfo.setBounds(10, 30, 300, 20);
        contentPane.add(lblInfo);
        
        cargarDiasActivos();
        actualizarInfo();
    }
    
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
    
    private void agregarHorarioAtraccion() {
        String codigo = JOptionPane.showInputDialog(this, 
            "Ingrese el código de la atracción:", 
            "Agregar Horario", JOptionPane.QUESTION_MESSAGE);
        
        if (codigo == null || codigo.trim().isEmpty()) return;
        
        String horaInicio = JOptionPane.showInputDialog(this, 
            "Ingrese hora de inicio (HH:mm):", "10:00");
        if (horaInicio == null) return;
        
        String horaFin = JOptionPane.showInputDialog(this, 
            "Ingrese hora de fin (HH:mm):", "11:00");
        if (horaFin == null) return;
        
        try {
			if (parque.agregarHorarioAAtraccion(codigo.trim().toUpperCase(), 
			                                   horaInicio.trim(), horaFin.trim())) {
			    JOptionPane.showMessageDialog(this, 
			        "Horario agregado exitosamente a la atracción", 
			        "Éxito", JOptionPane.INFORMATION_MESSAGE);
			} else {
			    JOptionPane.showMessageDialog(this, 
			        "Error: Atracción no encontrada", 
			        "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (HeadlessException | HorarioMalFormateadoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void listarHorariosAtraccion() {
        String codigo = JOptionPane.showInputDialog(this, 
            "Ingrese el código de la atracción:", 
            "Listar Horarios", JOptionPane.QUESTION_MESSAGE);
        
        if (codigo != null && !codigo.trim().isEmpty()) {
            Atraccion atraccion = parque.buscarAtraccion(codigo.trim().toUpperCase());
            
            if (atraccion == null) {
                JOptionPane.showMessageDialog(this, 
                    "No se encontró atracción con código: " + codigo, 
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
    }
    
    private void buscarPorFecha() {
        String fecha = txtFecha.getText().trim();
        if (fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una fecha para buscar", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        DiasActivosAnuales dia = parque.buscarDiaActivoAnual(fecha);
        if (dia == null) {
            JOptionPane.showMessageDialog(this, "No se encontró día activo: " + fecha, 
                "No encontrado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        tableModel.setRowCount(0);
        Object[] row = {
            dia.getFecha(),
            dia.getBloques().size(),
            dia.getBloques().size() > 0 ? "Con bloques" : "Sin bloques"
        };
        tableModel.addRow(row);
        table.setRowSelectionInterval(0, 0);
    }
    
    private void actualizarInfo() {
        for (java.awt.Component comp : contentPane.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getText().startsWith("Total")) {
                ((JLabel) comp).setText("Total de días activos: " + parque.getDiasActivosAnuales().size());
                break;
            }
        }
    }
}