package smartcity.util;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * Utilitaire JSON
 * ---------------
 * Convertisseur de données Object[] vers format JSON.
 * Utilisé pour l'export et la sérialisation des statistiques.
 *
 * Gestion des types primitifs et chaînes
 */
public class JsonUtil {
    public static String toJsonArray(List<Object[]> data, int index) {
        if (data == null || data.isEmpty()) {
            return "[]";
        }
        return data.stream()
                  .map(row -> row[index] == null ? "null" : 
                       row[index] instanceof String ? "\"" + row[index].toString().replace("\"", "\\\"") + "\"" : 
                       row[index].toString())
                  .collect(Collectors.joining(",", "[", "]"));
    }
}