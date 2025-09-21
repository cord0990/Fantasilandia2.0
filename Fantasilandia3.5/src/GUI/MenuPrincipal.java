package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Excepciones.BloqueMalFormateadoException;
import Excepciones.FechaMalFormateadaException;
import Excepciones.HorarioMalFormateadoException;
import Excepciones.RutMalFormateadoException;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;

// Importar las clases del modelo
import fantasilandia.*;

public class MenuPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Fantasilandia parque; // Instancia compartida del sistema

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MenuPrincipal frame = new MenuPrincipal();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     * @throws BloqueMalFormateadoException 
     * @throws HorarioMalFormateadoException 
     * @throws FechaMalFormateadaException 
     * @throws RutMalFormateadoException 
     */
    public MenuPrincipal() throws RutMalFormateadoException, FechaMalFormateadaException, HorarioMalFormateadoException, BloqueMalFormateadoException {
        // Inicializar el sistema Fantasilandia
        parque = new Fantasilandia(true); // Con datos de ejemplo
        
        // Intentar cargar datos existentes
        try {
            parque.cargarDesdeCSV("data");
        } catch (Exception e) {
            System.out.println("No se pudieron cargar datos previos. Usando datos de ejemplo.");
        }
        
        initComponents();
    }
    
    /**
     * Constructor que acepta una instancia existente de Fantasilandia
     */
    public MenuPrincipal(Fantasilandia parqueExistente) {
        this.parque = parqueExistente;
        initComponents();
    }
    
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 727, 543);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        setTitle("Sistema de Administracion de parque tematico");
        setResizable(false);
        setLocationRelativeTo(null);
        
        JLabel Titulo = new JLabel("Sistema Administrativo de Fantasilandia");
        Titulo.setHorizontalAlignment(SwingConstants.CENTER);
        Titulo.setBounds(48, 22, 607, 84);
        Titulo.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        contentPane.add(Titulo);
        
        JButton IngresoClientes = new JButton("Gestion de Clientes");
        IngresoClientes.setFont(new Font("Tw Cen MT", Font.PLAIN, 23));
        IngresoClientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirMenuClientes();
            }
        });
        IngresoClientes.setBounds(361, 117, 291, 154);
        contentPane.add(IngresoClientes);
        
        JButton IngresoAtracciones = new JButton("Gestion de Atracciones");
        IngresoAtracciones.setFont(new Font("Tw Cen MT", Font.PLAIN, 23));
        IngresoAtracciones.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirMenuAtracciones();
            }
        });
        IngresoAtracciones.setBounds(48, 117, 291, 154);
        contentPane.add(IngresoAtracciones);
        
        JButton IngresoHorarios = new JButton("Gestion de Horarios");
        IngresoHorarios.setFont(new Font("Tw Cen MT", Font.PLAIN, 23));
        IngresoHorarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirMenuHorarios();
            }
        });
        IngresoHorarios.setBounds(48, 302, 291, 154);
        contentPane.add(IngresoHorarios);
        
        JButton IngresoBloques = new JButton("Gestion de Bloques");
        IngresoBloques.setFont(new Font("Tw Cen MT", Font.PLAIN, 23));
        IngresoBloques.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirMenuBloques();
            }
        });
        IngresoBloques.setBounds(361, 302, 291, 154);
        contentPane.add(IngresoBloques);
        
        // Agregar botón de guardar datos
        JButton btnGuardar = new JButton("Guardar Datos");
        btnGuardar.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarDatos();
            }
        });
        btnGuardar.setBounds(572, 467, 129, 35);
        contentPane.add(btnGuardar);
        
     // Dentro del constructor de MenuPrincipal (donde defines botones):
        JButton btnReporte = new JButton("Generar Reporte");
        btnReporte.setBounds(231, 467, 126, 34); // tamaño pequeño
        btnReporte.addActionListener(e -> {
            try {
                parque.generarReporteTxt("data");  // genera reporte.txt en carpeta data
                JOptionPane.showMessageDialog(this, "Reporte generado con éxito en /data/reporte.txt");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage());
            }
        });
        getContentPane().add(btnReporte);

        
        
        // Agregar información del sistema
        JLabel lblInfo = new JLabel();
        actualizarInfoSistema(lblInfo);
        lblInfo.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblInfo.setBounds(10, 468, 500, 35);
        contentPane.add(lblInfo);
        
        // Configurar cierre con guardado automático
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(MenuPrincipal.this, 
                    "¿Desea guardar los datos antes de salir?", "Confirmar salida", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    guardarDatos();
                }
                System.exit(0);
            }
        });
    }
    
    private void abrirMenuClientes() {
        MenuClientesGui menuClientes = new MenuClientesGui(parque);
        menuClientes.setVisible(true);
        // No cerrar la ventana principal
    }
    
    private void abrirMenuAtracciones() {
        MenuAtraccionesGui menuAtracciones = new MenuAtraccionesGui(parque);
        menuAtracciones.setVisible(true);
    }
    
    private void abrirMenuHorarios() {
        MenuHorariosGui menuHorarios = new MenuHorariosGui(parque);
        menuHorarios.setVisible(true);
    }
    
    private void abrirMenuBloques() {
        MenuBloquesGui menuBloques = new MenuBloquesGui(parque);
        menuBloques.setVisible(true);
    }
    
    private void guardarDatos() {
        try {
            parque.guardarEnCSV("data");
            JOptionPane.showMessageDialog(this, 
                "Datos guardados exitosamente en carpeta 'data'", 
                "Guardado exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al guardar datos: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarInfoSistema(JLabel lblInfo) {
        String info = String.format("Clientes: %d | Atracciones: %d | Días activos: %d", 
            parque.contarClientes(), 
            parque.contarAtracciones(),
            parque.getDiasActivosAnuales().size());
        lblInfo.setText(info);
    }
    
    // Getter para obtener la instancia de Fantasilandia
    public Fantasilandia getParque() {
        return parque;
    }
}