//Eric Gelphman
//cse8bwabz
//A12689271
//February 5, 2016
//Board.java

//Name: Board
//Purpose: To represent a 2048 game board and has methods to implement game features
import java.util.*;
import java.io.*;

public class Board {
    public final int NUM_START_TILES = 2;//Number of starting tiles
    public final int TWO_PROBABILITY = 90;//Probability of a new 2 tile to be generated
    public final int GRID_SIZE;//Size of board


    private final Random random;//Random number generator
    private int[][] grid;//The actual board
    private int score;

    /*Name: Board constructor
    Purpose: To create a new board
    Parameters: int boardSize = size of board
    Random random = random number generator
    Return: None
    */
    public Board(int boardSize, Random random) {
        this.random = random;
        GRID_SIZE = boardSize;
        this.score = 0;
        this.grid = new int[GRID_SIZE][GRID_SIZE];
        for(int i = 0; i < NUM_START_TILES; i++)//Adding random starting tiles
          addRandomTile();
    }

    /*Name: Board constructor
    Purpose: To construct a new board based on a save file
    Paramters: String inputBoard = name of save File
    Random random = random number generator
    Return: None
    */
    public Board(String inputBoard, Random random) throws IOException {
        if(isInputFileCorrectFormat(inputBoard))
        {
          File setup = new File(inputBoard);
          Scanner input = new Scanner(setup);
          GRID_SIZE = input.nextInt();//Initializing final and instance variables
          this.score = input.nextInt();
          this.grid = new int[GRID_SIZE][GRID_SIZE];
          this.random = random;
          for(int r = 0; r < GRID_SIZE; r++)//Initializing board
          {
            for(int c = 0; c < GRID_SIZE; c++)
            {
              grid[r][c] = input.nextInt();
            }
          }
        }
        else//Save file invalid, instead creates brand new board
        {
          System.out.print("The save file is not in the correct format, ");
          System.out.print("Creating new board with default size 4");
          GRID_SIZE = 4;
          this.score = 0;
          this.grid = new int[GRID_SIZE][GRID_SIZE];
          this.random = random;
          for(int i = 0; i < NUM_START_TILES; i++)
            addRandomTile();
        }
    }

    /*Name: saveBoard method
    Purpose: To save the board setup to a file
    Paramters: String outputBoard = name of save file
    Return: None
    */
    public void saveBoard(String outputBoard) throws IOException {
      java.io.File destination = new java.io.File(outputBoard);
      java.io.PrintWriter output = new java.io.PrintWriter(destination);
      output.println(GRID_SIZE);//Writing size and score to save file
      output.println(score);
      for(int r = 0; r < GRID_SIZE; r++)//Writing tiles on board to save file
      {
        for(int c = 0; c < GRID_SIZE; c++)
        {
          output.print(grid[r][c] + " ");
        }
        output.println();
      }
      output.close();
    }

    /*Name: addRandomTile method
    Purpose: To add a new tile (a 2 or 4) to a random empty space on the board
    Paramters: None
    Return: None
    */
    public void addRandomTile() {
      ArrayList<int[]> emptySpaces = new ArrayList<int[]>();/*Array of size
      2 of empty spaces, first cell is row position, second cell is column
      position*/
      for(int row = 0; row < GRID_SIZE; row++)
      {
        for(int col = 0; col < GRID_SIZE; col++)
        {
          if(this.grid[row][col] == 0)//If space is empty
          {
            int[] holder = new int[2];
            holder[0] = row;//First cell is row position
            holder[1] = col;//Second cell is column position
            emptySpaces.add(holder);
          }
        }
      }
      if(emptySpaces.size() > 0)//If there are empty spaces
      {
        int rnJesus = random.nextInt(emptySpaces.size());//Random empty space
        int[] emptySpace = emptySpaces.get(rnJesus);
        int rnMuhammed = random.nextInt(100);/*Because other religions need to
        be represented - #MMWGlobalCitizenMeetsCompSci. Actually being an
        engineer in ERC kind of blows because of all the GE requirements*/
        //Randomly generates number between 0 and 99
        if(rnMuhammed < TWO_PROBABILITY)//90% probability of new tile being a 2
          grid[emptySpace[0]][emptySpace[1]] = 2;
        else//Tile is a 4
          grid[emptySpace[0]][emptySpace[1]] = 4;
      }
    }

