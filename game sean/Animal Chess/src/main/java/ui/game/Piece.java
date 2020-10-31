package ui.game;

import ui.drawing.DrawableBufferedSection;
import ui.drawing.Section;
import ui.drawing.colour.BackgroundColour;
import ui.drawing.colour.Colour;
import ui.drawing.colour.ColouredChar;
import ui.input.KeyHandler;
import ui.input.selection.DrawableSelectableBufferedSection;

/**
 * Represents a Piece in the game.<br>
 * {@inheritDoc}
 */
public class Piece extends DrawableSelectableBufferedSection<DrawableBufferedSection>
{
    public static final Colour PLAYER1_COLOUR = Colour.RED_BOLD_BRIGHT;
    public static final Colour PLAYER2_COLOUR = Colour.BLUE_BOLD_BRIGHT;
    public static final BackgroundColour ENABLED_COLOUR = BackgroundColour.YELLOW;
    public static final BackgroundColour PLAYER1_FOCUSED_COLOUR = BackgroundColour.CYAN;
    public static final BackgroundColour PLAYER2_FOCUSED_COLOUR = BackgroundColour.MAGENTA;
    
    public static final char DOT = '•';
    public static final int WIDTH = 5;
    public static final int HEIGHT = 3;
    
    private logic.piece.Piece piece;
    private boolean focused;
    private final Game game;
    private boolean flipped;
    
    /**
     * Constructor for Piece.
     * @param enabled If the piece is enabled for selection.
     * @param piece The logic.Piece being represented.
     * @param game The game the piece belongs to.
     */
    public Piece(boolean enabled, logic.piece.Piece piece, Game game)
    {
        super(new DrawableBufferedSection(WIDTH, HEIGHT), enabled);
        this.piece = piece;
        this.game = game;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void unfocus()
    {
        focused = false;
    }
    
    /**
     * {@inheritDoc}
     */
    public KeyHandler focus(KeyHandler current)
    {
        focused = true;
        return super.focus(current);
    }
    
    /**
     * {@inheritDoc}
     * In this case call the game's selectPiece method.
     */
    @Override
    public KeyHandler select(KeyHandler current)
    {
        game.selectPiece(this);
        return current;
    }
    
    /**
     * {@inheritDoc}
     * In this case redraw the piece.
     */
    @Override
    public void update()
    {
        redraw();
        super.update();
    }
    
    /**
     * Renders the piece that is stored in the piece field.
     * If this is null then the space is empty, but can still be selected.
     * If there's a piece add its representation and move directions.
     * Directions where the player can actually move are highlighter in the player's colour.
     */
    private void redraw()
    {
        fill();
        if(piece == null)
            return;
        
        var pieceChar = getBuffer()[HEIGHT / 2][WIDTH / 2];
        pieceChar.setChar(piece.getRepresentation());
        var playerColour = focused && piece.isPlayer2() != game.getGame().isPlayer2Turn() ? Colour.GREEN_BOLD_BRIGHT : getPlayerColour();
        pieceChar.setColour(playerColour);
        for(var move : piece.getMoveDirections())
        {
            var flippedMove = flipped ? move.getReversed() : move;
            var moveChar = getBuffer()[HEIGHT / 2 - flippedMove.getYDelta()][WIDTH / 2 + 2 * flippedMove.getXDelta()];
            moveChar.setChar(DOT);
            moveChar.setColour(piece.getPosition() != null
                    && game.getGame().canMove(piece, logic.Game.Coord.move(piece.getPosition(), move), true)
                    ? playerColour : Colour.DEFAULT);
        }
    }
    
    /**
     * Gets the current colour of the piece.
     * @return The colour.
     */
    private Colour getPlayerColour()
    {
        return piece.isPlayer2() ? PLAYER2_COLOUR : PLAYER1_COLOUR;
    }
    
    /**
     * Fills the piece with a set of coloured chars of either the unenabled, enabled, or focused background colour.
     */
    private void fill()
    {
        var filler_colour = BackgroundColour.DEFAULT;
        if(focused)
            filler_colour = game.getGame().isPlayer2Turn() ? PLAYER2_FOCUSED_COLOUR : PLAYER1_FOCUSED_COLOUR;
        else if(enabled())
            filler_colour = ENABLED_COLOUR;
    
        for(var row : getBuffer())
            for(var x = 0; x < row.length; x++)
                row[x] = new ColouredChar(Section.FILLER_CHAR, filler_colour);
    }
    
    /**
     * Set the logic.Piece of the space.
     * @param piece The piece to set.
     * @param flipped Should the move directions be reversed.
     */
    public void setPiece(logic.piece.Piece piece, boolean flipped)
    {
        this.piece = piece;
        this.flipped = flipped;
        if(piece == null)
            setEnabled(false);
    }
    
    /**
     * Getter for the logic.Piece.
     * @return the piece.
     */
    public logic.piece.Piece getPiece()
    {
        return piece;
    }
}
