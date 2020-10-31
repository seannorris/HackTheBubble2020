package ui.drawing.composition;

/**
 * Represents a coordinate with a z-index.<br>
 * {@inheritDoc}
 */
public class OrderedCoordinate extends Coordinate
{
    private final int z;
    
    
    /**
     * Constructor for OrderedCoordinate without z index.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public OrderedCoordinate(int x, int y)
    {
        this(x, y, 0);
    }
    
    /**
     * Constructor for OrderedCoordinate with z index.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z index.
     */
    public OrderedCoordinate(int x, int y, int z)
    {
        super(x, y);
        this.z = z;
    }
    
    /**
     * Shortcut method to get OrderedCoordinate object for specified position.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The OrderedCoordinate object.
     */
    public static OrderedCoordinate at(int x, int y)
    {
        return new OrderedCoordinate(x, y);
    }
    
    /**
     * Shortcut method to get OrderedCoordinate object for specified position.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z index.
     * @return The OrderedCoordinate object.
     */
    public static OrderedCoordinate at(int x, int y, int z)
    {
        return new OrderedCoordinate(x, y, z);
    }
    
    /**
     * Shortcut method to get OrderedCoordinate object from a Coordinate object.
     * @param coordinate The Coordinate object.
     * @return The OrderedCoordinate object.
     */
    public static OrderedCoordinate at(Coordinate coordinate)
    {
        return at(coordinate.getX(), coordinate.getY());
    }
    
    /**
     * Getter for the z index.
     * @return The z index.
     */
    public int getZ()
    {
        return z;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object in)
    {
        return super.equals(in) && in instanceof OrderedCoordinate && ((OrderedCoordinate)in).getZ() == z;
    }
    
    /**
     * {@inheritDoc}
     * In this case also multiply z index by a bigger number.
     */
    public int hashcode()
    {
        return super.hashcode() + 1000000 * z;
    }
    
    /**
     * {@inheritDoc}
     * In this case order by z index primarily, then x.
     */
    public int compareTo(Coordinate in)
    {
        if(!(in instanceof OrderedCoordinate))
            return super.compareTo(in);
        
        var o = (OrderedCoordinate)in;
        if(o.z == z)
            return super.compareTo(o);
        
        return z - o.z;
    }
}