    /*Name: rotate method
    Purpose: To totate the board 90 degrees clockwise if rotateClockwise = true
    or rotate the board 90 degrees counterclockwise otherwise
    Paramters: boolean RotateClockwise = true for clockwise rotation, false otherwise
    Return: None
    */
    public void rotate(boolean rotateClockwise) {
      int[][] temp = new int[GRID_SIZE][GRID_SIZE];//Saving original positions
      for(int r = 0; r < GRID_SIZE; r++)
      {
        for(int c = 0; c < GRID_SIZE; c++)
          temp[r][c] = grid[r][c];
      }
      if(rotateClockwise == true)
      {
        for(int r = 0; r < GRID_SIZE; r++)
        {
          for(int c = 0; c < GRID_SIZE; c++)
            grid[r][c] = temp[GRID_SIZE - 1 - c][r];
        }

      }
      else//Rotate counter-clockwise
      {
        for(int r = 0; r < GRID_SIZE; r++)
        {
          for(int c = 0; c < GRID_SIZE; c++)
            grid[r][c] = temp[c][GRID_SIZE - 1 - r];
        }
      }
    }

    /*Name: isInputFileCorrectFormat method
    Purpose: Determines if input file is in correct format
    Paramters: String input file = save file to be verified
    Return: Returns true if input file is in correct format, returns false otherwise
    */
    public static boolean isInputFileCorrectFormat(String inputFile) {
        try {
            Scanner input = new Scanner(new File(inputFile));
            int numGridCells = 0;//Number of cells in grid 16 for GRID_SIZE of 4
            if(input.hasNextInt())//If first argument is an integer(board size)
            {
              int boardSize = input.nextInt();//GRID_SIZE
              numGridCells = (int)(Math.pow(boardSize, 2));
              if(input.hasNextInt())//If second argument is an integer(score)
              {
                int score = input.nextInt();/*Actual value not needed, only to
                move counter forward 1*/
                int numGridValues = 0;//Number of values representing tiles file has
                while(input.hasNextInt())//While arguments are integers
                {
                  int number = input.nextInt();
                  if(number >= 0)
                  {
                    if(number == 0 || (Math.log(number) / Math.log(2) == (int)(Math.log(number) / Math.log(2))))
                      numGridValues++;//If number is positive and is zero or a power of 2
                  }
                }
                if(numGridValues == numGridCells)/*If the number of values representing
                tiles in file equals to board size from first argument in file squared
                */
                  return true;
              }
            }
            return false;//Else return false
        } catch (Exception e) {
            return false;
        }
    }

    /*Name: move method
    Purpose: To move the game tiles
    Parameters: Direction direction = direction of move
    Return: Returns true if the move was successful, false if it was unsuccessful
    */
    public boolean move(Direction direction)
    {
      if(direction.equals(Direction.UP))//Move up
      {
        moveUp();
        return true;
      }
      /*Note: The method rotates the board so all moves are up. The method then
      rotates the board back to its original orientation after the move is
      completed. This makes the algorithm more efficient.
      */
      else if(direction.equals(Direction.DOWN))//Move down
      {
        rotate(true);//Rotating board 180 degrees clockwise
        rotate(true);
        moveUp();//Executing move
        rotate(false);//Rotating board back to original position
        rotate(false);
        return true;
      }
      else if(direction.equals(Direction.LEFT))//Move left
      {
        rotate(true);//Rotating board 90 degrees clockwise
        moveUp();
        rotate(false);
        return true;
      }
      else if(direction.equals(Direction.RIGHT))//Move right
      {
        rotate(false);//Rotating board 90 degrees counterclockwise
        moveUp();
        rotate(true);
        return true;
      }
      return false;
    }

    /*Name: moveUp method
    Purpose: Move the board tiles up. This is the helper method that actually
    does the moving
    Parameters: None
    Return: None
    */
    public void moveUp()
    {
      for(int col = 0; col < GRID_SIZE; col++)
      {
        for(int row = 0; row < GRID_SIZE - 1; row++)
        {
          if((grid[row][col] != 0 && grid[row + 1][col] != 0) && (grid[row][col] == grid[row + 1][col]))
          /* if two nonzero tiles that are next to each other (one on top and the other
          on bottom) are equal in value*/
          {
            int sum = grid[row][col] + grid[row + 1][col];//Summing tiles
            grid[row][col] = sum;//Tile goes on top
            grid[row + 1][col] = 0;//Bottom tile is zero
            score += sum;//Increasing score
          }
        }
        shiftUp(col);//Shifting all tiles upward, if there is space
      }
    }

