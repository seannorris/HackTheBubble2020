package logic.piece;

import logic.Game;

/**
 * Represents a giraffe.<br>
 * {@inheritDoc}
 */
public class Giraffe extends Piece
{
    private static final Move[] MOVE_DIRECTIONS = new Move[]
            {
                    Move.LEFT,
                    Move.RIGHT,
                    Move.UP,
                    Move.DOWN
            };
    private static final boolean PROMOTABLE = false;
    private static final char REPRESENTATION = 'G';
    
    /**
     * Auto id giraffe constructor.
     * @param player2 Whether giraffe should belong to player one or two.
     * @param position Position of giraffe on board.
     */
    public Giraffe(boolean player2, Game.Coord position)
    {
        super(MOVE_DIRECTIONS, player2, position, PROMOTABLE, REPRESENTATION);
    }
    
    /**
     * Specified id giraffe constructor.
     * @param player2 Whether giraffe should belong to player one or two.
     * @param position Position of giraffe on board.
     * @param id Id to assign the giraffe.
     */
    public Giraffe(boolean player2, Game.Coord position, int id)
    {
        super(MOVE_DIRECTIONS, player2, position, PROMOTABLE, REPRESENTATION, id);
    }
    
    /**
     * Bare bones constructor for copying purposes.
     */
    private Giraffe()
    {
        super(PROMOTABLE, REPRESENTATION);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Giraffe getCopiedInstance(Game game)
    {
        return new Giraffe();
    }
}
