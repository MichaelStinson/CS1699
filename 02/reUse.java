import java.util.*;
import java.io.*;

public class reUse
{

    Scanner reader;
    boolean opened;

    public void readFile(String fileName)
    {
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
            while (reader.hasNext())
            {
                //PROGRAM SPECIFIC CODE
                //PROGRAM SPECIFIC CODE
            }
            System.out.println();
            reader.close();
        }
        else 
            System.out.println("File could not be opened. Try again.");
    }


    public int[] sort(int []input)
    { 
        for(int i=0;i<input.length;i++) 
            for(int j=i+1;j<input.length;j++)
            { 
                if(input[i]>input[j])
                { 
                    int temp = input[i]; //swap the elements 
                    input[i] = input[j]; 
                    input[j] = temp; 
                } 
            }
        return input;
    }



    public boolean isInt(String item)
    {
        int intCount = 0;
        char chars[] = item.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            char currentChar = chars[i];
            if (Character.isDigit(currentChar))
                intCount = intCount + 1;
        }
        if (intCount == chars.length)
            return true;
        else
            return false;
    }


    public String removeTrailing(String inString, char aChar)
    {
        int i;
        int length = inString.length();
        for (i = length; --i >= 0; )
        {
            if (inString.charAt(i) != aChar) 
            {
                break;
            }
        }
        i++;
        if (length == i) 
        {
            return inString;
        }
        return inString.substring(0, i);
    }
}