    /*Name: shiftUp method
    Purpose: To move tiles up if there is space (if nonzero tiles are below zero
    tiles, the tiles move up until another non-zero tile is above them)
    Parameters: int col = column of grid
    Return: None
    */

    public void shiftUp(int col)
    {
      ArrayList<Integer> tiles = new ArrayList<Integer>(GRID_SIZE);/* ArrayList
      of nonzero tiles in row*/
      for(int row = 0; row < GRID_SIZE; row++)//Finding non-zero tiles in column
      {
        if(grid[row][col] > 0)
          tiles.add(grid[row][col]);
      }
      if(tiles.isEmpty() == false)//If there are nonzero tiles in column
      {
        for(int row = 0; row < GRID_SIZE; row++)//Removing all tiles in column
          grid[row][col] = 0;
        for(int row = 0; row < tiles.size(); row++)/*Adding tiles to column in
        correct order*/
          grid[row][col] = tiles.get(row);
      }
    }

    /*Name: isGameOver method
    Purpose: To check to see if the game is over
    Parameters: None
    Return: Returns true if game is over, false if game is not over
    */
    public boolean isGameOver()
    {
      if(canMove(Direction.UP) == false && canMove(Direction.DOWN) == false && canMove(Direction.LEFT) == false && canMove(Direction.RIGHT) == false)
      //If a valid move is possible
          return true;
      else//If there are no valid moves possible
          return false;
    }

    /*Name: canMove method
    Purpose: To check if a valid move is possible
    Parameters: Direction direction = direction to check for possible moves
    Return: Returns true if a valid move in the specified direction is possible,
    return false if a valid move in the specified direction is impossible
    */
    public boolean canMove(Direction direction)
    {
      boolean result = false;
      if(direction.equals(Direction.UP))//Checking up
      {
        if(canMoveUp())
          result = true;
      }
      /*Note: This method functions in a similar way to the move method in that
      all checks are performed in the up direction. The board is rotated so
      each check is upwards. After each check is performed, the board is rotated
      back to its original orientation. As mentioned earlier, this makes the
      algorithm*/
      else if(direction.equals(Direction.DOWN))//Checking down
      {
        rotate(true);//Rotating board 180 degrees clockwise
        rotate(true);
        if(canMoveUp())//If a valid move is possible
          result = true;
        rotate(false);//Rotating board back to original orientation
        rotate(false);
      }
      else if(direction.equals(Direction.LEFT))
      {
        rotate(true);//Rotating board 90 degrees clockwise
        if(canMoveUp())
          result = true;
        rotate(false);
      }
      else if(direction.equals(Direction.RIGHT))
      {
        rotate(false);//Rotating board 90 degrees counterclockwise
        if(canMoveUp())
          result = true;
        rotate(true);
      }
      return result;
    }

    /*Name: canMoveUp method
    Purpose: Does the actual checking if a valid move is possible
    Parameters: None
    Return: None
    */
    public boolean canMoveUp()
    {
      for(int col = 0; col < GRID_SIZE; col++)
      {
        for(int row = GRID_SIZE - 1; row > 0; row--)
        {
          int top = grid[row - 1][col];
          int bottom = grid[row][col];
          if(bottom != 0)
          {
            if(top == 0 || top == bottom)/*If the cell in the grid above a
            nonzero tile is 0 or equal in value to the tile*/
              return true;//Valid move found
          }
        }
      }
      return false;//No valid moves
    }

    // Return the reference to the 2048 Grid
    public int[][] getGrid() {
        return grid;
    }

    // Return the score
    public int getScore() {
        return score;
    }

    public int getSize()
    {
      return GRID_SIZE;
    }

    //toString method
    @Override
    public String toString() {
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", score));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++)
                outputString.append(grid[row][column] == 0 ? "    -" :
                        String.format("%5d", grid[row][column]));

            outputString.append("\n");
        }
        return outputString.toString();
    }
}
