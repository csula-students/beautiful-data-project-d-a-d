package edu.csula.datascience.acquisition;

import com.google.common.collect.Lists;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import twitter4j.Status;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An example of Collector implementation using Twitter4j with MongoDB Java driver
 */
public class TwitterCollector implements Collector<Status, Status> {
    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> collection;
    boolean  isFound = false;
    
    public TwitterCollector() {
        // establish database connection to MongoDB
        mongoClient = new MongoClient();

        // select `bd-example` as testing database
        database = mongoClient.getDatabase("twitter_db");

        // select collection by name `tweets`
        collection = database.getCollection("tweets");
    }
    @Override
    public Collection<Status> mungee(Collection<Status> src) {
    	
    	
    	List<Status> list = Lists.newArrayList();
    	
    	list = (List<Status>) src;
    	
    	src = Lists.newArrayList();
    	
   
    	for (int i = 0; i < list.size(); i++)
    	{
    		if (list.get(i).getGeoLocation() == null)
    		{
    			list.remove(i);
    			
    		}
    	}
        return list;
    }
    
    public Status mungee(Status src) // get rid of duplicates
    {
    	Status toReturn;
    	FindIterable<Document> iterable = collection.find(new Document("tweetId", src.getId()));
    	
    	iterable.forEach(new Block<Document>() {
    	    @Override
    	    public void apply(final Document document) {
    	        System.out.println("FOUND ALREADY");
    	        isFound = true;
    	    }
    	});
    	
    	if(isFound){
    		isFound = false;
    		return null;
    	}
    	
    	return src;
    	
    }

    //Saves in path
    @Override
    public void save(Collection<Status> data) {
        List<Document> documents = data.stream()
            .map(item -> new Document()
                .append("tweetId", item.getId())
                .append("username", item.getUser().getName())
                .append("text", item.getText())
                .append("times_favorited", item.getFavoriteCount())
                .append("location", item.getGeoLocation())
                .append("date", item.getCreatedAt().toString())
                .append("times_retweeted", item.getRetweetCount())
               .append("source", item.getSource()))
                
            .collect(Collectors.toList());

        collection.insertMany(documents);
    }

    public void save(Status tweet) {
        collection.insertOne(new Document()
            .append("tweetId", tweet.getId())
                    .append("username", tweet.getUser().getName())
                    .append("text", tweet.getText())
                    .append("times_favorited", tweet.getFavoriteCount())
                    .append("location", tweet.getGeoLocation())
                    .append("date", tweet.getCreatedAt().toString())
                    .append("times_retweeted", tweet.getRetweetCount())
                    .append("is_tetweeted", tweet.isRetweeted())
                    .append("is_sensitive", tweet.isPossiblySensitive())
                    .append("witheld_in_countries", tweet.getWithheldInCountries())
        );
    }
}
