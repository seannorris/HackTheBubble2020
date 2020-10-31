import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import javax.swing.Timer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * A class I used to experiment with JLine.
 * A simple demo where you can move a coloured character around the terminal window.
 */
public class ConsoleTest
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        System.out.println("Doesn't work over SSH on some (trenco) host servers!!!");
        System.out.println("Press ctrl+c to escape if this screen shows for over 5 seconds.");
        System.out.println("SSH into a lab machine instead (or just type something like ssh pc7-031-l).");
        var terminal = TerminalBuilder.builder()
                .system(true)
                .dumb(false)
                .jna(true)
                .build();
        terminal.enterRawMode();
        var in = new BufferedReader(new InputStreamReader(terminal.input()));
        //var in = terminal.reader();
        var out = terminal.writer();
        int read;
        /*var escape = false;
        var arrow = false;
        var resetCounter = 0;*/
        xSize = terminal.getWidth();
        ySize = terminal.getHeight();
        draw(out, terminal);
        var escapeTimer = new Timer(1, actionEvent->
        {
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.flush();
            System.exit(0);
        });
        
        while((read = in.read()) != -1)
        {
            /*if(read == -2)
            {
                if(escape)
                {
                    resetCounter++;
                    if (resetCounter > 10)
                    {
                        escape = false;
                        resetCounter = 0;
                    }
                }
                continue;
            }
            if(escape)
            {
                escape = false;
                if(read == 79)
                {
                    arrow = true;
                    continue;
                }
                resetCounter = 0;
            }
            if(arrow)
            {
                switch (read)
                {
                    case 68:
                        //out.print("<");
                        xCoord = bound(xCoord - 1, xSize);
                        break;
                    case 67:
                        //out.print(">");
                        xCoord = bound(xCoord + 1, xSize);
                        break;
                    case 65:
                        //out.print("^");
                        yCoord = bound(yCoord - 1, ySize);
                        break;
                    case 66:
                        //out.print("\\/");
                        yCoord = bound(yCoord + 1, ySize);
                        break;
                }
                arrow = false;
            }*/
            switch (read)
            {
                case 27:
                    escapeTimer.start();
                    read = in.read();
                    escapeTimer.stop();
                    if(read == 91 || read == 79)
                        continue;
                    
                    terminal.puts(InfoCmp.Capability.clear_screen);
                    return;
                case 119:
                    //out.print("w");
                    yCoord = bound(yCoord - 1, ySize);
                    break;
                case 97:
                    //out.print("a");
                    xCoord = bound(xCoord - 1, xSize);
                    break;
                case 115:
                    //out.print("s");
                    yCoord = bound(yCoord + 1, ySize);
                    break;
                case 100:
                    //out.print("d");
                    xCoord = bound(xCoord + 1, xSize);
                    break;
                case 68:
                    //out.print("<");
                    xCoord = bound(xCoord - 1, xSize);
                    break;
                case 67:
                    //out.print(">");
                    xCoord = bound(xCoord + 1, xSize);
                    break;
                case 65:
                    //out.print("^");
                    yCoord = bound(yCoord - 1, ySize);
                    break;
                case 66:
                    //out.print("\\/");
                    yCoord = bound(yCoord + 1, ySize);
                    break;
                    
            }
            draw(out, terminal);
        }
    }
    
    private static int xCoord = 0;
    private static int yCoord = 0;
    private static int xSize = 10;
    private static int ySize = 10;
    private static int lastX = -1;
    private static int lastY = -1;
    private static void draw(PrintWriter out, Terminal terminal)
    {
        if(lastX == xCoord && lastY == yCoord)
            return;
            
        terminal.puts(InfoCmp.Capability.clear_screen);
        var screen = new StringBuilder();
        screen.append("\u001B[32m");
        for(var y = 0; y < ySize; y++)
        {
            if(y > 0)
                screen.append('\n');
            
            for (var x = 0; x < xSize; x++)
                screen.append(x == xCoord && y == yCoord ? "\u001B[31m" + CHAR + "\u001B[32m" : CHAR);
        }
        screen.append("\u001B[0m");
        out.print(screen);
        out.flush();
        lastX = xCoord;
        lastY = yCoord;
    }
    private static final char CHAR = '@';
    
    private static int bound(int in, int bound)
    {
        while (in >= bound)
            in -= bound;
        while (in < 0)
            in += bound;
        
        return in;
    }
}
