package GUI;

//import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Excepciones.BloqueMalFormateadoException;
import Excepciones.FechaMalFormateadaException;
import Excepciones.HorarioMalFormateadoException;
// Importar las clases del modelo
import fantasilandia.*;

/*
 * MENU EXTRA PARA VISUALIZAR TODOS LOS BLOQUES EXISTENTES,
 * Metodos e implementacion utilizados en MenuBloquesDeDia para mayor Eficacia y comodidad del usuario.
 * Se puede descomentar del menu principal y tener las funcionalidades asociadas a este menu. 
 */

public class MenuBloquesGui extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private Fantasilandia parque;
    private JTextField txtFechaBuscar;

    public MenuBloquesGui(Fantasilandia parque) {
        this.parque = parque;
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 950, 700);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        setTitle("Gestión de Bloques de Horarios - Fantasilandia");
        setLocationRelativeTo(null);
        
        String[] columnNames = {"Código Bloque", "Fecha", "Atracción", "Horario", "Clientes"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 60, 650, 580);
        contentPane.add(scrollPane);
        
        JButton btnAgregarBloque = new JButton("Agregar Bloque");
        btnAgregarBloque.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarBloque();
            }
        });
        btnAgregarBloque.setBounds(690, 113, 190, 44);
        contentPane.add(btnAgregarBloque);
        
        JButton btnEliminarBloque = new JButton("Eliminar Bloque");
        btnEliminarBloque.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarBloque();
            }
        });
        btnEliminarBloque.setBounds(690, 168, 190, 44);
        contentPane.add(btnEliminarBloque);
        
        JButton btnModificarBloque = new JButton("Modificar Bloque");
        btnModificarBloque.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					modificarBloque();
				} catch (BloqueMalFormateadoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (HorarioMalFormateadoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (FechaMalFormateadaException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        btnModificarBloque.setBounds(690, 223, 190, 44);
        contentPane.add(btnModificarBloque);
        
        JButton btnListarPorFecha = new JButton("Listar por Fecha");
        btnListarPorFecha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listarBloquesPorFecha();
            }
        });
        btnListarPorFecha.setBounds(690, 278, 190, 44);
        contentPane.add(btnListarPorFecha);
        
        JLabel TituloVentana = new JLabel("Gestión de Bloques");
        TituloVentana.setHorizontalAlignment(SwingConstants.CENTER);
        TituloVentana.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 32));
        TituloVentana.setBounds(690, 20, 190, 60);
        contentPane.add(TituloVentana);
        
        JButton btnInsertarCliente = new JButton("Insertar Cliente");
        btnInsertarCliente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					insertarClienteEnBloque();
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (BloqueMalFormateadoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        btnInsertarCliente.setBounds(690, 333, 190, 44);
        contentPane.add(btnInsertarCliente);
        
        JButton btnListarClientes = new JButton("Listar Clientes");
        btnListarClientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listarClientesDeBloque();
            }
        });
        btnListarClientes.setBounds(690, 388, 190, 44);
        contentPane.add(btnListarClientes);
        
        // Campo para buscar por fecha
        txtFechaBuscar = new JTextField();
        txtFechaBuscar.setBounds(690, 480, 190, 25);
        contentPane.add(txtFechaBuscar);
        
        JLabel lblFechaBuscar = new JLabel("Fecha (YYYY-MM-DD):");
        lblFechaBuscar.setBounds(690, 460, 150, 20);
        contentPane.add(lblFechaBuscar);
        
        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarTodosLosBloques();
            }
        });
        btnActualizar.setBounds(690, 520, 90, 35);
        contentPane.add(btnActualizar);
        
        JButton btnBuscarFecha = new JButton("Buscar");
        btnBuscarFecha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarPorFecha();
            }
        });
        btnBuscarFecha.setBounds(790, 520, 90, 35);
        contentPane.add(btnBuscarFecha);
        
        JLabel lblInfo = new JLabel("Total de bloques: 0");
        lblInfo.setBounds(10, 30, 300, 20);
        contentPane.add(lblInfo);
        
        cargarTodosLosBloques();
        actualizarInfo();
    }
    
    private void cargarTodosLosBloques() {
        tableModel.setRowCount(0);
        
        for (DiasActivosAnuales dia : parque.getDiasActivosAnuales().values()) {
            for (BloqueDeAtraccion bloque : dia.getBloques()) {
                Object[] row = {
                    bloque.getCodigoBloque(),
                    bloque.getFecha(),
                    bloque.getAtraccion().getNombre(),
                    bloque.getHorario().toString(),
                    bloque.getClientesParticipantes().size() + " clientes"
                };
                tableModel.addRow(row);
            }
        }
        actualizarInfo();
    }
    
    @SuppressWarnings("unused")
	private void agregarBloque() {
        JTextField txtFecha = new JTextField();
        JTextField txtCodigo = new JTextField();
        JTextField txtHoraInicio = new JTextField("10:00");
        JTextField txtHoraFin = new JTextField("11:00");
        
        // Crear combobox con atracciones disponibles
        JComboBox<String> cbAtracciones = new JComboBox<>();
        for (Atraccion atraccion : parque.getAtracciones()) {
            cbAtracciones.addItem(atraccion.getCodigoAtraccion() + " - " + atraccion.getNombre());
        }
        
        Object[] message = {
            "Fecha (YYYY-MM-DD):", txtFecha,
            "Código de Bloque:", txtCodigo,
            "Atracción:", cbAtracciones,
            "Hora Inicio (HH:mm):", txtHoraInicio,
            "Hora Fin (HH:mm):", txtHoraFin
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Agregar Bloque", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String fecha = txtFecha.getText().trim();
            String codigo = txtCodigo.getText().trim();
            String horaInicio = txtHoraInicio.getText().trim();
            String horaFin = txtHoraFin.getText().trim();
            
            if (fecha.isEmpty() || codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fecha y código son obligatorios", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (cbAtracciones.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una atracción", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String seleccionAtraccion = (String) cbAtracciones.getSelectedItem();
            String codigoAtraccion = seleccionAtraccion.split(" - ")[0];
            Atraccion atraccion = parque.buscarAtraccion(codigoAtraccion);
            
            try {
                BloqueDeAtraccion bloque = parque.getOCrearBloque(fecha, codigo, atraccion, 
                    new HorariosAtraccion(horaInicio, horaFin));
                
                cargarTodosLosBloques();
                JOptionPane.showMessageDialog(this, "Bloque creado exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear bloque: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void eliminarBloque() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un bloque de la tabla", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String codigoBloque = (String) tableModel.getValueAt(selectedRow, 0);
        String fecha = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar el bloque " + codigoBloque + " del " + fecha + "?", 
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            DiasActivosAnuales dia = parque.buscarDiaActivoAnual(fecha);
            if (dia != null) {
                BloqueDeAtraccion bloque = dia.buscarBloque(codigoBloque);
                if (bloque != null) {
                    dia.getBloques().remove(bloque);
                    cargarTodosLosBloques();
                    JOptionPane.showMessageDialog(this, "Bloque eliminado exitosamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
    
    private void modificarBloque() throws BloqueMalFormateadoException, HorarioMalFormateadoException, FechaMalFormateadaException {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un bloque de la tabla", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String codigoBloque = (String) tableModel.getValueAt(selectedRow, 0);
        String fecha = (String) tableModel.getValueAt(selectedRow, 1);
        
        DiasActivosAnuales dia = parque.buscarDiaActivoAnual(fecha);
        if (dia == null) return;
        
        BloqueDeAtraccion bloque = dia.buscarBloque(codigoBloque);
        if (bloque == null) return;
        
        String[] opciones = {"Cambiar código", "Cambiar atracción", "Cambiar horario", "Mover a otra fecha"};
        String seleccion = (String) JOptionPane.showInputDialog(this, 
            "¿Qué desea modificar?", "Modificar Bloque", 
            JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        
        if (seleccion == null) return;
        
        switch (seleccion) {
            case "Cambiar código":
                String nuevoCodigo = JOptionPane.showInputDialog(this, "Nuevo código:", codigoBloque);
                if (nuevoCodigo != null && !nuevoCodigo.trim().isEmpty()) {
                    bloque.setCodigoBloque(nuevoCodigo.trim());
                }
                break;
                
            case "Cambiar atracción":
                JComboBox<String> cbAtracciones = new JComboBox<>();
                for (Atraccion atr : parque.getAtracciones()) {
                    cbAtracciones.addItem(atr.getCodigoAtraccion() + " - " + atr.getNombre());
                }
                int resultado = JOptionPane.showConfirmDialog(this, cbAtracciones, 
                    "Seleccionar nueva atracción", JOptionPane.OK_CANCEL_OPTION);
                if (resultado == JOptionPane.OK_OPTION && cbAtracciones.getSelectedItem() != null) {
                    String sel = (String) cbAtracciones.getSelectedItem();
                    String codAtr = sel.split(" - ")[0];
                    Atraccion nuevaAtraccion = parque.buscarAtraccion(codAtr);
                    if (nuevaAtraccion != null) {
                        bloque.setAtraccion(nuevaAtraccion);
                    }
                }
                break;
                
            case "Cambiar horario":
                String nuevaHoraInicio = JOptionPane.showInputDialog(this, "Hora inicio:", 
                    bloque.getHorario().getHoraInicio());
                if (nuevaHoraInicio == null) return;
                
                String nuevaHoraFin = JOptionPane.showInputDialog(this, "Hora fin:", 
                    bloque.getHorario().getHoraFin());
                if (nuevaHoraFin == null) return;
                
                bloque.setHorario(new HorariosAtraccion(nuevaHoraInicio.trim(), nuevaHoraFin.trim()));
                break;
                
            case "Mover a otra fecha":
                String nuevaFecha = JOptionPane.showInputDialog(this, "Nueva fecha (YYYY-MM-DD):");
                if (nuevaFecha != null && !nuevaFecha.trim().isEmpty()) {
                    // Quitamos del día original primero
                    dia.getBloques().remove(bloque);
                    String fechaOriginal = dia.getFecha();
                    bloque.setFecha(nuevaFecha.trim());
                    DiasActivosAnuales nuevoDia = parque.getODia(nuevaFecha.trim());
                    //SIA 2.8 insercion de caso de try-catch ante un error donde el bloque ya existe en una fecha especifica.
                    try {
                        nuevoDia.addBloque(bloque);
                    } catch (BloqueMalFormateadoException ex) {
                        // Revertir la operación para no perder el bloque
                        bloque.setFecha(fechaOriginal);
                        dia.getBloques().add(bloque);
                        JOptionPane.showMessageDialog(this,
                            "No se pudo mover el bloque: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;

        }
        
        cargarTodosLosBloques();
        JOptionPane.showMessageDialog(this, "Bloque modificado exitosamente", 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void listarBloquesPorFecha() {
        String fecha = JOptionPane.showInputDialog(this, 
            "Ingrese la fecha (YYYY-MM-DD):", "Listar Bloques por Fecha", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (fecha != null && !fecha.trim().isEmpty()) {
            buscarYMostrarBloquesPorFecha(fecha.trim());
        }
    }
    
    private void insertarClienteEnBloque() throws HeadlessException, BloqueMalFormateadoException {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un bloque de la tabla", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String codigoBloque = (String) tableModel.getValueAt(selectedRow, 0);
        String fecha = (String) tableModel.getValueAt(selectedRow, 1);
        
        String rut = JOptionPane.showInputDialog(this, 
            "Ingrese el RUT del cliente:", "Insertar Cliente", 
            JOptionPane.QUESTION_MESSAGE);
        
        //SIA 2.8 Try catch en caso de que no se encuentre el cliente o bloque, y en casos de no poder inscribir al cliente.
        if (rut != null && !rut.trim().isEmpty()) {
        	try {
        	    if (parque.agregarClienteABloquePorRut(fecha, codigoBloque, rut.trim())) {
        	        cargarTodosLosBloques();
        	        JOptionPane.showMessageDialog(this, "Cliente inscrito exitosamente en el bloque",
        	            "Éxito", JOptionPane.INFORMATION_MESSAGE);
        	    } else {
        	        JOptionPane.showMessageDialog(this, "Error: Cliente o bloque no encontrado",
        	            "Error", JOptionPane.ERROR_MESSAGE);
        	    }
        	} catch (Exception e) {
        	    JOptionPane.showMessageDialog(this,
        	        "Error al inscribir cliente en bloque: " + e.getMessage(),
        	        "Error", JOptionPane.ERROR_MESSAGE);
        	}
        }
    }
    
    private void listarClientesDeBloque() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un bloque de la tabla", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String codigoBloque = (String) tableModel.getValueAt(selectedRow, 0);
        String fecha = (String) tableModel.getValueAt(selectedRow, 1);
        
        DiasActivosAnuales dia = parque.buscarDiaActivoAnual(fecha);
        if (dia == null) return;
        
        BloqueDeAtraccion bloque = dia.buscarBloque(codigoBloque);
        if (bloque == null) return;
        
        StringBuilder clientes = new StringBuilder();
        clientes.append("Clientes del bloque ").append(codigoBloque)
               .append(" (").append(fecha).append("):\n\n");
        
        List<Cliente> clientesBloque = bloque.getClientesParticipantes();
        if (clientesBloque.isEmpty()) {
            clientes.append("No hay clientes inscritos en este bloque.");
        } else {
            for (int i = 0; i < clientesBloque.size(); i++) {
                Cliente c = clientesBloque.get(i);
                clientes.append((i + 1)).append(") ")
                        .append(c.getNombre()).append(" - ")
                        .append(c.getRut()).append("\n");
            }
        }
        
        JOptionPane.showMessageDialog(this, clientes.toString(), 
            "Clientes del Bloque", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void buscarPorFecha() {
        String fecha = txtFechaBuscar.getText().trim();
        if (fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una fecha para buscar", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        buscarYMostrarBloquesPorFecha(fecha);
    }
    
    private void buscarYMostrarBloquesPorFecha(String fecha) {
        DiasActivosAnuales dia = parque.buscarDiaActivoAnual(fecha);
        if (dia == null) {
            JOptionPane.showMessageDialog(this, "No se encontró día activo: " + fecha, 
                "No encontrado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        tableModel.setRowCount(0);
        List<BloqueDeAtraccion> bloques = dia.getBloques();
        
        if (bloques.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay bloques para la fecha: " + fecha, 
                "Sin bloques", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (BloqueDeAtraccion bloque : bloques) {
                Object[] row = {
                    bloque.getCodigoBloque(),
                    bloque.getFecha(),
                    bloque.getAtraccion().getNombre(),
                    bloque.getHorario().toString(),
                    bloque.getClientesParticipantes().size() + " clientes"
                };
                tableModel.addRow(row);
            }
        }
        actualizarInfo();
    }
    
    private void actualizarInfo() {
        int totalBloques = 0;
        for (DiasActivosAnuales dia : parque.getDiasActivosAnuales().values()) {
            totalBloques += dia.getBloques().size();
        }
        
        for (java.awt.Component comp : contentPane.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getText().startsWith("Total")) {
                ((JLabel) comp).setText("Total de bloques: " + totalBloques);
                break;
            }
        }
    }
}