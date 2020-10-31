package swing;

import java.awt.GridBagConstraints;
import java.awt.Insets;

//The Constraints class is for controlling the size of any component of a grid back layer with padding
public class Constraints extends GridBagConstraints
{
    public Constraints(int x, int y, int width, int height, Insets insets)
    {
        gridx = x;
        gridy = y;
        this.gridwidth = width;
        this.gridheight = height;
        this.insets = insets;
    }
    
    public Constraints(int x, int y, int width, int height, int margin)
    {
        this(x, y, width, height, new Insets(margin,margin,margin,margin));
    }
    
    public Constraints(int x, int y, int width, int height)
    {
        this(x, y, width, height, 1);
    }
    
    public Constraints(int x, int y)
    {
        this(x, y, 1, 1 );
    }
}
