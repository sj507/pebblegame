
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
    }

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

    public class Player implements Runnable
    {

        private int id;
        private ArrayList<Pebble> playerPebbles;
        private String lastBlackBagDrawnFrom;
        private boolean bagsInitialised;
        private Object lock;

        public Pebble Draw()
        {
            synchronized (lock) 
            {
                //draw a pebble from a black bag at random
                Random random = new Random();
            
                int chosenBag = random.nextInt(3);
                    
                Bag tempBag = blackBags.get(chosenBag);
            
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
                    
                return drawnPebble;
            }
        }

        public synchronized void Discard()
        {
            //discard a pebble from their hand to the white bag paired with the last drawn black bag
            Random random = new Random();
            
            synchronized (lock) 
            {
                
                int randomPebble = random.nextInt(this.getPebbles().size());
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

            System.out.println("Done.");
          
            
          
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

    public void initialisePlayerBags()
    {

    }

    public static void main(String[] args)
    {
        PebbleGame mainGame = initialiseGame();

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
        
        mainGame.getPlayers().forEach(player -> {
            System.out.println("Player " + String.valueOf(player.getId()));
            player.getPebbles().forEach(peb -> {
                System.out.println(peb.getWeight());
            });
            System.out.println("Next Player");
        });

    }
}
