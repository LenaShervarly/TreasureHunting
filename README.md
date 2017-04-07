# TreasureHunting Game

A free-to-play android game that utilizes geolocation through native GPS module of a smartphone.
Currently at prototype stage.

## Contributors

* Ahmad Zahlan
* Aiham Alkaseer
* Deema Gawriyeh
* Kaan Karaoglu
* Lena Shervarly
* Ramsha Wasimuddin
* Valentina Aleksieva

![Image of JSQUAD](https://cloud.githubusercontent.com/assets/25244078/24811567/da465672-1bc6-11e7-88e3-74aba3194847.png)


## Code base
The GPS module for this project was taken from the open audio guide project of Dmytro Shervarly (https://github.com/candylovers/audiotravel) with his fully agreement and adapted for the requirements of KnowHunt Game by Lena Shervarly. 

*The following folders within the package "com.home.jsquad.knowhunt.android" were provided with the initial project: instrumentation, location, maps, math, security and settings. 
*Classes in the folders "audio" and "domain" contain some of the provided files, but were ajusted according to needs of KnowHunt Game
*Classes in the folders "activities" and "database", as well, as "core" package were developped by J-SQUAD team members.

In the folder "activities" you can find information regarding how everything, that appears on the screen works. Strating from StartTheGameActivity:

![Image of StartGame](https://cloud.githubusercontent.com/assets/25244078/24811570/de0096ec-1bc6-11e7-8bf7-50e1c6cc578f.png)

throw Registration 

![Image of Registration](https://cloud.githubusercontent.com/assets/25244078/24811593/f1ee19f4-1bc6-11e7-8d1a-29a40d7edc15.png)

and Login in, you'll get to the MapActivity

![Image of MapActivity](https://cloud.githubusercontent.com/assets/25244078/24811573/e18dbae2-1bc6-11e7-9a51-c947999e6145.png)

MapActivity is calling MapHelper to initialize the map and add the route and the blue nodes to it, then the ActivityFactory, using the modulo and the names of existing activities assigns different activities for every node:

*For the first node we've developed a mini-game, using LibGDX library. In the androin faulder you'll find AndroidLauncher class, that launches the game. All the logic and the game itself can me seen in the package "core":

![Image of MiniGame](https://cloud.githubusercontent.com/assets/25244078/24811579/e74ddab6-1bc6-11e7-8bc8-610985d8a40a.png)

*For the second node we've developed a "Guess Melody" activity, which calls the audio player and at the moment uses the local SQLite database to call every time a new song from the raw directory and shows a question and 4 options related to this song only. On the next stages of the project developments, we're planning to use a remote server and load our tracks there, and the questions load to MySQL database, which will provide more flexibility:

![Image of GuessMelody](https://cloud.githubusercontent.com/assets/25244078/24811589/edd80258-1bc6-11e7-9d88-48eed1b2b81f.png)

*For the third node, we've developed a "Take picture activity", wich gives a task to the user of taking a picture at a definite location or with definite ovbjects. At the further stages, we're planning to make a collage of all of the picures, taken by different users on the node and to show it at the end of the game:

*For the forth node, we've developed a "Quizz activity with 4 answers", which works with the remote database and at the moment it's the only one, that can be customized from a remote website.

![Image of Quizz4](https://cloud.githubusercontent.com/assets/25244078/24811652/29412270-1bc7-11e7-98fa-804f28d444e6.png)

*As an example of what else can we do, there is a fiveth node with 2 yes|no questions, that are hardcoded at the moment. According to our plans, they are going to be connected to the remote database too.

Appart from the application itself, the website prototype had been developed (can be found in the folder website) and a webservice, connecting the app with MySQL database (can be found in the folder "service")
