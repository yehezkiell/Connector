<h1>Connector</h1>



[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

This is an Android Application for Duta Wacana Christian University Management Class. I created this for my last project in college, this app actually have a website but it doesnt connect each other. Im using firebase for the backend and pure java in front end.


<img src="https://i.imgur.com/5pEbAWe.png" width="150"> <img src="https://i.imgur.com/OcPBSBv.png" width="150">   <img src="https://i.imgur.com/jdRlgva.png" width="150">   <img src="https://i.imgur.com/3V19oX3.png" width="150">   <img src="https://i.imgur.com/vLPPoSy.png" width="150">

<img src="https://i.imgur.com/jRnDH5z.png" width="150"> <img src="https://i.imgur.com/8lNoDej.png" width="150"> <img src="https://i.imgur.com/z34D3fZ.png" width="150"> <img src="https://i.imgur.com/8lNoDej.png" width="150"> <img src="https://i.imgur.com/9MAzWRu.png" width="150">


## Information
The website is called E-Class, E-class is a website that contains information and many things to help university, lecturer, and student to improve lecture activities. Students can download course material, latest homework, latest news, submit the homework , and many more. So this application is like prototype with all of the real E-Class function in it. I'm trying to fix the problem that students had so I created this application in native app.

* [E-Class UKDW](http://eclass.ukdw.ac.id/id/) - The Eclass Website
* [UKDW Portal](https://www.ukdw.ac.id/) - Portal University


<h1>Tech</h1>

Connector uses a number of open source projects to work properly:

* [Java](https://www.java.com/en/) - Main language
* [Firebase](https://firebase.google.com/) - Database, authentication, storage, etc.
* [Node.js](https://nodejs.org/en/) - As a back-end system


And of course Connector itself is open source with a [public repository](https://github.com/yehezkiell/EclassApp)
 on GitHub.

### <h1>Installation</h1>

#### If you want to contribute in this project you should do : 

Connector requires newest [Node.js](https://nodejs.org/) and [NPM](https://www.npmjs.com/) to run.

Install the dependencies and devDependencies and start the server.

```sh
npm install -g firebase-tools
```

Initialize Firebase SDK for Cloud Functions

* Run ```firebase login``` to log in via the browser and authenticate the firebase tool.
* Go to your Firebase project directory.
* Run ```firebase init functions```. The tool gives you an option to install dependencies with npm. It is safe to decline if you want to manage dependencies in another way.

You can run the code which is index.js with this command in the root directory :
```sh
firebase deploy
```


</br>

#### If you only want to run or test the project you should do : 

***There are 2 ways :***

First :
* Clone/download the project
* Open the project from Android Studio and run in your own emulator or device.

Second :
* You can take the apk in this path 
    ```sh
    AndroidStudioProjects\EclassUkdw\app\build\outputs\apk\debug 
    ```
* Copy to your own device and install it.



