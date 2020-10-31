package logic.player;

import logic.Game;
import logic.piece.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 * Stores the player's hand.
 */
public abstract class Player
{
    private final List<Piece> hand;
    private final boolean player2;
    private String name;
    
    /**
     * Constructor for Player.
     * @param player2 Boolean; true if player is player 2.
     * @param hand The player's hand.  List of pieces.
     * @param name The name of the player.
     */
    protected Player(boolean player2, List<Piece> hand, String name)
    {
        this.player2 = player2;
        this.hand = hand;
        this.name = name;
    }
    
    /**
     * Constructor for Player with empty hand.
     * @param player2 Boolean; true if player is player 2.
     * @param name The name of the player.
     */
    protected Player(boolean player2, String name)
    {
        this(player2, new ArrayList<>(), name);
    }
    
    /**
     * Getter for the player's hand.
     * @return the list of pieces in the hand.
     */
    public List<Piece> getHand()
    {
        return hand;
    }
    
    /**
     * Called when it is this player's turn.
     */
    public abstract void doMove();
    
    /**
     * Getter for player2.
     * @return Boolean; true if player is player 2.
     */
    public boolean isPlayer2()
    {
        return player2;
    }
    
    /**
     * Returns a copy of the player using the CopyOfPlayer class.
     * Copies each piece in the hand and adds it to a new list.
     * @param game The game for the copied player to belong to.
     * @return The copied player.
     */
    public Player getCopy(Game game)
    {
        var handCopy = new ArrayList<Piece>(hand.size());
        hand.forEach(x -> handCopy.add(x.getCopy(game)));
        return new CopyOfPlayer(handCopy);
    }
    
    /**
     * Represents a copied player.
     * This is a stub that cannot actually move and is used as a placeholder.<br>
     * {@inheritDoc}
     */
    public class CopyOfPlayer extends Player
    {
        /**
         * Copied player constructor.
         * As this class is not static just use the original player's values.
         * @param hand The hand of the copied player.
         */
        private CopyOfPlayer(List<Piece> hand)
        {
            super(player2, hand, name);
        }
    
        /**
         * {@inheritDoc}
         * In this case throw an error as this piece is not meant to move.
         */
        @Override
        public void doMove()
        {
            throw new RuntimeException("Copied player cannot move.");
        }
    }
    
    /**
     * Called when the game ends.
     */
    public void gameOver()
    {
    
    }
    
    /**
     * Getter for player's name.
     * @return The player's name.
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Setter for the player's name.
     * @param name The new name.
     */
    public void setName(String name)
    {
        this.name = name;
    }
}
