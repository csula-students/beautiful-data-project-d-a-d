package edu.csula.datascience.examples;

import com.google.gson.Gson;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import com.mongodb.MongoClient;
import org.bson.Document;
import com.mongodb.DBCursor;
import com.mongodb.DBCollection;
import com.mongodb.DB;
import com.mongodb.BasicDBObject;


import java.text.ParseException;

import java.util.Date;
import java.text.SimpleDateFormat;


import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * A quick elastic search example app
 *
 * It will parse the csv file from the resource folder under main and send these
 * data to elastic search instance running locally
 *
 * After that we will be using elastic search to do full text search
 *
 * gradle command to run this app `gradle twitter`
 */
public class ElasticSearchTwitterApp {
    private final static String indexName = "full-bd-tweets";
    private final static String typeName = "tweets";
    public static MongoClient mongoClient;


    public static void main(String[] args) {
        Node node = nodeBuilder().settings(Settings.builder()
            .put("cluster.name", "elasticsearch_adamsornoso")
            .put("path.home", "elasticsearch-data")).node();
        Client client = node.client();

        /**
         *
         *
         * INSERT data to elastic search
         */


         // establish database connection to MongoDB
        mongoClient = new MongoClient();
        DB db = mongoClient.getDB( "twitter_db" );
        DBCollection collection = db.getCollection("tweets");



        // create bulk processor
        BulkProcessor bulkProcessor = BulkProcessor.builder(
            client,
            new BulkProcessor.Listener() {
                @Override
                public void beforeBulk(long executionId,
                                       BulkRequest request) {
                }

                @Override
                public void afterBulk(long executionId,
                                      BulkRequest request,
                                      BulkResponse response) {
                }

                @Override
                public void afterBulk(long executionId,
                                      BulkRequest request,
                                      Throwable failure) {
                    System.out.println("Facing error while importing data to elastic search");
                    failure.printStackTrace();
                }
            })
            .setBulkActions(10000)
            .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
            .setFlushInterval(TimeValue.timeValueSeconds(5))
            .setConcurrentRequests(1)
            .setBackoffPolicy(
                BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
            .build();

        // Gson library for sending json to elastic search
        Gson gson = new Gson();



            DBCursor cursor = collection.find();
            //DBCursor cursor = collection.find().limit(10000);
            try {
                while(cursor.hasNext()) {

                    BasicDBObject obj = (BasicDBObject) cursor.next();
                    //System.out.println(obj.getString("text"));

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

                    Tweet temp = new Tweet(d, obj.getString("text"),  obj.getInt("times_favorited"), obj.getInt("times_retweeted"));
                    
                    bulkProcessor.add(new IndexRequest(indexName, typeName)
                        .source(gson.toJson(temp))
                    );



               }
            } finally {
               cursor.close();
            }



        /**
         * AGGREGATION
         */
        SearchResponse sr = node.client().prepareSearch(indexName)
            .setTypes(typeName)
            .setQuery(QueryBuilders.matchAllQuery())
            .addAggregation(
                AggregationBuilders.terms("stateAgg").field("state")
                    .size(Integer.MAX_VALUE)
            )
            .execute().actionGet();

        // Get your facet results
        Terms agg1 = sr.getAggregations().get("stateAgg");

        for (Terms.Bucket bucket: agg1.getBuckets()) {
            System.out.println(bucket.getKey() + ": " + bucket.getDocCount());
        }
    }


    static class Tweet {
        final String date;
        final String text;
        final int times_favorited;
        final int times_retweeted;


        public Tweet(String date, String text, int times_favorited, int times_retweeted) {
            this.date = date;
            this.text = text;
            this.times_retweeted = times_retweeted;
            this.times_favorited = times_favorited;
        }
    }
}
