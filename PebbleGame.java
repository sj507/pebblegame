import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class PebbleGame
{
    private int numberOfPlayers;

    private volatile String lastDrawnBlackBag;

    private int[] XRange;
    private int[] YRange;
    private int[] ZRange;

    private ArrayList<int[]> rangess = new ArrayList<>();

    private ArrayList<Bag> whiteBags;
    private ArrayList<Bag> blackBags;
    private ArrayList<Player> players = new ArrayList<>();

    private boolean running;

    public ArrayList<Bag> getBlackBags()
    {
        return this.blackBags;
    }

    public ArrayList<Player> getPlayers() {
      return this.players;
    }

    public ArrayList<int[]> getRangess()
    {
        return this.rangess;
    }

    public int getNumberOfPlayers()
    {
        return this.numberOfPlayers;
    }

    public void initialisePlayerThreads(PebbleGame pebbleGame)
    {

      ArrayList<Thread> threads = new ArrayList<>();

      pebbleGame.getPlayers().forEach(player -> {

        threads.add(new Thread(player));
      });
    }
    
    //Constructor method for the pebble game itself.
    public PebbleGame(int n, String x, String y, String z)
    {
        this.numberOfPlayers = n;

        this.rangess.add(readInRanges(x));
        this.rangess.add(readInRanges(y));
        this.rangess.add(readInRanges(z));

        Object lock = new Object();

        for (int i = 0; i < n; i++) {
            Player temp = new Player(lock);
            players.add(temp);
            temp = null;
        }
        this.blackBags = initialiseBlackBags();

        this.whiteBags = initialiseWhiteBags();

        this.running = true;
    }
    
    //Method to initialise all of the white bags.
    public ArrayList<Bag> initialiseWhiteBags()
    {
        ArrayList<Bag> tempBags = new ArrayList<Bag>();

        tempBags.add(new Bag("A", "White", new ArrayList<Pebble>()));
        tempBags.add(new Bag("B", "White", new ArrayList<Pebble>()));
        tempBags.add(new Bag("C", "White", new ArrayList<Pebble>()));

        return tempBags;
    }

    public boolean checkAllBags()
    {

      for (Player player : players) {
        if (player.getBagsInitialised() == false) {
          return false;
        }
      }

      return true;
    }
    
    //Player class to represent the players and the threads.
    public class Player implements Runnable
    {

        private int id;
        private ArrayList<Pebble> playerPebbles;
        private String lastBlackBagDrawnFrom;
        private boolean bagsInitialised;
        private Object lock;

        public int calculateScore() {
          int score = 0;

          for (Pebble pebble : this.playerPebbles) {
            score += pebble.getWeight();
          }

          return score;
        }

        public Pebble Draw()
        {
            synchronized (lock)
            {
                //draw a pebble from a black bag at random
                Random random = new Random();
                boolean choosingBag = true;
                int chosenBag = 0;
                Bag tempBag = null;

                while (choosingBag) {

                  chosenBag = random.nextInt(3);
                  tempBag = blackBags.get(chosenBag);

                  if (tempBag.getPebbles().size() != 0) {
                    choosingBag = false;
                  }
                }

                int randomPebble = random.nextInt(tempBag.getPebbles().size());

                Pebble drawnPebble = tempBag.dropPebble(randomPebble);

                switch(chosenBag) {
                    case 0:
                        lastDrawnBlackBag = "X";
                        break;
                    case 1:
                        lastDrawnBlackBag = "Y";
                        break;
                    default:
                        lastDrawnBlackBag = "Z";
                }

                System.out.println("Player " + String.valueOf(this.getId()) + " has drawn a " +
                String.valueOf(drawnPebble.getWeight()) +  " from bag " + tempBag.getName());

                return drawnPebble;
            }
        }

        public void Discard()
        {
            //discard a pebble from their hand to the white bag paired with the last drawn black bag
            Random random = new Random();

            synchronized (lock)
            {

                int randomPebble = random.nextInt(10);
                Pebble tempPebble = this.getPebbles().remove(randomPebble);
                Bag tempBag;
                switch(lastDrawnBlackBag) {
                    case "X":
                        tempBag = whiteBags.get(0);
                        tempBag.getPebbles().add(tempPebble);
                        whiteBags.set(0, tempBag);
                        break;
                    case "Y":
                        tempBag = whiteBags.get(1);
                        tempBag.getPebbles().add(tempPebble);
                        whiteBags.set(1, tempBag);
                        break;
                    default:
                        tempBag = whiteBags.get(2);
                        tempBag.getPebbles().add(tempPebble);
                        whiteBags.set(2, tempBag);
                }

                System.out.println("Player " + String.valueOf(this.getId()) + " has discarded a " +
                String.valueOf(tempPebble.getWeight()) +  " to bag " + tempBag.getName());
            }
        }

        public Player(Object l)
        {
            this.lock = l;
            this.id = players.size() + 1;
            this.bagsInitialised = false;
            this.playerPebbles = new ArrayList<Pebble>();
        }

        public ArrayList<Pebble> getPebbles()
        {
            return this.playerPebbles;
        }

        public int getId()
        {
            return this.id;
        }

        public boolean getBagsInitialised()
        {
          return this.bagsInitialised;
        }

        public void run()
        {
            Random random = new Random();

            int chosenBag = random.nextInt(3);
            Bag tempBag = blackBags.get(chosenBag);

            for (int i = 0; i < 10; i ++)
            {
                int randomPebble = random.nextInt(tempBag.getPebbles().size());

                Pebble drawnPebble = tempBag.dropPebble(randomPebble);

                this.playerPebbles.add(drawnPebble);

                switch(chosenBag) {
                        case 0:
                            lastDrawnBlackBag = "X";
                        break;
                        case 1:
                            lastDrawnBlackBag = "Y";
                        break;
                        default:
                            lastDrawnBlackBag = "Z";
                }
            }

            this.bagsInitialised = true;

            try {
                synchronized (lock) {

                    System.out.println(checkAllBags());
                    while (checkAllBags() == false)
                    {
                        lock.wait();
                    }

                    lock.notifyAll();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Calculate score - if 100, tell other threads - else, discard/draw

            if (this.calculateScore() == 100) {
              running = false;
              System.out.println("Player " + String.valueOf(this.getId()) + " is the winner!");
            }
            try {
              Thread.sleep(1000);
            }
            catch (InterruptedException e) {
              e.printStackTrace();
            }

            while (running) {
              this.Discard();
              String tempString = "";
              for (Pebble tempPebble : this.playerPebbles) {
                tempString += " " + tempPebble.getWeight();
              }
              System.out.println("Player " + String.valueOf(this.getId()) + " hand is " + tempString);
              if (checkBlackBagsEmpty()) {
                refillBags();
              }
              this.playerPebbles.add(this.Draw());
              if (this.calculateScore() == 100) {
                running = false;
              }
              tempString = "";
              for (Pebble tempPebble : this.playerPebbles) {
                tempString += " " + tempPebble.getWeight();
              }
              System.out.println("Player " + String.valueOf(this.getId()) + " hand is " + tempString);
              if (this.calculateScore() == 100) {
                running = false;
                System.out.println("Player " + String.valueOf(this.getId()) + " is the winner!");
              }
            }



        }
    }
    
    //Method to check if any of the black bags are empty
    public boolean checkBlackBagsEmpty() {
      for (Bag blackBag : this.blackBags) {
        if (blackBag.getPebbles().size() == 0) {
          return true;
        }
      }
      return false;
    }
    
    //Method to refill all the nessesary black bags by emptying the corresponding white bag into it. 
    public void refillBags() {
      for (int i = 0; i < this.blackBags.size(); i ++) {
        Bag tempBlackBag = this.blackBags.get(i);
        Bag tempWhiteBag = this.whiteBags.get(i);
        if (tempBlackBag.getPebbles().size() == 0) {
          tempBlackBag.setPebbles(tempWhiteBag.getPebbles());
          this.blackBags.set(i, tempBlackBag);
          tempWhiteBag.setPebbles(new ArrayList<Pebble>());
          this.whiteBags.set(i, tempWhiteBag);
        }
      }
    }

    //Method to read in the range files to assist in creating the black bags.
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
    
    //Method for handling the user side of initialising the game.
    public static PebbleGame initialiseGame()
    {
        //for user input just access userInput.nextLine();
        Scanner userInput = new Scanner(System.in);
        Boolean tempBool = true;
        int numOfPlayers = 0;
        String numOfPlayersString = "";
        String fileOne = "";
        String fileTwo = "";
        String fileThree = "";

        System.out.println("Welcome to the PebbleGame!!");
        System.out.println("You will be asked to enter the number of players.");
        System.out.println("and then for the location of three files in turn containing comma seperated integer values for the pebble weights.");
        System.out.println("The integer values must be strictly positive.");
        System.out.println("The game will then be simulated, and the output written to files in this directory.");

        System.out.println("Please enter the number of players:");

        while (tempBool) {
          try  {
            numOfPlayersString = userInput.nextLine();
            numOfPlayers = Integer.parseInt(numOfPlayersString);
            if (numOfPlayers > 0) {
              tempBool = false;
            }
            else {
              System.out.println("Integer must be positive.");
              System.out.println("Please enter the number of players:");
            }
          }
          catch (NumberFormatException e) {
            if (numOfPlayersString.equals("E"))
            {
                System.exit(0);
            }
              
            System.out.println("Please enter a positive integer.");
            System.out.println("Please enter the number of players:");
          }
        }

        tempBool = true;

        System.out.println("Please enter location of bag number 0 to load:");

        while (tempBool) {
          try {
            fileOne = userInput.nextLine();
            File myObj = new File(fileOne);
            Scanner myReader = new Scanner(myObj);
            String handle = fileOne.substring(fileOne.indexOf(".") + 1, fileOne.length());
            if (handle.equals("txt") || handle.equals("csv")) {
              tempBool = false;
            }
            else {
              System.out.println("File must be .txt or .csv");
              System.out.println("Please enter location of bag number 0 to load:");
            }
          }
          catch (FileNotFoundException e) {
            if (fileOne.equals("E"))
            {
                System.exit(0);
            }
              
            System.out.println("File does not exist.");
            System.out.println("Please enter location of bag number 0 to load:");
          }
        }

        tempBool = true;

        System.out.println("Please enter location of bag number 1 to load:");

        while (tempBool) {
          try {
            fileTwo = userInput.nextLine();
            File myObj = new File(fileTwo);
            Scanner myReader = new Scanner(myObj);
            String handle = fileTwo.substring(fileTwo.indexOf(".") + 1, fileTwo.length());
            if (handle.equals("txt") || handle.equals("csv")) {
              tempBool = false;
            }
            else {
              System.out.println("File must be .txt or .csv");
              System.out.println("Please enter location of bag number 1 to load:");
            }
          }
          catch (FileNotFoundException e) {
            if (fileTwo.equals("E"))
            {
                System.exit(0);
            }
              
              System.out.println("File does not exist.");
            System.out.println("Please enter location of bag number 1 to load:");
          }
        }

        tempBool = true;

        System.out.println("Please enter location of bag number 2 to load:");

        while (tempBool) {
          try {
            fileThree = userInput.nextLine();
            File myObj = new File(fileThree);
            Scanner myReader = new Scanner(myObj);
            String handle = fileThree.substring(fileThree.indexOf(".") + 1, fileThree.length());
            if (handle.equals("txt") || handle.equals("csv")) {
              tempBool = false;
            }
            else {
              System.out.println("File must be .txt or .csv");
              System.out.println("Please enter location of bag number 2 to load:");
            }
          }
          catch (FileNotFoundException e) {
            if (fileThree.equals("E"))
            {
                System.exit(0);
            }
              
              System.out.println("File does not exist.");
            System.out.println("Please enter location of bag number 2 to load:");
          }
        }


        PebbleGame tempGame = new PebbleGame(numOfPlayers, fileOne, fileTwo, fileThree);
        return tempGame;
    }
    
    //Method which initialises the black bags for the game.
    public ArrayList<Bag> initialiseBlackBags()
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
            else if (x == 1) {tempLetter = "Y";}
            else if (x == 2) {tempLetter = "Z";};

            Bag tempBag = new Bag(tempLetter, "Black", tempPebbles);
            tempBags.add(tempBag);
        }
        return tempBags;
    }
    
    //Method which takes and array and an index and returns the list back but with the indexed item removed
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

    public void initialisePlayerBags()
    {

    }
    
    //Main Method where the game is initialised from and the threads are started
    public static void main(String[] args)
    {
        PebbleGame mainGame = initialiseGame();
        Scanner userInput = new Scanner(System.in);
        boolean warning = false;
        
        for (Bag bag : mainGame.getBlackBags()) 
        {
            for (Pebble peb : bag.getPebbles())
            {
                if (peb.getPositive() == false) 
                {
                    warning = true;
                }
            }
        }
        
        
        
           
        if (warning)
        {
            System.out.println("Warning you have input files with atleast 1 negative pebble weight");
            try {Thread.sleep(500);}
            catch (InterruptedException e) {e.printStackTrace();}
        }
        
        
       
        

        ArrayList<Thread> threads = new ArrayList<>();

        mainGame.getPlayers().forEach(player -> {
          threads.add(new Thread(player));
        });

        threads.forEach(thread -> {
          thread.start();
        });



        try {
            Thread.sleep(1000);
          }
          catch (InterruptedException e) {
            e.printStackTrace();
          }

        // mainGame.getPlayers().forEach(player -> {
        //     System.out.println("Player " + String.valueOf(player.getId()));
        //     player.getPebbles().forEach(peb -> {
        //         System.out.println(peb.getWeight());
        //     });
        //     System.out.println("Next Player");
        // });

    }
}
