import java.util.ArrayList;
import java.util.Random;
import java.io.File;  
import java.io.FileNotFoundException;  
import java.util.Scanner; 
import java.util.*;

public class PebbleGame
{
    private int numberOfPlayers;
    
    private int[] XRange;
    private int[] YRange;
    private int[] ZRange;
    
    private ArrayList<Bag> whiteBags;
    private ArrayList<Bag> blackBags;
    private ArrayList<Player> players;
    
    public PebbleGame(int n, String x, String y, String z) 
    {
        this.numberOfPlayers = n;
        this.XRange = readInRanges(x);
        this.YRange = readInRanges(y);
        this.ZRange = readInRanges(z);
        for (int i = 0; i < n; i++) {
            Player temp = new Player();
            players.add(temp);
            temp = null;
        }
    }
    
    
    public class Player
    {
        
        private int id;
        private ArrayList<Pebble> playerPebbles;
        
        
        public Player()
        {
            this.id = players.size() + 1;
        }
    }
    
    public static int[] readInRanges(String fileName) //ADD A CHECK TO MAKE SURE THAT PEBBLES WIEHGTS ARE POSTIVIE
    {
        String[] values;
        List<String> items = Arrays.asList("foo", "bar");
        int[] ranges;
       
        try {
           File myObj = new File(fileName);
           Scanner myReader = new Scanner(myObj);
           while (myReader.hasNextLine()) {
               String data = myReader.nextLine();
               values = data.split(",",0);
               items = Arrays.asList(data.split("\\s*,\\s*"));
           }
           myReader.close();
        } catch (FileNotFoundException e) {
           System.out.println("An error occurred.");
           e.printStackTrace();
        }
        
        values = items.toArray(new String[0]);
        
        ranges = Arrays.asList(values).stream().mapToInt(Integer::parseInt).toArray(); 
        
        return ranges;
    }
    
    public static PebbleGame Initialise() 
    {
        //for user input just access userInput.nextLine();
        Scanner userInput = new Scanner(System.in); 
        
        System.out.println("Welcome to the PebbleGame!!");
        System.out.println("You will be asked to enter the number of players.");
        System.out.println("and then for the location of three files in turn containing comma seperated integer values for the pebble weights.");
        System.out.println("The integer values must be strictly positive.");
        System.out.println("The game will then be simulated, and the output written to files in this directory.");
        
        System.out.println("Please enter the number of players:");
        String numofPlayers = userInput.nextLine();
        int numOfPlayers = Integer.parseInt(numofPlayers);
        
        System.out.println("Please enter location of bag number 0 to load:");
        String fileOne = userInput.nextLine();
        
        System.out.println("Please enter location of bag number 1 to load:");
        String fileTwo = userInput.nextLine();
        
        System.out.println("Please enter location of bag number 2 to load:");
        String fileThree = userInput.nextLine();
        
        PebbleGame tempGame = new PebbleGame(numOfPlayers, fileOne, fileTwo, fileThree);
        return tempGame;
    }
    
    public static void main(String[] args) 
    {
        PebbleGame mainGame = Initialise();
    }
}
