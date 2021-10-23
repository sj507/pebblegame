import java.util.ArrayList;
import java.util.Random;

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
        //this.XRange = readInRanges(x);
        //this.YRange = readInRanges(y);
        //this.ZRange = readInRanges(z);
        for (int i = 0; i < n; i++) {
            Player temp = new Player();
            players.add(temp);
            temp = null;
        }
    }
    
    //
    public class Player
    {
        //
        private int id;
        private ArrayList<Pebble> playerPebbles;
        
        
        public Player()
        {
            this.id = players.size() + 1;
        }
    }
    
    public static void main(String[] args) 
    {
        //
    }
}
