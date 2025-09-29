package persistencia;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;

//==SIA 2.2. PERSISTENCIA DE DATA Y USO DE BATCH.==
/** Utilidad para resolver la carpeta de datos (CSV) de forma robusta. */
public final class DataPaths {
    private DataPaths(){}

    private static final String DATA_DIR_NAME = "data";

    /**
     * Retorna la carpeta "data" a utilizar.
     * - Respeta el override por propiedad del sistema (-Dfantasilandia.dataDir=/ruta).
     * - Si no existe, la busca ascendiendo desde el CWD y el directorio del .jar/clases.
     * - Si aún no existe, la crea en el CWD.
     */
    public static Path findDataDir() {
        // 1) Propiedad del sistema o variable de entorno
        String override = System.getProperty("fantasilandia.dataDir");
        if (override == null || override.isBlank()) {
            override = System.getenv("FANTASILANDIA_DATA_DIR");
        }
        if (override != null && !override.isBlank()) {
            Path p = Paths.get(override).toAbsolutePath().normalize();
            if (Files.isDirectory(p)) return p;
        }

        // 2) Buscar ascendiendo desde CWD
        Path cwd = Paths.get("").toAbsolutePath().normalize();
        Path hit = searchUpwardsForData(cwd);
        if (hit != null) return hit;

        // 3) Buscar ascendiendo desde el directorio del código/jar
        try {
            Path here = Paths.get(DataPaths.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
            Path base = Files.isDirectory(here) ? here : here.getParent();
            hit = searchUpwardsForData(base);
            if (hit != null) return hit;
        } catch (URISyntaxException ignore) {}

        // 4) Fallback: crear ./data en CWD
        Path fallback = cwd.resolve(DATA_DIR_NAME);
        try { Files.createDirectories(fallback); } catch (IOException ignore) {}
        return fallback;
    }

    /** Resuelve un archivo dentro de la carpeta de datos. */
    public static Path resolve(String fileName) {
        return findDataDir().resolve(fileName);
    }

    // ===== internos =====
    private static Path searchUpwardsForData(Path start) {
        Path p = start;
        for (int i=0; i<6 && p != null; i++) {
            Path candidate = p.resolve(DATA_DIR_NAME);
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
            p = p.getParent();
        }
        // Búsqueda superficial (hasta 3 niveles) por si el proyecto está anidado
        try {
            try (var walk = Files.walk(start, 3)) {
                return walk
                        .filter(Files::isDirectory)
                        .filter(dir -> dir.getFileName() != null
                                && DATA_DIR_NAME.equalsIgnoreCase(dir.getFileName().toString()))
                        .findFirst()
                        .orElse(null);
            }
        } catch (IOException ignore) {}
        return null;
    }
}
