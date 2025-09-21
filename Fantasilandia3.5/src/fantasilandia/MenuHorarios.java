package fantasilandia;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import Excepciones.HorarioMalFormateadoException;


public class MenuHorarios {

    public static void menu(Fantasilandia parque, BufferedReader br) throws IOException, HorarioMalFormateadoException {
        int op;
        do {
            System.out.println("\n--- GESTIONAR DIAS ACTIVOS ANUALES ---");
            System.out.println("1) Agregar Día Activo");
            System.out.println("2) Listar Días Activos");
            System.out.println("3) Modificar Día Activo");
            System.out.println("4) Eliminar Día Activo");
            System.out.println("5) Agregar Horario a Atracción");
            System.out.println("6) Listar Horarios de Atracción");
            System.out.println("0) Volver");
            System.out.print("Seleccione: ");

            op = leerInt(br);

            switch (op) {
                case 1: {
                    System.out.print("Fecha (YYYY-MM-DD): ");
                    String fecha = br.readLine().trim();
                    if (parque.agregarDiaActivoAnual(fecha))
                        System.out.println("✓ Día activo agregado.");
                    else
                        System.out.println("✗ Ese día ya existe.");
                    break;
                }
                case 2: {
                    Map<String, DiasActivosAnuales> dias = parque.getDiasActivosAnuales();
                    if (dias == null || dias.isEmpty()) {
                        System.out.println("No hay días activos cargados.");
                    } else {
                        System.out.println("\nDías activos:");
                        dias.values().stream()
                            .sorted((a, b) -> a.getFecha().compareTo(b.getFecha()))
                            .forEach(d -> System.out.println(" - " + d));
                    }
                    break;
                }
                case 3: {
                    System.out.print("Fecha actual (YYYY-MM-DD): ");
                    String fOld = br.readLine().trim();
                    System.out.print("Nueva fecha (YYYY-MM-DD): ");
                    String fNew = br.readLine().trim();
                    if (parque.modificarDiaActivoAnual(fOld, fNew))
                        System.out.println("✓ Día modificado.");
                    else
                        System.out.println("✗ No se pudo modificar (fecha inexistente o nueva fecha ya existe).");
                    break;
                }
                case 4: {
                    System.out.print("Fecha a eliminar (YYYY-MM-DD): ");
                    String fDel = br.readLine().trim();
                    if (parque.eliminarDiaActivoAnual(fDel))
                        System.out.println("✓ Día eliminado.");
                    else
                        System.out.println("✗ No existe ese día activo.");
                    break;
                }
                case 5: {
                    System.out.print("Código de atracción: ");
                    String cod = br.readLine().trim();
                    System.out.print("Hora inicio (HH:mm): ");
                    String hi = br.readLine().trim();
                    System.out.print("Hora fin (HH:mm): ");
                    String hf = br.readLine().trim();
                    if (parque.agregarHorarioAAtraccion(cod, hi, hf))
                        System.out.println("✓ Horario agregado a la atracción.");
                    else
                        System.out.println("✗ No existe la atracción.");
                    break;
                }
                case 6: {
                    System.out.print("Código de atracción: ");
                    String cod2 = br.readLine().trim();
                    parque.listarHorariosDeAtraccion(cod2);
                    break;
                }
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida.");
            }

        } while (op != 0);
    }

    private static int leerInt(BufferedReader br) throws IOException {
        try {
            String s = br.readLine();
            if (s == null) return -1;
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    

}
