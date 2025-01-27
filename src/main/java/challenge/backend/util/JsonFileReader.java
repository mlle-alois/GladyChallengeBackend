package challenge.backend.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class JsonFileReader {
    private final ObjectMapper objectMapper;

    /**
     * Méthode générique pour lire un fichier JSON et retourner une liste d'objets.
     */
    public <T> Optional<List<T>> readJsonFile(String filePath, TypeReference<List<T>> typeReference) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new IOException("Le fichier n'existe pas à l'emplacement: " + filePath);
            }

            InputStream inputStream = file.toURI().toURL().openStream();
            List<T> entities = objectMapper.readValue(inputStream, typeReference);
            return Optional.of(entities);
        } catch (IOException exception) {
            log.error("Error reading JSON file: {}", filePath, exception);
            throw new RuntimeException(exception);
        }
    }
}

