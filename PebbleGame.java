import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class PebbleGame
{
    private int numberOfPlayers;

    // Changed the ranges to be an array of arrays; just helps with looping when initialising the bags.
    private int[][] ranges = new int[3][2];

    private ArrayList<Bag> whiteBags;
    private ArrayList<Bag> blackBags;
    private ArrayList<Player> players = new ArrayList<>();

    public PebbleGame(int n, String x, String y, String z)
    {
        this.numberOfPlayers = n;
        this.ranges[0] = readInRanges(x);
        this.ranges[1] = readInRanges(y);
        this.ranges[2] = readInRanges(z);
        for (int i = 0; i < n; i++) {
            Player temp = new Player();
            players.add(temp);
            temp = null;
        }
        this.blackBags = initialiseBags();
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

    public ArrayList<Bag> initialiseBags()
    {
      Random random = new Random();
      int[] bagMax = new int[3];
      int totalPebbles = (this.numberOfPlayers * 33) + (random.nextInt(numberOfPlayers) * 10);
      ArrayList<Bag> tempBags = new ArrayList<>();

      for (int i = 0; i < 2; i ++) {
  			int pebblesInBag = random.nextInt(totalPebbles);
  			bagMax[i] = (pebblesInBag);
  			totalPebbles -= pebblesInBag;
  		}
      bagMax[2] = totalPebbles;

      for (int x = 0; x < 3; x ++)
      {
        String letter = "";
        switch (x)
        {
          case 0:
            letter = "X";
            break;
          case 1:
            letter = "Y";
            break;
          case 2:
            letter = "Z";
            break;
        }
        ArrayList<Pebble> tempPebbles = new ArrayList<>();
        for (int z = 0; z < bagMax[x]; z ++)
        {
          tempPebbles.add(new Pebble(weightInRange(ranges[x][0], ranges[x][1])));
        }
        Bag tempBag = new Bag(letter, "Black", tempPebbles);
        tempBags.add(tempBag);
      }
      return tempBags;
    }

    public int weightInRange(int min, int max) {
      Random random = new Random();
      return random.nextInt((max - min) + 1) + min;
  	}

    public static void main(String[] args)
    {
        PebbleGame mainGame = Initialise();

    }
}
