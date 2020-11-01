package swing;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.LinkedHashSet;
import java.util.Set;

// Grid class stores the label grid
public class Grid extends JPanel
{
    private Char[][] grid;
    
    //constructor
    public Grid(int sizeX, int sizeY)
    {
        super(new GridBagLayout());
        //setBackground(Color.BLACK);

        grid = new Char[sizeX][sizeY];

        for(var x = 0; x < sizeX; x++)
        {
            for (var y = 0; y < sizeY; y++)
            {
                grid[x][y] = new Char(" ", Color.BLACK, x, y);
                var square = grid[x][y];
                add(square, new Constraints(x, y, 1, 1, 0));
            }
        }
    }
    
    public Char[][] getGrid()
    {
        return grid;
    }
}
