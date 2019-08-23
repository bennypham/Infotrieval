# finalproject-ir
finalproject-ir created by GitHub Classroom

# Group Name: IR(Infotrieval)

# Group Members
```
Chianh Wu
Benny Pham
Moses Park
```

# Description
- Created a Twitter Developer Account to access their API
- Programmed a Python script to crawl through tweets and output it as a JSON file
- Parsed the Tweet JSON file in Python to gather data:
  - User(Their handle)
  - Tweet
  - Tweet URL
  - Tweet URL Titles
  - Geo-Location
- Integrated data from the parser into Lucene for indexing query
- Setup a Django project that hosted our map webpage
  - Used python package called Folium to display our map
    - Initialized markers based on the tweets' geo-location

# How to run
Everything is installed on your home directory within Terminal.
- Install Python3(Everything is ran on python3)
- Install pip(Package manager for Python packages)
  - ` curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py `
  - ` python3 get-pip.py `
- Install Django
  - ` pip install Django `
- Install Folium
  - ` pip install folium `
- Install Geocoder
  - ` pip install geocoder `
- Install GeoPandas
  - ` pip install geopandas `
- Install Tweepy
  - ` pip install tweepy `

- In order to obtain the JSONs, run `Raw_Tweet.py`. Remember to edit the script and place your API keys for Twitter here. If you need one, please contact a group member and we will provide one. The program will attempt to accrue 1GB of tweets. However, we provide a much smaller data sample in the `data` directory on the repository (raw_twitDB3.json).

- After obtaining the JSONs, run `JSON_Parser.py`. After the program terminates, run `parse_title.py`. This will produce 6 text files. They The paths of these files are important, and will be used later in `Demo.java`.

- It is important that the contents (not the folder) inside the `user_interface` folder are placed inside the folder `LuceneDemo`. However, to ease the grading, we have already included the files within `LuceneDemo`. Please refer to the Final Report to confirm that your directory is set up correctly! It is also  important to update the pathnames utilized inside `Demo.java`, located inside the `LuceneDemo` directory. A quick note is that the Java file is in Mac pathnames and not Windows. For specific instructions regarding pathnames, please read the Final Report in the Indexer Sections part 2: Limitations of the System. The program WILL NOT work if these pathnames are unchanged. 

- Now we are ready to run the program. `cd` into wherever the project is downloaded, `finalproject-ir`, and then `LuceneDemo`. You will want to run `Demo.java`. You will be able to input a query with how many to display in the in-browser extension. The program should automatically initalize the Django server, this was tested using Windows. The in-browser extension is hosted on: http://localhost:8000/locations/

- For Mac, we have to initialize the Django server first where our map is being hosted: run `python3 manage.py runserver`. The in-browser extension is hosted on: http://localhost:8000/locations/

# References
- [Tweet JSON Documentations](https://developer.twitter.com/en/docs/tweets/data-dictionary/overview/entities-object#entitiesobject)
- [JSON Viewer](http://jsonviewer.stack.hu)
- [Retrieving URL Title](https://stackoverflow.com/questions/51233/how-can-i-retrieve-the-page-title-of-a-webpage-using-python)
- [Lucene Tutorial](https://www.youtube.com/watch?v=pVDVURw_AJQ&t=285s)
- [Install Pip](https://pip.pypa.io/en/stable/installing/)
- [Install Django](https://docs.djangoproject.com/en/2.1/topics/install/)
- [Install Geocoder](https://geocoder.readthedocs.io/api.html)
- [Install GeoPandas](http://geopandas.org/install.html)
- [Install Folium](https://python-visualization.github.io/folium/installing.html)
- [Open/Run Terminal with command using Java](https://stackoverflow.com/questions/15356405/how-to-run-a-command-at-terminal-from-java-program/15356451?noredirect=1#comment54151315_15356451)
- [Current windows/process opened on Mac](https://stackoverflow.com/questions/54686/how-to-get-a-list-of-current-open-windows-process-with-java/4465630)
- [Python program location on Mac](https://stackoverflow.com/questions/6819661/python-location-on-mac-osx)
