from tweepy import Stream
from tweepy import OAuthHandler
from tweepy.streaming import StreamListener
import tweepy
import sys

class TweetStreamer(tweepy.StreamListener):

	def __init__(self):
		self.tweets = []
		self.ckey = 'hulzQCIk8E8Z0TwIhYxWPG2BL'
		self.csecret = 'XFJT1MyVW75PDEDf2kBSmp3opRAOE2a1zqyO0gkV9uuQahmPCp'
		self.atoken = '724768430138642432-pzDZpYGhjdWdj1ye769B5nXJKkecZB8'
		self.asecret = 'TDoZINWCqa1SZQC5nLNV6na6s1qVcEHDdcfCRlYeEwtNj'
		self.auth = OAuthHandler(self.ckey, self.csecret)
		self.auth.set_access_token(self.atoken, self.asecret)
		self.stream = Stream(self.auth, self)
		self.isClosed = True
		
		print 'setup: done.'

	def on_data(self, data):
		self.tweets.append(data)


	def on_error(self, status):
		print str(status) + "error"
		self.kill()
		self.isClosed = True
		self.startFilter()

	def dump(self):
		newTweets = list(self.tweets)
		self.tweets = []
		return newTweets

	def on_warning(self, notice):
		print notice

	def on_exception(self, exception):
		print str(exception) + " :Exception-----"
		self.kill()
		self.isClosed = True
		self.startFilter()

	def on_disconnect(self, notice):
		print str(notice) + " :Notice-----"
		self.kill()
		self.isClosed = True
		self.startFilter()	

	def startFilter(self):
		print 'started filter'
		self.isClosed = False
		self.stream.filter(track=[   '#giveaway', '#sweepstakes', '#contest', '#give-away', '#win', 'enter to win', 'for a chance to win', ' to win', ' to enter'], async=True , stall_warnings=True)	

		# self.stream.filter(track=[ 'retweet to win', 'like to win', 'follow to win', 'retweet to enter', 'like to enter', 'follow to enter', 'RT to enter', 'RT to win', 'Fav to enter', 'Fav to win' , '#competition', '#giveaway', '#sweepstakes', '#giveaway', '#give-away', '#win', 'enter to win', 'for a chance', 'to win'], async=True)	
		# self.stream.filter(track=[ 'retweet to win', 'like to win', 'follow to win','#sweepstakes', '#giveaway', '#give-away'], async=True)	
	
	def kill(self):
		self.stream.disconnect()