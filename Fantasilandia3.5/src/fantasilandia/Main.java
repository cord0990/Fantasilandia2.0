package fantasilandia;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import Excepciones.BloqueMalFormateadoException;
import Excepciones.FechaMalFormateadaException;
import Excepciones.HorarioMalFormateadoException;
import Excepciones.RutMalFormateadoException;

import GUI.MenuPrincipal;
import persistencia.PersistenceBridge;

public final class Main {
    private Main(){}

    public static void main(String[] args) throws FechaMalFormateadaException, HorarioMalFormateadoException, RutMalFormateadoException, BloqueMalFormateadoException {
        // === 0) Determinar modo UI (por defecto: GUI) === 
		boolean uiModoGrafico = true; // TRUE → Ventana, FALSE → Consola

		//Metodo de seguridad
		String prop = System.getProperty("fantasilandia.gui");
		if (prop != null) {
		    uiModoGrafico = Boolean.parseBoolean(prop.trim());
		}


		// === 1) Instancia de dominio ===
		// true: mantiene tu comportamiento de poblar datos de ejemplo si no hay CSV
		Fantasilandia parque = new Fantasilandia(true);

		// === 2) Intentar cargar CSV con ruta dinámica (no falla si no hay CSV) === SIA 2.2 PERSISTENCIA DE DATOS
		boolean loaded = PersistenceBridge.loadFromCsv(parque);
		System.out.println("[INFO] CSV cargados: " + loaded);

		// === 3) Despachar según modo ===
		if (uiModoGrafico) {
		    lanzarGUI(parque);
		} else {
		    lanzarConsola(parque);
		}
    }

    // ===== GUI ===== SIA 2.3 ACCESO A LA VENTANA SWING
    private static void lanzarGUI(Fantasilandia parque) {
        EventQueue.invokeLater(() -> {
            try {
                MenuPrincipal frame = new MenuPrincipal(parque);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // ===== Consola mínima (sin dependencias extra) =====
    private static void lanzarConsola(Fantasilandia parque) {
        System.out.println("=== Fantasilandia (Modo Consola) ===");
        System.out.println("Carpeta CSV: " + persistencia.PersistenceBridge.getDataDir().toAbsolutePath());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println();
                System.out.println("1) Ver resumen (clientes/atracciones/días)");
                System.out.println("2) Guardar CSV");
                System.out.println("3) Generar reporte TXT");
                System.out.println("4) Recargar CSV");
                System.out.println("0) Salir");
                System.out.print("> ");

                String op = br.readLine();
                if (op == null) break;
                switch (op.trim()) {
                    case "1":
                        System.out.printf("Clientes: %d | Atracciones: %d | Días activos: %d%n",
                                parque.contarClientes(),
                                parque.contarAtracciones(),
                                parque.getDiasActivosAnuales().size());
                        break;
                    case "2":
                        if (persistencia.PersistenceBridge.saveToCsv(parque)) {
                            System.out.println("✔ Guardado correcto.");
                        } else {
                            System.out.println("✖ No se pudo guardar. Ver consola de errores.");
                        }
                        break;
                    case "3":
                        try {
                            String dir = persistencia.PersistenceBridge.getDataDir().toString();
                            parque.generarReporteTxt(dir);
                            System.out.println("✔ Reporte generado en la carpeta data.");
                        } catch (Exception ex) {
                            System.out.println("✖ Error generando reporte: " + ex.getMessage());
                        }
                        break;
                    case "4":
                        boolean ok = persistencia.PersistenceBridge.loadFromCsv(parque);
                        System.out.println(ok ? "✔ CSV recargados." : "✖ No se pudieron cargar CSV.");
                        break;
                    case "0":
                        System.out.println("Adiós.");
                        return;
                    default:
                        System.out.println("Opción inválida.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error en consola: " + e.getMessage());
        }
    }
}
