from django.shortcuts import render
from django.http import HttpResponse
import folium
from folium.plugins import Search
import os
import geopandas as gpd, folium, branca
import pandas as pd
import geocoder
# Create your views here.

def index(request):
    
    # Passing our searched query
    latArr = []
    lonArr = []
    textArr = []
    complete_Flag = 0
    if os.path.exists("input_file_lat.txt"):
        with open('input_file_lat.txt') as f:
            complete_Flag += 1
            for line in f:
                line = line.rstrip('\n')
                val = float(line)
                latArr.append(val)
    if os.path.exists("input_file_long.txt"):
        with open('input_file_long.txt') as f:
            complete_Flag += 1
            for line in f:
                line = line.rstrip('\n')
                val = float(line)
                lonArr.append(val)        
    if os.path.exists("input_file_text.txt"):
        with open('input_file_text.txt', encoding = 'utf-8') as f:
            complete_Flag += 1
            for line in f:
                textArr.append(line)

    # Make a data frame with dots to show on the map
    data = pd.DataFrame({
    'lat' : latArr,
    'lon' : lonArr,
    'text' : textArr
    })
    
    # Initialize an empty map
    map = folium.Map(location=[20, 0], tiles="Mapbox Bright", zoom_start=2)

    user_location = geocoder.ip('Me')
    user_latitude = user_location.latlng[0]
    user_longitude = user_location.latlng[1]
    folium.Marker(location = [user_latitude, user_longitude], popup = "You are currently here!", icon = folium.Icon(color='red')).add_to(map)
    
    # Adding markers based on users' locations
    if complete_Flag == 3:
        for i in range(0,len(data)):
            folium.Marker([data.iloc[i]['lat'], data.iloc[i]['lon']], popup=data.iloc[i]['text'], icon = folium.Icon(color='green')).add_to(map)
        complete_Flag = 0


    if os.path.exists("input_file_lat.txt"):
        os.remove("input_file_lat.txt")
    if os.path.exists("input_file_long.txt"):
        os.remove("input_file_long.txt")
    if os.path.exists("input_file_text.txt"):
        os.remove("input_file_text.txt")


    template_dir = (os.path.join(os.path.dirname(__file__), 'templates/index.html'))
    map.save(template_dir)
    return render(request, 'index.html')