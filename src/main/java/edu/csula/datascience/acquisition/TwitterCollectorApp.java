package edu.csula.datascience.acquisition;

import twitter4j.Status;

import java.util.Collection;

/**
 * A simple example of using Twitter
 */
public class TwitterCollectorApp {
    public static void main(String[] args) {
        TwitterSource source = new TwitterSource(Long.MAX_VALUE, "#bigdata");
        TwitterCollector collector = new TwitterCollector();

        while (source.hasNext()) {
            Collection<Status> tweets = source.next();
            Collection<Status> cleanedTweets = collector.mungee(tweets);
            collector.save(cleanedTweets);
        }
    }
}
