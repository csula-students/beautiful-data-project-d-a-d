from pymongo import MongoClient
from nltk.classify import NaiveBayesClassifier
from nltk.corpus import stopwords
import numpy as np
import datetime

#stop words(a, the, to, etc) to help filter out useless words and get the keywords
stopset = list(set(stopwords.words('english')))

#breaks down a string into words and ignores stopset words(a, the, to , it , etc)
def word_feats(words):
    return dict([(word, True) for word in words.split() if word not in stopset])



def trainer():
	# these are key words that appear in each field
	
	VRID = ['VR virtual reality augmented augment Vive oculus rift ']
	greenID = ['green clean energy solar wind electric environment renewable reusable recycle natural']
	aiID = ['AI artifical intelligence intelligent machine learning']
	financeID = ['ecommerce finance financial money monetization']
	IoTID = ['internet of things  IoT iot InternetOfThings internetofthings']
	
	printingID = ['3D printing 3D-printing 3D-printer 3D-print']

	#this breaks down the key words into a set feature for natural langauge larning
	
	VR_feats = [(word_feats(f), 'VR') for f in VRID ]
	green_feats = [(word_feats(f), 'green') for f in greenID ]
	ai_feats = [(word_feats(f), 'AI') for f in aiID ]
	finance_feats = [(word_feats(f), 'finance') for f in financeID ]
	IoT_feats = [(word_feats(f), 'IoT') for f in IoTID ]
	


	#set your trainer with all the feature sets you created above
	trainfeats = VR_feats + green_feats + ai_feats + finance_feats + IoT_feats
	
	#Create the classifier object that you can now give strings to to classify
	classifier = NaiveBayesClassifier.train(trainfeats)

	return classifier



#simple hardcoded tests for the classifier to solve
#is only wrong in one case
def ezTester(classifier):
	print 'Predicted -- Real'
	print classifier.classify(word_feats('Startup creates new space shuttle!')), ' -- space'
	print classifier.classify(word_feats('Startup updates old rockets for relaunch!')), ' -- space'
	print classifier.classify(word_feats('Startup creates best self driving car ever!')), ' -- car'
	print classifier.classify(word_feats('Apple buys new automous car startup')), ' -- car'
	print classifier.classify(word_feats('Google buys new mobile development startup')), ' -- mobile'
	print classifier.classify(word_feats('Startup helps veterans heal PTSD through virtual reality')), ' -- VR'
	print classifier.classify(word_feats('Apple is making solar power a reality')), ' -- green'

	print classifier.classify(word_feats('Im making a startup for phone apps!')), ' -- mobile'
	print classifier.classify(word_feats('Zzz is a startup to help you sleep using augmented reality')), ' -- VR'
	print classifier.classify(word_feats('We are using machine learning to help your coffee taste better #uselessstartup')), ' -- AI'
	print classifier.classify(word_feats('This Detroit startup is pumping out electric cars')), ' -- green'
	print classifier.classify(word_feats('We use python and complex algorithms that learn how to make your day easier for you')), ' -- AI'
	print classifier.classify(word_feats('Obama helps clean energy startups with government rebates')), ' -- green'
	print classifier.classify(word_feats('Apple is making wind power a reality')), ' -- green'

