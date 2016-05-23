import got
import time
from pymongo import MongoClient


def main():
    client = MongoClient()
    db = client.twitter

    # Example 3 - Get tweets by username and bound dates
    searchTerms = '#startup OR "#startups OR #startuplife OR startup OR startups'
    tweetLimit = 100000000000000000

    # Following code is to get tweets for the month of January
    # Change the range according to the month you're looking for
    # Ex. If doing Feb (28 days) it would be 1, 28
    for day in range(1, 31):
        start = time.time()
        date1 = "2016-01-" + str(day)
        date2 = "2016-01-" + str(day + 1)
        tweetCriteria = got.manager.TweetCriteria().setQuerySearch(searchTerms).setSince(date1).setUntil(date2).setMaxTweets(tweetLimit)
        tweets = got.manager.TweetManager.getTweets(tweetCriteria)

        for tweet in tweets:
            insert = db.tweets.insert_one({
                "username": tweet.username,
                "retweets": tweet.retweets,
                "text": tweet.text,
                "hashtahgs": tweet.hashtags,
                "mentions": tweet.mentions,
                "date": tweet.date
            }).inserted_id
            insert
        end = time.time()
        elapsed = end - start;

        print "Inserted day", date1, "through", date2 + ".", "Time Elapsed: ]", elapsed

if __name__ == '__main__':
    main()