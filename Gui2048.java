//Eric Gelphman
//cse8bwabz
//A12689271
//March 4, 2016
//Gui2048


import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;
//This class enables the creation of a GUI to play the game 2048
public class Gui2048 extends Application
{
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    private static final int TILE_WIDTH = 106;
    private static final int DEFAULT_SIZE = 4;
    private static final int TEXT_SIZE_LOW = 55; // Low value tiles (2,4,8,etc)
    private static final int TEXT_SIZE_MID = 45; // Mid value tiles
                                                 //(128, 256, 512)
    private static final int TEXT_SIZE_HIGH = 35; // High value tiles
                                                  //(1024, 2048, Higher)

    // Fill colors for each of the Tile values
    private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
    private static final Color COLOR_2 = Color.rgb(238, 228, 218);
    private static final Color COLOR_4 = Color.rgb(237, 224, 200);
    private static final Color COLOR_8 = Color.rgb(242, 177, 121);
    private static final Color COLOR_16 = Color.rgb(245, 149, 99);
    private static final Color COLOR_32 = Color.rgb(246, 124, 95);
    private static final Color COLOR_64 = Color.rgb(246, 94, 59);
    private static final Color COLOR_128 = Color.rgb(237, 207, 114);
    private static final Color COLOR_256 = Color.rgb(237, 204, 97);
    private static final Color COLOR_512 = Color.rgb(237, 200, 80);
    private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
    private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
    private static final Color COLOR_OTHER = Color.BLACK;
    private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

