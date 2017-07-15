/*Eric Gelphman
 * ee15fjx
 * A12689271
 * November 15, 2016
 * Homework 8 Problem 2*/

 #include <stdio.h>
 #include <stdlib.h>
 #include <stdbool.h>

 //Checks to see if a given location is in bounds
 bool inBounds(int row, int col, int size)
 {
    if(row < 0 || row >= size || col < 0 || col >= size)//If not in bounds
    {
        return false;
    }
    return true;//In bounds
 }

 //Prints dashes that divide the columns of the board
 void printColumnDividers(int size, int board[][size])
 {
   int i;
   printf("  ");
   for(i = 0; i < size; i += 1)//Top of board
   {
       printf("  - ");
   }
   printf("\n");
 }

 //Prints the game board. If showmines == true, also shows locations of mines
 void printBoard(int size, int board[][size], bool showMines)
 {
    int i, j;//Set-up
    printf("  ");
    for(i = 0; i < size; i += 1)//Column numbers
    {
        printf("  %d ", i);
    }
    printf("\n");
    printColumnDividers(size, board);
    for(i = 0; i < size; i += 1)
    {
        printf("%d ", i);//Row numbers
        for(j = 0; j < size; j += 1)
        {
          if(board[i][j] != -10)//Not a mine
          {
            if(board[i][j] < 0)//Space not visited yet
            {
                printf("|   ");
            }
            else//Space visited
            {
                printf("| %d ", board[i][j]);
            }
          }
          else if(board[i][j] == -10 && showMines == false)
          {
              printf("|   ");
          }
          else if(showMines == true && board[i][j] == -10)//If mines are to be shown
          {
            printf("| * ");
          }
        }
        printf("|\n");
        printColumnDividers(size, board);
    }
 }

 // Initializes board with mines
 void initializeBoard(int size, int board[][size], int numMines)
 {
    int i, row, col, rseed;
    for(row = 0; row < size; row += 1)
    {
      for(col = 0; col < size; col += 1)
      {
          board[row][col] = -1;
      }
    }
    i = 0;
    printf("Random seed: ");
    scanf("%d", &rseed);
    srand(rseed);
    while(i < numMines)//Filling with mines
    {
        row = rand() % size;
        col = rand() % size;
        if(board[row][col] == -10)
        {
            continue;
        }
        else
        {
            board[row][col] = -10;
            i += 1;
        }
    }
    if(numMines < size * size)
    {
        printBoard(size, board, false);
    }
    else if(numMines == size * size)
    {
        printBoard(size, board, true);
    }
 }

 //Checks to see if a move is valid
 bool validMove(int size, int board[][size], int row, int column)
 {
    if(inBounds(row, column, size))//If in bounds, move is valid
    {
        return true;
    }
    return false;//Out of bounds, not valid
 }

 //Returns number of mines adjacent to a given location
 int numMinesNear(int size, int board[][size], int row, int column)
 {
    int r, c, numMines;
    numMines = 0;
    for(r = row - 1; r <= row + 1; r += 1)//Iterate through all 8 spaces around location
    {
        for(c = column - 1; c <= column + 1; c += 1)
        {
            //If in bounds, search location != given location, and a mine is found
            if(inBounds(r, c, size) && board[r][c] == -10)
            {
                numMines += 1;
            }
        }
    }
    return numMines;
 }

 int main()
 {
    int size, nMines, r, c, nSpacesLeft;
    printf("Board size [1 to 10]: ");
    scanf("%d", &size);
    int board[size][size];
    nSpacesLeft = size * size;
    printf("Number of mines [0 to %d]: ", size * size);
    scanf("%d", &nMines);
    initializeBoard(size, board, nMines);
    int mineHit = 0;//If mine is hit
    int nr = 0;//Keep track of number of game rounds
    while(!mineHit && !(nSpacesLeft == nMines))
    {
        printf("Row and column [0 to %d] each: ", size - 1);
        scanf("%d %d", &r, &c);
        if(inBounds(r, c, size))
        {
            if(board[r][c] == -1)
            {
                board[r][c] = numMinesNear(size, board, r , c);
                nSpacesLeft -= 1;
                if(nSpacesLeft == nMines)
                {
                    printBoard(size, board, true);
                }
                else
                {
                    printBoard(size, board, false);
                }
            }
            else if(board[r][c] == -10)
            {
                mineHit = 1;
                printBoard(size, board, true);
            }
            else
            {
                printf("Already cleared!\n");
            }
        }
        else
        {
            printf("Out of bounds!\n");
        }
        nr += 1;
    }
    if(mineHit)//Player loses
    {
        printf("You lost!\n");
    }
    else if(nSpacesLeft == nMines)//Player wins
    {
        printf("You won!\n");
    }
    return 0;
 }
