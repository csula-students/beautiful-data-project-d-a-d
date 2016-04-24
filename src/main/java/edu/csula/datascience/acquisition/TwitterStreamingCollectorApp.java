package edu.csula.datascience.acquisition;

import twitter4j.Status;

import java.util.ArrayList;
import java.util.Collection;

/**
 * THIS CLASS USES TwitterStreamingSource.java and TwitterCollector to get collections of tweets from the streaming api
 */
public class TwitterStreamingCollectorApp {
    public static void main(String[] args) {
        TwitterStreamingSource source = new TwitterStreamingSource();
        TwitterStreamingCollector collector = new TwitterStreamingCollector();
        Collection<Status> cleanedTweets = new ArrayList<Status>();

     	Collection<Status> tweets = source.next();
     	int count = 0;
     	while (true){
     		if(!tweets.isEmpty()) //if filled, clean and save
     		{
     			cleanedTweets = collector.mungee(tweets);
                collector.save(cleanedTweets);
     			count += cleanedTweets.size();
     		}
     		
            tweets = source.next();
            System.out.println(cleanedTweets.size()+" added to "+count);
     		//Wait 2 seconds so that the collections can fill before we retrieve some tweets
     		try {
				Thread.sleep(2000);
				System.out.println("SLEEPING");
					
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
     		
        }  
    }
}