from tweepy import Stream
from tweepy import OAuthHandler
from tweepy.streaming import StreamListener
import json, csv
import os

nameFile = open('names.txt', 'w', encoding = 'utf-8')
nameFile.close()
textFile = open('text.txt', 'w', encoding = 'utf-8')
textFile.close()
urlFile = open('tweet_url.txt', 'w', encoding = 'utf-8')
urlFile.close()
longitudeFile = open('longitude.txt', 'w', encoding = 'utf-8')
longitudeFile.close()
latitudeFile = open('latitude.txt', 'w', encoding = 'utf-8')
latitudeFile.close()
counter = 0
# data_File = open('raw_twitDB3.json')
with open('raw_twitDB3.json') as dataLine:
	print("Loading file...")
	for line in dataLine:
		# print(line)
		if len(line) > 1:
			# print("this: " + str(len(line)))
			parse_data = json.loads(line)

			# print(parse_data['truncated'])
			twitter_handle = (parse_data['user']['name'])
			nameFile = open('names.txt', 'a', encoding = 'utf-8')
			twitter_handle = str(twitter_handle)
			nameFile.write(twitter_handle)
			nameFile.write('\n')
			nameFile.close()
			# if parse_data['truncated'] == 'false'
			if parse_data.get('extended_tweet'):
				if parse_data.get('extended_tweet').get('full_text'):
					user_text = parse_data['extended_tweet']['full_text']
					# print(user_text)
				else:
					user_text = parse_data['text']

			elif parse_data.get('retweeted_status'):
				if parse_data.get('retweeted_status').get('extended_tweet'):
					if parse_data.get('retweeted_status').get('extended_tweet').get('full_text'):
						user_text = parse_data['retweeted_status']['extended_tweet']['full_text']
					else:
						user_text = parse_data['text']
				else:
					user_text = parse_data['text']
			else:
				user_text = parse_data['text']
			combined_line = "".join(user_text.splitlines())
			textFile = open('text.txt', 'a', encoding = 'utf-8')
			textFile.write(combined_line)
			textFile.write('\n')
			textFile.close()

			if parse_data.get('entities'):
				if parse_data.get('entities').get('urls'):
					if parse_data.get('entities').get('urls')[0].get('expanded_url'):
						tweet_url = parse_data['entities']['urls'][0]['expanded_url']
						urlFile = open('tweet_url.txt', 'a')
						base_url = "https://twitter.com/i/web/status/"
						if parse_data.get('id'):
							tweet_id = parse_data['id']
							combined_id = base_url + str(tweet_id)
							if tweet_url == combined_id:
								tweet_url = "No urls"
						urlFile.write(tweet_url)
						urlFile.write("\n")
						urlFile.close()
					else:
						tweet_url = "No urls"
						urlFile = open('tweet_url.txt', 'a')
						urlFile.write(tweet_url)
						urlFile.write("\n")
						urlFile.close()

			if parse_data.get('place'):
				if parse_data.get('place').get('bounding_box'):
					if parse_data.get('place').get('bounding_box').get('coordinates'):
						geo_location1 = parse_data['place']['bounding_box']['coordinates'][0][1][0]
						geo_location2 = parse_data['place']['bounding_box']['coordinates'][0][1][1]
						longitudeFile = open('longitude.txt', 'a')
						latitudeFile = open('latitude.txt', 'a')
						longitudeFile.write(str(geo_location1))
						latitudeFile.write(str(geo_location2))
						longitudeFile.write("\n")
						latitudeFile.write("\n")
						longitudeFile.close()
						latitudeFile.close()
