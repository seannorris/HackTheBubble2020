package ui;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import ui.drawing.PrintHandler;
import ui.input.KeyHandler;
import ui.input.ResizeHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * Handles the JLine terminal and orchestrates user interaction.
 */
public class TerminalHandler
{
    private final Terminal terminal;
    private KeyHandler keyHandler;
    private PrintHandler printHandler;
    private ResizeHandler resizeHandler;
    private final BufferedReader in;
    private final BufferedWriter out;
    private boolean exit;
    private int width;
    private int height;
    private Reader oldReader;
    
    /**
     * Constructor for TerminalHandler.
     * @param keyHandler The key handler for user input.
     * @param printHandler The print handler for printing to the terminal.
     * @param resizeHandler The resize handler for when the user resizes the terminal window.
     * @throws IOException Pass exception up chain.
     */
    public TerminalHandler(KeyHandler keyHandler, PrintHandler printHandler, ResizeHandler resizeHandler) throws IOException
    {
        System.out.println("Doesn't work over SSH on some (trenco) host servers!!!");
        System.out.println("Press ctrl+c to escape if this screen shows for over 5 seconds.");
        System.out.println("SSH into a lab machine instead (just type something like ssh pc7-031-l).");
        System.out.println("If it still doesn't work try a different terminal emulator!");
        terminal = TerminalBuilder.builder()
                .system(true)
                .dumb(false)
                .jna(true)
                .encoding(StandardCharsets.UTF_8)
                .build();
        terminal.enterRawMode();
        this.keyHandler = keyHandler;
        this.printHandler = printHandler;
        this.resizeHandler = resizeHandler;
        in = new BufferedReader(new InputStreamReader(terminal.input(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(terminal.output(), StandardCharsets.UTF_8));
    }
    
    /**
     * Getter for the key handler.
     * @return The key handler.
     */
    public KeyHandler getKeyHandler()
    {
        return keyHandler;
    }
    
    /**
     * The setter for the key handler.
     * @param keyHandler The new key handler.
     */
    public void setKeyHandler(KeyHandler keyHandler)
    {
        this.keyHandler = keyHandler;
    }
    
    /**
     * Getter for the print handler.
     * @return The print handler.
     */
    public PrintHandler getPrintHandler()
    {
        return printHandler;
    }
    
    /**
     * The setter for the print handler.
     * @param printHandler The new print handler.
     */
    public void setPrintHandler(PrintHandler printHandler)
    {
        this.printHandler = printHandler;
    }
    
    /**
     * Getter for the resize handler.
     * @return The resize handler.
     */
    public ResizeHandler getResizeHandler()
    {
        return resizeHandler;
    }
    
    /**
     * The setter for the resize handler.
     * Send the current terminal size to the new handler.
     * @param resizeHandler The new resize handler.
     */
    public void setResizeHandler(ResizeHandler resizeHandler)
    {
        this.resizeHandler = resizeHandler;
        resizeHandler.resize(terminal.getWidth(), terminal.getHeight());
    }
    
    /**
     * The getter for the underlying JLine terminal object.
     * @return The JLine terminal.
     */
    public Terminal getTerminal()
    {
        return terminal;
    }
    
    /**
     * Gets the next character from the input reader with a timeout.
     * If the timeout is reached then -2 is returned.
     * In this case the Reader thread is kept and reused the next time the method is called.
     * While waiting for result still check for resizes and redraw as appropriate.
     * @param timeout The timeout in milliseconds.
     * @return The read character.
     * @throws IOException Pass exception up chain.
     */
    public int getNextChar(int timeout) throws IOException
    {
        var usingOld = oldReader != null;
        var reader = usingOld ? oldReader : new Reader(in);
        if(!usingOld)
            reader.start();
        
        var startTime = System.currentTimeMillis();
        while(!reader.done() && (timeout < 0 || System.currentTimeMillis() - startTime <= timeout))
        {
            try
            {
                Thread.sleep(1);
            }
            catch(InterruptedException e)
            {
                throw new RuntimeException(e);
            }
            checkResize();
        }
        var out = reader.getRead();
        oldReader = out == -2 ? reader : null;
        
        return out;
    }
    
    /**
     * Sets the exit field to true.
     */
    public void exit()
    {
        exit = true;
    }
    
    /**
     * Main program loop.
     * @throws IOException Pass exception up chain.
     */
    public void run() throws IOException
    {
        width = terminal.getWidth();
        height = terminal.getHeight();
        resizeHandler.resize(width, height);
        draw();
        
        while(getInput());
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();
        if(oldReader != null)
        {
            out.write("exiting... if nothing happens press enter.");
            out.flush();
        }
        terminal.close();
    }
    
    /**
     * Get the string to draw from the print handler and draw it.
     * @throws IOException Pass exception up chain.
     */
    public void draw() throws IOException
    {
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();
        out.write(printHandler.getDisplay());
        out.flush();
    }
    
    /**
     * Overload method to call {@link #getInput(int)} with unlimited timeout.
     * @return Boolean; true if the program should exit.
     * @throws IOException Pass exception up chain.
     */
    public boolean getInput() throws IOException
    {
        return getInput(-1);
    }
    
    /**
     * Gets an input using {@link #getNextChar(int)} and passes it to the key handler.
     * The key handler is set to the result of this.
     * The terminal window is then drawn.
     * @param timeout The timeout in milliseconds.
     * @return Boolean; true if the program should exit.
     * @throws IOException Pass exception up chain.
     */
    public boolean getInput(int timeout) throws IOException
    {
        if(exit)
            return false;
        
        var read = getNextChar(timeout);
        
        if(read == -1 || exit)
            return false;
    
        if(read == -2)
            return true;
        
        checkResize();
        keyHandler = keyHandler.keyPress(read);
    
        if(exit)
            return false;
        
        draw();
        
        return true;
    }
    
    /**
     * Checks user input while waiting for a BlockingThread to complete.
     * @param thread The BlockingThread to run.
     * @return Boolean; rue if the thread completed successfully.
     * @throws IOException Pass exception up chain.
     */
    public boolean await(BlockingThread thread) throws IOException
    {
        draw();
        thread.start();
        while(!thread.done())
        {
            if(!getInput(1))
                return false;
        }
        return true;
    }
    
    private static final int GCINTERVAL = 100;
    private static long lastGC;
    
    /**
     * Checks if the terminal window has been resized by the user.
     * Also performs garbage collection frequently as this game creates a large number of objects.
     * @throws IOException Pass exception up chain.
     */
    private void checkResize() throws IOException
    {
        if(terminal.getWidth() != width || terminal.getHeight() != height)
        {
            width = terminal.getWidth();
            height = terminal.getHeight();
            resizeHandler.resize(width, height);
            draw();
        }
        if(System.currentTimeMillis() > lastGC + GCINTERVAL)
        {
            System.gc();
            lastGC = System.currentTimeMillis();
        }
    }
}
