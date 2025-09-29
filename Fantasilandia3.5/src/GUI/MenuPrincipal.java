package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Excepciones.BloqueMalFormateadoException;
import Excepciones.FechaMalFormateadaException;
import Excepciones.HorarioMalFormateadoException;
import Excepciones.RutMalFormateadoException;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;

import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.*;
import java.awt.*;

// Modelo
import fantasilandia.*;
// Persistencia
import persistencia.PersistenceBridge;

import java.awt.EventQueue;

//=======SIA 2.3 VENTANA SWING PARA MENU PRINCIPAL=======
public class MenuPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Fantasilandia parque;
    private JLabel lblInfo;
    private Path dataDir;


    //======SIA 2.9. Utilizacion de Excepciones==========
    public MenuPrincipal() throws RutMalFormateadoException, FechaMalFormateadaException,
            HorarioMalFormateadoException, BloqueMalFormateadoException {

        dataDir = PersistenceBridge.getDataDir();
        parque = new Fantasilandia(true);

        try {
            boolean loaded = PersistenceBridge.loadFromCsv(parque);
            if (!loaded) {
                System.out.println("[INFO] No se cargaron CSV. Se mantienen datos de ejemplo.");
            }
        } catch (Exception ex) {
            System.err.println("[WARN] Error cargando CSV: " + ex.getMessage());
        }

        initComponents();
    }

    //Constructor
    public MenuPrincipal(Fantasilandia parqueExistente) {
        this.parque = parqueExistente;
        this.dataDir = PersistenceBridge.getDataDir();
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        contentPane = new BackgroundPanel(); // ← mantiene el fondo
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        setTitle("Sistema de Administración de parque temático");
        setResizable(false);
        setLocationRelativeTo(null);

        setJMenuBar(buildMenuBar());

        JLabel Titulo = new JLabel("Sistema Administrativo de Fantasilandia");
        Titulo.setHorizontalAlignment(SwingConstants.CENTER);
        Titulo.setBounds(55, 20, 780, 60);
        Titulo.setFont(new Font("Comic Sans MS", Font.PLAIN, 28));
        contentPane.add(Titulo);

        // ===== Botones principales =====
        JButton IngresoClientes = new JButton("Gestión de Clientes");
        IngresoClientes.setFont(new Font("Tw Cen MT", Font.PLAIN, 23));
        IngresoClientes.addActionListener(e -> abrirMenuClientes());
        IngresoClientes.setBounds(15, 117, 355, 70);
        contentPane.add(IngresoClientes);

        JButton IngresoAtracciones = new JButton("Gestión de Atracciones");
        IngresoAtracciones.setFont(new Font("Tw Cen MT", Font.PLAIN, 23));
        IngresoAtracciones.addActionListener(e -> abrirMenuAtracciones());
        IngresoAtracciones.setBounds(15, 198, 355, 70);
        contentPane.add(IngresoAtracciones);

        /*======Menu bloques primitivo, insertar en caso de necesitar constancia de todos los bloques en un solo menu=======
        JButton IngresoBloques = new JButton("Gestión de Bloques ");
        IngresoBloques.setFont(new Font("Tw Cen MT", Font.PLAIN, 23));
        IngresoBloques.addActionListener(e -> abrirMenuBloques());
        IngresoBloques.setBounds(15, 279, 355, 70);
        contentPane.add(IngresoBloques);
        */

        JButton IngresoDias = new JButton("Gestión de Días");
        IngresoDias.setFont(new Font("Tw Cen MT", Font.PLAIN, 23));
        IngresoDias.addActionListener(e -> abrirMenuDias());
        IngresoDias.setBounds(15, 279, 355, 70); // ajusta y coordina con tu layout
        contentPane.add(IngresoDias);

        // Botón para abrir el menú de reportes
        JButton btnGestinDeReportes = new JButton("Gestión de Reportes/Estadisticas");
        btnGestinDeReportes.setFont(new Font("Tw Cen MT", Font.PLAIN, 23));
        btnGestinDeReportes.setBounds(15, 360, 355, 70);  // Ajusta la posición según tu preferencia
        btnGestinDeReportes.addActionListener(e -> abrirMenuReportes());
        contentPane.add(btnGestinDeReportes);

        // Botón Guardar
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
                parque.generarReporteTxt(dataDir.toString());
                JOptionPane.showMessageDialog(this,
                    "Reporte generado con éxito en: " + dataDir.resolve("reporte_parque.txt"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage());
            }
        });
        contentPane.add(btnReporte);

        lblInfo = new JLabel();
        actualizarInfoSistema(lblInfo);
        lblInfo.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblInfo.setBounds(10, 468, 500, 35);
        contentPane.add(lblInfo);

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
    //Opciones inferiores
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
                parque.generarReporteTxt(dataDir.toString());
                JOptionPane.showMessageDialog(this,
                    "Reporte generado en: " + dataDir.resolve("reporte_parque.txt"));
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

    //=== SIA 2.2 UTILIZACION DE PERSISTENCIA DE DATOS====
    private void ensureDataDir() {
        try {
            if (dataDir == null) dataDir = PersistenceBridge.getDataDir();
            if (dataDir != null && !Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "No se pudo crear carpeta 'data': " + e.getMessage(),
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Refrescar el estado al volver de subventanas
    private void abrirMenuClientes() {
        var w = new MenuClientesGui(parque, this); // pasa el owner
        w.setSize(800, 600);                       // tamaño que diseñaste para Clientes
        w.setLocationRelativeTo(this);             // centrado relativo
        setVisible(false);                         // oculta el principal
        w.setVisible(true);
    }

    private void abrirMenuAtracciones() {
        var w = new MenuAtraccionesGui(parque, this);
        w.setLocation(getLocation());
        w.setResizable(isResizable());
        setVisible(false);
        w.setVisible(true);
    }

    private void abrirMenuReportes() {
        MenuReportes menuReportes = new MenuReportes(parque);
        menuReportes.setVisible(true);
        dispose();  // Cierra el MenuPrincipal
    }

     // === Apertura de Días (tolerante a nombre/clase)
     // === SIA 2.8. UTILIZACION DE TRY CATCH PARA ERRORES: SI NO PUEDE ACCEDER A LA INFORMACION MANDA ERROR.
     private void abrirMenuDias() {
         // Intentar GUI.MenuDiasGui con (Fantasilandia, JFrame)
         try {
             Class<?> k = Class.forName("GUI.MenuDiasGui");
             try {
                 var ctor = k.getConstructor(fantasilandia.Fantasilandia.class, javax.swing.JFrame.class);
                 java.awt.Window w = (java.awt.Window) ctor.newInstance(parque, this);
                 setVisible(false);
                 w.setVisible(true);
                 return;
             } catch (NoSuchMethodException ignore) {
                 // ctor sin owner
                 var ctor = k.getConstructor(fantasilandia.Fantasilandia.class);
                 java.awt.Window w = (java.awt.Window) ctor.newInstance(parque);
                 setVisible(false);
                 // si no hay owner, MenuDiasGui debe tener botón Volver que haga new MenuPrincipal(...)
                 w.setVisible(true);
                 return;
             }
         } catch (Throwable ignore) {
             try {
                 Class<?> k2 = Class.forName("GUI.DiasGui");
                 try {
                     var ctor2 = k2.getConstructor(fantasilandia.Fantasilandia.class, javax.swing.JFrame.class);
                     java.awt.Window w2 = (java.awt.Window) ctor2.newInstance(parque, this);
                     setVisible(false);
                     w2.setVisible(true);
                     return;
                 } catch (NoSuchMethodException ignore2) {
                     var ctor2 = k2.getConstructor(fantasilandia.Fantasilandia.class);
                     java.awt.Window w2 = (java.awt.Window) ctor2.newInstance(parque);
                     setVisible(false);
                     w2.setVisible(true);
                     return;
                 }
             } catch (Throwable ignore3) { }
         }

         // 3) Fallback: abrir Bloques (sin owner)
         try {
             MenuBloquesGui menuBloques = new MenuBloquesGui(parque);
             setVisible(false);
             menuBloques.setVisible(true);
         } catch (Throwable t) {
             JOptionPane.showMessageDialog(this,
                     "No se pudo abrir el menú de Días ni el fallback de Bloques.\n" +
                             "Detalle: " + t.getClass().getSimpleName() + " - " + String.valueOf(t.getMessage()),
                     "Error", JOptionPane.ERROR_MESSAGE);
         }
     }
    private void guardarDatos() {
        try {
            ensureDataDir();
            boolean ok = PersistenceBridge.saveToCsv(parque);
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Datos guardados en: " + dataDir.toAbsolutePath(),
                        "Guardado exitoso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudieron guardar los datos (revisar consola).",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar datos: " + e.getMessage(),
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarInfoSistema(JLabel lblInfo) {
        String info = String.format("Clientes: %d | Atracciones: %d | Días activos: %d",
                parque.contarClientes(),
                parque.contarAtracciones(),
                parque.getDiasActivosAnuales().size());
        lblInfo.setText(info);
    }

 // ===== Panel con fondo (rutas originales restauradas) =====
    private static class BackgroundPanel extends JPanel {
        /**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private Image bg;

        BackgroundPanel() {
            setOpaque(false);

            // 1) rutas originales en el CLASSPATH
            java.net.URL imgURL = getClass().getResource("/resources/MenuCanvav2.jpg");
            if (imgURL == null) {
                // Compatibilidad: raíz del classpath
                imgURL = getClass().getResource("/MenuCanvav2.jpg");
            }

            if (imgURL != null) {
                bg = new ImageIcon(imgURL).getImage();
            } else {
                // 2) Tus fallbacks a DISCO (cuando ejecutas desde IDE sin empacar recursos)
                java.nio.file.Path[] fsCandidates = new java.nio.file.Path[] {
                    java.nio.file.Paths.get("src", "resources", "MenuCanvav2.jpg"),
                    java.nio.file.Paths.get("resources", "MenuCanvav2.jpg"),
                    java.nio.file.Paths.get("MenuCanvav2.jpg")
                };
                boolean loaded = false;
                for (java.nio.file.Path p : fsCandidates) {
                    if (java.nio.file.Files.exists(p)) {
                        bg = new ImageIcon(p.toString()).getImage();
                        loaded = true;
                        break;
                    }
                }

                if (!loaded) {
                    System.err.println("⚠ No se encontró 'MenuCanvav2.jpg' en classpath ni en src/resources/");
                    // 3) Fallback opcional a tu 'MenuDefault.jpg' (también según tu código original)
                    java.net.URL fall = getClass().getResource("/resources/MenuDefault.jpg");
                    if (fall == null) fall = getClass().getResource("/MenuDefault.jpg");
                    if (fall != null) {
                        bg = new ImageIcon(fall).getImage();
                    } else {
                        bg = null; // evita NPE en paintComponent
                    }
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bg != null) {
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

}
