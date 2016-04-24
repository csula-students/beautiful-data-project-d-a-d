

package edu.csula.datascience.acquisition;

/**
 * Mock raw data
 */
public class MockData2 {
    private final String id;
    private final String tweetId;
    private final String username;
    private final String text;
    private final int fav;
    private final String date;
    private final int retweet;
    private final boolean retweeted;
    private final boolean sensitive;
    
    

    public MockData2(String id, String tid, String username, String text, int fav, String date, int retweet, boolean retweeted, boolean sensitive) {
        this.id = id;
        this.tweetId = tid;
        this.username = username;
        this.text = text;
        this.fav = fav;
        this.date = date;
        this.retweet = retweet;
        this.retweeted = retweeted;
        this.sensitive = sensitive;
        
        
        
        
   }

    public String getId() {
        return id;
    }

    public String getTweetId() {
        return tweetId;
    }

    public String getText() {
        return text;
    }

    public int getFav() {
        return fav;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public int getRetweet() {
        return retweet;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public boolean isSensitive() {
        return sensitive;
    }

    
}