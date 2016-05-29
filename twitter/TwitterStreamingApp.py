from TweetStreamer import TweetStreamer
from TweetModel import TweetModel
from pymongo import MongoClient
import time



try:

	twitterStream = TweetStreamer()
	twitterStream.startFilter()

	batch = []
	cleaned = []
	client = MongoClient()
	db = client.twitter_contest # change the .twitter_contest to your mongodb database name

	while True:
		#make sure steram is open
		if twitterStream.isClosed:
			twitterStream.startFilter()
	
		time.sleep(60) # delays for 1 minute

		batch = list(twitterStream.dump())
		# break data into models and insert into database
		for data in batch:
			tweet = TweetModel(data)

		if len(cleaned) > 0:
			result = db.tweets.insert_many(cleaned)	#change .tweets to whatever your mongo colleciton is named

		#clear
		print 'cleaned: '+str(len(cleaned))

		batch = []
		cleaned = []
    
except (KeyboardInterrupt, SystemExit):
	twitterStream.kill()
	sys.exit()


