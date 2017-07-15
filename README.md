Games Repository

Though I am not a game programmer, I have made these two games for assignments in two different programming classes(one in Java and another in C).

2048 Game with GUI
Eric Gelphman 
University of California San Diego Jacobs School of Engineering
Departmen of Electrical and Computer Engineering (ECE)

The three Java files attached are to run the game 2048, this time with graphics, like the original game. 
The Gui2048 file contains code to implement the GUI and actually run the game. 
The Board and Direction files are needed to implement the game mechanics(Board is a class, Direction is an enumeration). 
The gameplay is exactly the same, except that in my version, the board can be rotated 90 degrees by pressing the “R” key, 
and the user can save the game, and load it from a previously saved board. To load the game from a save file or to create a 
new game with a board size different than the standard size (4x4), you must use command line arguments(explained later). 
Controls:
Move using arrow keys
R - rotate board 90 degrees clockwise
Q - quit and save board (to quit without saving just exit the window) All other commands do nothing
Command Line Arguments:
java Gui2048 -o -i
java Gui2048 -s
-o: string output file name(include extension) -i: string input file name(include extension) -s: size of board(int)

Minesweper Game

Runs just like the classic minesweeper game, and by classic I mean classic because the game was designed to be completely no-frills and one must play it in command line, just like games were played outside of arcades in the late '70s and early '80s.
Game implemented entirely in C. Controls should be included in the comments of the mine.c file.
