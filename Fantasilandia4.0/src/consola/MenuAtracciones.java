package consola;

import java.io.BufferedReader;
import java.io.IOException;
import fantasilandia.*;


//==MENUS CONSOLA: ENTREGA SIA 1====
public class MenuAtracciones {

    public static void menu(Fantasilandia parque, BufferedReader br) throws IOException {
        int op;
        do {
            System.out.println("\n--- GESTIONAR ATRACCIONES ---");
            System.out.println("1) Agregar Atracción");
            System.out.println("2) Mostrar Atracciones");
            System.out.println("3) Editar Atracción");
            System.out.println("4) Eliminar Atracción");
            System.out.println("0) Volver");
            System.out.print("Seleccione: ");
            op = leerInt(br);

            switch (op) {
                case 1:  agregarAtraccionUI(parque, br); break;
                case 2:  parque.mostrarAtracciones(); break;
                case 3:  editarAtraccionUI(parque, br); break;
                case 4: eliminarAtraccionUI(parque, br); break;
                case 0: { break;}
                default:  System.out.println("Opción inválida."); break;
            }
        } while (op != 0);
    }

    private static void agregarAtraccionUI(Fantasilandia parque, BufferedReader br) throws IOException {
        System.out.print("Nombre atracción: ");
        String nombre = br.readLine().trim();

        String clasif = leerClasificacion(br); 
        System.out.print("Descripción (enter = vacío): ");
        String desc = br.readLine().trim();
        boolean activa = true;

        String codigo = generarCodigoAtraccion(parque, clasif);

        Atraccion a = new Atraccion(nombre, clasif, codigo, activa, desc);
        parque.addAtraccion(a);
        System.out.println("✓ Atracción agregada. Código = " + codigo);
    }

    private static void editarAtraccionUI(Fantasilandia parque, BufferedReader br) throws IOException {
        System.out.print("Ingrese código de atracción a editar: ");
        String codigo = br.readLine().trim().toUpperCase();
        Atraccion a = parque.buscarAtraccion(codigo);
        if (a == null) {
            System.out.println("✗ Atracción no encontrada.");
            return;
        }
        System.out.println("Atracción actual: " + a);

        System.out.print("Nuevo nombre (enter = conservar): ");
        String s = br.readLine();
        if (s != null && !s.trim().isEmpty()) a.setNombre(s.trim());

        System.out.print("Nueva clasificación (enter = conservar): ");
        s = br.readLine();
        if (s != null && !s.trim().isEmpty()) a.setClasificacion(s.trim());

        System.out.print("Cambiar estado? (s = activa / n = inactiva / enter = conservar): ");
        s = br.readLine();
        if (s != null && !s.trim().isEmpty()) {
            if (s.trim().equalsIgnoreCase("s")) a.setActiva(true);
            else if (s.trim().equalsIgnoreCase("n")) a.setActiva(false);
        }

        System.out.print("Nueva descripción (enter = conservar): ");
        s = br.readLine();
        if (s != null && !s.trim().isEmpty()) a.setDescripcion(s.trim());

        System.out.println("✓ Atracción actualizada.");
    }

    private static void eliminarAtraccionUI(Fantasilandia parque, BufferedReader br) throws IOException {
        System.out.print("Ingrese código de atracción a eliminar: ");
        String codigo = br.readLine().trim().toUpperCase();
        boolean ok = parque.eliminarAtraccion(codigo);
        System.out.println(ok ? "✓ Atracción eliminada." : "✗ Atracción no encontrada.");
    }

    // =========================
    // Helpers específicos
    // =========================

    private static String leerClasificacion(BufferedReader br) throws IOException {
        while (true) {
            System.out.println("Clasificación:");
            System.out.println("1) INFANTILES");
            System.out.println("2) ADRENALINA");
            System.out.println("3) ACUATICOS");
            System.out.println("4) FAMILIARES");
            System.out.print("Opción (1-4 o nombre): ");

            String opcion = br.readLine().trim().toUpperCase();

            switch (opcion) {
                case "1": return "INFANTILES";
                case "2": return "ADRENALINA";
                case "3": return "ACUATICOS";
                case "4": return "FAMILIARES";
                case "INFANTILES":
                case "ADRENALINA":
                case "ACUATICOS":
                case "FAMILIARES":
                    return opcion;
                default:
                    System.out.println("✗ Opción inválida. Intente nuevamente.");
            }
        }
    }

    private static String prefijoDe(String clasificacion) {
        if (clasificacion == null) return "XX";
        String c = clasificacion.trim().toUpperCase().replaceAll("\\s+", " ");
        // support both the new categories and common legacy names
        if (c.equals("ADRENALINA") || c.contains("ADREN")) return "AD";
        if (c.equals("FAMILIARES") || c.equals("FAMILIAR") || c.equals("FAMILIAR?")) return "FA";
        if (c.equals("ZONA KIDS") || c.equals("ZONAKIDS") || c.equals("ZONA KID") || c.equals("KIDS") || c.equals("INFANTILES") || c.equals("INFANTIL")) return "ZK";
        if (c.equals("JUEGOS DE DESTREZA") || c.equals("JUEGOS DEDESTREZA") || c.contains("DESTREZA") || c.contains("JUEGOS")) return "JD";
        if (c.equals("ACUATICOS") || c.equals("ACUÁTICOS")) return "AC";
        String noSpace = c.replaceAll("\\s+","");
        return noSpace.length() >= 2 ? noSpace.substring(0,2) : (noSpace.length()==1?noSpace+"X":"XX");
    }


    private static String generarCodigoAtraccion(Fantasilandia parque, String clasificacion) {
        String pref = prefijoDe(clasificacion);
        int i = 1;
        String candidate;
        do {
            candidate = pref + formatearNumero(String.valueOf(i));
            i++;
        } while (parque.buscarAtraccion(candidate) != null);
        return candidate;
    }

    // =========================
    // Helpers reutilizados
    // =========================

    private static int leerInt(BufferedReader br) throws IOException {
        try {
            String s = br.readLine();
            if (s == null) return -1;
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String formatearNumero(String num) {
        int n = Integer.parseInt(num);
        return String.format("%03d", n);
    }
}
