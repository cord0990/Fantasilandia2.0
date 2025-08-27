import java.io.BufferedReader;
import java.io.IOException;

public final class MenuHorarios {

    private MenuHorarios() {} // evitar instanciación

    public static void gestionar(Fantasilandia parque, BufferedReader br) throws IOException {
        System.out.println("\n=== Gestión de HORARIOS estándar por ATRACCIÓN (SIA1.8) ===");
        System.out.print("Ingrese código de atracción: ");
        String codigo = br.readLine().trim().toUpperCase();

        Atraccion atr = parque.buscarAtraccion(codigo);
        if (atr == null) {
            System.out.println("✗ No existe atracción con código: " + codigo);
            return;
        }

        System.out.println("\nAtracción encontrada: " + atr.getNombre() + " (" + atr.getCodigoAtraccion() + ")");
        System.out.println("1) Agregar horario (inserción manual)");
        System.out.println("2) Mostrar horarios (listado)");
        System.out.print("Opción: ");

        int op;
        try {
            op = Integer.parseInt(br.readLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("✗ Opción inválida.");
            return;
        }

        switch (op) {
            case 1:
                agregarHorario(parque, codigo, br);
                break;
            case 2:
                parque.listarHorariosDeAtraccion(codigo);
                break;
            default:
                System.out.println("✗ Opción inválida.");
        }
    }

    private static void agregarHorario(Fantasilandia parque, String codigo, BufferedReader br) throws IOException {
        System.out.print("Hora inicio (HH:mm): ");
        String ini = br.readLine().trim();
        System.out.print("Hora fin (HH:mm): ");
        String fin = br.readLine().trim();

        boolean ok = parque.agregarHorarioAAtraccion(codigo, ini, fin);
        if (ok) {
            System.out.println("✓ Horario agregado correctamente.");
        }
    }
}
