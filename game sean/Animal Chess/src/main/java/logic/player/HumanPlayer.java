package logic.player;

import logic.GameHandler;

/**
 * Represents a human player.<br>
 * {@inheritDoc}
 */
public class HumanPlayer extends Player
{
    private final GameHandler handler;
    
    /**
     * Constructor for HumanPlayer.
     * @param player2 Boolean; true if player is player 2.
     * @param name The name of the player.
     * @param handler The GameHandler associated with the game.
     */
    public HumanPlayer(boolean player2, String name, GameHandler handler)
    {
        super(player2, name);
        this.handler = handler;
    }
    
    /**
     * {@inheritDoc}
     * In this case calls the doHumanMove method of the GameHandler.
     */
    @Override
    public void doMove()
    {
        handler.doHumanMove();
    }
    
}
