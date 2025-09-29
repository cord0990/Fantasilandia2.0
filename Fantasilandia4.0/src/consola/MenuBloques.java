package consola;
import fantasilandia.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import Excepciones.BloqueMalFormateadoException;
import Excepciones.FechaMalFormateadaException;
import Excepciones.HorarioMalFormateadoException;
import fantasilandia.Fantasilandia;

//==MENUS CONSOLA: ENTREGA SIA 1====
public final class MenuBloques {

    public static void gestionar(Fantasilandia parque, BufferedReader br) throws IOException, HorarioMalFormateadoException, BloqueMalFormateadoException, FechaMalFormateadaException {
        int op;
        do {
            System.out.println("\n=== Gestión de BLOQUES de Horario ===");
            System.out.println("1) Agregar Bloque");
            System.out.println("2) Listar Bloques por Fecha");
            System.out.println("3) Modificar Bloque");
            System.out.println("4) Eliminar Bloque");
            System.out.println("5) Insertar cliente en bloque de Horario");
            System.out.println("6) Listar clientes de un bloque de Horario");
            System.out.println("0) Volver");
            System.out.print("Ingrese una Opción: ");

            op = leerInt(br);

            switch (op) {
                case 1: {
                    System.out.print("Fecha (YYYY-MM-DD): ");
                    String fechaB = br.readLine().trim();
                    System.out.print("Código de Bloque (ej: B001): ");
                    String codBloque = br.readLine().trim();
                    System.out.print("Código de Atracción: ");
                    String codAtr = br.readLine().trim();

                    Atraccion atrSel = parque.buscarAtraccion(codAtr);
                    if (atrSel == null) {
                        System.out.println("✗ Atracción no encontrada.");
                        break;
                    }

                    System.out.print("Hora inicio (HH:mm): ");
                    String hi = br.readLine().trim();
                    System.out.print("Hora fin (HH:mm): ");
                    String hf = br.readLine().trim();

                    parque.getOCrearBloque(fechaB, codBloque, atrSel, new HorariosAtraccion(hi, hf));
                    System.out.println("✓ Bloque creado/obtenido.");
                    break;
                }
                case 2: {
                    System.out.print("Fecha (YYYY-MM-DD): ");
                    String fechaB = br.readLine().trim();
                    DiasActivosAnuales dia = parque.buscarDiaActivoAnual(fechaB);
                    if (dia == null) {
                        System.out.println("No existe día activo " + fechaB);
                        break;
                    }
                    List<BloqueDeAtraccion> bloques = dia.getBloques();
                    if (bloques.isEmpty()) {
                        System.out.println("No hay bloques para esa fecha.");
                    } else {
                        System.out.println("\nBloques para " + fechaB + ":");
                        for (BloqueDeAtraccion b : bloques) System.out.println(" - " + b);
                    }
                    break;
                }
                case 3: {
                    System.out.print("Fecha del bloque (YYYY-MM-DD): ");
                    String fechaB = br.readLine().trim();
                    System.out.print("Código de Bloque: ");
                    String codBloque = br.readLine().trim();

                    DiasActivosAnuales dia = parque.buscarDiaActivoAnual(fechaB);
                    if (dia == null) {
                        System.out.println("No existe día activo " + fechaB);
                        break;
                    }

                    BloqueDeAtraccion b = dia.buscarBloque(codBloque);
                    if (b == null) {
                        System.out.println("No existe el bloque " + codBloque + " en " + fechaB);
                        break;
                    }

                    System.out.println("1) Cambiar código de bloque");
                    System.out.println("2) Cambiar atracción");
                    System.out.println("3) Cambiar horario");
                    System.out.println("4) Mover bloque a otra fecha");
                    System.out.println("0) Cancelar");
                    System.out.print("Opción: ");
                    int opt = leerInt(br);

                    switch (opt) {
                        case 1: {
                            System.out.print("Nuevo código de bloque: ");
                            String nuevo = br.readLine().trim();
                            b.setCodigoBloque(nuevo);
                            System.out.println("✓ Código actualizado.");
                            break;
                        }
                        case 2: {
                            System.out.print("Código de nueva atracción: ");
                            String codA = br.readLine().trim();
                            Atraccion a = parque.buscarAtraccion(codA);
                            if (a == null) {
                                System.out.println("✗ Atracción no encontrada.");
                            } else {
                                b.setAtraccion(a);
                                System.out.println("✓ Atracción actualizada.");
                            }
                            break;
                        }
                        case 3: {
                            System.out.print("Hora inicio (HH:mm): ");
                            String hi = br.readLine().trim();
                            System.out.print("Hora fin (HH:mm): ");
                            String hf = br.readLine().trim();
                            b.setHorario(new HorariosAtraccion(hi, hf));
                            System.out.println("✓ Horario actualizado.");
                            break;
                        }
                        case 4: {
                            System.out.print("Nueva fecha (YYYY-MM-DD): ");
                            String nuevaFecha = br.readLine().trim();
                            dia.getBloques().remove(b);
                            b.setFecha(nuevaFecha);
                            DiasActivosAnuales nuevoDia = parque.getODia(nuevaFecha);
                            nuevoDia.addBloque(b);
                            System.out.println("✓ Bloque movido a " + nuevaFecha);
                            break;
                        }
                        case 0:
                            break;
                        default:
                            System.out.println("Opción inválida.");
                    }
                    break;
                }
                case 4: {
                    System.out.print("Fecha del bloque (YYYY-MM-DD): ");
                    String fechaB = br.readLine().trim();
                    System.out.print("Código de Bloque: ");
                    String codBloque = br.readLine().trim();

                    DiasActivosAnuales dia = parque.buscarDiaActivoAnual(fechaB);
                    if (dia == null) {
                        System.out.println("No existe día activo " + fechaB);
                        break;
                    }

                    BloqueDeAtraccion b = dia.buscarBloque(codBloque);
                    if (b == null) {
                        System.out.println("No existe el bloque " + codBloque + " en " + fechaB);
                        break;
                    }

                    dia.getBloques().remove(b);
                    System.out.println("✓ Bloque eliminado.");
                    break;
                }
                case 5: {
                    System.out.print("Fecha (YYYY-MM-DD): ");
                    String fechaB = br.readLine().trim();
                    System.out.print("Código de Bloque: ");
                    String codBloque = br.readLine().trim();
                    System.out.print("RUT del cliente: ");
                    String rut = br.readLine().trim();

                    if (parque.agregarClienteABloquePorRut(fechaB, codBloque, rut))
                        System.out.println("✓ Cliente inscrito en bloque.");
                    else
                        System.out.println("✗ No se pudo inscribir (cliente o bloque inexistente).");
                    break;
                }
                case 6: {
                    System.out.print("Fecha (YYYY-MM-DD): ");
                    String fechaB = br.readLine().trim();
                    System.out.print("Código de Bloque: ");
                    String codBloque = br.readLine().trim();
                    parque.listarClientesDeBloque(fechaB, codBloque);
                    break;
                }
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida.");
            }

        } while (op != 0);
    }

    private MenuBloques() {}

    private static int leerInt(BufferedReader br) throws IOException {
        try {
            String s = br.readLine();
            if (s == null) return -1;
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
    
}
