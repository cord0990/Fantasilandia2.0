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

//Carpetas para chekear la existencia de los archivos
import java.nio.file.Files;
import java.nio.file.Paths;

//para las imagenes
import javax.swing.*;
import java.awt.*;

// Importar las clases del modelo
import fantasilandia.*;

public class MenuPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Fantasilandia parque; // Instancia compartida del sistema

    //lblInfo como campo para poder refrescarlo al cerrar subventanas
    private JLabel lblInfo;


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
            if (Files.exists(Paths.get("data"))) {
                parque.cargarDesdeCSV("data");
            } else {
                System.out.println("No hay datos previos, se usarán datos de ejemplo.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un problema al cargar datos previos.\nSe usarán datos de ejemplo.\nDetalle: " + e.getMessage(),
                    "Aviso de carga", JOptionPane.WARNING_MESSAGE);
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
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        // Usa el panel con fondo
        contentPane = new BackgroundPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        setTitle("Sistema de Administracion de parque tematico");
        setResizable(false);
        setLocationRelativeTo(null);

        // Menú superior (Archivo: Guardar, Reporte, Salir)
        setJMenuBar(buildMenuBar());

        //creacion de titulo
        JLabel Titulo = new JLabel("Sistema Administrativo de Fantasilandia");
        Titulo.setHorizontalAlignment(SwingConstants.CENTER);
        Titulo.setBounds(55, 20, 700, 84);
        Titulo.setFont(new Font("Comic Sans MS", Font.PLAIN, 28));
        contentPane.add(Titulo);

        //Botones
        JButton IngresoClientes = new JButton("Gestion de Clientes");
        IngresoClientes.setFont(new Font("Tw Cen MT", Font.PLAIN, 23));
        IngresoClientes.addActionListener(e -> abrirMenuClientes());
        IngresoClientes.setBounds(15, 117, 355, 70);
        contentPane.add(IngresoClientes);

        JButton IngresoAtracciones = new JButton("Gestion de Atracciones");
        IngresoAtracciones.setFont(new Font("Tw Cen MT", Font.PLAIN, 23));
        IngresoAtracciones.addActionListener(e -> abrirMenuAtracciones());
        IngresoAtracciones.setBounds(15, 200, 355, 70);
        contentPane.add(IngresoAtracciones);

        JButton IngresoHorarios = new JButton("Gestion de Horarios");
        IngresoHorarios.setFont(new Font("Tw Cen MT", Font.PLAIN, 23));
        IngresoHorarios.addActionListener(e -> abrirMenuHorarios());
        IngresoHorarios.setBounds(15, 280, 355, 70);
        contentPane.add(IngresoHorarios);

        JButton IngresoBloques = new JButton("Gestion de Bloques");
        IngresoBloques.setFont(new Font("Tw Cen MT", Font.PLAIN, 23));
        IngresoBloques.addActionListener(e -> abrirMenuBloques());
        IngresoBloques.setBounds(15, 360, 355, 70);
        contentPane.add(IngresoBloques);

        // Botón Guardar (batch out)
        JButton btnGuardar = new JButton("Guardar Datos");
        btnGuardar.setFont(new Font("Tw Cen MT", Font.PLAIN, 16));
        btnGuardar.addActionListener(e -> guardarDatos());
        btnGuardar.setBounds(500, 467, 165, 35);
        contentPane.add(btnGuardar);

        // Botón Reporte TXT
        JButton btnReporte = new JButton("Generar Reporte");
        btnReporte.setBounds(231, 467, 165, 34);
        btnReporte.addActionListener(e -> {
            try {
                ensureDataDir();
                parque.generarReporteTxt("data");  // genera reporte.txt en carpeta data
                JOptionPane.showMessageDialog(this, "Reporte generado con éxito en data/reporte.txt");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage());
            }
        });
        //consistencia (usar contentPane.add, no getContentPane().add)
        contentPane.add(btnReporte);

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

    // ===== Helpers de UI =====

    // Menú superior
    private javax.swing.JMenuBar buildMenuBar() {
        javax.swing.JMenuBar mb = new javax.swing.JMenuBar();

        javax.swing.JMenu archivo = new javax.swing.JMenu("Archivo");
        javax.swing.JMenuItem miGuardar = new javax.swing.JMenuItem("Guardar (CSV)");
        javax.swing.JMenuItem miReporte = new javax.swing.JMenuItem("Generar Reporte TXT");
        javax.swing.JMenuItem miSalir   = new javax.swing.JMenuItem("Salir");

        miGuardar.addActionListener(e -> guardarDatos());
        miReporte.addActionListener(e -> {
            try {
                ensureDataDir();
                parque.generarReporteTxt("data");
                JOptionPane.showMessageDialog(this, "Reporte generado en data/reporte.txt");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        miSalir.addActionListener(e ->
                dispatchEvent(new java.awt.event.WindowEvent(this, java.awt.event.WindowEvent.WINDOW_CLOSING))
        );

        archivo.add(miGuardar);
        archivo.add(miReporte);
        archivo.addSeparator();
        archivo.add(miSalir);
        mb.add(archivo);

        return mb;
    }

    // Mostrar subventana y refrescar lblInfo al cerrarla
    private void mostrarYRefrescar(JFrame frame) {
        frame.setLocationRelativeTo(this);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosed(java.awt.event.WindowEvent e) {
                actualizarInfoSistema(lblInfo);
            }
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                actualizarInfoSistema(lblInfo);
            }
        });
        frame.setVisible(true);
    }

    // Asegurar carpeta data/ antes de escribir
    private void ensureDataDir() throws java.io.IOException {
        java.nio.file.Files.createDirectories(java.nio.file.Paths.get("data"));
    }


    private void abrirMenuClientes() {
        MenuClientesGui menuClientes = new MenuClientesGui(parque);
        menuClientes.setVisible(true);
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

    // ===== Persistencia =====
    private void guardarDatos() {
        try {
            ensureDataDir(); // ← agregar
            parque.guardarEnCSV("data");
            JOptionPane.showMessageDialog(this,
                    "Datos guardados exitosamente en carpeta 'data'",
                    "Guardado exitoso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar datos: " + e.getMessage(),
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== Status =====
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

    //Clase para generar la imagen de fondo del Menu
    class BackgroundPanel extends JPanel {
        private final Image bg;

        BackgroundPanel() {
            // Carga desde el classpath (resources root)
            bg = new ImageIcon(getClass().getResource("/MenuCanvav2.jpg")).getImage();
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Dibuja la imagen escalada al tamaño del panel
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}