package logic.piece;

import logic.Game;

/**
 * Represents a chick.<br>
 * {@inheritDoc}
 */
public class Chick extends Piece
{
    private static final Move[] MOVE_DIRECTIONS = new Move[]
            {
                    Move.UP
            };
    private static final boolean PROMOTABLE = true;
    private static final char REPRESENTATION = 'C';
    
    /**
     * Auto id chick constructor.
     * @param player2 Whether chick should belong to player one or two.
     * @param position Position of chick on board.
     */
    public Chick(boolean player2, Game.Coord position)
    {
        super(MOVE_DIRECTIONS, player2, position, PROMOTABLE, REPRESENTATION);
    }
    
    /**
     * Specified id chick constructor.
     * @param player2 Whether chick should belong to player one or two.
     * @param position Position of chick on board.
     * @param id Id to assign the chick.
     */
    public Chick(boolean player2, Game.Coord position, int id)
    {
        super(MOVE_DIRECTIONS, player2, position, PROMOTABLE, REPRESENTATION, id);
    }
    
    /**
     * Bare bones constructor for copying purposes.
     */
    private Chick()
    {
        super(PROMOTABLE, REPRESENTATION);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Chick getCopiedInstance(Game game)
    {
        return new Chick();
    }
    
    /**
     * {@inheritDoc}
     * In this case return a rooster if promoted.
     */
    public Piece moved(Game.Coord newPosition)
    {
        if(isLastRow(newPosition))
            return new Rooster(isPlayer2(), newPosition, getId());
        
        return super.moved(newPosition);
    }
    
    /**
     * {@inheritDoc}
     * In this case return a rooster.
     */
    public Piece promoted()
    {
        return new Rooster(isPlayer2(), getPosition(), getId());
    }
}
