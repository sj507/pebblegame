
public class Pebble
{
    private int weight;

    public int getWeight() 
    {
        return this.weight;
    }
    
    public Pebble(int w) 
    {
        if (w < 0) 
        {
            //throw exeception
        }
        else 
        {
            this.weight = w;
        }
    }
}
