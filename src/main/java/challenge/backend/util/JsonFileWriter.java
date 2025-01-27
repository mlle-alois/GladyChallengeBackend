package challenge.backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JsonFileWriter {

    private final ObjectMapper objectMapper;

    /**
     * Méthode pour écrire une liste d'objets dans un fichier JSON.
     * Cette méthode va convertir la liste d'objets en JSON et l'écrire dans un fichier spécifié.
     */
    public <T> void writeListToFile(String filePath, List<T> data) {
        try {
            File file = new File(filePath);

            objectMapper.writeValue(file, data);
        } catch (IOException exception) {
            log.error("Error writing JSON file: {}", filePath, exception);
            throw new RuntimeException(exception);
        }
    }
}
