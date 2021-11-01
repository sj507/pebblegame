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
    
    private ArrayList<int[]> rangess = new ArrayList<>();
    
    private ArrayList<Bag> whiteBags;
    private ArrayList<Bag> blackBags;
    private ArrayList<Player> players = new ArrayList<>();
    
    public ArrayList<Bag> getBlackBags()
    {
        return this.blackBags;
    }
    
    public ArrayList<int[]> getRangess()
    {
        return this.rangess;
    }
    
    public int getNumberOfPlayers()
    {
        return this.numberOfPlayers;
    }

    public PebbleGame(int n, String x, String y, String z)
    {
        this.numberOfPlayers = n;
        
        this.rangess.add(readInRanges(x));
        this.rangess.add(readInRanges(y));
        this.rangess.add(readInRanges(z));
        
        for (int i = 0; i < n; i++) {
            Player temp = new Player();
            players.add(temp);
            temp = null;
        }
        this.blackBags = initialiseBagsTwo();
    }

    public class Player
    {

        private int id;
        private ArrayList<Pebble> playerPebbles;
        private String lastBlackBagDrawnFrom;

        
        
        public synchronized void Draw()
        {
            //draw a pebble from a black bag at random
        }
        
        public synchronized void Discard()
        {
            //discard a pebble from their hand to the white bag paired with the last drawn black bag
        }
        
        public Player()
        {
            this.id = players.size() + 1;
        }
    }

    public static int[] readInRanges(String fileName) //ADD A CHECK TO MAKE SURE THAT PEBBLES WIEHGTS ARE POSTIVIE
    {
        String[] values;
        List<String> items = Arrays.asList("foo", "bar");
        int[] rangess;

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

        rangess = Arrays.asList(values).stream().mapToInt(Integer::parseInt).toArray();

        return rangess;
    }

    public static PebbleGame initialiseGame()
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

    public ArrayList<Bag> initialiseBagsTwo()
    {
        Random random = new Random();
        ArrayList<Bag> tempBags = new ArrayList<>();
        int shortestInputArray = 0;
        int temp = 0;
        for(int[] item : this.getRangess()) {   
            temp = item.length;
            if (temp > shortestInputArray) { shortestInputArray = temp; }
        }

        int numofpebinbagone = (this.getNumberOfPlayers() * 11) + (random.nextInt((shortestInputArray - (this.getNumberOfPlayers() * 11)) - 1 ));
        int numofpebinbagtwo = (this.getNumberOfPlayers() * 11) + (random.nextInt((shortestInputArray - (this.getNumberOfPlayers() * 11)) - 1 ));
        int numofpebinbagthree = (this.getNumberOfPlayers() * 11) + (random.nextInt((shortestInputArray - (this.getNumberOfPlayers() * 11)) - 1 ));
        
        int[] maximums = new int[3];
        maximums[0] = numofpebinbagone;
        maximums[1] = numofpebinbagtwo;
        maximums[2] = numofpebinbagthree;
        
        for (int x = 0; x < 3; x ++)
        {
            ArrayList<Pebble> tempPebbles = new ArrayList<>();
            for (int y = 0; y < maximums[x]; y ++) 
            {
                int chosenpebbleindex = random.nextInt(this.getRangess().get(x).length);
                tempPebbles.add(new Pebble(this.getRangess().get(x)[chosenpebbleindex]));
                int[] bing = removeElementFromArray(this.getRangess().get(x), chosenpebbleindex);
                this.getRangess().set(x, bing);
            }
            
            String tempLetter = "";
            if (x == 0) {tempLetter = "X";}
            else if (x == 0) {tempLetter = "Y";}
            else if (x == 0) {tempLetter = "Z";};
            
            Bag tempBag = new Bag(tempLetter, "Black", tempPebbles);
            tempBags.add(tempBag);
        }
        return tempBags;
    }
    
    public int[] removeElementFromArray(int[] oldArray, int index)
    {
        if (oldArray == null || index < 0 || index >= oldArray.length) {
            return oldArray;
        }
        
        int[] newArray = new int[oldArray.length - 1];
 
        for (int i = 0, k = 0; i < oldArray.length; i++) {
            if (i == index) {
                continue;
            }
            newArray[k++] = oldArray[i];
        }

        return newArray;
    }
    
    public int weightInRange(int min, int max) {
      Random random = new Random();
      return random.nextInt((max - min) + 1) + min;
    }

    public static void main(String[] args)
    {
        PebbleGame mainGame = initialiseGame();
        
        mainGame.getBlackBags().forEach(bag -> {
            bag.getPebbles().forEach( peb -> {
                System.out.println(peb.getWeight());
            });
            System.out.println("next");
        });
        
        
    }
}
