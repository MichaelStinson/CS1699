import java.util.*;

public class DriverClass // directly or indirectly calls every method and every class:: User interaction class
{
    genericQueue<Integer> que;
    Scanner input;
    String command;
    mazeCreation maze;
    reUse reUse;

    public DriverClass()
    {
        que = new genericQueue();
        reUse = new reUse();
        maze = new mazeCreation();
        input = new Scanner(System.in);
    }

    public String getCommand()
    {
        System.out.print("enter command: ");
        return input.next();
    }

    public void run() // user interaction loop
    {
        showMenu();	
        command = getCommand();
        while (!command.equals("exit"))
        {

            if (command.equals("make"))
            {
                String n = input.next();
                String m = input.next();
                if (reUse.isInt(n) == true && reUse.isInt(m) == true)
                    maze.makeRandomMaze(Integer.parseInt(n), Integer.parseInt(m));
                else
                    System.out.println("Invalid row/column combination. Ensure both values given are integers and try again. Previous maze is unchanged.");
            }
            else if (command.equals("read"))
            {
                String mazeFile = input.next();
                maze.readMaze(mazeFile);
            }
            else if (command.equals("search"))
                maze.traverseMaze();
            else if (command.equals("show"))
                maze.autoDisplay();
            else if (command.equals("help"))
                showMenu();
            else 
                System.out.println("Command not found. Try again.");
            command = getCommand();
        }
    }


    public void showMenu() //command options
    {
        System.out.println();
        System.out.println("make <n> <m>              // make a new randomly generated maze with n rows and m columns");
        System.out.println("read <filename>          // read a maze from a text file");
        System.out.println("search                  // find a path through the current maze");
        System.out.println("show                   // display the current maze on the screen");
        System.out.println("help                  // display menu");
        System.out.println("exit                 // close program");
        System.out.println();
    }


    public static void main(String args[])
    {
        DriverClass driver = new DriverClass();
        driver.run();

    }
}
