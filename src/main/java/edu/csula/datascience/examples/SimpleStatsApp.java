package edu.csula.datascience.examples;

import com.google.common.collect.Lists;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

/**
 * Example of using Http request to get data
 */
public class SimpleStatsApp {
    public static void main(String[] args) {
        List<Double> data = Lists.newArrayList();
        try {
            JsonNode response = Unirest.get("http://104.154.21.59:9000/api/random")
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .asJson()
                .getBody();

            response.getArray()
                .forEach(item -> {
                    data.add(Double.valueOf(item.toString()));
                });

            SimpleStats stats = new SimpleStats(data);
            System.out.println(stats.median());

        } catch (UnirestException e) {
            throw new IllegalStateException("Server may not be up and running.", e);
        }
    }
}