#classifies the list given using the classifyer given
def classify(classifier, a_list):
	

	length = len(a_list)
	count = 0
	ai_1 = 0
	vr_1 = 0
	green_1 = 0
	ecommerce_1 = 0
	iot_1 = 0
	ai_2 = 0
	vr_2 = 0
	green_2 = 0
	ecommerce_2 = 0
	iot_2 = 0

	for item in a_list:
		#print classifier.prob_classify(word_feats(item))
		dist = classifier.prob_classify(word_feats(item))

		values = []
		for label in dist.samples():
			values.append(dist.prob(label) * 100)

		np_array = np.array(values, dtype=np.float)

		#sets a threshold for confidence. if it is below confidence, ignore it(unclassified or other field we havent defined)
		#  higher confidenve level: lower error of classified tweet. Lower amount of tweets classified
		#  lower confidence level: higher error of classified tweets. Higher amount of tweets classified
		#Find a good balance of level of error and amount of tweets classified
		# @8: 15 out of 15 get classified
		# @15: 2 out of 15 get classified
		# @20: 2 out of 15 get classified
		# @22: 1 out of 15 get classified
		thres = 21
		if len(np.where(np_array >= thres)[0]) == 0:
			#print item, 'OTHER'
			pass
			length -= 1
		else:
			field = classifier.classify(word_feats(item)), np.amax(np_array)
			field = field[0]

			if count > length / 2:
				if field == 'VR':
					vr_1 += 1

				if field == 'green':
					green_1 += 1

				if field == 'AI':
					ai_1 += 1

				if field == 'finance':
					ecommerce_1 += 1

				if field == 'IoT':
					iot_1 += 1
			else:
				if field == 'VR':
					vr_2 += 1

				if field == 'green':
					green_2 += 1

				if field == 'AI':
					ai_2 += 1

				if field == 'finance':
					ecommerce_2 += 1

				if field == 'IoT':
					iot_2 += 1


		count += 1


	print 'Classified ', length, ' out of ', len(a_list)
	print 'AI: ', ai_1, ai_2
	print 'VR: ', vr_1, vr_2
	print 'Green: ', green_1, green_2
	print 'Ecommerce: ', ecommerce_1, ecommerce_2
	print 'IoT: ', iot_1, iot_2

#gets all the tweets from your mongodb
def getTweets():
	client = MongoClient()
	db = client.all_tweets # change the your mongodb database name
	results = db.tweets.find()
	
	tweets = []

	for item in results:
		tweet = item['text']
		tweets.append(tweet.encode('ascii','ignore'))
	return tweets

def getLineGraphData(classifier):
	client = MongoClient()
	db = client.all_tweets # change the your mongodb database name
	results = db.tweets.find()
	
	tweets = []
	ai_tweets = {}
	vr_tweets = {}
	ecommerce_tweets = {}
	iot_tweets = {}
	green_tweets = {}
	tweet = {}

	for item in results:
		text = item['text'].encode('ascii','ignore')
		field =  classify_1(classifier, text)
		if not field == 'other':
			tweet['field'] = field
			# tweet['date'] = str(item['date'].year)+'-'+str(item['date'].month)+'-'+str(item['date'].day)
			date = str(item['date'].year)+'-'+str(item['date'].month)+'-'+str(item['date'].day)
			tweet['date'] = date#datetime.date(item['date'].year, item['date'].month , item['date'].day)
			tweets.append(tweet)
			
			# print date+','+tweet['field']
		
	for tweet in tweets:
		field = tweet['field']
		date = tweet['date']

		if field == 'VR':
			if date in ai_tweets:
				vr_tweets[date] += 1
			else:
				vr_tweets[date] = 0
				vr_tweets['field'] = field

		if field == 'green':
			if date in green_tweets:
				green_tweets[date] += 1
			else:
				green_tweets[date] = 0
				green_tweets['field']= field

		if field == 'AI':
			if date in ai_tweets:
				ai_tweets[date] += 1
			else:
				ai_tweets[date] = 0
				ai_tweets['field'] = field


		if field == 'finance':
			if date in ecommerce_tweets:
				ecommerce_tweets[date] += 1
			else:
				ecommerce_tweets[date] = 0
				ecommerce_tweets['field'] = field

		if field == 'IoT':
			if date in iot_tweets:
				iot_tweets[date] += 1
			else:
				iot_tweets[date] = 0
				iot_tweets['field']= field


	dicts = []
	dicts.append(ai_tweets)
	dicts.append(vr_tweets)
	dicts.append(ecommerce_tweets)
	dicts.append(iot_tweets)
	dicts.append(green_tweets)
	print len(ai_tweets)
	print len(vr_tweets)
	print len(ecommerce_tweets)
	print len(iot_tweets)
	print len(green_tweets)
	return dicts

#gets some amount of tweets from your db based on limit given
def getTweetsWithLimit(lim):
	client = MongoClient()
	db = client.all_tweets # change the your mongodb database name
	results = db.tweets.find().limit(lim)
	
	tweets = []

	for item in results:
		tweet = item['text']
		tweets.append(tweet.encode('ascii','ignore'))
	return tweets

