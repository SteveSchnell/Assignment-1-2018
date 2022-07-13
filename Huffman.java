/******************************************************************************************************************************
*Program Name: Huffman.java                                                                                                   *
*Programmer: Steve Schnell                                                                                                    *
*Assignment: Assignment 1                                                                                                     *
*Purpose:The purpose of this program is to implement a Huffman tree and encode and decode a sentence.                         *
*Date: February 12, 2018                                                                                                      *
******************************************************************************************************************************/

import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Huffman {

    static PriorityQueue<Node> nodes = new PriorityQueue<>((o1, o2) -> (o1.value < o2.value) ? -1 : 1);

    static TreeMap<Character, String> codes = new TreeMap<>();

    static String message = "";//Hold the import text
    static String encoded = "";//Holds the encoded message
    static String decoded = "";//Holds the  decoded message
    
    static int depth;
    
    static char markerChar = '@'; // sentinal at end of file


    static int ASCII[] = new int[128];  

//-----------------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws FileNotFoundException //Main function that executes the program
      {
        System.out.println("\n...opening message.txt");
        Scanner scanner = new Scanner(new File("message.txt")); //Reeds the message from a text file
        System.out.println("\n...message.txt loaded successfully");        
        newMessage(scanner);  //Starts to process a message       


      }

//-----------------------------------------------------------------------------------------------------------

    private static void newMessage(Scanner scanner) //Set up all the different variables to execute the program and call the different functions
    {
        message = scanner.nextLine();

            ASCII = new int[128];

            nodes.clear();//Make sure that the tree and the array are both empty 
            codes.clear();

            encoded = "";
            decoded = "";

            System.out.println("\n\n <  Input Text: " + message + "  >\n");//print out to the message
            System.out.println(" <  Input Text is: " + message.length() + " characters long.  >\n");
            
            System.out.println("\n...calculating intervals");   
            calculateCharIntervals(nodes);//Calculate the intervals of the different characters

            System.out.println("\n...building huffman tree");  
            buildTree(nodes);//Builds the Huffman tree
            
            if (message.length() < 20)//if string is shorter than 15 characters
            {
               System.out.println("\n\n  << Huffman Tree >>\n");
               printNode(nodes.peek());//prints Huffman tree
            }
            else
            {
               System.out.println("\n...huffman tree is too big to display");  
            }
            
            generateCodes(nodes.peek(), "");//Generate code from the Hoffman tree

            printCodes();//print out of the generated code
            System.out.println("\n\n  << Encoding/Decoding >>\n");
            
            

            encodeMessage();//Encodes the message using the generated code
            decodeMessage();//Decodes the message
    }

//-----------------------------------------------------------------------------------------------------------


    private static void decodeMessage() //This function is used to decode an encoded message
    {
        decoded = "";
        Node node = nodes.peek();

        for (int i = 0; i < encoded.length(); ) //Loops through the entire in coded message
        {
            Node tmpNode = node;

            while (tmpNode.left != null && tmpNode.right != null && i < encoded.length()) // Loop through the entire tree
            {
                if (encoded.charAt(i) == '1')//If 1
                    tmpNode = tmpNode.right;
                else //If 0
                    tmpNode = tmpNode.left;

                i++;
            }

            if (tmpNode != null)//Checks if the note is null
            {
                if (tmpNode.character.length() == 1)
                    decoded += tmpNode.character;

                else
                    System.out.println("Input not Valid");//If notice no print out error message
             }
        }
        System.out.println("Decoded Text: " + decoded);//print out to the decoded message
    }

//-----------------------------------------------------------------------------------------------------------

    private static void encodeMessage() //This function in encodeds the message into binary
    {
        encoded = "";

        for (int i = 0; i < message.length(); i++)//Loops through the entire message
        {
            encoded += codes.get(message.charAt(i));//Converts the characters into binary code
        }

        System.out.println("Encoded Text: " + encoded + "\n");//print Outlet in coded message
    }

//-----------------------------------------------------------------------------------------------------------

    private static void buildTree(PriorityQueue<Node> vector) //This function builds a tree
    {
        while (vector.size() > 1)
        {
            vector.add(new Node(vector.poll(), vector.poll()));
        }
    }

//-----------------------------------------------------------------------------------------------------------

    private static void printCodes() //This function prints out at Code table
    {
        System.out.println("\n     << Code Table >>\n");
        codes.forEach((k, v) -> System.out.println("'" + k + "' : " + v));
    }

//-----------------------------------------------------------------------------------------------------------

    private static void calculateCharIntervals(PriorityQueue<Node> vector) //This function calculates intervals
    {
        for (int i = 0; i < message.length(); i++)//Loop through the entire message
        {
            ASCII[message.charAt(i)]++;
        }

        for (int i = 0; i < ASCII.length; i++)//Loop through the ASCII code
        {
            if (ASCII[i] > 0) 
            {
                vector.add(new Node(ASCII[i] / (message.length() * 1.0), ((char) i) + ""));
            }
        }
    }

//-----------------------------------------------------------------------------------------------------------

    private static void generateCodes(Node node, String s) //This function generates the ASCII code
    {

        if (node != null) //Checks if the node is not null
        {
            if (node.right != null)//Checks if the right node is not null
                generateCodes(node.right, s + "1");

            if (node.left != null)//Checks if the left node is not null
                generateCodes(node.left, s + "0");

            if (node.left == null && node.right == null)
                codes.put(node.character.charAt(0), s);
        }
    }
    
//-----------------------------------------------------------------------------------------------------------

    public static void printNode(Node root) //prints out the huffman tree
    {
        int maxLevel = maxLevel(root);//gets max level
        printNodePInternal(Collections.singletonList(root), 1, maxLevel);
    }

//-----------------------------------------------------------------------------------------------------------

    private static  void printNodePInternal(List<Node> nodes, int level, int maxLevel) //internal recursion function for tree
    {
        if (nodes.isEmpty() || isAllElementsNull(nodes))//checks that the node is not null
            return;
            
        //format variables
        int floor = maxLevel - level;
        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        printWhitespaces(firstSpaces);//prints out spaces

        List<Node> newNodePs = new ArrayList<Node>();//create a list of nodes
        
        for (Node node : nodes) //transfers the nodes to the list of nodes
        {
            if (node != null) 
            {
                System.out.print(node.character);
                newNodePs.add(node.left);
                newNodePs.add(node.right);
            } 
            else 
            {
                newNodePs.add(null);
                newNodePs.add(null);
                System.out.print(" ");
            }

            printWhitespaces(betweenSpaces);//prints out spaces
        }
        System.out.println("");

        for (int i = 1; i <= endgeLines; i++) //loops true the list and builds the first level of the tree
        {
            for (int j = 0; j < nodes.size(); j++) 
            {
                printWhitespaces(firstSpaces - i);
                if (nodes.get(j) == null) 
                {
                    printWhitespaces(endgeLines + endgeLines + i + 1);//prints out spaces
                    continue;
                }

                if (nodes.get(j).left != null)
                    System.out.print("/");
                else
                    printWhitespaces(1);//prints out spaces

                printWhitespaces(i + i - 1);

                if (nodes.get(j).right != null)
                    System.out.print("\\");
                else
                    printWhitespaces(1);

                printWhitespaces(endgeLines + endgeLines - i);//prints out spaces
            }

            System.out.println("");
        }

        printNodePInternal(newNodePs, level + 1, maxLevel);//runs the function in recursion with the next level of the tree
    }
    
//-----------------------------------------------------------------------------------------------------------

    private static void printWhitespaces(int count) //this function prints out spaces
    {
        for (int i = 0; i < count; i++)//loopes for given amount
            System.out.print(" ");
    }

//-----------------------------------------------------------------------------------------------------------

    private static  int maxLevel(Node node) //return the max level of the tree
    {
        if (node == null)
            return 0;//if tree is null

        return Math.max(maxLevel(node.left), maxLevel(node.right)) + 1;
    }

//-----------------------------------------------------------------------------------------------------------

    private static <T> boolean isAllElementsNull(List list) //check if any element in the list null
    {
        for (Object object : list) 
        {
            if (object != null)// if not null
                return false;
        }

        return true;//return true if all null
    }
//-----------------------------------------------------------------------------------------------------------

}// Huffman class End

//-----------------------------------------------------------------------------------------------------------

class Node //This is the node class It is used to create the
{

    Node left, right;
    double value;
    String character;

//-----------------------------------------------------------------------------------------------------------

    public Node(double value, String character) //This function is for holding a value and a string character
    {

        this.value = value;
        this.character = character;

        left = null;
        right = null;
    }

//-----------------------------------------------------------------------------------------------------------

    public Node(Node left, Node right) //This function is holding the left and right node
    {
        this.value = left.value + right.value;
        character = left.character + right.character;

        if (left.value < right.value) 
        {
            this.right = right;
            this.left = left;
        } 
        else 
        {
            this.right = left;
            this.left = right;
        }
    }
    
//-----------------------------------------------------------------------------------------------------------

}// Node Class End


class BTreePrinter {

   
}