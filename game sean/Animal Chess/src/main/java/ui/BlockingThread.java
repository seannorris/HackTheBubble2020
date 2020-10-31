package ui;

/**
 * Represents a thread for running a blocking operation.
 * Used to allow the UI to continue to update.
 */
public abstract class BlockingThread extends Thread
{
    private volatile boolean complete;
    
    /**
     * Run the blocking operation then set the completed field to true.
     */
    @Override
    public void run()
    {
        operation();
        complete = true;
    }
    
    /**
     * The blocking operation to run.
     */
    protected abstract void operation();
    
    /**
     * Getter for the completed field.
     * @return True if operation is finished.
     */
    public boolean done()
    {
        return complete;
    }
}
