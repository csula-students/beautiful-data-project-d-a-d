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
public class TwitterStreamingCollector implements Collector<Status, Status> {
    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> collection;
    boolean  isFound = false;
    
    public TwitterStreamingCollector() {
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
        	if ((Long) list.get(i).getId() == null || list.get(i).getUser().getName() == null || list.get(i).getText() == null ||
        			list.get(i).getCreatedAt().toString() == null)
    		{
    			list.remove(i); // remove that tweet from the document
    			
    		}
        	
        	// checked if the tweet is good enough (has all fields)
        	
        	// (now check for duplication)
        	
//        	FindIterable<Document> iterable = collection.find(new Document("tweetId", list.get(i).getId()));
//        	
//        	iterable.forEach(new Block<Document>() {
//        	    @Override
//        	    public void apply(final Document document) {
//        	        System.out.println("FOUND ALREADY");
//        	        isFound = true;
//        	    }
//        	});
//        	
//        	if(isFound){
//        		isFound = false;
//        		list.remove(i);
//        	}
//        		
        	
    	}
        return list;
        
    }
    
  //Saves in path
    @Override
    public void save(Collection<Status> data) {
        List<Document> documents = data.stream()
            .map(tweet -> new Document()
            		 .append("tweetId", tweet.getId())
                     .append("username", tweet.getUser().getName())
                     .append("text", tweet.getText())
                     .append("times_favorited", tweet.getFavoriteCount())
                     .append("date", tweet.getCreatedAt().toString())
                     .append("times_retweeted", tweet.getRetweetCount())
                     .append("is_retweeted", tweet.isRetweeted())
                     .append("is_sensitive", tweet.isPossiblySensitive()))
                
            .collect(Collectors.toList());

        collection.insertMany(documents);
    }

}
