from tweepy import Stream
from tweepy import OAuthHandler
from tweepy.streaming import StreamListener
import json, csv
import os

ckey = ""
csecret = ""
atoken = ""
asecret = ""
Latitude = 33.9936
Longitude = -117.383 
km_radius = 160.934 # 100 miles
bounding_radius = km_radius / 10001.965729
up_Latitude = Latitude + bounding_radius
up_Longitude = Longitude + bounding_radius
down_Latitude = Latitude - bounding_radius
down_Longitude = Longitude - bounding_radius


class listener(StreamListener):

    def on_status(self, status):
        print(status.truncated)
        print(status.user.geo_enabled)
        if status.truncated == True or status.coordinates is None or status.place is None or status.user.geo_enabled == False:
            return

    def on_data(self, data):
        counter = 0
        coordinates_flag = 0
        pass_flag = 0
        geo_flag = 0
        english_flag = 0
        hold_json = json.loads(data)
        # print(hold_json)
        # exit()
        if hold_json.get('user'):
            if hold_json.get('user').get('lang'):
                tweet_language = hold_json['user']['lang']
                if tweet_language == 'en':
                    english_flag = 1
            if hold_json.get('user').get('geo_enabled'):
                geo_enabled = hold_json['user']['geo_enabled']
                # print("Geo_enabled: " + str(geo_enabled))
                if geo_enabled == True:
                    geo_flag = 1
        if hold_json.get('coordinates'):
            if hold_json.get('coordinates').get('coordinates'):
                user_coordinates = hold_json['coordinates']['coordinates']
                # print("user_coordinates: " + str(user_coordinates))
                if user_coordinates is not None:
                    coordinates_flag = 1
        if hold_json.get('place'):
            if hold_json.get('place').get('bounding_box'):
                if hold_json.get('place').get('bounding_box').get('coordinates'):
                    user_box_coordinates = hold_json.get('place').get('bounding_box').get('coordinates')
                    # print("user_box_coordinates: " + str("user_box_coordinates"))
                    if user_box_coordinates is not None:
                    # user_location_type = hold_json['coordinates']['type']
            # if user_location_type == "Point":
                        pass_flag = 1
                    # counter += 1
            # else:
                # pass_flag = 0
        # print('Language:' + str(tweet_language))
        # print('Geo:' + str(geo_flag))
        # print('Box:' + str(pass_flag))
        # if tweet_language == 'eng' and geo_enabled == True and user_place is not None and is_truncated == False and user_coordinates is not None and user_location_type == "Point":
        # if english_flag == 1 and (geo_flag + pass_flag >= 1):
        if english_flag == 1 and (coordinates_flag == 1 or pass_flag == 1):
            path = os.path.join(os.getcwd(), "raw_twitDB3.json")
            file_size = os.path.getsize(path)
            print('File Size: ' + str(file_size))
            if file_size < 1000000000:
                saveFile = open('raw_twitDB3.json', 'a', encoding = 'utf-8')
                saveFile.write(data)
                # saveFile.write('\n')
                saveFile.close()

            else:
                exit()
        counter = 0
        return True



    def on_error(self, status):
        if status == 420:
            print("Twitter has rate limited us!")
            return False

newFile = open('raw_twitDB3.json', 'w', encoding = 'utf-8')
newFile.close()
auth = OAuthHandler(ckey, csecret)
auth.set_access_token(atoken, asecret)
twitterStream = Stream(auth, listener())
# twitterStream.sample()
twitterStream.filter(track=["the", "i", "to", "and", "a"])

# twitterStream.filter(locations=[-122.75,36.8,-121.75,37.8])
# twitterStream.filter(locations=[down_Longitude, down_Latitude, up_Longitude, up_Latitude])