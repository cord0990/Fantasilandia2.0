package persistencia;

import java.nio.file.Path;

//==SIA 2.2. PERSISTENCIA DE DATA Y USO DE BATCH.==
/** Compatibilidad: atajos para rutas CSV usando DataPaths. */
public final class CsvPaths {
    private CsvPaths(){}

    /** Carpeta base de datos (CSV). */
    public static Path dataDir() {
        return DataPaths.findDataDir();
    }

    /** Resuelve un archivo CSV dentro de la carpeta de datos. */
    public static Path resolveCsv(String fileName) {
        return DataPaths.resolve(fileName);
    }
}
