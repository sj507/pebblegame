import java.util.ArrayList;

/**
 * Support class Bag. Acts as a bag in the pebble game. Can be a white bag or a black bag. Contains
 an array of pebbles that must all be positive. 
 * @author Sam, Matt
 * @version 1.0
 */

public class Bag {

    private ArrayList<Pebble> pebbles = new ArrayList<Pebble>();

    private String Name;
    private String Colour;

    public Bag(String n, String c, ArrayList<Pebble> p)
    {
        this.Name = n;
        this.Colour = c;
        this.pebbles = p;
    }

    public String getName()
    {
        return Name;
    }

    public String getColour()
    {
        return Colour;
    }

    public ArrayList<Pebble> getPebbles()
    {
        return pebbles;
    }

    /**
     * Used to remove pebble from the pebble array based on an index. The removed pebble in then
     returned.
     */

    public Pebble dropPebble(int i)
    {
        Pebble temp = pebbles.get(i);
        pebbles.remove(temp);
        return temp;
    }

    public void setPebbles(ArrayList<Pebble> p)
    {
        this.pebbles = p;
    }

    /**
     * Simple override method used to print out the pebbles in the bag in a readable manner.
     */

    @Override
		public String toString() {
			String pebbleString = "";
			for (int i = 0; i < pebbles.size(); i++) {
				pebbleString = pebbleString + " " + Integer.toString(pebbles.get(i).getWeight());
			}
			return pebbleString;
		}

}
