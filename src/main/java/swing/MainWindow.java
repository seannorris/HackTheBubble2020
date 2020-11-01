package swing;

import javax.swing.*;
import java.awt.Color;

public class MainWindow extends JFrame
{
    private final Grid grid;
    
    public MainWindow(int sizeX, int sizeY)
    {
        grid = new Grid(sizeX, sizeY);
        this.setSize(sizeX * 10, sizeY * 25);       //Setting the size of the window
        this.add(grid);         //Adding the JEditorPane to the JFrame
        //setBackground(Color.BLACK);
        this.setVisible(true);        //Making the window visible
    }
    
    public Grid getGrid()
    {
        return grid;
    }
}