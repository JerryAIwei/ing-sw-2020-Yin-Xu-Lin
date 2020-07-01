# ing-sw-2020-Yin-Xu-Lin

## 19/20 Software Engineering Final Project 

### About the game

Santorini is an abstract strategy board game for 2-4 players, the name was inspired by the architecture of cliffside villages on Santorini Island in Greece. The game is played on a grid where each turn players build a town by placing building pieces up to three levels high. To win the game, players must move one of their two characters to the third level of the town. Normally a game takes about 20-30 minutes.

### About the project

This is the repo of the fianl project of the Polimi Software Engineering course. The project is developed by Lin Xin, Xu Shaoxun and Yin Aiwei.

The project implements:


- [x] Simplified Rules

- [x] Complete Rules

- [x] Socket

- [x] CLI

- [x] GUI

- [x] Advanced Feature - Multiple Games

- [x] Advanced Feature - Persistence

### How to use the Jar

The `Santorini.jar` is all-in-one. You can both establish the game server and run the game client:

* If you want to establish a game server, run 

    ```java -jar Santorini.jar -S```
    
    or 
    
    ```java -jar Santorini.jar -S PORT_NUMBER```

For example `java -jar Santorini.jar -S 8888`. The default port number is 7777.

* If you want to run the game client, run 

    `java -jar Santorini.jar -C -c` for Command-line interface
    
    or
    
    `java -jar Santorini.jar -C -g` for Graphical User Interface
    
    
    Since we used javaFX3D, in case of a **warning** like `WARNING: System can't support ConditionalFeature.SCENE3D` or the situation where you **cannot see the 3D elements** of the game board, `java -Dprism.forceGPU=true -jar Santorini.jar -C -g` might help.

### Special Attention

The CLI version of the game is better in the terminal with dark background.

