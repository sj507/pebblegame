/**
 * Pebble class, for creating all the pebbles that we would need and storing any methods that an individual pebble might need.
 * 
 * @authors Sam Judges, Matt Morris
 */

public class Pebble
{
    private int weight;
    private boolean positive;

    public int getWeight()
    {
        return this.weight;
    }
    
    public boolean getPositive()
    {
        return this.positive;
    }

    public Pebble(int w)
    {
        if (w < 0)
        {
            this.positive = false;
        }
        else
        {
            this.weight = w;
            this.positive = true;
        }
    }
}
