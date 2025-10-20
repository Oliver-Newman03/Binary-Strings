import java.util.LinkedList;
import java.util.Queue;

public class Solution {
    
    // Question 1a
    public static boolean reachable1(String x, String y) 
    {
        /*
        LOGIC:

        x can only be converted to y if they have the same amount of 1's and 0's
        This is because the two operations available conserve the amount of 1's and 0's in the string
        Furthermore, the second operation is the inverse of the first so there is no limitation on the ordering of the String
        By checking they have the same amount of 1's, they will also have the same amount of 0's since every character that is not a 1, has to be a 0

        O(n), 1 for loop
        */
        
        int xunos = 0; //Declaring an integer variable for the amount of 1's in the string x
        int yunos = 0; //Declaring an integer variable for the amount of 1's in the string y
        int length = x.length(); //Getting the length (n) of x - which is also they length of y as stated in the assignment brief

        for (int i = 0; i < length; i++) //Loops through the x String (which is also the same length as the y String)
        {
            if (x.charAt(i) == '1') //If the character at position i in x is a 1
            {
                xunos++; //Increment the amount of 1's in x by 1
            }

            if (y.charAt(i) == '1') //If the character at position i in y is a 1
            {
                yunos++; //Increment the amount of 1's in y by 1
            }
        }
        
        if (xunos == yunos) //If the amount of 1's in x is the same as the amount of 1's in y
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    // Question 1b
    public static int distance1(String x, String y) 
    {
        if (reachable1(x, y)) //If the reachable1 function returns true
        {
            /*
            LOGIC:

            Each 1 in x, needs to move from its current position to a position in y
                The same is true for the 0's, but if the 1's are ordered correctly then so will the 0's
            Each step a 1 has to make to the left or right is 1 operation
            The total amount of steps that needs to be taken is the sum of the differences of the indexes of 1's in x and y

            O(2n) = O(n)
            2 for loops, not nested

            Example:

            x = 00111, y = 10101
            x1pos = [2, 3, 4, 0, 0]
            y1pos = [0, 2, 4, 0, 0]
            steps = (2-0) + (3-2) + (4-4) + (0-0) + (0-0)
            steps = 2 + 1 = 3
            */
            
            int steps = 0; //Variable for how many applications of the operation need to be done
            int[] x1pos = new int[x.length()]; //Integer array to store the positions of 1's in String x
            int[] y1pos = new int[y.length()]; //Integer array to store the positions of 1's in String y
            int xindex = 0; //Variable to record the position of x1pos to input the next value (homemade array append)
            int yindex = 0; //Variable to record the position of y1pos to input the next value (homemade array append)

            for (int i = 0; i < x.length(); i++)
            {
                if (x.charAt(i) == '1') //If there is a 1 at position i in x
                {
                    x1pos[xindex] = i; //Append the position of the 1 to the x1pos array
                    xindex++; //Increment xindex by 1
                }
                
                if (y.charAt(i) == '1') //If there is a 1 at position i in y
                {
                    y1pos[yindex] = i; //Append the position of the 1 to the y1pos array
                    yindex++; //Increment yindex by 1
                }
            }

            for (int j = 0; j < x.length(); j++) 
            {
                steps += (Math.abs(y1pos[j] - x1pos[j])); //Gets the absolute value of the difference in positions at index j and adds it to the total of steps
            }
            
            return steps;
        }
        else //If x cannot convert into y (reachable returns false)
        {
            return -1;
        }
    }

    

    // Question 2

    
    public static int bintoden(String binary) //Function that returns the binary String to a denary integer
    //This will be used to store the level of each unique binary String permtation of length n in the integer array 'level', using their denary value as their index
    //Time complexity of O(n) as it loops through all n elements of the String
    {
        int length = binary.length(); //Gets the length of the String
        int denary = 0; //Variable to store and eventually return the denary value of the binary String
        int digit = 0; //Variable that records what the variable at the current position of the String is, default set to 0

        for (int i = length-1; i >= 0; i--) //Loops through the String, starting at the end (where the binary value corresponds to denary 1)
        {
            if (binary.charAt(i) == '1') //Checks if the character at the current position in the String is a '1'
            {
                digit = 1; 
            }
            else //If the character at the current position in the String is a '0'
            {
                digit = 0;
            }
            
            denary += (int) (((Math.pow(2,(length-1-i))))*digit); //Increments the current denary value by (2^(length-1-i))*digit
            //The (length-1-i) goes from 0 to 1 to 2 all the way up to length - 1 and corresponds to the exponent of the 2 for the current binary position
            //e.g. the third character fron the right will be 2^2 = 4 cos the third integer in a binary String represents the denary number 4
            //It is the multiplied by digit which is either 0 or 1
        }
        
        return denary;
    }

    
    public static int distance2(String x, String y) 
    {
        //COMPLEXITY A
        
        int permutations = (int) Math.pow(2,(x.length())); //Creating an integer of length 2^n (where n is the length of x)

        int[] level = new int[permutations]; //Initialises an integer array of length 'permutations' to store the level+1 that each node resides, a node with level 0 is one that has not been visited yet (used 0 since that is the default value of entries in an integer array)
        level[bintoden(x)] = 1; //The root node is at level 0 but since the array uses 0 to mean an unvisited node, we increase each node's level value by 1 so the recorded level of the root node is set to 0


        //A Queue will be implemented for the BFS
        //The Queue will be implemented as a Linked List for constant Queue and Dequeue time complexities
        //Each element in the Queue will be a String in the form: binary,parent
        //Where binary is the binary String to be checked for potential operations
        //And parent is the binary String of the parent of binary
        
        Queue<String> frontier = new LinkedList<>(); //Initialises a Queue (as a Linked List) of Strings for the frontier of the graph
        frontier.add(x + "," + x); //Adds the input String x to the frontier queue, the second x is to represent that x is its own parent as it's the root

        String current = new String(""); //Creates a temporary String variables to store the current node being inspected
        String temp = new String (""); //Creates a temporary String variable to store the current String after it has been operated on
        String append = new String(""); //String variable to be used to append new values (binary,parent) to the Queue
        int dentemp = -1; //Integer to store the denary value of the temporary String (so don't have to call the method multiple times)
        int denpar = -1; //Integer to store the denary value of the parent node of 'temp' (so don't have to call multiple times)

        boolean found = false; //Boolean used to store if y has been reached
        int depth = 0; //Integer representing the depth of the graph (which is equal to the amount of operations performed sequentially) to be returned
        
        while (!frontier.isEmpty() && !found) //While there is still an element in the Queue and found is false (haven't reached y)
        {
            //COMPLEXITY C
            
            current = frontier.poll(); //Dequeues element from the Queue and sets 'current' to its value

            if ((current.substring(0, x.length()).equals(y))) //If a node is equal to y, this means the node is the goal node
            {
                found = true;
                depth = level[bintoden(current.substring((x.length()+1), ((2*x.length())+1)))]; //The depth of the goal node is equal to the depth of its parent 
                //Normally, the depth of the goal node would be the depth of its parent+1 but since we made the depth of the root 1 instead of 0, it causes each depth to be 1 greater than it should be so the true depth of the goal node is the depth of its parent
                return depth;
            }
            else
            {
                denpar = bintoden(current.substring(0, x.length())); //Defining denpar once for all the child nodes
                
                for (int i = 0; i < (x.length() - 2); i++) //Loops through the String from 0 to (2 before the end of the String)
                {
                    //COMPLEXITY E
                    
                    temp = current.substring(0, x.length()); //Assigns the value of the node being explored to temp
                
                    if (current.charAt(i) == '1' && current.charAt(i+1) == '1' && current.charAt(i+2) == '0') //Checks for first operation 110 -> 001
                    {
                        temp = temp.substring(0, i) + "001" + temp.substring((i+3)); //Perform operation at i and surround with the unchanged values
                    }

                    else if (current.charAt(i) == '0' && current.charAt(i+1) == '1' && current.charAt(i+2) == '1') //Checks for second operation 011 -> 100
                    {
                        temp = temp.substring(0, i) + "100" + temp.substring((i+3)); //Perform operation at i and surround with the unchanged values
                    }

                    else if (current.charAt(i) == '1' && current.charAt(i+1) == '0' && current.charAt(i+2) == '1') //Checks for third operation 101 -> 110
                    {
                        temp = temp.substring(0, i) + "110" + temp.substring((i+3)); //Perform operation at i and surround with the unchanged values
                    }

                    dentemp = bintoden(temp);

                    if (level[dentemp] == 0) //If that node has not already been visited (unvisited nodes have level value of 0)
                    {
                        append = temp + "," + current.substring(0, x.length()); //Append becomes 'temp' which is the binary component of the operated String, concatenated with a comma and then the substring of current from the start to n
                        //This substring of current is the parent binary String of temp, and is stored in append after the commma to record the parent
                        
                        frontier.add(append); //Add 'append' to the Queue
                        level[dentemp] = level[denpar] + 1; //Below:
                        //The level of the newly found String is equal to the level of its parent String +1
                        //The substring [0,x.length()] is the newly found String
                        //The substring [(x.length()+1),((2*x.length())+1)] is the parent String
                        //So level[bintoden(new)] = level[bintoden(parent)]+1
                    }
                }
            }
        }
        return -1;
    }
}