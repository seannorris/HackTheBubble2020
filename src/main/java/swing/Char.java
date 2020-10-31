package swing;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class Char extends JLabel
{
    private static final int SIZE = 15;
    private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    
    private int x;
    private int y;
    
    public Char(String text, Color colour, int x, int y)
    {
        this.x = x;
        this.y = y;
    
        //setSize(new Dimension(SIZE, SIZE));
        //setPreferredSize(new Dimension(SIZE, SIZE));
        setFont(FONT);
        updateText(text, colour);
    }
    
    public int getXCoord()
    {
        return x;
    }
    
    public int getYCoord()
    {
        return y;
    }
    
    public void updateText(String text, Color colour)
    {
        setText(text);
        setForeground(colour);
    }
}
