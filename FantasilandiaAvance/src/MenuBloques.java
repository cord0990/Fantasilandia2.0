
import java.io.BufferedReader;
import java.io.IOException;

public final class MenuBloques {

    // Metodo estático: lo llamamos desde Main sin instanciar esta clase
    public static void gestionar(Fantasilandia parque, BufferedReader br) throws IOException {
        System.out.println("\n=== Gestión de BLOQUES de Horario (2ª colección) ===");
        System.out.println("1) Insertar cliente en bloque de Horario");
        System.out.println("2) Listar clientes de un bloque de Horario");
        System.out.print("Ingrese una Opción: ");
        int opB = Integer.parseInt(br.readLine().trim());

        System.out.print("Fecha (YYYY-MM-DD): ");
        String fechaB = br.readLine().trim();

        System.out.print("Código de bloque (ej: B001): ");
        String codBloque = br.readLine().trim();

        switch (opB) {
            case 1:
                // Insertar cliente en bloque
                System.out.println("¿El bloque ya existe?");
                System.out.println("1) Sí");
                System.out.println("2) No");
                System.out.print("Elija una Opción: ");
                int existeBloque = Integer.parseInt(br.readLine().trim());

                if (existeBloque == 2) {
                    // Crear bloque
                    System.out.print("Código de la atracción (ej: A001): ");
                    String codAtr = br.readLine().trim();
                    Atraccion atrSel = parque.buscarAtraccion(codAtr);
                    if (atrSel == null) {
                        System.out.println("✗ Atracción no encontrada.");
                        return; // salimos del metodo
                    }

                    System.out.print("Hora inicio (HH:mm): ");
                    String hi = br.readLine().trim();
                    System.out.print("Hora fin (HH:mm): ");
                    String hf = br.readLine().trim();

                    parque.getOCrearBloque(fechaB, codBloque, atrSel, new HorariosAtraccion(hi, hf));
                    System.out.println("✓ Bloque creado.");
                }

                // Inscribir cliente en el bloque
                System.out.print("RUT del cliente a agregar: ");
                String rutAdd = br.readLine().trim();
                boolean ok = parque.agregarClienteABloquePorRut(fechaB, codBloque, rutAdd);
                if (ok) System.out.println("✓ Cliente agregado al bloque.");
                else    System.out.println("✗ No se pudo agregar (RUT o bloque inexistente).");
                break;

            case 2:
                // Listar clientes del bloque
                parque.listarClientesDeBloque(fechaB, codBloque);
                break;

            default:
                System.out.println("Opción inválida.");
        }
    }

    // Evita instanciar por accidente (opcional)
    private MenuBloques() {}
}

