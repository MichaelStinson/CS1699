import java.util.*;
import java.io.*;

public class mazeCreation //calls genericQueue and reUse
{

    Scanner reader;
    String[][] maze, searchMaze;
    String[] visited;
    boolean okayToTraverse;
    int cols, rows, rowPos, colPos;
    reUse reUse = new reUse();
    genericQueue<String> que;

    public void readMaze(String fileName) //saves maze from file to 2d array "maze"
    {
        okayToTraverse = true;
        boolean opened, setSize = false, wrongSize = false, blockReached = false;
        int rowVerify = 0, colVerify = 0;
        File infile = new File(fileName);
        try
        {
            reader = new Scanner(infile);
            opened = true;
        }
        catch (FileNotFoundException e)
        {
            opened = false;
        }
        if (opened == true)
        {
            String first = reader.next(), second = reader.next();
            if (reUse.isInt(first) == true && reUse.isInt(second) == true)
            {
                rows = Integer.parseInt(first);
                cols = Integer.parseInt(second);
                maze = new String[rows][cols];
                setSize = true;
            }
            String currentLine = "";
            int currentRow = 0, currentCol = 0;
            while (reader.hasNext() && setSize == true)
            {
                String currentString = reader.next();
                currentLine = currentLine + currentString + " "; // concatinate string untill the next if-statement stops the process

                if (currentLine.length() > 2*cols + 2)   // if true, then array will be too small to be filled completely. IE: the user gave incorrect
                {					// row/col values. Breaks loop here and is dealt with below.
                    wrongSize = true;
                    break;
                }
                else if (currentLine.length() == (2*cols + 2)) // "2*cols + 2" is the number of characters contained in a maze of any size 
                {					      // | |x| | | <<-- 4 cols, 4*2 + 1 = total chars = 9. the reason that it is + 2 and not + 1
                    // is due to the concatination of one extra white space 
                    for (int i = 1; i < 2*cols + 1; i = i + 2)
                    {
                        char item = currentLine.charAt(i);
                        if (currentRow >= rows || currentCol >= cols)  // if either currentRow or currentCol exceeds its respective fixed array value, then
                            break;                                     // there is a sizing issue, and an error will eventually culminate. This error is SIZE
                        // based, and is caught pre-emptively and delt with below. This ultimately means that 
                        maze[currentRow][currentCol] = Character.toString(item); 
                        currentCol++;
                    }
                    currentRow++; // the current file line means new row 
                    rowVerify = currentRow;
                    colVerify = currentCol; //necisary for comparison after loop has ended, due to the statement "currentCol = 0;"
                    currentCol = 0; //reset colums for new row
                    currentLine = ""; // reset concatination variable for new line in file
                }
            }
            if (colVerify != cols || rowVerify != rows)
                wrongSize = true;
        }
        if (opened == false)
        {
            cols = 0;
            rows = 0;
            maze = new String[rows][cols];
            searchMaze = maze;
            okayToTraverse = false;
            System.out.println("File could not be opened. Ensure maze file exists and is in accessible folder. Maze set to size = 0");
        }
        if (setSize == false && opened != false)
        {
            cols = 0;
            rows = 0;
            maze = new String[rows][cols];
            searchMaze = maze;
            okayToTraverse = false;
            System.out.println("Maze size not found. Ensure maze size is the first item in maze file in format \"<n> <m>\". Maze set to size = 0");
        }
        if (wrongSize == true)
        {
            System.out.println("Given maze does not fit the specified size. Fix maze size and try again (No maze was saved because of size issues. \nFix formatting and try again.) Maze set to size = 0");
            cols = 0;
            rows = 0;
            okayToTraverse = false;
            maze = new String[rows][cols];
            searchMaze = maze;
        }
        if (opened == true)
            reader.close();
        que = new genericQueue();
        searchMaze = new String[rows][cols];
        searchMaze = maze;
    }


