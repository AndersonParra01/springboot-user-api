package pdf.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

    public static <T> String convertirObjetoToJson(T object) {
        try {
            // Crear un objeto ObjectMapper (Jackson)
            ObjectMapper objectMapper = new ObjectMapper();

            // Convertir Persona a JSON como una cadena
            String jsonString = objectMapper.writeValueAsString(object);

            return jsonString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
