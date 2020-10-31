package logic.piece;

import logic.Game;

/**
 * Represents an elephant.<br>
 * {@inheritDoc}
 */
public class Elephant extends Piece
{
    private static final Move[] MOVE_DIRECTIONS = new Move[]
            {
                    Move.UP_LEFT,
                    Move.UP_RIGHT,
                    Move.DOWN_LEFT,
                    Move.DOWN_RIGHT
            };
    private static final boolean PROMOTABLE = false;
    private static final char REPRESENTATION = 'E';
    
    /**
     * Auto id elephant constructor.
     * @param player2 Whether elephant should belong to player one or two.
     * @param position Position of elephant on board.
     */
    public Elephant(boolean player2, Game.Coord position)
    {
        super(MOVE_DIRECTIONS, player2, position, PROMOTABLE, REPRESENTATION);
    }
    
    /**
     * Specified id elephant constructor.
     * @param player2 Whether elephant should belong to player one or two.
     * @param position Position of elephant on board.
     * @param id Id to assign the elephant.
     */
    public Elephant(boolean player2, Game.Coord position, int id)
    {
        super(MOVE_DIRECTIONS, player2, position, PROMOTABLE, REPRESENTATION, id);
    }
    
    /**
     * Bare bones constructor for copying purposes.
     */
    private Elephant()
    {
        super(PROMOTABLE, REPRESENTATION);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Elephant getCopiedInstance(Game game)
    {
        return new Elephant();
    }
}
