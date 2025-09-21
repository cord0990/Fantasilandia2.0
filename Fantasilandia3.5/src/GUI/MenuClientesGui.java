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

// Importar las clases del modelo
import fantasilandia.*;

public class MenuClientesGui extends JFrame {

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
                    MenuClientesGui frame = new MenuClientesGui(parque);
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
    public MenuClientesGui(Fantasilandia parque) {
        this.parque = parque;
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        setTitle("Gestión de Clientes - Fantasilandia");
        setLocationRelativeTo(null);
        
        // Configurar tabla con modelo personalizado
        String[] columnNames = {"ID Cliente", "Nombre", "RUT", "Fecha Nacimiento"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer tabla no editable directamente
            }
        };
        
        table = new JTable(tableModel);
        table.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 60, 480, 480);
        contentPane.add(scrollPane);
        
        JButton btnAgregar = new JButton("Agregar Cliente");
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					agregarCliente();
				} catch (RutMalFormateadoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (FechaMalFormateadaException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        btnAgregar.setBounds(520, 120, 190, 50);
        contentPane.add(btnAgregar);
        
        JButton btnEliminar = new JButton("Eliminar Cliente");
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarCliente();
            }
        });
        btnEliminar.setBounds(520, 180, 190, 50);
        contentPane.add(btnEliminar);
        
        JButton btnModificar = new JButton("Modificar Cliente");
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					modificarCliente();
				} catch (FechaMalFormateadaException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        btnModificar.setBounds(520, 240, 190, 50);
        contentPane.add(btnModificar);
        
        JButton btnBuscar = new JButton("Buscar por RUT");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarCliente();
            }
        });
        btnBuscar.setBounds(520, 470, 190, 50);
        contentPane.add(btnBuscar);
        
        JLabel TituloVentana = new JLabel("Gestión de Clientes");
        TituloVentana.setHorizontalAlignment(SwingConstants.CENTER);
        TituloVentana.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 32));
        TituloVentana.setBounds(520, 20, 190, 60);
        contentPane.add(TituloVentana);
        
        // Campo de búsqueda
        txtBuscar = new JTextField();
        txtBuscar.setBounds(520, 434, 190, 25);
        contentPane.add(txtBuscar);
        txtBuscar.setColumns(10);
        
        JLabel lblBuscar = new JLabel("RUT a buscar:");
        lblBuscar.setBounds(520, 403, 100, 20);
        contentPane.add(lblBuscar);
        
        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarClientes();
            }
        });
        btnActualizar.setBounds(520, 301, 190, 40);
        contentPane.add(btnActualizar);
        
        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        btnMostrarTodos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarTodosLosClientes();
            }
        });
        btnMostrarTodos.setBounds(520, 352, 190, 40);
        contentPane.add(btnMostrarTodos);
        
        // Etiqueta con información
        JLabel lblInfo = new JLabel("Total de clientes: 0");
        lblInfo.setBounds(10, 30, 300, 20);
        contentPane.add(lblInfo);
        
        // Cargar datos iniciales
        cargarClientes();
        actualizarInfo();
    }
    
    private void cargarClientes() {
        // Limpiar tabla
        tableModel.setRowCount(0);
        
        // Obtener lista de clientes desde Fantasilandia
        for (Cliente cliente : parque.getClientes()) {
            Object[] row = {
                cliente.getIdCliente(),
                cliente.getNombre(),
                cliente.getRut(),
                cliente.getFechaNacimiento()
            };
            tableModel.addRow(row);
        }
        actualizarInfo();
    }
    
    private void agregarCliente() throws RutMalFormateadoException, FechaMalFormateadaException {
        // Crear panel de entrada de datos
        JTextField txtNombre = new JTextField();
        JTextField txtRut = new JTextField();
        JTextField txtFechaNacimiento = new JTextField();
        
        Object[] message = {
            "Nombre:", txtNombre,
            "RUT:", txtRut,
            "Fecha de Nacimiento (YYYY-MM-DD):", txtFechaNacimiento
        };
        
        int option = JOptionPane.showConfirmDialog(
            this, message, "Agregar Cliente", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String rut = txtRut.getText().trim();
            String fecha = txtFechaNacimiento.getText().trim();
            
            if (nombre.isEmpty() || rut.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y RUT son obligatorios", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Verificar si el cliente ya existe
            if (parque.buscarCliente(rut) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un cliente con ese RUT", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Generar ID automático
            String id = "CLI" + String.format("%03d", parque.contarClientes() + 1);
            
            // Crear y agregar cliente
            Cliente nuevoCliente = new Cliente(nombre, rut, id, fecha);
            parque.addCliente(nuevoCliente);
            
            // Actualizar tabla
            cargarClientes();
            
            JOptionPane.showMessageDialog(this, 
                "Cliente agregado exitosamente con ID: " + id, 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
        
        // Pre-llenar campos con datos actuales
        JTextField txtNombre = new JTextField(cliente.getNombre());
        JTextField txtFechaNacimiento = new JTextField(cliente.getFechaNacimiento());
        
        Object[] message = {
            "Nombre:", txtNombre,
            "Fecha de Nacimiento (YYYY-MM-DD):", txtFechaNacimiento,
            "Nota: El RUT no se puede modificar"
        };
        
        int option = JOptionPane.showConfirmDialog(
            this, message, "Modificar Cliente", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String nuevoNombre = txtNombre.getText().trim();
            String nuevaFecha = txtFechaNacimiento.getText().trim();
            
            if (!nuevoNombre.isEmpty()) {
                cliente.setNombre(nuevoNombre);
            }
            if (!nuevaFecha.isEmpty()) {
                try {
					cliente.setFechaNacimiento(nuevaFecha);
				} catch (FechaMalFormateadaException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            cargarClientes();
            JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
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
        
        // Mostrar solo el cliente encontrado
        tableModel.setRowCount(0);
        Object[] row = {
            cliente.getIdCliente(),
            cliente.getNombre(),
            cliente.getRut(),
            cliente.getFechaNacimiento()
        };
        tableModel.addRow(row);
        
        // Seleccionar la fila
        table.setRowSelectionInterval(0, 0);
    }
    
    private void mostrarTodosLosClientes() {
        txtBuscar.setText("");
        cargarClientes();
    }
    
    private void actualizarInfo() {
        // Actualizar etiqueta de información
        for (java.awt.Component comp : contentPane.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getText().startsWith("Total")) {
                ((JLabel) comp).setText("Total de clientes: " + parque.contarClientes());
                break;
            }
        }
    }
}