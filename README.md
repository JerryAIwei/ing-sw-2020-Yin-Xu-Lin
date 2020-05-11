# ing-sw-2020-Yin-Xu-Lin

## 19/20 Software Engineering Final Project 

### About the game

Santorini is an abstract strategy board game for 2-4 players, the name was inspired by the architecture of cliffside villages on Santorini Island in Greece. The game is played on a grid where each turn players build a town by placing building pieces up to three levels high. To win the game, players must move one of their two characters to the third level of the town. Normally a game takes about 20-30 minutes.

### Anout the project

This is the repo of the fianl project of the Polimi Software Engineering course. The project is developed by Lin Xin, Xu Shaoxun and Yin Aiwei.

The current progress of the project:


- [x] Simplified Rules: finished

- [ ] Complete Rules: nearly finished, debugging: GodPower's checkWin()

- [x] Socket: finished

- [ ] CLI: nearly finished

- [ ] GUI: WIP

- [ ] Advanced Feature - Multiple Games: finished, debugging

- [ ] Advanced Feature - Persistence: finished, debugging

### Attention

The CLI version of the game is better in the terminal with dark background, we haven't tested whether the color setter code used can run on Windows platform.

Since the project is still under development, the data organization might be changed, especially the local data files stored for the advanced feature Persistence. If the server doesn't run as expected, please delete the data directory manually.

#### special description of the data file: 

All the data files of a game is in data/game*, where * is the game ID. The server will always restore the virtualGame_i.ser file, which is the latest version of the game i. 

To make it easier for debugging, we store each change of the game in virtualGame_i_j.ser file, as the game goes on, the j goes larger. You can always stop the server, rename the data file to virtualGame_i.ser and restart the server, relogin the game with the same nicknames and test.