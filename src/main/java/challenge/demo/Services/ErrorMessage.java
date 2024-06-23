package challenge.demo.Services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorMessage {
    private String handledBy;
    private String exception;
    private String oriStatus;
    private String status;
    private String uri;
    private String localizedMessage;
    private String[] messages;

    public String readErrorResponse(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorMessage errorMessage = objectMapper.readValue(response.toString(), ErrorMessage.class);
        return errorMessage.getLocalizedMessage();
    }
}
