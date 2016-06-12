package edu.csula.datascience.examples;

import com.google.common.collect.Lists;
import io.searchbox.action.BulkableAction;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collection;




import com.mongodb.MongoClient;
import org.bson.Document;
import com.mongodb.DBCursor;
import com.mongodb.DBCollection;
import com.mongodb.DB;
import com.mongodb.BasicDBObject;


import java.text.ParseException;

import java.util.Date;
import java.text.SimpleDateFormat;



/**
 * A quick example app to send data to elastic search on AWS
 */
public class JestExampleApp {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String indexName = "bd-full-tweets";
        String typeName = "tweets";
        MongoClient mongoClient;

        // String indexName = "bd-data";
        // String typeName = "city-temperatures";
        String awsAddress = "https://search-bigdatafinal-7ybkxjgskx2sarveirqmn6g7gq.us-west-2.es.amazonaws.com/";
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
            .Builder(awsAddress)
            .multiThreaded(true)
            .build());
        JestClient client = factory.getObject();

       


         // establish database connection to MongoDB
        mongoClient = new MongoClient();
        DB db = mongoClient.getDB( "all_tweets" );
        DBCollection collection = db.getCollection("tweets");



        DBCursor cursor = collection.find();
        //DBCursor cursor = collection.find().limit(10000);
/*
        try {
            // after reading the csv file, we will use CSVParser to parse through
            // the csv files
            CSVParser parser = CSVParser.parse(
                csv,
                Charset.defaultCharset(),
                CSVFormat.EXCEL.withHeader()
            );
            Collection<Temperature> temperatures = Lists.newArrayList();

            int count = 0;

            // for each record, we will insert data into Elastic Search
//            parser.forEach(record -> {
            for (CSVRecord record: parser) {
                // cleaning up dirty data which doesn't have time or temperature
                if (
                    !record.get("dt").isEmpty() &&
                    !record.get("AverageTemperature").isEmpty()
                ) {
                    Temperature temp = new Temperature(
                        record.get("dt"),
                        Double.valueOf(record.get("AverageTemperature")),
                        record.get("State"),
                        record.get("Country")
                    );

                    if (count < 500) {
                        temperatures.add(temp);
                        count ++;
                    } else {
                        try {
                            Collection<BulkableAction> actions = Lists.newArrayList();
                            temperatures.stream()
                                .forEach(tmp -> {
                                    actions.add(new Index.Builder(tmp).build());
                                });
                            Bulk.Builder bulk = new Bulk.Builder()
                                .defaultIndex(indexName)
                                .defaultType(typeName)
                                .addAction(actions);
                            client.execute(bulk.build());
                            count = 0;
                            temperatures = Lists.newArrayList();
                            System.out.println("Inserted 500 documents to cloud");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            Collection<BulkableAction> actions = Lists.newArrayList();
            temperatures.stream()
                .forEach(tmp -> {
                    actions.add(new Index.Builder(tmp).build());
                });
            Bulk.Builder bulk = new Bulk.Builder()
                .defaultIndex(indexName)
                .defaultType(typeName)
                .addAction(actions);
            client.execute(bulk.build());
        } catch (IOException e) {
            e.printStackTrace();
        }

*/

           try {
                while(cursor.hasNext()) {

                    BasicDBObject obj = (BasicDBObject) cursor.next();
                    Collection<Tweet> tweets = Lists.newArrayList();

                    int count = 0;


//CONVERT DATE
                    String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
                    SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
                    sf.setLenient(true);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    try {
                       date = sf.parse(obj.getString("date"));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String d = format.format(date);


                    String tags = "";

                    if (obj.containsKey("hashtahgs"))
                    {
                        tags = obj.getString("hashtahgs");
                    }
                    else
                    {
                        tags = obj.getString("hashtags");
                    }


                    Tweet temp = new Tweet(d, obj.getString("text"),  obj.getInt("retweets"), tags);
                    




                    if (count < 500) {
                        tweets.add(temp);
                        count ++;
                    } else {
                        try {
                            Collection<BulkableAction> actions = Lists.newArrayList();
                            tweets.stream()
                                .forEach(tmp -> {
                                    actions.add(new Index.Builder(tmp).build());
                                });
                            Bulk.Builder bulk = new Bulk.Builder()
                                .defaultIndex(indexName)
                                .defaultType(typeName)
                                .addAction(actions);
                            client.execute(bulk.build());
                            count = 0;
                            tweets = Lists.newArrayList();
                            System.out.println("Inserted 500 documents to cloud");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }



                Collection<BulkableAction> actions = Lists.newArrayList();
                tweets.stream()
                    .forEach(tmp -> {
                        actions.add(new Index.Builder(tmp).build());
                    });
                Bulk.Builder bulk = new Bulk.Builder()
                    .defaultIndex(indexName)
                    .defaultType(typeName)
                    .addAction(actions);
                client.execute(bulk.build());



               }
            } finally {
               cursor.close();
            }





        System.out.println("We are done! Yay!");
    }

      static class Tweet {
        final String date;
        final String text;
        final int retweets;
        final String hashtags;


        public Tweet(String date, String text, int retweets, String hashtags) {
            this.date = date;
            this.text = text;
            this.retweets = retweets;
            this.hashtags = hashtags;
        }
    }
}
