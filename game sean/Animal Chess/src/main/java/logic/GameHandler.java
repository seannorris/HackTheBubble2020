package logic;

/**
 * Represents something that can run a game.
 */
public interface GameHandler
{
    /**
     * Called at the end of a turn.
     */
    void moveEnd();
    
    /**
     * Called to get human input for a move.
     */
    void doHumanMove();
    
    /**
     * Getter for the paused state of the game.
     * @return Boolean; true if the game is paused.
     */
    boolean isPaused();
}
