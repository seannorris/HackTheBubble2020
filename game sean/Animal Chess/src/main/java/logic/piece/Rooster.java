package logic.piece;

import logic.Game;

/**
 * Represents a rooster.<br>
 * {@inheritDoc}
 */
public class Rooster extends Piece
{
    private static final Move[] MOVE_DIRECTIONS = new Move[]
            {
                    Move.UP_LEFT,
                    Move.UP,
                    Move.UP_RIGHT,
                    Move.LEFT,
                    Move.RIGHT,
                    Move.DOWN
            };
    private static final boolean PROMOTABLE = false;
    private static final char REPRESENTATION = 'R';
    
    /**
     * Specified id rooster constructor.
     * @param player2 Whether rooster should belong to player one or two.
     * @param position Position of rooster on board.
     * @param id Id to assign the rooster.
     */
    public Rooster(boolean player2, Game.Coord position, int id)
    {
        super(MOVE_DIRECTIONS, player2, position, PROMOTABLE, REPRESENTATION, id);
    }
    
    /**
     * Bare bones constructor for copying purposes.
     */
    private Rooster()
    {
        super(PROMOTABLE, REPRESENTATION);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Rooster getCopiedInstance(Game game)
    {
        return new Rooster();
    }
    
    /**
     * {@inheritDoc}
     * In this case return a chick.
     */
    public Piece captured()
    {
        return new Chick(!isPlayer2(), null, getId());
    }
}
