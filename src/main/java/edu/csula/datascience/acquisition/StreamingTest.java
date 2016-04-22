package edu.csula.datascience.acquisition;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Collection;

public class StreamingTest {

    public static void main(String[] args) throws TwitterException {


        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("l6ny8YvpqT5HtKFZQZIREqbn9")
                .setOAuthConsumerSecret("ppNSR0iDGq7EOTTv5xsC0r73nXaKrl5cNNbCHJMKcVeLI5Bjwg")
                .setOAuthAccessToken("708345771750371330-vPcegWoya034SXdJd4KriBCzw1dEPEt")
                .setOAuthAccessTokenSecret("wAAOqmB0vXoKepoQ4ZLX4wcaW5Aqdj5JdgLstxNziEPhW");

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        TwitterCollector twitterCollector = new TwitterCollector();
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status tweet) {
                System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
                	
//                	if (twitterCollector.mungee(tweet) == null) // clean tweet
//                	{
//                		System.out.println("cleaned tweet");
//                	}
                	
//                	else
//                	{
                		twitterCollector.save(tweet); // save a singleton
//                		System.out.println("saved tweet");
//                	}

                	
            	// COLLECTION OF TWEETS
            	
//            	int counter = 0;
//            	
//            	Collection<Status> tweets = new ArrayList<>();
//            	
//            	while (counter < 100) // save 100 tweets a time
//            	{
//            		tweets.add(tweet);
//            		System.out.println("Got a tweet");
//            		counter++;
//            	
//            		
//            	}
//            	
//            	System.out.println("Made it to 100 tweets");
//            	
//            	twitterCollector.save(tweets); // put collection of tweets in the DB
//            	
//            	tweets.clear(); // clear the collection to avoid duplicates

            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
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
            }
        };


        String list[] = {"#tech", "#startup"};


        // filter tweets
        FilterQuery fq = new FilterQuery();

        fq.track(list);

        fq.language("en");

        twitterStream.addListener(listener);

        twitterStream.filter(fq);


//
//        twitterStream.addListener(listener);
//        twitterStream.sample();
    }
}
