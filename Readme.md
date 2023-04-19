# Music Player Desktop Application using Java

### How to run : 
1. Clone the repository at [this](https://github.com/VarunSH-15-11-2001/Java-project.git) link.
2. Before proceeding, make sure to change the direcotry for where the music files are stored. This directory should be changed at lines 53 and 184 of the `MusicPlayerView.java` file. Also note that the supported file extension is `.wav` only.
3. Open a terminal in the same direcotry and run the following commands one after the other.
```
javac Main.java
java Main
```

### About the project : 
This project was part of our OOAD course and the main objective was to famirialize ourselves with the implementation of the *MVC framework*. 

The MVC framework is used for large scale object oriented projects and applications. It involves separaring the working components of the project/application into three interconnected pieces - **the Model**, **the View** and **the Controller**.

1. **The View** component contains all the functionailities related to the user interface of the application. An analogy to the view component is the front end in a web app. 

2. **The Model** component is responsible for managing the application's data, logic, and rules. It communicates with the View and Controller components, but does not directly depend on either. 

3. **The Controller** component acts as an intermediary between the View and the Model, interpreting user actions and triggering updates to the Model or View as necessary. It provides a way for the user to interact with the application and coordinates the flow of information between the other two components.

As the names suggest, `MusicPlayerView.java` implements the view component, `MusicPlayerModel.java` implements the model component and `MusicPlayerController.java` implements the controller component. 

