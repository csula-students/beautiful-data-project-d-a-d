package edu.csula.datascience.acquisition;

import com.google.common.collect.Lists;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This Class uses Twitter4j to get tweets and implements StatusListener to fill a list with tweets and returns that data 
 */

public class TwitterStreamingSource2 implements Source<Status>, StatusListener {
    public List<Status> list = Lists.newArrayList();
    public TwitterStream twitterStream;
    public TwitterStreamingCollector twitterCollector;
    

    public TwitterStreamingSource2() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey("asKL6BfWWA1FdXuZWI5IuHhaf")
            .setOAuthConsumerSecret("ZOAnT1KZuIHQlr4ENSeC41JVJQg55YZ3AwpQJiohckcoa2NnG9")
            .setOAuthAccessToken("2562981560-Yc8qhBw3b1QzhtFN02d0O6xUJ8R4VIEE01iHGXy")
            .setOAuthAccessTokenSecret("dOTA2Gquh8KlZmvy9fRVRh7umr2vZPzBnFuUl5hd9RpAa");
    
        twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        twitterCollector = new TwitterStreamingCollector();
        
        twitterStream.addListener(this);
        twitterStream.sample();
        
    }

    @Override
    public boolean hasNext() {
        if(list.size() > 0)
        	return true;
        else
        	return false;
    }
    

    @Override
    public Collection<Status> next() {
        //list.addAll(getTweets(twitterStream, twitterCollector));
        List<Status> data = Lists.newArrayList();
        data.addAll(list);
        list.clear();
        return data;
    }

    @Override
    public void onStatus(Status tweet) {
  	 // COLLECTION OF TWEETS
  	  list.add(tweet);
    }
    
    public List<Status> getData(){
    	//SAVE CURRENT LIST LOCALLY, CLEAR GLOBAL LIST, RETURN LOCAL LIST
  	  List<Status> saved = Lists.newArrayList(); 
  	  saved.addAll(list);
  	  list.clear();
  	  return saved;
    }
    
    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
       // System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
        
    }

    @Override
    public void onStallWarning(StallWarning warning) {
        System.out.println("Got stall warning:" + warning);
    }

    @Override
    public void onException(Exception ex) {
        ex.printStackTrace();
        try {
			Thread.sleep(2000);
			System.out.println("EXCEPTION: WAITING 2 SECONDS FOR RESTART");
				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        list.addAll(next());
        
    }
    
    
}
