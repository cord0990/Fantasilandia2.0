package fantasilandia;

//codigo Principal (MAIN)
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Excepciones.BloqueMalFormateadoException;
import Excepciones.FechaMalFormateadaException;
import Excepciones.HorarioMalFormateadoException;
import Excepciones.RutMalFormateadoException;
//import javax.swing.SwingUtilities;
import GUI.MenuPrincipal;
//import javax.swing.JOptionPane;

/**
 * Main (versión consola + opción para abrir GUI).
 * - Mantiene las funcionalidades de consola (MenuClientes, MenuAtracciones, MenuBloques, MenuHorarios).
 * - Opción 5 abre la interfaz Swing (MenuPrincipalFrame) sobre la misma instancia de Fantasilandia.
 * - Al salir (0) guarda en "data" mediante el método guardarEnCSV.
 *
 */
public class Main {

    public static void main(String[] args) throws RutMalFormateadoException, FechaMalFormateadaException, HorarioMalFormateadoException, BloqueMalFormateadoException {
        final Fantasilandia parque = new Fantasilandia();

        // Intentar cargar datos (si existe carpeta/data)
        try {
            parque.cargarDesdeCSV("data");
        } catch (Exception e) {
            // no interrumpe el programa si falla la carga
            System.err.println("Aviso: no se pudieron cargar datos (ok si es la primera ejecución): " + e.getMessage());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean continuar = true;

        while (continuar) {
            try {
                System.out.println("\n=== MENÚ PRINCIPAL (Consola) ===");
                System.out.println("1) Gestionar Clientes (consola)");
                System.out.println("2) Gestionar Atracciones (consola)");
                System.out.println("3) Gestionar Bloques (consola)");
                System.out.println("4) Gestionar Horarios (consola)");
                System.out.println("5) Generar Reporte (consola)");
                System.out.println("6) Abrir Menú gráfico (Swing)"); // <--- aquí está la opción que pediste
                System.out.println("0) Guardar y Salir");
                System.out.print("Seleccione: ");

                String linea = br.readLine();
                if (linea == null) { // EOF
                    System.out.println("Entrada cerrada. Saliendo.");
                    break;
                }
                int opcion;
                try {
                    opcion = Integer.parseInt(linea.trim());
                } catch (NumberFormatException nfe) {
                    System.out.println("Por favor ingrese un número válido.");
                    continue;
                }

                switch (opcion) {
                    case 1:
                        // Llama al menú de clientes por consola (sin modificar su código)
                        MenuClientes.menu(parque, br);
                        break;
                    case 2:
                        MenuAtracciones.menu(parque, br);
                        break;
                    case 3:
                        // Observa que MenuBloques usa 'gestionar' como método público
                        MenuBloques.gestionar(parque, br);
                        break;
                    case 4:
                        MenuHorarios.menu(parque, br);
                        break;
                    case 5:
                        try {
                            parque.generarReporteTxt("data");
                            System.out.println("Reporte generado correctamente en /data/reporte.txt");
                        } catch (Exception e) {
                            System.out.println("Error al generar el reporte: " + e.getMessage());
                        }
                        break;

                    case 6:
                    	MenuGui();
                        break;
                    case 0:
                        System.out.println("Guardando datos...");
                        try {
                            parque.guardarEnCSV("data");
                            System.out.println("Datos guardados en carpeta 'data'.");
                        } catch (Exception e) {
                            System.err.println("Error al guardar datos: " + e.getMessage());
                        }
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }

            } catch (IOException ioe) {
                System.err.println("Error de E/S: " + ioe.getMessage());
                break;
            }
        }

        System.out.println("Programa finalizado.");
    }

	private static void MenuGui() throws RutMalFormateadoException, FechaMalFormateadaException, HorarioMalFormateadoException, BloqueMalFormateadoException {
		MenuPrincipal menuPrincipal =new MenuPrincipal();
		menuPrincipal.setVisible(true);
	}
}
