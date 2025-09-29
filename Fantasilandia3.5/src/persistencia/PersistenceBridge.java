package persistencia;

import fantasilandia.Fantasilandia;

import java.nio.file.Files;
import java.nio.file.Path;

//==SIA 2.2. PERSISTENCIA DE DATA Y USO DE BATCH.==
/** Capa de conveniencia para cargar/guardar CSV con rutas dinámicas. */
public final class PersistenceBridge {
    private PersistenceBridge(){}

    /** Directorio base de datos. Nunca retorna null (se crea si hace falta). */
    public static Path getDataDir() {
        Path dir = DataPaths.findDataDir();
        try {
            if (!Files.exists(dir)) Files.createDirectories(dir);
        } catch (Exception ignore) {}
        return dir;
    }

    /** Carga todos los CSV si existen; retorna true si al menos intentó cargar. */
    public static boolean loadFromCsv(Fantasilandia parque) {
        Path dir = getDataDir();
        try {
            if (Files.exists(dir)) {
                // Fantasilandia expone cargarDesdeCSV(String)
                parque.cargarDesdeCSV(dir.toString());
                return true;
            }
        } catch (Exception e) {
            System.err.println("[WARN] No se pudieron cargar CSV desde " + dir + ": " + e.getMessage());
        }
        return false;
    }

    /** Guarda todos los CSV; retorna true si no hubo excepciones. */
    public static boolean saveToCsv(Fantasilandia parque) {
        Path dir = getDataDir();
        try {
            parque.guardarEnCSV(dir.toString());
            return true;
        } catch (Exception e) {
            System.err.println("[WARN] No se pudieron guardar CSV en " + dir + ": " + e.getMessage());
            return false;
        }
    }
}