def getLineGraphDataLimit(lim, classifier):
	client = MongoClient()
	db = client.all_tweets # change the your mongodb database name
	results = db.tweets.find().limit(lim)
	
	tweets = []
	ai_tweets = {}
	vr_tweets = {}
	ecommerce_tweets = {}
	iot_tweets = {}
	green_tweets = {}
	tweet = {}

	for item in results:
		text = item['text'].encode('ascii','ignore')
		field =  classify_1(classifier, text)
		if not field == 'other':
			tweet['field'] = field
			# tweet['date'] = str(item['date'].year)+'-'+str(item['date'].month)+'-'+str(item['date'].day)
			date = str(item['date'].year)+'-'+str(item['date'].month)+'-'+str(item['date'].day)
			tweet['date'] = date#datetime.date(item['date'].year, item['date'].month , item['date'].day)
			tweets.append(tweet)
			
			# print date+','+tweet['field']
		
	for tweet in tweets:
		field = tweet['field']
		date = tweet['date']
		
		if field == 'VR':
			if date in ai_tweets:
				vr_tweets[date] += 1
			else:
				vr_tweets[date] = 0
				vr_tweets['field'] = 'VR'

		if field == 'green':
			if date in green_tweets:
				green_tweets[date] += 1
			else:
				green_tweets[date] = 0
				green_tweets['field']='Clean Energy'

		if field == 'AI':
			if date in ai_tweets:
				ai_tweets[date] += 1
			else:
				ai_tweets[date] = 0
				ai_tweets['field'] = 'AI'


		if field == 'finance':
			if date in ecommerce_tweets:
				ecommerce_tweets[date] += 1
			else:
				ecommerce_tweets[date] = 0
				ecommerce_tweets['field'] = 'Ecommerce'

		if field == 'IoT':
			if date in iot_tweets:
				iot_tweets[date] += 1
			else:
				iot_tweets[date] = 0
				iot_tweets['field'] = 'IoT'


	dicts = []
	dicts.append(ai_tweets)
	dicts.append(vr_tweets)
	dicts.append(ecommerce_tweets)
	dicts.append(iot_tweets)
	dicts.append(green_tweets)
	print len(ai_tweets)
	print len(vr_tweets)
	print len(ecommerce_tweets)
	print len(iot_tweets)
	print len(green_tweets)
	return dicts

def classify_1(classifier, item):

	dist = classifier.prob_classify(word_feats(item))

	values = []
	for label in dist.samples():
		values.append(dist.prob(label) * 100)

	np_array = np.array(values, dtype=np.float)

		#sets a threshold for confidence. if it is below confidence, ignore it(unclassified or other field we havent defined)
		#  higher confidenve level: lower error of classified tweet. Lower amount of tweets classified
		#  lower confidence level: higher error of classified tweets. Higher amount of tweets classified
		#Find a good balance of level of error and amount of tweets classified
		# @8: 15 out of 15 get classified
		# @15: 2 out of 15 get classified
		# @20: 2 out of 15 get classified
		# @22: 1 out of 15 get classified
	thres = 21
	if len(np.where(np_array >= thres)[0]) == 0:
		return 'other'
	else:
		field = classifier.classify(word_feats(item)), np.amax(np_array)
		return field[0]


def main():
	#create classifier
	classifier = trainer()

	#test against hard coded tests above
	#ezTester(classifier)

	#get some amount of tweets from db // can also use getTweets() to get all tweets in your db
	#tweets = getTweetsWithLimit(15000)
	#tweets = getTweets();


	#get tweet,date
	#classify tweet

	print 'date,value,field'
	dicts = getLineGraphDataLimit(100000, classifier)
	#dicts = getLineGraphData( classifier)

	data = []

	# for d in dicts:
	# 	if len(d) > 0:
	# 		field = d['field']
	# 		print field
	# 		for key, value in d.iteritems():
	# 			if key != 'field':
	# 				date = ''
	# 				if not key == 'field':
	# 					date = key
	# 				data.append(date+','+str(value)+','+field)


	print data


	#classifies your tweets
	#classify(classifier, tweets)

	

if __name__ == "__main__":
    main()