# from tweepy import Stream
# from tweepy import OAuthHandler
# from tweepy.streaming import StreamListener
import json, csv
import time
import os
from bs4 import BeautifulSoup
import pandas as pd

url = open('tweet_url.txt', 'a')
url.close()
longitude = open('longitude.txt', 'a')
longitude.close()
latitude = open('latitude.txt', 'a')
latitude.close()

with open('geo_true.json') as myFile:
	coordinates_flag = 0
	for line in myFile:		
		if len(line) > 1:
			parse_data = json.loads(line)
			if parse_data.get('entities'):
				if parse_data.get('entities').get('urls'):
					if parse_data.get('entities').get('urls')[0].get('expanded_url'):
						tweet_url = parse_data['entities']['urls'][0]['expanded_url']
						url = open('tweet_url.txt', 'a')
						url.write(tweet_url)
						# print ("This is tweet url: ", tweet_url)
						url.write("\n")
						url.close()
					else 
						tweet_url = "No url"
						url = open('tweet_url.txt', 'a')
						url.write(tweet_url)
						url.write("\n")
						url.close()
			# if parse_data.get('geo'):
			# 	if parse_data.get('geo').get('coordinates'):
			# 		geo_location = parse_data['coordinates']['coordinates'][0]
					# longitude = open('longitude.txt', 'a')
					# longitude.write(geo_location)
			# 		# print ("This is geo-coordinates: ", geo_location)
			# 		# print ("\n")
					# longitude.write("\n")
					# longitude.close()
			if parse_data.get('place'):
				if parse_data.get('place').get('bounding_box'):
					if parse_data.get('place').get('bounding_box').get('coordinates'):
						geo_location1 = parse_data['place']['bounding_box']['coordinates'][0][1][0]
						geo_location2 = parse_data['place']['bounding_box']['coordinates'][0][1][1]
						longitude = open('longitude.txt', 'a')
						latitude = open('latitude.txt', 'a')
						longitude.write(str(geo_location1))
						latitude.write(str(geo_location2))
						# print("This is polygon-coordinates: ", geo_location)
						# print ("\n")
						longitude.write("\n")
						latitude.write("\n")
						longitude.close()
						latitude.close()