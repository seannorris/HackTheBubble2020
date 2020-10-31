package swing;

import javax.swing.*;

public class MainWindow extends JFrame
{
    private final Grid grid;
    
    public MainWindow(int sizeX, int sizeY)
    {
        grid = new Grid(sizeX, sizeY);
        this.setSize(sizeX * 15, sizeY * 15);       //Setting the size of the window
        this.add(grid);         //Adding the JEditorPane to the JFrame
        this.setVisible(true);        //Making the window visible
    }
    
    public Grid getGrid()
    {
        return grid;
    }
}