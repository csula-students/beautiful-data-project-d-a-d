package edu.csula.datascience.auth;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

/**
 * Simple authorization to gain access to course server
 */
public class Authorization {
    public String getToken(String key) {
        String token;

        JSONObject json = new JSONObject();
        json.put("key", key);

        try {
            JsonNode response = Unirest.post("http://104.154.21.59:9000/api/token")
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .body(json)
                .asJson()
                .getBody();

            token = response.getObject().getString("token");
        } catch (UnirestException e) {
            throw new IllegalStateException("Server may not be up and running.", e);
        }

        return token;
    }
}