    private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242);
                        // For tiles >= 8

    private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101);
                       // For tiles < 8
    private GridPane pane;//Pane in which game elements are stored
    private StackPane background;//Holds pane and overlay(for Game Over screen)
    private StackPane overlay;//For Game Over screen
    //If you haven't seen The Matrix, I would highly recommend it, as it is a
    //dope film
    private Rectangle[][] neo;//Game Tiles
    private Text[][] morpheus;//Text on tiles(numbers)
    private String outputFileName;//File in which save game is to be stored
    private int tileWidth;//Width of tile, changes based on size of grid


    /*Name: start method
    Purpose: Sets up the GUI at the start of each game
    Parameters: Stage primaryStage = stage which holds the game panes
    Return: None
    */
    @Override
    public void start(Stage primaryStage)
    {
        // Process Arguments and Initialize the Game Board
        processArgs(getParameters().getRaw().toArray(new String[0]));
        // Create the pane that will hold all of the visual objects
        //Initializing instance variables
        outputFileName = "SaveGame";
        neo = new Rectangle[board.getSize()][board.getSize()];
        morpheus = new Text[board.getSize()][board.getSize()];
        background = new StackPane();
        background.setAlignment(Pos.CENTER);
        pane = new GridPane();
        tileWidth = determineTileWidth();
        setUpGridPane();
        overlay = new StackPane();
        overlay.setAlignment(Pos.CENTER);
        //Adding panes to background pane
        background.getChildren().addAll(pane, overlay);
        Scene scene = new Scene(background);
        //Registering event handler
        scene.setOnKeyPressed(new KeyHandler());
        primaryStage.setTitle("2048");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*Name: determineTileWidth method
    Purpose: Changes tile width based on size of grid
    Parameters: None
    Return: Width of tile (int)
    */
    public int determineTileWidth()
    {
      int boardSize = board.getSize();
      int answer = TILE_WIDTH;
      if(boardSize != DEFAULT_SIZE)//If boardSize != 4
      {
        double x = ((double)TILE_WIDTH) * (DEFAULT_SIZE / (double)boardSize);
        answer = (int)x;
        if(answer < (TEXT_SIZE_LOW + 1))//Min size must be greater than text
          answer = TEXT_SIZE_LOW + 1;
      }
      return answer;
    }

    /*Name: setUpGridPane method
    Purpose: to set up the elements in the grid pane, which holds the game
    elements, at the beginning of each game
    Parameters: None
    Return: None
    */
    public void setUpGridPane()
    {
      pane.setAlignment(Pos.CENTER);
      pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
      pane.setStyle("-fx-background-color: rgb(187, 173, 160)");
      // Set the spacing between the Tiles
      pane.setHgap(15);
      pane.setVgap(15);
      setUpHeader();//Creating score and title
      makeTiles();//Making tiles
      for(int row = 0; row < board.getSize(); row++)
      {
        for(int col = 0; col < board.getSize(); col++)
        {
          //Adding tiles to grid
          pane.add(neo[row][col], col, row + 1);
          if(!morpheus[row][col].getText().equals("0"))// Only adds text if > 0
            pane.add(morpheus[row][col], col, row + 1);
        }
      }
    }

    /*Name: setUpHeader method
    Purpose: To display the game title(2048) and score
    Parameters: None
    Return: None
    */
    public void setUpHeader()
    {
      //Displaying title
      Text title = new Text();
      title.setText("2048");
      title.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 40));
      title.setFill(Color.BLACK);
      pane.add(title, 0, 0);
      pane.setColumnSpan(title, (board.getSize() / 2));//Title and score span
      //multiple columns
      GridPane.setHalignment(title, HPos.CENTER);
      //Displaying score
      Text score = new Text();
      score.setText("Score: " + board.getScore());
      score.setFont(Font.font("Arial", FontWeight.BOLD, 40));
      score.setFill(Color.BLACK);
      pane.add(score, (board.getSize() / 2), 0);
      pane.setColumnSpan(score, (board.getSize() / 2));
      GridPane.setHalignment(title, HPos.CENTER);
    }

    /*Name: makeTiles method
    Purpose: To make the game tiles (rectangles and text)
    Parameters: None
    Return: None
    */
    public void makeTiles()
    {
      for(int row = 0; row < board.getSize(); row++)
      {
        for(int col = 0; col < board.getSize(); col++)
        {
          //Creates tiles based on board positions
          int value = board.getGrid()[row][col];
          Rectangle rekt = new Rectangle();
          rekt.setWidth(tileWidth);
          rekt.setHeight(tileWidth);
          rekt.setFill(determineColor(value));
          neo[row][col] = rekt;//Adding rectangles to matrix
          morpheus[row][col] = makeText(value);//Adding text to matrix
          GridPane.setHalignment(morpheus[row][col], HPos.CENTER);
        }
      }
    }

    /*Name: makeText method
    Purpose: Creates text (numbers) for tile rectangles based on board values
    Parameters: int value = board value(number to go on rectangle)
    Return: Text to be displayed on GUI
    */
    public Text makeText(int value)
    {
      Text text1 = new Text();
      text1.setText("" + value);
      int fontSize = determineFontSize(value);
      text1.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
      text1.setFill(Color.WHITE);
      return text1;
    }

    /*Name: updateTiles method
    Purpose: To update the positions of the tiles after each move
    Parameters: None
    Return: None
    */
    public void updateTiles()
    {
      pane.getChildren().clear();//Clears GridPane and creates a new one each
      //move
      setUpHeader();//Creates header
      makeTiles();//Making tiles based on new board positions after move
      for(int row = 0; row < board.getSize(); row++)
      {
        for(int col = 0; col < board.getSize(); col++)
        {
          pane.add(neo[row][col], col, row + 1);
          if(!morpheus[row][col].getText().equals("0"))//Only adds text if > 0
            pane.add(morpheus[row][col], col, row + 1);
        }
      }
    }

    /*Name: determineColor method
    Purpose: To determine the color of the tile based on its value
    Parameters: int value = value of tile
    Return: Color of tile
    */
    public Color determineColor(int value)
    {
      if(value == 0)
        return COLOR_EMPTY;
      else if (value == 2)
        return COLOR_2;
      else if(value == 4)
        return COLOR_4;
      else if(value == 8)
        return COLOR_8;
      else if(value == 16)
        return COLOR_16;
      else if(value == 32)
        return COLOR_32;
      else if(value == 64)
        return COLOR_64;
      else if(value == 128)
        return COLOR_128;
      else if(value == 256)
        return COLOR_256;
      else if(value == 512)
        return COLOR_512;
      else if(value == 1024)
        return COLOR_1024;
      else if(value == 2048)
        return COLOR_2048;
      else
        return COLOR_OTHER;
    }

    /*Name: determineFontSize method
    Purpose: to determine font size of tile based on tile value
    Parameters: int value = value of tile
    Return: Size of font (int)
    */
    public int determineFontSize(int value)
    {
      if(value < 8)
        return TEXT_SIZE_LOW;
      else if(value > 8 && value <= 512)
        return TEXT_SIZE_MID;
      else
        return TEXT_SIZE_HIGH;
    }

    /*Name: displayGameOver method
    Purpose: To display the Game Over screen when the game is over
    Parameters: None
    Return: None
    */
    public void displayGameOver()
    {
      //Creates rectangle that fills whole pane with text "GAME OVER"
      Rectangle shrekt = new Rectangle();
      shrekt.setHeight(pane.getHeight());
      shrekt.setWidth(pane.getWidth());
      //Rectangle is semi-transparent
      shrekt.setFill(COLOR_GAME_OVER);
      Text message = new Text();
      message.setText("GAME OVER");
      message.setFont(Font.font("Arial", FontWeight.BOLD, 75));
      message.setFill(Color.BLACK);
      overlay.getChildren().addAll(shrekt, message);
    }

    //This inner EventHandler class reads gameplay inputs from the keyboard to
    //execute moves
    private class KeyHandler implements EventHandler<KeyEvent>
    {
      private boolean gameOver = false;//False if game is not over, true if
      //game is over

      @Override
      public void handle(KeyEvent e)
      {
        if(gameOver == false)//If game is not over
        {
          KeyCode controlInput = e.getCode();
          switch(controlInput)
          {
            case UP: if(board.canMove(Direction.UP))//Move up
                     {
                       System.out.println("move up");
                       board.move(Direction.UP);
                       board.addRandomTile();
                       updateTiles();//Updates tiles after each move
                     }
                     else
                       System.out.println("Cannot Move Up");
                     break;
            case DOWN: if(board.canMove(Direction.DOWN))//Move down
                       {
                         System.out.println("move down");
                         board.move(Direction.DOWN);
                         board.addRandomTile();
                         updateTiles();
                       }
                       else
                         System.out.println("Cannot Move Down");
                       break;
            case LEFT: if(board.canMove(Direction.LEFT))//Move left
                       {
                         System.out.println("move left");
                         board.move(Direction.LEFT);
                         board.addRandomTile();
                         updateTiles();
                       }
                       else
                         System.out.println("Cannot Move Left");
                       break;
            case RIGHT: if(board.canMove(Direction.RIGHT))//Move right
                        {
                          System.out.println("move right");
                          board.move(Direction.RIGHT);
                          board.addRandomTile();
                          updateTiles();
                        }
                        else
                          System.out.println("Cannot Move Right");
                        break;
            case R: board.rotate(true);//Rotates board clockwise
                    System.out.println("Board rotated clockwise");
                    updateTiles();
                    break;
            case Q: try//Quit and save game
                    {
                      board.saveBoard(outputFileName);//Save game
                      gameOver = true;
                      System.out.println("Game saved in " + outputFileName);
                    }
                    catch(IOException i)
                    {
                      System.out.println("An Error Occurred with saveBoard");
                    }
                    break;
            default: System.out.println("Invalid command!, valid commands are:");
                     //If an invalid command is entered
                     printControls();
                     break;
          }
          gameOver = board.isGameOver();
        }
        else
        {
          displayGameOver();//Display Game Over scren
          System.out.println("Game Over");
        }
      }

      /*Name: printControls method
      Purpose: Prints valid user inputs if user enters an invalid input
      Parameters: None
      Return: None
      */
      public void printControls()
      {
        System.out.println("Controls: Arrow Keys");
        System.out.println("Up Key - Move Up");
        System.out.println("Down Key - Move Down");
        System.out.println("Left Key - Move Left");
        System.out.println("Right Key - Move Right");
        System.out.println("q - Quit and Save Board");
        System.out.println("r - rotate board 90 degrees clockwise");
      }
    }
    /** DO NOT EDIT BELOW */

    // The method used to process the command line arguments
    private void processArgs(String[] args)
    {
        String inputBoard = null;   // The filename for where to load the Board
        int boardSize = 0;          // The Size of the Board

        // Arguments must come in pairs
        if((args.length % 2) != 0)
        {
            printUsage();
            System.exit(-1);
        }

        // Process all the arguments
        for(int i = 0; i < args.length; i += 2)
        {
            if(args[i].equals("-i"))
            {   // We are processing the argument that specifies
                // the input file to be used to set the board
                inputBoard = args[i + 1];
            }
            else if(args[i].equals("-o"))
            {   // We are processing the argument that specifies
                // the output file to be used to save the board
                outputBoard = args[i + 1];
            }
            else if(args[i].equals("-s"))
            {   // We are processing the argument that specifies
                // the size of the Board
                boardSize = Integer.parseInt(args[i + 1]);
            }
            else
            {   // Incorrect Argument
                printUsage();
                System.exit(-1);
            }
        }

        // Set the default output file if none specified
        if(outputBoard == null)
            outputBoard = "2048.board";
        // Set the default Board size if none specified or less than 2
        if(boardSize < 2)
            boardSize = 4;

        // Initialize the Game Board
        try{
            if(inputBoard != null)
                board = new Board(inputBoard, new Random());
            else
                board = new Board(boardSize, new Random());
        }
        catch (Exception e)
        {
            System.out.println(e.getClass().getName() +
                               " was thrown while creating a " +
                               "Board from file " + inputBoard);
            System.out.println("Either your Board(String, Random) " +
                               "Constructor is broken or the file isn't " +
                               "formated correctly");
            System.exit(-1);
        }
    }

    // Print the Usage Message
    private static void printUsage()
    {
        System.out.println("Gui2048");
        System.out.println("Usage:  Gui2048 [-i|o file ...]");
        System.out.println();
        System.out.println("  Command line arguments come in pairs of the "+
                           "form: <command> <argument>");
        System.out.println();
        System.out.println("  -i [file]  -> Specifies a 2048 board that " +
                           "should be loaded");
        System.out.println();
        System.out.println("  -o [file]  -> Specifies a file that should be " +
                           "used to save the 2048 board");
        System.out.println("                If none specified then the " +
                           "default \"2048.board\" file will be used");
        System.out.println("  -s [size]  -> Specifies the size of the 2048" +
                           "board if an input file hasn't been");
        System.out.println("                specified.  If both -s and -i" +
                           "are used, then the size of the board");
        System.out.println("                will be determined by the input" +
                           " file. The default size is 4.");
    }
}
