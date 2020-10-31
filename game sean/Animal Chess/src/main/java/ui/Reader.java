package ui;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Reads a single character from a buffered reader.<br>
 * {@inheritDoc}
 */
public class Reader extends BlockingThread
{
    private final BufferedReader reader;
    private int read = -2;
    
    /**
     * Constructor for Reader.
     * @param reader the buffered reader to read from.
     */
    public Reader(BufferedReader reader)
    {
        this.reader = reader;
    }
    
    /**
     * {@inheritDoc}
     * In this case a read operation on the buffered reader.
     */
    @Override
    protected void operation()
    {
        try
        {
            this.read = reader.read();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Getter for the read value.
     * @return The read in value, or -2 if read is not finished.
     */
    public synchronized int getRead()
    {
        return read;
    }
}
