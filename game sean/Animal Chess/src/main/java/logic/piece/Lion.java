package logic.piece;

import logic.Game;

/**
 * Represents a lion.<br>
 * {@inheritDoc}
 */
public class Lion extends Piece
{
    private static final Move[] MOVE_DIRECTIONS = Move.values();
    private static final boolean PROMOTABLE = true;
    private static final char REPRESENTATION = 'L';
    
    private final Game game;
    
    /**
     * Auto id lion constructor.
     * @param player2 Whether lion should belong to player one or two.
     * @param position Position of lion on board.
     * @param game The game that the lion belongs to.
     */
    public Lion(boolean player2, Game.Coord position, Game game)
    {
        super(MOVE_DIRECTIONS, player2, position, PROMOTABLE, REPRESENTATION);
        this.game = game;
    }
    
    /**
     * Specified id lion constructor.
     * @param player2 Whether lion should belong to player one or two.
     * @param position Position of lion on board.
     * @param game The game that the lion belongs to.
     * @param id Id to assign the lion.
     */
    public Lion(boolean player2, Game.Coord position, Game game, int id)
    {
        super(MOVE_DIRECTIONS, player2, position, PROMOTABLE, REPRESENTATION, id);
        this.game = game;
    }
    
    /**
     * Bare bones constructor for copying purposes.
     * @param game The game that the lion belongs to.
     */
    private Lion(Game game)
    {
        super(PROMOTABLE, REPRESENTATION);
        this.game = game;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Lion getCopiedInstance(Game game)
    {
        return new Lion(game);
    }
    
    /**
     * {@inheritDoc}
     * In this case the other player should win.
     */
    public Piece captured()
    {
        game.win(!isPlayer2());
        return super.captured();
    }
    
    /**
     * {@inheritDoc}
     * In this case if the lion is moved to the last row and cannot be captured then the player it belongs to should win.
     */
    public Piece moved(Game.Coord newPosition)
    {
        if(isLastRow(newPosition))
        {
            var untakeable = true;
            for(var piece : game.getBoard().values())
                if(piece.canCapture(this))
                {
                    untakeable = false;
                    break;
                }
                
            if(untakeable)
                game.win(isPlayer2());
        }
        return super.moved(newPosition);
    }
    
    /**
     * {@inheritDoc}
     * In this case the player that the lion belongs to should win.
     */
    public Piece promoted()
    {
        game.win(isPlayer2());
        return super.promoted();
    }
}