    public void makeRandomMaze(int tempRows, int tempCols) // creates random n x m maze saved as 2d array "maze"
    {
        Random randomGen = new Random();
        rows = tempRows;
        cols = tempCols;
        maze = new String[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
            {
                int randomInt = randomGen.nextInt(4); // 1/5 probability of "X"
                if (randomInt == 0 && (i + j) != 0 && (i + j) != (rows + cols - 2)) // Prevents X from being placed at enterance position (0,0)
                    maze[i][j] = "X";
                else
                    maze[i][j] = " ";
            }
        que = new genericQueue();
        searchMaze = new String[rows][cols];
        searchMaze = maze;
        okayToTraverse = true;
    }

    public void toInt(String p) 		// called by traverseMaze to retrieve string formated positions given by "x,y" and saves x to rowPos
    {				       // and y to colPos (thought of as position IN a given column, up down
        char[] c = p.toCharArray();
        int commaPos = 0;
        for (int i = 0; i < p.length(); i++)
        {
            String current = Character.toString(c[i]);
            if (current.equals(","))
                break;
            commaPos++;
        }
        String row = p.substring(0, commaPos);
        String col = p.substring(commaPos + 1,p.length());
        rowPos = Integer.parseInt(row); //saves as int
        colPos = Integer.parseInt(col); //saves as int
    }

    public void traverseMaze() // calls genericQueue to traverse maze, and saves (col + row) to each element 
    {			    // in searchMaze (identical to maze[][]) for eventual drawing of path. This method determins IF there is a path, and
        // backtrackMaze draws the actual path and deletes all left over numbers. 
        if (okayToTraverse == true) // makes sure maze exists to avoid nullpointer errorh
        {
            visited = new String[1000];
            boolean finalPosition = false;
            String position = "0,0";
            que.enque("0,0");
            int N = 0; // N = the sum of the sum of the ordered (x,y) pair. N = (x + y). N = 0 ==> N = (x, y) = x + y = 0 + 0 = 0
            searchMaze[0][0] = Integer.toString(N++);

            while (que.returnTotalNodes() != 0)
            {
                int currentOptions = 0;
                position = que.deque();
                toInt(position); 		 //sets two integer variables, colPos and rowPos, equal to the position poped returned by the que.deque
                if (colPos == (rows - 1) && rowPos == (cols - 1))
                {
                    finalPosition = true;
                    break;
                }
                else 
                {
                    if ((rowPos - 1) >= 0)
                        if(searchMaze[colPos][rowPos - 1].equals(" "))
                        {
                            que.enque((rowPos - 1) + "," + colPos);
                            searchMaze[colPos][rowPos-1] = Integer.toString(N);
                            currentOptions++;
                        }	  
                    if ((rowPos + 1) <= (cols - 1))
                        if(searchMaze[colPos][rowPos + 1].equals(" "))
                        {
                            que.enque((rowPos + 1) + "," + colPos);
                            searchMaze[colPos][rowPos+1] = Integer.toString(N);
                            currentOptions++;
                        }
                    if((colPos - 1) >= 0) 
                        if(searchMaze[colPos - 1][rowPos].equals(" "))
                        {
                            que.enque(rowPos + "," + (colPos - 1));
                            searchMaze[colPos-1][rowPos] = Integer.toString(N);
                            currentOptions++;
                        }
                    if((colPos + 1) <= (rows - 1))
                        if(searchMaze[colPos + 1][rowPos].equals(" "))
                        {
                            que.enque(rowPos + "," + (colPos + 1));
                            searchMaze[colPos+1][rowPos] = Integer.toString(N);
                            currentOptions++;
                        }
                    if (currentOptions >= 1)
                        N++;
                }
            }		//maze fully traversed --> begin printing searched maze to screen w/ coords
            if (finalPosition == true)
            {
                show(backtrackMaze(searchMaze)); 
                System.out.print("VISITED: ");
                for (int i = 0; i < visited.length; i++)
                    if (visited[i] != null)
                        System.out.print("("+visited[i]+") ");
                System.out.println("\n");
                finalPosition = false;
            }
            else
            {
                for (int i = 0; i < rows; i++)
                    for (int j = 0; j < cols; j++)
                        if (reUse.isInt(maze[i][j]) == true)
                            maze[i][j] = " ";
                System.out.println("\nCurrent maze has no solution.\n");
            }
        }
        else
            System.out.println("There is no maze saved to solve. Load or generate a maze and continue.");
    }

    public String[][] backtrackMaze(String[][] array) // starts at the end of the maze and works its way toward 0,0 on a single defined path.
        // it finds this path by the sum of the ordered pair of the matrix position of each element maze
        // cols + rows = #, it checks that each # < current # and if so, moves to that position.
        // process continues untill # = 0. (at this point it is known to be solvable)
    {    
        int row = rows - 1, col = cols - 1, n = 0, current = Integer.parseInt(array[row][col]);
        boolean infiniteLoop = false;
        while (current != 0)
        {
            boolean filled = false, hasMoved = false;
            if ((row + 1) < (rows - 1))
            {
                if (reUse.isInt(array[row + 1][col]) == true)
                {
                    if (Integer.parseInt(array[row + 1][col]) < current) // down -- checks to see if position below will bring you toward origin
                    {
                        current = Integer.parseInt(array[row + 1][col]);
                        visited[n] = row+ ", "+col;
                        n++;
                        array[row][col] = ".";
                        row = row + 1;
                        filled = true;
                        hasMoved = true;
                    }
                }
            }
            if (row - 1 >= 0 && filled == false) // up -- checks to see if position above will bring you toward origin
            {
                if (reUse.isInt(array[row - 1][col]) == true)
                {
                    if (Integer.parseInt(array[row - 1][col]) < current)
                    {
                        current = Integer.parseInt(array[row - 1][col]);
                        array[row][col] = ".";
                        visited[n] = row+ ", "+col;
                        n++;
                        row = row - 1;
                        filled = true;
                        hasMoved = true;
                    }
                }
            }
            if ((col + 1) < (cols - 1) && filled == false) // right -- checks to see if psotion to the right will bring you toward origin
            {
                if (reUse.isInt(array[row][col + 1]) == true)
                {
                    if (Integer.parseInt(array[row][col + 1]) < current)
                    {
                        current = Integer.parseInt(array[row][col + 1]);
                        array[row][col] = ".";
                        col = col + 1;
                        filled = true;
                        hasMoved = true;
                    }
                }
            }
            if ((col - 1) >= 0 && filled == false) // left -- checks to see if position to the left will bring you toward the origin
            {
                if (reUse.isInt(array[row][col - 1]) == true)
                {
                    if (Integer.parseInt(array[row][col - 1]) < current)
                    {
                        current = Integer.parseInt(array[row][col - 1]);
                        array[row][col] = ".";
                        visited[n] = row+ ", "+col;
                        hasMoved = true;
                        n++;
                        col = col - 1;
                    }
                }
            }
            if (hasMoved == false)
            {
                infiniteLoop = true;
                break;
            }
        }
        if (infiniteLoop == true)
            System.out.println("\nA (rare) error has occured, resulting in an infinite loop. There IS a solution to this maze, but the path could not be traced.\n");
        else
        {
            array[0][0] = ".";
            visited[n] = "0,0";
        }
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if (reUse.isInt(array[i][j]) == true)
                    array[i][j] = " ";
        return array;
    }


    public void show(String[][] toDisplay) // prints input maze to screen, logic is self explanitory -- traverses maze 
    {				        // and prints out as a formated maze
        for (int i = 0; i < rows; i++)
        {
            System.out.println();
            for (int j = 0; j < cols; j++)
            {
                if (j == 0)
                    System.out.print("|"+toDisplay[i][j]+"|");
                else 
                    System.out.print(toDisplay[i][j]+"|");
            }
        }
        System.out.println();
        System.out.println();
    }

    public void autoDisplay() //used for printing maze to screen in DriverClass 
    {
        show(maze);
    }
}
