import java.util.ArrayList;

public class Bag {
    
    //list pebbles
    private ArrayList<Pebble> pebbles = new ArrayList<Pebble>();
    
    //name string
    private String Name;
    private String Colour;
    
    
    
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
    
    public Bag(String n, String c, ArrayList<Pebble> p) 
    {
        this.Name = n;
        this.Colour = c;
        this.pebbles = p;
    }
}
