class TweetModel():

	def __init__(self, data):
		self.tweet_id = None
		self.user_id = None
		self.text = None
		self.retweeted = None
		self.misc = []
		self.getId(data)
		self.getText(data)
		self.getUserId(data)

	def getId(self, data):
		begin = data.find('"id":')
		cut = data.find(',"id_str":')
		if(len(data[begin+8:cut]) > 0 and begin > 0 and cut > 0):
			self.tweet_id = data[begin+5:cut]



	def getText(self, data):
		begin = data.find('"text":')
		cut = data.find('","source"')
		if(len(data[begin+8:cut]) > 0 and begin > 0 and cut > 0):
			self.text = data[begin+8:cut]
			self.getRetweeted()




	def getUserId(self, data):
		begin = data.find('"user":{"id":')
		data = data[begin+13:]
		cut = data.find(',"id_str":')
		if(len(data[:cut]) > 0 and begin > 0 and cut > 0):
			self.user_id = data[:cut]



	def getRetweeted(self):
		t = self.text
		if t.find('RT @') >= 0 or t.find('RT  @') >= 0:
			self.retweeted = True
		else:
			self.retweeted = False



	def toString(self):

		if self.text == None:
			return 'TEXT IS NONE'
		elif self.tweet_id == None:
			return 'ID IS NONE'
		elif self.user_id == None:
			return 'USER_ID IS NONE'
		else:
			 return 'ID: '+ str(self.tweet_id) +' TEXT: '+self.text +' USER_ID'+str(self.user_id) 


